package com.certoclav.certoscale.settings.item;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemEditAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;

import java.util.ArrayList;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuItemEditActivity extends Activity implements ButtonEventListener{

    private Navigationbar navigationbar = new Navigationbar(this);
    private ItemEditAdapter adapter = null;
    private ListView listView = null;
    private Item itemFromDb = null;

    public static final String INTENT_EXTRA_ITEM_ID = "item_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_item_edit_activity);
        navigationbar.onCreate();
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText("EDIT ITEMS");
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonSave().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_item_edit_list);



        adapter = new ItemEditAdapter(this,new ArrayList<Item>());
        listView.setAdapter(adapter);

        int extra = 0;
        try {
            extra = getIntent().getIntExtra(INTENT_EXTRA_ITEM_ID, 0);
        }catch (Exception e){
            extra = 0;
        }
        if(extra != 0) {
            DatabaseService db = new DatabaseService(this);
            itemFromDb = db.getItemById(extra);
            adapter.add(itemFromDb);
        }else{
            adapter.add(new Item("","name",1.0d, "Item ID"));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationbar.setButtonEventListener(this);



    }

    @Override
    protected void onPause() {

        navigationbar.removeNavigationbarListener(this);
        super.onPause();

    }








    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {

        if(buttonId == Navigationbar.BUTTON_SAVE){
            try
            {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Confirm operation");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you really want to save the item? " + adapter.getItem(0).getName());
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
                        DatabaseService db = new DatabaseService(MenuItemEditActivity.this);
                        if(itemFromDb != null){
                            db.deleteItem(itemFromDb);
                        }
                        for(int i = 0; i< adapter.getCount();i++){
                            Item item = adapter.getItem(i);
                            Log.e("MenuItemEditActivity", "item json: "+ item.getItemJson());
                        }

                        if(adapter.getCount()>0){
                            Item item = adapter.getItem(0);
                            Log.e("MenuItemEditActivty", "insert item: "+ item.getItemArticleNumber() + item.getName() + item.getWeight());
                                db.insertItem(item);
                        }

                        dialog.dismiss();
                        finish();
                    }
                });

                dialog.show();


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
