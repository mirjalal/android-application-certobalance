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


public class ApplicationFragmentSettingsFilling extends Fragment {


    private Button buttonCalculateTarget = null;
    private TextView textInstruction = null;
    private Button buttonOK = null;
    private Button buttonCancel = null;
    private Button button_target = null;

    private LinearLayout containerSettingsButtons = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_filling,container, false);
        buttonOK = (Button) rootView.findViewById(R.id.settings_filling_button_ok);
        buttonOK.setVisibility(View.INVISIBLE);
      //  buttonSave = (Button) rootView.findViewById(R.id.settings_filling_button_save);
        buttonCancel = (Button) rootView.findViewById(R.id.settings_filling_button_cancel);
        buttonCancel.setVisibility(View.INVISIBLE);
        textInstruction = (TextView) rootView.findViewById(R.id.settings_filling_text_instruction);
        textInstruction.setVisibility(View.INVISIBLE);
        buttonCalculateTarget = (Button) rootView.findViewById(R.id.settings_filling_button_calc_target);
      

        button_target = (Button) rootView.findViewById(R.id.settings_filling_button_target);
        button_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle("Please enter the target weight");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(ApplicationManager.getInstance().getCurrentUnit().getName());

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
                                Double inputval = Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString());
                                Double inputvalTransformed = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                ApplicationManager.getInstance().getCurrentLibrary().setTarget(inputvalTransformed);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setTarget(0);

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




        buttonCalculateTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scale.getInstance().setScaleApplication(ScaleApplication.FILLING_CALC_TARGET); //problem
                buttonCalculateTarget.setEnabled(false);
                button_target.setEnabled(false);
                textInstruction.setText(R.string.Place_target_weight_on_pan);
                textInstruction.setVisibility(View.VISIBLE);
                Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                a.reset();
                textInstruction.clearAnimation();
                textInstruction.startAnimation(a);
                buttonOK.setVisibility(View.VISIBLE);
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplicationManager.getInstance().getCurrentLibrary().setTarget(ApplicationManager.getInstance().getTaredValueInGram());
                        buttonOK.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.INVISIBLE);
                        textInstruction.setVisibility(View.INVISIBLE);
                        buttonCalculateTarget.setEnabled(true);
                        button_target.setEnabled(true);
                        Scale.getInstance().setScaleApplication(ScaleApplication.FILLING);
                        onResume();
                    }
                });
                buttonCancel.setVisibility(View.VISIBLE);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonOK.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.INVISIBLE);
                        textInstruction.setVisibility(View.INVISIBLE);
                        buttonCalculateTarget.setEnabled(true);
                        Scale.getInstance().setScaleApplication(ScaleApplication.FILLING);
                        onResume();
                    }
                });


            }
        });


        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        button_target.setVisibility(View.VISIBLE);
        button_target.setText("Target Weight:\n"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getTarget()) + " g");



    }

    @Override
    public void onPause() {

        super.onPause();

    }




}
