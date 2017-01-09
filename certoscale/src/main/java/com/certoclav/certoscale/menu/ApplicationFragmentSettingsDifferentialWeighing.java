package com.certoclav.certoscale.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.settings.item.MenuItemActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;


public class ApplicationFragmentSettingsDifferentialWeighing extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonRecipe = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_differential_weighing,container, false);

        buttonRecipe = (Button) rootView.findViewById(R.id.application_settings_differential_button_item_name);


        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), MenuItemActivity.class);
                getActivity().startActivity(intent);
            }
        });




        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        if(ApplicationManager.getInstance().getCurrentItem() != null) {
            buttonRecipe.setText("Current Item:\n" + ApplicationManager.getInstance().getCurrentItem().getName());
        }else{
            buttonRecipe.setText("Click to choose item");
        }


        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
