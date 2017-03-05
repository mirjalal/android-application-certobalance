package com.certoclav.certoscale.settings.item;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.certocloud.GetUtil;
import com.certoclav.library.certocloud.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuItemActivity extends Activity implements ItemAdapter.OnClickButtonListener, ButtonEventListener{

    private Navigationbar navigationbar = new Navigationbar(this);
    private Item item = null;
    private ItemAdapter adapter = null;
    private ListView listView = null;

    public static final String INTENT_EXTRA_RECIPE_ID = "item_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_item_edit_activity);
        navigationbar.onCreate();
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(R.string.items_capitalized);
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_item_edit_list);



        adapter = new ItemAdapter(this,new ArrayList<Item>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationManager.getInstance().setCurrentItem(adapter.getItem(position));
                if(getIntent().getBooleanExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK,false)==true) {
                    finish();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setOnClickButtonListener(this);
        navigationbar.setButtonEventListener(this);
        adapter.clear();
        new AsyncTask<Boolean, Boolean, Boolean >(){

            @Override
            protected Boolean  doInBackground(Boolean... params) {
                ArrayList<String> itemsList = new ArrayList<String>();
                Items items = new Items();
                Integer retval = items.getItemsFromCloud();
                if(retval == GetUtil.RETURN_OK){
                    if(items.getItemJsonStringArray() != null){
                        DatabaseService db = new DatabaseService(ApplicationController.getContext());
                        List<Item> itemsFromDb = db.getItems();
                        List<Item> itemsFromCloud = new ArrayList<Item>();

                        for(String itemJsonString : items.getItemJsonStringArray()){
                            itemsFromCloud.add(new Item(itemJsonString));
                        }
                        for(Item cloudItem : itemsFromCloud){
                            boolean cloudItemAlreadyInDb = false;
                            for(Item dbitem : itemsFromDb){
                                if(cloudItem.getCloudId().equals(dbitem.getCloudId())){
                                    cloudItemAlreadyInDb = true;
                                    continue;
                                }
                            }
                            if(cloudItemAlreadyInDb == false){
                                db.insertItem(cloudItem);
                            }
                        }

                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                Toast.makeText(ApplicationController.getContext(), "Items updated", Toast.LENGTH_LONG);
                DatabaseService db = new DatabaseService(ApplicationController.getContext());
                List<Item> items = db.getItems();
                adapter.clear();
                if(items != null){
                    for(Item item : items){
                        adapter.add(item);
                    }
                }
                adapter.notifyDataSetChanged();

                super.onPostExecute(b);
            }
        }.execute();
        DatabaseService db = new DatabaseService(this);
        List<Item> items = db.getItems();
        if(items != null){
            for(Item item : items){
                adapter.add(item);
            }
        }
        if(getIntent().hasExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK)){
            adapter.setHideActionButtons(true);
        }else{
            adapter.setHideActionButtons(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        adapter.removeOnClickButtonListener(this);
        navigationbar.removeNavigationbarListener(this);
        super.onPause();

    }




    @Override
    public void onClickButtonDelete(final Item item) {
        try
        {



            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.setTitle("Confirm deletion");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(getString(R.string.do_you_really_want_to_delete) + " " + item.getName());
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
                    DatabaseService db = new DatabaseService(MenuItemActivity.this);
                    db.deleteItem(item);
                    adapter.remove(item);
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
    public void onClickButtonEdit(Item item) {
        Intent intent = new Intent(MenuItemActivity.this, MenuItemEditActivity.class);
        intent.putExtra(MenuItemEditActivity.INTENT_EXTRA_ITEM_ID, item.getItem_id());
        startActivity(intent);
    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
        if(buttonId == ActionButtonbarFragment.BUTTON_ADD){
            Intent intent = new Intent(MenuItemActivity.this, MenuItemEditActivity.class);
            startActivity(intent);

        }
    }
}
