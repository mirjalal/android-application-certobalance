package com.certoclav.certoscale.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.RecipeElementAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.listener.RecipeEntryListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.settings.recipe.MenuRecipeEditActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ApplicationFragmentFormulation extends Fragment implements RecipeEntryListener  {

private TextView textInstruction = null;
    private Button buttonNext = null;
    private int currentRecipeStepIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_formulation,container, false);
        textInstruction = (TextView) rootView.findViewById(R.id.application_fragment_formulation_text);
        buttonNext = (Button) rootView.findViewById(R.id.application_fragment_formulation_button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set measured Weight
                ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex).setMeasuredWeight(ApplicationManager.getInstance().getTaredValueInGram());
                ApplicationManager.getInstance().getCurrentRecipeEntry().setMeasuredWeight(ApplicationManager.getInstance().getTaredValueInGram());


                //ApplicationManager.getInstance().getCurrentRecipe().
                ApplicationManager.getInstance().getCurrentRecipe().setMeasuredWegiht(currentRecipeStepIndex);


                Log.e("MeasuredWeight",String.format("%.4f",ApplicationManager.getInstance().getCurrentRecipeEntry().getMeasuredWeight()));
                Log.e("MeasuredWeight",String.format("%.4f",ApplicationManager.getInstance().getCurrentRecipeEntry().getWeight()));
                Log.e("MeasuredWeight",ApplicationManager.getInstance().getCurrentRecipeEntry().getName());



                //ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().set(currentRecipeStepIndex,
                //        ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex)).setMeasuredWeight(ApplicationManager.getInstance().getTaredValueInGram());

                currentRecipeStepIndex++;
                ApplicationManager.getInstance().getCurrentRecipeEntry().setMeasuredWeight(ApplicationManager.getInstance().getTaredValueInGram());
                if(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size() > currentRecipeStepIndex) {
                    ApplicationManager.getInstance().setCurrentRecipeEntry(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex));
                    ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
                }else{
                    Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION);
                    ApplicationManager.getInstance().setTareInGram(0d);
                }
            }
        });
        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {

        ApplicationManager.getInstance().setOnRecipeEntryListener(this);
        Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION_RUNNING);
        if(ApplicationManager.getInstance().getCurrentRecipe()!= null){
           if(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().isEmpty() == false) {
               ApplicationManager.getInstance().setCurrentRecipeEntry(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex));
           }else{
               Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION);

           }
        }else{
            Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION);

        }
        if(ApplicationManager.getInstance().getCurrentRecipeEntry() != null){
            textInstruction.setText("Please put " + ApplicationManager.getInstance().getCurrentRecipeEntry().getWeight() + " g of " + ApplicationManager.getInstance().getCurrentRecipeEntry().getName() + " on the pan and press NEXT");
        }

        super.onResume();



    }

    @Override
    public void onPause() {
        ApplicationManager.getInstance().removeOnRecipeEntryListener(this);
        super.onPause();


    }


    @Override
    public void onRecipeEntryChanged(RecipeEntry entry) {
        if(ApplicationManager.getInstance().getCurrentRecipeEntry() != null){
            textInstruction.setText("Please put " + ApplicationManager.getInstance().getCurrentRecipeEntry().getWeight() + " g of " + ApplicationManager.getInstance().getCurrentRecipeEntry().getName() + " on the pan and press NEXT");
        }
    }
}
