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
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.util.ExportUtils;


public class ApplicationFragmentAshDetermination extends Fragment implements ScaleApplicationListener {

private TextView textInstruction = null;
    private Button buttonNext = null;
    private Button buttonBack = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_ash_determination,container, false);
        textInstruction = (TextView) rootView.findViewById(R.id.application_fragment_ash_determination_text);
        buttonNext = (Button) rootView.findViewById(R.id.application_fragment_ash_determination_button_next);
        buttonBack = (Button) rootView.findViewById(R.id.application_fragment_ash_determination_button_back);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch(Scale.getInstance().getScaleApplication()) {
                    case ASH_DETERMINATION_1_HOME:
                       // Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_2_BATCH_STARTED);
                        //ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
                        break;
                    case ASH_DETERMINATION_2_BATCH_STARTED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_WEIGH_BEAKER);
                        break;
                    case ASH_DETERMINATION_3_WEIGH_BEAKER:
                        if(Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentLibrary().setAshWeightBeaker(currentWeight);
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_4_WEIGHING_SAMPLE);
                        }else {
                            Toast.makeText(getActivity(),"Bitte warten Sie bis das Gewicht stabil ist",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_4_WEIGHING_SAMPLE:
                        if(Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentLibrary().setAshWeightBeakerWithSample(currentWeight);
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_5_WAIT_FOR_GLOWING);
                        }else {
                            Toast.makeText(getActivity(),"Bitte warten Sie bis das Gewicht stabil ist",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_5_WAIT_FOR_GLOWING:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_6_WEIGHING_GLOWED_SAMPLE);
                        break;
                    case ASH_DETERMINATION_6_WEIGHING_GLOWED_SAMPLE:
                        if(Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentLibrary().setAshDeltaWeight(currentWeight - ApplicationManager.getInstance().getCurrentLibrary().getAshWeightBeakerWithSample());
                            ApplicationManager.getInstance().getCurrentLibrary().setAshWeightBeakerWithSample(currentWeight);
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_7_CHECK_DELTA_WEIGHT);
                        }else {
                            Toast.makeText(getActivity(),"Bitte warten Sie bis das Gewicht stabil ist",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_7_CHECK_DELTA_WEIGHT:
                        if(Math.abs(ApplicationManager.getInstance().getAshDifferenceInGram()) >= 0.002) {
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_5_WAIT_FOR_GLOWING);
                        }else{


                            saveAshDeterminationProtocols();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_8_BATCH_FINISHED);
                            Toast.makeText(getActivity(),"Protokoll gespeichert",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_8_BATCH_FINISHED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
                        break;
                }


            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch(Scale.getInstance().getScaleApplication()) {
                    case ASH_DETERMINATION_1_HOME:
                        break;
                    case ASH_DETERMINATION_2_BATCH_STARTED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
                        break;
                    case ASH_DETERMINATION_3_WEIGH_BEAKER:
                        ApplicationManager.getInstance().setBatchName("");
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_2_BATCH_STARTED);
                        break;
                    case ASH_DETERMINATION_4_WEIGHING_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_WEIGH_BEAKER);
                        break;
                    case ASH_DETERMINATION_5_WAIT_FOR_GLOWING:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_4_WEIGHING_SAMPLE);
                        break;
                    case ASH_DETERMINATION_6_WEIGHING_GLOWED_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_5_WAIT_FOR_GLOWING);
                        break;
                    case ASH_DETERMINATION_7_CHECK_DELTA_WEIGHT:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_6_WEIGHING_GLOWED_SAMPLE);
                        break;
                    case ASH_DETERMINATION_8_BATCH_FINISHED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_7_CHECK_DELTA_WEIGHT);
                        break;
                }


            }
        });



        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {

        Scale.getInstance().setOnApplicationListener(this);
        updateUIAccordingToApplicationState();

        super.onResume();



    }

    private void updateUIAccordingToApplicationState() {

        switch(Scale.getInstance().getScaleApplication()){
            case ASH_DETERMINATION_1_HOME:
                textInstruction.setText("Drücken Sie START um die Aschewertbestimmung zu starten");
                buttonNext.setText("");
                break;
            case ASH_DETERMINATION_2_BATCH_STARTED:
                if(ApplicationManager.getInstance().getBatchName().isEmpty())
                    showBatchNameEditor();
                textInstruction.setText("Geben Sie die Probennummer an. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_3_WEIGH_BEAKER:
                textInstruction.setText("Legen Sie den Tiegel auf die Waage. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_4_WEIGHING_SAMPLE:
                textInstruction.setText("Legen Sie die Probe in den Tiegel. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_5_WAIT_FOR_GLOWING:
                textInstruction.setText("Entnehmen Sie den Tiegel zur Vorveraschung und Glühung. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_6_WEIGHING_GLOWED_SAMPLE:
                textInstruction.setText("Legen Sie den Tiegel zusammen mit der Probe auf die Wägefläche. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_7_CHECK_DELTA_WEIGHT:
                if(Math.abs(ApplicationManager.getInstance().getAshDifferenceInGram()) >= 0.002) {
                    textInstruction.setText("Die Probe muss noch einmal nachgeglüht werden. Bestätigen Sie mit WEITER");
                    buttonNext.setText("WEITER");
                }else{
                    textInstruction.setText("Die Messung wird Beendet. Bestätigen Sie mit WEITER");
                    buttonNext.setText("WEITER");
                }
                break;
            case ASH_DETERMINATION_8_BATCH_FINISHED:
                textInstruction.setText("Die Messung wurde Beendet.");
                buttonNext.setText("WEITER");
                break;
        }

    }

    @Override
    public void onPause() {

        Scale.getInstance().removeOnApplicationListener(this);
        super.onPause();


    }


    private void showBatchNameEditor(){
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_text);
            dialog.setTitle("Geben Sie die Probennummer ein");
            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
            editText.setSingleLine(true);
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

                        if(editText.getText().toString().isEmpty() == false) {
                            ApplicationManager.getInstance().setBatchName((editText.getText().toString()));
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_WEIGH_BEAKER);
                        }

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


    public void saveAshDeterminationProtocols(){

        StringBuilder sb = new StringBuilder();
        sb.append("samplenumber" +  ","+ ApplicationManager.getInstance().getBatchName() + "\r\n");
        sb.append("sampleweight" + "," + ApplicationManager.getInstance().getAshWeightBeakerAsStringWithUnit() + "\r\n");
        sb.append("ashweight" +  ","+ ApplicationManager.getInstance().getAshWeightAsStringWithUnit() + "\r\n");

        ExportUtils exportUtils = new ExportUtils();
        exportUtils.writeCSVFileToInternalSD(sb.toString());


    }

    @Override
    public void onApplicationChange(ScaleApplication application) {
        updateUIAccordingToApplicationState();
    }
}