package com.certoclav.certoscale.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.certoclav.certoscale.model.Scale;

import android_serialport_api.MessageReceivedListener;


public class ReadAndParseSerialSicsService extends Service implements MessageReceivedListener {


	@Override
	public void onCreate(){
		Scale.getInstance().getSerialsServiceSics().setOnMessageReceivedListener(this);
		Log.e("Service","onCreate");
	}





	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onMessageReceived(String message) {


		String reply = SicsProtocol.getInstance().processCommand(message);
		Scale.getInstance().getSerialsServiceSics().sendMessage(reply);

	}
	



}
