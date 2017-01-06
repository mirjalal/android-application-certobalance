package com.certoclav.certoscale.supervisor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.listener.StatisticListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager {

    ArrayList<StatisticListener> statisticListeners = new ArrayList<StatisticListener>();

    public void setOnStatisticListener (StatisticListener listener){
        this.statisticListeners.add(listener);
    }
    public void removeOnStatisticListener (StatisticListener listener){
        this.statisticListeners.remove(listener);
    }

    public static synchronized ApplicationManager getInstance() {
        return instance;

    }

    public SummaryStatistics getStatistic() {
        return statistic;
    }



    private SummaryStatistics statistic = new SummaryStatistics();

    private static ApplicationManager instance = new ApplicationManager();

    public Library getCurrentLibrary() {
        return currentLibrary;
    }

    public void setCurrentLibrary(Library currentLibrary) {
        this.currentLibrary = currentLibrary;
    }

    private Library currentLibrary = new Library(
            "admin",
            ScaleApplication.WEIGHING.ordinal(),
            "",
            0,
            "default library",
            0f,
            10f,
            10f,
            1f,
            0f,
            0f,
            20f,
            0f,
            0f,
            0f,
            0f,
            0,
            new Date(),
            true,
            1f
    );


    public String getAnimalWeight() {

        return String.format("%.6f", animalWeight);

    }
    public void setAnimalWeight(Double animalWeight) {

        this.animalWeight = animalWeight;
    }

    private Double animalWeight = 0d;

     public Double getAwpCalcSampleSize() {
        return currentLibrary.getSampleSize();
    }

    public String getAwpCalcSampleSizeAsString() {
        return String.format("%.6f", getAwpCalcSampleSize()) + " g";
    }



    public void setAwpCalcSampleSize(int awpCalcSampleSize) {
        currentLibrary.setSampleSize(awpCalcSampleSize);
    }

    public void setUnderLimit(int underLimit) {
        currentLibrary.setUnderLimit(underLimit);
    }

    public void setUnderLimitPieces(int underLimitPieces) {
        currentLibrary.setUnderLimitPieces(underLimitPieces);
    }

    public void setTarget(int target) {
        currentLibrary.setTarget(target);
    }

    public void setOverLimit(int overLimit) {
        currentLibrary.setOverLimit(overLimit);
    }

    public void setOverLimitPieces(int overLimitPieces) {
        currentLibrary.setOverLimitPieces(overLimitPieces);
    }


    private static final int UNIT_GRAM = 1;
    private static final int UNIT_PIECES = 2;


    public void setTareInGram(Double tareInGram) {
        currentLibrary.setTara(tareInGram);
    }

    public Double getAveragePieceWeightInGram() {
        return currentLibrary.getAveragePieceWeight();
    }

    public void setAveragePieceWeightInGram(Double averagePieceWeight) {
        currentLibrary.setAveragePieceWeight(averagePieceWeight);
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }


    private int unit = UNIT_GRAM;

    public String getUnitAsString() {
        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return "pcs";

            case PERCENT_WEIGHING:
                return "%";

            default:
                return "g";
        }
    }


    public Double getSumInGram() {
        return Scale.getInstance().getWeightInGram();
    }

    public int getSumInPieces() {
        return (int) Math.round(Scale.getInstance().getWeightInGram() / currentLibrary.getAveragePieceWeight());
    }

    public Double getTareInGram() {
        return currentLibrary.getTara();
    }

    public int getTareInPieces() {
        return (int) Math.round(currentLibrary.getTara() / currentLibrary.getAveragePieceWeight());
    }

    public int getLoadInPercent() {
        return (int) Math.round((Scale.getInstance().getWeightInGram() / AppConstants.WEIGHT_MAX) * 100.0);
    }


    public String getSumAsStringWithUnit() {

        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return String.format("%d", getSumInPieces()) + " " + getUnitAsString();
            default:
                return String.format("%.4f", getSumInGram()) + " " + getUnitAsString();
        }

    }

    public String getSumAsString() {
        return String.format("%.4f", getSumInGram());
    }


    public String getTaredValueAsStringWithUnit() {

        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return String.format("%d", getSumInPieces() - getTareInPieces()) + " " + getUnitAsString();
            default:
                return String.format("%.4f", getSumInGram() - getTareInGram()) + " " + getUnitAsString();
        }

    }


    public String getTareAsStringWithUnit() {
        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return String.format("%d", getTareInPieces()) + " " + getUnitAsString();
            default:
                return String.format("%.4f", getTareInGram()) + " " + getUnitAsString();
        }
    }


    public String getTareAsString() {
        return String.format("%d", getTareInPieces());
    }

    private ApplicationManager() {

    }


    public String getLoadInGramAsStringWithUnit() {
        return String.format("%.4f", getSumInGram()) + " g";
    }

    public Double getTaredValueInGram() {
        return getSumInGram() - getTareInGram();
    }

    public String getSumAsStringInGram() {
        return String.format("%.4f", getSumInGram()) + " g";
    }

    public String getTaredValueAsStringInGram() {
        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return String.format("%d", getSumInPieces() - getTareInPieces()) + " g";
            default:
                return String.format("%.4f", getSumInGram() - getTareInGram()) + " g";
        }
    }

    public void accumulateStatistics() {
        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                statistic.addValue((double) (getSumInPieces() - getTareInPieces()));
                break;
            case ANIMAL_WEIGHING:
                statistic.addValue((double)  animalWeight);

                break;

            default:
                statistic.addValue(getTaredValueInGram());
                break;
        }
        for (StatisticListener listener : statisticListeners){
            listener.onStatisticChanged(statistic);
        }


    }

    public void clearStatistics() {
        statistic.clear();
        for(StatisticListener listener : statisticListeners){
            listener.onStatisticChanged(statistic);
        }
    }

    public void showStatisticsNotification(final Context eContext, DialogInterface.OnDismissListener listener) {
        try {
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_statistics);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Statistics");
 //           statistic = new SummaryStatistics();
 //           for (Double value : statisticsArray) {
 //               statistic.addValue(value);
 //           }
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_sample_number)).setText("" + statistic.getN());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_average)).setText(String.format("%.4f", statistic.getMean()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_maximum)).setText(String.format("%.4f", statistic.getMax()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_minimum)).setText(String.format("%.4f", statistic.getMin()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_range)).setText(String.format("%.4f", statistic.getVariance()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_stdev)).setText(String.format("%.4f", statistic.getStandardDeviation()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_total)).setText(String.format("%.4f", statistic.getSum()) + " " + getUnitAsString());

            // set the custom dialog components - text, image and button

            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_statistics_button_clear);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clearStatistics();
                    dialog.dismiss();
                }
            });
            Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_statistics_button_print);
            dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(eContext, "Todo: Send statistics to COM port", Toast.LENGTH_LONG).show();
                }
            });
            Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_button_close);
            dialogButtonClose.setOnClickListener(new View.OnClickListener() {
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

    public String getAveragePieceWeightAsStringInGram() {
        return String.format("%.5f", currentLibrary.getAveragePieceWeight());
    }

    public String getUnderLimitAsStringInGram() {
        return String.format("%.5f", currentLibrary.getUnderLimit());
    }

    public String getUnderLimitPiecesAsStringInGram() {
        return String.format("%.1f", currentLibrary.getUnderLimitPieces());
    }

    public String getUnderLimitPiecesAsString() {
        return String.format("%.1f", currentLibrary.getUnderLimitPieces());
    }


    public String getOverlimitPiecesAsString() {
        return String.format("%.1f", currentLibrary.getOverLimitPieces());
    }

    ;

    public String getDifferenceAsString() {
        return String.format("%.1f", getSumInPieces() - getTareInPieces() - currentLibrary.getTarget());
    }

    ;

    public String getTargetPiecesAsString() {
        return String.format("%.1f", currentLibrary.getTargetPieces());
    }


    public String getOverlimitPiecesAsStringInGram() {
        return String.format("%.1f", currentLibrary.getOverLimitPieces());
    }

    ;


    public Double getUnderLimitValueInGram() {
        return currentLibrary.getUnderLimit();
    }

    public String getUnderLimitAsStringWithUnit() {
        switch (Scale.getInstance().getScaleApplication()) {
            case WEIGHING:
                return String.format("%.4f", currentLibrary.getUnderLimit()) + getUnitAsString();
            case PART_COUNTING:
                return String.format("%d", currentLibrary.getUnderLimitPieces()) + getUnitAsString();
        }
        return "todo";

    }

    public String getReferenceWeightAsStringInGram() {
        return String.format("%.4f", (currentLibrary.getReferenceWeight() * currentLibrary.getReferenceweightAdjustment() / 100)) + " " + "g";
    }

    public String getDifferenceInGram() {
        double ref=(currentLibrary.getReferenceWeight() * currentLibrary.getReferenceweightAdjustment() / 100);
        double netto=(getSumInGram() - getTareInGram());

        double difference=netto-ref;


        return String.format("%.4f", difference);
    }


    public String getDifferenceInPercent() {

        double ref=(currentLibrary.getReferenceWeight() * currentLibrary.getReferenceweightAdjustment() / 100);

        if (ref==0){
            return String.format("%.4f", (ref));
        }else {

            double netto = (getSumInGram() - getTareInGram());

            double percent = ((netto/ref)*100)-100;


            return String.format("%.4f", percent);
        }
    }


    public String getPercent() {

        double ref=(currentLibrary.getReferenceWeight() * currentLibrary.getReferenceweightAdjustment() / 100);

        if (ref==0){
            return String.format("%.4f", (ref));
        }else {

            double netto = (getSumInGram() - getTareInGram());

            double percent = (netto/ref)*100;


            return String.format("%.4f", percent);
        }
    }
}