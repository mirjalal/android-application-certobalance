package com.certoclav.library.certocloud;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Protocols {



	public static final String JSON_PROTPERTY_ID = "_id";

	public static final int ID_IF_SUCCESSFUL = 1;
	public static final int ID_IF_MAINTENANCE = 3;
	public static final int ID_IF_ERROR = 2;

	public ArrayList<String> getProtocolJsonStringArray() {
		return protocolJsonStringArray;
	}

	public void setprotocolJsonStringArray(ArrayList<String> protocolJsonStringArray) {
		this.protocolJsonStringArray = protocolJsonStringArray;
	}

	private ArrayList<String> protocolJsonStringArray = new ArrayList<String>();

	public Protocols() {
			
	}	
	

	public int getProtocolsFromCloud(){
		
		Integer success = 0;
		try {
			
			if(CloudUser.getInstance().isLoggedIn() ==false){
				return GetUtil.RETURN_ERROR;
			}
			
			
			GetUtil getUtil = new GetUtil();
			success = getUtil.getFromCertocloud(CertocloudConstants.SERVER_URL + CertocloudConstants.REST_API_GET_PROTOCOLS + CloudUser.getInstance().getCurrentDeviceKey());
			
			if(success == GetUtil.RETURN_OK){
				

				JSONObject jsonObjectResult = new JSONObject(getUtil.getResponseBody());
				CloudUser.getInstance().setPremiumAccount(jsonObjectResult.getBoolean("ispremium"));

				
				JSONArray jsonProtocolArray = jsonObjectResult.getJSONArray("protocols");
						
		       //only overwrite content of already existing conditions
				for(int i = 0; i< jsonProtocolArray.length();i++){
					protocolJsonStringArray.add(jsonProtocolArray.getJSONObject(i).toString());
				}
			}

		}catch (Exception e) {
		return GetUtil.RETURN_ERROR;
	}
		return success;
		
	}

	public int addItemToCloud(){
		Integer success = 0;

		try{

			if(CloudUser.getInstance().isLoggedIn() ==false){
				return GetUtil.RETURN_ERROR;
			}

			GetUtil getUtil = new GetUtil();



		}catch (Exception e) {
			return GetUtil.RETURN_ERROR;
		}

		return success;
	}
	
	

}