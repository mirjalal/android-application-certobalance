package com.certoclav.certoscale.settings.unit;

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
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;


public class SettingsUnitEditActivity extends Activity implements ButtonEventListener {

    private Navigationbar navigationbar = new Navigationbar(this);
    private Unit unit = null;
    EditText editTextName = null;
    EditText editTextDescription = null;
    EditText editTextFactor = null;
    EditText editTextExponent = null;
    boolean overwriteExistingUnit = false;


    public static final String INTENT_EXTRA_UNIT_ID = "unit_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_unit_edit_activity);
        navigationbar.onCreate();
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText("EDIT UNIT");
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonSave().setVisibility(View.VISIBLE);
        navigationbar.getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    final Dialog dialog = new Dialog(SettingsUnitEditActivity.this);
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle("Cancel without saving");

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Do you really want to  go back without saving the current unit?");
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

        editTextName = (EditText) findViewById(R.id.menu_main_unit_edit_name);
        editTextDescription = (EditText) findViewById(R.id.menu_main_unit_edit_description);
        editTextFactor = (EditText) findViewById(R.id.menu_main_unit_edit_factor);
        editTextExponent = (EditText) findViewById(R.id.menu_main_unit_edit_text_exponent);


    }

    @Override
    protected void onResume() {


        int extra = 0;
        try {
            extra = getIntent().getIntExtra(INTENT_EXTRA_UNIT_ID, 0);
        }catch (Exception e){
            extra = 0;
        }
        if(extra != 0) {
            try {
                DatabaseService db = new DatabaseService(this);
                unit = db.getUnitbyId(extra);
                overwriteExistingUnit = true;
            }catch (Exception e){
                overwriteExistingUnit = false;
            }
        }else{
            unit = new Unit(0.0,1.0,"g","gram","",true,true);
            overwriteExistingUnit = false;
        }

        editTextName.setHint(unit.getName());
        editTextDescription.setHint(unit.getDescription());
        editTextName.setText(unit.getName());
        editTextDescription.setText(unit.getDescription());
        editTextFactor.setText(unit.getFactor().toString());
        editTextExponent.setText(unit.getExponent().toString());
        try {
            editTextFactor.setHint(unit.getFactor().toString());
        }catch (Exception e){
            editTextFactor.setHint("1.0");
        }
        try {
            editTextExponent.setHint(unit.getFactor().toString());
        }catch (Exception e){
            editTextExponent.setHint("0");
        }

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
                if(editTextName.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please enter the name of the unit", Toast.LENGTH_LONG).show();
                    return;
                }
                if(editTextDescription.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please enter a description of the unit", Toast.LENGTH_LONG).show();
                    return;
                }
                if(editTextExponent.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please enter a valid exponent", Toast.LENGTH_LONG).show();
                    return;
                }
                if(editTextFactor.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please enter a valid factor", Toast.LENGTH_LONG).show();
                    return;
                }
                unit.setDescription(editTextDescription.getText().toString());
                try {
                    unit.setFactor(Double.parseDouble(editTextFactor.getText().toString()));
                }catch (Exception e){
                    unit.setFactor(1d);
                }
                try {
                    unit.setExponent(Double.parseDouble(editTextExponent.getText().toString()));
                }catch (Exception e){
                    unit.setExponent(0d);
                }
                unit.setName(editTextName.getText().toString());

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Confirm operation");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you really want to save the unit " + unit.getDescription() + "?");

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
                        if(overwriteExistingUnit){
                            db.deleteUnit(unit);
                        }
                       int retval =  db.insertUnit(unit);
                        if(retval == 1){
                            Toast.makeText(SettingsUnitEditActivity.this,"Unit saved", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            finish();
                        }else{
                            Toast.makeText(SettingsUnitEditActivity.this,"Saving failed", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
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
