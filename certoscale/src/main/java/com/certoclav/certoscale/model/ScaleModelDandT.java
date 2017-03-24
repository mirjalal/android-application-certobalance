package com.certoclav.certoscale.model;

import android.content.Context;
import android.util.Log;

import com.certoclav.certoscale.service.ReadAndParseSerialService;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelDandT extends ScaleModel {

    public void ScaleModelDandT(){
        maximumCapazity=120;
        decimalPlaces=4;
    }


    @Override
    public int initializeParameters() {
        maximumCapazity=120;
        decimalPlaces=4;

        hasZerobutton=true;

        return 1;
    }

    @Override
    public int sendOnOffCommand() {
        ReadAndParseSerialService.getInstance().getCommandQueue().add("O\r\n");
        return 0;
    }

    @Override
    public int sendModeCommand() {
        ReadAndParseSerialService.getInstance().getCommandQueue().add("M\r\n");
        return 0;
    }

    @Override
    public int sendPrintCommand() {
        Scale.getInstance().getSerialsServiceScale().sendMessage("P\r\n");

        return 0;
    }

    @Override
    public int initializeScale() {
        return 1;
    }

    @Override
    public int pressTara() {


        return 0;
    }

    @Override
    public int pressZero() {
        Scale.getInstance().getSerialsServiceScale().sendMessage("T\r\n");
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
    public int externelCalibration(Context context) {

        return 0;
    }
}
