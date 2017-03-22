package com.certoclav.certoscale.model;

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
    public int initializeScale() {
        return 1;
    }

    @Override
    void pressTara() {

    }

    @Override
    void pressZero() {

    }

    @Override
    void internalCalibration() {

    }

    @Override
    void externelCalibration() {

    }
}
