package com.certoclav.certoscale.supervisor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.StatisticListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.menu.MenuActivity;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager implements WeightListener , ScaleApplicationListener {

    private boolean  peakholdactivated=false;
    private double Ingrediant_Unit_Cost=0;
    private double Ingrediant_Total_Weight=0;
    private double Ingrediant_Total_Cost=0;

    private Double weighOld = 0d;
    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    private Item currentItem = null;

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
            1f,
            0f,
            0f,
            0f,
            0f,
            0f
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

    public void setUnderLimitCheckWeighing(double underLimit) {
        currentLibrary.setUnderLimitCheckWeighing(underLimit);
    }

    public void setCheckNominal(double nominal){
        currentLibrary.setCheckNominal(nominal);
    }

    public void setCheckNominalToleranceUnder(double NominalToleranceUnder){
        currentLibrary.setCheckNominalToleranceUnder(NominalToleranceUnder);
    }

    public void setCheckNominalToleranceUnderPercent(double NominalToleranceUnderPercent){
        currentLibrary.setCheckNominalToleranceUnderPercent(NominalToleranceUnderPercent);
    }

    public void setCheckNominalToleranceOverPercent(double NominalToleranceOverPercent){
        currentLibrary.setCheckNominalToleranceOverPercent(NominalToleranceOverPercent);
    }
    public void setCheckNominalToleranceOver(double NominalToleranceOver){
        currentLibrary.setCheckNominalToleranceOver(NominalToleranceOver);
    }


    public void setOverLimitCheckWeighing(double underLimit) {
        currentLibrary.setOverLimitCheckWeighing(underLimit);
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
        Scale.getInstance().setOnWeightListener(this);
        Scale.getInstance().setOnApplicationListener(this);
    }


    public String getLoadInGramAsStringWithUnit() {
        return String.format("%.4f", getSumInGram()) + " g";
    }

    public Double getTaredValueInGram() {
        return getSumInGram() - getTareInGram();
    }

    public boolean getPeakHoldActivated(){
        return peakholdactivated;
    }

    public boolean setPeakHoldActivated( boolean status){
        peakholdactivated=status;
        return true;
    }

    public double getIngrediantUnitCost(){
        return Ingrediant_Unit_Cost;
    }

    public double setIngrediantUnitCost(double unitcost){
        Ingrediant_Unit_Cost=unitcost;
        return 0;
    }

    public double getIngrediantTotalCost(){
        return Ingrediant_Total_Cost;
    }

    public double setIngrediantTotalCost(double totalcost){
        Ingrediant_Total_Cost=totalcost;
        return 0;
    }

    public double getIngrediantTotalWeight(){
        return Ingrediant_Total_Weight;
    }
    public double setIngrediantTotalWeight(double totalweight){
        Ingrediant_Total_Weight=totalweight;
        return 0;
    }


    public double getUnderLimitCheckWeighing(){return currentLibrary.getUnderLimitCheckWeighing();}
    public double getOverLimitCheckWeighing(){return currentLibrary.getOverLimitCheckWeighing();}

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
                statistic.addValue( animalWeight);
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


    public void showIngrediantNotification(final Context eContext, DialogInterface.OnDismissListener listener) {
        try {
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_ingrediantcosts);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Ingredient Costs");
            //           statistic = new SummaryStatistics();
            //           for (Double value : statisticsArray) {
            //               statistic.addValue(value);
            //

            // set the custom dialog components - text, image and button


            ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_ingrediants_List);

            // Das ist das Stringarray dessen Elemente Zeile für Zeile angezeigt werden sollen
            List<String> your_array_list = new ArrayList<String>();

            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    //MenuActivity.this,

                    //this,
                    eContext,
                    android.R.layout.simple_list_item_1,
                    your_array_list );

            listView.setAdapter(arrayAdapter);


            arrayAdapter.add("");
            arrayAdapter.add("Text von Listenelement 2");



            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_clear);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clearStatistics();
                    dialog.dismiss();
                }
            });
            Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_print);
            dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(eContext, "Todo: Send statistics to COM port", Toast.LENGTH_LONG).show();
                }
            });
            Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_close);
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

    public String getUnderLimitCheckWeighingAsString() {
        return String.format("%.4f", currentLibrary.getUnderLimitCheckWeighing());
    }
    public String getCheckNominal() {
        return String.format("%.4f", currentLibrary.getCheckNominal());
    }
    public double getCheckNominaldouble() {
       return currentLibrary.getCheckNominal();
    }
    public String getCheckNominalToleranceOver() {
        return String.format("%.4f", currentLibrary.getCheckNominalToleranceOver());
    }

    public String getCheckNominalToleranceOverPercent() {
        return String.format("%.4f", currentLibrary.getCheckNominalToleranceOverPercent());
    }
    public String getCheckNominalToleranceUnderPercent() {
        return String.format("%.4f", currentLibrary.getCheckNominalToleranceUnderPercent());
    }
    public double getCheckNominalToleranceOverdouble() {
        return currentLibrary.getCheckNominalToleranceOver();
    }
    public String getCheckNominalToleranceUnder() {
        return String.format("%.4f", currentLibrary.getCheckNominalToleranceUnder());
    }


    public double getCheckNominalToleranceUnderdouble() {
        return  currentLibrary.getCheckNominalToleranceUnder();
    }

    public String getOverLimitCheckWeighingAsString() {
        return String.format("%.4f", currentLibrary.getOverLimitCheckWeighing());
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


    public Double getDifferenceToInitialWeightInGram() {
        Double retval = 0d;
        try {
            retval = getTaredValueInGram() - getCurrentItem().getWeight();
        }catch (Exception e){
            retval = 0d;
        }
        return retval;
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

    public String getFillingDifferenceInPercent() {

        double ref=currentLibrary.getTarget();
        double netto = getTaredValueInGram();

        double diff=netto-ref;

        if (ref==0){
            return String.format("%.4f", (ref));
        }
        else {
            double percent = (diff / ref) * 100;
            return String.format("%.4f", percent);
        }

    }

    public String getDifferenceFilling(){

        return String.format("%.4f",getTaredValueInGram()-currentLibrary.getTarget());
    }
    public String getTargetasString(){

        return String.format("%.4f",currentLibrary.getTarget());
    }

    public double getTarget(){
        return currentLibrary.getTarget();
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

    public String getPercentFilling() {

        double ref=currentLibrary.getTarget();

        if (ref==0){
            return String.format("%.4f", (ref));
        }else {

            double netto = (getSumInGram() - getTareInGram());

            double percent = (netto/ref)*100;


            return String.format("%.4f", percent);
        }
    }

    public String getDifferenceAsStringInGramWithUnit() {
        String retVal = "";
        try {
            retVal =  String.format("%.4f", getSumInGram() - getTareInGram() - currentItem.getWeight()) + " g";
        }catch (Exception e){
            retVal = "";
        }
        return retVal;
    }

    public String getDifferenceToInitialInPercentWithUnit() {
        String retVal = "";

        try {
            retVal = String.format("%.4f", getDifferenceToInitialWeightInGram() / currentItem.getWeight() * 100.0);
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
        }

    @Override
    public void onWeightChanged(Double weight, String unit) {
        if(Math.abs(weighOld - weight) <= 0.0001){
            if(Scale.getInstance().isStable() == false) {
                Scale.getInstance().setStable(true);
            }
        }else{
            if(Scale.getInstance().isStable() == true) {
                Scale.getInstance().setStable(false);
            }
        }
        weighOld = weight;
    }

    @Override
    public void onApplicationChange(ScaleApplication application) {
        ApplicationManager.getInstance().setIngrediantTotalCost(0);
        ApplicationManager.getInstance().setIngrediantTotalWeight(0);
    }
}