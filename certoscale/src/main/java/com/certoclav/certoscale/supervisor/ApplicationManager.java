package com.certoclav.certoscale.supervisor;

import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Scale;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager {

    public float getAwpCalcSampleSize() {
        return awpCalcSampleSize;
    }

    public String getAwpCalcSampleSizeAsString(){
        return String.format("%.6f", getAwpCalcSampleSize()) + " g";
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
}
