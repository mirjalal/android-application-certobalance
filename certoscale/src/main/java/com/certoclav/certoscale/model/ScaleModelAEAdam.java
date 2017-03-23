package com.certoclav.certoscale.model;

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
    void internalCalibration() {

    }

    @Override
    void externelCalibration() {

    }
}
