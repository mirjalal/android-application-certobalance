package com.certoclav.certoscale.settings.communication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleModel;
import com.certoclav.certoscale.model.ScaleModelAEAdam;
import com.certoclav.certoscale.model.ScaleModelDandT;
import com.certoclav.certoscale.model.ScaleModelGandG;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;


public class SettingsCommunicationFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
private SharedPreferences prefs = null;

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_communication);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setSummary(getString(R.string.assigned_to_com) +" 2, "+Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(),"Protocol").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setSummary(getString(R.string.assigned_to_com) +" 3, "+Scale.getInstance().getSerialsServiceLabelPrinter().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(),"Label").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) +" 1, "+Scale.getInstance().getSerialsServiceSics().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(),"SICS").commit();


                return false;
            }
        });




    }






    @Override
    public void onResume() {

        Preference devicePref = findPreference("preferences_communication_list_devices");
        devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(prefs.getString("preferences_communication_list_devices", "1"))-1]);

        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setSummary(getString(R.string.assigned_to_com) +" 1, "+Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");



        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        super.onResume();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "preferences_communication_list_devices":

                Preference devicePref = findPreference(key);
                devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(sharedPreferences.getString(key, "1"))-1]);


                String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (modelValue) {
                    case "1":
                        ScaleModelGandG modelGandG=new ScaleModelGandG();
                        Scale.getInstance().setScaleModel((ScaleModel)modelGandG);
                        Scale.getInstance().getScaleModel().initializeParameters(600,1,1,9600,8,0,1,false);
                      //  Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());
                        break;

                    case "2":
                        ScaleModelDandT modelDandT=new ScaleModelDandT();
                        Scale.getInstance().setScaleModel((ScaleModel)modelDandT);
                        Scale.getInstance().getScaleModel().initializeParameters(120,4,2,9600,8,0,1,true);
                        Scale.getInstance().getScaleModel().pressZero();
                        Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());
                        //Scale.getInstance().getSerialsServiceScale().resetConnection();
                        //Scale.getInstance().getSerialsServiceScale().startReadSerialThread();
                        break;

                    case "3":
                        ScaleModelAEAdam modelAEAdam=new ScaleModelAEAdam();
                        Scale.getInstance().setScaleModel((ScaleModel)modelAEAdam);
                        Scale.getInstance().getScaleModel().initializeParameters(600,2,2,4800,8,0,1,false);
                        Scale.getInstance().getScaleModel().pressZero();
                        Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());

                        //Scale.getInstance().getSerialsServiceScale().resetConnection();
                        //Scale.getInstance().getSerialsServiceScale().startReadSerialThread();




                        break;
                }



        }
    }
}