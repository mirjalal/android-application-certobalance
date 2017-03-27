package com.certoclav.certoscale.settings.recipe;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.RecipeElementAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuRecipeEditActivity extends Activity implements RecipeElementAdapter.OnClickButtonListener, ButtonEventListener{

    private Navigationbar navigationbar = new Navigationbar(this);
    private Recipe recipe = null;
    private RecipeElementAdapter adapter = null;
    private ListView listView = null;
    private TextView textRecipeName = null;
    public static final String INTENT_EXTRA_RECIPE_ID = "recipe_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_recipe_edit_activity);

        navigationbar.onCreate();
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getButtonSave().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(R.string.edit_recipe).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    final Dialog dialog = new Dialog(MenuRecipeEditActivity.this);
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(R.string.cancel_without_saving);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(R.string.do_you_really_want_to_go_back_without_saving_recipe);
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
        listView = (ListView) findViewById(R.id.menu_main_recipe_edit_list);
        textRecipeName = (TextView) findViewById(R.id.menu_main_recipe_edit_text);


        adapter = new RecipeElementAdapter(this,new ArrayList<RecipeEntry>());
        listView.setAdapter(adapter);
       try {
           int recipeId = getIntent().getExtras().getInt(INTENT_EXTRA_RECIPE_ID);
           DatabaseService db = new DatabaseService(this);
           recipe = db.getRecipeById(recipeId);
       }catch (Exception e){
            ArrayList<RecipeEntry> recipeEntries = new ArrayList<RecipeEntry>();
            recipeEntries.add(new RecipeEntry("H2O",100d,1,"1300131","g","Please tare the balance and place 100g H2O onto the pan and press NEXT",0d));
            recipeEntries.add(new RecipeEntry("NaCl",0.9,2,"4827383", "g", "Please add 0,9g NaCl and press NEXT",0d));
           Date date = new Date();
           recipe = new Recipe("","My recipe", recipeEntries,((Long)date.getTime()).toString(), Scale.getInstance().getSafetyKey(),"private",Scale.getInstance().getUser().getEmail());
       }
        textRecipeName.setText(recipe.getRecipeName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setOnClickButtonListener(this);
        navigationbar.setButtonEventListener(this);
        adapter.clear();
        if(recipe != null){
            for(RecipeEntry entry : recipe.getRecipeEntries()){
                adapter.add(entry);
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
    public void onClickButtonDelete(final RecipeEntry recipeEntry) {
        try
        {



            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.setTitle(getString(R.string.confirm_deletion));

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(getString(R.string.do_you_really_want_to_delete) + " " + recipeEntry.getDescription());
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
                    adapter.remove(recipeEntry);
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
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {

        if(buttonId == ActionButtonbarFragment.BUTTON_ADD){
            adapter.add(new RecipeEntry(" ",0d,0," ","g"," ",0d));
            adapter.notifyDataSetChanged();
        }
        if(buttonId == ActionButtonbarFragment.BUTTON_SAVE){
            try
            {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle(R.string.confirm_operation);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(R.string.do_you_really_want_to_save_the_recipe);
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
                        DatabaseService db = new DatabaseService(MenuRecipeEditActivity.this);

                        if(recipe != null){
                            db.deleteRecipe(recipe);
                        }
                        List<RecipeEntry> recipeEntries = new ArrayList<RecipeEntry>();
                        for(int i = 0; i< adapter.getCount();i++){
                            RecipeEntry recipeEntry=adapter.getItem(i);
                           recipeEntry.setInstruction(getString(R.string.pleas_put)+" "+recipeEntry.getWeight()+getString(R.string.g_of)+recipeEntry.getDescription()+" "+getString(R.string.on_the_pan_and_press_next));
                            recipeEntries.add(recipeEntry);
                        }
                        Date date = new Date();
                        recipe = new Recipe("",textRecipeName.getText().toString(),recipeEntries,((Long)date.getTime()).toString(),Scale.getInstance().getSafetyKey(),"private",Scale.getInstance().getUser().getEmail());
                        db.insertRecipe(recipe);
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


/*
        if(buttonId == ActionButtonbarFragment.BUTTON_BACK){
            try
            {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Return");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you really want to return without saving");
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
                        DatabaseService db = new DatabaseService(MenuRecipeEditActivity.this);


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
*/


    }
}
