package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.MenuElementAdapter;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.MenuElement;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuActivity extends Activity implements ButtonEventListener {


    private Navigationbar navigationbar = new Navigationbar(this);
    private MenuElementAdapter menuMainElementAdapter = null;
    private GridView gridView = null;
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

        //Set up navigation bar
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText("Main menu".toUpperCase());
        navigationbar.getButtonLogout().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setImageResource(R.drawable.ic_menu_help);

        //Set up menu items
        gridView = (GridView) findViewById(R.id.menu_main_grid);
        menuMainElementAdapter = new MenuElementAdapter(this,new ArrayList<MenuElement>());
        gridView.setAdapter(menuMainElementAdapter);
        menuMainElementAdapter.add(new MenuElement("Applications".toUpperCase(),R.drawable.ic_menu_weighing, MenuElement.MenuItemId.MENU_ITEM_APPLICATIONS));
        menuMainElementAdapter.add(new MenuElement("Calibration".toUpperCase(),R.drawable.ic_menu_calibration, MenuElement.MenuItemId.MENU_ITEM_CALIBRATION));
        menuMainElementAdapter.add(new MenuElement("Library".toUpperCase(),R.drawable.ic_menu_library, MenuElement.MenuItemId.MENU_ITEM_LIBRARY));
        menuMainElementAdapter.add(new MenuElement("Recipes".toUpperCase(),R.drawable.ic_menu_library, MenuElement.MenuItemId.MENU_ITEM_RECIPES));
        menuMainElementAdapter.add(new MenuElement("Items".toUpperCase(),R.drawable.ic_menu_library, MenuElement.MenuItemId.MENU_ITEM_ITEMS));
        menuMainElementAdapter.add(new MenuElement("Device settings".toUpperCase(),R.drawable.ic_menu_settings, MenuElement.MenuItemId.MENU_ITEM_DEVICE));
        menuMainElementAdapter.add(new MenuElement("Application settings".toUpperCase(),R.drawable.ic_menu_app_settings, MenuElement.MenuItemId.MENU_ITEM_APPLICATION_SETTINGS));
        menuMainElementAdapter.add(new MenuElement("User management".toUpperCase(),R.drawable.ic_menu_settings_user, MenuElement.MenuItemId.MENU_ITEM_USER));
        menuMainElementAdapter.add(new MenuElement("Glp settings".toUpperCase(),R.drawable.ic_menu_settings_glp, MenuElement.MenuItemId.MENU_ITEM_GLP));
        menuMainElementAdapter.add(new MenuElement("Unit settings".toUpperCase(),R.drawable.ic_menu_unit, MenuElement.MenuItemId.MENU_ITEM_WEIGHING_UNITS));
        menuMainElementAdapter.add(new MenuElement("Communication settings".toUpperCase(),R.drawable.ic_menu_settings, MenuElement.MenuItemId.MENU_ITEM_COMMUNICATION));

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
            case ActionButtonbarFragment.BUTTON_ADD:
                try {

                    final Dialog dialog = new Dialog(MenuActivity.this);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.dialog_support);
                    dialog.setTitle("Support");

                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_support_button_speech);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MenuActivity.this, SpeechToTextActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    Button dialogButtonMail = (Button) dialog.findViewById(R.id.dialog_support_button_mail);
                    // if button is clicked, close the custom dialog
                    dialogButtonMail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MenuActivity.this, SettingsEmailActivity.class);
                            startActivity(intent);
                            dialog.dismiss();


                        }
                    });
                    Button dialogButtonTeamViewer = (Button) dialog.findViewById(R.id.dialog_support_button_teamviwer);
                    // if button is clicked, close the custom dialog
                    dialogButtonTeamViewer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MenuActivity.this, "Contact support@certoclav.com to enable remote service", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }catch(Exception e){

                }


                break;
            case ActionButtonbarFragment.BUTTON_LOGOUT:
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
                            Scale.getInstance().setScaleState(ScaleState.ON_AND_MODE_GRAM);
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
