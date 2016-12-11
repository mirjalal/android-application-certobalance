package com.certoclav.certoscale.menu;

import android.os.Bundle;
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


        if(value > 100){
            //todo color bar red
        }

        //TODO: define fixed indexes instead of hardcoding index positions.
        //TODO: show only data which is enabled in settings menu

        //standard result fields
        listReferenceFields.get(0).getTextName().setText("TARA");
        listReferenceFields.get(0).getTextValue().setText(ApplicationManager.getInstance().getTareAsStringWithUnit());

        listReferenceFields.get(1).getTextName().setText("SUM");
        listReferenceFields.get(1).getTextValue().setText(ApplicationManager.getInstance().getSumAsStringWithUnit());

        listReferenceFields.get(2).getTextName().setText("LOAD [%]");
        listReferenceFields.get(2).getTextValue().setText(ApplicationManager.getInstance().getLoadInPercent() + " %");

        listReferenceFields.get(3).getTextName().setText("LOAD [g]");
        listReferenceFields.get(3).getTextValue().setText(ApplicationManager.getInstance().getLoadInGramAsStringWithUnit());

        switch (Scale.getInstance().getScaleApplication()){
            case WEIGHING:

                break;
            case PART_COUNTING:
                listReferenceFields.get(4).getTextName().setText("PIECE WEIGHT");
                listReferenceFields.get(4).getTextValue().setText(ApplicationManager.getInstance().getAveragePieceWeightAsStringInGram() + " g");


                break;
        }

    }
}
