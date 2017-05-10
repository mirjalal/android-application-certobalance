package com.certoclav.certoscale.settings.glp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;


public class SettingsGlpFragment extends PreferenceFragment  {
private SharedPreferences prefs = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_glp);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


    }






    @Override
    public void onResume() {
        super.onResume();

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_glp), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_glp))==true) {
            Toast.makeText(getContext(), R.string.these_settings_are_locked_by_the_admin, Toast.LENGTH_SHORT).show();
            getPreferenceScreen().setEnabled(false);
        }else{
            getPreferenceScreen().setEnabled(true);
        }

    }


    @Override
    public void onPause() {

        super.onPause();

    }

}