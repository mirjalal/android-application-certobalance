package com.certoclav.certoscale.settings.communication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleModel;
import com.certoclav.certoscale.model.ScaleModelAEAdam;
import com.certoclav.certoscale.model.ScaleModelDandT;
import com.certoclav.certoscale.model.ScaleModelGandG;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.certocloud.SocketService;

import android_serialport_api.SerialService;


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


        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {



                return false;
            }
        });



        ((Preference) findPreference(getString(R.string.preferences_communication_list_baudrate))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.these_changes_will_be_applied_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });


        ((Preference) findPreference(getString(R.string.preferences_communication_list_transmission_standard))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.these_changes_will_be_applied_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });
        ((Preference) findPreference(getString(R.string.preferences_communication_list_handshake))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.these_changes_will_be_applied_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });



        SerialService serialService=Scale.getInstance().getSerialsServiceSics();

        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) +"1, 9600 baud, 8 data bits, parity: none, 1 stop bit, flow control: none");


        ((Preference) findPreference(getString(R.string.preferences_communication_list_baudrate))).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
/*
                String key = getString(R.string.preferences_communication_list_baudrate);
                String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "");
                int baudrate=9600;
                switch (modelValue) {
                    case "1":
                        baudrate=2400;
                        break;

                    case "2":
                        baudrate=4800;
                        break;

                    case "3":
                        baudrate=9600;
                        break;
                    case "4":
                        baudrate=19200;
                        break;
                    case "5":
                        baudrate=38400;
                        break;
                }


                Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(baudrate);
                Scale.getInstance().getSerialsServiceLabelPrinter().resetConnection();

                Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(baudrate);
                Scale.getInstance().getSerialsServiceProtocolPrinter().resetConnection();
*/
                return true;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_socket_connected))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.these_changes_will_be_applied_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });

    }






    @Override
    public void onResume() {




        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);








//        devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(prefs.getString(key, ""))-1]);

        //INIT DEVICE SETTING
        String key = "preferences_communication_list_devices";
        Preference devicePref = findPreference(key);
        key = getActivity().getString(R.string.preferences_communication_socket_connected);
        devicePref = findPreference(key);
        String summary = "";
        if( SocketService.getInstance().getSocket().connected()){
            summary = getString(R.string.connected);
        }else{
            summary = getString(R.string.not_connected);
        }
//        devicePref.setSummary(summary);


        super.onResume();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "preferences_communication_list_devices":

                Preference devicePref = findPreference(key);
                devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(sharedPreferences.getString(key, ""))-1]);


                String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "");
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