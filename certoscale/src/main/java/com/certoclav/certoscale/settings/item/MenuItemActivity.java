package com.certoclav.certoscale.settings.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.DatabaseListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.service.SyncItemsService;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;

import java.util.ArrayList;
import java.util.List;

import static com.certoclav.certoscale.R.string.items;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuItemActivity extends Activity implements ItemAdapter.OnClickButtonListener, ButtonEventListener,DatabaseListener{

    private Navigationbar navigationbar = new Navigationbar(this);
    private ItemAdapter adapter = null;
    private ListView listView = null;

    public static final String INTENT_EXTRA_RECIPE_ID = "item_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_item_activity);
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(items).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_item_edit_list);



        adapter = new ItemAdapter(this,new ArrayList<Item>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationManager.getInstance().setCurrentItem(adapter.getItem(position));
                try {
                    if (getIntent().getBooleanExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK, false) == true) {
                        finish();
                    }
                }catch (Exception e){

                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        //navigationbar.setButtonEventListener(this);
        adapter.clear();
        Scale.getInstance().setOnDatabaseListener(this);

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
        adapter.setOnClickButtonListener(this);

        Intent intent3 = new Intent(ApplicationController.getContext(), SyncItemsService.class);
        startService(intent3);
       // updateTitleText();

    }

    @Override
    protected void onPause() {
        adapter.removeOnClickButtonListener(this);
        //navigationbar.removeNavigationbarListener(this);
        Scale.getInstance().removeOnDatabaseListener(this);
        super.onPause();

    }




    @Override
    public void onClickButtonDelete(final Item item) {
        //updateTitleText();


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

    @Override
    public void onDatabaseChanged() {
        Log.e("MenuItemActivity","onDatabaseChanged()");
        DatabaseService db = new DatabaseService(this);
        List<Item> items = db.getItems();
        adapter.clear();
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

        navigationbar.getTextTitle().setText(getString(R.string.items).toUpperCase()+":  "+adapter.getCount());
        //updateTitleText();


    }

    /*
    public void updateTitleText(){
        navigationbar.getTextTitle().setText(getString(items).toUpperCase()+":  "+adapter.getCount());
    }*/
}
