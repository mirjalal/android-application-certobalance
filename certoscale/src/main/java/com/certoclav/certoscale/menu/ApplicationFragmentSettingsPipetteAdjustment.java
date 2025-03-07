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
import com.certoclav.certoscale.supervisor.ApplicationManager;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentSettingsPipetteAdjustment extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonNominal = null;
    private Button buttonWaterTemp = null;
    private Button buttonInaccuracy = null;
    private Button buttonImprecision = null;

    private Button buttonPressure = null;
    private Button buttonPipettename = null;
    private Button buttonPipettenumber = null;
    private Button buttonNumberofSamples = null;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_pipette_adjustment,container, false);
        buttonNominal =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_nominal);
        buttonNominal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_normal_pipette);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("ml");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteNominal(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteNominal(0);
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


        buttonWaterTemp =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_water_temp);
        buttonWaterTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_water);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(" °C");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteWaterTemp(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteWaterTemp(0);
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

        buttonInaccuracy =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_inaccuracy);
        buttonInaccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_pipette_inaccuracy);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(" %");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteInaccuracy(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteInaccuracy(0);
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




        buttonImprecision =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_impression);
        buttonImprecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_imprecision_of_the_pipette);
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
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteImprecision(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteImprecision(0);
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


        buttonPressure =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_pressure);
        buttonPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_pressure);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("ATM");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setPipettePressure(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setPipettePressure(0);
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


        buttonPipettename =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_pipettename);
        buttonPipettename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_text);
            dialog.setTitle(R.string.please_enter_the_pipette_name);

            // set the custom dialog components - text, image and button

            Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);

                    ApplicationManager.getInstance().setPipette_name(editText.getText().toString());

                    dialog.dismiss();
                    onResume();


                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
});


        buttonPipettenumber =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_pipettenumber);
        buttonPipettenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle(R.string.please_enter_the_pipette_number);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("");

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
                            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_number_edittext);

                        try{

                            ApplicationManager.getInstance().setPipette_number(Integer.parseInt(editText.getText().toString()));

                        }catch (NumberFormatException e){
                            ApplicationManager.getInstance().setPipette_number(0);
                        }

                            dialog.dismiss();
                            onResume();


                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        buttonNumberofSamples =  (Button) rootView.findViewById(R.id.application_settings_pipette_button_pipettenumberofsample);
        buttonNumberofSamples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_number);
                    dialog.setTitle(R.string.please_enter_the_number_of_samples);

                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("");

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
                            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_number_edittext);


                            try{
                            ApplicationManager.getInstance().getCurrentLibrary().setPipetteNumberofSamples(Integer.parseInt(editText.getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setPipetteNumberofSamples(0);
                            }

                            dialog.dismiss();
                            onResume();


                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });




        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {

        buttonNominal.setText(getString(R.string.nominal)+":\n"+ String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal())+" ml");

        buttonInaccuracy.setText(getString(R.string.inaccuracy)+":\n" +String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy())+ " %");
        buttonImprecision.setText(getString(R.string.imprecision)+":\n"+String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())+" %" );
        buttonWaterTemp.setText(getString(R.string.water_temp)+":\n"+String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp())+ " °C");
        buttonPressure.setText(getString(R.string.pressure)+":\n"+String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure())+" ATM");

        buttonPipettename.setText(getString(R.string.pipette_name)+":\n" +ApplicationManager.getInstance().getPipette_name());
        buttonPipettenumber.setText(getString(R.string.pipette_number)+ ":\n"+ApplicationManager.getInstance().getPipette_number());

        buttonNumberofSamples.setText(getString(R.string.number_of_samples)+ ":\n"+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples());
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();

    }




}
