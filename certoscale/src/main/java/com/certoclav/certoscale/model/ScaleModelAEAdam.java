package com.certoclav.certoscale.model;

import android.content.Context;
import android.util.Log;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelAEAdam extends ScaleModel{

    public void ScaleModelAEAdam(){
        maximumCapazity=600;
        decimalPlaces=2;
        stabilisationTime=2;
    }
    /*
    public int initializeParameters() {
        maximumCapazity=600;
        decimalPlaces=2;
        comBaudrate=4800;
        stable = false;
        comDataBits=8;
        comStopBits=1;
        Scale.getInstance().getSerialsServiceScale().setStringTerminatin("\r\n\r\n\r\n");

        hasZerobutton=true;

        return 0;
    }*/


    @Override
    public int initializeParameters(int maximumCapazity, int decimalPlaces, int stabilisationTime, int comBaudrate, int comDataBits, int comParity, int comStopBits,boolean hasZerobutton) {
        this.maximumCapazity=maximumCapazity;
        this.decimalPlaces=decimalPlaces;
        this.stabilisationTime=stabilisationTime;

        this.comBaudrate=comBaudrate;
        this.comDataBits=comDataBits;
        this.comParity=comParity;
        this.comStopBits=comStopBits;

        this.hasZerobutton=hasZerobutton;


        return 0;
    }

    @Override
    public int sendOnOffCommand() {
        return 0;
    }

    @Override
    public int sendModeCommand() {

        return 0;
    }

    @Override
    public int sendPrintCommand() {

        Scale.getInstance().getSerialsServiceScale().sendMessage("P\r\n");
        return 0;
    }

    @Override
    public int initializeScale() {
        return 0;
    }

    @Override
    public boolean isCommandResponse() {
        return false;
    }

    @Override
    public int pressTara() {
        Scale.getInstance().getSerialsServiceScale().sendMessage("T\r\n");
        return 0;
    }

    @Override
    public int pressZero() {

       Scale.getInstance().getSerialsServiceScale().sendMessage("Z\r\n");
        return 0;
    }

    @Override
    public double parseRecievedMessage(String message) {
        int sign=1;
        double value=0;
        if(message.length()>5) {
             String[] arguments = message.split(" ");
            if (arguments.length != 0) {
                for (String arg : arguments) {

                    if (arg.contains("-")){
                        sign=-1;
                    }
                    if(arg.contains("US")){
                        stable = false;
                    }
                    if(arg.contains("ST")){
                        stable = true;
                    }


                    if (arg.length() > 2 && arg.contains(".") && arg.matches("[0-9.]*") ) {
                        try {
                            value = Double.parseDouble(arg);
                        } catch (Exception e) {
                            value = 0d;
                            Log.e("ReadAndParseSerialServ", "Error parsing following Double: " + arg);
                        }
                    }
                }
                value=value*sign;
            }else{
                value = 0d;
            }

            }
        return value;
    }


    @Override
    public int internalCalibration() {

        return 0;
    }

    @Override
    public int externelCalibration(Context context) {

        return 0;
    }
}
