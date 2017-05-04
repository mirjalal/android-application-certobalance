package com.certoclav.certoscale.settings.reset;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;


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

                            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
                            prefs.edit().remove(getResources().getString(R.string.preferences_device_theme)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_device_bluetooth)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_device_snchronization)).apply();

                            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences_device, true);



                            /*
                            // Reset language
                            Resources res = getContext().getResources();
                            // Change locale settings in the app.
                            DisplayMetrics dm = res.getDisplayMetrics();
                            android.content.res.Configuration conf = res.getConfiguration();
                            conf.locale= Locale.US;
                            // Use conf.locale = new Locale(...) if targeting lower versions
                            res.updateConfiguration(conf, dm);

                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
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

                            //Weighing
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_minimum)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_auto_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_minimum_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_print_min)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_weigh_load_visible)).apply();

                            //PartCounting
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_auto_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_auto_optimization)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_apw_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_target_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_difference_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_over_limit_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_under_limit_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_print_apw)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_print_sample_size)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_print_target)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_print_difference)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_print_over_limit)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_counting_print_under_limit)).apply();

                            //Percent Weighing
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_reference_weight_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_difference_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_difference_percent_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_print_reference)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_print_difference)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_percent_print_difference_percent)).apply();

                            //Check Weighing
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_auto_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_over_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_under_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_target_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_overtolerance_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_undertolerance_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_print_target)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_print_overtolerance)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_print_undertolerance)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_print_overlimit)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_print_underlimit)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_limitmode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_check_displayoptions)).apply();


                            //Animal Weighing
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_auto_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_measuringtime_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_animal_print_measuring)).apply();


                            //Filling
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_auto_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_target_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_differencew_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_differencep_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_print_target)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_print_differencew)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_filling_print_differencep)).apply();

                            //Totalization
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_AutoSampleMode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalizaion_NumberofSamples)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_average_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_standarddeviation_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_minimum_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_maximum_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_range_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_total_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_current_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_print_samples)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_print_average)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_print_standard)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_print_minimum)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_totalization_print_maximum)).apply();


                            //Formulation
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_formulationmode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_filter)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_total_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_target_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_differencew_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_differencep_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_print_total)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_print_target)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_print_differencew)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_formulation_print_differencep)).apply();


                            //Differential
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_autoMode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_initial_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_final_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_differencew_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_differencep_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_print_initial)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_print_final)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_print_differencew)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_differential_print_differencep)).apply();


                            //Density Determination
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_liquidtyp)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_porous)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_autosample)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_denisty_Liquid_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_watertemp_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_oildensity_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_weightair_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_oiledweight_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_weightliquid_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_sinkervolume_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_porous_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_liquidtype_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_liquiddensity_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_watertemp_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_weightair_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_density_weightinliquid_print)).apply();

                            //Peak Hold
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_stableonly)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_peak_print_stableonly)).apply();


                            //Ingrediant Costing
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_unitcost_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_totalweight_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_totalcost_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_brutto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_netto_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_tara_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_print_totalweight)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_ingrediant_print_totalcost)).apply();


                            //Pipette Adjustment
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_autosamplemode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_liquidtype)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_liquidtype)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_presureunit)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_volumeunit)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_secondary_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_nominal_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_inacc_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_imprec_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_liquiddensity_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_barounit_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_watertemp_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_nominal_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_inacc_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_imprec_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_liquiddensity_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_barounit_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_pipette_watertemp_print)).apply();


                            //SQC
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_activated)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_lock)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_numberofsample)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_tolerance_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_mode)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_numbatches_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_numsamples_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_average_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_total_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_minimum_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_maximum_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_range_visible)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_numbatches_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_numsamples_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_average_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_total_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_minimum_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_maximum_print)).apply();
                            prefs.edit().remove(getResources().getString(R.string.preferences_statistic_range_print)).apply();



                            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences_device, true);


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
                    text.setText(getString(R.string.do_you_really_want_to_reset_the) + " " + getString(R.string.glp_and_gmp_data) +"?" );
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

                            Scale.getInstance().getSerialsServiceProtocolPrinter().setBaudrate(9600);
                            Scale.getInstance().getSerialsServiceProtocolPrinter().setmDatabits(8);
                            Scale.getInstance().getSerialsServiceProtocolPrinter().setmStopbits(1);
                            Scale.getInstance().getSerialsServiceProtocolPrinter().setmParity(0);
                            Scale.getInstance().getSerialsServiceProtocolPrinter().setmFlowControl(0);

                            Scale.getInstance().getSerialsServiceSics().setBaudrate(9600);
                            Scale.getInstance().getSerialsServiceSics().setmDatabits(8);
                            Scale.getInstance().getSerialsServiceSics().setmStopbits(1);
                            Scale.getInstance().getSerialsServiceSics().setmParity(0);
                            Scale.getInstance().getSerialsServiceSics().setmFlowControl(0);

                            Scale.getInstance().getSerialsServiceLabelPrinter().setBaudrate(9600);
                            Scale.getInstance().getSerialsServiceLabelPrinter().setmDatabits(8);
                            Scale.getInstance().getSerialsServiceLabelPrinter().setmStopbits(1);
                            Scale.getInstance().getSerialsServiceLabelPrinter().setmParity(0);
                            Scale.getInstance().getSerialsServiceLabelPrinter().setmFlowControl(0);


                            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
                            prefs.edit().remove(getResources().getString(R.string.preferences_communication_list_devices)).apply();


                            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences_communication, true);

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