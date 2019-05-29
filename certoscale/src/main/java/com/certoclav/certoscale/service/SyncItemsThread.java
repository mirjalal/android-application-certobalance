package com.certoclav.certoscale.service;

import android.os.Handler;
import android.util.Log;

import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.CloudUser;
import com.certoclav.library.certocloud.GetUtil;
import com.certoclav.library.certocloud.Items;
import com.certoclav.library.certocloud.PostUtil;

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
public class SyncItemsThread extends Thread {

	private Handler mParentHandler;
	List<Item> listItemsCloud = new ArrayList<Item>();
	

	public SyncItemsThread(Handler parentHandler) {
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
					List<Item> listItemsDb = databaseService.getItems();
					
					//upload offline items, which have never been successfully uploaded yet
						for(Item item : listItemsDb){
							if(item.getCloudId().isEmpty()){
								postItemToCertocloud(item); //may updates isUploaded and Cloud_id database entries of localProfiles
							}
						}
					Log.e("SyncItemsThread","uploading items done");



				
					
					//get all items stored in certocloud
					//saves all items in db if not there already
					//saves all items from cloud in listItemsCloud array
					boolean success = getAndParseItemsFromCertocloud();
					if(success == false){
						Log.e("SyncItemsThread", "getAndParseItems failed -> return");
						return;
					}
					Log.e("SyncItemsThread", "getAndParseItemsFromCloud done");

					listItemsDb = databaseService.getItems();

				
					//if an profile from db has been uploaded in past && is not online on certocloud anymore, then delete it from device
					for(Item dbItem : listItemsDb){
						try{
							if(dbItem.getCloudId().isEmpty() == false ){ //profile has been uploaded in past
								boolean dbProfileIsStillOnline = false;
								for(Item cloudItem : listItemsCloud){
									if(cloudItem.getCloudId().equals(dbItem.getCloudId())){
										dbProfileIsStillOnline = true;
										break;
									}
								}
								if(dbProfileIsStillOnline == false){
									Log.e("SyncItemsThread", "Item is not online anymore - delete local version");
									databaseService.deleteItem(dbItem);
								}else{
									Log.e("SyncItemsThread", "Profile is still online - do nothing");
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
				Log.e("SyncItemsTask", "all done -> send message to upload ui");
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
	private boolean getAndParseItemsFromCertocloud() {

		Log.e("SyncItemsThread", "getAndParseItemsFromCertoCloud()");

		ArrayList<String> itemsList = new ArrayList<String>();
		Items items = new Items();
		Integer retval = items.getItemsFromCloud();
		if(retval == GetUtil.RETURN_OK){
			if(items.getItemJsonStringArray() != null){
				DatabaseService db = new DatabaseService(ApplicationController.getContext());
				List<Item> itemsFromDb = db.getItems();
				listItemsCloud= new ArrayList<Item>();

				for(String itemJsonString : items.getItemJsonStringArray()){
					listItemsCloud.add(new Item(itemJsonString));
				}
				for(Item cloudItem : listItemsCloud){
					boolean cloudItemAlreadyInDb = false;
					for(Item dbitem : itemsFromDb){
						if(cloudItem.getCloudId().equals(dbitem.getCloudId())){
							cloudItemAlreadyInDb = true;
							continue;
						}
					}
					if(cloudItemAlreadyInDb == false){
						db.insertItem(cloudItem);
					}
				}

			}
		}else{
			return false;
		}

		return true;
		

		
		
	}
	
		
	

	private void postItemToCertocloud(Item item) {
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
    	Log.e("SyncItemsThread", "post Item to CertoCloud");
    	JSONObject itemJsonObject = new JSONObject(item.getItemJson());
			itemJsonObject.remove("_id");
		JSONObject itemJsonWrapperObject = new JSONObject();
		itemJsonWrapperObject.put("balanceitem",itemJsonObject);
		String body = itemJsonWrapperObject.toString();
    	
    	PostUtil postUtil = new PostUtil();
    	int success = postUtil.postToCertocloud(body, CertocloudConstants.SERVER_URL + CertocloudConstants.REST_API_POST_ITEM, true);
   
    	if(success == PostUtil.RETURN_OK){ 
    		DatabaseService db = new DatabaseService(ApplicationController.getContext());
    		JSONObject jsonResponse = new JSONObject(postUtil.getResponseBody());
    		JSONObject jsonResponseProgram = jsonResponse.getJSONObject("item");
    		String cloudId = jsonResponseProgram.getString("_id");
    		Log.e("SyncItemsThread", "parsed cloud id from response: " + cloudId);
    		db.updateItemCloudId(item,cloudId);
    	}
		}catch(Exception e){
			Log.e("SyncProfileThread", "exception: " + e.toString());
		}

		
		
		
		
		
	}



	public void endThread(){
		//no runFlag exists here because of no loop usage
	}




}