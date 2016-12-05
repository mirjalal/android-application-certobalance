package com.certoclav.certoscale.supervisor;

import android.os.Handler;

import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;



/**
 * Created by Michael on 12/5/2016.
 */

public class StateMachine {

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


    /**
     * This is the logic for the state machine
     * Called every about ~500ms from local Thread
     */
    public void updateStateMachine() {


        //after 30 seconds of communications failures, set state of microcontroller to disconnected
        if ((System.nanoTime() - nanoTimeAtLastMessageReceived) > (1000000000L * 30)) {
            Scale.getInstance().setMicrocontrollerReachable(false);
            Scale.getInstance().setState(ScaleState.NOT_CONNECTED);
        } else {
            Scale.getInstance().setMicrocontrollerReachable(true);
            Scale.getInstance().setState(ScaleState.READY); // TODO: seperate between READY and WAITING_FOR_LOGIN
        }

        ScaleState state = Scale.getInstance().getState();
        switch (state){
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


