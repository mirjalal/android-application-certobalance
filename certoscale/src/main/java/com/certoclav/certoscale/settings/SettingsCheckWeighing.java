package com.certoclav.certoscale.settings;



import android.os.Bundle;

import com.certoclav.certoscale.R;


public class SettingsCheckWeighing extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_check_weighing);


    }


}