package com.certoclav.certoscale.settings.communication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.certocloud.SocketService;


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



    }






    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        //INIT DEVICE SETTING
        String key = "preferences_communication_list_devices";


        Preference devicePref = findPreference(key);
//        devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(prefs.getString(key, ""))-1]);


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