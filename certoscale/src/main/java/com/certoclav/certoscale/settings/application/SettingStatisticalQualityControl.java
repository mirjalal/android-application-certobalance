package com.certoclav.certoscale.settings.application;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;

        import com.certoclav.certoscale.R;
        import com.certoclav.certoscale.model.Scale;

/**
 * Created by Enrico on 06.12.2016.
 */



public class SettingStatisticalQualityControl extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_statistical_quality_control);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getPreferenceScreen().findPreference(getString(R.string.preferences_statistic_lock)).setEnabled(Scale.getInstance().getUser().getIsAdmin());


    }




}