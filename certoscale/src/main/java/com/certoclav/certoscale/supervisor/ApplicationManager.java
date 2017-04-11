package com.certoclav.certoscale.supervisor;

import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.database.Statistics;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.RecipeEntryListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.StatisticListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.model.ScaleModelAEAdam;
import com.certoclav.certoscale.model.ScaleModelGandG;
import com.certoclav.library.application.ApplicationController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.certoclav.certoscale.constants.AppConstants.INTERNAL_TARA_ZERO_BUTTON;
import static com.certoclav.certoscale.constants.AppConstants.IS_IO_SIMULATED;
import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;
import static com.certoclav.certoscale.model.ScaleApplication.DIFFERENTIAL_WEIGHING;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING_CALC_TARGET;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_FREE;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_FREE_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING_CALC_AWP;
import static com.certoclav.certoscale.model.ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE;

/**
 * Created by Michael on 12/6/2016.
 */

public class ApplicationManager implements WeightListener , ScaleApplicationListener {




    long nanoTimeSinceStable=1000000000;

    boolean returnFromSubMenu=false;
    public boolean isReturnFromSubMenu() {return returnFromSubMenu;}
    public void setReturnFromSubMenu(boolean returnFromSubMenu) {this.returnFromSubMenu = returnFromSubMenu;}


    private String Currency="€";
    public String getCurrency() {return Currency;}
    public void setCurrency(String currency) {Currency = currency;}

    private Unit currentUnit=new Unit(0d,1d,"gram",Unit.UNIT_GRAM,"",true,false);
    public Unit getCurrentUnit() {return currentUnit;}
    public void setCurrentUnit(Unit currentUnit) {this.currentUnit = currentUnit;}


    public ProtocolManager getProtocolPrinter() {return protocolPrinter;}
    public void setProtocolPrinter(ProtocolManager protocolPrinter) {this.protocolPrinter = protocolPrinter;}
    private ProtocolManager protocolPrinter=new ProtocolManager();


    private double scalingFactor=1;
    public double getScalingFactor() {return scalingFactor;}
    public void setScalingFactor(double scalingFactor) {this.scalingFactor = scalingFactor;}


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
    public void setDensity_weight_air(double density_weight_air) {this.density_weight_air = density_weight_air;}

    private double density_weight_liquid=0;
    public double getDensity_weight_liquid() {
        return density_weight_liquid;
    }

    public void setDensity_weight_liquid(double density_weight_liquid) {
        this.density_weight_liquid = density_weight_liquid;
    }




    private double pholdWeight=0;
    public double getPholdWeight() {return pholdWeight;}
    public void setPholdWeight(double pholdWeight) {this.pholdWeight = pholdWeight;}



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
    private int sqcPT1=0;
    private int sqcPT2=0;
    private int sqcNT1=0;
    private int sqcNT2=0;


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

    private Library currentLibrary = null;



    public String getAnimalWeightAsStringWithUnit() {

        return getTransformedWeightAsStringWithUnit(animalWeight);

    }

    public Double getAnimalWeightInGram() {

        return animalWeight;

    }
    public void setAnimalWeight(Double animalWeight) {

        this.animalWeight = animalWeight;
    }

    private Double animalWeight = 0d;

     public Double getAwpCalcSampleSize() {
        return currentLibrary.getSampleSize();
    }

    public String getAwpCalcSampleSizeAsString() {
        return String.format("%.6f", transformGramToCurrentUnit(getAwpCalcSampleSize())) + " " + getCurrentUnit().getName();
    }

    public Double transformCurrentUnitToGram(Double value) {
         return value /(currentUnit.getFactor()* Math.pow(10,currentUnit.getExponent()));
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



    public String getUnitAsString() {
        switch (Scale.getInstance().getScaleApplication()) {
           // case PART_COUNTING:
           //     return "pcs";

            case PERCENT_WEIGHING:
                return "%";

            default:
                return currentUnit.getName();
        }
    }


    public Double getSumInGram() {
        return Scale.getInstance().getWeightInGram();
    }

    public Double getSumInCurrentUnit() {
        return transformGramToCurrentUnit(Scale.getInstance().getWeightInGram());
    }

    public int getSumInPieces() {
        return (int) Math.round(Scale.getInstance().getWeightInGram() / currentLibrary.getAveragePieceWeight());
    }

    public Double getTareInCurrentUnit(){
        return transformGramToCurrentUnit(getTareInGram());
    }

    public Double getTareInGram() {
        return currentLibrary.getTara();
    }

    public int getTareInPieces() {
        return (int) Math.round(currentLibrary.getTara() / currentLibrary.getAveragePieceWeight());
    }

    public int getLoadInPercent() {
        return (int) Math.round((Scale.getInstance().getWeightInGram() / Scale.getInstance().getScaleModel().getMaximumCapazityInGram()) * 100.0);
    }


    public String getSumAsStringWithUnit() {


        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return String.format("%d", getSumInPieces()) + " " + "pcs";
            default:
                if (INTERNAL_TARA_ZERO_BUTTON){
                    return getTransformedWeightAsStringWithUnit(getSumInGram());
                }else {
                    return getTransformedWeightAsStringWithUnit(getSumInGram() + getTareInGram());
                }
        }


    }
    public double getSum() {
        return getSumInGram()+getTareInGram();
    }



