package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class ApplicationFragmentSettingsPartCounting extends Fragment {

    private Button buttonEditAveragePieceWeight = null;
    private Button buttonEditSampleSize = null;
    private Button buttonCalculateAwp = null;
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
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_partcounting,container, false);
        buttonOK = (Button) rootView.findViewById(R.id.settings_partcounting_button_ok);
        buttonOK.setVisibility(View.INVISIBLE);
      //  buttonSave = (Button) rootView.findViewById(R.id.settings_partcounting_button_save);
        buttonCancel = (Button) rootView.findViewById(R.id.settings_partcounting_button_cancel);
        buttonCancel.setVisibility(View.INVISIBLE);
        textInstruction = (TextView) rootView.findViewById(R.id.settings_partcounting_text_instruction);
        textInstruction.setVisibility(View.INVISIBLE);
        buttonCalculateAwp = (Button) rootView.findViewById(R.id.settings_partcounting_button_calc_awp);
        buttonEditAveragePieceWeight = (Button) rootView.findViewById(R.id.settings_partcounting_button_awp);
        buttonEditAveragePieceWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                        dialog.setTitle("Please enter the average piece weight");
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
                                ApplicationManager.getInstance().setAveragePieceWeightInGram(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));
                            }catch(NumberFormatException e){
                                ApplicationManager.getInstance().setAveragePieceWeightInGram(0d);
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

        buttonEditSampleSize = (Button) rootView.findViewById(R.id.settings_partcounting_button_sample_size);
        buttonEditSampleSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle("Please enter the number of samples for the calculation of awp");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("pieces");
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
                                ApplicationManager.getInstance().setAwpCalcSampleSize(Integer.parseInt(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));
                            }catch(NumberFormatException e){
                                ApplicationManager.getInstance().setAwpCalcSampleSize(0);

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


        button_under_limit = (Button) rootView.findViewById(R.id.settings_partcounting_button_under_limit);
        button_under_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle("Please enter the under limit");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("pieces");
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
                                ApplicationManager.getInstance().setUnderLimitPieces(Integer.parseInt(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setUnderLimitPieces(0);

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



        button_over_limit = (Button) rootView.findViewById(R.id.settings_partcounting_button_over_limit);
        button_over_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle("Please enter the over limit");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("pieces");
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
                                ApplicationManager.getInstance().setOverLimitPieces(Integer.parseInt(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));
                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setOverLimitPieces(0);
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




        buttonCalculateAwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scale.getInstance().setScaleApplication(ScaleApplication.PART_COUNTING_CALC_AWP); //problem
                buttonEditSampleSize.setEnabled(false);
                buttonEditAveragePieceWeight.setEnabled(false);
                buttonCalculateAwp.setEnabled(false);
                button_under_limit.setEnabled(false);
                button_over_limit.setEnabled(false);
                textInstruction.setText("Pleace place " + ApplicationManager.getInstance().getAwpCalcSampleSize()+ " pcs. " + "onto the pan");
                textInstruction.setVisibility(View.VISIBLE);
                buttonOK.setVisibility(View.VISIBLE);
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplicationManager.getInstance().setAveragePieceWeightInGram(ApplicationManager.getInstance().getTaredValueInGram()/(double)ApplicationManager.getInstance().getAwpCalcSampleSize());
                        buttonOK.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.INVISIBLE);
                        textInstruction.setVisibility(View.INVISIBLE);
                        buttonEditSampleSize.setEnabled(true);
                        buttonEditAveragePieceWeight.setEnabled(true);
                        buttonCalculateAwp.setEnabled(true);
                        button_under_limit.setEnabled(true);
                        button_over_limit.setEnabled(true);
                        Scale.getInstance().setScaleApplication(ScaleApplication.PART_COUNTING);
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
                        buttonEditSampleSize.setEnabled(true);
                        buttonEditAveragePieceWeight.setEnabled(true);
                        buttonCalculateAwp.setEnabled(true);
                        Scale.getInstance().setScaleApplication(ScaleApplication.PART_COUNTING);
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
        buttonEditAveragePieceWeight.setText("Average piece weight\n" + ApplicationManager.getInstance().getAveragePieceWeightAsStringInGram() + " g");
        buttonEditSampleSize.setText("Sample size:\n" + ApplicationManager.getInstance().getAwpCalcSampleSize() + " pieces");
        button_under_limit.setText("Under limit:\n"+ApplicationManager.getInstance().getUnderLimitPiecesAsString() + " pieces");
        button_over_limit.setText("Over limit:\n"+ApplicationManager.getInstance().getOverlimitPiecesAsString() + " pieces");


    }

    @Override
    public void onPause() {
        Scale.getInstance().setScaleApplication(ScaleApplication.PART_COUNTING);
        super.onPause();

    }




}
