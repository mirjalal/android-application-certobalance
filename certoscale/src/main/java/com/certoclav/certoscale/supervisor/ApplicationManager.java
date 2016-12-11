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
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager {

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
            20f,
            0f,
            0,
            new Date(),
            true
    );

    private ArrayList<Double> statisticsArray = new ArrayList<Double>();
    public Double getAwpCalcSampleSize() {
        return currentLibrary.getSampleSize();
    }

    public String getAwpCalcSampleSizeAsString(){
        return String.format("%.6f", getAwpCalcSampleSize()) + " g";
    }

    public ArrayList<Double> getStatisticsArray() {
        return statisticsArray;
    }

    public void setStatisticsArray(ArrayList<Double> statisticsArray) {
        this.statisticsArray = statisticsArray;
    }

    public void setAwpCalcSampleSize(int awpCalcSampleSize) {
        currentLibrary.setSampleSize(awpCalcSampleSize);
    }


    private static final int UNIT_GRAM = 1;
    private static final int UNIT_PIECES = 2;

    private static ApplicationManager instance = new ApplicationManager();

    public void setTareInGram(Double tareInGram) {
        this.tareInGram = tareInGram;
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

    private Double tareInGram = 0d;
    private int unit = UNIT_GRAM;

    public String getUnitAsString(){
        switch (Scale.getInstance().getScaleApplication()){
            case PART_COUNTING:
                return "pcs";

            case PERCENT_WEIGHING:
                return "%";

            default:
                return "g";
        }
    }


    public Double getSumInGram(){
        return Scale.getInstance().getWeightInGram();
    }

    public int getSumInPieces(){
       return (int) Math.round(Scale.getInstance().getWeightInGram()/currentLibrary.getAveragePieceWeight());
    }

    public Double getTareInGram(){
        return tareInGram;
    }

    public int getTareInPieces(){
        return (int) Math.round(tareInGram/currentLibrary.getAveragePieceWeight());
    }

    public int getLoadInPercent(){
        return (int) Math.round((Scale.getInstance().getWeightInGram()/AppConstants.WEIGHT_MAX)*100.0);
    }


    public String getSumAsStringWithUnit() {

        switch (Scale.getInstance().getScaleApplication()){
            case PART_COUNTING:
                return String.format("%d", getSumInPieces()) + " "+ getUnitAsString();
            default:
                return String.format("%.4f", getSumInGram()) + " "+ getUnitAsString();
        }

    }

    public String getTaredValueAsStringWithUnit() {

        switch (Scale.getInstance().getScaleApplication()){
            case PART_COUNTING:
                return String.format("%d", getSumInPieces() - getTareInPieces() ) + " "+ getUnitAsString();
            default:
                return String.format("%.4f", getSumInGram() - getTareInGram()) + " "+ getUnitAsString();
        }

    }


    public String getTareAsStringWithUnit() {
        switch (Scale.getInstance().getScaleApplication()){
            case PART_COUNTING:
                return String.format("%d", getTareInPieces()) + " "+ getUnitAsString();
            default:
                return String.format("%.4f", getTareInGram()) + " "+ getUnitAsString();
        }
    }
    private ApplicationManager(){

    }

    public static synchronized ApplicationManager getInstance(){
        return instance;

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
        return String.format("%.4f", getSumInGram() - getTareInGram()) + " g";
    }

    public void accumulateStatistics() {
        switch (Scale.getInstance().getScaleApplication()){
            case PART_COUNTING:
                statisticsArray.add((double) (getSumInPieces()-getTareInPieces()));
            break;
            default:
                statisticsArray.add(getTaredValueInGram());
                break;
        }

    }
    public void clearStatistics(){
        statisticsArray.clear();
    }

    public void showStatisticsNotification(final Context eContext, DialogInterface.OnDismissListener listener) {
        try{
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_statistics);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Statistics");
            SummaryStatistics statistic = new SummaryStatistics();
            for(Double value : statisticsArray){
                statistic.addValue(value);
            }
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_sample_number)).setText(""+ statistic.getN());
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_average)).setText(String.format("%.4f",statistic.getMean()) + " "+getUnitAsString());
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_maximum)).setText(String.format("%.4f",statistic.getMax())  + " "+getUnitAsString());
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_minimum)).setText(String.format("%.4f",statistic.getMin())  + " "+getUnitAsString());
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_range)).setText(String.format("%.4f",statistic.getVariance())  + " "+getUnitAsString());
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_stdev)).setText(String.format("%.4f",statistic.getStandardDeviation()) +   " "+getUnitAsString());
            ((TextView)dialog.findViewById(R.id.dialog_statistics_text_total)).setText(String.format("%.4f",statistic.getSum()) +  " "+getUnitAsString());

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
                    Toast.makeText(eContext,"Todo: Send statistics to COM port",Toast.LENGTH_LONG).show();
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

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getAveragePieceWeightAsStringInGram() {
        return String.format("%.5f",currentLibrary.getAveragePieceWeight());
    }
}
