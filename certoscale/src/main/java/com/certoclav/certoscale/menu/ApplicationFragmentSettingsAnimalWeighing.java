package com.certoclav.certoscale.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.graph.GraphService;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;

import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;


public class ApplicationFragmentSettingsAnimalWeighing extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonMinimumWeight = null;
    private LinearLayout graphContainer = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_animal_weighing,container, false);

        graphContainer = (LinearLayout) rootView.findViewById(R.id.application_settings_animal_container_graph);
        Button buttonStart = (Button) rootView.findViewById(R.id.application_settings_animal_button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphContainer.removeAllViews();
                graphContainer.addView(GraphService.getInstance().getCurrentGraph(getActivity()));
                Scale.getInstance().setScaleApplication(ANIMAL_WEIGHING_CALCULATING);
            }
        });

        Button buttonAveragingTime = (Button) rootView.findViewById(R.id.application_settings_animal_button_averaging_time);


        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {


        super.onResume();



    }

    @Override
    public void onPause() {
        Scale.getInstance().setScaleApplication(ScaleApplication.ANIMAL_WEIGHING);
        super.onPause();

    }




}
