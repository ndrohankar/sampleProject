package com.chatbot.main;



import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.chatbot.business.ApiAi;
import com.chatbot.business.Bussiness;
import com.chatbot.model.JavaModel;
import com.chatbot.model.Parameters;
import com.chatbot.model.Response_Mdl;
import com.chatbot.model.Result;

@Path("gdpchatbox")
public class RequestResponse {


	@GET
	public Response GetMsg() throws IOException{
		return Response.status(200).entity("Welcome User ").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response odRequest(String outputJSON) throws IOException, InterruptedException
	{
		String email_id="";
		System.out.println("dsdw");
		String countryCode="";
		String year="";
		String result="";
		JavaModel JM_Response=null;
		ApiAi apiresponse=null;
		Result rs=null;
		Parameters params=null;
		

		try{
			System.out.println("Request recieved");
			apiresponse = new ApiAi();
			System.out.println("responceBO : "+apiresponse.toString());
		}catch(Exception e){
			e.printStackTrace();
		}

		
		try{
			JM_Response = apiresponse.jsonToJava(outputJSON);
			System.out.println("apiAiResponse : " +JM_Response);
		}catch(Exception e){
			e.printStackTrace();
		}

	//	try{
			rs=JM_Response.getResult();
			System.out.println("rs :"+rs.toString());

		//}catch(Exception e){e.printStackTrace();
		//}
	//
			params=rs.getParameters();
			email_id=params.getEmail();
			countryCode=params.getCountry();
			year=params.getYear();
			
			Bussiness b1= new Bussiness();
			result=b1.getData(email_id,countryCode,year);
			System.out.println(result);
			
			Response_Mdl res=new Response_Mdl();
			res.setSource("policyWS");
			res.setSpeech(result);
			ObjectMapper om=new ObjectMapper();
			String str2=om.writeValueAsString(res);
	return Response.status(200).entity(str2).header("Content-Type", "application/json").build();
	}

}

