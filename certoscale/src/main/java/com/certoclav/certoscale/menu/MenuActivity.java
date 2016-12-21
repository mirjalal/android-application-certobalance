package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.settings.device.SettingsDeviceActivity;
import com.certoclav.certoscale.settings.library.MenuLibraryActivity;
import com.certoclav.certoscale.settings.user.MenuUserActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuActivity extends Activity implements ButtonEventListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private ArrayAdapter<String> arrayAdapter = null;
    private ListView listView = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;




    private final int INDEX_CALIBRATION = 0;
    private final int INDEX_APPLICATIONS_USER = 1;
    private final int INDEX_DEVICE = 2;
    private final int INDEX_APPLICATIONS = 3;
    private final int INDEX_WEIGHING_UNITS  = 4;
    private final int INDEX_GLP = 5;
    private final int INDEX_LIBRARY =6;
    private final int INDEX_FACTORY_RESET = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_activity);
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonHome().setText("Logout");
        navigationbar.getSpinnerLib().setVisibility(View.GONE);
        navigationbar.getSpinnerMode().setVisibility(View.GONE);
        navigationbar.getTextTitle().setText("Main menu");
        navigationbar.getButtonBack().setVisibility(View.GONE);
        navigationbar.getButtonLogout().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setVisibility(View.GONE);
        navigationbar.getButtonGoToApplication().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_list);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item_menu, getResources().getStringArray(R.array.array_menu_main));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // On click applications, open the ApplicationActivity
                if (position == INDEX_APPLICATIONS) {
                    Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                if (position == INDEX_APPLICATIONS_USER) {
                    Intent intent = new Intent(MenuActivity.this, MenuUserActivity.class);
                    startActivity(intent);
                }
                if (position == INDEX_DEVICE) {
                    Intent intent = new Intent(MenuActivity.this, SettingsDeviceActivity.class);
                    startActivity(intent);
                }

                if (position == INDEX_LIBRARY) {
                    Intent intent = new Intent(MenuActivity.this, MenuLibraryActivity.class);
                    startActivity(intent);
                }
            }});


                ApplicationManager.getInstance();
                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
                client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {

        switch (buttonId){
            case Navigationbar.BUTTON_LOGOUT:
                try
                {

                    final Dialog dialog = new Dialog(MenuActivity.this);
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle("Logout");

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(R.string.do_you_really_want_to_logout);
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
                            dialog.dismiss();
                            Scale.getInstance().setState(ScaleState.OFF);
                            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();


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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
