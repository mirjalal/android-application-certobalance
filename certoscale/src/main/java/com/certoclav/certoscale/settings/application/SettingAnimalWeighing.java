package com.certoclav.certoscale.settings.application;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;


public class SettingAnimalWeighing extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_animal_weighing);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getPreferenceScreen().findPreference(getString(R.string.preferences_animal_lock)).setEnabled(Scale.getInstance().getUser().getIsAdmin());



    }

}