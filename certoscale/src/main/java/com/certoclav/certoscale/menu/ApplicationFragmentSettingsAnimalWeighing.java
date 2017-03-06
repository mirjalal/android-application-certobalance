package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.graphics.Paint;
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


public class ApplicationFragmentSettingsAnimalWeighing extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonAveragingTime = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_animal_weighing,container, false);

        buttonAveragingTime = (Button) rootView.findViewById(R.id.application_settings_animal_button_averaging_time);
        
        buttonAveragingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_averaging_time);
                    ((TextView)dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("s");
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
                                ApplicationManager.getInstance().getCurrentLibrary().setAveragingTime(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setAveragingTime(10);
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
        buttonAveragingTime.setText(getString(R.string.averaging_time)+ String.format("%.2f",ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime())+"s");


        super.onResume();



    }

    @Override
    public void onPause() {

        super.onPause();

    }




}
