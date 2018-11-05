package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.UnitAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.certoclav.certoscale.constants.AppConstants.INTERNAL_TARA_ZERO_BUTTON;
import static com.certoclav.certoscale.constants.AppConstants.IS_IO_SIMULATED;


public class ApplicationFragmentWeight extends Fragment implements WeightListener, StableListener {
    private FrameLayout barload = null;
    private FrameLayout barloadbackground = null;
    private TextView textInstruction = null;
    private TextView textSum = null;
    private TextView textValue = null;
    private ImageView imageStable = null;
    private static final int WIDTH_LOADING_BAR_TOTAL = 700;
    private long ctime_first = 0;

    private double beakerWeight = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_weight_display, container, false);
        //Access to views from menu_main xml file
        barload = (FrameLayout) rootView.findViewById(R.id.menu_main_bar_load);
        barloadbackground = (FrameLayout) rootView.findViewById(R.id.menu_main_countdown_bar_background);


        textInstruction = (TextView) rootView.findViewById(R.id.menu_main_text_instruction);
        textSum = (TextView) rootView.findViewById(R.id.menu_main_text_information);
        textValue = (TextView) rootView.findViewById(R.id.menu_main_text_value);
        imageStable = (ImageView) rootView.findViewById(R.id.menu_main_image_stable);

        textValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_pick_unit);
                    dialog.setTitle(R.string.pick_a_unit);
                    ListView listView = (ListView) dialog.findViewById(R.id.dialog_pick_unit_list);
                    final UnitAdapter adapter = new UnitAdapter(getActivity(), new ArrayList<Unit>());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (adapter.getItem(position).getEnabled()) {
                                ApplicationManager.getInstance().setCurrentUnit(adapter.getItem(position));
                                dialog.dismiss();
                            }
                        }
                    });
                    DatabaseService db = new DatabaseService(getActivity());
                    List<Unit> units = db.getUnits();

                    adapter.clear();
                    if (units != null) {
                        Collections.sort(units, new Comparator<Unit>() {
                            @Override
                            public int compare(Unit unit1, Unit unit2) {
                                return unit1.getName().compareToIgnoreCase(unit2.getName());
                            }
                        });
                        for (Unit unit : units) {
                            if (unit.getEnabled()) {
                                adapter.add(unit);
                                adapter.setHideCheckbox(true);
                            }
                            Log.e("SettingsUnitFragment", "added unit");
                        }
                    }
                    adapter.notifyDataSetChanged();
                    Log.e("SettingsUnitFragment", "notifydatasetchanged" + adapter.getCount());

                    Button buttonClose = (Button) dialog.findViewById(R.id.dialog_pick_unit_close);
                    buttonClose.setText(getString(R.string.close));

                    buttonClose.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Scale.getInstance().isStable()) {
            imageStable.setVisibility(View.VISIBLE);
        } else {
            imageStable.setVisibility(View.INVISIBLE);
        }
        Scale.getInstance().setOnWeightListener(this);
        Scale.getInstance().setOnStableListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        Scale.getInstance().removeOnWeightListener(this);
        Scale.getInstance().removeOnStableListener(this);
    }


    @Override
    public void onWeightChanged(Double weight, String unit) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean loadingbarnormal = true;
        switch (Scale.getInstance().getScaleApplication()) {
            case ANIMAL_WEIGHING_CALCULATING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(R.string.calculating);
                textSum.setText(getString(R.string.net_weight).toUpperCase() + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                break;

            case PERCENT_WEIGHING_CALC_REFERENCE:
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                break;
            case ANIMAL_WEIGHING:
                textInstruction.setText(R.string.press_start_for_a_new_measuremnt);
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getAnimalWeightAsStringWithUnit());
                textSum.setText(getString(R.string.net_weight).toUpperCase() + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                break;
            case PART_COUNTING_CALC_AWP:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getAwpCalcSampleSize() + " pcs");
                textSum.setText(getString(R.string.tared_weight).toString().toUpperCase() + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());

                break;
            case PART_COUNTING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText(getString(R.string.tared_weight).toString().toUpperCase() + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                break;
            case FORMULATION:
                if (ApplicationManager.getInstance().getCurrentRecipe() == null) {
                    textInstruction.setText(R.string.click_settings_and_choose_a_recipe);
                } else {
                    textInstruction.setText(R.string.press_start_to_begin_recipe);
                }
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                break;

            case FORMULATION_FREE:

                textInstruction.setText(getString(R.string.press_start_to_begin_with_a_free_formulation));
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                break;


            case FORMULATION_RUNNING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                break;

            case FORMULATION_FREE_RUNNING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                break;


            case DIFFERENTIAL_WEIGHING:
                textInstruction.setText("");
            case WEIGHING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);

                if (ApplicationManager.getInstance().getTaredValueInGram() < ApplicationManager.getInstance().getUnderLimitValueInGram()) {
                    if (prefs.getBoolean(getString(R.string.preferences_weigh_minimum), getResources().getBoolean(R.bool.preferences_weigh_minimum)) == true) {
                        textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                        textValue.setTextColor(Color.YELLOW);
                    }
                }

                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case PERCENT_WEIGHING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);

                textValue.setText(ApplicationManager.getInstance().getPercent() + " %");
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case CHECK_WEIGHING:
                textInstruction.setText("");

                String cmode = prefs.getString(getString(R.string.preferences_check_displayoptions), "");
                String checklimitmode = prefs.getString(getString(R.string.preferences_check_limitmode), "");
                double current = ApplicationManager.getInstance().getTaredValueInGram();
                double under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                double over = ApplicationManager.getInstance().getOverLimitCheckWeighing();


                if (checklimitmode.equals("1")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                    over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
                }
                if (checklimitmode.equals("2") || checklimitmode.equals("3")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getCheckNominaldouble() - ApplicationManager.getInstance().getCheckNominalToleranceUnderdouble();
                    over = ApplicationManager.getInstance().getCheckNominaldouble() + ApplicationManager.getInstance().getCheckNominalToleranceOverdouble();
                }


                //if(cmode.equals("1")) {
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);


                if (current < under) {
                    textValue.setTextColor(Color.RED);
                    textValue.setText("↓   " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (current > over) {
                    textValue.setTextColor(Color.RED);
                    textValue.setText("↑   " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (current >= under && current <= over) {
                    textValue.setTextColor(Color.GREEN);
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }


                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                if (cmode.equals("2")) {
                    textInstruction.setText("");
                    if (current < under) {
                        textValue.setTextColor(Color.RED);
                        textValue.setText(R.string.value_too_low);
                    }

                    if (current > over) {
                        textValue.setTextColor(Color.RED);
                        textValue.setText(R.string.value_too_high);
                    }

                    if (current >= under && current <= over) {
                        textValue.setTextColor(Color.GREEN);
                        textValue.setText("OK");
                    }
                    textSum.setText(getString(R.string.net_weight).toUpperCase() + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }
                break;
            case DENSITY_DETERMINATION_STARTED:
            case DENSITY_DETERMINATION:
                textInstruction.setText("");

                String densityliquidtype = prefs.getString(getString(R.string.preferences_density_liquidtyp), "");
                String densitymode = prefs.getString(getString(R.string.preferences_density_mode), "");

                if (densityliquidtype.isEmpty()) {
                    densityliquidtype = "1";
                }

                if (densitymode.isEmpty()) {
                    densitymode = "1";
                }

                if (ApplicationManager.getInstance().getDensity_step_counter() == 0) {
                    textValue.setTextColor(Color.WHITE);
                    textValue.setText(R.string.press_start);

                }
                if (ApplicationManager.getInstance().getDensity_step_counter() == 1) {
                    textSum.setText("");
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    textInstruction.setText(R.string.weigh_sample_in_air);
                    if (densitymode.equals("2") || densitymode.equals("3")) {
                        textInstruction.setText(R.string.weigh_sinker_in_air);
                    }
                }

                if (ApplicationManager.getInstance().getDensity_step_counter() == 2) {
                    textSum.setText("");
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    //textSum.setTextColor(Color.YELLOW);
                    textInstruction.setText(R.string.weigh_sample_in_liquid);
                    if (densitymode.equals("2")) {
                        textInstruction.setText(R.string.weigh_sample_in_liquid_push);
                    }

                    if (densitymode.equals("3")) {
                        textInstruction.setText(R.string.weigh_sinker_in_liquid);
                    }
                }
                if (ApplicationManager.getInstance().getDensity_step_counter() == 4) {
                    textSum.setText("");
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    textInstruction.setText(R.string.weigh_sinker_in_liquid_and);

                }


                if (ApplicationManager.getInstance().getDensity_step_counter() == 3) {
                    double Sinkervolume = ApplicationManager.getInstance().getCurrentLibrary().getSinkerVolume();
                    textSum.setText("");

                    //Equation according to http://www.hs-lausitz.de/fileadmin/user_upload/public/fak/fak2/pdf/Physiklabor/M01_Dichtebestimmung.pdf
                    double density = 0;
                    double mk = ApplicationManager.getInstance().getDensity_weight_air();
                    double pf = 0;
                    double dm = ApplicationManager.getInstance().getDensity_weight_liquid();
                    if (densitymode.equals("1")) {
                        if (densityliquidtype.equals("1")) {
                            pf = ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp());
                        } else {
                            pf = ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity();
                        }
                        density = (mk * pf) / (mk - dm);
                    }

                    if (densitymode.equals("2")) {

                        //Equations according to Page 3 from http://www.hs-lausitz.de/fileadmin/user_upload/public/fak/fak2/pdf/Physiklabor/M01_Dichtebestimmung.pdf
                        if (densityliquidtype.equals("1")) {
                            pf = ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp());
                        } else {
                            pf = ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity();
                        }
                        double Vk = dm / pf;
                        density = mk / Vk;
                    }

                    if (densitymode.equals("3")) {

                        //Equations according http://www.kern-sohn.com/manuals/files/German/ABT-A01-BA-d-0710.pdf  Seite 9
                        density = (mk - dm) / Sinkervolume + 0.0012;

                    }

                    textValue.setText(String.format("%.4f", density) + " g/cm³");
                    ApplicationManager.getInstance().setDensity(density);
                    //textSum.setTextColor(Color.YELLOW);
                    if (densitymode.equals("1")) {
                        textInstruction.setText(R.string.density_calculated);
                    }
                    if (densitymode.equals("2")) {
                        textInstruction.setText(R.string.density_of_liquid_calculated);
                    }
                }
                break;

            case FILLING:
                textInstruction.setText("");
                textSum.setText(getString(R.string.fill_status) + " " + ApplicationManager.getInstance().getPercentFilling() + " %");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());

                //textInstruction.setText(getString(R.string.fill_status)+" "+ ApplicationManager.getInstance().getPercentFilling()+" %");

                loadingbarnormal = false;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) barload.getLayoutParams();
                FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) barloadbackground.getLayoutParams();
                params.height = 15;
                params2.height = 15;

                double filling_width = (ApplicationManager.getInstance().getTaredValueInGram() / ApplicationManager.getInstance().getTarget()) * WIDTH_LOADING_BAR_TOTAL;

                //double filling_width=(ApplicationManager.getInstance().getTarget()/ApplicationManager.getInstance().getTaredValueInGram())*700;

                if (filling_width < 0) {
                    filling_width = 0;
                }
                if (filling_width > 700) {
                    filling_width = 700;
                }

                params.width = (int) filling_width;
                barload.setLayoutParams(params);
                barloadbackground.setLayoutParams(params2);
                barload.setBackgroundColor(Color.CYAN);


                break;

            case FILLING_CALC_TARGET:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case TOTALIZATION:
                Boolean isAutoSampleMode = prefs.getBoolean(getString(R.string.preferences_totalization_AutoSampleMode), getResources().getBoolean(R.bool.preferences_totalization_AutoSampleMode));
                if (isAutoSampleMode) {
                    textInstruction.setText(R.string.automatic_sample_mode_on);
                } else {
                    textInstruction.setText(R.string.manual_mode_on);
                }

                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;
            case PEAK_HOLD_STARTED:

                textValue.setText("");
                String PeakHoldMode = prefs.getString(getString(R.string.preferences_peak_mode), "");
                double PHcurrentvalue = ApplicationManager.getInstance().getTaredValueInGram();

                boolean stableonly = false;
                if (prefs.getBoolean(getString(R.string.preferences_peak_stableonly), getResources().getBoolean(R.bool.preferences_peak_stableonly)) == true) {
                    stableonly = true;
                }

                if (PeakHoldMode.equals("3")) {
                    textInstruction.setText(R.string.automatic_moce);
                } else if (PeakHoldMode.equals("2")) {
                    textInstruction.setText(R.string.semi_automatic_mode);
                } else {
                    textInstruction.setText(R.string.manual_mode);
                }
                if (stableonly) {
                    textInstruction.append(getString(R.string.apply_stable_weights));
                } else {
                    textInstruction.append(getString(R.string.apply_higher_weights));
                }


                boolean PHstable = Scale.getInstance().isStable();


                if (PeakHoldMode.equals("3")) {
                    if (ApplicationManager.getInstance().getSumInGram() < 0.02f && ApplicationManager.getInstance().getPholdWeight() > 0.02f) {
                        ctime_first = System.nanoTime();

                    }
                    // final String TAG = getClass().getSimpleName();
                    // Log.e(TAG, String.format("%d",(System.nanoTime() - ctime_first)));

                    if (ApplicationManager.getInstance().getTaredValueInGram() < 0.02f && (System.nanoTime() - ctime_first) >= 10000000000l) {
                        ApplicationManager.getInstance().setPeakHoldMaximum(0);


                    }
                }

                ApplicationManager.getInstance().setPholdWeight(ApplicationManager.getInstance().getTaredValueInGram());


                //if (ApplicationManager.getInstance().getPeakHoldActivated()==true){
                if (PHcurrentvalue >= ApplicationManager.getInstance().getPeakHoldMaximum() && (stableonly == false || PHstable == true)) {
                    ApplicationManager.getInstance().setPeakHoldMaximum(PHcurrentvalue);
                }

                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getPeakHoldMaximum()));
                textSum.setText(getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());

                break;

            case PEAK_HOLD:
                textValue.setTextColor(Color.WHITE);
                textValue.setText(R.string.press_start);

                //textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText(getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                ApplicationManager.getInstance().setPeakHoldMaximum(0);


                break;

            case INGREDIENT_COSTING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());


                break;

            case STATISTICAL_QUALITY_CONTROL_1_HOME:
                textInstruction.setText(R.string.press_new_batch);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText("");

                break;

            case STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText("");

                break;

            case STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED:
                textInstruction.setText(R.string.press_new_batch);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText("");

                break;

            case PIPETTE_ADJUSTMENT_1_HOME:

                textInstruction.setText(R.string.place_container_on_the_pan);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText(ApplicationManager.getInstance().getSumAsStringWithUnit());

                break;


            case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:

                textInstruction.setText(getString(R.string.dispense_sample_number) + " " + ApplicationManager.getInstance().getPipette_current_sample() + " " + getString(R.string.and_press_accept));


                //Equation according to http://www.wissenschaft-technik-ethik.de/wasser_dichte.html
                double pipetteDensity = ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp());
                pipetteDensity = pipetteDensity + (ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure() - 1) * 0.046;
                double pipetteML = ApplicationManager.getInstance().getTaredValueInGram() / pipetteDensity;


                textValue.setText(String.format("%.4f", pipetteML) + " ml");
                textValue.setTextColor(Color.WHITE);
                textSum.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                break;

            case PIPETTE_ADJUSTMENT_3_FINISHED:
                textInstruction.setText(R.string.pipette_adjustment_finisihed);


                textValue.setText(String.format("%.4f", ApplicationManager.getInstance().getPipetteCalculatedML()) + " ml");
                textValue.setTextColor(Color.WHITE);
                textSum.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                break;

            case ASH_DETERMINATION_1_HOME:
            case ASH_DETERMINATION_2_ENTER_NAME_SAMPLE:
            case ASH_DETERMINATION_3_ENTER_NAME_BEAKER:
            case ASH_DETERMINATION_4_WEIGH_BEAKER:
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                beakerWeight = ApplicationManager.getInstance().getTaredValueInGram();
                textSum.setText("");
                break;
            case ASH_DETERMINATION_5_WEIGHING_SAMPLE:
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                double materialWeight = (ApplicationManager.getInstance().getSumInGram() > beakerWeight) ? ApplicationManager.getInstance().getSumInGram() - beakerWeight : 0;
                textSum.setText("PROBENMASSE: " +
                        String.valueOf(NumberFormatUtils.roundNumber(materialWeight, 4)));
                break;
            case ASH_DETERMINATION_6_WAIT_FOR_GLOWING:
            case ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE:
            case ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT:
            case ASH_DETERMINATION_9_BATCH_FINISHED:
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                break;
            default:
                textValue.setTextColor(Color.WHITE);
                textValue.setText("not implemented");
                break;

        }

        if (loadingbarnormal) {
            //Update Loading bar
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) barload.getLayoutParams();
            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) barloadbackground.getLayoutParams();
            //int fwidth = (int) (((Scale.getInstance().getWeightInGram()/ AppConstants.WEIGHT_MAX_IN_GRAM) * WIDTH_LOADING_BAR_TOTAL));
            int fwidth = 0;
            if (IS_IO_SIMULATED || INTERNAL_TARA_ZERO_BUTTON) {
                fwidth = (int) (((Scale.getInstance().getWeightInGram() / Scale.getInstance().getScaleModel().getMaximumCapazityInGram()) * WIDTH_LOADING_BAR_TOTAL));
            } else {
                fwidth = (int) (((ApplicationManager.getInstance().getSum() / Scale.getInstance().getScaleModel().getMaximumCapazityInGram()) * WIDTH_LOADING_BAR_TOTAL));
            }
            if (fwidth < 0) {
                fwidth = 0;
            }
            if (fwidth > WIDTH_LOADING_BAR_TOTAL) {
                fwidth = WIDTH_LOADING_BAR_TOTAL;
            }
            params.width = fwidth;
            params.height = 15;
            barload.setLayoutParams(params);

            params2.height = 15;
            barloadbackground.setLayoutParams(params2);


            if (ApplicationManager.getInstance().getSumInGram() > (0.8 * Scale.getInstance().getScaleModel().getMaximumCapazityInGram())) {
                barload.setBackgroundColor(Color.RED);

            } else {
                barload.setBackgroundColor(Color.GREEN);
            }

        }
    }


    @Override
    public void onStableChanged(boolean isStable) {
        Log.e("ApplicationFragmentWe", "onStable called " + isStable);
        if (isStable) {
            imageStable.setVisibility(View.VISIBLE);
        } else {
            imageStable.setVisibility(View.INVISIBLE);
        }
    }
}
