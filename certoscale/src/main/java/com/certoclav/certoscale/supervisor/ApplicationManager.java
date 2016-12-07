package com.certoclav.certoscale.supervisor;

import android.util.Log;

import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.SensorDataListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager implements SensorDataListener, ScaleApplicationListener {


    private static ApplicationManager instance = new ApplicationManager();

    private ApplicationManager(){
        Scale.getInstance().setOnApplicationListener(this);
        Scale.getInstance().setOnSensorDataListener(this);
    }

    public static synchronized ApplicationManager getInstance(){
        return instance;

    }


    @Override
    public void onApplicationChange(ScaleApplication application) {

        Scale.getInstance().setWeightTara((float) 0);

        switch (application){

            case WEIGHING:
                Scale.getInstance().setUnitTransformed("g");
                break;
            case PART_COUNTING:
                Log.e("ApplicationFragmentW", "change unit to pieces");
                Scale.getInstance().setUnitTransformed("pieces");
                break;
            case PERCENT_WEIGHING:
                Scale.getInstance().setUnitTransformed("%");
                break;
            default:
                Scale.getInstance().setUnitTransformed("g");

        }
    }

    @Override
    public void onSensorDataChange(Float value, String unit) {

        //TODO: Do calculations here depending on current ScaleApplication

        //For example:
        switch (Scale.getInstance().getScaleApplication()){
            case PART_COUNTING:
                Scale.getInstance().setWeightMeasured((float) ((int)(value/Scale.getInstance().getAveragePieceWeight()))); //example if one piece weights 3 grams
                break;
            default:
                Scale.getInstance().setWeightMeasured(value);
        }

    }
}
