package com.certoclav.certoscale.settings.protocol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ProtocolAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuProtocolActivity extends Activity implements ButtonEventListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private ListView listView = null;
    private ProtocolAdapter adapter = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_recipe_activity);

        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText("PROTOCOLS");
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.menu_main_recipe_list);


        DatabaseService db = new DatabaseService(this);
        adapter = new ProtocolAdapter(this,new ArrayList<Protocol>());
        listView.setAdapter(adapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseService db = new DatabaseService(this);

        adapter.clear();
        List<Protocol> protocols = db.getProtocols();
       if(protocols != null) {
           for (Protocol protocol : protocols) {
               adapter.add(protocol);
           }
       }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {

    }
}
