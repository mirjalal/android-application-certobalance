package com.certoclav.certoscale.settings.calibration;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.application.PreferenceFragment;


public class SettingsCalibrationFragment extends PreferenceFragment {
private SharedPreferences prefs = null;

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_calibration);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        ((Preference) findPreference(getString(R.string.preferences_calibration_user))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Scale.getInstance().getScaleModel().externelCalibration(getContext());

                return false;
            }
        });

    }






    @Override
    public void onResume() {
        super.onResume();
    }



}