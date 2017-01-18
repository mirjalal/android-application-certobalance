package com.certoclav.certoscale.supervisor;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.menu.AnimationCalibrationActivity;
import com.certoclav.certoscale.menu.NotificationActivity;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.service.CloudSocketService;
import com.certoclav.certoscale.service.ReadAndParseSerialService;
import com.certoclav.certoscale.service.ReadAndParseSerialSicsService;
import com.certoclav.library.application.ApplicationController;

import static com.certoclav.certoscale.model.ScaleState.CABLE_NOT_CONNECTED;
import static com.certoclav.certoscale.model.ScaleState.ON_AND_CALIBRATING;
import static com.certoclav.certoscale.model.ScaleState.ON_AND_MODE_GRAM;
import static com.certoclav.certoscale.model.ScaleState.ON_AND_MODE_NOT_GRAM;


/**
 * Created by Michael on 12/5/2016.
 */

public class StateMachine implements WeightListener {

    public boolean isIgnoreErrors() {
        return ignoreErrors;
    }

    public void setIgnoreErrors(boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    private int delaycounter =0;
    private boolean ignoreErrors = false;

    private StateMachine(){
        Scale.getInstance().setOnWeightListener(this);
        nanoTimeAtLastMessageReceived = System.nanoTime();
        timerHandler.postDelayed(timerRunnable, 0);
    }


    private static StateMachine instance = new StateMachine();

    public static synchronized StateMachine getInstance(){
        return instance;

    }


    private long nanoTimeAtLastMessageReceived = 0;
    //state machine callback thread
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        //This Thread will be called every 0.5 seconds.
        @Override
        public void run() {
            updateStateMachine();
            timerHandler.postDelayed(this, 3000);
        }
    };
    private long nanoTimeAtLastServiceCheck = 0;
    private long nanoTimeAtLastONOFFCommand = 0;

    /**
     * This is the logic for the state machine
     * Called every about ~500ms from local Thread
     */
    public void updateStateMachine() {


        //CHECK IF SERVICES ARE STILL RUNNING. This is necessary because OS can kill service without our knowledge
        if ((System.nanoTime() - nanoTimeAtLastServiceCheck) > (1000000000L * 10)) { //3 seconds past
            Log.e("StateMachine", "STARTING SOCKET INTENT SERVICE");
            //Log.e("Monitor", "restart service");
            nanoTimeAtLastServiceCheck = System.nanoTime();

            //restart service. Mabye it has been killed by Operating System
            Intent intent = new Intent(ApplicationController.getContext(), CloudSocketService.class);
            ApplicationController.getContext().startService(intent);

            intent = new Intent(ApplicationController.getContext(), ReadAndParseSerialSicsService.class);
            ApplicationController.getContext().startService(intent);
        }



        //STATE PARSER
            //after 30 seconds of communications failures, set state of microcontroller to disconnected
            String rawResponseTransformed = Scale.getInstance().getRawResponseFromBalance().replace("\n","").replace("\r","").replaceAll("\\p{C}", "?");
            Log.e("StateMachine", "rawresp: " + rawResponseTransformed);


            if ((System.nanoTime() - nanoTimeAtLastMessageReceived) > (1000000000L * 30)) {
                    Scale.getInstance().setScaleState(CABLE_NOT_CONNECTED);
            }else{
                if(rawResponseTransformed.contains("g") && !rawResponseTransformed.contains("??")){
                    delaycounter = 0;
                    Scale.getInstance().setScaleState(ON_AND_MODE_GRAM);
                }else if(rawResponseTransformed.contains("?????")){
                    Scale.getInstance().setScaleState(ON_AND_CALIBRATING);
                }else if(rawResponseTransformed.contains("????")){
                    delaycounter++;
                    if(delaycounter> 10)
                    Scale.getInstance().setScaleState(ScaleState.OFF); //for example: "+   ????  g"
                }else{
                    delaycounter++;
                    if(delaycounter> 10)
                    Scale.getInstance().setScaleState(ON_AND_MODE_NOT_GRAM);
                }
            }



        //ISSUE HANDLER
            switch (Scale.getInstance().getState()) {
                case OFF:
                    if(ignoreErrors == false) {
                        Intent intent = new Intent(ApplicationController.getContext(), NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ApplicationController.getContext().startActivity(intent);
                    }
                    //Turn the balance on
                    if ((System.nanoTime() - nanoTimeAtLastONOFFCommand) > (1000000000L * 20)){
                        nanoTimeAtLastONOFFCommand = System.nanoTime();
                        ReadAndParseSerialService.getInstance().getCommandQueue().add("O\r\n");
                        Log.e("StateMachine", "Added O to commandqueue");
                     }
                    break;
                case CABLE_NOT_CONNECTED:
                    if(ignoreErrors == false) {
                        Intent intent = new Intent(ApplicationController.getContext(), NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ApplicationController.getContext().startActivity(intent);
                    }
                    break;
                case ON_AND_MODE_GRAM:
                    //Everything is well. Nothing to do right now.
                    break;
                case ON_AND_CALIBRATING:
                    Intent intent = new Intent(ApplicationController.getContext(), AnimationCalibrationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ApplicationController.getContext().startActivity(intent);
                    break;
                case ON_AND_MODE_NOT_GRAM:
                    if(ignoreErrors == false) {
                        intent = new Intent(ApplicationController.getContext(), NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ApplicationController.getContext().startActivity(intent);
                    }
                    //change mode until mode is gram
                    ReadAndParseSerialService.getInstance().getCommandQueue().add("M\r\n");
                    Log.e("StateMachine", "Added M to commandqueue");
                    break;

                default:
                    break;
            }

        Log.e("StateMachine", "state: " + Scale.getInstance().getState().toString());

    }



    @Override
    public void onWeightChanged(Double weight, String unit) {
        nanoTimeAtLastMessageReceived = System.nanoTime();
    }
}


