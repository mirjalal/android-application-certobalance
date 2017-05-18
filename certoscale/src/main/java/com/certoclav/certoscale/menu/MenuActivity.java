package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.certoclav.library.application.ApplicationController;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.teamviewer.sdk.screensharing.api.TVConfigurationID;
import com.teamviewer.sdk.screensharing.api.TVCreationError;
import com.teamviewer.sdk.screensharing.api.TVSession;
import com.teamviewer.sdk.screensharing.api.TVSessionConfiguration;
import com.teamviewer.sdk.screensharing.api.TVSessionCreationCallback;
import com.teamviewer.sdk.screensharing.api.TVSessionFactory;

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






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_activity);

        //Set up navigation bar
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(R.string.main_menu).toUpperCase());
        navigationbar.getButtonLogout().setVisibility(View.VISIBLE);
        navigationbar.getButtonCompanyLogo().setVisibility(View.GONE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setImageResource(R.drawable.ic_menu_help);


        //Set up menu items
        gridView = (GridView) findViewById(R.id.menu_main_grid);
        menuMainElementAdapter = new MenuElementAdapter(this,new ArrayList<MenuElement>());
        gridView.setAdapter(menuMainElementAdapter);
        menuMainElementAdapter.add(new MenuElement(getString(R.string.applications).toUpperCase(),R.drawable.ic_menu_weighing_orange, MenuElement.MenuItemId.MENU_ITEM_APPLICATIONS));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.calibration).toUpperCase(),R.drawable.ic_menu_calibration_orange, MenuElement.MenuItemId.MENU_ITEM_CALIBRATION));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.library).toUpperCase(),R.drawable.ic_menu_settings_bar_2, MenuElement.MenuItemId.MENU_ITEM_LIBRARY));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.recipes).toUpperCase(),R.drawable.ic_menu_document_text, MenuElement.MenuItemId.MENU_ITEM_RECIPES));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.items).toUpperCase(),R.drawable.ic_menu_list_orange, MenuElement.MenuItemId.MENU_ITEM_ITEMS));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.protocols).toUpperCase(),R.drawable.ic_menu_protocol_2,MenuElement.MenuItemId.MENU_ITEM_PROTOCOLS));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.device_settings).toUpperCase(),R.drawable.ic_menu_settings, MenuElement.MenuItemId.MENU_ITEM_DEVICE));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.factory_reset).toUpperCase(),R.drawable.ic_menu_settings_reset, MenuElement.MenuItemId.MENU_ITEM_RESET));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.lockout_settings).toUpperCase(),R.drawable.ic_menu_lock2, MenuElement.MenuItemId.MENU_ITEM_LOCKOUT));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.application_settings).toUpperCase(),R.drawable.ic_menu_app_settings_applications_orange, MenuElement.MenuItemId.MENU_ITEM_APPLICATION_SETTINGS));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.user_management).toUpperCase(),R.drawable.ic_menu_settings_user2, MenuElement.MenuItemId.MENU_ITEM_USER));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.glp_settings).toUpperCase(),R.drawable.ic_menu_settings_glp_2, MenuElement.MenuItemId.MENU_ITEM_GLP));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.unit_settings).toUpperCase(),R.drawable.ic_menu_unit_2, MenuElement.MenuItemId.MENU_ITEM_WEIGHING_UNITS));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.communicaton_settings).toUpperCase(),R.drawable.ic_menu_settings_communication, MenuElement.MenuItemId.MENU_ITEM_COMMUNICATION));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.videos).toUpperCase(), R.drawable.ic_menu_video_orange, MenuElement.MenuItemId.MENU_ITEM_VIDEO));
        menuMainElementAdapter.add(new MenuElement(getString(R.string.labels).toUpperCase(),R.drawable.ic_menu_label, MenuElement.MenuItemId.MENU_ITEM_LABELS));

        ApplicationManager.getInstance();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onResume() {

        String key = "preferences_communication_list_devices";
        String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
        switch (modelValue) {
            case "1":
                navigationbar.getButtonCompanyLogo().setImageResource(R.drawable.logo_gandg);
                break;

            case "2":
                navigationbar.getButtonCompanyLogo().setImageResource(R.drawable.logo_kern);
                break;

            case "3":
                navigationbar.getButtonCompanyLogo().setImageResource(R.drawable.logo_ae_adam_small);
                break;
        }
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
                    dialog.setTitle(R.string.support);

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
                            Toast.makeText(MenuActivity.this, R.string.contact_supportcertoclav_com_to_enable_remote_service, Toast.LENGTH_LONG).show();
                            String appToken = "23888b86-8e66-e941-79f0-1d9ee0b8651c";
                            final TVSessionConfiguration config =
                                    new TVSessionConfiguration.Builder(
                                            new TVConfigurationID("pff75tf"))
                                            .setServiceCaseName("NAME_FOR_SERVICE_CASE")
                                            .setServiceCaseDescription("DESCRIPTION_FOR_SERVICE_CASE")
                                            .build();

                    TVSessionFactory.createTVSession(MenuActivity.this, appToken,
                            new TVSessionCreationCallback() {
                                @Override
                                public void onTVSessionCreationSuccess(TVSession session) {
                                    session.start(config);
                                }

                                @Override
                                public void onTVSessionCreationFailed(TVCreationError error) {
                                }
                            });


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
                    dialog.setTitle(R.string.logout);

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
