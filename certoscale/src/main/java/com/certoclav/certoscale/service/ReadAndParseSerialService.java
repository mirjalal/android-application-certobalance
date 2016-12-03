package com.certoclav.certoscale.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android_serialport_api.MessageReceivedListener;
import android_serialport_api.SerialService;

import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Scale;



public class ReadAndParseSerialService implements MessageReceivedListener {
	
	


	
    Float value = (float) 0;
    String unit = "";

	private Handler handler = new Handler() {
		 
        public void handleMessage(Message msg) {
             
        	
        	Scale.getInstance().setValue(value);
			
	
	
        }
    };
	private int counter = 0;

	private boolean isSendEnabled  = true;
	private boolean isSendCalibrationCommand = false;


	
	public boolean isSendEnabled() {
		return isSendEnabled;
	}

	public void setSendEnabled(boolean isSendEnabled) {
		this.isSendEnabled = isSendEnabled;
	}

	public ReadAndParseSerialService() {
		Log.e("ReadAndParseSerialService", "constructor");
	}
	
	public void startParseSerialThread(){
		if(AppConstants.isIoSimulated == false){
			Scale.getInstance().getSerialsServiceScale().setOnMessageReceivedListener(this);
			Scale.getInstance().getSerialsServiceScale().startReadSerialThread();
			
			Thread thread = new Thread() {
			    

				@Override
			    public void run() {
			    	while(true) {
			    		try { 
				    		if(isSendEnabled){
				    			if(isSendCalibrationCommand == true){
				    				Scale.getInstance().getSerialsServiceScale().sendMessage("C\r\n");
				    				isSendCalibrationCommand = false;
				    			}else{
				    				Scale.getInstance().getSerialsServiceScale().sendMessage("P\r\n");
				    			}
				    		}
				                
			            } catch (Exception e) {
			            	e.printStackTrace();
			            }
			    	
			    		
			    		
			    		try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}
			    }
			};

			thread.start();
			
		}else{
			

		}
		
	}

	@Override
	public void onMessageReceived(String message) {
		
		Log.e("ReadAndParse", "received: " + message);
		String[] arguments = message.split(" ");
		if(arguments.length!= 0){
		for(String arg : arguments){
			if(arg.length()>2 && arg.contains(".")){
				try{
					value = Float.parseFloat(arg);
				}catch(Exception e){
					Log.e("ReadAndParseSerialService", "Error parsing float");
				}
				handler.sendEmptyMessage(0);
			}
		}
		    
		    

		 

		}
		
	}
	
	private void simulateMessage(){
		

 
	    
			value = (float) (60 + (30.0* Math.sin(((double)counter)*0.02)));
			
		    handler.sendEmptyMessage(0);
		    
	}

	public void sendCalibrationCommand() {
		isSendCalibrationCommand = true;
		
	}
}
