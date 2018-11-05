package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
    private Button buttonCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_ash_determination, container, false);
        textInstruction = rootView.findViewById(R.id.application_fragment_ash_determination_text);
        buttonNext = rootView.findViewById(R.id.application_fragment_ash_determination_button_next);
        buttonBack = rootView.findViewById(R.id.application_fragment_ash_determination_button_back);
        buttonCancel = rootView.findViewById(R.id.application_fragment_ash_determination_button_cancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Scale.getInstance().getScaleApplication()) {
                    case ASH_DETERMINATION_1_HOME:
                        break;
                    case ASH_DETERMINATION_2_ENTER_NAME_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_ENTER_NAME_BEAKER);
                        break;
                    case ASH_DETERMINATION_3_ENTER_NAME_BEAKER:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_4_WEIGH_BEAKER);
                        break;
                    case ASH_DETERMINATION_4_WEIGH_BEAKER:
                        if (Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentProtocol().setAshWeightBeaker(currentWeight);
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_5_WEIGHING_SAMPLE);
                        } else {
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_5_WEIGHING_SAMPLE:
                        if (Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentProtocol().setAshWeightBeakerWithSample(currentWeight);
                            saveProtocolContent();
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_6_WAIT_FOR_GLOWING);
                        } else {
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_6_WAIT_FOR_GLOWING:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
                        break;
                    case ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE:
                        if (Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights().add(currentWeight);
                            saveProtocolContent();
                            updateUIAccordingToApplicationState();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT);
                        } else {
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT:
                        if (Math.abs(ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeightsDifference()) >= 0.002) {
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_6_WAIT_FOR_GLOWING);
                        } else {
                            saveAshDeterminationProtocols();
                            ApplicationManager.getInstance().getCurrentProtocol().setIsPending(false);
                            saveProtocolContent();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
                            Toast.makeText(getActivity(), "Protokoll gespeichert", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_9_BATCH_FINISHED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
                        break;
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Scale.getInstance().getScaleApplication()) {
                    case ASH_DETERMINATION_1_HOME:
                        break;
                    case ASH_DETERMINATION_2_ENTER_NAME_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_1_HOME);
                        break;
                    case ASH_DETERMINATION_3_ENTER_NAME_BEAKER:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_2_ENTER_NAME_SAMPLE);
                        break;
                    case ASH_DETERMINATION_4_WEIGH_BEAKER:
                        ApplicationManager.getInstance().getCurrentProtocol().setAshSampleName("");
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_ENTER_NAME_BEAKER);
                        break;
                    case ASH_DETERMINATION_5_WEIGHING_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_4_WEIGH_BEAKER);
                        break;
                    case ASH_DETERMINATION_6_WAIT_FOR_GLOWING:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_5_WEIGHING_SAMPLE);
                        break;
                    case ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_6_WAIT_FOR_GLOWING);
                        break;
                    case ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE);
                        break;
                    case ASH_DETERMINATION_9_BATCH_FINISHED:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT);
                        break;
                }
            }
        });
        return rootView;
    }


    @Override
    public void onResume() {
        Scale.getInstance().setOnApplicationListener(this);
        updateUIAccordingToApplicationState();
        super.onResume();
    }

    private void updateUIAccordingToApplicationState() {
        switch (Scale.getInstance().getScaleApplication()) {
            case ASH_DETERMINATION_1_HOME:
                textInstruction.setText("Drücken Sie START um die Aschewertbestimmung zu starten");
                buttonNext.setText("");
                break;
            case ASH_DETERMINATION_2_ENTER_NAME_SAMPLE:
                if (ApplicationManager.getInstance().getCurrentProtocol().getAshSampleName().isEmpty())
                    showBatchNameEditor();
                textInstruction.setText("Geben Sie die Probennummer an. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_3_ENTER_NAME_BEAKER:
                if (ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName().isEmpty())
                    showBeakerNameEditor();
                textInstruction.setText("Geben Sie die Tiegelnummer an. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_4_WEIGH_BEAKER:
                textInstruction.setText("Legen Sie Tiegel " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName() + " auf die Waage");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_5_WEIGHING_SAMPLE:
                textInstruction.setText("Legen Sie die Probe in den Tiegel");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_6_WAIT_FOR_GLOWING:
                textInstruction.setText("Entnehmen Sie den Tiegel zur Glühung");
                buttonNext.setText("SPEICHERN");
                break;
            case ASH_DETERMINATION_7_WEIGHING_GLOWED_SAMPLE:
                textInstruction.setText("Legen Sie Tiegel " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName() + " auf die Waage");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_8_CHECK_DELTA_WEIGHT:
                if (Math.abs(ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeightsDifference()) >= 0.002) {
                    textInstruction.setText("Die Probe muss noch einmal nachgeglüht werden");
                    buttonNext.setText("SPEICHERN");
                } else {
                    textInstruction.setText("Aschebestimmung abgeschlossen");
                    buttonNext.setText("SPEICHERN");
                }
                break;
            case ASH_DETERMINATION_9_BATCH_FINISHED:
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


    private void showBatchNameEditor() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_text);
            dialog.setTitle("Geben Sie die Probennummer ein");
            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
            editText.setSingleLine(true);
            Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
                    if (!editText.getText().toString().isEmpty()) {
                        ApplicationManager.getInstance().getCurrentProtocol().setAshSampleName((editText.getText().toString()));
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_3_ENTER_NAME_BEAKER);
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBeakerNameEditor() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_text);
            dialog.setTitle("Geben Sie die Tiegelnummer ein");
            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
            editText.setSingleLine(true);
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            // set the custom dialog components - text, image and button

            Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);

                    try {

                        if (editText.getText().toString().isEmpty() == false) {
                            ApplicationManager.getInstance().getCurrentProtocol().setAshBeakerName((editText.getText().toString()));
                            StringBuilder sb = new StringBuilder();
                            sb.append("Tiegel Nr.: " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName());
                            sb.append("     Probe Nr.: " + ApplicationManager.getInstance().getCurrentProtocol().getAshSampleName());
                            ApplicationManager.getInstance().getCurrentProtocol().setName(sb.toString());
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_4_WEIGH_BEAKER);
                        }

                    } catch (NumberFormatException e) {
                        ApplicationManager.getInstance().getCurrentProtocol().setAshBeakerName("");
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveAshDeterminationProtocols() {
        StringBuilder sb = new StringBuilder();
        sb.append("samplenumber" + "," + ApplicationManager.getInstance().getCurrentProtocol().getAshSampleName() + "\r\n");
        sb.append("sampleweight" + "," + ApplicationManager.getInstance().getTransformedWeightAsString(ApplicationManager.getInstance().getCurrentProtocol().getAshWeightBeakerWithSample()) + "\r\n");
        sb.append("ash_percent" + "," + ApplicationManager.getInstance().getCurrentProtocol().getAshResultPercentageAsString() + "\r\n");
        ExportUtils exportUtils = new ExportUtils();
        exportUtils.writeCSVFileToInternalSD(sb.toString());
    }

    @Override
    public void onApplicationChange(ScaleApplication application) {
        updateUIAccordingToApplicationState();
    }

    private void saveProtocolContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(ApplicationManager.getInstance().getProtocolPrinter().getProtocolHeader());
        sb.append(ApplicationManager.getInstance().getProtocolPrinter().getApplicationData());
        sb.append(ApplicationManager.getInstance().getProtocolPrinter().getProtocolFooter());
        ApplicationManager.getInstance().getCurrentProtocol().setContent(sb.toString());
        ApplicationManager.getInstance().getCurrentProtocol().saveIntoDb();
    }
}