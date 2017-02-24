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
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.settings.recipe.MenuRecipeActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;


public class ApplicationFragmentSettingsFormulation extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonRecipe = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_formulation,container, false);

        buttonRecipe = (Button) rootView.findViewById(R.id.application_settings_formulation_button_recipe_name);


        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), MenuRecipeActivity.class);
                intent.putExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK,true);
                getActivity().startActivity(intent);
            }
        });




        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        if(ApplicationManager.getInstance().getCurrentRecipe() != null) {
            buttonRecipe.setText("Current Recipe:\n" + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());
        }else{
            buttonRecipe.setText("Click to choose recipe");
        }


        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
