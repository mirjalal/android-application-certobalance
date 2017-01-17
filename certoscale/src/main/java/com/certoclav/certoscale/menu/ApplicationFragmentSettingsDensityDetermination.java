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

import java.io.IOException;
import java.text.ParseException;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentSettingsDensityDetermination extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonWaterTemp = null;
    private Button buttonLiquid=null;
    private Button buttonSinkerVolume=null;










    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_density_determination,container, false);
        buttonWaterTemp =  (Button) rootView.findViewById(R.id.application_settings_density_button_water_temp);
        buttonWaterTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle("Please enter the water temperature");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("°C");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setDensityWaterTemp(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setDensityWaterTemp(0);
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

        buttonLiquid =  (Button) rootView.findViewById(R.id.application_settings_density_button_liquid_density);
        buttonLiquid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle("Please enter the liquid density");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("°C");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setDensityLiquidDensity(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setDensityLiquidDensity(0);
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

        buttonSinkerVolume=  (Button) rootView.findViewById(R.id.application_settings_density_button_Sinker_Volume);
        buttonSinkerVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle("Please enter the Sinker Volume");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("°C");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setSinkerVolume(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setSinkerVolume(0);
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String densityliquidtype = prefs.getString(getString(R.string.preferences_density_liquidtyp),"");
        String densitymode = prefs.getString(getString(R.string.preferences_density_mode),"");

        if (densitymode.equals("1")) {

            if (densityliquidtype.equals("1")) {
                buttonLiquid.setVisibility(View.GONE);
                buttonWaterTemp.setVisibility(View.VISIBLE);
                buttonSinkerVolume.setVisibility(View.GONE);
            } else {
                buttonLiquid.setVisibility(View.VISIBLE);
                buttonWaterTemp.setVisibility(View.GONE);
                buttonSinkerVolume.setVisibility(View.GONE);
            }
        }else{
            buttonLiquid.setVisibility(View.GONE);
            buttonWaterTemp.setVisibility(View.GONE);
            buttonSinkerVolume.setVisibility(View.VISIBLE);

        }


        buttonWaterTemp.setText("Water Temperature\n"+ String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp())+" °C" );

        buttonLiquid.setText(("Liquid Density\n"+ String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity())+" g/cm³" ));

        buttonSinkerVolume.setText("Sinker Volume\n"+ String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getSinkerVolume())+" ml" );
        //buttonWaterTemp.setText("Water Temperature\n"+ String.format("%.8f",ApplicationManager.getInstance().WaterTempInDensity(30.5))+" °C" );
        super.onResume();




    }

    @Override
    public void onPause() {
        super.onPause();

    }




}
