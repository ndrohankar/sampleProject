package com.chatbot.business;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.util.*;  

import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  
  

public class Bussiness 
{

	public  String getData(String contry_id, String year, String email) throws IOException
	{    
		//Year validation
				int userYear=Integer.valueOf(year);
				Date currentDate= new Date();
			    int currentYear=1900+currentDate.getYear();
				 System.out.println(currentYear);
				 //Email_id validation
				String result="";
				 String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
			      Boolean b = email.matches(EMAIL_REGEX);
			      System.out.println("E-mail: "+email+" , Valid = " + b);
			      if(!b)
			      {
			    	  //System.out.println("Sorry..!!!please enter valid email id. ");
			    	   result="Sorry..!!!please enter valid email id. ";
			    	  return result;
			      }
			      else if(userYear >= currentYear)
			      {
			    	result="Sorry..!!"+year+" year is not accepted.Please enter previous Year";  
			    	return result;
			      }
			      
			

				File file=new File("C:\\Users\\monika\\Desktop\\monika office\\projects\\Book1.xls");
				FileInputStream fin = new FileInputStream(file);
				
				HSSFWorkbook wb = new HSSFWorkbook(fin);
				HSSFSheet ws = wb.getSheetAt(0);
				HSSFCell cell=null;	
				HSSFRow rowHeader = ws.getRow(0);

				int rowNum = ws.getLastRowNum() + 1;
				int colNum = ws.getRow(0).getLastCellNum();
				int countryCodeIndexHeader=0;
				int yearIndex=-1;
				double data=0.0;
				boolean matchFound = false;
			      
				// System.out.println("Total Number of Columns in the excel is : "+colNum);
				 //System.out.println("Total Number of Rows in the excel is : "+rowNum);
				for (int j = 1; j < colNum; j++)
				{
					cell = rowHeader.getCell(j);
					String cellValue = cellToString(cell);
					//System.out.println(cellValue);
					if (year.equalsIgnoreCase(cellValue)) 
						{
						yearIndex=j;	
						break;
						}	
				}
				
			       for (int i = 1; i < rowNum; i++)
			        {
			           rowHeader = ws.getRow(i);
			           String coutrycodefromExcel  = cellToString(rowHeader.getCell(countryCodeIndexHeader));
			    
			         	if(contry_id.equals(coutrycodefromExcel) )
			         	{
				         	    data= Double.valueOf(cellToString(rowHeader.getCell(yearIndex)));
				         	    String data1=Double.valueOf(data).toString();
				         	    System.out.println("GDP Growth of "+coutrycodefromExcel+" is "+data+"%.");
				         	   
				         	    matchFound = true;
				         	   result=sendMail(email,data1,coutrycodefromExcel) ;
				         	   return result;
			              }
			        }
			       System.out.println("data:"+data);
		           if (!matchFound) {
			            System.out.println("Sorry...!! You Entered wrong Country code");
			        }
			       return "data:"+data;
			}
			
			
			public static String sendMail(String email_id,String data,String country)
			{
				  final String from="dhanorkar.monika@gmail.com";//change accordingly  
				  final String password="8007970174";//change accordingly  
				   
				//1)Get properties object   
				  Properties props = new Properties();    
				  props.put("mail.smtp.host", "smtp.gmail.com");    
				  props.put("mail.smtp.socketFactory.port", "465");    
				  props.put("mail.smtp.socketFactory.class",    
				            "javax.net.ssl.SSLSocketFactory");    
				  props.put("mail.smtp.auth", "true");    
				  props.put("mail.smtp.port", "587");    
				 //2)get Session   
				  Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator()
				  {  protected PasswordAuthentication getPasswordAuthentication()
				      { return new PasswordAuthentication(from,password); } } );
				  
				     
				  //2) compose message     
				  try{  
				    MimeMessage message = new MimeMessage(session);  
				    message.setFrom(new InternetAddress(from));  
				    message.addRecipient(Message.RecipientType.TO,new InternetAddress(email_id));  
				    message.setSubject("Data Regarding GDP ChatBot"); 
				  
				      
				    //3) create MimeBodyPart object and set your message text     
				    BodyPart messageBodyPart1 = new MimeBodyPart();
				    messageBodyPart1.setText("GDP Growth of "+country+" is "+data+"%.");  
				   // messageBodyPart1.setText("This is message body");  
				      
				    //4) create new MimeBodyPart object and set DataHandler object to this object      
				    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
				  
				    String file = "GDPGrowthData.txt";//change accordingly  
				    DataSource source = new FileDataSource(file);  
				    messageBodyPart2.setDataHandler(new DataHandler(source));  
				    messageBodyPart2.setFileName(file);  
				     
				     
				    //5) create Multipart object and add MimeBodyPart objects to this object      
				    Multipart multipart = new MimeMultipart();  
				    multipart.addBodyPart(messageBodyPart1);  
				    multipart.addBodyPart(messageBodyPart2);  
				  
				    //6) set the multiplart object to the message object  
				    message.setContent(multipart );  
				     
				    //7) send message  
				    Transport.send(message);  
				   
				  // System.out.println("message sent...."); 
				   return "Mail send successfully....";
					
				   }catch (MessagingException ex) {ex.printStackTrace();}  
				
				return "";
			}
			
			
			private static String cellToString(HSSFCell cell) 
			 {	
					Object result = null;
					switch (cell.getCellType()) 
					{
					case HSSFCell.CELL_TYPE_NUMERIC:
						result = Integer.valueOf( (int) cell.getNumericCellValue()).toString();
						break;
				
					case HSSFCell.CELL_TYPE_STRING:
						result = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BLANK:
						result = "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
						result = cell.getCellFormula();
					}
					return result.toString();
			}	
			
	
	
}
