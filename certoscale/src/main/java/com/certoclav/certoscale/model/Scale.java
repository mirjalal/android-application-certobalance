package com.certoclav.certoscale.model;

import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.SensorDataListener;
import com.certoclav.certoscale.listener.ValueTransformedListener;
import com.certoclav.certoscale.service.ReadAndParseSerialService;

import java.util.ArrayList;
import java.util.Observable;

import android_serialport_api.SerialService;


/**
 * This class is the Autoclave class model. It is a singleton class. 
 * 
 */
public class Scale extends Observable {


	ArrayList<SensorDataListener> sensorDataListeners = new ArrayList<SensorDataListener>();
	ArrayList<ScaleApplicationListener> applicationListeners = new ArrayList<ScaleApplicationListener>();
	ArrayList<ValueTransformedListener> valueTransformedListeners = new ArrayList<ValueTransformedListener>();

	private SerialService serialServiceScale = null;
	private SerialService serialServicePrinter = null;

	public String getUnitRaw() {
		return unitRaw;
	}

	public void setUnitRaw(String unitRaw) {
		this.unitRaw = unitRaw;
	}

	public String getUnitTransformed() {
		return unitTransformed;
	}

	public void setUnitTransformed(String unitTransformed) {
		this.unitTransformed = unitTransformed;
	}

	private String unitRaw = "g";
	private String unitTransformed = "g";


	private ScaleApplication scaleApplication = ScaleApplication.WEIGHING;
	private ScaleState state = ScaleState.READY; //default init state is READY_AND_WAITING_FOR_LOGIN

	public Float getWeightMultiplier() {
		return weightMultiplier;
	}

	public void setWeightMultiplier(Float weightMultiplier) {
		this.weightMultiplier = weightMultiplier;
	}

	private Float weightMultiplier = (float)1.0 ;
	private Float scaleValueRaw = (float) 0;

	public Float getScaleValueTransformed() {
		return scaleValueTransformed;
	}


	public String getTaraAsStringWithUnit() {
		switch (getScaleApplication()){
			case PART_COUNTING:
				return String.format("%d", Math.round(getTara())) + " "+ Scale.getInstance().getUnitTransformed();
			default:
				return String.format("%.4f",getTara()) + " "+ Scale.getInstance().getUnitTransformed();
		}
	}


	public String getScaleValueTransformedAsStringWithUnit() {

		switch (getScaleApplication()){
			case PART_COUNTING:
				return String.format("%d", Math.round(Scale.getInstance().getScaleValueTransformed())) + " "+ Scale.getInstance().getUnitTransformed();
			default:
				return String.format("%.4f",Scale.getInstance().getScaleValueTransformed()) + " "+ Scale.getInstance().getUnitTransformed();
		}

	}

	public void setScaleValueTransformed(Float scaleValueTransformed) {
		this.scaleValueTransformed = scaleValueTransformed;
		for(ValueTransformedListener listener: valueTransformedListeners){
			listener.onValueTransformedChanged(scaleValueTransformed);
		}
	}

	private Float scaleValueTransformed = (float) 0;
	private Float tara = (float) 0;

	//valveEnabled == false if valve is locked and is not able to blow steam out
	private boolean valveEnabled = true;
	private ReadAndParseSerialService readAndParseSerialService = null;


	private boolean microcontrollerReachable = false;

	
	private static Scale instance = new Scale();

	private Scale(){
				
	}
	
	public static synchronized Scale getInstance(){
		return instance;
		
	}

	public ScaleState getState() {
		return state;
	}

	public void setState(ScaleState state) {
		this.state = state;
	}

	public boolean isMicrocontrollerReachable() {
		return microcontrollerReachable;
	}

	public void setMicrocontrollerReachable(boolean microcontrollerReachable) {
		this.microcontrollerReachable = microcontrollerReachable;
	}

	public void setOnValueTransformedListener (ValueTransformedListener listener){
		this.valueTransformedListeners.add(listener);
	}
	public void removeOnValueTransformedListener (ValueTransformedListener listener){
		this.valueTransformedListeners.remove(listener);
	}
	public void setOnSensorDataListener (SensorDataListener listener){
		this.sensorDataListeners.add(listener);
	}	
	public void removeOnSensorDataListener (SensorDataListener listener){
		this.sensorDataListeners.remove(listener);
	}
	public void setOnApplicationListener (ScaleApplicationListener listener){
		this.applicationListeners.add(listener);
	}
	public void removeOnApplicationListener (ScaleApplicationListener listener){
		this.applicationListeners.remove(listener);
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
		setScaleValueRaw(value);
		
		
		for(SensorDataListener listener : sensorDataListeners){
			listener.onSensorDataChange(value, "g");
		}
		
	}

	public Float getScaleValueRaw() {
		return scaleValueRaw;
	}

	public void setScaleValueRaw(Float scaleValueRaw) {
		this.scaleValueRaw = scaleValueRaw;
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





	public ScaleApplication getScaleApplication() {
		return scaleApplication;
	}

	public void setScaleApplication(ScaleApplication scaleApplication) {
		this.scaleApplication = scaleApplication;
		for(ScaleApplicationListener listener : applicationListeners){
			listener.onApplicationChange(scaleApplication);
		}
	}


}

