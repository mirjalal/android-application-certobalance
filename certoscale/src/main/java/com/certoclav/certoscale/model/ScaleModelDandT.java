package com.certoclav.certoscale.model;

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
