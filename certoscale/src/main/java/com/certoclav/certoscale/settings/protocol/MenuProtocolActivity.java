package com.certoclav.certoscale.settings.protocol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ProtocolAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.DatabaseListener;
import com.certoclav.certoscale.menu.LoginActivity;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.FTPManager;
import com.certoclav.library.application.ApplicationController;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.certoclav.certoscale.R.string.protocols;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuProtocolActivity extends Activity implements ButtonEventListener, DatabaseListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private ListView listView = null;
    private ProtocolAdapter adapter = null;
    private GoogleApiClient client;
    private FTPManager ftpManager;

    Handler handler = new Handler();

    Runnable runnableLogout = new Runnable() {
        @Override
        public void run() {
            Scale.getInstance().setScaleState(ScaleState.ON_AND_MODE_GRAM);
            Intent intent = new Intent(MenuProtocolActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    public void actionDetected() {
        if (handler != null && runnableLogout != null) {
            handler.removeCallbacks(runnableLogout);
            handler.postDelayed(runnableLogout, AppConstants.SESSION_TIMEOUT);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_recipe_activity);
        ftpManager = FTPManager.getInstance();
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(protocols).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_recipe_list);
        DatabaseService db = new DatabaseService(this);
        adapter = new ProtocolAdapter(this, new ArrayList<Protocol>(), false);
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
                adapter.getItem(position).parseJson();
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


    long lastNotifiedTime;

    @Override
    protected void onResume() {
        super.onResume();
        final DatabaseService db = new DatabaseService(this);
        adapter.clear();
        Scale.getInstance().setOnDatabaseListener(this);

        List<Protocol> protocolList = getIntent().getBooleanExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK, false) ?
                db.getPengingProtocols() :
                db.getProtocols();
        Collections.sort(protocolList, new Comparator<Protocol>() {
            public int compare(Protocol emp1, Protocol emp2) {
                return emp2.getDate().compareTo(emp1.getDate()); // To compare string values
            }
        });

        adapter.addAll(protocolList);
        adapter.notifyDataSetChanged();

//        Intent intent = new Intent(ApplicationController.getContext(), SyncProtocolsService.class);
//        startService(intent);
        ftpManager.uploadProtocols(new FTPManager.FTPListener() {
            @Override
            public void onConnection(boolean isConnected, final String message) {
                Log.d("FTP_SERVER", "connection " + isConnected + " " + (message != null ? message : ""));
                if (!isConnected) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(ApplicationController.getContext(), (message != null && !message.isEmpty())
                                    ? message : getString(R.string.can_not_connect_to_ftp), Toast.LENGTH_LONG, true).show();
                        }
                    });

                }
            }

            @Override
            public void onUploaded(Protocol protocol) {
                if (db != null)
                    db.updateProtocolCloudId(protocol, "uploaded");
                Log.d("FTP_SERVER", "updatedall");
            }

            @Override
            public void onUploading(boolean isUploaded, final String message) {
                Log.d("FTP_SERVER", "uploading " + isUploaded + " " + (message != null ? message : ""));
                if (!isUploaded && lastNotifiedTime + 10 * 1000 < Calendar.getInstance().getTimeInMillis()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(ApplicationController.getContext(), (message != null && !message.isEmpty())
                                    ? message : getString(R.string.can_not_uploaded), Toast.LENGTH_SHORT, true).show();
                        }
                    });

                    lastNotifiedTime = Calendar.getInstance().getTimeInMillis();
                }
            }
        }, db.getProtocols(), false);
        actionDetected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnableLogout);
        }
        //Scale.getInstance().removeOnDatabaseListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnableLogout);
        }
    }

    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {

        actionDetected();
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
                            actionDetected();
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DatabaseService db = new DatabaseService(ApplicationController.getContext());
                            actionDetected();

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


