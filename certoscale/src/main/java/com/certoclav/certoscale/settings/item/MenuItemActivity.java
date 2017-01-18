package com.certoclav.certoscale.settings.item;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.supervisor.ApplicationManager;

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
        navigationbar.getTextTitle().setText("ITEMS");
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_item_edit_list);



        adapter = new ItemAdapter(this,new ArrayList<Item>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationManager.getInstance().setCurrentItem(adapter.getItem(position));
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setOnClickButtonListener(this);
        navigationbar.setButtonEventListener(this);
        adapter.clear();
        DatabaseService db = new DatabaseService(this);
        List<Item> items = db.getItems();
        if(items != null){
            for(Item item : items){
                adapter.add(item);
            }
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
