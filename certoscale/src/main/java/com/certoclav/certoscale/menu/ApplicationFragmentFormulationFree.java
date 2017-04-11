package com.certoclav.certoscale.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.listener.RecipeEntryListener;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.settings.recipe.MenuRecipeEditActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.ArrayList;
import java.util.Date;




public class ApplicationFragmentFormulationFree extends Fragment   {

private TextView textInstruction = null;
    private Button buttonNext = null;
    private int currentRecipeStepIndex = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {



        final ArrayList<RecipeEntry> recipeEntries = new ArrayList<RecipeEntry>();
        //recipeEntries.add(new RecipeEntry("H2O",100d,1,"1300131","g","Please tare the balance and place 100g H2O onto the pan and press NEXT",0d));
        //recipeEntries.add(new RecipeEntry("NaCl",0.9,2,"4827383", "g", "Please add 0,9g NaCl and press NEXT",0d));

        //DatabaseService db = new DatabaseService(getContext());

        //Recipe newRecipe= new Recipe("",getString(R.string.my_recipe), recipeEntries,((Long)date.getTime()).toString(), Scale.getInstance().getSafetyKey(),"private",Scale.getInstance().getUser().getEmail());
        //db.insertRecipe(newRecipe);

        View rootView = inflater.inflate(R.layout.menu_application_fragment_formulation,container, false);
        textInstruction = (TextView) rootView.findViewById(R.id.application_fragment_formulation_text);
        buttonNext = (Button) rootView.findViewById(R.id.application_fragment_formulation_button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Scale.getInstance().isStable()) {
                    //set measured Weight
                    Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();



                    Toast.makeText(getActivity(), getString(R.string.item)+" "+currentRecipeStepIndex+" "+getString(R.string.weight)+": "+String.format("%."+Scale.getInstance().getScaleModel().getDecimalPlaces()+"f",currentWeight)+" "+ApplicationManager.getInstance().getCurrentUnit().getName() , Toast.LENGTH_SHORT).show();
                    recipeEntries.add(new RecipeEntry(getString(R.string.item)+currentRecipeStepIndex,currentWeight,currentRecipeStepIndex,"0000000", "g", getString(R.string.pleas_put)+" "+currentWeight+getString(R.string.g_of)+getString(R.string.item)+currentRecipeStepIndex+" "+getString(R.string.on_the_pan_and_press_next),currentWeight));
                    Date date = new Date();
                    Recipe newRecipe= new Recipe("",getString(R.string.my_recipe), recipeEntries,((Long)date.getTime()).toString(), Scale.getInstance().getSafetyKey(),"private",Scale.getInstance().getUser().getEmail());
                    ApplicationManager.getInstance().setCurrentRecipe(newRecipe);


                    currentRecipeStepIndex++;
                    textInstruction.setText(getString(R.string.pleas_put) + " " +getString(R.string.item)  + " " +currentRecipeStepIndex + " " + getString(R.string.on_the_pan_and_press_next));

                }else{
                    if (ApplicationManager.getInstance().getTaredValueInGram()==0.0){
                        Toast.makeText(getActivity(), R.string.the_value_must_be_higher_than_zero, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), getString(R.string.wait_until_the_weight_is_stable), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }


    @Override
    public void onResume() {
        currentRecipeStepIndex = 0;
        Scale.getInstance().setScaleApplication(ScaleApplication.FORMULATION_FREE_RUNNING);
        textInstruction.setText(getString(R.string.pleas_put) + " " +getString(R.string.item)  + " " +currentRecipeStepIndex + " " + getString(R.string.on_the_pan_and_press_next));


        super.onResume();



    }

    @Override
    public void onPause() {
      //  ApplicationManager.getInstance().removeOnRecipeEntryListener(this);
        super.onPause();


    }

/*
    @Override
    public void onRecipeEntryChanged(RecipeEntry entry) {
        if(ApplicationManager.getInstance().getCurrentRecipeEntry() != null){
            if(ApplicationManager.getInstance().getCurrentRecipeEntry().getWeight() == 0){
                textInstruction.setText(ApplicationManager.getInstance().getCurrentRecipeEntry().getDescription() + " - " +getString(R.string.press_next));
            }else {
                textInstruction.setText(getString(R.string.pleas_put) + " " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipeEntry().getScaledWeight()) + " " + getString(R.string.of) + " " + ApplicationManager.getInstance().getCurrentRecipeEntry().getDescription() + " " + getString(R.string.on_the_pan_and_press_next));
            }
            }
    }
    */
}
