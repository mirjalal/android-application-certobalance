package com.certoclav.certoscale.model;

import android.content.Context;
import android.util.Log;

import com.certoclav.certoscale.service.ReadAndParseSerialService;

import android_serialport_api.SerialPort;


public class ScaleModelSartoriusCP64 extends ScaleModel {

    public void ScaleModelSartoriusCP64(){
        maximumCapazity=64;
        decimalPlaces=4;
        stabilisationTime = 2;
        comBaudrate = 1200;
        comDataBits = SerialPort.DATABITS_7;
        comParity = SerialPort.PARITY_ODD;
        comStopBits = SerialPort.STOPBITS_1;
        comFlowControl = SerialPort.FLOW_CONTROL_CRTSCTS;
        hasZerobutton = false;

    }



    @Override
    public int initializeParameters(int maximumCapazity, int decimalPlaces, int stabilisationTime, int comBaudrate, int comDataBits, int comParity, int comStopBits,boolean hasZerobutton) {
        return 0;
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

        byte[] byteEsc = new byte[1];
        byteEsc[0] = (byte)0x1B;
        Scale.getInstance().getSerialsServiceScale().sendMessage(new String(byteEsc) + "P\r\n");

        return 0;
    }

    @Override
    public int initializeScale() {
        return 1;
    }

    @Override
    public boolean isCommandResponse() {
        return true;
    }

    @Override
    public int pressTara() {

        byte[] byteEsc = new byte[1];
        byteEsc[0] = (byte)0x1B;
        Scale.getInstance().getSerialsServiceScale().sendMessage(new String(byteEsc) + "T\r\n");
        return 0;
    }

    @Override
    public int pressZero() {
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
    public int  internalCalibration() {

        byte[] byteEsc = new byte[1];
        byteEsc[0] = (byte)0x1B;
        Scale.getInstance().getSerialsServiceScale().sendMessage(new String(byteEsc) + "Z\r\n");

        return 0;
    }

    @Override
    public int externelCalibration(Context context) {

        return 0;
    }
}
