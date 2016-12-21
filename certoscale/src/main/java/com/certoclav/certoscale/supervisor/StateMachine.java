package com.certoclav.certoscale.supervisor;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.service.CloudSocketService;
import com.certoclav.library.application.ApplicationController;


/**
 * Created by Michael on 12/5/2016.
 */

public class StateMachine {



    private StateMachine(){
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
            timerHandler.postDelayed(this, 500);
        }
    };
    private long nanoTimeAtLastServiceCheck = 0;


    /**
     * This is the logic for the state machine
     * Called every about ~500ms from local Thread
     */
    public void updateStateMachine() {
        Log.e("StateMachine", "update");
        if ((System.nanoTime() - nanoTimeAtLastServiceCheck) > (1000000000L * 10)) { //3 seconds past
            Log.e("StateMachine", "STARTING SOCKET INTENT SERVICE");
            //Log.e("Monitor", "restart service");
            nanoTimeAtLastServiceCheck = System.nanoTime();

            //restart service. Mabye it has been killed by Operating System
            Intent intent = new Intent(ApplicationController.getContext(), CloudSocketService.class);
            ApplicationController.getContext().startService(intent);
        }

            //after 30 seconds of communications failures, set state of microcontroller to disconnected
            if ((System.nanoTime() - nanoTimeAtLastMessageReceived) > (1000000000L * 30)) {
                Scale.getInstance().setMicrocontrollerReachable(false);
                Scale.getInstance().setState(ScaleState.NOT_CONNECTED);
            } else {
                Scale.getInstance().setMicrocontrollerReachable(true);
                Scale.getInstance().setState(ScaleState.READY); // TODO: seperate between READY and WAITING_FOR_LOGIN
            }

            ScaleState state = Scale.getInstance().getState();
            switch (state) {
                case NOT_CONNECTED:
                    //TODO: Try to connect to scale. Maybe its turned off or not powered on currently
                    break;
                case READY:
                    //Everything is well. Nothing to do right now.
                    break;
                case OFF:
                    //Show message that scale is turned off right now (Power saving mode)
                    break;
                default:
                    break;
            }


    }

    }


