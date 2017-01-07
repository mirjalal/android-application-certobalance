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
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.RecipeEntry;

import java.util.ArrayList;

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
        navigationbar.getTextTitle().setText("EDIT RECIPE");
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.menu_main_recipe_edit_list);
        textRecipeName = (TextView) findViewById(R.id.menu_main_recipe_edit_text);


        adapter = new RecipeElementAdapter(this,new ArrayList<RecipeEntry>());
        listView.setAdapter(adapter);
       try {
           int recipeId = getIntent().getExtras().getInt(INTENT_EXTRA_RECIPE_ID);
           DatabaseService db = new DatabaseService(this);
           recipe = db.getRecipeById(recipeId);
       }catch (Exception e){
            recipe = new Recipe("","New recipe", new ArrayList<RecipeEntry>());
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
            dialog.setTitle("Confirm deletion");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(getString(R.string.do_you_really_want_to_delete) + " " + recipeEntry.getName());
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
        if(buttonId == Navigationbar.BUTTON_SAVE){
            try
            {



                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Confirm deletion");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you really want to save the recipe?");
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
    }
}
