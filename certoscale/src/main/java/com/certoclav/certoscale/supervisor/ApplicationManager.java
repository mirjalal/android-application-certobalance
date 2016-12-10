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
import com.certoclav.certoscale.model.Scale;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager {

    private ArrayList<Float> statisticsArray = new ArrayList<Float>();
    public float getAwpCalcSampleSize() {
        return awpCalcSampleSize;
    }

    public String getAwpCalcSampleSizeAsString(){
        return String.format("%.6f", getAwpCalcSampleSize()) + " g";
    }

    public ArrayList<Float> getStatisticsArray() {
        return statisticsArray;
    }

    public void setStatisticsArray(ArrayList<Float> statisticsArray) {
        this.statisticsArray = statisticsArray;
    }

    public void setAwpCalcSampleSize(float awpCalcSampleSize) {
        this.awpCalcSampleSize = awpCalcSampleSize;
    }

    private float awpCalcSampleSize = 10;
    private static final int UNIT_GRAM = 1;
    private static final int UNIT_PIECES = 2;

    private static ApplicationManager instance = new ApplicationManager();
    private float averagePieceWeight = 1;

    public void setTareInGram(float tareInGram) {
        this.tareInGram = tareInGram;
    }

    public float getAveragePieceWeightInGram() {
        return averagePieceWeight;
    }

    public void setAveragePieceWeightInGram(float averagePieceWeight) {
        this.averagePieceWeight = averagePieceWeight;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    private float tareInGram = 0;
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


    public float getSumInGram(){
        return Scale.getInstance().getWeightInGram();
    }

    public int getSumInPieces(){
       return Math.round(Scale.getInstance().getWeightInGram()/averagePieceWeight);
    }

    public float getTareInGram(){
        return tareInGram;
    }

    public int getTareInPieces(){
        return Math.round(tareInGram/averagePieceWeight);
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

    public float getTaredValueInGram() {
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
                statisticsArray.add((float)(getSumInPieces()-getTareInPieces()));
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
            for(Float value : statisticsArray){
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
}
