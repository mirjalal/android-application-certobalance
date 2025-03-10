package com.certoclav.certoscale.service;

import android.os.Handler;
import android.os.Message;

import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleModelAEAdam;
import com.certoclav.certoscale.model.ScaleModelGandG;
import com.certoclav.certoscale.supervisor.StateMachine;
import com.certoclav.certoscale.util.Log;

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

	private double lastWeightReceived = 0;

	public ArrayList<String> getCommandQueue() {
		return commandQueue;
	}




	private ArrayList<String> commandQueue = new ArrayList<String>();
	
    Double value = 0d;
	Double valueDiff = 0d;
    String rawResponse = "default";

	private Handler handler = new Handler() {
		 
        public void handleMessage(Message msg) {

			Log.e("ReadAndParseSerialSe", " "+ value);

			valueDiff = lastWeightReceived - value;
        	value = lastWeightReceived - (valueDiff/3.0);

        	Scale.getInstance().setValue(value, rawResponse);
			if(Scale.getInstance().getScaleModel() instanceof ScaleModelAEAdam || Scale.getInstance().getScaleModel() instanceof ScaleModelGandG) {
				Scale.getInstance().setStable(Scale.getInstance().getScaleModel().isStable());
			}
			
	
	
        }
    };
	private int counter = 0;
	private int counter2 = 0;

	/*
	*
	* This thread sends every 100ms the interpolated current weight to the Scale model
	*
	 */
	private Thread interpolationThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {

				try {
					if (AppConstants.IS_IO_SIMULATED == true) {
						simulateMessage();
					} else {
						handler.sendEmptyMessage(0); //upload current weight
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}catch (Exception e){

				}
			}
		}
	});



	private Thread communicationThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true) {

					try {

							if (commandQueue.size() >0) {
								Scale.getInstance().getSerialsServiceScale().sendMessage(commandQueue.get(0));
								commandQueue.remove(0);
							} else {
								if(Scale.getInstance().getScaleModel().isCommandResponse() == true) {
									if(Scale.getInstance().getScaleModel().isPeriodicMessagingEnabled()) {
										Log.e("ReadAndpParceSerialS", "sendPrint");
										Scale.getInstance().getScaleModel().sendPrintCommand(); //send print command every 300ms
									}
								}
							}


					} catch (Exception e) {
						e.printStackTrace();
					}



				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	});




	private ReadAndParseSerialService() {
		Log.e("ReadAndParseSerialServ", "constructor");

		//Scale.getInstance().getSerialsServiceScale().startReadSerialThread();

		if(!communicationThread.isAlive()){
			communicationThread.start();


			if(AppConstants.IS_IO_SIMULATED == false) {
				Log.e("ReadAndParseService", "setOnMessageReceivedListener");
				Scale.getInstance().getSerialsServiceScale().setOnMessageReceivedListener(this);
				Scale.getInstance().getSerialsServiceScale().startReadSerialThread();
			}
		}
		if(!interpolationThread.isAlive()){
			interpolationThread.start();
		}
	}


	@Override
	public void onMessageReceived(String message) {
		Log.e("ReadAndParse", "onMessageReceived(): " + message);

		try {
			StateMachine.getInstance().notifyMessageRecieved();

			if (message.length() > 5) {
				rawResponse = message;
				lastWeightReceived = Scale.getInstance().getScaleModel().parseRecievedMessage(message);
				if (lastWeightReceived == 0) {
					lastWeightReceived = +0;
				}

			}
		}catch (Exception e){

		}

	}

	private void simulateMessage(){
		 	counter++;

			if ((counter%200)==0){

				counter--;
				counter2++;
				Scale.getInstance().getScaleModel().setStable(true);
			}
			if (counter2>85){
				counter++;
				counter2=0;
			//	Scale.getInstance().getScaleModel().setStable(false);
			}




			rawResponse = "+ 0.0000 g";


			lastWeightReceived = (Double) (Scale.getInstance().getScaleModel().getMaximumCapazityInGram()*0.5+ (Scale.getInstance().getScaleModel().getMaximumCapazityInGram()*0.5* Math.sin(((double)counter)*0.002)));

		    handler.sendEmptyMessage(0);

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
