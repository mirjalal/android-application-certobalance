package com.certoclav.certoscale.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.WeightMeasuredListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.ItemListFragment.Callbacks;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentWeight extends Fragment implements WeightMeasuredListener {
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
        Scale.getInstance().setOnWeightMeasuredListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Scale.getInstance().removeOnWeightMeasuredListener(this);
    }



    @Override
    public void onWeightMeasuredChanged(Float value) {

        //Show weight to the user in respect to the TARA weight
        textValue.setText(Scale.getInstance().getRelativeWeightAsStringWithUnit());

        //Show measured total weight to the user
        textSum.setText("SUM: " + Scale.getInstance().getTotalWeightAsStringWithUnit());

        //Update Loading bar
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) barload.getLayoutParams();
        int width = (int) (value*7.0);
        if(width<0){
            width = 0;
        }
        if(width > 700){
            width = 700;
        }
        params.width = width;
        barload.setLayoutParams(params);
        if(value > 100){
            //todo color bar red
        }

        if(Scale.getInstance().getWeightRaw() - Scale.getInstance().getWeightTara() == 0){
            textInstruction.setText("Please place item");
        }else{
            textInstruction.setText("");
        }



    }
}
