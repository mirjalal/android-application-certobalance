package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.view.EditTextItem;
import com.certoclav.library.bcrypt.BCrypt;

import java.util.Date;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends Activity {


    private LinearLayout linEditTextItemContainer;
    private Button buttonRegister;

    private EditTextItem editEmailItem;
    private EditTextItem editPasswordItem;
    private EditTextItem editPasswordItemConfirm;
    private EditTextItem editMobile;
    private EditTextItem editFirstName;
    private EditTextItem editLastName;
    private Navigationbar navigationbar = null;
    private DatabaseService db;

    private boolean isEditingAdminAccount = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LoginActivity", "onCreate");
        setContentView(R.layout.login_register);

        db = new DatabaseService(RegisterActivity.this);
        navigationbar = new Navigationbar(this);
        navigationbar.onCreate();
        navigationbar.getTextTitle().setText(getString(R.string.register_new_user));
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonBack().setVisibility(View.VISIBLE);


        linEditTextItemContainer = (LinearLayout) findViewById(R.id.register_container_edit_text_items);


        editEmailItem = (EditTextItem) getLayoutInflater().inflate(R.layout.edit_text_item, linEditTextItemContainer, false);
        editEmailItem.setHint(getString(R.string.email));
        editEmailItem.addTextChangedListner(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editEmailItem.setHasValidString(android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        linEditTextItemContainer.addView(editEmailItem);


        editFirstName = (EditTextItem) getLayoutInflater().inflate(R.layout.edit_text_item, linEditTextItemContainer, false);
        editFirstName.setHint(getString(R.string.first_name));
        editFirstName.addTextChangedListner(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() == false)
                    editFirstName.setHasValidString(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        linEditTextItemContainer.addView(editFirstName);


        editLastName = (EditTextItem) getLayoutInflater().inflate(R.layout.edit_text_item, linEditTextItemContainer, false);
        editLastName.setHint(getString(R.string.last_name));
        editLastName.addTextChangedListner(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() == false)
                    editLastName.setHasValidString(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        linEditTextItemContainer.addView(editLastName);


        editMobile = (EditTextItem) getLayoutInflater().inflate(R.layout.edit_text_item, linEditTextItemContainer, false);
        editMobile.setHint(getString(R.string.mobile));
        editMobile.addTextChangedListner(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() == false)
                    editMobile.setHasValidString(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        linEditTextItemContainer.addView(editMobile);


        editPasswordItem = (EditTextItem) getLayoutInflater().inflate(R.layout.edit_text_item, linEditTextItemContainer, false);
        editPasswordItem.setHint(getString(R.string.password));
        editPasswordItem.setEditTextInputtype(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editPasswordItem.addTextChangedListner(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editPasswordItem.setHasValidString(s.toString().length() > 3);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        linEditTextItemContainer.addView(editPasswordItem);


        editPasswordItemConfirm = (EditTextItem) getLayoutInflater().inflate(R.layout.edit_text_item, linEditTextItemContainer, false);
        editPasswordItemConfirm.setHint(getString(R.string.password_repeat));
        editPasswordItemConfirm.setEditTextInputtype(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editPasswordItemConfirm.addTextChangedListner(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPasswordItem.getText().equals(s.toString())) {
                    editPasswordItemConfirm.setHasValidString(true);
                } else {
                    editPasswordItemConfirm.setHasValidString(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        linEditTextItemContainer.addView(editPasswordItemConfirm);


        final DatabaseService databaseService = new DatabaseService(RegisterActivity.this);
        buttonRegister = (Button) findViewById(R.id.register_button_ok);

        buttonRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isEmailAlreadyExists = false;
                if (getIntent().hasExtra(AppConstants.INTENT_EXTRA_USER_ID) == false) {
                    for (User user : databaseService.getUsers()) {
                        if (editEmailItem.getText().equals(user.getEmail())) {
                            isEmailAlreadyExists = true;
                            Toast.makeText(RegisterActivity.this, R.string.email_already_exists, Toast.LENGTH_LONG).show();
                        }
                    }
                }


                Log.e("RegisterActivity", "onclickRegisterButton");
                if (editPasswordItem.getVisibility() == View.VISIBLE) {

                    if (!editPasswordItem.hasValidString()) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.passwords_is_short), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!editPasswordItemConfirm.hasValidString()) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.passwords_do_not_match), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!editEmailItem.hasValidString()) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.please_enter_a_valid_email_address), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (isEmailAlreadyExists) {
                    Toasty.error(RegisterActivity.this, getString(R.string.email_already_exists), Toast.LENGTH_LONG, true).show();
                    return;
                }


                Boolean isLocal = true;
                User lastUser = null;
                if (getIntent().hasExtra(AppConstants.INTENT_EXTRA_USER_ID)) {
                    DatabaseService db = new DatabaseService(RegisterActivity.this);
                    lastUser = db.getUserById(getIntent().getExtras().getInt(AppConstants.INTENT_EXTRA_USER_ID));

                    if (lastUser.getIsAdmin()) {
                        int retval = db.deleteUser(lastUser);
                        if (retval != 1) {
                            Toast.makeText(RegisterActivity.this, R.string.failed_to_apply_changes, Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            User user = new User(
                                    editFirstName.getText(),
                                    "",
                                    editLastName.getText(),
                                    "Admin",
                                    editMobile.getText(),
                                    "",
                                    "",
                                    "",
                                    "adminkalaryigini",
                                    BCrypt.hashpw(editPasswordItem.getText(), BCrypt.gensalt()),
                                    new Date(),
                                    true,
                                    true);
                            db.insertUser(user);
                            Toasty.success(RegisterActivity.this, R.string.changes_successfully_saved, Toast.LENGTH_SHORT, true).show();
                            finish();
                        }
                    }
                }

                if (lastUser == null || !lastUser.getIsAdmin())
                    askForRFIDCard(lastUser);

            }
        });


    }


    private String textLastRFID = "";

    private void askForRFIDCard(final User lastUser) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_scan_rfid);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setTitle("");
            final Button dialogButton = dialog.findViewById(R.id.dialog_edit_text_button_save);
            dialog.setCancelable(false);
            final EditText editText = dialog.findViewById(R.id.dialog_edit_text_edittext);
            final ImageView imageViewRFID = dialog.findViewById(R.id.imageViewRFID);
            editText.setSingleLine(true);

            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Log.e("Login", "EVENT: " + v.getText().toString());
                    String text = editText.getText().toString();
                    if (text.length() > 0) {

                        if (db.getUserByRFID(text) != null && (lastUser == null || !db.getUserByRFID(text).getEmail().equals(lastUser.getEmail()))) {
                            imageViewRFID.setImageResource(R.drawable.rfid_scan_wrong);
                            dialogButton.setEnabled(false);
                            Toasty.error(RegisterActivity.this,
                                    getString(R.string.the_rfid_are_registered_already), Toast.LENGTH_LONG, true).show();
                        } else {
                            dialogButton.setEnabled(true);
                            imageViewRFID.setImageResource(R.drawable.rfid_scan_done);
                        }

                        textLastRFID = text;
                    }
                    editText.requestFocus();
                    editText.setText("");

                    return true;
                }
            });

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {

                        editText.requestFocus();
                        editText.setText("");

                    }
                }
            });
            final Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //register user
                    dialogButton.setEnabled(false);
                    dialogButton.setText(getString(R.string.saving));
                    dialogButtonNo.setEnabled(false);
                    buttonRegister.setEnabled(false);
                    buttonRegister.setText(getString(R.string.saving));
                    if (lastUser != null)
                        db.deleteUser(lastUser);
                    User user = new User(
                            editFirstName.getText(),
                            "",
                            editLastName.getText(),
                            editEmailItem.getText(),
                            editMobile.getText(),
                            "",
                            "",
                            "",
                            textLastRFID,
                            BCrypt.hashpw(editPasswordItem.getText(), BCrypt.gensalt()),
                            new Date(),
                            false,
                            true);
                    db.insertUser(user);
                    Toasty.success(getApplicationContext(), R.string.account_created,
                            Toast.LENGTH_LONG, true).show();
                    dialog.dismiss();
                    finish();
                }
            });

            dialog.show();

            editText.requestFocus();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {

        if (getIntent().hasExtra(AppConstants.INTENT_EXTRA_USER_ID)) {
            try {
                DatabaseService db = new DatabaseService(this);
                User user = db.getUserById(getIntent().getExtras().getInt(AppConstants.INTENT_EXTRA_USER_ID));
                editEmailItem.setText(user.getEmail());
                editEmailItem.setVisibility(View.GONE);
                editEmailItem.setFocusable(false);
//                if (user.getIsAdmin() == true) {
//                    editEmailItem.setVisibility(View.VISIBLE);
//                    editEmailItem.set
//                } else {
//                    editEmailItem.setVisibility(View.INVISIBLE);
//                }
                navigationbar.getTextTitle().setText(user.getEmail());
                editMobile.setText(user.getMobile());
                editFirstName.setText(user.getFirstName());
                editLastName.setText(user.getLastName());
                buttonRegister.setText(R.string.apply_changes);
                if (user.getIsAdmin()) {
                    isEditingAdminAccount = true;
                } else {
                    isEditingAdminAccount = false;
                }


            } catch (Exception e) {

            }
        } else {
            buttonRegister.setText(R.string.register);
            navigationbar.getTextTitle().setText(R.string.register_new_user);
            editEmailItem.setVisibility(View.VISIBLE);
            editEmailItem.setEnabled(true);
            editEmailItem.setFocusable(true);
            isEditingAdminAccount = false;
        }
        super.onResume();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

}
