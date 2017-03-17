package com.certoclav.certoscale.settings.item;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.library.application.ApplicationController;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuItemEditActivity extends Activity implements ButtonEventListener{

    private Navigationbar navigationbar = new Navigationbar(this);
    private Item itemFromDb = null;
    private EditText editName = null;
    private EditText editCost = null;
    private EditText editArticleNumber = null;
    private EditText editWeight = null;
    private EditText editDescription = null;
    private EditText editUnit = null;

    public static final String INTENT_EXTRA_ITEM_ID = "item_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_item_edit_activity);
        navigationbar.onCreate();
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(R.string.edit_items).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonSave().setVisibility(View.VISIBLE);
        editName = (EditText) findViewById(R.id.menu_main_item_edit_name);
        editCost = (EditText) findViewById(R.id.menu_main_item_edit_cost);
        editArticleNumber = (EditText) findViewById(R.id.menu_main_item_edit_artnumber);
        editWeight = (EditText) findViewById(R.id.menu_main_item_edit_weight);
        editDescription = (EditText) findViewById(R.id.menu_main_item_edit_description);
        editUnit = (EditText) findViewById(R.id.menu_main_item_edit_unit);

        navigationbar.getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    final Dialog dialog = new Dialog(MenuItemEditActivity.this);
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(R.string.cancel_without_saving);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(R.string.do_you_really_want_to_go_back);
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
        });


        int extra = 0;
        try {
            extra = getIntent().getIntExtra(INTENT_EXTRA_ITEM_ID, 0);
        }catch (Exception e){
            extra = 0;
        }
        if(extra != 0) {
            DatabaseService db = new DatabaseService(this);
            itemFromDb = db.getItemById(extra);
            editWeight.setText(itemFromDb.getWeight().toString());
            editCost.setText(itemFromDb.getCost().toString());
            editArticleNumber.setText(itemFromDb.getArticleNumber());
            editName.setText(itemFromDb.getName());
            editUnit.setText(itemFromDb.getUnit());
            editDescription.setText(itemFromDb.getDescription());
        }else{
            editWeight.setText("");
            editCost.setText("");
            editArticleNumber.setText("");
            editName.setText("");
            editDescription.setText("");
            editUnit.setText("");
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
                dialog.setTitle(R.string.confirm_operation);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(getString(R.string.do_you_really_want_to_save_the_itm) + editName.getText().toString());
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

                        boolean unitParsed=false;
                        List<Unit> units = db.getUnits();
                        for (Unit unit : units) {
                            if(editUnit.getText().toString().equals(unit.getName())){
                                unitParsed=true;
                            }

                        }

                        if (unitParsed==true) {
                            Date date = new Date();
                            db.insertItem(new Item(editName.getText().toString(),
                                            Double.parseDouble(editWeight.getText().toString()),
                                            Double.parseDouble(editCost.getText().toString()),
                                            editArticleNumber.getText().toString(),
                                            ((Long) date.getTime()).toString(),
                                            editDescription.getText().toString(),
                                            Scale.getInstance().getSafetyKey(),
                                            editUnit.getText().toString(),
                                            "",
                                            "private"
                                    )

                            );
                            dialog.dismiss();
                            finish();
                        }else{
                            Toast.makeText(ApplicationController.getContext(), "Item Unit is not valid" , Toast.LENGTH_SHORT).show();
                        }



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
