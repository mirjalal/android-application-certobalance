package com.certoclav.certoscale.service;

import android.os.Handler;
import android.util.Log;

import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.CloudUser;
import com.certoclav.library.certocloud.GetUtil;
import com.certoclav.library.certocloud.Items;
import com.certoclav.library.certocloud.PostUtil;
import com.certoclav.library.certocloud.Recipes;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that inherits {@link Thread} and manages the communication with the
 * microcontroller in order to read values from temperature and pressure sensors.
 * 
 * @author Iulia Rasinar &lt;iulia.rasinar@nordlogic.com&gt;
 * 
 */
public class SyncRecipesThread extends Thread {

	private Handler mParentHandler;
	List<Recipe> listRecipesCloud = new ArrayList<Recipe>();


	public SyncRecipesThread(Handler parentHandler) {
		mParentHandler =parentHandler;
	}
	



	/**
	 * Reads the temperature and pressure values from the microcontroller. 
	 */
	@Override
	public void run() {
	

				try {
				
					if(CloudUser.getInstance().isLoggedIn() ==false){
						return;//continue; //sleep for another 60 seconds
					}


					//get all items from db
					DatabaseService databaseService = new DatabaseService(ApplicationController.getContext());
					List<Recipe> listRecipesDb = databaseService.getRecipes();
					
					//upload offline items, which have never been successfully uploaded yet
						for(Recipe recipe : listRecipesDb){
							if(recipe.getCloudId().isEmpty()){
								postRecipeToCertocloud(recipe); //may updates isUploaded and Cloud_id database entries of localProfiles
							}
						}
					Log.e("SyncRecipeThread","uploading items done");



				
					
					//get all items stored in certocloud
					//saves all items in db if not there already
					//saves all items from cloud in listItemsCloud array
					boolean success = getAndParseRecipesFromCertocloud();
					if(success == false){
						Log.e("SyncRecipeThread", "getAndParseRecipes failed -> return");
						return;
					}
					Log.e("SyncRecipeThread", "getAndParseRecipesFromCloud done");

					listRecipesDb = databaseService.getRecipes();

				
					//if an profile from db has been uploaded in past && is not online on certocloud anymore, then delete it from device
					for(Recipe dbRecipe : listRecipesDb){
						try{
							if(dbRecipe.getCloudId().isEmpty() == false ){ //profile has been uploaded in past
								boolean dbProfileIsStillOnline = false;
								for(Recipe cloudRecipe : listRecipesCloud){
									if(cloudRecipe.getCloudId().equals(dbRecipe.getCloudId())){
										dbProfileIsStillOnline = true;
										break;
									}
								}
								if(dbProfileIsStillOnline == false){
									Log.e("SyncRecipeThread", "Recipe is not online anymore - delete local version");
									databaseService.deleteRecipe(dbRecipe);
								}else{
									Log.e("SyncRecipeThread", "Profile is still online - do nothing");
								}
							}
						}catch(Exception e){
							Log.e("SyncProfileTask", "exception: " + e.toString());
							return;//continue;
						}
					}//end for

				}catch(Exception e){
					Log.e("SyncProfileTask", "exception: " + e.toString());
					return;//continue;
				}
				Log.e("SyncRecipeTask", "all done -> send message to update ui");
			sendMessage();
	}

	/**
	 * Sends message on parent thread.
	 */
	private void sendMessage() {

		
		if (mParentHandler != null) {
			mParentHandler.sendEmptyMessage(0);
		}
	}


	/*
	gets all items from certocloud and adds them to the database, if they are not saved until now.
	 */
	private boolean getAndParseRecipesFromCertocloud() {

		Log.e("SyncRecipesThread", "getAndParseRecipesFromCertoCloud()");

		ArrayList<String> recipesList = new ArrayList<String>();
		Recipes recipes = new Recipes();
		Integer retval = recipes.getRecipesFromCloud();
		if(retval == GetUtil.RETURN_OK){
			if(recipes.getRecipeJsonStringArray()!= null){
				DatabaseService db = new DatabaseService(ApplicationController.getContext());
				List<Recipe> recipesFromDb = db.getRecipes();
				listRecipesCloud= new ArrayList<Recipe>();

				for(String recipeJsonString : recipes.getRecipeJsonStringArray()){
					listRecipesCloud.add(new Recipe(recipeJsonString));
				}
				for(Recipe cloudRecipe : listRecipesCloud){
					boolean cloudRecipeAlreadyInDb = false;
					for(Recipe dbrecipe: recipesFromDb){
						if(cloudRecipe.getCloudId().equals(dbrecipe.getCloudId())){
							cloudRecipeAlreadyInDb = true;
							continue;
						}
					}
					if(cloudRecipeAlreadyInDb == false){
						db.insertRecipe(cloudRecipe);
					}
				}

			}
		}else{
			return false;
		}

		return true;
		

		
		
	}
	
		
	

	private void postRecipeToCertocloud(Recipe recipe) {
		//syntax:
		
		/*

		{
			"balanceitem": {
				"devicekey": "93943649346387463",
				"date": "1431208326916",
				"visibility": "private",
				"artno": "Item83883838",
				"name": "Item from device",
				"description": "This is an item description posted from device",
				"weight": 10.7777,
				"cost": 12.1,
				"unit": "g"
		  }

		 */
		
	    //PROGRAM OBJECT
    	
		try{
		//Generate Commands of the program

    	//Generate Profile properties
    	Log.e("SyncRecipeThread", "post recipe to CertoCloud");
    	JSONObject recipeJsonObject = new JSONObject(recipe.getRecipeJson());
			recipeJsonObject.remove("_id");
		JSONObject recipeJsonWrapperObject = new JSONObject();
			recipeJsonWrapperObject.put("balanceitem",recipeJsonObject);
		String body = recipeJsonWrapperObject.toString();
    	
    	PostUtil postUtil = new PostUtil();
    	int success = postUtil.postToCertocloud(body, CertocloudConstants.SERVER_URL + CertocloudConstants.REST_API_POST_ITEM, true);
   
    	if(success == PostUtil.RETURN_OK){ 
    		DatabaseService db = new DatabaseService(ApplicationController.getContext());
    		JSONObject jsonResponse = new JSONObject(postUtil.getResponseBody());
    		JSONObject jsonResponseProgram = jsonResponse.getJSONObject("recipe");
    		String cloudId = jsonResponseProgram.getString("_id");
    		Log.e("SyncRecipeThread", "parsed cloud id from response: " + cloudId);
    		db.updateRecipeloudId(recipe,cloudId);
    	}
		}catch(Exception e){
			Log.e("SyncProfileThread", "exception: " + e.toString());
		}

		
		
		
		
		
	}



	public void endThread(){
		//no runFlag exists here because of no loop usage
	}




}