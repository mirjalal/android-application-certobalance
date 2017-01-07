package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;


public class ApplicationFragmentSettingsCheckWeighing extends Fragment {


    private TextView textInstruction = null;
    private Button buttonOK = null;
    private Button buttonCancel = null;
    // private Button buttonSave = null;

    private Button button_under_limit = null;
    private Button button_over_limit = null;



    private LinearLayout containerSettingsButtons = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_check_weighing,container, false);
        buttonOK = (Button) rootView.findViewById(R.id.settings_check_weighing_button_ok);
        buttonOK.setVisibility(View.INVISIBLE);
        //  buttonSave = (Button) rootView.findViewById(R.id.settings_partcounting_button_save);
        buttonCancel = (Button) rootView.findViewById(R.id.settings_check_weighing_button_cancel);
        buttonCancel.setVisibility(View.INVISIBLE);
        textInstruction = (TextView) rootView.findViewById(R.id.settings_check_weighing_text_instruction);
        textInstruction.setVisibility(View.INVISIBLE);



        button_under_limit = (Button) rootView.findViewById(R.id.settings_check_weighing_button_under_limit);
        button_under_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle("Please enter the under limit");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("g");
                    // set the custom dialog components - text, image and button

                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_number_button_cancel);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_number_button_ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ApplicationManager.getInstance().setUnderLimitCheckWeighing(Integer.parseInt(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setUnderLimitCheckWeighing(0);

                            }dialog.dismiss();
                            onResume();



                        }
                    });

                    dialog.show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });



        button_over_limit = (Button) rootView.findViewById(R.id.settings_check_weighing_button_over_limit);
        button_over_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle("Please enter the over limit");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("g");
                    // set the custom dialog components - text, image and button

                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_number_button_cancel);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_number_button_ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ApplicationManager.getInstance().setOverLimitCheckWeighing(Integer.parseInt(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));
                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setOverLimitCheckWeighing(0);
                            }
                            dialog.dismiss();
                            onResume();



                        }
                    });

                    dialog.show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });





        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        button_under_limit.setText("Under limit:\n"+ApplicationManager.getInstance().getUnderLimitChekcWeighingAsString() + " g");
        button_over_limit.setText("Over limit:\n"+ApplicationManager.getInstance().getOverLimitChekcWeighingAsString() + " g");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cmode = prefs.getString(getString(R.string.preferences_check_limitmode),"");

        if(cmode.equals("1")){
            button_under_limit.setVisibility(View.VISIBLE);
            button_over_limit.setVisibility(View.VISIBLE);


        }
        if(cmode.equals("2")){

            button_under_limit.setVisibility(View.INVISIBLE);
            button_over_limit.setVisibility(View.INVISIBLE);

        }

        if(cmode.equals("3")){
            button_under_limit.setVisibility(View.INVISIBLE);
            button_over_limit.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public void onPause() {
        Scale.getInstance().setScaleApplication(ScaleApplication.CHECK_WEIGHING);
        super.onPause();

    }




}
