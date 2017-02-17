package com.certoclav.certoscale.settings.unit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.UnitAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SettingsUnitFragment extends Fragment {


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
                    getActivity().finish();
                }
            }
        });

        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        Log.e("SettingsUnitFragment", "onResume");
        super.onResume();
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
                adapter.add(unit);
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




}
