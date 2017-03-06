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

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.settings.recipe.MenuRecipeActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;


public class ApplicationFragmentSettingsFormulation extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonRecipe = null;
    private Button buttonScalingFactor=null;

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
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(String.format("%.4f",ApplicationManager.getInstance().getScalingFactor()));

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
                                Double inputval = Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString());
                                ApplicationManager.getInstance().setScalingFactor(inputval);

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

            }
        });


        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        if(ApplicationManager.getInstance().getCurrentRecipe() != null) {
            buttonRecipe.setText(getString(R.string.current_recipe) + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());
        }else{
            buttonRecipe.setText(R.string.click_to_choose_item);
        }

       buttonScalingFactor.setText(getString(R.string.scaling_factor)+"\n"+ApplicationManager.getInstance().getScalingFactor());


        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
