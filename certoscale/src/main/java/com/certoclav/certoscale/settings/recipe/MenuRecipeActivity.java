package com.certoclav.certoscale.settings.recipe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.RecipeAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.DatabaseListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.service.SyncRecipesService;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuRecipeActivity extends Activity implements ButtonEventListener, RecipeAdapter.OnClickButtonListener, DatabaseListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private ListView listView = null;
    private RecipeAdapter adapter = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_recipe_activity);

        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(getString(R.string.recipes).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.menu_main_recipe_list);


        DatabaseService db = new DatabaseService(this);
        adapter = new RecipeAdapter(this,new ArrayList<Recipe>());
        listView.setAdapter(adapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationManager.getInstance().setCurrentRecipe(adapter.getItem(position));
                try {
                    if (getIntent().getExtras().getBoolean(AppConstants.INTENT_EXTRA_PICK_ON_CLICK, false) == true) {
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
        adapter.clear();


        Scale.getInstance().setOnDatabaseListener(this);





        DatabaseService db = new DatabaseService(this);

        navigationbar.getTextTitle().setText(getString(R.string.recipes).toUpperCase()+" "+db.getRecipes().size());

        List<Recipe> recipes = db.getRecipes();
       if(recipes != null) {
           for (Recipe recipe : recipes) {
               adapter.add(recipe);
           }
       }
        adapter.notifyDataSetChanged();
        adapter.setOnClickButtonListener(this);

        Intent intent = new Intent(ApplicationController.getContext(), SyncRecipesService.class);

        startService(intent);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                try {
                    navigationbar.getTextTitle().setText(getString(R.string.recipes).toUpperCase() + " " + adapter.getCount());
                    super.onChanged();
                }catch (Exception e){

                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.removeOnClickButtonListener(this);
        Scale.getInstance().removeOnDatabaseListener(this);
    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
        switch (buttonId){
            case ActionButtonbarFragment.BUTTON_ADD:
                Intent intent = new Intent(MenuRecipeActivity.this, MenuRecipeEditActivity.class);
                startActivity(intent);
                break;
        }
    }



    @Override
    public void onClickButtonDelete(final Recipe recipe) {

            try
            {



                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle(R.string.confirm_deletion);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(getString(R.string.do_you_really_want_to_delete) + " " + recipe.getRecipeName());
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
                        DatabaseService db = new DatabaseService(MenuRecipeActivity.this);
                        db.deleteRecipe(recipe);
                        adapter.remove(recipe);
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
    public void onClickButtonEdit(Recipe recipe) {

        Intent intent = new Intent(MenuRecipeActivity.this, MenuRecipeEditActivity.class);
        intent.putExtra(MenuRecipeEditActivity.INTENT_EXTRA_RECIPE_ID, recipe.getRecipe_id());
        startActivity(intent);
    }


    @Override
    public void onDatabaseChanged() {
        Log.e("MenuProtocolActivity","onDatabaseChanged()");
        DatabaseService db = new DatabaseService(this);
        List<Recipe> recipes = db.getRecipes();



        adapter.clear();
        if(recipes != null){
            for(Recipe recipe : recipes){
                adapter.add(recipe);
            }
        }

        navigationbar.getTextTitle().setText(getString(R.string.recipes).toUpperCase() + ":  " + recipes.size());
        adapter.notifyDataSetChanged();
    }


}
