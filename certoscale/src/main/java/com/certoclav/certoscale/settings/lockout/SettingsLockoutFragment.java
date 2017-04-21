package com.certoclav.certoscale.settings.lockout;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.application.PreferenceFragment;


public class SettingsLockoutFragment extends PreferenceFragment {
private SharedPreferences prefs = null;

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_lockout);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

       getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_calibration)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });


        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_user_settings)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });


        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_balance_setup)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });


        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_application_modes)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_weighing_units)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_glp)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_communication)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_library)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });


        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_io_settings)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.preferences_lockout_factory_reset)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(Scale.getInstance().getUser().getIsAdmin() == false) {
                    Toast.makeText(getContext(), R.string.only_the_admin_can_change_the_lockout_settings, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });


    }






    @Override
    public void onResume() {
        super.onResume();
    }



}