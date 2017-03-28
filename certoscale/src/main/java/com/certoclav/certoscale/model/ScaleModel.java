package com.certoclav.certoscale.model;

import android.content.Context;

/**
 * Created by Enrico on 22.03.2017.
 */

public abstract class ScaleModel {

	double stabilisationTime;
	public double getStabilisationTime() {return stabilisationTime;}
	public void setStabilisationTime(double stabilisationTime) {this.stabilisationTime = stabilisationTime;}



	double maximumCapazity;
	public double getMaximumCapazity() {return maximumCapazity;}
	public void setMaximumCapazity(double maximumCapazity) {this.maximumCapazity = maximumCapazity;}

	int decimalPlaces;
	public int getDecimalPlaces() {return decimalPlaces;}
	public void setDecimalPlaces(int decimalPlaces) {this.decimalPlaces = decimalPlaces;}


	boolean sendsPeriodical;

	int comBaudrate;
	int comDataBits;
	int comStopBits;
	int comParity;

	String endLineCommand;



	boolean hasZerobutton;


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


}


