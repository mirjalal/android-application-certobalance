package com.certoclav.certoscale.settings.communication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleModel;
import com.certoclav.certoscale.model.ScaleModelAEAdam;
import com.certoclav.certoscale.model.ScaleModelDandT;
import com.certoclav.certoscale.model.ScaleModelGandG;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;

import android_serialport_api.SerialService;

import static com.certoclav.certoscale.R.id.navigationbar;


public class SettingsCommunicationFragmentEditConnection extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
private SharedPreferences prefs = null;

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String tag=getActivity().getSupportFragmentManager().findFragmentById(R.id.settings_unit_container_fragment).getTag();
        if (tag=="Protocol") {
            addPreferencesFromResource(R.xml.preferences_communication_detail_protocol_printer);

        }
        if (tag=="Label"){
            addPreferencesFromResource(R.xml.preferences_communication_detail_label_printer);
        }
        if(tag=="SICS"){
            addPreferencesFromResource(R.xml.preferences_communication_detail_lims);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());





    }






    @Override
    public void onResume() {

        //Preference devicePref = findPreference("preferences_communication_list_baudrate_protocol");



        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        super.onResume();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        switch (key){

            case "preferences_communication_list_baudrate_protocol":

                //Preference devicePref = findPreference(key);
                //devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(sharedPreferences.getString(key, "1"))-1]);


                String baudProtocol = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "3");
                SerialService serialService;
                switch (baudProtocol) {
                    case "1":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(2400);
                       // serialService =new SerialService("/dev/ttymxc0",2400);
                       // Scale.getInstance().setSerialsServiceProtocolPrinter(serialService);
                        break;
                    case "2":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(4800);
                        //serialService =new SerialService("/dev/ttymxc0",4800);
                        //Scale.getInstance().setSerialsServiceProtocolPrinter(serialService);
                        break;
                    case "3":

                        Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(9600);
                        //serialService =new SerialService("/dev/ttymxc0",9600);
                        //Scale.getInstance().setSerialsServiceProtocolPrinter(serialService);
                        break;
                    case "4":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(19200);
                        //serialService =new SerialService("/dev/ttymxc0",19200);
                        //Scale.getInstance().setSerialsServiceProtocolPrinter(serialService);
                        break;
                    case "5":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(38400);
                        //serialService =new SerialService("/dev/ttymxc0",38400);
                        //Scale.getInstance().setSerialsServiceProtocolPrinter(serialService);
                        break;
                }
            break;

            case "preferences_communication_list_baudrate_label":

                //Preference devicePref = findPreference(key);
                //devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(sharedPreferences.getString(key, "1"))-1]);


                String baudLabel = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "3");
                switch (baudLabel) {
                    case "1":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(2400);
                        serialService = new SerialService("/dev/ttymxc1",2400);//COM2
                        Scale.getInstance().setSerialsServiceLabelPrinter(serialService);
                        break;
                    case "2":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(4800);
                        serialService = new SerialService("/dev/ttymxc1",4800);//COM2
                        Scale.getInstance().setSerialsServiceLabelPrinter(serialService);
                        break;
                    case "3":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(9600);
                        serialService = new SerialService("/dev/ttymxc1",9600);//COM2
                        Scale.getInstance().setSerialsServiceLabelPrinter(serialService);
                        break;
                    case "4":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(19200);
                        serialService = new SerialService("/dev/ttymxc1",19200);//COM2
                        Scale.getInstance().setSerialsServiceLabelPrinter(serialService);
                        break;
                    case "5":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(38400);
                        serialService = new SerialService("/dev/ttymxc1",38400);//COM2
                        Scale.getInstance().setSerialsServiceLabelPrinter(serialService);
                        break;
                }

            break;


            case "preferences_communication_list_baudrate_lims":

                //Preference devicePref = findPreference(key);
                //devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(sharedPreferences.getString(key, "1"))-1]);


                String baudLims = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "3");
                switch (baudLims) {
                    case "1":
                        Scale.getInstance().getSerialsServiceSics().setBaudrate(2400);
                        serialService = new SerialService("/dev/ttymxc2",2400);//COM3
                        Scale.getInstance().setSerialsServiceSics(serialService);
                        break;
                    case "2":
                        Scale.getInstance().getSerialsServiceSics().setBaudrate(4800);
                        serialService = new SerialService("/dev/ttymxc2",4800);//COM3
                        Scale.getInstance().setSerialsServiceSics(serialService);
                        break;
                    case "3":
                        Scale.getInstance().getSerialsServiceSics().setBaudrate(9600);
                        serialService = new SerialService("/dev/ttymxc2",9600);//COM3
                        Scale.getInstance().setSerialsServiceSics(serialService);
                        break;
                    case "4":
                        Scale.getInstance().getSerialsServiceSics().setBaudrate(19200);
                        serialService = new SerialService("/dev/ttymxc2",19200);//COM3
                        Scale.getInstance().setSerialsServiceSics(serialService);
                        break;
                    case "5":
                        Scale.getInstance().getSerialsServiceSics().setBaudrate(38400);
                        serialService = new SerialService("/dev/ttymxc2",38400);//COM3
                        Scale.getInstance().setSerialsServiceSics(serialService);
                        break;
                }


            break;


            case "preferences_communication_list_transmission_standard_protocol":
                String transmissionProtocol = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (transmissionProtocol) {
                    case "1":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(2);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(1);
                        break;

                    case "2":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(1);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(1);
                        break;

                    case "3":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(1);
                        break;

                    case "4":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(8);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(1);
                        break;
                    case "5":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(2);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(2);
                        break;
                    case "6":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(1);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(2);
                        break;
                    case "7":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(2);
                        break;
                    case "8":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(8);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(2);
                        break;

                }

            break;

            case "preferences_communication_list_transmission_standard_label":
                String transmissionLabel = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (transmissionLabel) {

                    case "1":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(2);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(1);
                        break;

                    case "2":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(1);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(1);
                        break;

                    case "3":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(1);
                        break;

                    case "4":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(8);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(1);
                        break;
                    case "5":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(2);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(2);
                        break;
                    case "6":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(1);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(2);
                        break;
                    case "7":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(2);
                        break;
                    case "8":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(8);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(0);
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(2);
                        break;

                }

                break;


            case "preferences_communication_list_transmission_standard_lims":
                String transmissionLims = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (transmissionLims) {

                    case "1":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceSics().setmParity(2);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(1);
                        break;

                    case "2":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceSics().setmParity(1);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(1);
                        break;

                    case "3":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceSics().setmParity(0);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(1);
                        break;

                    case "4":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(8);
                        Scale.getInstance().getSerialsServiceSics().setmParity(0);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(1);
                        break;
                    case "5":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceSics().setmParity(2);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(2);
                        break;
                    case "6":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceSics().setmParity(1);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(2);
                        break;
                    case "7":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(7);
                        Scale.getInstance().getSerialsServiceSics().setmParity(0);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(2);
                        break;
                    case "8":
                        Scale.getInstance().getSerialsServiceSics().setmDatabits(8);
                        Scale.getInstance().getSerialsServiceSics().setmParity(0);
                        Scale.getInstance().getSerialsServiceSics().setmStopbits(2);
                        break;

                }

                break;


            case "preferences_communication_list_handshake_protocol":
                String flowControlProtocol = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (flowControlProtocol) {
                    case "1":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmFlowControl(0);
                        break;
                    case "2":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmFlowControl(1);
                        break;
                    case "3":
                        Scale.getInstance().getSerialsServiceProtocolPrinter().setmFlowControl(2);
                        break;

                }
                break;

            case "preferences_communication_list_handshake_label":
                String flowControlLabel = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (flowControlLabel) {
                    case "1":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmFlowControl(0);
                        break;
                    case "2":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmFlowControl(1);
                        break;
                    case "3":
                        Scale.getInstance().getSerialsServiceLabelPrinter().setmFlowControl(2);
                        break;

                }
                break;

            case "preferences_communication_list_handshake_lims":
                String flowControlLims = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
                switch (flowControlLims) {
                    case "1":
                        Scale.getInstance().getSerialsServiceSics().setmFlowControl(0);
                        break;
                    case "2":
                        Scale.getInstance().getSerialsServiceSics().setmFlowControl(1);
                        break;
                    case "3":
                        Scale.getInstance().getSerialsServiceSics().setmFlowControl(2);
                        break;

                }
                break;


        }





        SerialService serialServiceProtocol = new SerialService("/dev/ttymxc0",Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate(),
                Scale.getInstance().getSerialsServiceProtocolPrinter().getmDatabits(),
                Scale.getInstance().getSerialsServiceProtocolPrinter().getmStopbits(),
                Scale.getInstance().getSerialsServiceProtocolPrinter().getmParity(),
                Scale.getInstance().getSerialsServiceProtocolPrinter().getmFlowControl()
        );
        Scale.getInstance().setSerialsServiceProtocolPrinter(serialServiceProtocol);

    /*
        SerialService serialServiceLabel = new SerialService("/dev/ttymxc1",Scale.getInstance().getSerialsServiceLabelPrinter().getBaudrate(),
                Scale.getInstance().getSerialsServiceLabelPrinter().getmDatabits(),
                Scale.getInstance().getSerialsServiceLabelPrinter().getmStopbits(),
                Scale.getInstance().getSerialsServiceLabelPrinter().getmParity(),
                Scale.getInstance().getSerialsServiceLabelPrinter().getmFlowControl()
        );
        Scale.getInstance().setSerialsServiceLabelPrinter(serialServiceLabel);


        SerialService serialServiceSics = new SerialService("/dev/ttymxc2",Scale.getInstance().getSerialsServiceSics().getBaudrate(),
                Scale.getInstance().getSerialsServiceSics().getmDatabits(),
                Scale.getInstance().getSerialsServiceSics().getmStopbits(),
                Scale.getInstance().getSerialsServiceSics().getmParity(),
                Scale.getInstance().getSerialsServiceSics().getmFlowControl()
        );
        Scale.getInstance().setSerialsServiceSics(serialServiceSics);
        */
    }
}