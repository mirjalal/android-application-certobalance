package com.certoclav.certoscale.model;

import android.content.Context;
import android.preference.PreferenceManager;

import com.certoclav.certoscale.R;
import com.certoclav.library.application.ApplicationController;

/**
 * Created by Enrico on 22.03.2017.
 */

public abstract class ScaleModel {

	double stabilisationTime;
	double maximumCapazity;
	int decimalPlaces;
	boolean stable;

    public int getComBaudrate() {
        return comBaudrate;
    }

    int comBaudrate;
	int comDataBits;
	int comStopBits;
	int comParity;
	private boolean isPeriodicMessagingEnabled = false;
	int comFlowControl;

	public double getMaximumCapazity() {
		return maximumCapazity;
	}

	public void setComBaudrate(int comBaudrate) {
		this.comBaudrate = comBaudrate;
	}

	public boolean isHasZerobutton() {
		return hasZerobutton;
	}

	public void setHasZerobutton(boolean hasZerobutton) {
		this.hasZerobutton = hasZerobutton;
	}

	boolean hasZerobutton;

public ScaleModel(	double stabilisationTime,
					  double maximumCapazity,
					  int decimalPlaces,
					  boolean stable,
					  int comBaudrate,
					  int comDataBits,
					  int comStopBits,
					  int comParity,
					  boolean isPeriodicMessagingEnabled,
					  int comFlowControl,
					  boolean hasZerobutton){

	setValues(stabilisationTime,
	maximumCapazity,
	decimalPlaces,
	stable,
	comBaudrate,
	comDataBits,
	comStopBits,
	comParity,
	isPeriodicMessagingEnabled,
	comFlowControl,
	hasZerobutton
			);
}

private void setValues(double stabilisationTime,
					   double maximumCapazity,
					   int decimalPlaces,
					   boolean stable,
					   int comBaudrate,
					   int comDataBits,
					   int comStopBits,
					   int comParity,
					   boolean isPeriodicMessagingEnabled,
					   int comFlowControl,
					   boolean hasZerobutton){
	setStabilisationTime(stabilisationTime);
	setMaximumCapazity(maximumCapazity);
	setDecimalPlaces(decimalPlaces);
	setStable(stable);
	setComBaudrate(comBaudrate);
	setComDataBits(comDataBits);
	setComStopBits(comStopBits);
	setComParity(comParity);
	setPeriodicMessagingEnabled(isPeriodicMessagingEnabled);
	setComFlowControl(comFlowControl);
	setHasZerobutton(hasZerobutton);
}






	public double getStabilisationTime() {return stabilisationTime;}
	public void setStabilisationTime(double stabilisationTime) {this.stabilisationTime = stabilisationTime;}


	public double getMaximumCapazityInGram() {return maximumCapazity;}
	public void setMaximumCapazity(double maximumCapazity) {this.maximumCapazity = maximumCapazity;}


	public int getDecimalPlaces() {return decimalPlaces;}
	public void setDecimalPlaces(int decimalPlaces) {this.decimalPlaces = decimalPlaces;}




	public boolean isStable() {
		return stable;
	}

	public void setStable(boolean stable) {
		this.stable = stable;
	}


	public int getComDataBits() {return comDataBits;}
	public void setComDataBits(int comDataBits) {this.comDataBits = comDataBits;}


	public int getComStopBits() {return comStopBits;}
	public void setComStopBits(int comStopBits) {this.comStopBits = comStopBits;}


	public int getComParity() {return comParity;}
	public void setComParity(int comParity) {this.comParity = comParity;}


	public int getComFlowControl() {return comFlowControl;}
	public void setComFlowControl(int flowControl) {this.comFlowControl = flowControl;}



	public boolean isPeriodicMessagingEnabled() {
		return isPeriodicMessagingEnabled;
	}

	public void setPeriodicMessagingEnabled(boolean periodicMessagingEnabled) {
		isPeriodicMessagingEnabled = periodicMessagingEnabled;
	}



	abstract public int sendOnOffCommand();
	abstract public int sendModeCommand();
	abstract public int sendPrintCommand();



	abstract public int pressTara();
	abstract public int pressZero();

	abstract public double parseRecievedMessage(String message);
	abstract public int externelCalibration(Context context);

	abstract public int internalCalibration();

    //Set mod to weighing, unit to gramm...
    abstract public int initializeScale();


	abstract public boolean isCommandResponse();

	public String getScaleModelName() {
		String modelName = "";
		try {
			String key = "preferences_communication_list_devices";
			String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
			modelName = ApplicationController.getContext().getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(modelValue) - 1];

		}catch (Exception e){
			modelName = "";
		}
		return modelName;

	}
}


