package com.certoclav.certoscale.model;

import com.certoclav.certoscale.service.ReadAndParseSerialService;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelGandG extends ScaleModel {


    public void ScaleModelGandG(){
        maximumCapazity=5000;
        decimalPlaces=1;
    }


    @Override
    public int initializeParameters() {
        maximumCapazity=5000;
        decimalPlaces=1;
        return 1;
    }

    @Override
    public int sendOnOffCommand() {
        return 0;
    }

    @Override
    public int sendModeCommand() {
        ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bs");
        return 0;
    }

    @Override
    public int sendPrintCommand() {
        Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bp");
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
        return 0;
    }

    @Override
    void internalCalibration() {

    }

    @Override
    void externelCalibration() {

    }
}
