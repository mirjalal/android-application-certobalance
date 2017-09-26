package com.certoclav.certoscale.model;

import android.content.Context;
import android.util.Log;

import android_serialport_api.SerialPort;


public class ScaleModelSartoriusCP64 extends ScaleModel {


    public ScaleModelSartoriusCP64(){
        super(2,//stabilisationTime,
               64,// maximumCapazity,
               4,// decimalPlaces,
               false,// stable,
               1200,// comBaudrate,
               SerialPort.DATABITS_7,// comDataBits,
               SerialPort.STOPBITS_1,// comStopBits,
               SerialPort.PARITY_ODD,// comParity,
               false,// isPeriodicMessagingEnabled,
               SerialPort.FLOW_CONTROL_NONE,// comFlowControl,
               false);// hasZerobutton);
    }



    @Override
    public int sendOnOffCommand() {
     //   ReadAndParseSerialService.getInstance().getCommandQueue().add("O\r\n");
        return 0;
    }

    @Override
    public int sendModeCommand() {
     //   ReadAndParseSerialService.getInstance().getCommandQueue().add("M\r\n");
        return 0;
    }

    @Override
    public int sendPrintCommand() {
        Log.e("ScaleModelSartorius", "SendPrint() called, but is deactiviated");
      //  byte[] byteEsc = new byte[1];
      //  byteEsc[0] = (byte)0x1B;
      //  Scale.getInstance().getSerialsServiceScale().sendMessage(new String(byteEsc) + "P\r\n");

        return 0;
    }

    @Override
    public int initializeScale() {
        return 1;
    }

    @Override
    public boolean isCommandResponse() {
        return false;
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
        stable = false;
        if(message.length()>5) {
            String[] arguments = message.split(" ");
            if (arguments.length != 0) {
                for (String arg : arguments) {

                    if (arg.contains("-")){
                        sign=-1;
                    }



                    if(arg.contains("g")){
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