    public String getSumAsString() {
        return String.format("%.4f", getSumInGram());
    }


    public String getTaredValueAsStringWithUnit() {

            switch (Scale.getInstance().getScaleApplication()) {
                case PART_COUNTING:
                    return String.format("%d", getSumInPieces() - getTareInPieces()) + " " + "pcs";
                default:
                    return getTransformedWeightAsStringWithUnit(getTaredValueInGram());

            }

    }


    public String getTareAsStringWithUnit() {
        switch (Scale.getInstance().getScaleApplication()) {
            case PART_COUNTING:
                return String.format("%d", getTareInPieces()) + " " + "pcs";
            default:
                return getTransformedWeightAsStringWithUnit(getTareInGram());
        }
    }


    public String getTareAsString() {
        return String.format("%d", getTareInPieces());
    }

    private ApplicationManager() {
        Scale.getInstance().setOnWeightListener(this);
        Scale.getInstance().setOnApplicationListener(this);
        DatabaseService db = new DatabaseService(ApplicationController.getContext());
        currentUnit = new Unit(0d,1d,"gram","g","",true,false);
        currentLibrary = new Library("",0,"",0,"default",0d,0d,0d,0d,0d,0d,0d,0d,0d,100,0d,0,new Date(),true,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,1);
    }


    public String getLoadInGramAsStringWithUnit() {
        return String.format("%.4f", getSumInGram()) + " g";
    }

    public Double getTaredValueInGram() {

        if (IS_IO_SIMULATED || INTERNAL_TARA_ZERO_BUTTON) {
            return getSumInGram() - getTareInGram();
        }else{
            return getSumInGram();
        }

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
       // switch (Scale.getInstance().getScaleApplication()) {
       //     case PART_COUNTING:
       //         return String.format("%d", getSumInPieces() - getTareInPieces()) + " PCS";
       //     default:
                return String.format("%.4f", getSumInGram() - getTareInGram()) + " g";
        //}
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
            case PIPETTE_ADJUSTMENT_1_HOME:
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


    public String getAveragePieceWeightAsStringWithUnit() {
        String retVal = "";

        int numDezimalPlaces = 4 - (int) Math.round(getCurrentUnit().getExponent());
        retVal = String.format("%." + numDezimalPlaces + "f", transformGramToCurrentUnit(currentLibrary.getAveragePieceWeight())) + " " + getCurrentUnit().getName();

        return retVal;

    }

    public String getAveragePieceWeightAsStringInGram() {
        return String.format("%.4f", currentLibrary.getAveragePieceWeight());
    }

    public String getUnderLimitAsStringInGram() {
        return String.format("%.4f", currentLibrary.getUnderLimit());
    }



    public Double transformGramToCurrentUnit(double gram) {
        return gram * currentUnit.getFactor()* Math.pow(10,currentUnit.getExponent());
    }


    public String getTransformedWeightAsString(double gram) {
        String retVal = "";
        try {
            if(Scale.getInstance().getScaleApplication() == ScaleApplication.PART_COUNTING){
                retVal = String.format("%.0f", gram);
            }else {
                int numDezimalPlaces = Scale.getInstance().getScaleModel().getDecimalPlaces() - (int) Math.round(getCurrentUnit().getExponent());
                if(numDezimalPlaces < 0){
                    numDezimalPlaces = 0;
                }
                retVal = String.format(Locale.US,"%." + numDezimalPlaces + "f", transformGramToCurrentUnit(gram));
            }
        }catch (Exception e){
            retVal = "";
        }
        return retVal;


    }

    public String getTransformedWeightAsStringWithUnit(double gram) {
        String retVal = "";
        try {
            if(Scale.getInstance().getScaleApplication() == ScaleApplication.PART_COUNTING){
                retVal = String.format("%.0f", gram) + " pcs";
            }else {
                int numDezimalPlaces = Scale.getInstance().getScaleModel().getDecimalPlaces() - (int) Math.round(getCurrentUnit().getExponent());
                if(numDezimalPlaces < 0){
                    numDezimalPlaces = 0;
                }
                retVal = String.format("%." + numDezimalPlaces + "f", transformGramToCurrentUnit(gram)) + " " + getCurrentUnit().getName();
            }
        }catch (Exception e){
            retVal = "";
        }
        return retVal;


    }

    public String getUnderLimitPiecesAsStringInGram() {
        return String.format("%.1f", currentLibrary.getUnderLimitPieces());
    }

    public String getUnderLimitPiecesAsString() {
        return String.format("%.1f", currentLibrary.getUnderLimitPieces());
    }


    public String getUnderLimitCheckWeighingAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(currentLibrary.getUnderLimitCheckWeighing());
    }


