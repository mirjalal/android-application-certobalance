

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
import com.certoclav.certoscale.settings.item.MenuItemActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;

/**
 * Created by Enrico on 16.01.2017.
 */

public class ApplicationFragmentSettingsIngrediantCosting extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonRecipe = null;
    private Button buttonCurrency = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_ingrediant_costing,container, false);

        buttonRecipe = (Button) rootView.findViewById(R.id.application_settings_ingrediant_button_item_name);
        buttonCurrency = (Button) rootView.findViewById(R.id.application_settings_ingrediant_button_Edit_Currency);

        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuItemActivity.class);
                getActivity().startActivity(intent);
            }
        });





        buttonCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle("Please enter the cost currency");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(" ");
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
                               // ApplicationManager.getInstance().setCheckNominalToleranceOver((((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                              //  ApplicationManager.getInstance().setCheckNominalToleranceOver(0);

                            }dialog.dismiss();
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
