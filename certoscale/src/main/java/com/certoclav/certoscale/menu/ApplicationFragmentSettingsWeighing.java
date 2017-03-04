package com.certoclav.certoscale.menu;

import android.app.Dialog;
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

import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.io.IOException;
import java.text.ParseException;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentSettingsWeighing extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonMinimumWeight = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_weighing,container, false);
        buttonMinimumWeight =  (Button) rootView.findViewById(R.id.application_settings_weighing_button_minimum);
        buttonMinimumWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle("Please enter the under limit weight");
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(ApplicationManager.getInstance().getCurrentUnit().getName());

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
                                Double inputvalTransformed = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                ApplicationManager.getInstance().getCurrentLibrary().setUnderLimit(inputvalTransformed);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setUnderLimit(0);
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
        buttonMinimumWeight.setText(getString(R.string.minimum_weight)+ "\n"+ ApplicationManager.getInstance().getUnderLimitAsStringWithUnit());
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();

    }




}
