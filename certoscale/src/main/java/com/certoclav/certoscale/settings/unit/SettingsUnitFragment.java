package com.certoclav.certoscale.settings.unit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.UnitAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SettingsUnitFragment extends Fragment implements UnitAdapter.OnClickButtonListener,ButtonEventListener{


    private ListView list = null;
    private UnitAdapter adapter = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("SettingsUnitFragment", "onCreate");
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_unit,container, false);
        list = (ListView) rootView.findViewById(R.id.application_settings_unit_list);
        adapter = new UnitAdapter(getActivity(),new ArrayList<Unit>());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItem(position).getEnabled()) {
                    ApplicationManager.getInstance().setCurrentUnit(adapter.getItem(position));
                    if(getActivity().getIntent().hasExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK)) {
                        getActivity().finish();
                    }
                }
            }
        });

        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {

        adapter.setOnClickButtonListener(this);

        Log.e("SettingsUnitFragment", "onResume");
        super.onResume();
        boolean checkboxVisible = true;
        try{
            checkboxVisible = getActivity().getIntent().getBooleanExtra(AppConstants.INTENT_EXTRA_CHECKBOX_VISIBLE,true);

        }catch (Exception e){
            checkboxVisible = true;
        }


        DatabaseService db = new DatabaseService(getActivity());
        List<Unit> units = db.getUnits();

        adapter.clear();
        if (units != null){
            Collections.sort(units, new Comparator<Unit>() {
                @Override
                public int compare(Unit unit1, Unit unit2) {
                    return unit1.getName().compareToIgnoreCase(unit2.getName());
                }
            });
            for (Unit unit : units) {
                if(checkboxVisible == false){
                    if(unit.getEnabled()){
                        adapter.add(unit);
                        adapter.setHideCheckbox(true);
                    }
                }else{
                    adapter.add(unit);
                    adapter.setHideCheckbox(false);
                }
                Log.e("SettingsUnitFragment", "added unit");
            }
         }
        adapter.notifyDataSetChanged();
        Log.e("SettingsUnitFragment", "notifydatasetchanged" + adapter.getCount());

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onClickButtonDelete(final Unit unit) {


        try{
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.setTitle("Confirm deletion");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(getString(R.string.do_you_really_want_to_delete) + " " + unit.getName());
            Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseService db = new DatabaseService(getContext());
                    db.deleteUnit(unit);
                    adapter.remove(unit);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            dialog.show();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


}

    @Override
    public void onClickButtonEdit(Unit unit) {
        Intent intent = new Intent(getActivity(), SettingsUnitEditActivity.class);
        intent.putExtra(SettingsUnitEditActivity.INTENT_EXTRA_UNIT_ID, unit.getUnit_id());
        startActivity(intent);


    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {

    }
}
