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
        return 0;
    }

    @Override
    public int initializeScale() {
        return 0;
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
