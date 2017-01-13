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
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.List;



public class ApplicationFragmentTable extends Fragment implements WeightListener, StatisticListener {


    private float tara = 0;
    private TextView textTara = null;
    private List<ReferenceField> listReferenceFields = new ArrayList<ReferenceField>();

    int indexTableTara=2;
    int brutto=0;
    int netto=4;
    int loadp=1;

    // Weighing
    int minweight=3;

    //Part Counting
    int apw=3;
    int underlimit=5;
    int overlimit=7;

    //Percent Weighing
    int reference_weight=1;
    int difference_weight=3;
    int difference_percent=5;

    //Check Weighing
    int checkunderlimit=1;
    int checkoverlimit=3;
    int nominal=1;
    int checkundertolerance=3;
    int checkovertolerance=5;

    //Animal Weighing
    int measuringTime=1;

    //Filling
    int FillingTarget=1;
    int FillingDifference=3;
    int FillingDifferencePercent=5;


    //Totalization
    int indexTableNumberOfSamples = 0;
    int indexTableTotal = 1;
    int indextableaverage= 2;
    int standarddeviation=4;
    int minimum=3;
    int maximum=5;
    int range=6;

    //Formulation
    int indexTableRecipeName = 0;

    //Differential Weighing
    int indexTableItemName = 0;
    int indexTableItemDifferenceWeight = 1;
    int indexTableItemDifferencePercentage = 2;
    int indexTableItemInitialWeight = 3;
    int indexTableItemFinalWeight = 4;


