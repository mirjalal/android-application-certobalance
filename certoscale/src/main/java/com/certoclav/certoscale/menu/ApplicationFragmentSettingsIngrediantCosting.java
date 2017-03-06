

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

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
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
                intent.putExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK,true);
                getActivity().startActivity(intent);
            }
        });





        buttonCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_text);
                    dialog.setTitle(R.string.please_enter_the_name_of_the_currency);

                    // set the custom dialog components - text, image and button

                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);

                            ApplicationManager.getInstance().setCurrency(editText.getText().toString());

                            dialog.dismiss();
                            onResume();


                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        if(ApplicationManager.getInstance().getCurrentItem() != null) {
            buttonRecipe.setText(R.string.current_item+"\n" + ApplicationManager.getInstance().getCurrentItem().getName());
        }else{
            buttonRecipe.setText(R.string.click_to_choose_item);
        }

        if(ApplicationManager.getInstance().getCurrency() == null){
            buttonCurrency.setText(R.string.currency+":  ");
        }
        else{
            buttonCurrency.setText(getString(R.string.currency)+":  "+ ApplicationManager.getInstance().getCurrency());
        }

        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
