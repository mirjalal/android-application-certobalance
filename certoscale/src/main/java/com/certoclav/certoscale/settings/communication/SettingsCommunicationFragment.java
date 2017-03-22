package com.certoclav.certoscale.settings.communication;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.certoscale.settings.device.SettingsLanguagePickerActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.ESCPos;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.certocloud.SocketService;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Calendar;

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

        SerialService serialService=Scale.getInstance().getSerialsServiceSics();

        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) +"1, 9600 baud, 8 data bits, parity: none, 1 stop bit, flow control: none");



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
            summary = "connected";
        }else{
            summary = "not connected";
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



        }
    }
}