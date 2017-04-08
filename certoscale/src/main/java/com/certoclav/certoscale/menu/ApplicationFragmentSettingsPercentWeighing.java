package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;



public class ApplicationFragmentSettingsPercentWeighing extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonReferenceWeight = null;
    private Button buttonReferenceAdjust = null;
    private Button buttonCalculateReferenceWeight = null;
    private Button buttonOK = null;
    private Button buttonCancel = null;
    private TextView textInstruction = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_percent_weighing,container, false);
        buttonCalculateReferenceWeight = (Button) rootView.findViewById(R.id.application_settings_percent_weighing_button_recalculate);
        buttonReferenceWeight =  (Button) rootView.findViewById(R.id.application_settings_percent_weighing_button_reference);
        buttonCancel = (Button) rootView.findViewById(R.id.application_settings_percentage_button_cancel);
        buttonCancel.setVisibility(View.INVISIBLE);
        buttonCancel.setText(getString(R.string.cancel).toUpperCase());

        buttonOK = (Button) rootView.findViewById(R.id.application_settings_percentage_button_ok);
        buttonOK.setVisibility(View.INVISIBLE);
        textInstruction = (TextView) rootView.findViewById(R.id.application_settings_percentage_text);
        textInstruction.setVisibility(View.INVISIBLE);
        buttonReferenceWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_reference_weight);
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
                                Double inputvalInGram = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                Log.e("FragmentPercWeigh", "inputval in gram: " + inputvalInGram);
                                ApplicationManager.getInstance().getCurrentLibrary().setReferenceWeight(inputvalInGram);
                            }catch (Exception e){
                                ApplicationManager.getInstance().getCurrentLibrary().setReferenceWeight(0);
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


        buttonReferenceAdjust =  (Button) rootView.findViewById(R.id.application_settings_percent_weighing_button_reference_adjustment);
        buttonReferenceAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle(R.string.please_enter_an_adjustment_factor_for_the_reference);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("%");
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

                                ApplicationManager.getInstance().getCurrentLibrary().setReferenceweightAdjustment(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));
                            }catch(NumberFormatException e) {
                                ApplicationManager.getInstance().getCurrentLibrary().setReferenceweightAdjustment(0);
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

        buttonCalculateReferenceWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scale.getInstance().setScaleApplication(ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE);
                buttonReferenceAdjust.setEnabled(false);
                buttonCalculateReferenceWeight.setEnabled(false);
                buttonReferenceWeight.setEnabled(false);
                buttonOK.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);
                textInstruction.setVisibility(View.VISIBLE);
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplicationManager.getInstance().getCurrentLibrary().setReferenceWeight(ApplicationManager.getInstance().getTaredValueInGram());
                        Scale.getInstance().setScaleApplication(ScaleApplication.PERCENT_WEIGHING);
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Scale.getInstance().setScaleApplication(ScaleApplication.PERCENT_WEIGHING);

                        onResume();
                    }
                });
            }
        });

        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }






    @Override
    public void onResume() {
        buttonReferenceWeight.setText(getString(R.string.refernce_weight)+"\n"+ ApplicationManager.getInstance().getReferenceWeightAsStringWithUnit());
        buttonReferenceAdjust.setText(getString(R.string.reference_adjustment)+"\n"+ String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getReferenceweightAdjustment())+ " %");
        super.onResume();



    }

    @Override
    public void onPause() {

        super.onPause();

    }




}
