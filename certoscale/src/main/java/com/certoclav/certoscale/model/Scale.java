package com.certoclav.certoscale.model;

import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.SensorDataListener;
import com.certoclav.certoscale.listener.WeightMeasuredListener;
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
	ArrayList<WeightMeasuredListener> weightMesuredListeners = new ArrayList<WeightMeasuredListener>();

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
	private Float averagePieceWeight = (float)1.0 ; //divider for part counting application
	private Float weightRaw = (float) 0; //raw value reiceived from Serial port of the balance
	private Float weightMeasured = (float) 0; //measured weight
	private Float weightTara = (float) 0; //tara

	public Float getAveragePieceWeight() {
		return averagePieceWeight;
	}

	public void setAveragePieceWeight(Float averagePieceWeight) {
		this.averagePieceWeight = averagePieceWeight;
	}
	
	public Float getWeightMeasured() {
		return weightMeasured;
	}


	public String getTaraAsStringWithUnit() {
		switch (getScaleApplication()){
			case PART_COUNTING:
				return String.format("%d", Math.round(getWeightTara())) + " "+ Scale.getInstance().getUnitTransformed();
			default:
				return String.format("%.4f", getWeightTara()) + " "+ Scale.getInstance().getUnitTransformed();
		}
	}


	public String getTotalWeightAsStringWithUnit() {

		switch (getScaleApplication()){
			case PART_COUNTING:
				return String.format("%d", Math.round(getWeightMeasured())) + " "+ Scale.getInstance().getUnitTransformed();
			default:
				return String.format("%.4f", getWeightMeasured()) + " "+ Scale.getInstance().getUnitTransformed();
		}

	}

	public String getRelativeWeightAsStringWithUnit() {

		switch (getScaleApplication()){
			case PART_COUNTING:
				return String.format("%d", Math.round( getWeightMeasured() - getWeightTara() )) + " "+ Scale.getInstance().getUnitTransformed();
			default:
				return String.format("%.4f", getWeightMeasured() - getWeightTara()) + " "+ Scale.getInstance().getUnitTransformed();
		}
		
	}
	
	public void setWeightMeasured(Float weightMeasured) {
		this.weightMeasured = weightMeasured;
		for(WeightMeasuredListener listener: weightMesuredListeners){
			listener.onWeightMeasuredChanged(weightMeasured);
		}
	}



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

	public void setOnWeightMeasuredListener(WeightMeasuredListener listener){
		this.weightMesuredListeners.add(listener);
	}
	public void removeOnWeightMeasuredListener(WeightMeasuredListener listener){
		this.weightMesuredListeners.remove(listener);
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
		setWeightRaw(value);
		
		
		for(SensorDataListener listener : sensorDataListeners){
			listener.onSensorDataChange(value, "g");
		}
		
	}

	public Float getWeightRaw() {
		return weightRaw;
	}

	public void setWeightRaw(Float weightRaw) {
		this.weightRaw = weightRaw;
	}

	public Float getWeightTara() {
		return weightTara;
	}

	public void setWeightTara(Float weightTara) {
		this.weightTara = weightTara;
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

