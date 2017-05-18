package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.certoclav.certoscale.supervisor.ApplicationManager;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentSettingsSQC extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonNominal = null;
    private Button buttonPTolerance1 = null;
    private Button buttonPTolerance2 = null;
    private Button buttonNTolerance1 = null;
    private Button buttonNTolerance2 = null;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_sqc,container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String mode = prefs.getString(getString(R.string.preferences_statistic_tolerance_mode), "1");

        buttonNominal =  (Button) rootView.findViewById(R.id.application_settings_sqc_button_nominal);
        buttonNominal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_float);
                    dialog.setTitle(R.string.please_enter_the_nominal);
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
                                ApplicationManager.getInstance().getCurrentLibrary().setSQCNominal(inputvalTransformed);

                            }catch (NumberFormatException e){
                                ApplicationManager.getInstance().getCurrentLibrary().setSQCNominal(0);
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

        buttonPTolerance1 =  (Button) rootView.findViewById(R.id.application_settings_sqc_button_ptolerance1);
        buttonPTolerance1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal()==0){
                    Toast.makeText(getActivity(), R.string.please_enter_the_nominal_weight_first, Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_edit_float);
                        dialog.setTitle(R.string.please_enter_the_first_pos_tol);
                        if (mode.equals("1")) {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("%");
                        }
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

                                    if (mode.equals("1")) {
                                        Double inputvalTransformed = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCpTolerance1(inputvalTransformed);
                                    } else {
                                        double current = ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal();
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCpTolerance1((inputval / 100) * current);
                                    }

                                } catch (NumberFormatException e) {
                                    ApplicationManager.getInstance().getCurrentLibrary().setSQCpTolerance1(0);
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
        });


        buttonPTolerance2 =  (Button) rootView.findViewById(R.id.application_settings_sqc_button_ptolerance2);
        buttonPTolerance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal()==0){
                    Toast.makeText(getActivity(), R.string.please_enter_the_nominal_weight_first, Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_edit_float);
                        dialog.setTitle(R.string.please_enter_the_second_pos_tol);
                        if (mode.equals("1")) {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("%");
                        }
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

                                    if (mode.equals("1")) {
                                        Double inputvalTransformed = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCpTolerance2(inputvalTransformed);
                                    } else {
                                        double current = ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal();
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCpTolerance2((inputval / 100) * current);
                                    }

                                } catch (NumberFormatException e) {
                                    ApplicationManager.getInstance().getCurrentLibrary().setSQCpTolerance2(0);
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
        });


        buttonNTolerance1 =  (Button) rootView.findViewById(R.id.application_settings_sqc_button_ntolerance1);
        buttonNTolerance1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal()==0){
                    Toast.makeText(getActivity(), R.string.please_enter_the_nominal_weight_first, Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_edit_float);
                        dialog.setTitle(R.string.please_enter_the_first_neg_tol);

                        if (mode.equals("1")) {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("%");
                        }
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

                                    if (mode.equals("1")) {
                                        Double inputvalTransformed = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCnTolerance1(inputvalTransformed);
                                    } else {
                                        double current = ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal();
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCnTolerance1((inputval / 100) * current);
                                    }

                                } catch (NumberFormatException e) {
                                    ApplicationManager.getInstance().getCurrentLibrary().setSQCnTolerance1(0);
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
        });


        buttonNTolerance2 =  (Button) rootView.findViewById(R.id.application_settings_sqc_button_ntolerance2);
        buttonNTolerance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal()==0){
                    Toast.makeText(getActivity(), R.string.please_enter_the_nominal_weight_first, Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_edit_float);
                        dialog.setTitle(R.string.please_enter_the_second_neg_tol);

                        if (mode.equals("1")) {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText(ApplicationManager.getInstance().getCurrentUnit().getName());
                        } else {
                            ((TextView) dialog.findViewById(R.id.dialog_edit_number_text_unit)).setText("%");
                        }
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
                                    if (mode.equals("1")) {
                                        Double inputvalTransformed = ApplicationManager.getInstance().transformCurrentUnitToGram(inputval);
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCnTolerance2(inputvalTransformed);
                                    } else {
                                        double current = ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal();
                                        ApplicationManager.getInstance().getCurrentLibrary().setSQCnTolerance2((inputval / 100) * current);
                                    }

                                } catch (NumberFormatException e) {
                                    ApplicationManager.getInstance().getCurrentLibrary().setSQCnTolerance2(0);
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
        });

        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mode = prefs.getString(getString(R.string.preferences_statistic_tolerance_mode), "1");

        if (mode.equals("1")) {
            buttonNominal.setText(getString(R.string.nominal) + "\n" + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal()));
            buttonPTolerance1.setText(getString(R.string.tolerance_plus_1) + "\n" + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance1()));
            buttonPTolerance2.setText(getString(R.string.tolerance_plus_2) + "\n" + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance2()));
            buttonNTolerance1.setText(getString(R.string.tolerance_minus_1) + "\n" + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentLibrary().getSQCnTolerance1()));
            buttonNTolerance2.setText(getString(R.string.tolerance_minus_2) + "\n" + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentLibrary().getSQCnTolerance2()));

        }
        if(mode.equals("2")){
            double nominal=ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal();
            if (nominal!=0) {
                buttonNominal.setText(getString(R.string.nominal) + "\n" + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal()));
                buttonPTolerance1.setText(getString(R.string.tolerance_plus_1) + "\n" + String.format("%.4f", (ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance1() / nominal) * 100) + " %");
                buttonPTolerance2.setText(getString(R.string.tolerance_plus_2) + "\n" + String.format("%.4f", (ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance2() / nominal) * 100) + " %");
                buttonNTolerance1.setText(getString(R.string.tolerance_minus_1) + "\n" + String.format("%.4f", (ApplicationManager.getInstance().getCurrentLibrary().getSQCnTolerance1() / nominal) * 100) + " %");
                buttonNTolerance2.setText(getString(R.string.tolerance_minus_2) + "\n" + String.format("%.4f", (ApplicationManager.getInstance().getCurrentLibrary().getSQCnTolerance2() / nominal) * 100) + " %");
            }

        }


        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }




}
