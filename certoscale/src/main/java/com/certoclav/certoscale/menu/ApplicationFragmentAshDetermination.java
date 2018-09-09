package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;


public class ApplicationFragmentAshDetermination extends Fragment  {

private TextView textInstruction = null;
    private Button buttonNext = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_ash_determination,container, false);
        textInstruction = (TextView) rootView.findViewById(R.id.application_fragment_ash_determination_text);
        buttonNext = (Button) rootView.findViewById(R.id.application_fragment_ash_determination_button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch(Scale.getInstance().getScaleApplication()) {
                    case ASH_DETERMINATION_1_HOME:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_2_BATCH_STARTED);
                        ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
                        updateUIAccordingToApplicationState();
                        showBatchNameEditor();
                        break;
                    case ASH_DETERMINATION_2_BATCH_STARTED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_TARE_BEAKER);
                        updateUIAccordingToApplicationState();
                        break;
                    case ASH_DETERMINATION_3_TARE_BEAKER:
                        if(Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentLibrary().setAshWeightBeaker(currentWeight);
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_4_WEIGHING_SAMPLE);
                        }else {
                            Toast.makeText(getActivity(),"Bitte warten Sie bis das Gewicht stabil ist",Toast.LENGTH_LONG).show();
                        }
                        break;


                }


            }
        });
        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {


        updateUIAccordingToApplicationState();

        super.onResume();



    }

    private void updateUIAccordingToApplicationState() {

        switch(Scale.getInstance().getScaleApplication()){
            case ASH_DETERMINATION_1_HOME:
                textInstruction.setText("Drücken Sie START um die Aschewertbestimmung zu starten");
                buttonNext.setText("START");
                break;
            case ASH_DETERMINATION_2_BATCH_STARTED:
                textInstruction.setText("Geben Sie die Probennummer an und drücken Sie auf WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_3_TARE_BEAKER:
                textInstruction.setText("Legen Sie den Tiegel auf die Waage und drücken Sie auf WEITER");
                buttonNext.setText("WEITER");
        }

    }

    @Override
    public void onPause() {

        super.onPause();


    }


    private void showBatchNameEditor(){
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_text);
            dialog.setTitle("Geben Sie die Probennummer ein");

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

                    try{

                        ApplicationManager.getInstance().setBatchName((editText.getText().toString()));

                    }catch (NumberFormatException e){
                        ApplicationManager.getInstance().setBatchName("");
                    }

                    dialog.dismiss();
                    onResume();


                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}