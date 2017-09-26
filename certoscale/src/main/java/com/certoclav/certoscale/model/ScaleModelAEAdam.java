package com.certoclav.certoscale.model;

import android.content.Context;
import android.util.Log;

import android_serialport_api.SerialPort;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelAEAdam extends ScaleModel{


    public ScaleModelAEAdam(){
        super(2,//stabilisationTime,
                600,// maximumCapazity,
                2,// decimalPlaces,
                false,// stable,
                9600,// comBaudrate,
                SerialPort.DATABITS_8,// comDataBits,
                SerialPort.STOPBITS_1,// comStopBits,
                SerialPort.PARITY_NONE,// comParity,
                false,// isPeriodicMessagingEnabled,
                SerialPort.FLOW_CONTROL_NONE,// comFlowControl,
                true);// hasZerobutton);
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
