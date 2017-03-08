package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.supervisor.ApplicationManager;


public class ApplicationFragmentSettingsCheckWeighing extends Fragment {


    private TextView textInstruction = null;
    private Button buttonOK = null;
    private Button buttonCancel = null;

    // private Button buttonSave = null;

    private Button button_under_limit = null;
    private Button button_over_limit = null;

    private Button button_nominal=null;
    private Button button_nominal_tolerance_over=null;
    private Button button_nominal_tolerance_under=null;

    private Button button_nominal_tolerance_over_percent=null;
    private Button button_nominal_tolerance_under_percent=null;


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

        button_nominal = (Button) rootView.findViewById(R.id.settings_check_weighing_button_nominal);
        button_nominal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_nominal_weight);
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
                                ApplicationManager.getInstance().setCheckNominal(inputvalTransformed);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setCheckNominal(0);

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
        button_nominal.setVisibility(View.GONE);
        button_nominal.setEnabled(true);


        button_nominal_tolerance_over = (Button) rootView.findViewById(R.id.settings_check_weighing_button_nominal_tolerance_over);
        button_nominal_tolerance_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_upper_tolerance_limit_weight);
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
                                ApplicationManager.getInstance().setCheckNominalToleranceOver(inputvalTransformed);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setCheckNominalToleranceOver(0);

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
        button_nominal_tolerance_over.setVisibility(View.GONE);
        button_nominal_tolerance_over.setEnabled(true);


        button_nominal_tolerance_under = (Button) rootView.findViewById(R.id.settings_check_weighing_button_nominal_tolerance_under);
        button_nominal_tolerance_under.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_under_tolerance_limit_weight);
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
                                ApplicationManager.getInstance().setCheckNominalToleranceUnder(inputvalTransformed);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setCheckNominalToleranceUnder(0);

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
        button_nominal_tolerance_under.setVisibility(View.GONE);
        button_nominal_tolerance_under.setEnabled(true);

        button_under_limit = (Button) rootView.findViewById(R.id.settings_check_weighing_button_under_limit);
        button_under_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_under_limit);
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
                                ApplicationManager.getInstance().setUnderLimitCheckWeighing(inputvalTransformed);

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
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_over_limit);
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
                                ApplicationManager.getInstance().setOverLimitCheckWeighing(inputvalTransformed);
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



        button_nominal_tolerance_under_percent = (Button) rootView.findViewById(R.id.settings_check_weighing_button_nominal_tolerance_under_percent);
        button_nominal_tolerance_under_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_under_tolerance_limit_in_percent);
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
                                ApplicationManager.getInstance().setCheckNominalToleranceUnderPercent(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setCheckNominalToleranceUnderPercent(0);

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

        button_nominal_tolerance_under_percent.setVisibility(View.GONE);
        button_nominal_tolerance_under_percent.setEnabled(true);




        button_nominal_tolerance_over_percent = (Button) rootView.findViewById(R.id.settings_check_weighing_button_nominal_tolerance_over_percent);
        button_nominal_tolerance_over_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_over_tolerance_in_percent);
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
                                ApplicationManager.getInstance().setCheckNominalToleranceOverPercent(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setCheckNominalToleranceOverPercent(0);

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

        button_nominal_tolerance_over_percent.setVisibility(View.GONE);
        button_nominal_tolerance_over_percent.setEnabled(true);

        //test


        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }






    @Override
    public void onResume() {
        super.onResume();



        button_under_limit.setText(getString(R.string.under_limit)+"\n" +ApplicationManager.getInstance().getUnderLimitCheckWeighingAsStringWithUnit());
        button_over_limit.setText(getString(R.string.over_limit)+"\n"+ApplicationManager.getInstance().getOverLimitCheckWeighingAsStringWithUnit());

        button_nominal.setText(getString(R.string.nominal)+ApplicationManager.getInstance().getCheckNominalAsStringWithUnit());
        button_nominal_tolerance_over.setText(getString(R.string.tolerance_plus)+ApplicationManager.getInstance().getCheckNominalToleranceOverAsStringWithUnit());
        button_nominal_tolerance_under.setText(getString(R.string.tolerance_minus)+ApplicationManager.getInstance().getCheckNominalToleranceUnderAsStringWithUnit());

        button_nominal_tolerance_over_percent.setText(getString(R.string.tolerance_plus)+"\n"+ApplicationManager.getInstance().getCheckNominalToleranceOverPercent() + " %");
        button_nominal_tolerance_under_percent.setText(getString(R.string.tolerance_minus)+"\n"+ApplicationManager.getInstance().getCheckNominalToleranceUnderPercent() + " %");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cmode = prefs.getString(getString(R.string.preferences_check_limitmode),"");

        if(cmode.equals("1")){ //Limit mode: Over And Under
            button_under_limit.setVisibility(View.VISIBLE);
            button_over_limit.setVisibility(View.VISIBLE);
            button_under_limit.setEnabled(true);
            button_over_limit.setEnabled(true);

            button_nominal.setVisibility(View.GONE);
            button_nominal.setEnabled(true);
            button_nominal_tolerance_over.setVisibility(View.GONE);
            button_nominal_tolerance_over.setEnabled(true);
            button_nominal_tolerance_under.setVisibility(View.GONE);
            button_nominal_tolerance_over.setEnabled(true);

            button_nominal_tolerance_over_percent.setVisibility(View.GONE);
            button_nominal_tolerance_under_percent.setVisibility(View.GONE);


        }
        if(cmode.equals("2")){// Limit mode: Nominal +- Weight Tolarance



            button_under_limit.setVisibility(View.GONE);
            button_over_limit.setVisibility(View.GONE);
            button_nominal.setVisibility(View.VISIBLE);
            button_nominal.setEnabled(true);
            button_nominal_tolerance_over.setVisibility(View.VISIBLE);
            button_nominal_tolerance_over.setEnabled(true);
            button_nominal_tolerance_under.setVisibility(View.VISIBLE);
            button_nominal_tolerance_under.setEnabled(true);



            button_nominal_tolerance_over_percent.setVisibility(View.GONE);
            button_nominal_tolerance_under_percent.setVisibility(View.GONE);


        }

        if(cmode.equals("3")){ // Nominal +- Percantage Tolerance


            button_under_limit.setVisibility(View.GONE);
            button_over_limit.setVisibility(View.GONE);
            button_nominal.setVisibility(View.VISIBLE);
            button_nominal.setEnabled(true);


            button_nominal_tolerance_over.setVisibility(View.GONE);
            button_nominal_tolerance_under.setVisibility(View.GONE);


            button_nominal_tolerance_over_percent.setVisibility(View.VISIBLE);
            button_nominal_tolerance_over_percent.setEnabled(true);
            button_nominal_tolerance_under_percent.setVisibility(View.VISIBLE);
            button_nominal_tolerance_under_percent.setEnabled(true);

        }


    }

    @Override
    public void onPause() {

        super.onPause();

    }




}