    public String getCheckNominalAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(currentLibrary.getCheckNominal());
    }




    public double getCheckNominaldouble() {
       return currentLibrary.getCheckNominal();
    }

    public String getCheckNominalToleranceOverAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(currentLibrary.getCheckNominalToleranceOver());
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

    public String getCheckNominalToleranceUnderAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(currentLibrary.getCheckNominalToleranceUnder());
    }


    public double getCheckNominalToleranceUnderdouble() {
        return  currentLibrary.getCheckNominalToleranceUnder();
    }


    public String getOverLimitCheckWeighingAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(currentLibrary.getOverLimitCheckWeighing());
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
            case PART_COUNTING:
                return String.format("%d ", currentLibrary.getUnderLimitPieces()) + " pcs";
            default:
                return getTransformedWeightAsStringWithUnit(currentLibrary.getUnderLimit());
        }

    }


    public String getReferenceWeightAdjustedAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(currentLibrary.getReferenceWeight() * currentLibrary.getReferenceweightAdjustment() / 100);
    }

   public String getReferenceWeightAsStringWithUnit() {
        return getTransformedWeightAsStringWithUnit(getCurrentLibrary().getReferenceWeight());
    }
    public Double getDifferenceInGram() {

        Double retval = 0d;
        if(Scale.getInstance().getScaleApplication() == DIFFERENTIAL_WEIGHING) {
            try {
                Double targetWeight = getCurrentItem().getWeight();
                Double taredWeight = getTaredValueInGram();
                Double difference = -(targetWeight - taredWeight);
                retval = difference;
            } catch (Exception e) {
                retval = 0d;
            }
        }else{
            try {
                Double targetWeight = getTarget();
                Double taredWeight = getTaredValueInGram();
                Double difference = -(targetWeight - taredWeight);
                retval = difference;
            } catch (Exception e) {
                retval = 0d;
            }
        }

        return retval;
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


    public String getDifferenceFillingAsStringWithUnit(){

        return getTransformedWeightAsStringWithUnit(getTaredValueInGram()-currentLibrary.getTarget());
    }




    public String getTargetAsStringWithUnit(){

        return getTransformedWeightAsStringWithUnit(currentLibrary.getTarget());
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



    public String getDifferenceToInitialInPercentAsString() {
        String retVal = "";

        try {
            retVal = String.format("%.4f", getDifferenceToInitialWeightInGram() / currentItem.getWeight() * 100.0) + " %";
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
        }




    public String getStatisticsCurrentAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(  getStats().getSamples().get(getStats().getSamples().size() - 1));
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
    }



    public String getStatisticsRangeAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(   getStats().getStatistic().getMax() - getStats().getStatistic().getMin()  );
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
    }

    public String getStatisticsMaxAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(getStats().getStatistic().getMax());
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
    }

    public String getStatisticsMinAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(getStats().getStatistic().getMin());
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
    }

    public String getStatisticsStandardDeviationAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(getStats().getStatistic().getStandardDeviation());
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
    }

    public String getStatisticsMeanAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(getStats().getStatistic().getMean());
        }catch (Exception e){
            retVal = "";
        }

        return retVal;
    }

    public String getStatisticsSumAsStringWithUnit() {
        String retVal = "";

        try {
            retVal = getTransformedWeightAsStringWithUnit(getStats().getStatistic().getSum());
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















    @Override
    public void onWeightChanged(Double absweight, String unit) {
        Double weight = ApplicationManager.getInstance().getTaredValueInGram();


        if (Scale.getInstance().getScaleModel() instanceof ScaleModelAEAdam || Scale.getInstance().getScaleModel() instanceof ScaleModelGandG) {

        } else {

            if (Math.abs(weighOld - weight) <= 0.0001 && Math.abs(weight) >= 0.0004) {

                if ((System.nanoTime() - nanoTimeSinceStable) > (1000000000L * Scale.getInstance().getScaleModel().getStabilisationTime()))

                    Scale.getInstance().setStable(true);

            } else {
                nanoTimeSinceStable = System.nanoTime();
                Scale.getInstance().setStable(false);
            }
            weighOld = weight;

        }
    }


    @Override
    public void onApplicationChange(ScaleApplication application) {
        //do not react on subApplications
        if(application == PART_COUNTING_CALC_AWP ||
                application == PERCENT_WEIGHING_CALC_REFERENCE ||
                application == ANIMAL_WEIGHING_CALCULATING ||
                application == FILLING_CALC_TARGET ||
                application == FORMULATION_RUNNING ||
                application == FORMULATION_FREE_RUNNING ){
            return;
        }
        DatabaseService db = new DatabaseService(ApplicationController.getContext());

        ProtocolManager protocolPrinterUtils = new ProtocolManager();



        ApplicationManager.getInstance().setIngrediantTotalCost(0);
        ApplicationManager.getInstance().setIngrediantTotalWeight(0);
        getIngrediantCostList().clear();
        lastStableweight=0;
        stableCounter=0;


        pholdWeight=0;
        setPeakHoldMaximum(0);


       // your_array_list.add("Article No.          Name           Cost          Weight   Unit");
    }



}