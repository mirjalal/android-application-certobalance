package com.certoclav.certoscale.model;

import android.util.Log;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelAEAdam extends ScaleModel{

    public void ScaleModelAEAdam(){
        maximumCapazity=600;
        decimalPlaces=2;
    }
    public int initializeParameters() {
        maximumCapazity=600;
        decimalPlaces=2;
        comBaudrate=4800;
        comDataBits=8;
        comStopBits=1;

        hasZerobutton=true;

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
            Log.e("ReadAndParse", "received: " + message);
            String[] arguments = message.split(" ");
            if (arguments.length != 0) {
                for (String arg : arguments) {

                    if (arg.equals("-")){
                        sign=-1;
                    }


                    if (arg.length() > 2 && arg.contains(".")) {
                        try {
                            value = Double.parseDouble(arg);
                        } catch (Exception e) {
                            value = 0d;
                            Log.e("ReadAndParseSerialServ", "Error parsing Double");
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
    void internalCalibration() {

    }

    @Override
    void externelCalibration() {

    }
}
