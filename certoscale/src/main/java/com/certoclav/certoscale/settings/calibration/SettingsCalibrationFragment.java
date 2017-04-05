package com.certoclav.certoscale.settings.calibration;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

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



        ((Preference) findPreference(getString(R.string.preferences_calibration_internal))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                    Toast.makeText(getActivity(), R.string.this_function_will_be_available_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_calibration_automatic))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.this_function_will_be_available_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });


        ((Preference) findPreference(getString(R.string.preferences_calibration_automatic))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.this_function_will_be_available_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });


        ((Preference) findPreference(getString(R.string.preferences_calibration_span))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Scale.getInstance().getScaleModel().externelCalibration(getActivity());

                return false;
            }
        });



        ((Preference) findPreference(getString(R.string.preferences_calibration_test))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Toast.makeText(getActivity(), R.string.this_function_will_be_available_in_the_next_release, Toast.LENGTH_LONG).show();

                return false;
            }
        });


    }






    @Override
    public void onResume() {
        super.onResume();
    }



}