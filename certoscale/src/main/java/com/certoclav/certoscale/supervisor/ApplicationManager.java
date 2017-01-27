package com.certoclav.certoscale.supervisor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemMeasuredAdapter;
import com.certoclav.certoscale.adapters.RecipeAdapter;
import com.certoclav.certoscale.adapters.RecipeElementAdapter;
import com.certoclav.certoscale.adapters.RecipeResultElementAdapter;
import com.certoclav.certoscale.adapters.SQCAdapter;
import com.certoclav.certoscale.adapters.SamplesAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.database.Statistics;
import com.certoclav.certoscale.listener.RecipeEntryListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.StatisticListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.util.ProtocolPrinterUtils;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager implements WeightListener , ScaleApplicationListener {

    private ProtocolPrinterUtils protocolPrinter=new ProtocolPrinterUtils();

    private double PeakHoldMaximum=0;
    public double getPeakHoldMaximum() {return PeakHoldMaximum;}
    public void setPeakHoldMaximum(double peakHoldMaximum) {PeakHoldMaximum = peakHoldMaximum;}


    //Density Determination Variables
    private int density_step_counter=0;
    public int getDensity_step_counter() {
        return density_step_counter;
    }
    public void setDensity_step_counter(int density_step_counter) {this.density_step_counter = density_step_counter;}

    private double density=0;
    public double getDensity() {return density;}
    public void setDensity(double density) {this.density = density;}


    private Recipe currentRecipe = null;
    private RecipeEntry currentRecipeEntry = null;
    ArrayList<RecipeEntryListener> recipeEntryListeners = new ArrayList<RecipeEntryListener>();
    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;

    }

    public void setOnRecipeEntryListener (RecipeEntryListener listener){
        this.recipeEntryListeners.add(listener);
    }
    public void removeOnRecipeEntryListener (RecipeEntryListener listener){
        this.recipeEntryListeners.remove(listener);
    }

    public RecipeEntry getCurrentRecipeEntry() {
        return currentRecipeEntry;
    }

    public void setCurrentRecipeEntry(RecipeEntry currentRecipeEntry) {
        this.currentRecipeEntry = currentRecipeEntry;
        for(RecipeEntryListener listener : recipeEntryListeners){
            listener.onRecipeEntryChanged(currentRecipeEntry);
        }
    }


    private double density_weight_air=0;
    public double getDensity_weight_air() {
        return density_weight_air;
    }

    public void setDensity_weight_air(double density_weight_air) {
        this.density_weight_air = density_weight_air;
    }

    private double density_weight_liquid=0;


    public double getDensity_weight_liquid() {
        return density_weight_liquid;
    }



    public void setDensity_weight_liquid(double density_weight_liquid) {
        this.density_weight_liquid = density_weight_liquid;
    }





    private boolean  peakholdactivated=false;

    //Ingrediant Costing variable
    private double Ingrediant_Unit_Cost=0;
    private double Ingrediant_Total_Weight=0;
    private double Ingrediant_Total_Cost=0;


    //Pipette Adjustment variables
    private String pipette_name="";
    private int pipette_number=0;



    private double pipetteCalculatedML=0;
    public double getPipetteCalculatedML() {return pipetteCalculatedML;}
    public void setPipetteCalculatedML(double pipetteCalculatedML) {this.pipetteCalculatedML = pipetteCalculatedML;}



    private int pipette_current_sample=0;
    public int getPipette_current_sample() {return pipette_current_sample;}
    public void setPipette_current_sample(int pipette_current_sample) {this.pipette_current_sample = pipette_current_sample;}


    public String getPipette_name() {return pipette_name;}
    public void setPipette_name(String pipette_name) {this.pipette_name = pipette_name;}
    public int getPipette_number() {return pipette_number;}
    public void setPipette_number(int pipette_number) {this.pipette_number = pipette_number;}



    //SQC variables
    private int sqc_state=0;




    private int sqcPT1=0;
    private int sqcPT2=0;
    private int sqcNT1=0;
    private int sqcNT2=0;



    public int getSqc_state() {
        return sqc_state;
    }
    public void setSqc_state(int sqc_state) {
        this.sqc_state = sqc_state;
    }

    public int getSqcPT1() {return sqcPT1;}
    public void setSqcPT1(int sqcPT1) {this.sqcPT1 = sqcPT1;}

    public int getSqcPT2() {return sqcPT2;}
    public void setSqcPT2(int sqcPT2) {this.sqcPT2 = sqcPT2;}

    public int getSqcNT1() {return sqcNT1;}
    public void setSqcNT1(int sqcNT1) {this.sqcNT1 = sqcNT1;}

    public int getSqcNT2() {return sqcNT2;}
    public void setSqcNT2(int sqcNT2) {this.sqcNT2 = sqcNT2;}




    private String batchName="";
    public String getBatchName() {return batchName;}
    public void setBatchName(String batchName) {this.batchName = batchName;}

    private SQC currentBatch = null;
    public SQC getCurrentBatch() {return currentBatch;}
    public void setCurrentBatch(SQC currentBatch) {this.currentBatch = currentBatch;}


    public List<SQC> batchList= new ArrayList<SQC>();
    public List<SQC> getBatchList() {return batchList;}
    public void setBatchList(List<SQC> batchList) {this.batchList = batchList;}




    public List<Item> getIngrediantCostList() {
        return ingrediantCostList;
    }

    public void setIngrediantCostList(List<Item> ingrediantCostList) {
        this.ingrediantCostList = ingrediantCostList;
    }

    // Das ist das Stringarray dessen Elemente Zeile für Zeile angezeigt werden sollen
    private List<Item> ingrediantCostList = new ArrayList<Item>();

    //Variables for isStable calculation
    private double lastStableweight=0;
    private Double weighOld = 0d;
    private int stableCounter=0;


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

    /*public SummaryStatistics getStatistic() {
        return statistic;
    }
    */


    //private SummaryStatistics statistic = new SummaryStatistics();

    public Statistics getStats() {return stats;}

    public void setStats(Statistics stats) {this.stats = stats;}

    private Statistics stats=new Statistics();

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
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0
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
                return String.format("%d", getSumInPieces() - getTareInPieces()) + " PCS";
            default:
                return String.format("%.4f", getSumInGram() - getTareInGram()) + " g";
        }
    }

    public void accumulateStatistics() {
        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:

                stats.getStatistic().addValue((double) (getSumInPieces() - getTareInPieces()));
                stats.getSamples().add((double) (getSumInPieces() - getTareInPieces()));
                break;
            case ANIMAL_WEIGHING:
                stats.getStatistic().addValue( animalWeight);
                stats.getSamples().add(animalWeight);
                break;
            case PIPETTE_ADJUSTMENT:
                stats.getStatistic().addValue(ApplicationManager.getInstance().getPipetteCalculatedML());
                stats.getSamples().add(ApplicationManager.getInstance().getPipetteCalculatedML());
                break;
            default:
                stats.getStatistic().addValue(getTaredValueInGram());
                stats.getSamples().add(getTaredValueInGram());
                break;
        }
        for (StatisticListener listener : statisticListeners){
            listener.onStatisticChanged(stats.getStatistic());
        }


    }

    public void clearStatistics() {
        stats.getSamples().clear();
        stats.getStatistic().clear();
        for(StatisticListener listener : statisticListeners){
            listener.onStatisticChanged(stats.getStatistic());
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
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_sample_number)).setText("" + stats.getStatistic().getN());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_average)).setText(String.format("%.4f", stats.getStatistic().getMean()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_maximum)).setText(String.format("%.4f", stats.getStatistic().getMax()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_minimum)).setText(String.format("%.4f", stats.getStatistic().getMin()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_range)).setText(String.format("%.4f", stats.getStatistic().getVariance()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_stdev)).setText(String.format("%.4f", stats.getStatistic().getStandardDeviation()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_text_total)).setText(String.format("%.4f", stats.getStatistic().getSum()) + " " + getUnitAsString());

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


                    protocolPrinter.printTop();
                    protocolPrinter.printStatistics();
                    protocolPrinter.printBottom();

                    Toast.makeText(eContext, "Statistics printed", Toast.LENGTH_LONG).show();
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



            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            ItemMeasuredAdapter arrayAdapter = new ItemMeasuredAdapter(eContext,new ArrayList<Item>());

            listView.setAdapter(arrayAdapter);
            for(Item item : getIngrediantCostList()){
                arrayAdapter.add(item);
            }

            //arrayAdapter.add(new Item(ApplicationManager.getInstance().getCurrentItem().getItemArticleNumber(),"ssdd"));




            //arrayAdapter.add("Atricle No.    Name       Cost           Weight    Unit");
            //arrayAdapter.add("Text von Listenelement 2");



            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_clear);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setIngrediantTotalWeight(0);
                    setIngrediantUnitCost(0);
                    setIngrediantTotalCost(0);
                    getIngrediantCostList().clear();
                    dialog.dismiss();
                }
            });
            Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_print);
            dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    protocolPrinter.printTop();
                    protocolPrinter.printApplicationData();
                    protocolPrinter.printBottom();

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


    public void showBatchList(final Context eContext, DialogInterface.OnDismissListener listener) {
        try {
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_batchlist);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Batch List");



            ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_batch_List);



            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            SQCAdapter arrayAdapter = new SQCAdapter(eContext,new ArrayList<SQC>());

            listView.setAdapter(arrayAdapter);
            for(SQC sqc : getBatchList()){
                arrayAdapter.add(sqc);
            }

            //arrayAdapter.add(new Item(ApplicationManager.getInstance().getCurrentItem().getItemArticleNumber(),"ssdd"));




            //arrayAdapter.add("Atricle No.    Name       Cost           Weight    Unit");
            //arrayAdapter.add("Text von Listenelement 2");



            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_clear);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ApplicationManager.getInstance().setBatchName("");
                    getBatchList().clear();
                    dialog.dismiss();
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

    public void showRecipeResults(final Context eContext, DialogInterface.OnDismissListener listener) {
        try {
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_recipe_results);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Result of Recipe: ");// + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());



            ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_batch_List);



            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.

            List<RecipeEntry> entryList=new ArrayList<RecipeEntry>();
            //List<String> entryList= new ArrayList<String>();



            //double formulationTotal = 0;
            double formulationTotalTarget = 0;
            double formulationTotalDifference = 0;
            int formulationcounter = 0;
            while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {
               // formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight();
               // formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();

                entryList.add(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter));


                formulationcounter++;
            }





            RecipeResultElementAdapter recipeAdapter = new RecipeResultElementAdapter(eContext,new ArrayList<RecipeEntry>());
            listView.setAdapter(recipeAdapter);
            for(RecipeEntry recipeEntry:entryList){
                recipeAdapter.add(recipeEntry);
            }


            //arrayAdapter.add(new Item(ApplicationManager.getInstance().getCurrentItem().getItemArticleNumber(),"ssdd"));




            //arrayAdapter.add("Atricle No.    Name       Cost           Weight    Unit");
            //arrayAdapter.add("Text von Listenelement 2");



            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_recipe_button_print);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    protocolPrinter.printTop();
                    //Printing the application data
                    protocolPrinter.printApplicationData();
                    //Print Signature liness
                    protocolPrinter.printBottom();
                    dialog.dismiss();
                }
            });

            Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_recipe_button_close);
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

    public double WaterTempInDensity(double temp){

        //This Code was adapted from view-source:http://antoine.frostburg.edu/chem/senese/javascript/water-density.html

        double[] table = {
                0.998120, 0.998398, 0.998650, 0.998877, 0.999080, 0.999259, 0.999417, 0.999553, 0.999669, 0.999765,
                0.9998425, 0.9999015, 0.9999429, 0.9999672, 0.9999750, 0.9999668, 0.9999432, 0.9999045, 0.9998512, 0.9997838,
                0.9997026, 0.9996018, 0.9995004, 0.9993801, 0.9992474, 0.9991026, 0.9989460, 0.9987779, 0.9985986, 0.9984082,
                0.9982071, 0.9979955, 0.9977735, 0.9975415, 0.9972995, 0.9970479, 0.9967867, 0.9965162, 0.9962365, 0.9959478,
                0.9956502, 0.9953440, 0.9950292, 0.9947060, 0.9943745, 0.9940349, 0.9936872, 0.9933316, 0.9929683, 0.9925973,
                0.9922187, 0.9918327, 0.9914394, 0.9910388, 0.9906310, 0.9902162, 0.9897944, 0.9893657, 0.9889303, 0.9884881,
                0.9880393, 0.9875839, 0.9871220, 0.9866537, 0.9861791, 0.9856982, 0.9852111, 0.9847178, 0.9842185, 0.9837132,
                0.9832018, 0.9826846, 0.9821615, 0.9816327, 0.9810981, 0.9805578, 0.9800118, 0.9794603, 0.9789032, 0.9783406,
                0.9777726, 0.9771991, 0.9766203, 0.9760361, 0.9754466, 0.9748519, 0.9742520, 0.9736468, 0.9730366, 0.9724212,
                0.9718007, 0.9711752, 0.9705446, 0.9699091, 0.9692686, 0.9686232, 0.9679729, 0.9673177, 0.9666576, 0.9659927,
                0.9653230, 0.9646486, 0.9639693, 0.9632854, 0.9625967, 0.9619033, 0.9612052, 0.9605025, 0.9597951, 0.9590831,
                0.9583665, 0.957662, 0.956937, 0.956207, 0.955472, 0.954733, 0.953989, 0.953240, 0.952488, 0.941730
        };

        //double t=temp-273.15;

        //Calculates water density at a given temperature between -8 and 108 deg C using
        // 5-point Lagrange interpolation.
        int i = (int) Math.floor(temp);
        double p = temp - i;
        double p2m1 = p * p - 1.0;
        double p2m4 = p2m1 - 3.0;
        i += 10;
        return p2m1*p*(p-2)*table[i-2]/24.0 - (p-1)*p*p2m4*table[i-1]/6.0 + p2m1*p2m4*table[i]/4.0 - (p+1)*p*p2m4*table[i+1]/6.0 + p2m1*p*(p+2)*table[i+2]/24.0;


    }




    public void showStatisticsSQC(final Context eContext, final SQC sqc) {
        try {

            SummaryStatistics statistic =sqc.getStatistics();
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_statistics_sqc);
            dialog.setTitle("Statistics of "+sqc.getName());
            //           statistic = new SummaryStatistics();
            //           for (Double value : statisticsArray) {
            //               statistic.addValue(value);
            //           }
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_sample_number)).setText("" + statistic.getN());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_average)).setText(String.format("%.4f", statistic.getMean()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_maximum)).setText(String.format("%.4f", statistic.getMax()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_minimum)).setText(String.format("%.4f", statistic.getMin()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_range)).setText(String.format("%.4f", statistic.getVariance()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_stdev)).setText(String.format("%.4f", statistic.getStandardDeviation()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_total)).setText(String.format("%.4f", statistic.getSum()) + " " + getUnitAsString());


            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_nominal)).setText(String.format("%.4f",sqc.getNominal()));

            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ptolerance1)).setText(String.format("%d",sqc.getSqcPT1())+ "   " +String.format("%.1f",((double)sqc.getSqcPT1()/(double)statistic.getN())*100 )+ "%");
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ptolerance2)).setText(String.format("%d",sqc.getSqcPT2())+ "   " +String.format("%.1f",((double)sqc.getSqcPT2()/(double)statistic.getN())*100 )+ "%");
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ntolerance1)).setText(String.format("%d",sqc.getSqcNT1())+ "   " +String.format("%.1f",((double)sqc.getSqcNT1()/(double)statistic.getN())*100 )+ "%");
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ntolerance2)).setText(String.format("%d",sqc.getSqcNT2())+ "   " +String.format("%.1f",((double)sqc.getSqcNT2()/(double)statistic.getN())*100 )+ "%");


            // set the custom dialog components - text, image and button


            Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_print);
            dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    protocolPrinter.printTop();

                    protocolPrinter.printSQCBatch(sqc);

                    protocolPrinter.printBottom();

                    Toast.makeText(eContext, "Todo: Send statistics to COM port", Toast.LENGTH_LONG).show();
                }
            });
            Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_close);
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


    public void showStatisticsTotalization(final Context eContext, DialogInterface.OnDismissListener listener) {
        try {


            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_statistics_totalization);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Statistics:");
            //           statistic = new SummaryStatistics();
            //           for (Double value : statisticsArray) {
            //               statistic.addValue(value);
            //           }
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_sample_number)).setText("" + stats.getStatistic().getN());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_average)).setText(String.format("%.4f", stats.getStatistic().getMean()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_maximum)).setText(String.format("%.4f", stats.getStatistic().getMax()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_minimum)).setText(String.format("%.4f", stats.getStatistic().getMin()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_range)).setText(String.format("%.4f", stats.getStatistic().getVariance()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_stdev)).setText(String.format("%.4f", stats.getStatistic().getStandardDeviation()) + " " + getUnitAsString());
            ((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_total)).setText(String.format("%.4f", stats.getStatistic().getSum()) + " " + getUnitAsString());



