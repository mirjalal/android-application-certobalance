package com.certoclav.library.certocloud;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

public class PostUtil {

	private String responseMessage = "";
	private String responseBody = "";
	private int responseCode = -1;
	
	public static final int RETURN_OK = 0;
	public static final int RETURN_ERROR_TIMEOUT = 1;
	public static final int RETURN_ERROR_UNKNOWN_HOST = 2;
	public static final int RETURN_ERROR_UNAUTHORISED_PASSWORD = 401; //equals returned responsecode
	public static final int RETURN_ERROR_UNAUTHORISED_MAIL = 404;//equals returned responsecode
	public static final int RETURN_ERROR_ACCOUNT_NOT_ACTIVATED = 403;//equals returned responsecode
	public static final int RETURN_ERROR = 4;
	public static final int RETURN_UNKNOWN = 5;
	




	/*
	 * Posts the given JSON body to CertoCloud
	 * returns status flags see constants RETURN_XXX
	 * 
	 * 
	 */
	public int postToCertocloud(String body, String urlpath, boolean auth){
	     Log.e("PostUtil", "send to Server: " + body);

	     int returnval = RETURN_UNKNOWN;
	     	if(auth == true){
	     		if(CloudUser.getInstance().getToken().isEmpty()){
	     			return -1;
	     			//return error because there must be a valid token available for auth messages
	     		}
	     	}
	        try{
	     
					URL url = new URL(urlpath);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(2000); //timeout if wifi is slow
					conn.setReadTimeout(7000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					if(auth == true){
						conn.setRequestProperty("X-Access-Token", CloudUser.getInstance().getToken());
						conn.setRequestProperty("X-Key", CloudUser.getInstance().getEmail());
					}
					conn.setRequestProperty("Content-Type", "application/json");
					OutputStream os = conn.getOutputStream(); //if host not available this function throws unknownhostexteption
					os.write( body.getBytes("UTF-8") );
					os.close();

	
					
					// read the response
					responseCode = -1;
					try{
						responseCode = conn.getResponseCode();
						returnval = responseCode;
						if(responseCode == HttpsURLConnection.HTTP_OK){
							returnval = RETURN_OK;
						}
					}catch(IOException e){
						returnval = RETURN_ERROR_UNAUTHORISED_PASSWORD;
						//this workaround is neccessary, because server doesn't reply with WWW-Authenticate in header in case of Responsecode 401
						Log.e("PostUtil", "error" + e.toString());	
						
					}
					Log.e("PostUtil", "Response code" + responseCode);	
					
					responseMessage = conn.getResponseMessage();
					Log.e("PostUtil", "Response Message: " + responseMessage);
					
				
					
					InputStream in = new BufferedInputStream(conn.getInputStream());
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder result = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null) {
					    result.append(line);
					}
					responseBody = result.toString();
					
	
					Log.e("PostUtil", "Response body" + responseBody);
 
	        }catch(Exception e){
	        	if(e instanceof UnknownHostException){
	        		returnval = RETURN_ERROR_UNKNOWN_HOST;
	        	}else if(e instanceof SocketTimeoutException){
	        		returnval = RETURN_ERROR_TIMEOUT;
	        	}else{
	        		
	        	}
	        	Log.e("ProstUtil", "Exception " + e.toString());
	        }
			return returnval;

	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public String getResponseBody() {
		return responseBody;
	}
	public int getResponseCode(){
		return responseCode;
	}
}
