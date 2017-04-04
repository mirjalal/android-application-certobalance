package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.service.ReadAndParseSerialService;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelGandG extends ScaleModel {


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

        this.comBaudrate=comBaudrate;
        this.comDataBits=comDataBits;
        this.comParity=comParity;
        this.comStopBits=comStopBits;
        setPeriodicMessagingEnabled(true);
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
        double value=0;
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
                            value = 0d;
                        }
                    }
                }
                value=value*sign;
            }else{
                value = 0d;
            }

            }
        return value;
    }

    @Override
    public int  internalCalibration() {

        ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
        return 0;
    }

    @Override
    public int externelCalibration(Context context) {

        setPeriodicMessagingEnabled(false);


        double chosenValue=0;


                final Dialog dialog3 = new Dialog(context);
            dialog3.setContentView(R.layout.dialog_instruction);
            dialog3.setTitle("Plase place" +Double.toString(chosenValue) +context.getString(R.string.on_the_pan));
            ((TextView)dialog3.findViewById(R.id.dialog_instruction_text)).setText("Plase place" +Double.toString(chosenValue) +context.getString(R.string.on_the_pan));

            // set the custom dialog components - text, image and button

            Button dialogButton13 = (Button) dialog3.findViewById(R.id.dialog_edit_instruction_button_1);
            dialogButton13.setText(R.string.cancel);
            dialogButton13.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pressTara();
                    dialog3.dismiss();
                    setPeriodicMessagingEnabled(true);

                }
            });

            Button dialogButton23 = (Button) dialog3.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton23.setText(R.string.ok);
            dialogButton23.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressTara();
                    dialog3.dismiss();
                    setPeriodicMessagingEnabled(true);
                }
            });


            final Dialog dialog2 = new Dialog(context);
            dialog2.setContentView(R.layout.dialog_instruction);
            dialog2.setTitle("Plase place" +Double.toString(chosenValue) +context.getString(R.string.on_the_pan));
            ((TextView)dialog2.findViewById(R.id.dialog_instruction_text)).setText("Plase place" +Double.toString(chosenValue) +context.getString(R.string.on_the_pan));

            // set the custom dialog components - text, image and button

            Button dialogButton11 = (Button) dialog2.findViewById(R.id.dialog_edit_instruction_button_1);
            dialogButton11.setText(R.string.cancel);
            dialogButton11.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    pressTara();
                    dialog2.dismiss();
                    try {
                        dialog3.show();
                    }catch(Exception e){

                    }

                }
            });

            Button dialogButton22 = (Button) dialog2.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton22.setText(R.string.ok);
            dialogButton22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressTara();
                    dialog2.dismiss();
                    try {
                        dialog3.show();
                    }catch(Exception e){

                    }

                }
            });










        try{
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_instruction);
            dialog.setTitle(R.string.please_choose_calibration_weight);
            ((TextView)dialog.findViewById(R.id.dialog_instruction_text)).setText(R.string.please_choose_calibration_weight);

            // set the custom dialog components - text, image and button

            Button dialogButton1 = (Button) dialog.findViewById(R.id.dialog_edit_instruction_button_1);
            dialogButton1.setText("2000 g");
            dialogButton1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    double chosenValue=2000;
                    ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
                    ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
                    dialog.dismiss();
                    try {
                        dialog2.show();
                    }catch(Exception e) {

                    }

                }
            });

            Button dialogButton2 = (Button) dialog.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton2.setText("5000g");
            dialogButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    double chosenValue=5000;

                    ReadAndParseSerialService.getInstance().getCommandQueue().add("\u001Bq");
                    dialog.dismiss();
                    try{
                    dialog2.show();
                    }catch(Exception e){

                    }
                }
            });

            dialog.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }








        return 0;
    }


}
