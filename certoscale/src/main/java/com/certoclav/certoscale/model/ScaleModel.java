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
	public double getStabilisationTime() {return stabilisationTime;}
	public void setStabilisationTime(double stabilisationTime) {this.stabilisationTime = stabilisationTime;}

	double maximumCapazity;
	public double getMaximumCapazityInGram() {return maximumCapazity;}
	public void setMaximumCapazity(double maximumCapazity) {this.maximumCapazity = maximumCapazity;}

	int decimalPlaces;
	public int getDecimalPlaces() {return decimalPlaces;}
	public void setDecimalPlaces(int decimalPlaces) {this.decimalPlaces = decimalPlaces;}


	boolean sendsPeriodical;

	public boolean isStable() {
		return stable;
	}

	public void setStable(boolean stable) {
		this.stable = stable;
	}

	boolean stable;
	int comBaudrate;
	public int getComBaudrate() {return comBaudrate;}
	public void setComBaudrate(int comBaudrate) {this.comBaudrate = comBaudrate;}

	int comDataBits;
	public int getComDataBits() {return comDataBits;}
	public void setComDataBits(int comDataBits) {this.comDataBits = comDataBits;}

	int comStopBits;
	public int getComStopBits() {return comStopBits;}
	public void setComStopBits(int comStopBits) {this.comStopBits = comStopBits;}

	int comParity;
	public int getComParity() {return comParity;}
	public void setComParity(int comParity) {this.comParity = comParity;}

	int comFlowControl;
	public int getComFlowControl() {return comFlowControl;}
	public void setComFlowControl(int flowControl) {this.comFlowControl = flowControl;}

	String endLineCommand;



	boolean hasZerobutton;
	public boolean isPeriodicMessagingEnabled() {
		return isPeriodicMessagingEnabled;
	}

	public void setPeriodicMessagingEnabled(boolean periodicMessagingEnabled) {
		isPeriodicMessagingEnabled = periodicMessagingEnabled;
	}

	private boolean isPeriodicMessagingEnabled = false;


	abstract public int initializeParameters(int maximumCapazity, int decimalPlaces, int stabilisationTime,  int comBaudrate, int comDataBits,int comParity,int comStopBits,boolean hasZerobutton);

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


