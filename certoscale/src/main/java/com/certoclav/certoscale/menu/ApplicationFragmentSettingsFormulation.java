package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.recipe.MenuRecipeActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_FREE;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_RUNNING;


public class ApplicationFragmentSettingsFormulation extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonRecipe = null;
    private Button buttonScalingFactor=null;
    private Button buttonCalculateScalingFactor=null;
    private Button buttonSwitchFormulationMode=null;

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

        buttonScalingFactor = (Button) rootView.findViewById(R.id.application_settings_formulation_button_scaling_factor);
        buttonScalingFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_scaling_factor);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("");

                    // set the custom dialog components - text, image and button

                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_number_button_cancel);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_number_button_ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Double scalingFactor = Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString());
                                ApplicationManager.getInstance().setScalingFactor(scalingFactor);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setScalingFactor(1d);
                            }
                            dialog.dismiss();
                            onResume();


                        }
                    });

                    dialog.show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        buttonCalculateScalingFactor = (Button) rootView.findViewById(R.id.application_settings_formulation_button_calculate_scaling_factor);
        buttonCalculateScalingFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ApplicationManager.getInstance().getCurrentRecipe()!=null) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_desired_total_weight_of_the_recipe);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(String.format(ApplicationManager.getInstance().getCurrentUnit().getName()));

                    // set the custom dialog components - text, image and button

                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_number_button_cancel);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_number_button_ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {


                                    Double targetWeightInCurrentUnit = Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString());
                                    Double targetWeightInGram = ApplicationManager.getInstance().transformCurrentUnitToGram(targetWeightInCurrentUnit);

                                    Double totalWeightOfRecipeInGram = ApplicationManager.getInstance().getCurrentRecipe().getRecipeTotalWeight();


                                    ApplicationManager.getInstance().setScalingFactor(targetWeightInGram / totalWeightOfRecipeInGram);

                                    Toast.makeText(getActivity(), getString(R.string.scaling_factor)+" = "+String.format("%.4f",ApplicationManager.getInstance().getScalingFactor())+" "+getString(R.string.calculated_and_updated), Toast.LENGTH_LONG).show();




                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().setScalingFactor(1);
                            }
                            dialog.dismiss();
                            onResume();


                        }
                    });

                    dialog.show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                }else{
                    Toast.makeText(getActivity(), R.string.you_have_to_choose_a_recipe_first, Toast.LENGTH_LONG).show();
                    ApplicationManager.getInstance().setScalingFactor(1);
                }

            }
        });



        buttonSwitchFormulationMode = (Button) rootView.findViewById(R.id.application_settings_formulation_button_switch_formulation_mode);
        buttonSwitchFormulationMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                  if (Scale.getInstance().getScaleApplication()==FORMULATION || Scale.getInstance().getScaleApplication()==FORMULATION_RUNNING){
                        Scale.getInstance().setScaleApplication(FORMULATION_FREE);


                  }else{
                        Scale.getInstance().setScaleApplication(FORMULATION);

                  }

                }
                catch (Exception e)
                {

                }

            }
        });


        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        if(ApplicationManager.getInstance().getCurrentRecipe() != null) {
            buttonRecipe.setText(getString(R.string.current_recipe) + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());
        }else{
            buttonRecipe.setText(getString(R.string.click_to_choose_a_recipe));
        }

       buttonScalingFactor.setText(getString(R.string.scaling_factor)+"\n"+String.format("%.4f",ApplicationManager.getInstance().getScalingFactor()));


        if (Scale.getInstance().getScaleApplication()==FORMULATION || Scale.getInstance().getScaleApplication()==FORMULATION_RUNNING){
            buttonSwitchFormulationMode.setText(getString(R.string.switch_to_free_formulation));
            buttonCalculateScalingFactor.setVisibility(View.VISIBLE);
            buttonRecipe.setVisibility(View.VISIBLE);
            buttonScalingFactor.setVisibility(View.VISIBLE);
        }else{
            buttonSwitchFormulationMode.setText(getString(R.string.switch_to_recipe_mode));
            buttonCalculateScalingFactor.setVisibility(View.GONE);
            buttonRecipe.setVisibility(View.GONE);
            buttonScalingFactor.setVisibility(View.GONE);
        }


        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
