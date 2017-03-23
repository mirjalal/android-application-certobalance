package com.certoclav.certoscale.model;

/**
 * Created by Enrico on 22.03.2017.
 */

public abstract class ScaleModel {


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

	String endLineCommand;



	boolean hasZerobutton;


	abstract public int initializeParameters();

	abstract public int sendOnOffCommand();
	abstract public int sendModeCommand();
	abstract public int sendPrintCommand();


	abstract public int pressTara();
	abstract public int pressZero();


    //Set mod to weighing, unit to gramm...
    abstract public int initializeScale();



    abstract void internalCalibration();
    abstract void externelCalibration();


}


