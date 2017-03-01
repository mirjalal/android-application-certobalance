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
import com.certoclav.certoscale.listener.RecipeEntryListener;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;


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
                Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex).setMeasuredWeight(currentWeight);
                Log.e("MeasuredWeight",String.format("%.4f",ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex).getMeasuredWeight()));


                currentRecipeStepIndex++;
                ApplicationManager.getInstance().getCurrentRecipeEntry().setMeasuredWeight(ApplicationManager.getInstance().getTaredValueInGram());
                if(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size() > currentRecipeStepIndex) {

                    RecipeEntry recipeEntry = ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex);
                    recipeEntry.setWeight(recipeEntry.getWeight()*ApplicationManager.getInstance().getScalingFactor());
                    ApplicationManager.getInstance().setCurrentRecipeEntry(recipeEntry);
                    ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
                }else{
                    Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION);
                    ApplicationManager.getInstance().setTareInGram(0d);
                    currentRecipeStepIndex = 0;
                }
            }
        });
        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        currentRecipeStepIndex = 0;
        ApplicationManager.getInstance().setOnRecipeEntryListener(this);
        Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION_RUNNING);
        if(ApplicationManager.getInstance().getCurrentRecipe()!= null){
           if(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().isEmpty() == false) {
               RecipeEntry recipeEntry = ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(currentRecipeStepIndex);
               recipeEntry.setWeight(recipeEntry.getWeight()*ApplicationManager.getInstance().getScalingFactor());
               ApplicationManager.getInstance().setCurrentRecipeEntry(recipeEntry);
           }else{
               Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION);

           }
        }else{
            Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION);

        }
        if(ApplicationManager.getInstance().getCurrentRecipeEntry() != null){
            textInstruction.setText("Please put " + ApplicationManager.getInstance().getCurrentRecipeEntry().getWeight()*ApplicationManager.getInstance().getScalingFactor() + " g of " + ApplicationManager.getInstance().getCurrentRecipeEntry().getName() + " on the pan and press NEXT");
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
            textInstruction.setText("Please put " + ApplicationManager.getInstance().getCurrentRecipeEntry().getWeight()*ApplicationManager.getInstance().getScalingFactor() + " g of " + ApplicationManager.getInstance().getCurrentRecipeEntry().getName() + " on the pan and press NEXT");
        }
    }
}
