package com.certoclav.certoscale.model;

import android.preference.PreferenceManager;
import android.util.Log;

import com.certoclav.library.application.ApplicationController;

/**
 * Created by Michael on 8/17/2017.
 */

public class ScaleModelManager {

    public void changeScaleModelAndRefreshComport() {
        Scale.getInstance().setScaleModel(getScaleModelAccordingToPreferences());

        Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());
        Scale.getInstance().getSerialsServiceScale().setmParity(Scale.getInstance().getScaleModel().getComParity());
        Scale.getInstance().getSerialsServiceScale().setmDatabits(Scale.getInstance().getScaleModel().getComDataBits());
        Scale.getInstance().getSerialsServiceScale().setmFlowControl(Scale.getInstance().getScaleModel().getComFlowControl());
        Scale.getInstance().getSerialsServiceScale().setmStopbits(Scale.getInstance().getScaleModel().getComStopBits());

        Log.e("ScaleModelManager", "Reset connection");
        //Scale.getInstance().getSerialsServiceScale().resetConnection();
    }

    public ScaleModel getScaleModelAccordingToPreferences() {

        Log.e("ScaleModelManager", "Set Scale Model");
        //Toast.makeText(ApplicationController.getContext(),"Set Scale Model",Toast.LENGTH_LONG);
        String key = "preferences_communication_list_devices";
        String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
        switch (modelValue) {
            case "1":
               // return new ScaleModelGandG();
                return new ScaleModelOHouse();
            case "2":
                return new ScaleModelDandT();
            case "3":
                return new ScaleModelAEAdam();
            default:
                return new ScaleModelSartoriusCP64();
        }
        // Toast.makeText(ApplicationController.getContext(),"Set Serial properties baud etc.",Toast.LENGTH_LONG);
        //Log.e("ScaleModelManager","Set Serial properties");
        //Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());
        //Scale.getInstance().getSerialsServiceScale().setmParity(Scale.getInstance().getScaleModel().getComParity());
        //Scale.getInstance().getSerialsServiceScale().setmDatabits(Scale.getInstance().getScaleModel().getComDataBits());
        //Scale.getInstance().getSerialsServiceScale().setmFlowControl(Scale.getInstance().getScaleModel().getComFlowControl());
        //Scale.getInstance().getSerialsServiceScale().setmStopbits(Scale.getInstance().getScaleModel().getComStopBits());
        // Toast.makeText(ApplicationController.getContext(),"Reset connection",Toast.LENGTH_LONG);
        //Log.e("ScaleModelManager","Reset connection");
        //Scale.getInstance().getSerialsServiceScale().resetConnection();


    }

}
