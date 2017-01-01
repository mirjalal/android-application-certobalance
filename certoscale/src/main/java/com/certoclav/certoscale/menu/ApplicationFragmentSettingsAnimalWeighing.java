package com.certoclav.certoscale.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;


public class ApplicationFragmentSettingsAnimalWeighing extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_animal_weighing,container, false);

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
