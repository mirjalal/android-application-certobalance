package com.certoclav.certoscale.model;

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
    void internalCalibration() {

    }

    @Override
    void externelCalibration() {

    }
}
