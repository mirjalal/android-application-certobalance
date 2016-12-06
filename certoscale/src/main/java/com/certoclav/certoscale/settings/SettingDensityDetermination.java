package com.certoclav.certoscale.settings;



import android.os.Bundle;

import com.certoclav.certoscale.R;


public class SettingDensityDetermination extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_density_determination);


    }

}