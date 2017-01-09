package com.certoclav.certoscale.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Scale;

import java.util.ArrayList;

import android_serialport_api.MessageReceivedListener;

/**
 * Behaviour of D&T electronics balance:
 * UART-Protocol:
 *
 * 1)
 * Command "O\r\n" to turn the balance ON or OFF
 * Response: no response
 *
 * 2)
 * Command "P\r\n" to turn receive the current weight including unit
 * Response: "+  0.0000  g\r\n\n" 			if mode is gram
 * Response: "+   <0><0><0><0>  g\r\n\n"	if balance is OFF
 * Response: "+0.000007 oz\r\n\n"			if mode is oz
 * Response: "+  0.0000 ct\r\n\n"			if mode is ct
 * Response: "+0.000000 lb\r\n\n"			if mode is lb
 * Response: "      0 pcs\r\n\n"			if mode is pcs
 * Response: "    0.00 %\r\n\n"				if mode is %
 * Response: "+<0><0><0><0><0><0><0>  g"	if scale is calibrating currently
 * 3)
 * Command "C\r\n" to push the CAL button
 * Response: no response, Print command not working
 *
 * 4)
 * Command "T\r\n" Tare command. DO NOT USE
 *
 */
public class ReadAndParseSerialService implements MessageReceivedListener {

	private static ReadAndParseSerialService instance = new ReadAndParseSerialService();

	public static ReadAndParseSerialService getInstance(){
		return instance;
	}

	public ArrayList<String> getCommandQueue() {
		return commandQueue;
	}

	public void setCommandQueue(ArrayList<String> commandQueue) {
		this.commandQueue = commandQueue;
	}

	private ArrayList<String> commandQueue = new ArrayList<String>();
	
    Double value = 0d;
    String rawResponse = "default";

	private Handler handler = new Handler() {
		 
        public void handleMessage(Message msg) {
             
        	
        	Scale.getInstance().setValue(value, rawResponse);
			
	
	
        }
    };
	private int counter = 0;


	private Thread serialThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true) {
				if(AppConstants.IS_IO_SIMULATED == true){
					simulateMessage();
				}else {


					try {

							if (commandQueue.size() >0) {
								Scale.getInstance().getSerialsServiceScale().sendMessage(commandQueue.get(0));
								commandQueue.remove(0);
							} else {
								Scale.getInstance().getSerialsServiceScale().sendMessage("P\r\n");
							}


					} catch (Exception e) {
						e.printStackTrace();
					}
				}


				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});



	private ReadAndParseSerialService() {
		Log.e("ReadAndParseSerialServ", "constructor");
		if(!serialThread.isAlive()){
			serialThread.start();
			if(AppConstants.IS_IO_SIMULATED == false) {
				Scale.getInstance().getSerialsServiceScale().setOnMessageReceivedListener(this);
				Scale.getInstance().getSerialsServiceScale().startReadSerialThread();
			}
		}
	}
	
	public void startParseSerialThread(){

		if(!serialThread.isAlive()){
			serialThread.start();
			Scale.getInstance().getSerialsServiceScale().setOnMessageReceivedListener(this);
			Scale.getInstance().getSerialsServiceScale().startReadSerialThread();
		}

		
	}

	@Override
	public void onMessageReceived(String message) {

		if(message.length()>5) {
			rawResponse = message;
			Log.e("ReadAndParse", "received: " + message);
			String[] arguments = message.split(" ");
			if (arguments.length != 0) {
				for (String arg : arguments) {
					if (arg.length() > 2 && arg.contains(".")) {
						try {
							value = Double.parseDouble(arg);
						} catch (Exception e) {
							value = 0d;
							Log.e("ReadAndParseSerialServ", "Error parsing Double");
						}
					}
				}
			}else{
				value = 0d;
			}
			handler.sendEmptyMessage(0);
		}
	}
	
	private void simulateMessage(){
		 	counter++;


			rawResponse = "+ 0.0000 g";
			value = (Double) (60 + (60.0* Math.sin(((double)counter)*0.02)));

		    handler.sendEmptyMessage(0);

	}


}
