package com.certoclav.certoscale.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.certoclav.certoscale.model.Scale;

public class SyncRecipesService extends Service{

	SyncRecipesThread syncRecipesThread = null;
	

	public Handler mGuiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Scale.getInstance().notifyDatabaseChanged();

		};
	};
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;//zum Server kann man sich nicht verbinden
	}
	
	@Override
	public void onCreate(){
		syncRecipesThread = new SyncRecipesThread(mGuiHandler);
		Log.e("Service","onCreate");
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("PostProtocolService", "onstart");
		if(!syncRecipesThread.isAlive()){
			syncRecipesThread = new SyncRecipesThread(mGuiHandler);
			syncRecipesThread.start();
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy(){
		Log.e("Service","OnDestroy");
		syncRecipesThread.endThread();
	}

}
