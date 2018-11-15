package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.util.ExportUtils;


public class ApplicationFragmentAshDetermination extends Fragment implements ScaleApplicationListener {

    private TextView textInstruction = null;
    private Button buttonNext = null;
    private Button buttonCancel;
    private Dialog warningDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_ash_determination, container, false);
        textInstruction = rootView.findViewById(R.id.application_fragment_ash_determination_text);
        buttonNext = rootView.findViewById(R.id.application_fragment_ash_determination_button_next);
        buttonCancel = rootView.findViewById(R.id.application_fragment_ash_determination_button_cancel);
        initErrorDialog();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ApplicationManager.getInstance().getCurrentProtocol().setIsPending(false);
                ApplicationManager.getInstance().setCurrentProtocol(new DatabaseService(getActivity()).getRecentProtocol());
                Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Scale.getInstance().getScaleApplication()) {
                    case ASH_DETERMINATION_HOME:
                        break;
                    case ASH_DETERMINATION_ENTER_NAME_SAMPLE:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_ENTER_NAME_BEAKER);
                        break;
                    case ASH_DETERMINATION_ENTER_NAME_BEAKER:
                          Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_ENTER_TEMPERATURE_OVEN);
                        break;
                    case ASH_DETERMINATION_ENTER_TEMPERATURE_OVEN:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_WEIGH_BEAKER);
                        break;
                    case ASH_DETERMINATION_WEIGH_BEAKER:
                        if (Scale.getInstance().isStable()) {
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentProtocol().saveBeakerWeight(currentWeight);
                            updateUI();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_WEIGHING_SAMPLE);
                        } else {
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_WEIGHING_SAMPLE:
                        if (Scale.getInstance().isStable()) {
                            saveAshDeterminationProtocols();
                            final Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            if (currentWeight - ApplicationManager.getInstance().getCurrentProtocol().getBeakerWeight() < 0.5){
                                TextView errorMessage= warningDialog.findViewById(R.id.dialog_warning_txt_message);
                                errorMessage.setText("The probe's weight is less than 0.5 grams!");
                                TextView ignoreButton = warningDialog.findViewById(R.id.dialog_warning_btn_ignore);
                                TextView abortButton = warningDialog.findViewById(R.id.dialog_warning_btn_abort);
                                ignoreButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ApplicationManager.getInstance().getCurrentProtocol().saveBeakerAndSampleWeight(currentWeight);
                                        saveProtocolContent();
                                        updateUI();
                                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_WAIT_FOR_GLOWING);
                                        warningDialog.dismiss();
                                    }
                                });
                                abortButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ApplicationManager.getInstance().setCurrentProtocol(new DatabaseService(getActivity()).getRecentProtocol());
                                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_HOME);
                                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED);
                                        warningDialog.dismiss();
                                    }
                                });
                                warningDialog.show();
                            }else{
                                ApplicationManager.getInstance().getCurrentProtocol().saveBeakerAndSampleWeight(currentWeight);
                                saveProtocolContent();
                                updateUI();
                                Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_WAIT_FOR_GLOWING);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_WAIT_FOR_GLOWING:
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_HOME);
                        break;
                    case ASH_DETERMINATION_WEIGHING_GLOWED_SAMPLE:
                        if (Scale.getInstance().isStable()) {
                            saveAshDeterminationProtocols();
                            Double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();
                            ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights().add(currentWeight);
                            ApplicationManager.getInstance().getCurrentProtocol().setRecentWeight(currentWeight - ApplicationManager.getInstance().getCurrentProtocol().getBeakerWeight());
                            saveProtocolContent();
                            updateUI();
                            Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_CHECK_DELTA_WEIGHT);
                        } else {
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ASH_DETERMINATION_CHECK_DELTA_WEIGHT:
                        if (Scale.getInstance().isStable()){
                            if (ApplicationManager.getInstance().getTaredValueInGram() > ApplicationManager.getInstance().getCurrentProtocol().getAshWeightBeakerWithSample() + 0.005) {
                                TextView errorMessage= warningDialog.findViewById(R.id.dialog_warning_txt_message);
                                errorMessage.setText("The beaker with the material is heavier after glowing than before!");
                                TextView ignoreButton = warningDialog.findViewById(R.id.dialog_warning_btn_ignore);
                                TextView abortButton = warningDialog.findViewById(R.id.dialog_warning_btn_abort);
                                ignoreButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED);
                                        Toast.makeText(getActivity(), "Protokoll gespeichert", Toast.LENGTH_LONG).show();
                                        warningDialog.dismiss();
                                    }
                                });
                                abortButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ApplicationManager.getInstance().setCurrentProtocol(new DatabaseService(getActivity()).getRecentProtocol());
                                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED);
                                        warningDialog.dismiss();
                                    }
                                });
                                warningDialog.show();
                            } else {
                                //ApplicationManager.getInstance().getCurrentProtocol().setIsPending(false);
                                Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED);
                                updateUI();
                                Toast.makeText(getActivity(), "Protokoll gespeichert", Toast.LENGTH_LONG).show();
                            }
                            break;
                        }else{
                            Toast.makeText(getActivity(), "Bitte warten Sie bis das Gewicht stabil ist", Toast.LENGTH_LONG).show();
                        }
                    case ASH_DETERMINATION_BATCH_FINISHED:
                        saveAshDeterminationProtocols();
                        saveProtocolContent();
                        break;
                }
            }
        });
        return rootView;
    }


    @Override
    public void onResume() {
        Scale.getInstance().setOnApplicationListener(this);
        updateUI();
        super.onResume();
    }

    private void updateUI() {
        switch (Scale.getInstance().getScaleApplication()) {
            case ASH_DETERMINATION_HOME:
                textInstruction.setText("Drücken Sie START um die Aschewertbestimmung zu starten");
                buttonNext.setText("");
                break;
            case ASH_DETERMINATION_ENTER_NAME_SAMPLE:
                if (ApplicationManager.getInstance().getCurrentProtocol().getAshSampleName().isEmpty())
                    showBatchNameEditor();
                textInstruction.setText("Geben Sie die Probennummer an. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_ENTER_NAME_BEAKER:
                if (ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName().isEmpty())
                    showBeakerNameEditor();
                textInstruction.setText("Geben Sie die Tiegelnummer an. Bestätigen Sie mit WEITER");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_ENTER_TEMPERATURE_OVEN:
                showOvenTemperatureDialog();
                break;
            case ASH_DETERMINATION_WEIGH_BEAKER:
                //textInstruction.setText("Legen Sie Tiegel " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName() + " auf die Waage");
                textInstruction.setText("Place the beaker " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName() + " on the scale");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_WEIGHING_SAMPLE:
                textInstruction.setText("Place the beaker with the probe on the scale");
                //textInstruction.setText("Legen Sie die Probe in den Tiegel");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_WAIT_FOR_GLOWING:
                //textInstruction.setText("Entnehmen Sie den Tiegel zur Glühung");
                textInstruction.setText("Remove the beaker for annealing");
                buttonNext.setText("SPEICHERN");
                break;
            case ASH_DETERMINATION_WEIGHING_GLOWED_SAMPLE:
                //textInstruction.setText("Legen Sie Tiegel " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName() + " auf die Waage");
                textInstruction.setText("Place the beaker " + ApplicationManager.getInstance().getCurrentProtocol().getAshBeakerName() + " on the scales for after annealing weighting");
                buttonNext.setText("WEITER");
                break;
            case ASH_DETERMINATION_CHECK_DELTA_WEIGHT:
                textInstruction.setText("Checking plausibility conditions");
                buttonNext.setText("SPEICHERN");
                break;
            case ASH_DETERMINATION_BATCH_FINISHED:
                //textInstruction.setText("Die Messung wurde Beendet.");
                textInstruction.setText("The process is complete!");
                buttonNext.setText("WEITER");
                break;
        }

    }

    @Override
    public void onPause() {
        Scale.getInstance().removeOnApplicationListener(this);
        super.onPause();
    }

    private void initErrorDialog(){
        try {
            warningDialog = new Dialog(getActivity());
            warningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            warningDialog.setContentView(R.layout.dialog_warning);
            warningDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBatchNameEditor() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_text);
            dialog.setTitle("Geben Sie die Probennummer ein");
            dialog.setCancelable(false);
            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
            editText.setSingleLine(true);
            Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationManager.getInstance().setCurrentProtocol(new DatabaseService(getActivity()).getRecentProtocol());
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
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_ENTER_NAME_BEAKER);
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOvenTemperatureDialog() {
        try {
            final Dialog dialog = new Dialog(getActivity(), R.style.TemperatureDialog);
            dialog.setContentView(R.layout.dialog_oven_temperature);
            //dialog.setTitle("Select oven temperature");
            dialog.setCancelable(false);

            final EditText ovenTemperature = dialog.findViewById(R.id.dialog_oven_temperature_edt_temp);

            Button button550 = dialog.findViewById(R.id.dialog_oven_temperature_btn_550);
            Button button600 = dialog.findViewById(R.id.dialog_oven_temperature_btn_600);
            Button button900 = dialog.findViewById(R.id.dialog_oven_temperature_btn_900);

            button550.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ovenTemperature.setText("550");
                }
            });
            button600.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ovenTemperature.setText("600");
                }
            });
            button900.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ovenTemperature.setText("900");
                }
            });

            Button buttonSave = dialog.findViewById(R.id.dialog_oven_temperature_btn_save);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ovenTemperature.getText().toString().length() > 0 && Double.parseDouble(ovenTemperature.getText().toString()) > 0){
                        ApplicationManager.getInstance().getCurrentProtocol().setOvenTemperature(Double.parseDouble(ovenTemperature.getText().toString()));
                        Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_WEIGH_BEAKER);
                        dialog.dismiss();
                    }
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
            dialog.setCancelable(false);
            dialog.setTitle("Geben Sie die Tiegelnummer ein");
            EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
            editText.setSingleLine(true);
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            Button dialogButtonNo = dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationManager.getInstance().setCurrentProtocol(new DatabaseService(getActivity()).getRecentProtocol());
                    dialog.dismiss();
                }
            });
            Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
                    try {
                        DatabaseService databaseService = new DatabaseService(getActivity());
                        if (!editText.getText().toString().isEmpty()) {
                            if (!databaseService.isInDatabase(editText.getText().toString())){
                                ApplicationManager.getInstance().getCurrentProtocol().setAshBeakerName((editText.getText().toString()));
                                Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_ENTER_TEMPERATURE_OVEN);
                                dialog.dismiss();
                            }else{
                                Toast.makeText(getActivity(), "Der Becher mit diesem Namen existiert bereits!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (NumberFormatException e) {
                        ApplicationManager.getInstance().getCurrentProtocol().setAshBeakerName("");
                    }
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
        updateUI();
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