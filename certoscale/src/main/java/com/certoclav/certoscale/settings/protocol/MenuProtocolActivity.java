package com.certoclav.certoscale.settings.protocol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ProtocolAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.DatabaseListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.service.SyncProtocolsService;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.certoclav.certoscale.R.string.protocols;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuProtocolActivity extends Activity implements ButtonEventListener, DatabaseListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private ListView listView = null;
    private ProtocolAdapter adapter = null;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_recipe_activity);

        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(protocols).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_recipe_list);
        DatabaseService db = new DatabaseService(this);
        adapter = new ProtocolAdapter(this, new ArrayList<Protocol>());
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                navigationbar.getTextTitle().setText(getString(R.string.protocols).toUpperCase() + ":  " + adapter.getCount());
                navigationbar.getTextTitle().setVisibility(View.VISIBLE);
            }
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationManager.getInstance().setCurrentProtocol(adapter.getItem(position));
                Scale.getInstance().setScaleApplication(ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED);
                try {
                    if (getIntent().getBooleanExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK, false)) {
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }


    @Override
    protected void onResume() {
        super.onResume();
        DatabaseService db = new DatabaseService(this);
        adapter.clear();
        List<Protocol> protocols = db.getProtocols();
        Scale.getInstance().setOnDatabaseListener(this);
        List<Protocol> protocolList = db.getProtocols();
        Collections.sort(protocolList, new Comparator<Protocol>() {
            public int compare(Protocol emp1, Protocol emp2) {
                return emp2.getDate().compareTo(emp1.getDate()); // To compare string values
            }
        });
        if (protocols != null) {
            for (Protocol protocol : protocolList) {

                try {
                    if (getIntent().getBooleanExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK, false) == true) {
                        if (protocol.getIsPending() == true) {
                            adapter.add(protocol);
                        }
                    } else {
                        adapter.add(protocol);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        adapter.notifyDataSetChanged();

        Intent intent = new Intent(ApplicationController.getContext(), SyncProtocolsService.class);

        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Scale.getInstance().removeOnDatabaseListener(this);
    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {


        switch (buttonId) {
            case ActionButtonbarFragment.BUTTON_DELETE:
                try {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(getString(R.string.delete_all));
                    ((TextView) dialog.findViewById(R.id.text)).setText(R.string.do_you_really_want_to_delete_all_protocols);
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
                            List<Protocol> protocolList = db.getProtocols();

                                /*for(int i=0; i<protocolList.size();i++) {
                                    db.deleteProtocol(protocolList.get(i));
                                }*/

                            for (Protocol protocol : protocolList) {
                                db.deleteProtocol(protocol);

                            }

                            dialog.dismiss();
                            onResume();


                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onDatabaseChanged() {
        Log.e("MenuProtocolActivity", "onDatabaseChanged()");
        DatabaseService db = new DatabaseService(this);
        List<Protocol> protocols = db.getProtocols();


        Collections.sort(protocols, new Comparator<Protocol>() {
            public int compare(Protocol emp1, Protocol emp2) {
                return emp2.getDate().compareTo(emp1.getDate()); // To compare string values

            }
        });
        adapter.clear();
        for (Protocol protocol : protocols) {
            adapter.add(protocol);
        }
        adapter.notifyDataSetChanged();

    }
}


