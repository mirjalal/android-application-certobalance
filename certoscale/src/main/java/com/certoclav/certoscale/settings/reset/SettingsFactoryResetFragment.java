package com.certoclav.certoscale.settings.reset;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.menu.LoginActivity;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.certoscale.settings.communication.SettingsCommunicationFragmentEditConnection;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.bcrypt.BCrypt;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.DeleteTask;

import java.util.Date;


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


        ((Preference) findPreference(getString(R.string.preferences_reset_user_settings))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.user_settings)  );
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatabaseService databaseService = new DatabaseService(getContext());

                            for (User user:databaseService.getUsers()){
                                databaseService.deleteUser(user);
                            }
                            User user1 = new User("Admin", "", "","Admin", "", "", "","", "", BCrypt.hashpw("admin",BCrypt.gensalt()), new Date(), true,true);
                            databaseService.insertUser(user1);

                            Toast.makeText(getActivity(), R.string.reset_successful, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });




        ((Preference) findPreference(getString(R.string.preferences_reset_device_settings))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.device_settings)  );
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
/*
                            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
                            prefs.edit().remove(getResources().getString(R.string.preferences_glp_header)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_header)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_date)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_balance_id)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_glp_balance_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_balance_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_glp_project_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_project_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_application_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_signature)).apply();

                            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences_glp, true);

*/
                            Toast.makeText(getActivity(), R.string.reset_successful, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });


        ((Preference) findPreference(getString(R.string.preferences_reset_application))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.application_settings)  );
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Toast.makeText(getActivity(), R.string.reset_successful, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });



        ((Preference) findPreference(getString(R.string.preferences_reset_units))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.weighing_units)  );
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatabaseService databaseService = new DatabaseService(getContext());

                            for (Unit unit:databaseService.getUnits()){
                                databaseService.deleteUnit(unit);
                            }


                            Unit unit = new Unit(0d,1d,"gram",Unit.UNIT_GRAM,"",true,false); // 1g = 1*10^0 g
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,0.35274d,"Unze",Unit.UNIT_OUNCE,"",true,false); //1g = 35274*10^-5 oz
                            databaseService.insertUnit(unit);
                            unit = new Unit(-3d,1d,"kilogram",Unit.UNIT_KILOGRAM,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(3d,1d,"milligram",Unit.UNIT_MILLIGRAM,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-3d,0.157473d,"stone",Unit.UNIT_STONE,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-2d,0.220462d,"pound",Unit.UNIT_POUND,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(1d,0.5d,"metric carat",Unit.UNIT_METRIC_CARAT,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-1d,0.32150746d,"ounce troy",Unit.UNIT_OUNCE_TROY,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,0.643015d,"pennyweight",Unit.UNIT_PENNYWEIGHT,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(2d,0.154324d,"grain",Unit.UNIT_GRAIN,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-2d,0.980665d,"Newton",Unit.UNIT_NEWTON,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,0.264555d,"momme",Unit.UNIT_MOMME,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,0.217391304d,"mesghal",Unit.UNIT_MESGHAL,"",true,false); //nochmal überprüfen
                            databaseService.insertUnit(unit);
                            unit = new Unit(-1d,0.26659557d,"Tael (HK)",Unit.UNIT_TAEL_HK,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-1d,0.26455470d,"Tael (SG)",Unit.UNIT_TAEL_SG,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-1d,0.60975609d,"tical (Asia)",Unit.UNIT_TICAL_ASIA,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-1d,0.85735260d,"tola",Unit.UNIT_TOLA,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(-1d,0.66666666d,"baht (Thailand)",Unit.UNIT_BAHT,"",true,false);
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,1d,"Custom unit 1","c1","",true,true);
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,1d,"Custom unit 2","c2","",true,true);
                            databaseService.insertUnit(unit);
                            unit = new Unit(0d,1d,"Custom unit 3","c3","",true,true);
                            databaseService.insertUnit(unit);

                            Toast.makeText(getActivity(), R.string.reset_successful, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_reset_glp))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.glp_and_gmp_data)  );
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
                            prefs.edit().remove(getResources().getString(R.string.preferences_glp_header)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_header)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_date)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_balance_id)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_glp_balance_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_balance_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_glp_project_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_project_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_application_name)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_print_signature)).apply();

                            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences_glp, true);

                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    Toast.makeText(getActivity(), R.string.reset_successful, Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });

        ((Preference) findPreference(getString(R.string.preferences_reset_communication))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.communicaton_settings)  );
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getActivity(), R.string.reset_successful, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });


        ((Preference) findPreference(getString(R.string.preferences_reset_complete))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                try
                {



                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getContext().getString(R.string.factory_reset));

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(R.string.do_you_really_want_to_perform_a_complete_factory_reset);
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                            // closing Entire Application
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("clear_cache", Context.MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            ApplicationController.getInstance().clearApplicationData();

                            //android.os.Process.killProcess(android.os.Process.myPid());


                            //User will be logged out to avoid bugs
                            Scale.getInstance().setScaleState(ScaleState.ON_AND_MODE_GRAM);
                            Intent intent = new Intent( getContext() ,LoginActivity.class);
                            startActivity(intent);

                            getActivity().finish();
                            android.os.Process.killProcess(android.os.Process.myPid());


                        }
                    });

                    dialog.show();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });




    }






    @Override
    public void onResume() {
        super.onResume();
    }



}