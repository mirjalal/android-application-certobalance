package com.certoclav.certoscale.menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.StatisticListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.ReferenceField;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.List;


public class ApplicationFragmentTable extends Fragment implements WeightListener, StatisticListener {


    private float tara = 0;
    private TextView textTara = null;
    private List<ReferenceField> listReferenceFields = new ArrayList<ReferenceField>();


    int indexTableTara = 2;
    int brutto = 0;
    int netto = 4;
    int loadp = 1;

    // Weighing
    int minweight = 3;

    //Part Counting
    int apw = 3;
    int underlimit = 5;
    int overlimit = 7;

    //Percent Weighing
    int reference_weight = 1;
    int difference_weight = 3;
    int difference_percent = 5;

    //Check Weighing
    int checkunderlimit = 1;
    int checkoverlimit = 3;
    int nominal = 1;
    int checkundertolerance = 3;
    int checkovertolerance = 5;

    //Animal Weighing
    int measuringTime = 1;

    //Filling
    int FillingTarget = 1;
    int FillingDifference = 3;
    int FillingDifferencePercent = 5;


    //Totalization
    int indexTableNumberOfSamples = 0;
    int indexTableTotal = 1;
    int indextableaverage = 2;
    int standarddeviation = 4;
    int minimum = 3;
    int maximum = 5;
    int range = 6;
    int totalizationCurrrent = 7;

    //Formulation
    int indexTableRecipeName = 0;
    int indexFormulationTotal = 3;
    int indexFormulationTotalTarget = 1;
    int indexFormulationTotalDifference = 5;

    //Differential Weighing
    int indexTableItemName = 0;
    int indexTableItemDifferenceWeight = 1;
    int indexTableItemDifferencePercentage = 2;
    int indexTableItemInitialWeight = 3;
    int indexTableItemFinalWeight = 4;

    //Peak Hold

    //Ingrediant Costing
    int indexTableIngrediantUnitCost = 1;
    int indexTableIngrediantTotalWeight = 3;
    int indexTableIngrediantTotalCost = 5;
    int indexTableIngrediantArticlename = 7;

    //Density Determination
    int DensityWaterTemp = 0;
    int DensityLiquidDensity = 2;
    int DensityOilDensity = 4;

    int DensityWeightAir = 1;
    int DensityWeightLiquid = 3;
    int DensityOiledWeight = 5;

    //Statistical Quality Control
    int SQCnumberofSamples = 0;
    int SQCaverage = 2;
    int SQCsum = 4;

    int SQCminimum = 1;
    int SQCmaximum = 3;
    int SQCrange = 5;

    //Pipette Adjustment
    int pipetteNominal = 0;
    int pipetteInaccuracy = 2;
    int pipetteImprecison = 4;

    int pipetteBarometric = 1;
    int pipetteLiquidDensity = 3;
    int pipetteWaterTemp = 5;

    //Ash determination
    int indexAshDeterminationWeightBeaker = 1;
    int indexAshDeterminationWeightBeakerWithSample = 3;
    int indexAshDeterminationDeltaWeight = 5;

    int indexAshDeterminationSampleName = 0;
    int indexAshDeterminationBeakerName = 2;


    @Override
    public void onResume() {
        super.onResume();
        if (listReferenceFields.isEmpty() == false) {
            for (ReferenceField refField : listReferenceFields) {
                refField.getTextName().setText("");
                refField.getTextValue().setText("");
            }
        }

        Scale.getInstance().setOnWeightListener(this);
        ApplicationManager.getInstance().setOnStatisticListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Scale.getInstance().removeOnWeightListener(this);
        ApplicationManager.getInstance().setOnStatisticListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_table, container, false);

