package com.certoclav.certoscale.settings;
import android.os.Bundle;

import com.certoclav.certoscale.R;

/**
 * Created by Enrico on 06.12.2016.
 */



public class SettingsPercentWeighing extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_percent_weighing);
    }




}
