package com.certoclav.certoscale.menu;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.ReferenceField;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;



public class ApplicationFragmentTable extends Fragment implements WeightListener {


    private float tara = 0;
    private TextView textTara = null;
    private List<ReferenceField> listReferenceFields = new ArrayList<ReferenceField>();

    @Override
    public void onResume() {
        super.onResume();
        Scale.getInstance().setOnWeightListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Scale.getInstance().removeOnWeightListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_table,container, false);

        //8 Textfields for showing data of interest
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_1),(TextView) rootView.findViewById(R.id.reference_val_1)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_2),(TextView) rootView.findViewById(R.id.reference_val_2)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_3),(TextView) rootView.findViewById(R.id.reference_val_3)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_4),(TextView) rootView.findViewById(R.id.reference_val_4)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_5),(TextView) rootView.findViewById(R.id.reference_val_5)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_6),(TextView) rootView.findViewById(R.id.reference_val_6)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_7),(TextView) rootView.findViewById(R.id.reference_val_7)));
        listReferenceFields.add(new ReferenceField((TextView)rootView.findViewById(R.id.reference_text_8),(TextView) rootView.findViewById(R.id.reference_val_8)));
        for(ReferenceField refField : listReferenceFields){
            refField.getTextName().setText("");
            refField.getTextValue().setText("");
        }

        return rootView;
    }


    @Override
    public void onWeightChanged(Double weight, String unit) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        //TODO: define fixed indexes instead of hardcoding index positions.
        //TODO: show only data which is enabled in settings menu

        //Clear listReferenceFields
        if (listReferenceFields.isEmpty()==false){
            for(ReferenceField refField : listReferenceFields){
                refField.getTextName().setText("");
                refField.getTextValue().setText("");
            }
        }


        int tara=2;
        int brutto=0;
        int netto=4;
        int loadp=1;

        // Weighing
        int minweight=3;

        //Part Counting
        int apw=3;

        //Percent Weighing
        int reference_weight=1;
        int difference_weight=3;
        int difference_percent=5;



        switch (Scale.getInstance().getScaleApplication()){
            case WEIGHING:

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_tara_visible),getResources().getBoolean(R.bool.preferences_weigh_tara_visible))==true) {
                    listReferenceFields.get(tara).getTextName().setText("TARA");
                    listReferenceFields.get(tara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_brutto_visible),getResources().getBoolean(R.bool.preferences_weigh_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_weigh_netto_visible),getResources().getBoolean(R.bool.preferences_weigh_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_load_visible),getResources().getBoolean(R.bool.preferences_weigh_load_visible))==true) {
                    listReferenceFields.get(loadp).getTextName().setText("LOAD [%]");
                    listReferenceFields.get(loadp).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");
                }


                //listReferenceFields.get(3).getTextName().setText("LOAD [g]");
                //listReferenceFields.get(3).getTextValue().setText(ApplicationManager.getInstance().getLoadInGramAsStringWithUnit());


                if (prefs.getBoolean(getString(R.string.preferences_weigh_minimum_visible),getResources().getBoolean(R.bool.preferences_weigh_minimum_visible))==true) {
                    listReferenceFields.get(minweight).getTextName().setText("MINIMUM WEIGHT");
                    listReferenceFields.get(minweight).getTextValue().setText(ApplicationManager.getInstance().getUnderLimitAsStringInGram()+ " g");
                }

                break;
            case PART_COUNTING:
                if  (prefs.getBoolean(getString(R.string.preferences_counting_tara_visible),getResources().getBoolean(R.bool.preferences_counting_tara_visible))==true) {
                    listReferenceFields.get(tara).getTextName().setText("TARA");
                    listReferenceFields.get(tara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_counting_brutto_visible),getResources().getBoolean(R.bool.preferences_counting_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_counting_netto_visible),getResources().getBoolean(R.bool.preferences_counting_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_weigh_load_visible),getResources().getBoolean(R.bool.preferences_weigh_load_visible))==true) {
                    listReferenceFields.get(loadp).getTextName().setText("LOAD [%]");
                    listReferenceFields.get(loadp).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");
                }


                if  (prefs.getBoolean(getString(R.string.preferences_counting_apw_visible),getResources().getBoolean(R.bool.preferences_counting_apw_visible))==true){
                    listReferenceFields.get(apw).getTextName().setText("PIECE WEIGHT");
                    listReferenceFields.get(apw).getTextValue().setText(ApplicationManager.getInstance().getAveragePieceWeightAsStringInGram() + " g");
                }
                break;
            case PERCENT_WEIGHING:
                if  (prefs.getBoolean(getString(R.string.preferences_percent_tara_visible),getResources().getBoolean(R.bool.preferences_percent_tara_visible))==true) {
                    listReferenceFields.get(tara).getTextName().setText("TARA");
                    listReferenceFields.get(tara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_percent_brutto_visible),getResources().getBoolean(R.bool.preferences_percent_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_percent_netto_visible),getResources().getBoolean(R.bool.preferences_percent_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }


                if  (prefs.getBoolean(getString(R.string.preferences_percent_reference_visible),getResources().getBoolean(R.bool.preferences_percent_reference_visible))==true) {
                    listReferenceFields.get(reference_weight).getTextName().setText("REFERENCE");
                    listReferenceFields.get(reference_weight).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_percent_reference_visible),getResources().getBoolean(R.bool.preferences_percent_reference_visible))==true) {
                    listReferenceFields.get(difference_weight).getTextName().setText("DIFFERENCE");
                    listReferenceFields.get(difference_weight).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_percent_difference_percent_visible),getResources().getBoolean(R.bool.preferences_percent_difference_percent_visible))==true) {
                    listReferenceFields.get(difference_percent).getTextName().setText("DIFFERENCE %");
                    listReferenceFields.get(difference_percent).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }
                break;
            case CHECK_WEIGHING:
                if  (prefs.getBoolean(getString(R.string.preferences_check_tara_visible),getResources().getBoolean(R.bool.preferences_check_tara_visible))==true) {
                    listReferenceFields.get(tara).getTextName().setText("TARA");
                    listReferenceFields.get(tara).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());
                }

                if  (prefs.getBoolean(getString(R.string.preferences_check_brutto_visible),getResources().getBoolean(R.bool.preferences_check_brutto_visible))==true) {
                    listReferenceFields.get(brutto).getTextName().setText("BRUTTO");
                    listReferenceFields.get(brutto).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if  (prefs.getBoolean(getString(R.string.preferences_check_netto_visible),getResources().getBoolean(R.bool.preferences_check_netto_visible))==true) {
                    listReferenceFields.get(netto).getTextName().setText("NETTO");
                    listReferenceFields.get(netto).getTextValue().setText(ApplicationManager.getInstance().getTaredValueAsStringInGram());
                }


                break;
        }

    }
}
