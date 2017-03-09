package com.certoclav.certoscale.settings.labels;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.UserAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.menu.RegisterActivity;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.view.EditTextItem;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.List;

/**
 * Created by Michael on 12/6/2016.
 */

public class MenuLabelPrinterActivity extends Activity implements ButtonEventListener, UserAdapter.OnClickButtonListener{

    private Navigationbar navigationbar = new Navigationbar(this);
    private ArrayAdapter<String> arrayAdapter = null;
    private ListView listView = null;
    private UserAdapter adapter = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_label_editor);
        navigationbar.onCreate();
        navigationbar.setButtonEventListener(this);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.GONE);
        navigationbar.getButtonSettings().setVisibility(View.GONE);
        navigationbar.getButtonHome().setVisibility(View.GONE);
        navigationbar.getSpinnerLib().setVisibility(View.GONE);
        navigationbar.getSpinnerMode().setVisibility(View.GONE);
        navigationbar.getTextTitle().setText(getString(R.string.label_printer).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);



        ImageView Barcode = (ImageView)  findViewById(R.id.imageViewBarcode);

        com.google.zxing.Writer writer = new QRCodeWriter();

        String finaldata = Uri.encode("test", "utf-8");


        try {
            //BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE, 150, 150);
            BitMatrix bm = new Code128Writer().encode("123456789", BarcodeFormat.CODE_128, 350, 120, null);

        Bitmap barcodeBitmap = Bitmap.createBitmap(350, 120, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 350; i++) {//width
            for (int j = 0; j < 120; j++) {//height
                barcodeBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        //Bitmap barcodeBitmap = encodeAsBitmap("test", BarcodeFormat.QR_CODE, 150, 150);

        Barcode.setImageBitmap(barcodeBitmap);


        }catch (Exception e){

        }

        EditText editText1 = (EditText)  findViewById(R.id.dialog_label_text_edittext_line1);


        EditText editText2 = (EditText)  findViewById(R.id.dialog_label_text_edittext_line1);





        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ImageView Barcode = (ImageView)  findViewById(R.id.imageViewBarcode);
        final EditText editText1 = (EditText)  findViewById(R.id.dialog_label_text_edittext_line1);


        EditText editText2 = (EditText)  findViewById(R.id.dialog_label_text_edittext_line1);

        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                com.google.zxing.Writer writer = new QRCodeWriter();

                String finaldata = Uri.encode(String.valueOf(editText1.getText()), "utf-8");


                try {
                    //BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE, 150, 150);
                    BitMatrix bm = new Code128Writer().encode(finaldata, BarcodeFormat.CODE_128, 350, 120, null);

                    Bitmap barcodeBitmap = Bitmap.createBitmap(350, 120, Bitmap.Config.ARGB_8888);

                    for (int i = 0; i < 350; i++) {//width
                        for (int j = 0; j < 120; j++) {//height
                            barcodeBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
                        }
                    }

                    //Bitmap barcodeBitmap = encodeAsBitmap("test", BarcodeFormat.QR_CODE, 150, 150);

                    Barcode.setImageBitmap(barcodeBitmap);


                }catch (Exception e){

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }

        });





    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
        switch (buttonId){
            case ActionButtonbarFragment.BUTTON_ADD:
                Intent intent = new Intent(MenuLabelPrinterActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }




    @Override
    public void onClickButtonDelete(final User user) {



    }

    @Override
    public void onClickButtonEdit(User user) {

    }


}