        //8 Textfields for showing data of interest
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_1), (TextView) rootView.findViewById(R.id.reference_val_1)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_2), (TextView) rootView.findViewById(R.id.reference_val_2)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_3), (TextView) rootView.findViewById(R.id.reference_val_3)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_4), (TextView) rootView.findViewById(R.id.reference_val_4)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_5), (TextView) rootView.findViewById(R.id.reference_val_5)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_6), (TextView) rootView.findViewById(R.id.reference_val_6)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_7), (TextView) rootView.findViewById(R.id.reference_val_7)));
        listReferenceFields.add(new ReferenceField((TextView) rootView.findViewById(R.id.reference_text_8), (TextView) rootView.findViewById(R.id.reference_val_8)));
        for (ReferenceField refField : listReferenceFields) {
            refField.getTextName().setText("");
            refField.getTextValue().setText("");
        }


        return rootView;
    }


    @Override
    public void onWeightChanged(Double weight, String unit) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        switch (Scale.getInstance().getScaleApplication()) {
            case WEIGHING:

                if (prefs.getBoolean(getString(R.string.preferences_weigh_tara_visible), getResources().getBoolean(R.bool.preferences_weigh_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_weigh_brutto_visible), getResources().getBoolean(R.bool.preferences_weigh_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_weigh_netto_visible), getResources().getBoolean(R.bool.preferences_weigh_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_weigh_load_visible), getResources().getBoolean(R.bool.preferences_weigh_load_visible)) == true) {
                    listReferenceFields.get(loadp).getTextName().setText(R.string.load_percent);
                    listReferenceFields.get(loadp).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");
                }


                //listReferenceFields.get(3).getTextName().setText("LOAD [g]");
                //listReferenceFields.get(3).getTextValue().setText(ApplicationManager.getInstance().getLoadInGramAsStringWithUnit());


                if (prefs.getBoolean(getString(R.string.preferences_weigh_minimum_visible), getResources().getBoolean(R.bool.preferences_weigh_minimum_visible)) == true) {
                    listReferenceFields.get(minweight).getTextName().setText(getString(R.string.minimum_weight).toUpperCase());
                    listReferenceFields.get(minweight).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitAsStringWithUnit());
                }

                break;
            case PART_COUNTING:
            case PART_COUNTING_CALC_AWP:
                if (prefs.getBoolean(getString(R.string.preferences_counting_tara_visible), getResources().getBoolean(R.bool.preferences_counting_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_counting_brutto_visible), getResources().getBoolean(R.bool.preferences_counting_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_counting_netto_visible), getResources().getBoolean(R.bool.preferences_counting_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_weigh_load_visible), getResources().getBoolean(R.bool.preferences_weigh_load_visible)) == true) {
                    listReferenceFields.get(loadp).getTextName().setText(R.string.load_percent);
                    listReferenceFields.get(loadp).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");
                }


                if (prefs.getBoolean(getString(R.string.preferences_counting_apw_visible), getResources().getBoolean(R.bool.preferences_counting_apw_visible)) == true) {
                    listReferenceFields.get(apw).getTextName().setText(getString(R.string.piece_weight).toUpperCase());
                    listReferenceFields.get(apw).getTextValue().setText(ApplicationManager.getInstance().getAveragePieceWeightAsStringWithUnit());
                }


                String cmode = prefs.getString(getString(R.string.preferences_counting_mode), "1");

                Log.e("AppFragTable", cmode);
                if (cmode.equals("2")) {
                    if (prefs.getBoolean(getString(R.string.preferences_counting_under_limit_visible), getResources().getBoolean(R.bool.preferences_counting_under_limit_visible)) == true) {
                        listReferenceFields.get(underlimit).getTextName().setText(getString(R.string.under_limit).toUpperCase());
                        listReferenceFields.get(underlimit).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitPiecesAsString() + " PCS");
                    }

                    if (prefs.getBoolean(getString(R.string.preferences_counting_over_limit_visible), getResources().getBoolean(R.bool.preferences_counting_over_limit_visible)) == true) {
                        listReferenceFields.get(overlimit).getTextName().setText(getString(R.string.over_limit).toUpperCase());
                        listReferenceFields.get(overlimit).getTextValue().setText(ApplicationManager.getInstance().getOverlimitPiecesAsString() + " PCS");
                    }
                }

                if (cmode.equals("3")) {
                    if (prefs.getBoolean(getString(R.string.preferences_counting_target_visible), getResources().getBoolean(R.bool.preferences_counting_target_visible)) == true) {
                        listReferenceFields.get(underlimit).getTextName().setText(getString(R.string.target).toUpperCase());
                        listReferenceFields.get(underlimit).getTextValue().setText(ApplicationManager.getInstance().getTargetPiecesAsString() + " PCS");
                    }

                    if (prefs.getBoolean(getString(R.string.preferences_counting_difference_visible), getResources().getBoolean(R.bool.preferences_counting_difference_visible)) == true) {
                        listReferenceFields.get(overlimit).getTextName().setText(getString(R.string.difference).toUpperCase());
                        listReferenceFields.get(overlimit).getTextValue().setText(ApplicationManager.getInstance().getDifferenceAsString() + " PCS");
                    }
                }


                break;
            case PERCENT_WEIGHING:
            case PERCENT_WEIGHING_CALC_REFERENCE:
                if (prefs.getBoolean(getString(R.string.preferences_percent_tara_visible), getResources().getBoolean(R.bool.preferences_percent_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_percent_brutto_visible), getResources().getBoolean(R.bool.preferences_percent_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_percent_netto_visible), getResources().getBoolean(R.bool.preferences_percent_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }


                if (prefs.getBoolean(getString(R.string.preferences_percent_reference_weight_visible), getResources().getBoolean(R.bool.preferences_percent_reference_weight_visible)) == true) {
                    listReferenceFields.get(reference_weight).getTextName().setText(getString(R.string.reference_weight).toUpperCase());
                    listReferenceFields.get(reference_weight).getTextValue().setText(ApplicationManager.getInstance().getReferenceWeightAdjustedAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_percent_difference_visible), getResources().getBoolean(R.bool.preferences_percent_difference_visible)) == true) {
                    listReferenceFields.get(difference_weight).getTextName().setText(getString(R.string.difference).toUpperCase() + " " + "[" + ApplicationManager.getInstance().getCurrentUnit().getName() + "]");
                    listReferenceFields.get(difference_weight).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDifferenceInGram()));
                }
                if (prefs.getBoolean(getString(R.string.preferences_percent_difference_percent_visible), getResources().getBoolean(R.bool.preferences_percent_difference_percent_visible)) == true) {
                    listReferenceFields.get(difference_percent).getTextName().setText(getString(R.string.difference).toUpperCase() + " [%]");
                    listReferenceFields.get(difference_percent).getTextValue().setText(ApplicationManager.getInstance().getDifferenceInPercent() + " %");
                }
                break;
            case CHECK_WEIGHING:

                String cmode_check = prefs.getString(getString(R.string.preferences_check_limitmode), "1");
                if (prefs.getBoolean(getString(R.string.preferences_check_tara_visible), getResources().getBoolean(R.bool.preferences_check_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_check_brutto_visible), getResources().getBoolean(R.bool.preferences_check_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_check_netto_visible), getResources().getBoolean(R.bool.preferences_check_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto));
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (cmode_check.equals("1")) {

                    if (prefs.getBoolean(getString(R.string.preferences_check_under_visible), getResources().getBoolean(R.bool.preferences_check_under_visible)) == true) {
                        listReferenceFields.get(checkunderlimit).getTextName().setText(getString(R.string.under_limit).toUpperCase());
                        listReferenceFields.get(checkunderlimit).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitCheckWeighingAsStringWithUnit());
                    }
                    if (prefs.getBoolean(getString(R.string.preferences_check_over_visible), getResources().getBoolean(R.bool.preferences_check_over_visible)) == true) {
                        listReferenceFields.get(checkoverlimit).getTextName().setText(getString(R.string.over_limit).toUpperCase());
                        listReferenceFields.get(checkoverlimit).getTextValue().setText(ApplicationManager.getInstance().getOverLimitCheckWeighingAsStringWithUnit());
                    }
                }

                if (cmode_check.equals("2")) {

                    if (prefs.getBoolean(getString(R.string.preferences_check_target_visible), getResources().getBoolean(R.bool.preferences_check_target_visible)) == true) {
                        listReferenceFields.get(nominal).getTextName().setText(getString(R.string.target).toUpperCase());
                        listReferenceFields.get(nominal).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalAsStringWithUnit());
                    }
                    if (prefs.getBoolean(getString(R.string.preferences_check_undertolerance_visible), getResources().getBoolean(R.bool.preferences_check_undertolerance_visible)) == true) {
                        listReferenceFields.get(checkundertolerance).getTextName().setText(getString(R.string.under_toleranc).toUpperCase());
                        listReferenceFields.get(checkundertolerance).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalToleranceUnderAsStringWithUnit());
                    }
                    if (prefs.getBoolean(getString(R.string.preferences_check_undertolerance_visible), getResources().getBoolean(R.bool.preferences_check_undertolerance_visible)) == true) {
                        listReferenceFields.get(checkovertolerance).getTextName().setText(getString(R.string.over_tolerance).toUpperCase());
                        listReferenceFields.get(checkovertolerance).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalToleranceOverAsStringWithUnit());
                    }
                }

                if (cmode_check.equals("3")) {
                    if (prefs.getBoolean(getString(R.string.preferences_check_target_visible), getResources().getBoolean(R.bool.preferences_check_target_visible)) == true) {
                        listReferenceFields.get(nominal).getTextName().setText(getString(R.string.target).toUpperCase());
                        listReferenceFields.get(nominal).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalAsStringWithUnit());
                    }

                    if (prefs.getBoolean(getString(R.string.preferences_check_undertolerance_visible), getResources().getBoolean(R.bool.preferences_check_undertolerance_visible)) == true) {
                        listReferenceFields.get(checkundertolerance).getTextName().setText(getString(R.string.under_toleranc).toUpperCase());
                        listReferenceFields.get(checkundertolerance).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalToleranceUnderPercent() + " %");
                    }

                    if (prefs.getBoolean(getString(R.string.preferences_check_overtolerance_visible), getResources().getBoolean(R.bool.preferences_check_overtolerance_visible)) == true) {
                        listReferenceFields.get(checkovertolerance).getTextName().setText(getString(R.string.over_tolerance).toUpperCase());
                        listReferenceFields.get(checkovertolerance).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalToleranceOverPercent() + " %");
                    }


                }

                break;
            case ANIMAL_WEIGHING:
                if (prefs.getBoolean(getString(R.string.preferences_animal_tara_visible), getResources().getBoolean(R.bool.preferences_animal_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_animal_brutto_visible), getResources().getBoolean(R.bool.preferences_animal_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_animal_netto_visible), getResources().getBoolean(R.bool.preferences_animal_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_animal_measuringtime_visible), getResources().getBoolean(R.bool.preferences_animal_measuringtime_visible)) == true) {
                    listReferenceFields.get(measuringTime).getTextName().setText(getString(R.string.averaging_time).toUpperCase());
                    listReferenceFields.get(measuringTime).getTextValue().setText(ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime() + " s");
                }


                break;

            case FILLING:
            case FILLING_CALC_TARGET:
                if (prefs.getBoolean(getString(R.string.preferences_filling_tara_visible), getResources().getBoolean(R.bool.preferences_animal_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_filling_brutto_visible), getResources().getBoolean(R.bool.preferences_animal_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_filling_netto_visible), getResources().getBoolean(R.bool.preferences_filling_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_filling_target_visible), getResources().getBoolean(R.bool.preferences_filling_target_visible)) == true) {
                    listReferenceFields.get(FillingTarget).getTextName().setText(getString(R.string.target).toUpperCase());
                    listReferenceFields.get(FillingTarget).getTextValue().setText(ApplicationManager.getInstance().getTargetAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_filling_differencew_visible), getResources().getBoolean(R.bool.preferences_filling_differencew_visible)) == true) {
                    listReferenceFields.get(FillingDifference).getTextName().setText(getString(R.string.difference).toUpperCase() + ": ");
                    listReferenceFields.get(FillingDifference).getTextValue().setText(ApplicationManager.getInstance().getDifferenceFillingAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_filling_differencep_visible), getResources().getBoolean(R.bool.preferences_filling_differencep_visible)) == true) {
                    listReferenceFields.get(FillingDifferencePercent).getTextName().setText(getString(R.string.difference).toUpperCase() + "%: ");
                    listReferenceFields.get(FillingDifferencePercent).getTextValue().setText(ApplicationManager.getInstance().getFillingDifferenceInPercent() + " %");
                }


                break;
            case PEAK_HOLD_STARTED:
                if (prefs.getBoolean(getString(R.string.preferences_peak_tara_visible), getResources().getBoolean(R.bool.preferences_animal_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_peak_brutto_visible), getResources().getBoolean(R.bool.preferences_animal_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_peak_netto_visible), getResources().getBoolean(R.bool.preferences_filling_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }
            case PEAK_HOLD:
                if (prefs.getBoolean(getString(R.string.preferences_peak_tara_visible), getResources().getBoolean(R.bool.preferences_animal_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_peak_brutto_visible), getResources().getBoolean(R.bool.preferences_animal_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_peak_netto_visible), getResources().getBoolean(R.bool.preferences_filling_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }


                break;

            case INGREDIENT_COSTING:
                if (prefs.getBoolean(getString(R.string.preferences_ingrediant_tara_visible), getResources().getBoolean(R.bool.preferences_ingrediant_tara_visible)) == true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText(getString(R.string.tara).toUpperCase());
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if (prefs.getBoolean(getString(R.string.preferences_ingrediant_brutto_visible), getResources().getBoolean(R.bool.preferences_ingrediant_brutto_visible)) == true) {
                    listReferenceFields.get(brutto).getTextName().setText(getString(R.string.brutto).toUpperCase());
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if (prefs.getBoolean(getString(R.string.preferences_ingrediant_netto_visible), getResources().getBoolean(R.bool.preferences_ingrediant_netto_visible)) == true) {
                    listReferenceFields.get(netto).getTextName().setText(getString(R.string.netto).toUpperCase());
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }


                if (prefs.getBoolean(getString(R.string.preferences_ingrediant_unitcost_visible), getResources().getBoolean(R.bool.preferences_ingrediant_unitcost_visible)) == true) {
                    listReferenceFields.get(indexTableIngrediantUnitCost).getTextName().setText(getString(R.string.unit_cost).toUpperCase());
                    listReferenceFields.get(indexTableIngrediantUnitCost).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getIngrediantUnitCost()) + " " + ApplicationManager.getInstance().getCurrency());
                }

                if (prefs.getBoolean(getString(R.string.preferences_ingrediant_totalweight_visible), getResources().getBoolean(R.bool.preferences_ingrediant_totalweight_visible)) == true) {
                    listReferenceFields.get(indexTableIngrediantTotalWeight).getTextName().setText(getString(R.string.total_weight).toUpperCase());
                    listReferenceFields.get(indexTableIngrediantTotalWeight).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getIngrediantTotalWeight()));
                }

                if (prefs.getBoolean(getString(R.string.preferences_ingrediant_totalcost_visible), getResources().getBoolean(R.bool.preferences_ingrediant_totalcost_visible)) == true) {
                    listReferenceFields.get(indexTableIngrediantTotalCost).getTextName().setText(getString(R.string.total_cost).toUpperCase());
                    listReferenceFields.get(indexTableIngrediantTotalCost).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getIngrediantTotalCost()) + " " + ApplicationManager.getInstance().getCurrency());
                }

                if (ApplicationManager.getInstance().getCurrentItem() != null) {

                    listReferenceFields.get(indexTableIngrediantArticlename).getTextName().setText(getString(R.string.item_name).toUpperCase());
                    listReferenceFields.get(indexTableIngrediantArticlename).getTextValue().setText(ApplicationManager.getInstance().getCurrentItem().getName());
                }

                break;
            case DENSITY_DETERMINATION_STARTED:
            case DENSITY_DETERMINATION:
                String densityliquidtype = prefs.getString(getString(R.string.preferences_density_liquidtyp), "1");
                String densitymode = prefs.getString(getString(R.string.preferences_density_mode), "1");
                if (densityliquidtype.equals("1")) {

                    ApplicationManager.getInstance().getCurrentLibrary().setDensityLiquidDensity(ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()));
                }

                if (prefs.getBoolean(getString(R.string.preferences_density_watertemp_visible), getResources().getBoolean(R.bool.preferences_density_watertemp_visible)) == true) {
                    if ((densityliquidtype.equals("1") && (densitymode.equals("1") || densitymode.equals("2"))) || densitymode.equals("4")) {
                        listReferenceFields.get(DensityWaterTemp).getTextName().setText(getString(R.string.water_temp).toUpperCase());
                        listReferenceFields.get(DensityWaterTemp).getTextValue().setText(String.format("%.1f", ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()) + " °C");
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_density_sinkervolume_visible), getResources().getBoolean(R.bool.preferences_density_sinkervolume_visible)) == true) {
                    if (densitymode.equals("3")) {
                        listReferenceFields.get(0).getTextName().setText(getString(R.string.sinker_volume).toUpperCase());
                        listReferenceFields.get(0).getTextValue().setText(String.format("%.2f", ApplicationManager.getInstance().getCurrentLibrary().getSinkerVolume()) + " ml");
                    }
                }


                if (prefs.getBoolean(getString(R.string.preferences_denisty_Liquid_visible), getResources().getBoolean(R.bool.preferences_denisty_Liquid_visible)) == true) {

                    if ((densitymode.equals("1") || densitymode.equals("2")) || (densitymode.equals("4"))) {
                        listReferenceFields.get(DensityLiquidDensity).getTextName().setText(getString(R.string.liquid_density).toUpperCase());
                        listReferenceFields.get(DensityLiquidDensity).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity()) + " g/cm³");
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_density_weightair_visible), getResources().getBoolean(R.bool.preferences_density_weightair_visible)) == true) {
                    listReferenceFields.get(DensityWeightAir).getTextName().setText(getString(R.string.weight_in_air).toUpperCase());
                    listReferenceFields.get(DensityWeightAir).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDensity_weight_air()));
                }

                if (prefs.getBoolean(getString(R.string.preferences_density_weightinliquid_print), getResources().getBoolean(R.bool.preferences_density_weightinliquid_print)) == true) {
                    listReferenceFields.get(DensityWeightLiquid).getTextName().setText(getString(R.string.weight_in_liquid).toUpperCase());
                    listReferenceFields.get(DensityWeightLiquid).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDensity_weight_liquid()));
                }

                if (densitymode.equals("4")) {
                    if (prefs.getBoolean(getString(R.string.preferences_density_oildensity_visible), getResources().getBoolean(R.bool.preferences_density_oildensity_visible)) == true) {
                        listReferenceFields.get(DensityOilDensity).getTextName().setText(getString(R.string.oil_density).toUpperCase());
                        listReferenceFields.get(DensityOilDensity).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_liquid()) + " g/cm³");
                    }
                }

                if (densitymode.equals("4")) {
                    if (prefs.getBoolean(getString(R.string.preferences_density_oiledweight_visible), getResources().getBoolean(R.bool.preferences_density_oiledweight_visible)) == true) {
                        listReferenceFields.get(DensityOiledWeight).getTextName().setText(getString(R.string.oiled_weight).toUpperCase());
                        listReferenceFields.get(DensityOiledWeight).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getOiledWeight()) + " g");
                    }
                }


                break;


            case TOTALIZATION:

                // PLACE NO CODE HERE - TABLE WILL BE FILLED BY onStatisticChanged() callback function below in this code Why? Whats the benefit? Visibility options are easier to use if the table is filled here
                if (prefs.getBoolean(getString(R.string.preferences_totalizaion_NumberofSamples), getResources().getBoolean(R.bool.preferences_totalizaion_NumberofSamples)) == true) {
                    listReferenceFields.get(indexTableNumberOfSamples).getTextName().setText(getString(R.string.samples).toUpperCase());
                    try {
                        listReferenceFields.get(indexTableNumberOfSamples).getTextValue().setText(Long.toString(ApplicationManager.getInstance().getStats().getStatistic().getN()));
                    } catch (Exception e) {
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_totalization_total_visible), getResources().getBoolean(R.bool.preferences_totalization_total_visible)) == true) {
                    listReferenceFields.get(indexTableTotal).getTextName().setText(getString(R.string.total).toUpperCase());
                    try {
                        if (ApplicationManager.getInstance().getStats().getStatistic().getSum() == 0) {
                            listReferenceFields.get(indexTableTotal).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            listReferenceFields.get(indexTableTotal).getTextValue().setText(ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_totalization_average_visible), getResources().getBoolean(R.bool.preferences_totalization_average_visible)) == true) {
                    listReferenceFields.get(indextableaverage).getTextName().setText(getString(R.string.average).toUpperCase());
                    try {
                        if (Double.isNaN(ApplicationManager.getInstance().getStats().getStatistic().getMean())) {
                            listReferenceFields.get(indextableaverage).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            listReferenceFields.get(indextableaverage).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }

                }

                if (prefs.getBoolean(getString(R.string.preferences_totalization_standarddeviation_visible), getResources().getBoolean(R.bool.preferences_totalization_standarddeviation_visible)) == true) {
                    listReferenceFields.get(standarddeviation).getTextName().setText(getString(R.string.standard_dev).toUpperCase());
                    try {
                        if (Double.isNaN(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation())) {
                            listReferenceFields.get(standarddeviation).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            listReferenceFields.get(standarddeviation).getTextValue().setText(ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_totalization_minimum_visible), getResources().getBoolean(R.bool.preferences_totalization_minimum_visible)) == true) {
                    listReferenceFields.get(minimum).getTextName().setText(getString(R.string.minimum).toUpperCase());
                    try {
                        if (Double.isNaN(ApplicationManager.getInstance().getStats().getStatistic().getMin())) {
                            listReferenceFields.get(minimum).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            listReferenceFields.get(minimum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }
                }


                if (prefs.getBoolean(getString(R.string.preferences_totalization_maximum_visible), getResources().getBoolean(R.bool.preferences_totalization_maximum_visible)) == true) {

                    listReferenceFields.get(maximum).getTextName().setText(getString(R.string.maximum).toUpperCase());
                    try {
                        if (Double.isNaN(ApplicationManager.getInstance().getStats().getStatistic().getMax())) {
                            listReferenceFields.get(maximum).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            listReferenceFields.get(maximum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_totalization_range_visible), getResources().getBoolean(R.bool.preferences_totalization_range_visible)) == true) {

                    listReferenceFields.get(range).getTextName().setText(getString(R.string.range).toUpperCase());
                    try {
                        if (Double.isNaN(ApplicationManager.getInstance().getStats().getStatistic().getMax() - ApplicationManager.getInstance().getStats().getStatistic().getMin())) {
                            listReferenceFields.get(range).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            listReferenceFields.get(range).getTextValue().setText(ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }
                }


                if (prefs.getBoolean(getString(R.string.preferences_totalization_range_visible), getResources().getBoolean(R.bool.preferences_totalization_range_visible)) == true) {

                    listReferenceFields.get(totalizationCurrrent).getTextName().setText(getString(R.string.current).toUpperCase());
                    try {

                        if (ApplicationManager.getInstance().getStats().getSamples().isEmpty() == true) {

                            listReferenceFields.get(totalizationCurrrent).getTextValue().setText("0 " + ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {

                            //ApplicationManager.getInstance().getStats().getSamples().get(ApplicationManager.getInstance().getStats().getSamples().size()-1)
                            listReferenceFields.get(totalizationCurrrent).getTextValue().setText(ApplicationManager.getInstance().getStatisticsCurrentAsStringWithUnit());
                        }
                    } catch (Exception e) {
                    }

                }


                break;
            case FORMULATION:
                listReferenceFields.get(indexTableRecipeName).getTextName().setText(getString(R.string.recipe).toUpperCase());
                if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText(ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());
                } else {
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText(getString(R.string.no_recipe).toUpperCase());
                }
                if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
                    double formulationTotal = 0;
                    double formulationTotalTarget = 0;
                    double formulationTotalDifference = 0;
                    int formulationcounter = 0;
                    while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {
                        formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getScaledWeight();
                        formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();
                        formulationcounter++;
                    }


                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_target)) == true) {
                        listReferenceFields.get(indexFormulationTotalTarget).getTextName().setText(getString(R.string.total_target).toUpperCase());
                        listReferenceFields.get(indexFormulationTotalTarget).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotalTarget));
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
                        listReferenceFields.get(indexFormulationTotal).getTextName().setText(getString(R.string.total_actual).toUpperCase());
                        listReferenceFields.get(indexFormulationTotal).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotal));
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
                        formulationTotalDifference = Math.abs(formulationTotal - formulationTotalTarget) / Math.abs(formulationTotalTarget);
                        formulationTotalDifference = formulationTotalDifference * 100;
                        listReferenceFields.get(indexFormulationTotalDifference).getTextName().setText(getString(R.string.total_diff).toUpperCase());
                        listReferenceFields.get(indexFormulationTotalDifference).getTextValue().setText(String.format("%.2f", formulationTotalDifference) + " %");
                    }
                    listReferenceFields.get(2).getTextName().setText(getString(R.string.scaling_factor).toUpperCase());
                    listReferenceFields.get(2).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getScalingFactor()));
                }
                break;


            case FORMULATION_FREE:
                listReferenceFields.get(indexTableRecipeName).getTextName().setText(getString(R.string.recipe).toUpperCase());
                if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText(ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());
                } else {
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText(getString(R.string.no_recipe).toUpperCase());
                }
                if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
                    double formulationTotal = 0;
                    double formulationTotalTarget = 0;
                    double formulationTotalDifference = 0;
                    int formulationcounter = 0;
                    while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {
                        formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getScaledWeight();
                        formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();
                        formulationcounter++;
                    }


                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_target)) == true) {
                        listReferenceFields.get(indexFormulationTotalTarget).getTextName().setText(getString(R.string.total_target).toUpperCase());
                        listReferenceFields.get(indexFormulationTotalTarget).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotalTarget));
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
                        listReferenceFields.get(indexFormulationTotal).getTextName().setText(getString(R.string.total_actual).toUpperCase());
                        listReferenceFields.get(indexFormulationTotal).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotal));
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
                        formulationTotalDifference = Math.abs(formulationTotal - formulationTotalTarget) / Math.abs(formulationTotalTarget);
                        formulationTotalDifference = formulationTotalDifference * 100;
                        listReferenceFields.get(indexFormulationTotalDifference).getTextName().setText(getString(R.string.total_diff).toUpperCase());
                        listReferenceFields.get(indexFormulationTotalDifference).getTextValue().setText(String.format("%.2f", formulationTotalDifference) + " %");
                    }
                }

                break;

            case DIFFERENTIAL_WEIGHING:
                listReferenceFields.get(indexTableItemName).getTextName().setText(getString(R.string.item).toUpperCase());
                if (ApplicationManager.getInstance().getCurrentItem() != null) {
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText(ApplicationManager.getInstance().getCurrentItem().getName());
                    listReferenceFields.get(indexTableItemDifferenceWeight).getTextName().setText(getString(R.string.difference).toUpperCase() + " " + "[" + ApplicationManager.getInstance().getCurrentUnit().getName() + "]");
                    listReferenceFields.get(indexTableItemDifferenceWeight).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDifferenceInGram()));
                    listReferenceFields.get(indexTableItemDifferencePercentage).getTextName().setText(getString(R.string.difference).toUpperCase() + " [%]");
                    listReferenceFields.get(indexTableItemDifferencePercentage).getTextValue().setText(ApplicationManager.getInstance().getDifferenceToInitialInPercentAsString());
                    listReferenceFields.get(indexTableItemFinalWeight).getTextName().setText(getString(R.string.final_weight).toUpperCase());
                    listReferenceFields.get(indexTableItemFinalWeight).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    listReferenceFields.get(indexTableItemInitialWeight).getTextName().setText(getString(R.string.initial_weight).toUpperCase());
                    listReferenceFields.get(indexTableItemInitialWeight).getTextValue().setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentItem().getWeight()));
                } else {
                    listReferenceFields.get(indexTableItemName).getTextValue().setText("-");
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText("");
                    listReferenceFields.get(indexTableItemDifferenceWeight).getTextName().setText("");
                    listReferenceFields.get(indexTableItemDifferenceWeight).getTextValue().setText("");
                    listReferenceFields.get(indexTableItemDifferencePercentage).getTextName().setText("");
                    listReferenceFields.get(indexTableItemDifferencePercentage).getTextValue().setText("");
                    listReferenceFields.get(indexTableItemFinalWeight).getTextName().setText("");
                    listReferenceFields.get(indexTableItemFinalWeight).getTextValue().setText("");
                    listReferenceFields.get(indexTableItemInitialWeight).getTextName().setText("");
                    listReferenceFields.get(indexTableItemInitialWeight).getTextValue().setText("");

                }

                break;

            case PIPETTE_ADJUSTMENT_1_HOME:
            case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:
            case PIPETTE_ADJUSTMENT_3_FINISHED:
                if (prefs.getBoolean(getString(R.string.preferences_pipette_nominal_visible), getResources().getBoolean(R.bool.preferences_pipette_nominal_visible)) == true) {
                    listReferenceFields.get(pipetteNominal).getTextName().setText(getString(R.string.nominal).toUpperCase());
                    listReferenceFields.get(pipetteNominal).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal()) + " ml");
                }

                if (prefs.getBoolean(getString(R.string.preferences_pipette_inacc_visible), getResources().getBoolean(R.bool.preferences_pipette_inacc_visible)) == true) {
                    listReferenceFields.get(pipetteInaccuracy).getTextName().setText(getString(R.string.incaccuracy).toUpperCase());
                    listReferenceFields.get(pipetteInaccuracy).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy()) + " %");
                }

                if (prefs.getBoolean(getString(R.string.preferences_pipette_imprec_visible), getResources().getBoolean(R.bool.preferences_pipette_imprec_visible)) == true) {
                    listReferenceFields.get(pipetteImprecison).getTextName().setText(getString(R.string.imprecision).toUpperCase());
                    listReferenceFields.get(pipetteImprecison).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()) + " %");
                }

                if (prefs.getBoolean(getString(R.string.preferences_pipette_barounit_visible), getResources().getBoolean(R.bool.preferences_pipette_barounit_visible)) == true) {
                    listReferenceFields.get(pipetteBarometric).getTextName().setText(getString(R.string.barometric_unit_capitalized).toUpperCase());
                    listReferenceFields.get(pipetteBarometric).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure()) + " ATM");
                }

                if (prefs.getBoolean(getString(R.string.preferences_pipette_liquiddensity_visible), getResources().getBoolean(R.bool.preferences_pipette_liquiddensity_visible)) == true) {
                    listReferenceFields.get(pipetteLiquidDensity).getTextName().setText(getString(R.string.liquid_density).toUpperCase());
                    listReferenceFields.get(pipetteLiquidDensity).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp())) + " g/cm³");
                }

                if (prefs.getBoolean(getString(R.string.preferences_pipette_watertemp_visible), getResources().getBoolean(R.bool.preferences_pipette_watertemp_visible)) == true) {
                    listReferenceFields.get(pipetteWaterTemp).getTextName().setText(getString(R.string.water_temp).toUpperCase());
                    listReferenceFields.get(pipetteWaterTemp).getTextValue().setText(String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp()) + " " + "°C");
                }


                break;


            case STATISTICAL_QUALITY_CONTROL_1_HOME:
                if (prefs.getBoolean(getString(R.string.preferences_statistic_numsamples_visible), getResources().getBoolean(R.bool.preferences_statistic_numsamples_visible)) == true) {
                    listReferenceFields.get(SQCnumberofSamples).getTextName().setText(getString(R.string.samples).toUpperCase());

                    listReferenceFields.get(SQCnumberofSamples).getTextValue().setText(String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()));


                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_average_visible), getResources().getBoolean(R.bool.preferences_statistic_average_visible)) == true) {
                    listReferenceFields.get(SQCaverage).getTextName().setText(getString(R.string.average).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCaverage).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCaverage).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_total_visible), getResources().getBoolean(R.bool.preferences_statistic_total_visible)) == true) {
                    listReferenceFields.get(SQCsum).getTextName().setText(getString(R.string.sum_).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCsum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCsum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_minimum_visible), getResources().getBoolean(R.bool.preferences_statistic_minimum_visible)) == true) {
                    listReferenceFields.get(SQCminimum).getTextName().setText(getString(R.string.minimum).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCminimum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCminimum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_maximum_visible), getResources().getBoolean(R.bool.preferences_statistic_maximum_visible)) == true) {
                    listReferenceFields.get(SQCmaximum).getTextName().setText(getString(R.string.maximum).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCmaximum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCmaximum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_range_visible), getResources().getBoolean(R.bool.preferences_statistic_range_visible)) == true) {
                    listReferenceFields.get(SQCrange).getTextName().setText(getString(R.string.range).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCrange).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCrange).getTextValue().setText(ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit());
                    }
                }


                break;
            case STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED:
                if (prefs.getBoolean(getString(R.string.preferences_statistic_numsamples_visible), getResources().getBoolean(R.bool.preferences_statistic_numsamples_visible)) == true) {
                    listReferenceFields.get(SQCnumberofSamples).getTextName().setText(getString(R.string.samples).toUpperCase());

                    listReferenceFields.get(SQCnumberofSamples).getTextValue().setText(String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()));


                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_average_visible), getResources().getBoolean(R.bool.preferences_statistic_average_visible)) == true) {
                    listReferenceFields.get(SQCaverage).getTextName().setText(getString(R.string.average).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCaverage).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCaverage).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_total_visible), getResources().getBoolean(R.bool.preferences_statistic_total_visible)) == true) {
                    listReferenceFields.get(SQCsum).getTextName().setText(getString(R.string.sum_).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCsum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCsum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_minimum_visible), getResources().getBoolean(R.bool.preferences_statistic_minimum_visible)) == true) {
                    listReferenceFields.get(SQCminimum).getTextName().setText(getString(R.string.minimum).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCminimum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCminimum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_maximum_visible), getResources().getBoolean(R.bool.preferences_statistic_maximum_visible)) == true) {
                    listReferenceFields.get(SQCmaximum).getTextName().setText(getString(R.string.maximum).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCmaximum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCmaximum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_range_visible), getResources().getBoolean(R.bool.preferences_statistic_range_visible)) == true) {
                    listReferenceFields.get(SQCrange).getTextName().setText(getString(R.string.range).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCrange).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCrange).getTextValue().setText(ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit());
                    }
                }


                break;

            case STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED:
                if (prefs.getBoolean(getString(R.string.preferences_statistic_numsamples_visible), getResources().getBoolean(R.bool.preferences_statistic_numsamples_visible)) == true) {
                    listReferenceFields.get(SQCnumberofSamples).getTextName().setText(getString(R.string.samples).toUpperCase());

                    listReferenceFields.get(SQCnumberofSamples).getTextValue().setText(String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()));


                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_average_visible), getResources().getBoolean(R.bool.preferences_statistic_average_visible)) == true) {
                    listReferenceFields.get(SQCaverage).getTextName().setText(getString(R.string.average).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCaverage).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCaverage).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_total_visible), getResources().getBoolean(R.bool.preferences_statistic_total_visible)) == true) {
                    listReferenceFields.get(SQCsum).getTextName().setText(getString(R.string.sum_).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCsum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCsum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_minimum_visible), getResources().getBoolean(R.bool.preferences_statistic_minimum_visible)) == true) {
                    listReferenceFields.get(SQCminimum).getTextName().setText(getString(R.string.minimum).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCminimum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCminimum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_maximum_visible), getResources().getBoolean(R.bool.preferences_statistic_maximum_visible)) == true) {
                    listReferenceFields.get(SQCmaximum).getTextName().setText(getString(R.string.maximum).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCmaximum).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCmaximum).getTextValue().setText(ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit());
                    }
                }

                if (prefs.getBoolean(getString(R.string.preferences_statistic_range_visible), getResources().getBoolean(R.bool.preferences_statistic_range_visible)) == true) {
                    listReferenceFields.get(SQCrange).getTextName().setText(getString(R.string.range).toUpperCase());
                    if (ApplicationManager.getInstance().getStats().getStatistic().getN() == 0) {
                        listReferenceFields.get(SQCrange).getTextValue().setText(String.format("%d", 0));
                    } else {
                        listReferenceFields.get(SQCrange).getTextValue().setText(ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit());
                    }
                }

            case ASH_DETERMINATION_1_HOME:
            case ASH_DETERMINATION_2_ENTER_NAME_SAMPLE:
            case ASH_DETERMINATION_3_ENTER_NAME_BEAKER:
            case ASH_DETERMINATION_4_WEIGH_BEAKER:
            case ASH_DETERMINATION_5_WEIGHING_SAMPLE:
            case ASH_DETERMINATION_6_WAIT_FOR_GLOWING:
            case ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE:
            case ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT:
            case ASH_DETERMINATION_9_BATCH_FINISHED:
                listReferenceFields.get(indexAshDeterminationWeightBeaker).getTextName().setText("Gewicht Tiegel");
                try {
                    listReferenceFields.get(indexAshDeterminationWeightBeaker).getTextValue().setText(
                            ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentProtocol().getAshWeightBeaker())
                    );
                } catch (Exception e) {
                    listReferenceFields.get(indexAshDeterminationWeightBeaker).getTextValue().setText("");
                }

                listReferenceFields.get(indexAshDeterminationWeightBeakerWithSample).getTextName().setText("Gewicht Tiegel mit Probe");
                try {
                    listReferenceFields.get(indexAshDeterminationWeightBeakerWithSample).getTextValue().setText(
                            ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentProtocol().getAshWeightBeakerWithSample())
                    );
                } catch (Exception e) {
                    listReferenceFields.get(indexAshDeterminationWeightBeakerWithSample).getTextValue().setText("");
                }


                listReferenceFields.get(indexAshDeterminationSampleName).getTextName().setText("Probennummer");
                try {
                    listReferenceFields.get(indexAshDeterminationSampleName).getTextValue().setText(ApplicationManager.getInstance().getCurrentProtocol().getAshSampleName());
                } catch (Exception e) {
                    listReferenceFields.get(indexAshDeterminationSampleName).getTextValue().setText("Nicht selektiert");
                }


                listReferenceFields.get(indexAshDeterminationBeakerName).getTextName().setText("Tiegelnummer");
                try {
                    listReferenceFields.get(indexAshDeterminationBeakerName).getTextValue().setText(ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName());
                } catch (Exception e) {
                    listReferenceFields.get(indexAshDeterminationBeakerName).getTextValue().setText("Nicht selektiert");
                }


                listReferenceFields.get(indexAshDeterminationDeltaWeight).getTextName().setText("Anzahl Glühungen");
                try {
                    listReferenceFields.get(indexAshDeterminationDeltaWeight).getTextValue().setText(
                            ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights().size() + "x");
                } catch (Exception e) {
                    listReferenceFields.get(indexAshDeterminationDeltaWeight).getTextValue().setText("");
                }
                break;

        }

    }

    @Override
    public void onStatisticChanged(SummaryStatistics statistic) {
    }


}
