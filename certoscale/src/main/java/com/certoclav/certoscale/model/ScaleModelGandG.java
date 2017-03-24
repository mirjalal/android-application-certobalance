package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.service.ReadAndParseSerialService;
import com.certoclav.certoscale.supervisor.ApplicationManager;

/**
 * Created by Enrico on 22.03.2017.
 */

public class ScaleModelGandG extends ScaleModel {


    public void ScaleModelGandG(){
        maximumCapazity=5000;
        decimalPlaces=1;
    }


    @Override
    public int initializeParameters() {
        maximumCapazity=5000;
        decimalPlaces=1;

        hasZerobutton=false;
        return 1;
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
    public int pressTara() {

        ReadAndParseSerialService.getInstance().pauseParseSerialThread();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bt");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bt");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bt");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bt");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bt");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ReadAndParseSerialService.getInstance().startParseSerialThread();

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
            //Log.e("ReadAndParse", "received: " + message);
            String[] arguments = message.split(" ");
            if (arguments.length != 0) {
                for (String arg : arguments) {

                    if (arg.equals("-")){
                        sign=-1;
                    }


                    if (arg.length() > 2 && arg.contains(".")) {
                        try {
                            value = Double.parseDouble(arg);
                        } catch (Exception e) {
                            value = 0d;
                            //Log.e("ReadAndParseSerialServ", "Error parsing Double");
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
    void internalCalibration() {

    }

    @Override
    public int externelCalibration(Context context) {

        ReadAndParseSerialService.getInstance().pauseParseSerialThread();

        try{
            Thread.sleep(200);
        }catch(Exception e){

        }

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


                    Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bq");
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){

                    }
                    Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bq");

                }
            });

            Button dialogButton2 = (Button) dialog.findViewById(R.id.dialog_edit_instruction_button_2);

            dialogButton2.setText("5000g");
            dialogButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Scale.getInstance().getSerialsServiceScale().sendMessage("\u001Bq");

                }
            });

            dialog.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        ReadAndParseSerialService.getInstance().startParseSerialThread();

        return 0;
    }
}
