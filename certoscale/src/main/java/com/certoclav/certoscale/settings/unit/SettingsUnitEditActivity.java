package com.certoclav.certoscale.settings.unit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemEditAdapter;
import com.certoclav.certoscale.adapters.RecipeElementAdapter;
import com.certoclav.certoscale.adapters.UnitEditAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.settings.item.MenuItemEditActivity;
import com.certoclav.certoscale.settings.recipe.MenuRecipeEditActivity;

import java.util.ArrayList;
import java.util.List;


public class SettingsUnitEditActivity extends FragmentActivity implements ButtonEventListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private UnitEditAdapter adapter = null;
    private ListView listView = null;
    private Unit unitFromDb = null;

    public static final String INTENT_EXTRA_UNIT_ID = "unit_id";
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



        adapter = new UnitEditAdapter(this,new ArrayList<Unit>());
        listView.setAdapter(adapter);

        int extra = 0;
        try {
            extra = getIntent().getIntExtra(INTENT_EXTRA_UNIT_ID, 0);
        }catch (Exception e){
            extra = 0;
        }
        if(extra != 0) {
            DatabaseService db = new DatabaseService(this);
            unitFromDb = db.getUnitbyId(extra);
            adapter.add(unitFromDb);
        }else{
            adapter.add(new Unit("","name",1.0d,1.0d, "Item ID"));
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

        if(buttonId == ActionButtonbarFragment.BUTTON_SAVE){
            try
            {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Confirm operation");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you really want to save the custom unit? " + adapter.getItem(0).getName());
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
                        DatabaseService db = new DatabaseService(getApplicationContext());
                        if(unitFromDb != null){
                            db.deleteUnit(unitFromDb);
                        }
                        /*
                        for(int i = 0; i< adapter.getCount();i++){
                            Item item = adapter.getItem(i);
                            Log.e("MenuItemEditActivity", "item json: "+ item.getItemJson());
                        }

                        if(adapter.getCount()>0){
                            Item item = adapter.getItem(0);
                            Log.e("MenuItemEditActivty", "insert item: "+ item.getItemArticleNumber() + item.getName() + item.getWeight());
                            db.insertItem(item);
                        }
                        */

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