/*
            List<RecipeEntry> entryList=new ArrayList<RecipeEntry>();
            //List<String> entryList= new ArrayList<String>();



            //double formulationTotal = 0;
            double formulationTotalTarget = 0;
            double formulationTotalDifference = 0;
            int formulationcounter = 0;
            while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {
                // formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight();
                // formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();

                entryList.add(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter));


                formulationcounter++;
            }*/



           /* RecipeResultElementAdapter recipeAdapter = new RecipeResultElementAdapter(eContext,new ArrayList<RecipeEntry>());
            listView.setAdapter(recipeAdapter);
            for(RecipeEntry recipeEntry:entryList){
                recipeAdapter.add(recipeEntry);




            }
*/
            ListView listView = (ListView) dialog.findViewById(R.id.dialog_sample_list);

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = ApplicationManager.getInstance().getStats().getSamples().size()*50;
            listView.setLayoutParams(params);
            listView.requestLayout();

            SamplesAdapter sAdapter= new SamplesAdapter(eContext,new ArrayList<Double>());
            listView.setAdapter(sAdapter);

            for(Double sample:ApplicationManager.getInstance().getStats().getSamples()){
                sAdapter.add(sample);
            }



            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_statistics_button_clear);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clearStatistics();
                    dialog.dismiss();

                }
            });


            Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_print);
            dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {






                }
            });
            Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_close);
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



    public void showPipetteResults(final Context eContext, DialogInterface.OnDismissListener listener) {
        try {
            final Dialog dialog = new Dialog(eContext);
            dialog.setContentView(R.layout.dialog_pipette_results);
            dialog.setOnDismissListener(listener);
            dialog.setTitle("Pipette Adjustment Results");



            ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_pipette_listview);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(eContext,R.layout.dialog_pipette_row,R.id.menu_main_pipette_edit_text);
            listView.setAdapter(adapter);

            adapter.add("Inaccuracy: \n");

            double meanError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getMean()-ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
            adapter.add("Mean Error:"+String.format("%.4f",meanError)+" ml\n");
            double meanErrorPercent=Math.abs(meanError/ApplicationManager.getInstance().getStats().getStatistic().getMean())*100;
            adapter.add("Mean Error %:"+String.format("%.4f",meanErrorPercent)+" %\n");
            adapter.add("Limit %:"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy())+" %\n");


            adapter.add("\n");
            adapter.add("Impreccision:\n");
            adapter.add("Standard Deviation:"+String.format("%.4f",ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation())+" ml\n");
            double standardError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()/ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())*100;
            adapter.add("Error CS%:"+String.format("%.4f",standardError)+" %\n");
            adapter.add("Limit CV:"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())+" %\n");

            if (meanErrorPercent<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy() && standardError<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()){
                adapter.add("\n");
                adapter.add("Result: Pass");
                adapter.add("\n");
            }else{
                adapter.add("\n");
                adapter.add("Result: Fail");
                adapter.add("\n");
            }

            adapter.add("\n");
            adapter.add("Number of Samples"+ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()+"\n");

            double standardDeviation=ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation();
            double mean=ApplicationManager.getInstance().getStats().getStatistic().getMean();
            int nstandard1=0;
            int nstandard2=0;
            int pstandard1=0;
            int pstandard2=0;
            double pcurrent=0;

            for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
                pcurrent=mean-ApplicationManager.getInstance().getStats().getSamples().get(i);
                if (pcurrent<0){
                    if (Math.abs(pcurrent)>standardDeviation){
                        if (Math.abs(pcurrent)>2*standardDeviation){
                            nstandard2++;
                        }else{
                            nstandard1++;
                        }
                    }
                }else {
                    if (Math.abs(pcurrent)>standardDeviation){
                        if (Math.abs(pcurrent)>2*standardDeviation){
                            pstandard2++;
                        }else{
                            pstandard1++;
                        }
                    }

                }
            }

            adapter.add("> +2s:"+pstandard2+"\n");
            adapter.add("> +2s:"+pstandard1+"\n");
            adapter.add("*+1S > Mean > –1S:"+(ApplicationManager.getInstance().getStats().getSamples().size()-pstandard1-pstandard2-nstandard1-nstandard2)+"\n");
            adapter.add("- +2s:"+nstandard1+"\n");
            adapter.add("- +2s:"+nstandard2+"\n");

            adapter.add("\n");


            adapter.add("---Sample Data---\n");
            for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
                adapter.add("Item "+String.format("%d",i)+" "+String.format("%.4f",ApplicationManager.getInstance().getStats().getSamples().get(i))+" g\n");

            }



            Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_pipette_button_clear);
            dialogButtonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ApplicationManager.getInstance().setBatchName("");
                    getBatchList().clear();
                    dialog.dismiss();
                }
            });

            Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_pipette_button_close);
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



    @Override
    public void onWeightChanged(Double absweight, String unit) {
        Double weight=ApplicationManager.getInstance().getTaredValueInGram();
        if(Math.abs(weighOld - weight) <= 0.0001){

            if(Scale.getInstance().isStable() == false) {
                stableCounter=stableCounter+1;
                if(stableCounter>=8 && Math.abs(weight)>0.002) {
                    if (Math.abs(lastStableweight-weight)>0.002) {
                        Scale.getInstance().setStable(true);
                        stableCounter = 0;
                        lastStableweight=weight;
                       // Log.e("Stable", "Stable Stable Stable Stable Stable Stable Stable Stable Stable Stable");
                    }
                }
            }
        }else{
            stableCounter=0;
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
        getIngrediantCostList().clear();
        lastStableweight=0;
        stableCounter=0;

        sqc_state=0;
       // your_array_list.add("Article No.          Name           Cost          Weight   Unit");
    }
}