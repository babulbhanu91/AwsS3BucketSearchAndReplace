package com.main;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestRestApi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path ="https://reqres.in/api/users";
		String requestType = "POST";
		String accept = "application/json";
		String testcase = "{\"name\":\"babul\",\"job\":\"software engineer\"}";
		TestRestApi obj = new TestRestApi();
		HttpURLConnection conn = obj.formConnectionObject(path,requestType,accept);
		if(conn!=null){
		JSONObject response = obj.test(conn,testcase);
		if(response!=null && !response.containsKey("status")){
			System.out.println("status:SUCCESS");
			System.out.println(response);
		}
		else if(response.containsKey("status")){
			System.out.println(response.get("status"));
		}
		}
	}
	public HttpURLConnection formConnectionObject(String path, String requestType, String accept){
		HttpURLConnection conn= null;
		try{
		URL url = new URL(path);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(requestType);
		conn.setRequestProperty("Accept", accept);
		conn.addRequestProperty("User-Agent", "Mozilla/4.0");
		if(requestType=="POST")
			conn.setDoOutput(true);
		else
			conn.setDoOutput(false);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	public  JSONObject test(HttpURLConnection conn, String testcase){
		JSONObject resonseObject = null;
		try{
			
		if(conn.getDoOutput()){
			OutputStream os = conn.getOutputStream();
			if(testcase!=null)
			os.write(testcase.getBytes());
			os.flush();
		}

		if (conn.getResponseCode() != 200  && conn.getResponseCode() != 201) {
			resonseObject= new JSONObject();
			resonseObject.put("status", conn.getResponseCode()+" "+conn.getResponseMessage());
			return resonseObject;
		}
		else{            
            JSONParser jsonParser = new JSONParser();
            resonseObject = (JSONObject)jsonParser.parse(
                  new InputStreamReader(conn.getInputStream(), "UTF-8"));
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resonseObject;

	}

}