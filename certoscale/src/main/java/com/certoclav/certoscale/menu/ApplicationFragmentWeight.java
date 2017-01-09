package com.certoclav.certoscale.menu;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.graph.GraphService;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.listener.StatisticListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentWeight extends Fragment implements WeightListener, StableListener {
    private FrameLayout barload = null;
    private TextView textInstruction = null;
    private TextView textSum = null;
    private TextView textValue = null;
    private ImageView imageStable = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_weight_display,container, false);
        //Access to views from menu_main xml file
        barload = (FrameLayout) rootView.findViewById(R.id.menu_main_bar_load);
        textInstruction = (TextView) rootView.findViewById(R.id.menu_main_text_instruction);
        textSum = (TextView) rootView.findViewById(R.id.menu_main_text_information);
        textValue = (TextView) rootView.findViewById(R.id.menu_main_text_value);
        imageStable = (ImageView) rootView.findViewById(R.id.menu_main_image_stable);

        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        Scale.getInstance().setOnWeightListener(this);
        Scale.getInstance().setOnStableListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        Scale.getInstance().removeOnWeightListener(this);
        Scale.getInstance().removeOnStableListener(this);
    }





    @Override
    public void onWeightChanged(Double weight, String unit) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());



        switch (Scale.getInstance().getScaleApplication()){
            case ANIMAL_WEIGHING_CALCULATING:
                textValue.setTextColor(Color.WHITE);
                textValue.setText("calculating...");
                textSum.setText("SUM: " + ApplicationManager.getInstance().getTaredValueInGram());


                break;

            case ANIMAL_WEIGHING:
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getAnimalWeight() + " g");
                textSum.setText("SUM: " + ApplicationManager.getInstance().getTaredValueInGram()+ " g");
                break;
            case PART_COUNTING_CALC_AWP:
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getAwpCalcSampleSize() + " pcs");
                textSum.setText("TARED WEIGHT: " + ApplicationManager.getInstance().getTaredValueAsStringInGram());

                break;
            case PART_COUNTING:
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());

                break;
            case FORMULATION:
            case FORMULATION_RUNNING:
            case DIFFERENTIAL_WEIGHING:
            case WEIGHING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);

                if (ApplicationManager.getInstance().getTaredValueInGram()<ApplicationManager.getInstance().getUnderLimitValueInGram() ) {
                    if (prefs.getBoolean(getString(R.string.preferences_weigh_minimum), getResources().getBoolean(R.bool.preferences_weigh_minimum)) == true) {
                        textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                        textValue.setTextColor(Color.YELLOW);
                    }
                }
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case PERCENT_WEIGHING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);

                textValue.setText(ApplicationManager.getInstance().getPercent()+ " %");
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsString()+ " g");
                break;

            case CHECK_WEIGHING:
                String cmode = prefs.getString(getString(R.string.preferences_check_displayoptions),"");
                String checklimitmode = prefs.getString(getString(R.string.preferences_check_limitmode),"");
                double current = ApplicationManager.getInstance().getTaredValueInGram();
                double under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                double over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
                if(checklimitmode.equals("1")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                     over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
                }
                if(checklimitmode.equals("2")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getCheckNominaldouble()-ApplicationManager.getInstance().getCheckNominalToleranceUnderdouble();
                    over = ApplicationManager.getInstance().getCheckNominaldouble()+ApplicationManager.getInstance().getCheckNominalToleranceOverdouble();
                }



                if(cmode.equals("1")) {
                    textInstruction.setText("");
                    textValue.setTextColor(Color.WHITE);


                    textInstruction.setText("");

                    if (current<under){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("↓   "+ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    }

                    if (current>over){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("↑   "+ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    }

                    if (current>=under && current<=over){
                        textValue.setTextColor(Color.GREEN);
                        textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());;
                    }


                    textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if(cmode.equals("2")) {

                    textInstruction.setText("");

                    if (current<under){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("Value to low");
                    }

                    if (current>over){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("Value to high");
                    }

                    if (current>=under && current<=over){
                        textValue.setTextColor(Color.GREEN);
                        textValue.setText("OK");
                    }


                    textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                break;

            case DENSITIY_DETERMINATION:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.BLUE);
                break;

            case FILLING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case TOTALIZATION:
                textInstruction.setText("Place sample on the pan. Press ADD TO STATS to add to the total.");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            default:
                textValue.setTextColor(Color.WHITE);
                textValue.setText("not implemented");
                break;

        }

        //Update Loading bar
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) barload.getLayoutParams();
        int width = (int) (Scale.getInstance().getWeightInGram()*5.83);
        if(width<0){
            width = 0;
        }
        if(width > 700){
            width = 700;
        }
        params.width = width;
        barload.setLayoutParams(params);

        if(ApplicationManager.getInstance().getSumInGram() > 100){
            barload.setBackgroundColor(Color.RED);

        }else{

            barload.setBackgroundColor(Color.GREEN);
        }


    }


    @Override
    public void onStableChanged(boolean isStable)
    {
        if(isStable){
            imageStable.setVisibility(View.VISIBLE);
        }else {
            imageStable.setVisibility(View.INVISIBLE);
        }
    }
}
