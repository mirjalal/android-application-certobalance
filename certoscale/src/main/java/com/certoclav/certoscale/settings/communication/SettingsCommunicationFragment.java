package com.certoclav.certoscale.settings.communication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleModelManager;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;


public class SettingsCommunicationFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
private SharedPreferences prefs = null;

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_communication);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setSummary(getString(R.string.assigned_to_com) +" 1, "+Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(),"Protocol").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setSummary(getString(R.string.assigned_to_com) +" 2, "+Scale.getInstance().getSerialsServiceLabelPrinter().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(),"Label").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) +" 3, "+Scale.getInstance().getSerialsServiceSics().getBaudrate()+" baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(),"SICS").commit();


                return false;
            }
        });




    }






    @Override
    public void onResume() {



        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_communication), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_communication))==true) {
            Toast.makeText(getContext(), R.string.these_settings_are_locked_by_the_admin, Toast.LENGTH_SHORT).show();
            getPreferenceScreen().setEnabled(false);
        }else{
            getPreferenceScreen().setEnabled(true);
        }


        Preference devicePref = findPreference("preferences_communication_list_devices");
        devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(prefs.getString("preferences_communication_list_devices", "1"))-1]);

        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setSummary(getString(R.string.assigned_to_com) +" 1, "+Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate()+ " baud, "+ Scale.getInstance().getSerialsServiceProtocolPrinter().getmDatabits()+" data bits, parity: "+getParityString(Scale.getInstance().getSerialsServiceProtocolPrinter().getmParity())+" , "+Scale.getInstance().getSerialsServiceProtocolPrinter().getmStopbits()+" stop bit, flow control: "+getFlowControlString(Scale.getInstance().getSerialsServiceProtocolPrinter().getmFlowControl()));
        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) +" 2, "+Scale.getInstance().getSerialsServiceSics().getBaudrate()+" baud, "+ Scale.getInstance().getSerialsServiceSics().getmDatabits()+"data bits, parity: "+getParityString(Scale.getInstance().getSerialsServiceSics().getmParity())+" , "+Scale.getInstance().getSerialsServiceSics().getmStopbits()+" stop bit, flow control: "+ getFlowControlString(Scale.getInstance().getSerialsServiceSics().getmFlowControl()));
        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setSummary(getString(R.string.assigned_to_com) +" 3, "+Scale.getInstance().getSerialsServiceLabelPrinter().getBaudrate()+" baud, "+ Scale.getInstance().getSerialsServiceLabelPrinter().getmDatabits()+ " data bits, parity: "+getParityString(Scale.getInstance().getSerialsServiceLabelPrinter().getmParity())+" , "+Scale.getInstance().getSerialsServiceLabelPrinter().getmStopbits()+" stop bit, flow control: "+ getFlowControlString(Scale.getInstance().getSerialsServiceLabelPrinter().getmFlowControl()));

        ((Preference) findPreference(getString(R.string.preferences_communication_scanner))).setSummary(getString(R.string.assigned_to) +" USB host");
        ((Preference) findPreference(getString(R.string.preferences_communication_balance))).setSummary(getString(R.string.assigned_to_com) +" 4, "+Scale.getInstance().getSerialsServiceScale().getBaudrate()+" baud, "+ Scale.getInstance().getSerialsServiceScale().getmDatabits()+ " data bits, parity: "+getParityString(Scale.getInstance().getSerialsServiceScale().getmParity())+" , "+Scale.getInstance().getSerialsServiceScale().getmStopbits()+" stop bit, flow control: "+ getFlowControlString(Scale.getInstance().getSerialsServiceScale().getmFlowControl()));

        //Toast.makeText(getContext(), String.valueOf(Scale.getInstance().getSerialsServiceSics().getBaudrate()), Toast.LENGTH_SHORT).show();


        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        super.onResume();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "preferences_communication_list_devices":
                ScaleModelManager scaleModelManager = new ScaleModelManager();
                scaleModelManager.changeScaleModelAndRefreshComport();
        }
    }

    private String getParityString (int parity){
        switch (parity){
            case 0:
                return "none";
            case 1:
                return "odd";
            case 2:
                return "even";
            default:
                return "none";
        }
    }

    private String getFlowControlString(int flowControl){
        switch (flowControl){
            case 0:
                return "none";
            case 1:
                return "Xon-Xoff" ;
            case 2:
                return"hardware";
            default:
                return "none";
        }
    }
}