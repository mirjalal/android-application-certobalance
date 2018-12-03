package com.certoclav.certoscale.settings.communication;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleModelManager;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.certoscale.util.FTPManager;
import com.certoclav.library.application.ApplicationController;


public class SettingsCommunicationFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
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

        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setSummary(getString(R.string.assigned_to_com) + " 1, " + Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate() + " baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(), "Protocol").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setSummary(getString(R.string.assigned_to_com) + " 2, " + Scale.getInstance().getSerialsServiceLabelPrinter().getBaudrate() + " baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(), "Label").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) + " 3, " + Scale.getInstance().getSerialsServiceSics().getBaudrate() + " baud, 8 data bits, parity: none, 1 stop bit, flow control: none");
        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragmentEditConnection(), "SICS").commit();


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_communication_lims_ftp))).setSummary(
                getString(R.string.ftp_summary, FTPManager.getInstance().getAddress(),FTPManager.getInstance().getPort(), FTPManager.getInstance().getUsername()));
        ((Preference) findPreference(getString(R.string.preferences_communication_lims_ftp))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showsFtpInfoDialog();
                return false;
            }
        });


        ((Preference) findPreference(getString(R.string.preferences_communication_local_ftp))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
//                    Intent shareIntent = new Intent(Intent.ACTION_VIEW);
//                    shareIntent.setType("*/*");
//                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()
//                            + "/IFP_ILIMS/")));
//                    shareIntent.setPackage("com.estrongs.android.pop");
//
//                    startActivity(shareIntent);


                    Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/IFP_ILIMS/");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(selectedUri, "resource/folder");
                    intent.setPackage("com.estrongs.android.pop");
                    startActivity(intent);

//            com.estrongs.android.ui.preference.FtpServerPreference
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Please install ES FTP to use FTP features",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }


    private void showsFtpInfoDialog() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_ftp);
            dialog.setCancelable(false);
            final EditText editTextServer = dialog.findViewById(R.id.dialog_edit_text_address);
            final EditText editTextPort = dialog.findViewById(R.id.dialog_edit_text_port);
            final EditText editTextUsername = dialog.findViewById(R.id.dialog_edit_text_username);
            final EditText editTextPassword = dialog.findViewById(R.id.dialog_edit_text_password);
            final EditText editTextRawDataFolder = dialog.findViewById(R.id.dialog_edit_text_raw_data_folder);
            final EditText editTextRawILIMSFolder = dialog.findViewById(R.id.dialog_edit_text_ilims_folder);
            final TextView textViewResult = dialog.findViewById(R.id.dialog_text_view_test_result);

            editTextServer.setText(FTPManager.getInstance().getAddress());
            editTextPort.setText(FTPManager.getInstance().getPort() + "");
            editTextUsername.setText(FTPManager.getInstance().getUsername());
            editTextPassword.setText(FTPManager.getInstance().getPassword());
            editTextRawDataFolder.setText(FTPManager.getInstance().getFolder());
            editTextRawILIMSFolder.setText(FTPManager.getInstance().getFolderIlims());

            final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);

            final Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final Button dialogButtonTest = (Button) dialog.findViewById(R.id.dialog_edit_text_button_test);
            dialogButtonTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Integer.valueOf(editTextPort.getText().toString());
                    } catch (Exception e) {
                        editTextPort.setText("");
                    }
                    dialogButtonNo.setEnabled(false);
                    dialogButtonTest.setEnabled(false);
                    dialogButton.setEnabled(false);
                    textViewResult.setVisibility(View.VISIBLE);
                    textViewResult.setTextColor(Color.BLUE);
                    textViewResult.setText(getString(R.string.connecting));
                    FTPManager.getInstance().testConnection(editTextServer.getText().toString(),
                            editTextPort.getText().length() == 0 ? 21 : Integer.valueOf(editTextPort.getText().toString()),
                            editTextUsername.getText().toString(),
                            editTextPassword.getText().toString(), new FTPManager.FTPListener() {
                                @Override
                                public void onConnection(final boolean isConnected, final String message) {
                                    try {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialogButtonNo.setEnabled(true);
                                                dialogButtonTest.setEnabled(true);
                                                dialogButton.setEnabled(true);
                                                textViewResult.setVisibility(View.VISIBLE);
                                                if (isConnected) {
                                                    textViewResult.setTextColor(Color.GREEN);
                                                    textViewResult.setText(getString(R.string.success));
                                                } else {
                                                    textViewResult.setTextColor(Color.RED);
                                                    textViewResult.setText(getString(R.string.fail));
                                                    if (message != null && message.length() > 0)
                                                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }catch (Exception e){
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onUpdated() {

                                }

                                @Override
                                public void onUploading(boolean isUploaded, String message) {

                                }
                            });
                }
            });

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Integer.valueOf(editTextPort.getText().toString());
                    } catch (Exception e) {
                        editTextPort.setText("");
                    }
                    FTPManager.getInstance().saveFTPInformation(editTextServer.getText().toString(),
                            editTextPort.getText().length() == 0 ? 21 : Integer.valueOf(editTextPort.getText().toString()),
                            editTextUsername.getText().toString(),
                            editTextPassword.getText().toString(),
                            editTextRawDataFolder.getText().toString(),
                            editTextRawILIMSFolder.getText().toString());
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {


        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_communication), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_communication)) == true) {
            Toast.makeText(getContext(), R.string.these_settings_are_locked_by_the_admin, Toast.LENGTH_SHORT).show();
            getPreferenceScreen().setEnabled(false);
        } else {
            getPreferenceScreen().setEnabled(true);
        }


        Preference devicePref = findPreference("preferences_communication_list_devices");
        devicePref.setSummary(getResources().getStringArray(R.array.preferences_communication_string_array_devices)[Integer.parseInt(prefs.getString("preferences_communication_list_devices", "1")) - 1]);

        ((Preference) findPreference(getString(R.string.preferences_communication_protocolprinter))).setSummary(getString(R.string.assigned_to_com) + " 1, " + Scale.getInstance().getSerialsServiceProtocolPrinter().getBaudrate() + " baud, " + Scale.getInstance().getSerialsServiceProtocolPrinter().getmDatabits() + " data bits, parity: " + getParityString(Scale.getInstance().getSerialsServiceProtocolPrinter().getmParity()) + " , " + Scale.getInstance().getSerialsServiceProtocolPrinter().getmStopbits() + " stop bit, flow control: " + getFlowControlString(Scale.getInstance().getSerialsServiceProtocolPrinter().getmFlowControl()));
        ((Preference) findPreference(getString(R.string.preferences_communication_lims))).setSummary(getString(R.string.assigned_to_com) + " 2, " + Scale.getInstance().getSerialsServiceSics().getBaudrate() + " baud, " + Scale.getInstance().getSerialsServiceSics().getmDatabits() + "data bits, parity: " + getParityString(Scale.getInstance().getSerialsServiceSics().getmParity()) + " , " + Scale.getInstance().getSerialsServiceSics().getmStopbits() + " stop bit, flow control: " + getFlowControlString(Scale.getInstance().getSerialsServiceSics().getmFlowControl()));
        ((Preference) findPreference(getString(R.string.preferences_communication_labelprinter))).setSummary(getString(R.string.assigned_to_com) + " 3, " + Scale.getInstance().getSerialsServiceLabelPrinter().getBaudrate() + " baud, " + Scale.getInstance().getSerialsServiceLabelPrinter().getmDatabits() + " data bits, parity: " + getParityString(Scale.getInstance().getSerialsServiceLabelPrinter().getmParity()) + " , " + Scale.getInstance().getSerialsServiceLabelPrinter().getmStopbits() + " stop bit, flow control: " + getFlowControlString(Scale.getInstance().getSerialsServiceLabelPrinter().getmFlowControl()));

        ((Preference) findPreference(getString(R.string.preferences_communication_scanner))).setSummary(getString(R.string.assigned_to) + " USB host");
        ((Preference) findPreference(getString(R.string.preferences_communication_balance))).setSummary(getString(R.string.assigned_to_com) + " 4, " + Scale.getInstance().getSerialsServiceScale().getBaudrate() + " baud, " + Scale.getInstance().getSerialsServiceScale().getmDatabits() + " data bits, parity: " + getParityString(Scale.getInstance().getSerialsServiceScale().getmParity()) + " , " + Scale.getInstance().getSerialsServiceScale().getmStopbits() + " stop bit, flow control: " + getFlowControlString(Scale.getInstance().getSerialsServiceScale().getmFlowControl()));

        //Toast.makeText(getContext(), String.valueOf(Scale.getInstance().getSerialsServiceSics().getBaudrate()), Toast.LENGTH_SHORT).show();


        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        super.onResume();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "preferences_communication_list_devices":
                ScaleModelManager scaleModelManager = new ScaleModelManager();
                scaleModelManager.changeScaleModelAndRefreshComport();
        }
    }

    private String getParityString(int parity) {
        switch (parity) {
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

    private String getFlowControlString(int flowControl) {
        switch (flowControl) {
            case 0:
                return "none";
            case 1:
                return "Xon-Xoff";
            case 2:
                return "hardware";
            default:
                return "none";
        }
    }
}