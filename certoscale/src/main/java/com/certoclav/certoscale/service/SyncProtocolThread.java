package com.certoclav.certoscale.service;

import android.os.Handler;
import android.util.Log;

import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.CloudUser;
import com.certoclav.library.certocloud.GetUtil;
import com.certoclav.library.certocloud.PostUtil;
import com.certoclav.library.certocloud.Protocols;
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
public class SyncProtocolThread extends Thread {

	private Handler mParentHandler;
	List<Protocol> listProtocolsCloud = new ArrayList<Protocol>();


	public SyncProtocolThread(Handler parentHandler) {
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
					List<Protocol> listProtocolsDb = databaseService.getProtocols();
					
					//upload offline items, which have never been successfully uploaded yet
						for(Protocol protocol : listProtocolsDb){
							if(protocol.getCloudId().isEmpty()){
								postProtocolToCertocloud(protocol); //may updates isUploaded and Cloud_id database entries of localProfiles
							}
						}
					Log.e("SyncProtocolThread","uploading protocols done");



				
					
					//get all items stored in certocloud
					//saves all items in db if not there already
					//saves all items from cloud in listItemsCloud array
					boolean success = getAndParseProtocolsFromCertocloud();
					if(success == false){
						Log.e("SyncProtocolThread", "getAndParseProtocols failed -> return");
						return;
					}
					Log.e("SyncRecipeThread", "getAndParseRecipesFromCloud done");

					listProtocolsDb = databaseService.getProtocols();

				
					//if an profile from db has been uploaded in past && is not online on certocloud anymore, then delete it from device
					for(Protocol dbProtocol : listProtocolsDb){
						try{
							if(dbProtocol.getCloudId().isEmpty() == false ){ //profile has been uploaded in past
								boolean dbProfileIsStillOnline = false;
								for(Protocol cloudProtocol : listProtocolsCloud){
									if(cloudProtocol.getCloudId().equals(dbProtocol.getCloudId())){
										dbProfileIsStillOnline = true;
										break;
									}
								}
								if(dbProfileIsStillOnline == false){
									Log.e("SyncProtocolThread", "Protocol is not online anymore - delete local version");
									databaseService.deleteProtocol(dbProtocol);
								}else{
									Log.e("SyncProtocolThread", "Profile is still online - do nothing");
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
				Log.e("SyncProtocolTask", "all done -> send message to update ui");
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
	private boolean getAndParseProtocolsFromCertocloud() {

		Log.e("SyncProtocolsThread", "getAndParseProtocolsFromCertoCloud()");

		ArrayList<String> protocolList = new ArrayList<String>();
		Protocols protocols = new Protocols();
		Integer retval = protocols.getProtocolsFromCloud();
		if(retval == GetUtil.RETURN_OK){
			if(protocols.getProtocolJsonStringArray()!= null){
				DatabaseService db = new DatabaseService(ApplicationController.getContext());
				List<Protocol> protocolsFromDb = db.getProtocols();
				listProtocolsCloud= new ArrayList<Protocol>();

				for(String protcolJsonString : protocols.getProtocolJsonStringArray()){
					listProtocolsCloud.add(new Protocol(protcolJsonString));
				}
				for(Protocol cloudProtocol : listProtocolsCloud){
					boolean cloudProtocolAlreadyInDb = false;
					for(Protocol dbprotocol: protocolsFromDb){
						if(cloudProtocol.getCloudId().equals(dbprotocol.getCloudId())){
							cloudProtocolAlreadyInDb = true;
							continue;
						}
					}
					if(cloudProtocolAlreadyInDb == false){
						db.insertProtocol(cloudProtocol);
					}
				}

			}
		}else{
			return false;
		}

		return true;
		

		
		
	}
	
		
	

	private void postProtocolToCertocloud(Protocol protocol) {
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
    	Log.e("SyncProtocolThread", "post protocol to CertoCloud");
    	JSONObject protocolJsonObject = new JSONObject(protocol.getProtocolJson());
			protocolJsonObject.remove("_id");
		JSONObject protocolJsonWrapperObject = new JSONObject();
			protocolJsonWrapperObject.put("balanceprotocol",protocolJsonObject);
		String body = protocolJsonWrapperObject.toString();
    	
    	PostUtil postUtil = new PostUtil();
    	int success = postUtil.postToCertocloud(body, CertocloudConstants.SERVER_URL + CertocloudConstants.REST_API_POST_PROTOCOLS, true);
   
    	if(success == PostUtil.RETURN_OK){ 
    		DatabaseService db = new DatabaseService(ApplicationController.getContext());
    		JSONObject jsonResponse = new JSONObject(postUtil.getResponseBody());
    		JSONObject jsonResponseProgram = jsonResponse.getJSONObject("protocol");
    		String cloudId = jsonResponseProgram.getString("_id");
    		Log.e("SyncProtocolThread", "parsed cloud id from response: " + cloudId);
    		db.updateProtocolCloudId(protocol,cloudId);
    	}
		}catch(Exception e){
			Log.e("SyncProfileThread", "exception: " + e.toString());
		}

		
		
		
		
		
	}



	public void endThread(){
		//no runFlag exists here because of no loop usage
	}




}