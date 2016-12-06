package com.certoclav.certoscale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.certoclav.certoscale.listener.SensorDataListener;
import com.certoclav.certoscale.model.ReferenceField;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.ItemListFragment.Callbacks;

import java.util.ArrayList;
import java.util.List;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentTable extends Fragment implements SensorDataListener {


    private float tara = 0;
    private TextView textTara = null;
    private List<ReferenceField> listReferenceFields = new ArrayList<ReferenceField>();

    @Override
    public void onResume() {
        super.onResume();
        Scale.getInstance().setOnSensorDataListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Scale.getInstance().removeOnSensorDataListener(this);
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
    public void onSensorDataChange(Float value, String unit) {



        if(value > 100){
            //todo color bar red
        }

        //TODO: define fixed indexes instead of hardcoding index positions.
        //TODO: show only data which is enabled in settings menu

        listReferenceFields.get(0).getTextName().setText("TARA");
        listReferenceFields.get(0).getTextValue().setText((String.format("%.4f",Scale.getInstance().getTara()) + " g"));

        listReferenceFields.get(1).getTextName().setText("SUM");
        listReferenceFields.get(1).getTextValue().setText(String.format("%.4f",Scale.getInstance().getScaleValue()) + " g");

        listReferenceFields.get(2).getTextName().setText("LOAD");
        listReferenceFields.get(2).getTextValue().setText(String.format("%d", Math.round(Scale.getInstance().getScaleValue())) + " %");


    }
}
