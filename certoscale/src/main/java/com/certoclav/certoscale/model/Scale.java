package com.certoclav.certoscale.model;

import android.util.Log;

import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.ScaleStateListener;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.listener.WifiListener;
import com.certoclav.certoscale.service.ReadAndParseSerialService;

import java.util.ArrayList;
import java.util.Observable;

import android_serialport_api.SerialService;


/**
 * This class is the Autoclave class model. It is a singleton class. 
 * 
 */
public class Scale extends Observable {


	ArrayList<WeightListener> weightListeners = new ArrayList<WeightListener>();
	ArrayList<ScaleApplicationListener> applicationListeners = new ArrayList<ScaleApplicationListener>();
	ArrayList<WifiListener> wifiListeners = new ArrayList<WifiListener>();
	ArrayList<ScaleStateListener> scaleStateListeners = new ArrayList<ScaleStateListener>();
	ArrayList<StableListener> stableListeners = new ArrayList<StableListener>();

	private SerialService serialServiceScale = null;
	private SerialService serialServiceLabelPrinter = null;
	private SerialService serialServiceProtocolPrinter = null;
	private SerialService serialServiceSics = null;

	public boolean isStable() {
		return stable;
	}

	public void setStable(boolean stable) {
		boolean stableOld = this.stable;
		this.stable = stable;
		if(stableOld != stable) {
			for (StableListener listener : stableListeners) {
				listener.onStableChanged(stable);
			}
		}


	}

	private boolean stable = false;




	public String getRawResponseFromBalance() {
		return rawResponseFromBalance;
	}

	public void setRawResponseFromBalance(String rawResponseFromBalance) {
		this.rawResponseFromBalance = rawResponseFromBalance;
	}

	private String rawResponseFromBalance = "";



	private ScaleApplication scaleApplication = ScaleApplication.WEIGHING;
	private ScaleState state = ScaleState.OFF; //default init state is READY_AND_WAITING_FOR_LOGIN

	private Double weightInGram = 0d; //raw value reiceived from Serial port of the balance


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private User user = null;







	//valveEnabled == false if valve is locked and is not able to blow steam out
	private boolean valveEnabled = true;
	private ReadAndParseSerialService readAndParseSerialService = null;


	private boolean microcontrollerReachable = false;

	private Scale(){

	}
	private static Scale instance = new Scale();

	public static synchronized Scale getInstance(){
		return instance;

	}

	public boolean isWifiConnected() {
		return wifiConnected;
	}
	public void setOnWifiListener(WifiListener listener){
		this.wifiListeners.add(listener);
	}
	public void removeOnWifiListener(WifiListener listener){
		this.wifiListeners.remove(listener);
	}
	public void setWifiConnected(boolean wifiConnected) {
		this.wifiConnected = wifiConnected;
		for (WifiListener listener : wifiListeners){
			listener.onWifiConnectionChange(wifiConnected);
		}
	}

	private boolean wifiConnected = false;

	


	public ScaleState getState() {
		return state;
	}

	public void setScaleState(ScaleState state) {
		this.state = state;
		Log.e("Scale", "STATE: " + state.toString());
		for(ScaleStateListener listener : scaleStateListeners){
			listener.onScaleStateChange(state);
		}
	}

	public boolean isMicrocontrollerReachable() {
		return microcontrollerReachable;
	}

	public void setMicrocontrollerReachable(boolean microcontrollerReachable) {
		this.microcontrollerReachable = microcontrollerReachable;
	}

	public void setOnWeightListener(WeightListener listener){
		this.weightListeners.add(listener);
	}	
	public void removeOnWeightListener(WeightListener listener){
		this.weightListeners.remove(listener);
	}
	public void setOnApplicationListener (ScaleApplicationListener listener){
		this.applicationListeners.add(listener);
	}
	public void removeOnApplicationListener (ScaleApplicationListener listener){
		this.applicationListeners.remove(listener);
	}

	public void setOnStableListener (StableListener listener){
		this.stableListeners.add(listener);
	}

	public void removeOnStableListener (StableListener listener){
		this.stableListeners.remove(listener);
	}



	public void setOnScaleStateListener (ScaleStateListener listener){
		this.scaleStateListeners.add(listener);
	}
	public void removeOnScaleStateListener (ScaleStateListener listener){
		this.scaleStateListeners.remove(listener);
	}

	public SerialService getSerialsServiceScale() {
		if(serialServiceScale == null){
			serialServiceScale = new SerialService("/dev/ttymxc3",9600); //COM4
		}
		return serialServiceScale;
		}

	public SerialService getSerialsServiceLabelPrinter() {
		if(serialServiceLabelPrinter == null){
			serialServiceLabelPrinter = new SerialService("/dev/ttymxc1",9600);//COM2
		}
		return serialServiceLabelPrinter;
		}

	public SerialService getSerialsServiceProtocolPrinter() {
		if(serialServiceProtocolPrinter == null){
			serialServiceProtocolPrinter = new SerialService("/dev/ttymxc2",9600);//COM1
		}
		return serialServiceProtocolPrinter;
	}

	public SerialService getSerialsServiceSics() {
		if(serialServiceProtocolPrinter == null){
			serialServiceProtocolPrinter = new SerialService("/dev/ttymxc0",9600);//COM1
		}
		return serialServiceProtocolPrinter;
	}


	public void setValue(Double value, String rawresponse) {
		setWeightInGram(value);
		this.rawResponseFromBalance = rawresponse;
		
		for(WeightListener listener : weightListeners){
			listener.onWeightChanged(value, rawresponse);
		}
		
	}

	public Double getWeightInGram() {
		return weightInGram;
	}

	public void setWeightInGram(Double weightInGram) {
		this.weightInGram = weightInGram;
	}









	public ScaleApplication getScaleApplication() {
		return scaleApplication;
	}

	public void setScaleApplication(ScaleApplication scaleApplication) {
		Log.e("Scale","SCALEAPPLICATION STATE: " + scaleApplication.toString());
		this.scaleApplication = scaleApplication;
		for(ScaleApplicationListener listener : applicationListeners){
			listener.onApplicationChange(scaleApplication);
		}
	}


	public String getSafetyKey() {
		return "93943649346387463";
	} //savetykey of certobalance W01.0001

	public String getFirmwareVersion() {
		return "1.0";
	}

	public String getSerialnumber() {
		return "Serialnumber";
	}


}

