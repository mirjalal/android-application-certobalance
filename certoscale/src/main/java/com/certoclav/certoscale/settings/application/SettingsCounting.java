package com.certoclav.certoscale.settings.application;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.library.application.ApplicationController;


public class SettingsCounting extends PreferenceFragment  {
	


	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_parts_counting);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getPreferenceScreen().findPreference(getString(R.string.preferences_counting_lock)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    return false;
                }
                return true;
            }
        });



        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_application_modes), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_application_modes))==true) {
            Toast.makeText(getContext(), R.string.these_settings_are_locked_by_the_admin, Toast.LENGTH_SHORT).show();
            getPreferenceScreen().setEnabled(false);
        }else{
            getPreferenceScreen().setEnabled(true);
        }



    }

    

    
    












    
}