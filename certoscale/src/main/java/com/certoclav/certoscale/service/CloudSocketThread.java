package com.certoclav.certoscale.service;

import android.util.Log;

import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.certocloud.SocketService;
import com.certoclav.library.certocloud.SocketService.SocketEventListener;

import org.json.JSONObject;

import io.socket.client.Socket;






/**
 * Class that inherits {@link Thread} and manages the communication with the
 * microcontroller in order to read values from temperature and pressure sensors.
 * 
 * @author Iulia Rasinar &lt;iulia.rasinar@nordlogic.com&gt;
 * 
 */
public class CloudSocketThread extends Thread implements SocketEventListener {

	
	private int counterSendLiveDataToServer = 0;
	private JSONObject jsonLiveMessageObj = null;
	private boolean runFlag = true;
	private int index = 0;
	public CloudSocketThread() {
	}
	

	@Override
	public void run() {
	
		try{
			Log.e("SocketTread", "Thread init");
			SocketService.getInstance().setOnSocketEventListener(this);
			SocketService.getInstance().setDeviceKey(Scale.getInstance().getSafetyKey());
			SocketService.getInstance().connectToCertocloud();
			jsonLiveMessageObj = new JSONObject();
			counterSendLiveDataToServer = 0;
		}catch(Exception e){
			Log.e("SocketTread", "Thread init exception: " + e.toString());
		}
		
		while(runFlag){
			//if(! SocketService.getInstance().getSocket().connected()){
			//	Log.e("SocketTread", "trying to connect to socket");
			//	SocketService.getInstance().connectToCertocloud();
			//}
			if(counterSendLiveDataToServer > 0){
				counterSendLiveDataToServer--;
				Log.e("MainActivity", "Counter: " + counterSendLiveDataToServer);
				if(SocketService.getInstance().getSocket().connected()){
						 jsonLiveMessageObj = new JSONObject();
						 Log.e("MainActivity", "Sending data");
					

							
				String libraryName = "";
				try{
					libraryName = ApplicationManager.getInstance().getCurrentLibrary().getName();
				}catch(Exception e){
					libraryName = "";
				}
				

				
				String deviceKey = "";
				try{
					deviceKey = Scale.getInstance().getSafetyKey();
				}catch(Exception e){
					deviceKey = "";
				}
				
				String errorMessage = "";




						 
						 try {
							jsonLiveMessageObj.put("device_key", deviceKey);
							jsonLiveMessageObj.put("index", index);
							
							JSONObject jsonLiveMessageDataObj = new JSONObject();
							

							
							if(Scale.getInstance().getState() != ScaleState.OFF){
								
								jsonLiveMessageDataObj.put("User", Scale.getInstance().getUser().getEmail());
							}else{
								jsonLiveMessageDataObj.put("User", "No user signed in");
							}

							
							if(!errorMessage.equals("")){
								jsonLiveMessageDataObj.put("Warning", errorMessage);
							}
							jsonLiveMessageDataObj.put("STATUS", Scale.getInstance().getState().toString().replace("_", " "));

							 jsonLiveMessageDataObj.put("Weight", ApplicationManager.getInstance().getTaredValueAsStringWithUnit());


							jsonLiveMessageObj.put("data", jsonLiveMessageDataObj);
							Log.e("CloudSocketThread", "sending: " + jsonLiveMessageObj.toString().replace("{", "[").replace("}", "]"));
							
						} catch (Exception e) {
								Log.e("MainActivity", "exception json: " + e.toString());
						}
						
						 SocketService.getInstance().getSocket().emit(SocketService.EVENT_SEND_DATA_FROM_ANDROID_TO_SERVER, jsonLiveMessageObj);
				} 
			}    
			
			 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 
			}
			  
			  
				
				 
				 
	}
	
			
			
	
 
	@Override
	public void onSocketEvent(String eventIdentifier, Object... args) {
		if(eventIdentifier.equals(SocketService.EVENT_START_SEND)){
			
			int tempIndex = 0;
			//counterSendLiveDataToServer = 60;
			Log.e("MainActivity", "SOCKET RECEIVED EVENT TO START");
			String deviceKeyFromJson = "unknown_key";
			try {
				JSONObject obj = (JSONObject)args[0];
				if(obj == null){
					Log.e("MainActivity", "jsonObj == null");
				}
				deviceKeyFromJson = obj.getString("device_key");
				tempIndex = obj.getInt("index");
				

				
			} catch (Exception e) {
				Log.e("MainActivity", "Exception parsing json: " + e.toString());
				e.printStackTrace();
				deviceKeyFromJson = "unknown_key";
			}
			try{
			if(deviceKeyFromJson.equals(Scale.getInstance().getSafetyKey())){
				index = tempIndex;
				Log.e("MainActivity", "SOCKET DEVICE KEY MATCHES");
				counterSendLiveDataToServer = 60;
			}
			}catch(Exception e){
				Log.e("MainActivity", "Error matching savetykeys");
			}
			
			
		}else if(eventIdentifier.equals(Socket.EVENT_CONNECT)){
			Log.e("MainActivity", "SOCKET CONNECTED");
		}

	
	}
 
 
	public void endThread(){
		runFlag = false;
		Log.e("SocketThread","close and destroy thread");
		//wenn runflag false ist, dann l�uft die run() Methode zu ende und der Thread wird zerst�rt.
	}
}