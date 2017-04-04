package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.service.ReadAndParseSerialService;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelGandG extends ScaleModel {

    double chosenValue = 0;
    double lastValue = 0;

    public void ScaleModelGandG(){
        maximumCapazity=5000;
        decimalPlaces=1;
        stabilisationTime=1;
        setPeriodicMessagingEnabled(true);
    }

    /*
    @Override
    public int initializeParameters() {
        maximumCapazity=5000;
        decimalPlaces=1;

        hasZerobutton=false;
        return 1;
    }
    */

    @Override
    public int initializeParameters(int maximumCapazity, int decimalPlaces, int stabilisationTime, int comBaudrate, int comDataBits, int comParity, int comStopBits,boolean hasZerobutton) {
        this.maximumCapazity=maximumCapazity;
        this.decimalPlaces=decimalPlaces;
        this.stabilisationTime=stabilisationTime;

        setPeriodicMessagingEnabled(true);
        this.comBaudrate=comBaudrate;
        this.comDataBits=comDataBits;
        this.comParity=comParity;
        this.comStopBits=comStopBits;
        this.hasZerobutton=hasZerobutton;


        return 0;
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
    public boolean isCommandResponse() {
        return true;
    }

    @Override
    public int pressTara() {

        ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bt");
        return 0;
    }

    @Override
    public int pressZero() {
        return 0;
    }

    @Override
    public double parseRecievedMessage(String message) {
        int sign=1;
        double value=lastValue;
        if(message.length()>5) {
            try {
                if (message.contains("-")) {
                    sign = -1;
                }

                if (message.contains("g")) {
                    stable = true;
                } else {
                     stable = false;
                }
            }catch (Exception e){

            }

            String[] arguments = message.split(" ");
            if (arguments.length != 0) {
                for (String arg : arguments) {



                    if (arg.length() > 2 && arg.contains(".")) {
                        try {
                            value = Double.parseDouble(arg);
                            break;
                        } catch (Exception e) {
                            value = lastValue;
                        }
                    }
                }
                value=value*sign;
            }else{
                value = lastValue;
            }

            }
            lastValue = value;

        return value;
    }

    @Override
    public int  internalCalibration() {

        ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
        return 0;
    }

    @Override
    public int externelCalibration(final Context context) {

        setPeriodicMessagingEnabled(false);



            final Dialog dialogTareBalance = new Dialog(context);
            dialogTareBalance.setContentView(R.layout.dialog_instruction);
            dialogTareBalance.setTitle(context.getString(R.string.external_calibration));

            ((TextView)dialogTareBalance.findViewById(R.id.dialog_instruction_text)).setText("Remove the calibration weight from the pan");

            Button dialogButton13 = (Button) dialogTareBalance.findViewById(R.id.dialog_edit_instruction_button_1);
            dialogButton13.setText(R.string.cancel);
            dialogButton13.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pressTara();
                    dialogTareBalance.dismiss();
                    setPeriodicMessagingEnabled(true);

                }
            });

            Button dialogButton23 = (Button) dialogTareBalance.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton23.setText(R.string.ok);
            dialogButton23.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressTara();
                    dialogTareBalance.dismiss();
                    setPeriodicMessagingEnabled(true);
                }
            });



            final PutCalibrationWeightDialog dialogPutCalibrationWeight = new PutCalibrationWeightDialog(context);
            dialogPutCalibrationWeight.setContentView(R.layout.dialog_instruction);
            dialogPutCalibrationWeight.setTitle(context.getString(R.string.external_calibration));
            ((TextView)dialogPutCalibrationWeight.findViewById(R.id.dialog_instruction_text)).setText(context.getString(R.string.place) + " "+ Double.toString(chosenValue) + " " +context.getString(R.string.on_the_pan));
        // set the custom dialog components - text, image and button
            // set the custom dialog components - text, image and button



            Button dialogButton11 = (Button) dialogPutCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_1);
            dialogButton11.setText(R.string.cancel);
            dialogButton11.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    pressTara();
                    dialogPutCalibrationWeight.dismiss();
                    try {
                        dialogTareBalance.show();
                    }catch(Exception e){

                    }

                }
            });

            final Button dialogButton22 = (Button) dialogPutCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton22.setText(R.string.ok);
            dialogButton22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Scale.getInstance().isStable()) {
                        pressTara();
                    }else{
                       Toast.makeText(context, "Wait until weight is stable", Toast.LENGTH_LONG).show();
                   }
                    dialogPutCalibrationWeight.dismiss();
                    try {
                        dialogTareBalance.show();
                    }catch(Exception e){

                    }


                }
            });

            dialogPutCalibrationWeight.setOnStableListener(new StableListener() {
                @Override
                public void onStableChanged(boolean isStable) {
                    if(isStable){
                        dialogButton22.setEnabled(true);
                    }else{
                        dialogButton22.setEnabled(false);
                    }
                }
            });











        try{
            final Dialog dialogChooseCalibrationWeight = new Dialog(context);
            dialogChooseCalibrationWeight.setContentView(R.layout.dialog_instruction);
            dialogChooseCalibrationWeight.setTitle(R.string.please_choose_calibration_weight);
            ((TextView)dialogChooseCalibrationWeight.findViewById(R.id.dialog_instruction_text)).setText(R.string.please_choose_calibration_weight);

            // set the custom dialog components - text, image and button

            Button dialogButton1 = (Button) dialogChooseCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_1);
            dialogButton1.setText("2000 g");
            dialogButton1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    chosenValue=2000;
                    ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
                    ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
                    dialogChooseCalibrationWeight.dismiss();
                    try {
                        dialogPutCalibrationWeight.show();
                    }catch(Exception e) {

                    }

                }
            });

            Button dialogButton2 = (Button) dialogChooseCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton2.setText("5000g");
            dialogButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    chosenValue=5000;

                    ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
                    dialogChooseCalibrationWeight.dismiss();
                    try{
                    dialogPutCalibrationWeight.show();
                    }catch(Exception e){

                    }
                }
            });

            dialogChooseCalibrationWeight.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }








        return 0;
    }


    public class PutCalibrationWeightDialog extends Dialog
    {
        private StableListener listener = null;
        public PutCalibrationWeightDialog(final Context context)
        {
            // Set your theme here
            super(context, android.R.style.Theme_Dialog);

            // This is the layout XML file that describes your Dialog layout
           // this.setContentView(R.layout.myDialogLayout);
        }

        public void setOnStableListener(StableListener listener){
            Scale.getInstance().setOnStableListener(listener);
        }


        @Override
        protected void onStop() {
            if(listener != null) {
                Scale.getInstance().removeOnStableListener(listener);
            }
            super.onStop();
        }


    }

}
