package com.certoclav.certoscale.settings.unit;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.settings.application.PreferenceFragment;


public class SettingUnitFragment extends PreferenceFragment {
private SharedPreferences prefs = null;

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_unit);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

    }






    @Override
    public void onResume() {
        super.onResume();
    }



}