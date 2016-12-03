package com.certoclav.certoscale.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import android_serialport_api.SerialService;

import com.certoclav.certoscale.listener.SensorDataListener;
import com.certoclav.certoscale.service.ReadAndParseSerialService;





/**
 * This class is the Autoclave class model. It is a singleton class. 
 * 
 */
public class Scale extends Observable {


	ArrayList<SensorDataListener> sensorDataListeners = new ArrayList<SensorDataListener>();
	private SerialService serialServiceScale = null;
	private SerialService serialServicePrinter = null;
   
	
	private Float scaleValue = (float) 0;
	private Float tara = (float) 0;

	//valveEnabled == false if valve is locked and is not able to blow steam out
	private boolean valveEnabled = true;
	private ReadAndParseSerialService readAndParseSerialService = null;



	
	
	
	
	private static Scale instance = new Scale();

	private Scale(){
				
	}
	
	public static synchronized Scale getInstance(){
		return instance;
		
	}
	
	

	public void setOnSensorDataListener (SensorDataListener listener){
		this.sensorDataListeners.add(listener);
	}	
	public void removeOnSensorDataListener (SensorDataListener listener){
		this.sensorDataListeners.remove(listener);
	}

	public SerialService getSerialsServiceScale() {
		if(serialServiceScale == null){
			serialServiceScale = new SerialService("/dev/ttymxc3",9600); //COM4
		}
		return serialServiceScale;
		}

	public SerialService getSerialsServicePrinter() {
		if(serialServicePrinter == null){
			serialServicePrinter = new SerialService("/dev/ttymxc1",9600);//COM2
		}
		return serialServicePrinter;
		}
	
	public void setValue(Float value) {
		setScaleValue(value);
		
		
		for(SensorDataListener listener : sensorDataListeners){
			listener.onSensorDataChange(value, "g");
		}
		
	}

	public Float getScaleValue() {
		return scaleValue;
	}

	public void setScaleValue(Float scaleValue) {
		this.scaleValue = scaleValue;
	}

	public Float getTara() {
		return tara;
	}

	public void setTara(Float tara) {
		this.tara = tara;
	}

	public ReadAndParseSerialService getReadAndParseSerialService() {
		if(readAndParseSerialService == null){
			readAndParseSerialService = new ReadAndParseSerialService();
		}
		return readAndParseSerialService ;
	}
	





}

