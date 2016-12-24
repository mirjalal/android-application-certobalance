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
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentWeight extends Fragment implements WeightListener {
    private FrameLayout barload = null;
    private TextView textInstruction = null;
    private TextView textSum = null;
    private TextView textValue = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_weight_display,container, false);
        //Access to views from menu_main xml file
        barload = (FrameLayout) rootView.findViewById(R.id.menu_main_bar_load);
        textInstruction = (TextView) rootView.findViewById(R.id.menu_main_text_instruction);
        textSum = (TextView) rootView.findViewById(R.id.menu_main_text_information);
        textValue = (TextView) rootView.findViewById(R.id.menu_main_text_value);

        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        Scale.getInstance().setOnWeightListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Scale.getInstance().removeOnWeightListener(this);
    }





    @Override
    public void onWeightChanged(Double weight, String unit) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        switch (Scale.getInstance().getScaleApplication()){
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
            case WEIGHING:
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

            case DENSITIY_DETERMINATION:
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.BLUE);



            default:
                //Show weight to the user in respect to the TARA weight
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                //Show measured total weight to the user
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
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

        if(Scale.getInstance().getWeightInGram()  == 0){
            textInstruction.setText("Please place item");
        }else{
            textInstruction.setText("");
        }


    }
}
