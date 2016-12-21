package com.certoclav.certoscale.settings.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.UserAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.menu.RegisterActivity;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuUserActivity extends Activity implements ButtonEventListener, UserAdapter.OnClickButtonListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private ArrayAdapter<String> arrayAdapter = null;
    private ListView listView = null;
    private UserAdapter adapter = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_user_activity);
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getButtonSettings().setVisibility(View.GONE);
        navigationbar.getButtonHome().setVisibility(View.GONE);
        navigationbar.getSpinnerLib().setVisibility(View.GONE);
        navigationbar.getSpinnerMode().setVisibility(View.GONE);
        navigationbar.getTextTitle().setText("USER SETTINGS");
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_user_list);


        DatabaseService db = new DatabaseService(this);
        List<User> usersVisible = db.getUsers();
        if(usersVisible != null){
            adapter = new UserAdapter(this,usersVisible);
            listView.setAdapter(adapter);
        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseService db = new DatabaseService(this);
        List<User> usersVisible = db.getUsers();
        adapter.clear();
        for(User user : usersVisible){
            adapter.add(user);
        }
        adapter.notifyDataSetChanged();

        adapter.setOnClickButtonListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.removeOnClickButtonListener(this);
    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
        switch (buttonId){
            case Navigationbar.BUTTON_ADD:
                Intent intent = new Intent(MenuUserActivity.this,RegisterActivity.class);
                startActivity(intent);
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




    @Override
    public void onClickButtonDelete(final User user) {
        if(user.getIsAdmin()){
            Toast.makeText(MenuUserActivity.this, "Admin account can not be deleted", Toast.LENGTH_LONG).show();
            return;
        }
        if(Scale.getInstance().getState() == ScaleState.OFF){
            try
            {



                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle(getString(R.string.delete_user));

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(getString(R.string.do_you_really_want_to_delete) + " " + user.getEmail());
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
                        DatabaseService db = new DatabaseService(MenuUserActivity.this);
                        db.deleteUser(user);
                        adapter.remove(user);
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

        }else{
            Toast.makeText(MenuUserActivity.this, getString(R.string.please_log_out_first), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClickButtonEdit(User user) {
        Intent intent = new Intent(MenuUserActivity.this,RegisterActivity.class);
        intent.putExtra(AppConstants.INTENT_EXTRA_USER_ID, user.getUserId());
        startActivity(intent);
    }


}
