package com.certoclav.library.certocloud;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipes {



	public static final String JSON_PROTPERTY_ID = "_id";

	public static final int ID_IF_SUCCESSFUL = 1;
	public static final int ID_IF_MAINTENANCE = 3;
	public static final int ID_IF_ERROR = 2;

	public ArrayList<String> getRecipeJsonStringArray() {
		return recipeJsonStringArray;	}

	public void setRecipeJsonStringArray(ArrayList<String> recipeJsonStringArray) {
		this.recipeJsonStringArray = recipeJsonStringArray;
	}

	private ArrayList<String> recipeJsonStringArray = new ArrayList<String>();

	public Recipes() {
			
	}	
	

	public int getRecipesFromCloud(){
		
		Integer success = 0;
		try {
			
			if(CloudUser.getInstance().isLoggedIn() ==false){
				return GetUtil.RETURN_ERROR;
			}
			
			
			GetUtil getUtil = new GetUtil();
			success = getUtil.getFromCertocloud(CertocloudConstants.SERVER_URL + CertocloudConstants.REST_API_GET_RECIPES + CloudUser.getInstance().getCurrentDeviceKey());
			
			if(success == GetUtil.RETURN_OK){
				

				JSONObject jsonObjectResult = new JSONObject(getUtil.getResponseBody());
				CloudUser.getInstance().setPremiumAccount(jsonObjectResult.getBoolean("ispremium"));

				
				JSONArray jsonRecipeArray = jsonObjectResult.getJSONArray("items");
						
		       //only overwrite content of already existing conditions
				for(int i = 0; i< jsonRecipeArray.length();i++){
					recipeJsonStringArray.add(jsonRecipeArray.getJSONObject(i).toString());
				}
			}

		}catch (Exception e) {
		return GetUtil.RETURN_ERROR;
	}
		return success;
		
	}

	public int addRecipeToCloud(){
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