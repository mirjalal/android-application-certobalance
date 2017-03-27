package com.certoclav.certoscale.settings.communication;

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
import com.certoclav.certoscale.settings.item.MenuItemEditActivity;
import com.certoclav.library.application.ApplicationController;

import java.util.Date;
import java.util.List;

/**
 * Created by Enrico on 17.03.2017.
 */

public class SettingsCommunicationEditActivity extends Activity implements ButtonEventListener{

    private Navigationbar navigationbar = new Navigationbar(this);


    public static final String INTENT_EXTRA_ITEM_ID = "item_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_communication_edit_acitivity);
        navigationbar.onCreate();
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getTextTitle().setText(R.string.communication_config);
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonSave().setVisibility(View.VISIBLE);

        navigationbar.getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    final Dialog dialog = new Dialog(SettingsCommunicationEditActivity.this);
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

        }else{
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
                text.setText(getString(R.string.do_you_really_want_to_save_the_itm) );
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
