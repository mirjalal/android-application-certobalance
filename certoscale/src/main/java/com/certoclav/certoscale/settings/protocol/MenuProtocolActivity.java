package com.certoclav.certoscale.settings.protocol;

import android.app.Activity;
import android.app.Dialog;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ProtocolAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        navigationbar.getTextTitle().setText(getString(R.string.protocols).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonDelete().setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.menu_main_recipe_list);


        DatabaseService db = new DatabaseService(this);
        adapter = new ProtocolAdapter(this,new ArrayList<Protocol>());
        listView.setAdapter(adapter);


        /*
        List<Protocol> protocolList=db.getProtocols();
        Collections.sort(protocolList, new Comparator<Protocol>(){
            public int compare(Protocol emp1, Protocol emp2) {
                // ## Ascending order



                return emp1.getDate().compareTo(emp2.getDate()); // To compare string values

            }
        });


        Toast.makeText(getApplicationContext(), protocolList.get(1).getName(), Toast.LENGTH_LONG).show();
        adapter = new ProtocolAdapter(this, protocolList);
        listView.setAdapter(adapter);
        */

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


        List<Protocol> protocolList=db.getProtocols();
        Collections.sort(protocolList, new Comparator<Protocol>(){
            public int compare(Protocol emp1, Protocol emp2) {
                // ## Ascending order



                return emp2.getDate().compareTo(emp1.getDate()); // To compare string values

            }
        });


        ;



       if(protocols != null) {
           for (Protocol protocol : protocolList) {
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


            switch (buttonId){
                case ActionButtonbarFragment.BUTTON_DELETE:
                    try{
                        final Dialog dialog = new Dialog(this);
                        dialog.setContentView(R.layout.dialog_yes_no);
                        dialog.setTitle("Delete all");
                        ((TextView)dialog.findViewById(R.id.text)).setText(R.string.do_you_really_want_to_delete_all_protocols);
                        // set the custom dialog components - text, image and button



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
                                final DatabaseService db = new DatabaseService(ApplicationController.getContext());


                                //db.getProtocols().removeAll((Collection<Protocol>) db.getProtocols());
                                List <Protocol> protocolList=db.getProtocols();

                                /*for(int i=0; i<protocolList.size();i++) {
                                    db.deleteProtocol(protocolList.get(i));
                                }*/

                                for (Protocol protocol:protocolList){
                                    db.deleteProtocol(protocol);
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
                    break;
            }

    }
}


