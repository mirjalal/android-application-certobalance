package com.certoclav.certoscale.service;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.library.application.ApplicationController;

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

	private ArrayList<String> getCommandQueue() {
		return commandQueue;
	}

	public void sendCalibrationCommand(){
		//check current model
		String key = "preferences_communication_list_devices";
		String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "");

		switch (modelValue) {
			case "1": // G&G
				getCommandQueue().add("\u001Bq");
				break;
			case "2": // D&T
				getCommandQueue().add("C\r\n");
				break;
		}

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
	private int counter2 = 0;


	private Thread serialThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true) {
				if(AppConstants.IS_IO_SIMULATED == true){
					simulateMessage();
				}else {

					//check current model
					String key = "preferences_communication_list_devices";
					String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "");

					switch (modelValue){
						case "1": // G&G
							try {

								if (commandQueue.size() >0) {
									Scale.getInstance().getSerialsServiceScale().sendMessage(commandQueue.get(0));
									commandQueue.remove(0);
								} else {

									//mPrinter.write(0x1B);
									//mPrinter.write(0x70);
									//byte[] bytes = {27,112};
									//byte[] bytes = hexStringToByteArray2("1B70");
									//Log.e("SerialService", "SEND: " + new String(bytes));
									//Scale.getInstance().getSerialsServiceScale().sendBytes(bytes);
									Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bp");
								}


							} catch (Exception e) {
								e.printStackTrace();
							}

							break;
						case "2": //D&T
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

							break;
					}

				}


				try {
					Thread.sleep(100);
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
		Log.e("ReadAndParse", "received: " + message);
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

			if ((counter%100)==0){

				counter--;
				counter2++;
			}
			if (counter2>25){
				counter++;
				counter2=0;
			}




			rawResponse = "+ 0.0000 g";
			value = (Double) (60 + (60.0* Math.sin(((double)counter)*0.002)));

		    handler.sendEmptyMessage(0);

	}


	public void sendOnOffCommand() {
		String key = "preferences_communication_list_devices";
		String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "");

		switch (modelValue) {
			case "1": // G&G

				break;
			case "2": // D&T
				getCommandQueue().add("O\r\n");
				break;
		}

	}

	public void sendModeCommand() {
		String key = "preferences_communication_list_devices";
		String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "");

		switch (modelValue) {
			case "1": // G&G
				getCommandQueue().add("\u001Bs");
				break;
			case "2": // D&T
				getCommandQueue().add("M\r\n");
				break;
		}



	}

	public static byte[] hexStringToByteArray2(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}

}
