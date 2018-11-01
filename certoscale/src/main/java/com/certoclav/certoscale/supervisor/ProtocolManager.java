package com.certoclav.certoscale.supervisor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.util.ESCPos;
import com.certoclav.library.application.ApplicationController;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ProtocolManager implements ScaleApplicationListener {
    private ESCPos posPrinter = null;

    public ProtocolManager() {
        posPrinter = new ESCPos();
    }


    public void printText(String text) {
        posPrinter.printString(text);
    }

    public String getProtocolFooter() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
        StringBuilder sb = new StringBuilder();

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_signature), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_signature)) == true) {
            sb.append("\n");
            sb.append(ApplicationController.getContext().getString(R.string.signature) + ":    _______________" + "\n");
            sb.append("\n");
            sb.append(ApplicationController.getContext().getString(R.string.verified_by) + ":  _______________" + "\n");
        }

        sb.append("\n");
        sb.append("\n");
        sb.append("\n");

        return sb.toString();

    }

    public String getProtocolHeader() {

        StringBuilder sb = new StringBuilder();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());

        //Print GLP and GMP Data which is independent of the application
        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_header), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_header)) == true) {

            sb.append(prefs.getString("preferences_glp_header", "") + "\n");
        }

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_date), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_date)) == true) {
            sb.append(Calendar.getInstance().getTime().toGMTString() + "\n");
        }

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_balance_id), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_balance_id)) == true) {
            sb.append(ApplicationController.getContext().getString(R.string.serialnumber) + ": " + Scale.getInstance().getSerialnumber() + "\n");

        }

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_balance_name), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_balance_name)) == true) {
            String balanceName = prefs.getString("preferences_glp_balance_name", "");
            if (balanceName.isEmpty()) {
                balanceName = Scale.getInstance().getScaleModel().getScaleModelName();
            }
            sb.append(ApplicationController.getContext().getString(R.string.balance_name) + ":" + " " + balanceName + "\n");
        }

        /*if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_user_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_user_name))==true) {
            sb.append(ApplicationController.getContext().getString(R.string.user_name)+": " + " "+ Scale.getInstance().getUser().getFirstName() + " " +Scale.getInstance().getUser().getLastName() + "\n");

        }*/
        sb.append(ApplicationController.getContext().getString(R.string.user_name) + ": " + " " + Scale.getInstance().getUser().getEmail() + "\n");


        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_project_name), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_project_name)) == true) {
            String projectName = prefs.getString("preferences_glp_project_name", "");
            sb.append(ApplicationController.getContext().getString(R.string.project_name) + ": " + projectName + "\n");
        }

        if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_application_name), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_application_name)) == true) {
            switch (Scale.getInstance().getScaleApplication()) {
                case PIPETTE_ADJUSTMENT_1_HOME:
                case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:
                case PIPETTE_ADJUSTMENT_3_FINISHED:


                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_pipette_adjustment) + "\n");
                    break;
                case WEIGHING:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_weighing) + "\n");
                    break;
                case PART_COUNTING:
                case PART_COUNTING_CALC_AWP:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_part_counting) + "\n");
                    break;
                case PERCENT_WEIGHING:
                case PERCENT_WEIGHING_CALC_REFERENCE:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_percent_weighing) + "\n");
                    break;

                case CHECK_WEIGHING:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_check_weighing) + "\n");
                    break;
                case ANIMAL_WEIGHING:
                case ANIMAL_WEIGHING_CALCULATING:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_animal_weighing) + "\n");
                    break;
                case FILLING:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_filling) + "\n");
                    break;
                case TOTALIZATION:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_totalization) + "\n");
                    break;
                case FORMULATION:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_formulation) + "\n");
                    break;
                case DIFFERENTIAL_WEIGHING:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_differential_weighing) + "\n");
                    break;
                case DENSITY_DETERMINATION:
                case DENSITY_DETERMINATION_STARTED:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_density_determination) + "\n");
                    break;
                case PEAK_HOLD:
                case PEAK_HOLD_STARTED:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_peak_hold) + "\n");
                    break;
                case INGREDIENT_COSTING:
                    sb.append(ApplicationController.getContext().getString(R.string.application) + ":  " + ApplicationController.getContext().getString(R.string.app_ingrediant_costing) + "\n");
                    break;
                case STATISTICAL_QUALITY_CONTROL_1_HOME:
                case STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED:
                case STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED:
                    sb.append(R.string.application + ":  " + R.string.app_statistical_quality_control + "\n");
                    break;


            }

        }
        return sb.toString();


    }


    public void printHeader(String header) {

        //Log.e("Print Header", header.getText().toString());

        posPrinter.printString(header);

    }

    public void printDate() {
        posPrinter.printString(Calendar.getInstance().getTime().toString() + "\n");
    }

    public void printBalanceId() {
        posPrinter.printString(ApplicationController.getContext().getString(R.string.serialnumber) + ": " + Scale.getInstance().getSerialnumber() + "\n");
    }

    public void printBalanceName(String balanceName) {
        posPrinter.printString(ApplicationController.getContext().getString(R.string.balance_name) + ":" + " " + balanceName + "\n");
    }

    public void printUserName() {
        posPrinter.printString(ApplicationController.getContext().getString(R.string.user_name) + ":" + " " + Scale.getInstance().getUser().getFirstName() + " " + Scale.getInstance().getUser().getLastName() + "\n");
    }

    public void printProjectName(String projectName) {
        posPrinter.printString(ApplicationController.getContext().getString(R.string.project_name) + ":" + projectName + "\n");
    }

    public void printApplicationName() {
        posPrinter.printString(ApplicationController.getContext().getString(R.string.application) + ":" + " " + Scale.getInstance().getScaleApplication().toString().replace("_", " ") + "\n");
    }

    public void printResults() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());


        switch (Scale.getInstance().getScaleApplication()) {
            case WEIGHING:

                posPrinter.printString(ApplicationController.getContext().getString(R.string.result) + ":" + " " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                posPrinter.printString(ApplicationController.getContext().getString(R.string.brutto) + ":" + " " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                posPrinter.printString(ApplicationController.getContext().getString(R.string.netto) + ":" + " " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                posPrinter.printString(ApplicationController.getContext().getString(R.string.tara) + ":" + " " + ApplicationManager.getInstance().getTareAsStringWithUnit());

                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_weigh_tara_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_weigh_tara_visible)) == true) {
                    posPrinter.printString(ApplicationController.getContext().getString(R.string.minimum_weight) + ":" + " " + ApplicationManager.getInstance().getUnderLimitAsStringWithUnit());
                }
                break;
            default:
                posPrinter.printString("Todo: printResults()"); //TODO
        }
    }

    public void printSignature() {
        posPrinter.printString(ApplicationController.getContext().getString(R.string.signature) + ":_______________" + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.verified_by) + ":_______________" + "\n");
    }


    public void printSQCBatch(SQC sqc) {

        posPrinter.printString(ApplicationController.getContext().getString(R.string.statistics_of) + " " + sqc.getName() + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.samples) + " " + String.format("%d", sqc.getStatistics().getN()) + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.average) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMean()) + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.maximum) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax()) + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.minimum) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMin()) + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.range) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax() - sqc.getStatistics().getMin()) + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.standard_deviation) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getStandardDeviation()) + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.total) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getSum()) + "\n");
        posPrinter.printString("\n");

        posPrinter.printString(ApplicationController.getContext().getString(R.string.nominal) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getNominal()) + "\n");

        posPrinter.printString(ApplicationController.getContext().getString(R.string.tolerance_plus_1) + " " + String.format("%d", sqc.getSqcPT1()) + "   " + String.format("%.1f", ((double) sqc.getSqcPT1() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.tolerance_plus_2) + " " + String.format("%d", sqc.getSqcPT2()) + "   " + String.format("%.1f", ((double) sqc.getSqcPT2() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.tolerance_minus_1) + " " + String.format("%d", sqc.getSqcNT1()) + "   " + String.format("%.1f", ((double) sqc.getSqcNT1() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");
        posPrinter.printString(ApplicationController.getContext().getString(R.string.tolerance_minus_2) + " " + String.format("%d", sqc.getSqcNT2()) + "   " + String.format("%.1f", ((double) sqc.getSqcNT2() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");


    }

    public String getSQCBatch(SQC sqc) {

        StringBuilder sb = new StringBuilder();

        sb.append(ApplicationController.getContext().getString(R.string.statistics_of) + " " + sqc.getName() + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.samples) + " " + String.format("%d", sqc.getStatistics().getN()) + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.average) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMean()) + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.maximum) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax()) + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.minimum) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMin()) + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.range) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax() - sqc.getStatistics().getMin()) + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.standard_deviation) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getStandardDeviation()) + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.total) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getSum()) + "\n");
        sb.append(("\n"));

        sb.append(ApplicationController.getContext().getString(R.string.nominal) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getNominal()) + "\n");

        sb.append(ApplicationController.getContext().getString(R.string.tolerance_plus_1) + " " + String.format("%d", sqc.getSqcPT1()) + "   " + String.format("%.1f", ((double) sqc.getSqcPT1() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.tolerance_plus_2) + " " + String.format("%d", sqc.getSqcPT2()) + "   " + String.format("%.1f", ((double) sqc.getSqcPT2() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.tolerance_minus_1) + " " + String.format("%d", sqc.getSqcNT1()) + "   " + String.format("%.1f", ((double) sqc.getSqcNT1() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");
        sb.append(ApplicationController.getContext().getString(R.string.tolerance_minus_2) + " " + String.format("%d", sqc.getSqcNT2()) + "   " + String.format("%.1f", ((double) sqc.getSqcNT2() / (double) sqc.getStatistics().getN()) * 100) + "%" + "\n");

        return sb.toString();
    }

    public void printStatistics() {


        switch (Scale.getInstance().getScaleApplication()) {


            case PART_COUNTING:
                posPrinter.printString(ApplicationController.getContext().getString(R.string.statistics) + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.samples) + " " + String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()) + " \n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.total) + " " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getSum()) + " PCS\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.average) + " " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMean()) + " PCS\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.standard_deviation) + " " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()) + " PCS\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.minimum) + " " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMin()) + " PCS\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.maximum) + " " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMax()) + " PCS\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.range) + " " + String.format("%.0f", (ApplicationManager.getInstance().getStats().getStatistic().getMax() - ApplicationManager.getInstance().getStats().getStatistic().getMin())) + " PCS\n");

                break;

            default:
                posPrinter.printString(ApplicationController.getContext().getString(R.string.statistics) + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.samples) + " " + String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()) + " \n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.total) + " " + ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit() + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.average) + " " + ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit() + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.standard_deviation) + " " + ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit() + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.minimum) + " " + ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit() + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.maximum) + " " + ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit() + "\n");
                posPrinter.printString(ApplicationController.getContext().getString(R.string.range) + " " + ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit() + "\n");
                break;
        }
    }


    public String getApplicationData() {


        StringBuilder sb = new StringBuilder();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
        switch (Scale.getInstance().getScaleApplication()) {
            case WEIGHING:
                sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_weigh_print_min), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_weigh_print_min)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.minimum_weight) + ": " + ApplicationManager.getInstance().getUnderLimitAsStringWithUnit() + "\n");
                }
                break;

            case PART_COUNTING:

                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_sample_size), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_sample_size)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                }
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_apw), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_apw)) == true) {
                    sb.append("APW: " + ApplicationManager.getInstance().getAveragePieceWeightAsStringWithUnit() + "\n");
                }

                String cmode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_counting_mode), "");
                if (cmode.equals("2")) {
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_under_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_under_limit)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.under_limit) + ": " + ApplicationManager.getInstance().getUnderLimitPiecesAsString() + " pcs" + "\n");
                    }
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_over_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_over_limit)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.over_limit) + ": " + ApplicationManager.getInstance().getOverlimitPiecesAsString() + " PCS" + "\n");
                    }
                }
                if (cmode.equals("3")) {
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_target)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.target) + ": " + ApplicationManager.getInstance().getTargetPiecesAsString() + " PCS" + "\n");
                    }
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_difference_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_difference_visible)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getDifferenceAsString() + " PCS" + "\n");
                    }
                }
                break;

            case PERCENT_WEIGHING:
                sb.append(ApplicationController.getContext().getString(R.string.percentage) + ": " + ApplicationManager.getInstance().getPercent() + " %" + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");

                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_reference), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_reference)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.reference_weight) + ": " + ApplicationManager.getInstance().getReferenceWeightAsStringWithUnit() + "\n");
                    sb.append(ApplicationController.getContext().getString(R.string.reference_adjustment) + ": " + ApplicationManager.getInstance().getCurrentLibrary().getReferenceweightAdjustment() + " %" + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDifferenceInGram()) + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference_percent), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference_percent)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getDifferenceInPercent() + " %" + "\n");
                }
                break;

            case CHECK_WEIGHING:
                String cmode_check = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_check_limitmode), "");
                String checklimitmode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_check_limitmode), "");
                double current = ApplicationManager.getInstance().getTaredValueInGram();
                double under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                double over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
                if (checklimitmode.equals("1")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                    over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
                }
                if (checklimitmode.equals("2")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getCheckNominaldouble() - ApplicationManager.getInstance().getCheckNominalToleranceUnderdouble();
                    over = ApplicationManager.getInstance().getCheckNominaldouble() + ApplicationManager.getInstance().getCheckNominalToleranceOverdouble();
                }

                if (current < under) {
                    sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationController.getContext().getString(R.string.under) + "\n");
                }
                if (current > over) {
                    sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationController.getContext().getString(R.string.over) + "\n");
                }
                if (current >= under && current <= over) {
                    sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + "OK" + "\n");
                }

                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");

                if (cmode_check.equals("1")) {
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_underlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_underlimit)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.under_limit) + ": " + ApplicationManager.getInstance().getUnderLimitCheckWeighingAsStringWithUnit() + "\n");
                    }
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overlimit)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.over_limit) + ": " + ApplicationManager.getInstance().getOverLimitCheckWeighingAsStringWithUnit() + "\n");
                    }
                }

                if (cmode_check.equals("2")) {
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.target) + ": " + ApplicationManager.getInstance().getCheckNominalAsStringWithUnit() + "\n");
                    }
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.under_toleranc) + ": " + ApplicationManager.getInstance().getCheckNominalToleranceUnderAsStringWithUnit() + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.over_tolerance) + ": " + ApplicationManager.getInstance().getCheckNominalToleranceOverAsStringWithUnit() + "\n");
                    }
                }

                if (cmode_check.equals("3")) {
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.target) + ": " + ApplicationManager.getInstance().getCheckNominalAsStringWithUnit() + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.under_toleranc) + ": " + ApplicationManager.getInstance().getCheckNominalToleranceUnderPercent() + " %" + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.over_tolerance) + ": " + ApplicationManager.getInstance().getCheckNominalToleranceOverPercent() + " %" + "\n");
                    }


                }
                break;

            case ANIMAL_WEIGHING:
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_animal_print_measuring), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_animal_print_measuring)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.averaging_time) + ": " + ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime() + " s" + "\n");
                }
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.final_weight) + ": " + ApplicationManager.getInstance().getAnimalWeightAsStringWithUnit() + "\n");
                break;

            case FILLING:
                sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_target)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.target) + ": " + ApplicationManager.getInstance().getTargetAsStringWithUnit() + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_differencew), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_differencew)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getDifferenceFillingAsStringWithUnit() + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_differencep_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_differencep_visible)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getFillingDifferenceInPercent() + " %" + "\n");
                }
                break;

            case TOTALIZATION:
                sb.append(ApplicationController.getContext().getString(R.string.total) + ": " + ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit() + "\n");
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_samples), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_samples)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.samples) + ": " + Long.toString(ApplicationManager.getInstance().getStats().getStatistic().getN()) + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_average), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_average)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.average) + ": " + ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit() + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_standard), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_standard)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.standard_deviation) + ": " + ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit() + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_minimum), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_minimum)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.minimum) + ": " + ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit() + "\n");
                }
                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_maximum), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_maximum)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.maximum) + ": " + ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit() + "\n");
                }
                sb.append(ApplicationController.getContext().getString(R.string.range) + ": " + ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit() + "\n");


                sb.append("---" + ApplicationController.getContext().getString(R.string.sample_data) + "---\n");
                for (int i = 0; i < ApplicationManager.getInstance().getStats().getSamples().size(); i++) {
                    sb.append(ApplicationController.getContext().getString(R.string.item) + " " + String.format("%d", i) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i)) + "\n");

                }

                break;

            case FORMULATION:
            case FORMULATION_FREE:
                if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
                    sb.append(ApplicationController.getContext().getString(R.string.name) + ": " + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName() + "\n");
                    double formulationTotal = 0;
                    double formulationTotalTarget = 0;
                    double formulationTotalDifference = 0;
                    int formulationcounter = 0;
                    while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {


                        sb.append(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getDescription() + "\n");

                        formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getScaledWeight();
                        formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();
                        double currentdifference = Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getScaledWeight() - ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight()) / Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getScaledWeight());
                        currentdifference = currentdifference * 100;

                        sb.append(ApplicationController.getContext().getString(R.string.target) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getScaledWeight()) + "\n");
                        sb.append(ApplicationController.getContext().getString(R.string.actual) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight()) + "\n");
                        sb.append(ApplicationController.getContext().getString(R.string.diff) + "(%): " + String.format("%.2f", currentdifference) + " %\n");

                        formulationcounter++;
                    }


                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_target)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.total_target) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotalTarget) + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.total_actual) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotal) + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
                        formulationTotalDifference = Math.abs(formulationTotal - formulationTotalTarget) / Math.abs(formulationTotalTarget);
                        formulationTotalDifference = formulationTotalDifference * 100;
                        sb.append(ApplicationController.getContext().getString(R.string.total_diff) + ": " + String.format("%.2f", formulationTotalDifference) + " %" + "\n");
                    }
                } else {

                    Toast.makeText(ApplicationController.getContext(), "No results to print", Toast.LENGTH_LONG).show();
                }


                break;

            case DIFFERENTIAL_WEIGHING:
                sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");

                if (ApplicationManager.getInstance().getCurrentItem() != null) {
                    sb.append(ApplicationController.getContext().getString(R.string.item_name) + ": " + ApplicationManager.getInstance().getCurrentItem().getName() + "\n");
                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_initial), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_initial)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.initial) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentItem().getWeight()) + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_final), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_final)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.final_weight) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencew), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencew)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDifferenceInGram()) + "\n");
                    }

                    if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencep), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencep)) == true) {
                        sb.append(ApplicationController.getContext().getString(R.string.difference) + ": " + ApplicationManager.getInstance().getDifferenceToInitialInPercentAsString() + "\n");
                    }
                }
                break;
            case DENSITY_DETERMINATION:
                String densityliquidtype = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_density_liquidtyp), "");
                String densitymode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_density_mode), "");


                if (densitymode.equals("1") || densitymode.equals("2")) {
                    sb.append(ApplicationController.getContext().getString(R.string.solid));
                    sb.append(ApplicationController.getContext().getString(R.string.solid));
                } else if (densitymode.equals("3")) {
                    sb.append(ApplicationController.getContext().getString(R.string.liquid));
                }

                if (densityliquidtype.equals("1")) {
                    ApplicationManager.getInstance().getCurrentLibrary().setDensityLiquidDensity(ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()));
                }

                sb.append(ApplicationController.getContext().getString(R.string.density_determ) + ": " + String.format("%.4f", ApplicationManager.getInstance().getDensity()) + " g/cm3" + "\n");


                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");

                sb.append(ApplicationController.getContext().getString(R.string.weight_in_air) + ": " + String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_air()) + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.weight_in_liquid) + ": " + String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_liquid()) + "\n");

                if (densityliquidtype.equals("1")) {
                    sb.append(ApplicationController.getContext().getString(R.string.water_temp) + ": " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()) + "\n");
                }

                sb.append(ApplicationController.getContext().getString(R.string.liquid_density) + ": " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity()) + " g/cm3" + "\n");


                break;
            case PEAK_HOLD_STARTED:
            case PEAK_HOLD:
                sb.append(ApplicationController.getContext().getString(R.string.peak_weight) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getPeakHoldMaximum()) + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.brutto) + ": " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.tara) + ": " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.netto) + ": " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");

                if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_peak_print_stableonly), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_peak_print_stableonly)) == true) {
                    sb.append(ApplicationController.getContext().getString(R.string.stable_only) + " : " + ApplicationController.getContext().getString(R.string.yes) + "\n");
                }
                sb.append(ApplicationController.getContext().getString(R.string.stable_only) + " : " + ApplicationController.getContext().getString(R.string.no) + "\n");
                break;

            case INGREDIENT_COSTING:

                List<Item> costList = ApplicationManager.getInstance().getIngrediantCostList();


                for (int i = 0; i < costList.size(); i++) {
                    sb.append(costList.get(i).getName() + "\n");
                    sb.append(ApplicationController.getContext().getString(R.string.item_weight) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(costList.get(i).getWeight()) + "\n");
                    sb.append(ApplicationController.getContext().getString(R.string.item_cost) + ": " + String.format(Locale.US, "%.2f", costList.get(i).getCost()) + " " + ApplicationManager.getInstance().getCurrency() + "\n");
                }


                sb.append("------------------\n");
                sb.append(ApplicationController.getContext().getString(R.string.total_items) + ": " + costList.size() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.total_weight) + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getIngrediantTotalWeight()) + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.total_cost) + ": " + String.format(Locale.US, "%.2f", ApplicationManager.getInstance().getIngrediantTotalCost()) + " " + ApplicationManager.getInstance().getCurrency() + "\n");


                break;

            case PIPETTE_ADJUSTMENT_1_HOME:
            case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:
            case PIPETTE_ADJUSTMENT_3_FINISHED:
                sb.append(ApplicationController.getContext().getString(R.string.pipette_name) + ":   " + ApplicationManager.getInstance().getPipette_name() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.pipette_number) + ": " + ApplicationManager.getInstance().getPipette_number() + "\n");

                sb.append(ApplicationController.getContext().getString(R.string.nominal_volume) + ": " + ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.water_temp) + ":     " + ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp() + "\n");
                sb.append(ApplicationController.getContext().getString(R.string.pressure) + ":       " + ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure() + "\n");


                sb.append("\n");
                sb.append("---" + ApplicationController.getContext().getString(R.string.inaccuracy) + "---\n");

                double meanError = Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getMean() - ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
                sb.append(ApplicationController.getContext().getString(R.string.mean_error) + ": " + String.format("%.4f", meanError) + " ml\n");
                double meanErrorPercent = Math.abs(meanError / ApplicationManager.getInstance().getStats().getStatistic().getMean()) * 100;
                sb.append(ApplicationController.getContext().getString(R.string.mean_error) + ": " + String.format("%.4f", meanErrorPercent) + " %\n");
                sb.append(ApplicationController.getContext().getString(R.string.limit) + ":      " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy()) + " %\n");


                sb.append("\n");
                sb.append("---" + ApplicationController.getContext().getString(R.string.imprecision) + "---\n");
                sb.append(ApplicationController.getContext().getString(R.string.standard_deviation) + ": " + String.format("%.4f", ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()) + " ml\n");
                double standardError = Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation() / ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()) * 100;
                sb.append(ApplicationController.getContext().getString(R.string.error_cs) + ":           " + String.format("%.4f", standardError) + " %\n");
                sb.append(ApplicationController.getContext().getString(R.string.limit_cv) + ":           " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()) + " %\n");

                if (meanErrorPercent <= ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy() && standardError <= ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()) {
                    sb.append("\n");
                    sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationController.getContext().getString(R.string.pass));
                    sb.append("\n");
                } else {
                    sb.append("\n");
                    sb.append(ApplicationController.getContext().getString(R.string.result) + ": " + ApplicationController.getContext().getString(R.string.fail));
                    sb.append("\n");
                }

                sb.append("\n");
                sb.append(ApplicationController.getContext().getString(R.string.number_of_samples) + ": " + ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples() + "\n");

                double standardDeviation = ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation();
                double mean = ApplicationManager.getInstance().getStats().getStatistic().getMean();
                int nstandard1 = 0;
                int nstandard2 = 0;
                int pstandard1 = 0;
                int pstandard2 = 0;
                double pcurrent = 0;

                for (int i = 0; i < ApplicationManager.getInstance().getStats().getSamples().size(); i++) {
                    pcurrent = mean - ApplicationManager.getInstance().getStats().getSamples().get(i);
                    if (pcurrent < 0) {
                        if (Math.abs(pcurrent) > standardDeviation) {
                            if (Math.abs(pcurrent) > 2 * standardDeviation) {
                                nstandard2++;
                            } else {
                                nstandard1++;
                            }
                        }
                    } else {
                        if (Math.abs(pcurrent) > standardDeviation) {
                            if (Math.abs(pcurrent) > 2 * standardDeviation) {
                                pstandard2++;
                            } else {
                                pstandard1++;
                            }
                        }

                    }
                }

                sb.append("> +2s: " + pstandard2 + "\n");
                sb.append("> +2s: " + pstandard1 + "\n");
                sb.append("*+1S > " + ApplicationController.getContext().getString(R.string.mean) + " > –1S: " + (ApplicationManager.getInstance().getStats().getSamples().size() - pstandard1 - pstandard2 - nstandard1 - nstandard2) + "\n");
                sb.append("- +2s: " + nstandard1 + "\n");
                sb.append("- +2s: " + nstandard2 + "\n");

                sb.append("\n");


                sb.append("---" + ApplicationController.getContext().getString(R.string.sample_data) + "---\n");
                for (int i = 0; i < ApplicationManager.getInstance().getStats().getSamples().size(); i++) {
                    sb.append(ApplicationController.getContext().getString(R.string.item) + " " + String.format("%02d: ", i) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i)) + "\n");

                }

                break;
            case ASH_DETERMINATION_1_HOME:
            case ASH_DETERMINATION_2_ENTER_NAME_SAMPLE:
            case ASH_DETERMINATION_3_ENTER_NAME_BEAKER:
            case ASH_DETERMINATION_4_WEIGH_BEAKER:
            case ASH_DETERMINATION_5_WEIGHING_SAMPLE:
            case ASH_DETERMINATION_6_WAIT_FOR_GLOWING:
            case ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE:
            case ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT:
            case ASH_DETERMINATION_9_BATCH_FINISHED:
                sb.append("\n");
                sb.append("Probennummer" + ": " + ApplicationManager.getInstance().getCurrentProtocol().getAshSampleName() + "\n");
                try {
                    for (int i = 0; i < ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights().size(); i++) {
                        sb.append((i + 1) + ". Glühen" + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights().get(i)) + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sb.append("Asche Gewicht" + ": " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentProtocol().getAshResultInGram()) + "\n");
                sb.append("Asche Prozent" + ": " + String.format(Locale.US, "%.3f", ApplicationManager.getInstance().getCurrentProtocol().getAshResultInPercent()) + "%\n");
                break;


            default:
                Toast.makeText(ApplicationController.getContext(), R.string.not_implemented, Toast.LENGTH_LONG).show();
                break;


        }
        return sb.toString();
    }

    @Override
    public void onApplicationChange(ScaleApplication application) {
        DatabaseService db = new DatabaseService(ApplicationController.getContext());
        //DateFormat df = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        //String date = df.format(Calendar.getInstance().getTime());

        switch (application) {
            case WEIGHING:
                break;

            case PIPETTE_ADJUSTMENT_3_FINISHED:
                //  db.insertProtocol(new Protocol("","PIPETTE ADJUSTMENT", Scale.getInstance().getUser().getEmail(), Scale.getInstance().getSafetyKey(), Calendar.getInstance().getTime().toGMTString(),"private",getPipetteAdjustmentProtocol()));

                break;
        }

    }


}