    @Override
    public void onResume() {
        super.onResume();
        if (listReferenceFields.isEmpty()==false){
            for(ReferenceField refField : listReferenceFields){
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

        View rootView = inflater.inflate(R.layout.menu_application_fragment_table,container, false);

        //8 Textfields for showing data of interest
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_1),(TextView) rootView.findViewById(R.id.reference_val_1)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_2),(TextView) rootView.findViewById(R.id.reference_val_2)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_3),(TextView) rootView.findViewById(R.id.reference_val_3)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_4),(TextView) rootView.findViewById(R.id.reference_val_4)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_5),(TextView) rootView.findViewById(R.id.reference_val_5)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_6),(TextView) rootView.findViewById(R.id.reference_val_6)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_7),(TextView) rootView.findViewById(R.id.reference_val_7)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_8),(TextView) rootView.findViewById(R.id.reference_val_8)));
        for(ReferenceField refField : listReferenceFields){
            refField.getTextName().setText("");
            refField.getTextValue().setText("");
        }

        return rootView;
    }


    @Override
    public void onWeightChanged(Double weight, String unit) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());




        switch (Scale.getInstance().getScaleApplication()){
            case WEIGHING:

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_tara_visible),getResources().getBoolean(R.bool.preferences_weigh_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_brutto_visible),getResources().getBoolean(R.bool.preferences_weigh_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_weigh_netto_visible),getResources().getBoolean(R.bool.preferences_weigh_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_load_visible),getResources().getBoolean(R.bool.preferences_weigh_load_visible))==true) {
                    listReferenceFields.get(loadp).getTextName().setText("LOAD [%]");
                    listReferenceFields.get(loadp).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");
                }


                //listReferenceFields.get(3).getTextName().setText("LOAD [g]");
                //listReferenceFields.get(3).getTextValue().setText(ApplicationManager.getInstance().getLoadInGramAsStringWithUnit());


                if (prefs.getBoolean(getString(R.string.preferences_weigh_minimum_visible),getResources().getBoolean(R.bool.preferences_weigh_minimum_visible))==true) {
                    listReferenceFields.get(minweight).getTextName().setText("MINIMUM WEIGHT");
                    listReferenceFields.get(minweight).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitAsStringInGram()+ " g");
                }

                break;
            case PART_COUNTING:
                if  (prefs.getBoolean(getString(R.string.preferences_counting_tara_visible),getResources().getBoolean(R.bool.preferences_counting_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_counting_brutto_visible),getResources().getBoolean(R.bool.preferences_counting_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_counting_netto_visible),getResources().getBoolean(R.bool.preferences_counting_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_load_visible),getResources().getBoolean(R.bool.preferences_weigh_load_visible))==true) {
                    listReferenceFields.get(loadp).getTextName().setText("LOAD [%]");
                    listReferenceFields.get(loadp).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");
                }


                if  (prefs.getBoolean(getString(R.string.preferences_counting_apw_visible),getResources().getBoolean(R.bool.preferences_counting_apw_visible))==true){
                    listReferenceFields.get(apw).getTextName().setText("PIECE WEIGHT");
                    listReferenceFields.get(apw).getTextValue().setText(ApplicationManager.getInstance().getAveragePieceWeightAsStringInGram() + " g");
                }


                String cmode = prefs.getString(getString(R.string.preferences_counting_mode),"");

                Log.e("AppFragTable", cmode);
                if  (cmode.equals("2")) {
                    if (prefs.getBoolean(getString(R.string.preferences_counting_under_limit_visible), getResources().getBoolean(R.bool.preferences_counting_under_limit_visible)) == true) {
                        listReferenceFields.get(underlimit).getTextName().setText("UNDER LIMIT");
                        listReferenceFields.get(underlimit).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitPiecesAsString());
                    }

                    if (prefs.getBoolean(getString(R.string.preferences_counting_over_limit_visible), getResources().getBoolean(R.bool.preferences_counting_over_limit_visible)) == true) {
                        listReferenceFields.get(overlimit).getTextName().setText("OVER LIMIT");
                        listReferenceFields.get(overlimit).getTextValue().setText(ApplicationManager.getInstance().getOverlimitPiecesAsString());
                    }
                }

                if  (cmode.equals("3")) {
                    if (prefs.getBoolean(getString(R.string.preferences_counting_target_visible), getResources().getBoolean(R.bool.preferences_counting_target_visible)) == true) {
                        listReferenceFields.get(underlimit).getTextName().setText("TARGET");
                        listReferenceFields.get(underlimit).getTextValue().setText(ApplicationManager.getInstance().getTargetPiecesAsString());
                    }

                    if (prefs.getBoolean(getString(R.string.preferences_counting_difference_visible), getResources().getBoolean(R.bool.preferences_counting_difference_visible)) == true) {
                        listReferenceFields.get(overlimit).getTextName().setText("DIFFERENCE");
                        listReferenceFields.get(overlimit).getTextValue().setText(ApplicationManager.getInstance().getDifferenceAsString());
                    }
                }



                break;
            case PERCENT_WEIGHING:
                if  (prefs.getBoolean(getString(R.string.preferences_percent_tara_visible),getResources().getBoolean(R.bool.preferences_percent_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsString()+ " g");
                }

                if  (prefs.getBoolean(getString(R.string.preferences_percent_brutto_visible),getResources().getBoolean(R.bool.preferences_percent_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsString()+ " g");
                }
                if  (prefs.getBoolean(getString(R.string.preferences_percent_netto_visible),getResources().getBoolean(R.bool.preferences_percent_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }


                if  (prefs.getBoolean(getString(R.string.preferences_percent_reference_weight_visible),getResources().getBoolean(R.bool.preferences_percent_reference_weight_visible))==true) {
                    listReferenceFields.get(reference_weight).getTextName().setText("REFERENCE WEIGHT");
                    listReferenceFields.get(reference_weight).getTextValue().setText(ApplicationManager.getInstance().getReferenceWeightAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_percent_difference_visible),getResources().getBoolean(R.bool.preferences_percent_difference_visible))==true) {
                    listReferenceFields.get(difference_weight).getTextName().setText("DIFFERENCE [g]");
                    listReferenceFields.get(difference_weight).getTextValue().setText(ApplicationManager.getInstance().getDifferenceInGram()+ " g");
                }
                if  (prefs.getBoolean(getString(R.string.preferences_percent_difference_percent_visible),getResources().getBoolean(R.bool.preferences_percent_difference_percent_visible))==true) {
                    listReferenceFields.get(difference_percent).getTextName().setText("DIFFERENCE [%]");
                    listReferenceFields.get(difference_percent).getTextValue().setText(ApplicationManager.getInstance().getDifferenceInPercent()+ " %");
                }
                break;
            case CHECK_WEIGHING:

                String cmode_check = prefs.getString(getString(R.string.preferences_check_limitmode),"");
                if  (prefs.getBoolean(getString(R.string.preferences_check_tara_visible),getResources().getBoolean(R.bool.preferences_check_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_check_brutto_visible),getResources().getBoolean(R.bool.preferences_check_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_check_netto_visible),getResources().getBoolean(R.bool.preferences_check_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (cmode_check.equals("1")) {

                    if (prefs.getBoolean(getString(R.string.preferences_check_under_visible), getResources().getBoolean(R.bool.preferences_check_under_visible)) == true) {
                        listReferenceFields.get(checkunderlimit).getTextName().setText("UNDER LIMIT");
                        listReferenceFields.get(checkunderlimit).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitCheckWeighingAsString() + " g");
                    }
                    if (prefs.getBoolean(getString(R.string.preferences_check_over_visible), getResources().getBoolean(R.bool.preferences_check_over_visible)) == true) {
                        listReferenceFields.get(checkoverlimit).getTextName().setText("OVERLIMIT");
                        listReferenceFields.get(checkoverlimit).getTextValue().setText(ApplicationManager.getInstance().getOverLimitCheckWeighingAsString() + " g");
                    }
                }

                if  (cmode_check.equals("2")) {

                    if (prefs.getBoolean(getString(R.string.preferences_check_target_visible), getResources().getBoolean(R.bool.preferences_check_target_visible)) == true) {
                        listReferenceFields.get(nominal).getTextName().setText("TARTGET");
                        listReferenceFields.get(nominal).getTextValue().setText(ApplicationManager.getInstance().getCheckNominal() + " g");
                    }
                    if (prefs.getBoolean(getString(R.string.preferences_check_undertolerance_visible), getResources().getBoolean(R.bool.preferences_check_undertolerance_visible)) == true) {
                        listReferenceFields.get(checkundertolerance).getTextName().setText("UNDER TOLERANCE");
                        listReferenceFields.get(checkundertolerance).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalToleranceUnder() + " g");
                    }
                    if (prefs.getBoolean(getString(R.string.preferences_check_undertolerance_visible), getResources().getBoolean(R.bool.preferences_check_undertolerance_visible)) == true) {
                        listReferenceFields.get(checkovertolerance).getTextName().setText("OVER TOLERANCE");
                        listReferenceFields.get(checkovertolerance).getTextValue().setText(ApplicationManager.getInstance().getCheckNominalToleranceOver() + " g");
                    }
                }

                break;
            case ANIMAL_WEIGHING:
                if  (prefs.getBoolean(getString(R.string.preferences_animal_tara_visible),getResources().getBoolean(R.bool.preferences_animal_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_animal_brutto_visible),getResources().getBoolean(R.bool.preferences_animal_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_animal_netto_visible),getResources().getBoolean(R.bool.preferences_animal_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_animal_measuringtime_visible),getResources().getBoolean(R.bool.preferences_animal_measuringtime_visible))==true) {
                    listReferenceFields.get(measuringTime).getTextName().setText("AVERAGING TIME");
                    listReferenceFields.get(measuringTime).getTextValue().setText(ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime() + " s");
                }


                break;

            case FILLING:
                if  (prefs.getBoolean(getString(R.string.preferences_filling_tara_visible),getResources().getBoolean(R.bool.preferences_animal_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_filling_brutto_visible),getResources().getBoolean(R.bool.preferences_animal_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_filling_netto_visible),getResources().getBoolean(R.bool.preferences_filling_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_filling_target_visible),getResources().getBoolean(R.bool.preferences_filling_target_visible))==true) {
                    listReferenceFields.get(FillingTarget).getTextName().setText("TARGET");
                    listReferenceFields.get(FillingTarget).getTextValue().setText(ApplicationManager.getInstance().getTargetasString() +" g");
                }
                if  (prefs.getBoolean(getString(R.string.preferences_filling_differencew_visible),getResources().getBoolean(R.bool.preferences_filling_differencew_visible))==true) {
                    listReferenceFields.get(FillingDifference).getTextName().setText("DIFFERENCE [g]: ");
                    listReferenceFields.get(FillingDifference).getTextValue().setText(ApplicationManager.getInstance().getDifferenceFilling() + " g ");
                }
                if  (prefs.getBoolean(getString(R.string.preferences_filling_differencep_visible),getResources().getBoolean(R.bool.preferences_filling_differencep_visible))==true) {
                    listReferenceFields.get(FillingDifferencePercent).getTextName().setText("DIFFERENCE [%]: ");
                    listReferenceFields.get(FillingDifferencePercent).getTextValue().setText(ApplicationManager.getInstance().getFillingDifferenceInPercent()+ " %");
                }




                break;

            case PEAK_HOLD:
                if  (prefs.getBoolean(getString(R.string.preferences_peak_tara_visible),getResources().getBoolean(R.bool.preferences_animal_tara_visible))==true) {
                    listReferenceFields.get(indexTableTara).getTextName().setText("TARA");
                    listReferenceFields.get(indexTableTara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_peak_brutto_visible),getResources().getBoolean(R.bool.preferences_animal_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_peak_netto_visible),getResources().getBoolean(R.bool.preferences_filling_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }


                break;


            case TOTALIZATION:
               // PLACE NO CODE HERE - TABLE WILL BE FILLED BY onStatisticChanged() callback function below in this code
                break;
            case FORMULATION:
                listReferenceFields.get(indexTableRecipeName).getTextName().setText("RECIPE");
                if(Scale.getInstance().getCurrentRecipe() != null) {
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText(Scale.getInstance().getCurrentRecipe().getRecipeName());
                }else{
                    listReferenceFields.get(indexTableRecipeName).getTextValue().setText("No recipe");
                }
                    break;
            case DIFFERENTIAL_WEIGHING:
                listReferenceFields.get(indexTableItemName).getTextName().setText("ITEM");
                if(ApplicationManager.getInstance().getCurrentItem() != null) {
                   listReferenceFields.get(indexTableRecipeName).getTextValue().setText(ApplicationManager.getInstance().getCurrentItem().getName());
                   listReferenceFields.get(indexTableItemDifferenceWeight).getTextName().setText("DIFFERENCE [g]");
                   listReferenceFields.get(indexTableItemDifferenceWeight).getTextValue().setText(ApplicationManager.getInstance().getDifferenceAsStringInGramWithUnit());
                   listReferenceFields.get(indexTableItemDifferencePercentage).getTextName().setText("DIFFERENCE [%]");
                   listReferenceFields.get(indexTableItemDifferencePercentage).getTextValue().setText(ApplicationManager.getInstance().getDifferenceToInitialInPercentWithUnit());
                   listReferenceFields.get(indexTableItemFinalWeight).getTextName().setText("FINAL WEIGHT");
                   listReferenceFields.get(indexTableItemFinalWeight).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                   listReferenceFields.get(indexTableItemInitialWeight).getTextName().setText("INITIAL WEIGHT");
                   listReferenceFields.get(indexTableItemInitialWeight).getTextValue().setText(String.format("%.4f",ApplicationManager.getInstance().getCurrentItem().getWeight()) + " g");
                }else{
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
        }

    }

    @Override
    public void onStatisticChanged(SummaryStatistics statistic) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(Scale.getInstance().getScaleApplication() == ScaleApplication.TOTALIZATION){
         //   if  (prefs.getBoolean(getString(R.string.preferences_totalizaion_NumberofSamples),getResources().getBoolean(R.bool.preferences_totalizaion_NumberofSamples))==true) {
                listReferenceFields.get(indexTableNumberOfSamples).getTextName().setText("SAMPLES");
                try {
                    listReferenceFields.get(indexTableNumberOfSamples).getTextValue().setText(Long.toString(ApplicationManager.getInstance().getStatistic().getN()));
                }catch (Exception e){
                }

                listReferenceFields.get(indexTableTotal).getTextName().setText("TOTAL");
                try {
                    listReferenceFields.get(indexTableTotal).getTextValue().setText(String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getSum()));
                }catch (Exception e){
                }
                listReferenceFields.get(indextableaverage).getTextName().setText("AVERAGE");
            try {
                listReferenceFields.get(indextableaverage).getTextValue().setText(String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMean()));
            }catch (Exception e){
            }

            listReferenceFields.get(standarddeviation).getTextName().setText("STANDARD DEV.");
            try {
                listReferenceFields.get(standarddeviation).getTextValue().setText(String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getStandardDeviation()));
            }catch (Exception e){
            }

            listReferenceFields.get(minimum).getTextName().setText("MINIMUM");
            try {
                listReferenceFields.get(minimum).getTextValue().setText(String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMin()));
            }catch (Exception e){
            }

            listReferenceFields.get(maximum).getTextName().setText("MAXIMUM");
            try {
                listReferenceFields.get(maximum).getTextValue().setText(String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMax()));
            }catch (Exception e){
            }

            listReferenceFields.get(range).getTextName().setText("Range");
            try {
                listReferenceFields.get(range).getTextValue().setText(String.format("%.4f g",(ApplicationManager.getInstance().getStatistic().getMax())-ApplicationManager.getInstance().getStatistic().getMin()));
            }catch (Exception e){
            }


            }

        }


}
