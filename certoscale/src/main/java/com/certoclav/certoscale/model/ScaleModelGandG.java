package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.service.ReadAndParseSerialService;

import android_serialport_api.SerialPort;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelGandG extends ScaleModel {

    private static double chosenValue = 0;
    private double lastValue = 0;

    public double getChosenValue() {
        return chosenValue;
    }

    public synchronized void setChosenValue(double chosenValue) {
        this.chosenValue = chosenValue;
    }



    public ScaleModelGandG(){
        super(2,//stabilisationTime,
                220,// maximumCapazity,
                4,// decimalPlaces,
                false,// stable,
                9600,// comBaudrate,
                SerialPort.DATABITS_8,// comDataBits,
                SerialPort.STOPBITS_1,// comStopBits,
                SerialPort.PARITY_NONE,// comParity,
                true,// isPeriodicMessagingEnabled,
                SerialPort.FLOW_CONTROL_NONE,// comFlowControl,
                true);// hasZerobutton);
    }



    @Override
    public int sendOnOffCommand() {
        return 0;
    }

    @Override
    public int sendModeCommand() {
        //ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bs");
        return 0;
    }

    @Override
    public int sendPrintCommand() {
        Scale.getInstance().getSerialsServiceScale().sendMessage("PRT\r\n");
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

        ReadAndParseSerialService.getInstance().getCommandQueue().add("T\r\n");
        return 0;
    }

    @Override
    public int pressZero() {
        return 0;
    }

    public void pressCalibrationButton(){
        //ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
    }

    public void pressUnitButton(){
        //ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bs");
    }

    public void pressCountingButton(){
        //ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Br");
    }


    public void pressLightButton(){
        //ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bu");
    }
    @Override
    public double parseRecievedMessage(String message) {
        int sign=1;
        double value=lastValue;
        message.replace("(", "");
        message.replace(")", "");
        message.replace("o", "");
        if(message.length()>5) {
            try {

                if (message.contains("-.H")) { //overweight
                    return maximumCapazity;
                }

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
                            Log.e("PARSE ARG", arg);
                            arg = arg.replace("(","");
                            arg = arg.replace(")","");
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

        //ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
        return 0;
    }

    @Override
    public int externelCalibration(final Context context) {

        setPeriodicMessagingEnabled(false);


        try{


            showChooseCalibrationWeightDialog(context);

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



    public void showChooseCalibrationWeightDialog(final Context context){
        final Dialog dialogChooseCalibrationWeight = new Dialog(context);
        dialogChooseCalibrationWeight.setContentView(R.layout.dialog_instruction);
        dialogChooseCalibrationWeight.setTitle(R.string.please_choose_calibration_weight);
        ((TextView)dialogChooseCalibrationWeight.findViewById(R.id.dialog_instruction_text)).setText(R.string.please_choose_calibration_weight);
        dialogChooseCalibrationWeight.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setPeriodicMessagingEnabled(true);
            }
        });

        // set the custom dialog components - text, image and button

        Button dialogButton1 = (Button) dialogChooseCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_1);
        dialogButton1.setText("2000 g");
        dialogButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setChosenValue(2000d);

                pressTara();
                pressCalibrationButton();
                pressCalibrationButton();

                dialogChooseCalibrationWeight.dismiss();
                try {
                    showPutCalibrationWeightDialog(context);
                }catch(Exception e) {

                }

            }
        });

        Button dialogButton2 = (Button) dialogChooseCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_2);

        dialogButton2.setText("5000 g");
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setChosenValue(5000);
                pressTara();
                pressCalibrationButton();
                dialogChooseCalibrationWeight.dismiss();
                try{
                    showPutCalibrationWeightDialog(context);
                }catch(Exception e){

                }
            }
        });

        dialogChooseCalibrationWeight.show();
    }


    public void showTareDialog(Context context){



        final Dialog dialogTareBalance = new Dialog(context);
        dialogTareBalance.setContentView(R.layout.dialog_instruction);
        dialogTareBalance.setTitle(context.getString(R.string.external_calibration));
        ((TextView)dialogTareBalance.findViewById(R.id.dialog_instruction_text)).setText(R.string.remove_the_calibration_weight_from_the_pan);
        dialogTareBalance.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pressTara();
                setPeriodicMessagingEnabled(true);
            }
        });

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


        dialogTareBalance.show();

    }



    public void showPutCalibrationWeightDialog(final Context context){

        final PutCalibrationWeightDialog dialogPutCalibrationWeight = new PutCalibrationWeightDialog(context);
        dialogPutCalibrationWeight.setContentView(R.layout.dialog_instruction);
        dialogPutCalibrationWeight.setTitle(context.getString(R.string.external_calibration));
        ((TextView)dialogPutCalibrationWeight.findViewById(R.id.dialog_instruction_text)).setText(context.getString(R.string.place) + " "+ getChosenValue() + " g " +context.getString(R.string.on_the_pan));
        // set the custom dialog components - text, image and button
        // set the custom dialog components - text, image and button
        dialogPutCalibrationWeight.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pressTara();
                setPeriodicMessagingEnabled(true);
            }
        });


        Button dialogButton11 = (Button) dialogPutCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_1);
        dialogButton11.setText(R.string.cancel);
        dialogButton11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               pressTara();
               setPeriodicMessagingEnabled(true);
                dialogPutCalibrationWeight.dismiss();


            }
        });

        final Button dialogButton22 = (Button) dialogPutCalibrationWeight.findViewById(R.id.dialog_edit_instruction_button_2);

        dialogButton22.setText(R.string.ok);
        dialogButton22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    pressTara();
                    dialogPutCalibrationWeight.dismiss();
                    try {
                        showTareDialog(context);

                    }catch(Exception e){

                    }




            }
        });
        dialogPutCalibrationWeight.show();
    }
}
