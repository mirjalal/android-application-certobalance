package com.certoclav.certoscale.settings.reset;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;


public class SettingsFactoryResetFragment extends PreferenceFragment {
private SharedPreferences prefs = null;

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_factory_reset);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_factory_reset), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_factory_reset))==true) {
            Toast.makeText(getContext(), R.string.these_settings_are_locked_by_the_admin, Toast.LENGTH_SHORT).show();
            getPreferenceScreen().setEnabled(false);
        }else{
            getPreferenceScreen().setEnabled(true);
        }




    }






    @Override
    public void onResume() {
        super.onResume();
    }



}