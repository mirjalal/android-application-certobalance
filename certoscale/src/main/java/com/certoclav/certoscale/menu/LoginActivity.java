package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.UserDropdownAdapter;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.graph.GraphService;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.service.ReadAndParseSerialService;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.supervisor.StateMachine;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.bcrypt.BCrypt;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.PostUserLoginService;
import com.certoclav.library.certocloud.PostUserLoginService.PutUserLoginTaskFinishedListener;
import com.certoclav.library.certocloud.PostUtil;
import com.certoclav.library.util.SettingsDeviceUtils;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends Activity implements ButtonEventListener, PutUserLoginTaskFinishedListener {


    private Button buttonLogin;
    private EditText editTextPassword;
    private EditText editTextRFID;
    private Spinner spinner;
    private List<User> listUsers;
    private User currentUser;
    private UserDropdownAdapter adapterUserDropdown;
    private PostUserLoginService postUserLoginService = null;
    private String loginFailedMessage = "";
    private TextView textViewNotification = null;
    private ProgressBar progressBar = null; // progess bar which shows cloud
    // login process
    private Navigationbar navigationbar = null;
    private int counter = 0;

    // Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();


    final Runnable mShowLoginFailed = new Runnable() {
        public void run() {
            buttonLogin.setEnabled(true);
            Toasty.error(getApplicationContext(),
                    R.string.password_not_correct, Toast.LENGTH_LONG, true).show();
        }
    };

    final Runnable mShowCloudLoginFailed = new Runnable() {

        public void run() {
            buttonLogin.setEnabled(true);

            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setTitle(R.string.login_failed);
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(loginFailedMessage);
            text.append(getString(R.string.do_you_want_to_switch_to_offline_mode_));
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences prefs =
                            getDefaultSharedPreferences(LoginActivity.this);
                    Editor editor = prefs.edit();
                    editor.putBoolean(getString(R.string.preferences_device_snchronization),
                            false);
                    editor.commit();
                    dialog.dismiss();
                    onResume();
                }
            });

            Button dialogButtonNo = (Button) dialog
                    .findViewById(R.id.dialogButtonNO);
            dialogButtonNo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    };

    final Runnable mShowLoginSuccessfull = new Runnable() {
        public void run() {

            Toasty.success(LoginActivity.this, getString(R.string.login_successful), Toast.LENGTH_LONG, true).show();

            buttonLogin.setEnabled(true);
            Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
            startActivity(intent);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseService databaseService = new DatabaseService(this);

        if (getIntent() != null && getIntent().hasExtra("userid") && getIntent().getStringExtra("userid") != null) {
            User user = databaseService.getUserByRFID(getIntent().getStringExtra("userid"));
            if (user != null) {
                currentUser = user;
                Scale.getInstance().setUser(user);
                Toasty.success(LoginActivity.this,
                        getString(R.string.login_successful),
                        Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(LoginActivity.this,
                        ApplicationActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toasty.error(LoginActivity.this, getString(R.string.the_rfid_are_not_registered), Toast.LENGTH_LONG, true).show();
            }
        }

//        try {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setComponent(new ComponentName("com.estrongs.android.pop", "com.estrongs.android.pop.ftp.ESFtpShortcut"));
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toasty.warning(this, "Please install ES FTP to use FTP features", Toast.LENGTH_SHORT, true).show();
//        }

        //super.setTheme(R.style.the);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.login_activity);

        editTextRFID = (EditText) findViewById(R.id.loginEditTextRFID);
        editTextRFID.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String text = editTextRFID.getText().toString().trim();
                Log.d("RFID TEXT", text);
                if (text.length() > 0) {
                    //check in database the user with the RFID exists
                    User user = databaseService.getUserByRFID(text);

                    if (user != null) {
                        currentUser = user;
                        Scale.getInstance().setUser(user);
                        Toasty.success(LoginActivity.this,
                                getString(R.string.login_successful),
                                Toast.LENGTH_LONG, true).show();
                        Intent intent = new Intent(LoginActivity.this,
                                ApplicationActivity.class);
                        startActivity(intent);
                    } else {
                        Toasty.error(LoginActivity.this, getString(R.string.the_rfid_are_not_registered), Toast.LENGTH_LONG, true).show();
                    }
                }
                editTextRFID.requestFocus();
                editTextRFID.setText("");

                return false;
            }
        });

        editTextRFID.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editTextRFID.requestFocus();
                    editTextRFID.setText("");
                }
            }
        });
        navigationbar = new Navigationbar(this);
        navigationbar.onCreate();
        navigationbar.getTextTitle().setText(getString(R.string.login_menu).toUpperCase());
        navigationbar.getTextTitle().setVisibility(View.VISIBLE);
        navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
        navigationbar.getButtonSettingsDevice().setVisibility(View.VISIBLE);
        navigationbar.getButtonCompanyLogo().setVisibility(View.GONE);

        //View view = this.getWindow().getDecorView();
        //view.setBackgroundColor(Color.WHITE);


        StateMachine.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.login_progressbar);
        SettingsDeviceUtils settingsUtils = new SettingsDeviceUtils();

        try {
            settingsUtils.setvolumeToMaximum(this);
            settingsUtils.setScreenBrightnessToMaximum(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        TextView textSimulationMode = (TextView) findViewById(R.id.menu_main_text_simulation_mode);
        if (AppConstants.IS_IO_SIMULATED == true) {

            textSimulationMode.setTextColor(Color.YELLOW);
            textSimulationMode.setText(R.string.simulation_mode);


        } else {
            textSimulationMode.setText("");
        }

        // initialize login form
        buttonLogin = (Button) findViewById(R.id.loginButtonLogin);
        editTextPassword = (EditText) findViewById(R.id.loginEditTextPassword);
        spinner = (Spinner) findViewById(R.id.login_spinner);

        textViewNotification = (TextView) findViewById(R.id.login_text_notification);
        textViewNotification.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setTitle(getString(R.string.enable_network_communication));
                TextView text = (TextView) dialog.findViewById(R.id.text);
                ImageView image = (ImageView) dialog
                        .findViewById(R.id.dialog_image);
                image.setVisibility(View.GONE);

                text.setText(getString(R.string.do_you_want_to_enable_network_communication));
                Button dialogButton = (Button) dialog
                        .findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefs =
                                getDefaultSharedPreferences(LoginActivity.this);
                        Editor editor = prefs.edit();
                        editor.putBoolean(
                                getString(R.string.preferences_device_snchronization), true);
                        textViewNotification.setVisibility(View.GONE);
                        editor.commit();
                        dialog.dismiss();
                    }
                });

                Button dialogButtonNo = (Button) dialog
                        .findViewById(R.id.dialogButtonNO);
                dialogButtonNo.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        // read all users from database
        listUsers = databaseService.getUsers();

        // Fill Spinner with Emailaddresses of users

        adapterUserDropdown = new UserDropdownAdapter(this, listUsers);

        // adapterUserDropdown.setDropDownViewResource(R.layout.spinner_dropdown_item_large);
        spinner.setAdapter(adapterUserDropdown);
        spinner.setAdapter(adapterUserDropdown);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                try {
                    Scale.getInstance().setUser(listUsers.get(position));
                    currentUser = listUsers.get(position);
                    editTextPassword.setText("");
                    SharedPreferences prefs = getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(AppConstants.PREFERENCE_LAST_LOGGED_IN_USER_ID, currentUser.getUserId());
                    editor.commit();

                } catch (IndexOutOfBoundsException e) {
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editTextPassword.setText("");
            }
        });

        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentUser.getPublicKey().equals("")) {
                    KeyPair keyPair = generateKeyPair();
                    //Toast.makeText(ApplicationController.getContext(),keyPair.getPublic().toString(), Toast.LENGTH_LONG).show();
                    try {
                        currentUser.setPublicKey(ApplicationManager.getInstance().savePublicKey(keyPair.getPublic()));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }

                    try {
                        currentUser.setPrivateKey(ApplicationManager.getInstance().savePrivateKey(keyPair.getPrivate()));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                    databaseService.deleteUser(currentUser);
                    databaseService.insertUser(currentUser);

                }


                if (getDefaultSharedPreferences(
                        LoginActivity.this).getBoolean(
                        getString(R.string.preferences_device_snchronization), false) == true) {
                    if (ApplicationController.getInstance().isNetworkAvailable()) {

                        buttonLogin.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);

                        postUserLoginService = new PostUserLoginService();
                        postUserLoginService.setOnTaskFinishedListener(LoginActivity.this);
                        postUserLoginService.loginUser(currentUser.getEmail(),
                                editTextPassword.getText().toString(),
                                Scale.getInstance().getSafetyKey());
                    } else {
                        showNotificationForNetworkNavigation();
                        Toasty.warning(LoginActivity.this,
                                getString(R.string.network_not_connected), Toast.LENGTH_LONG, true)
                                .show();
                    }

                } else {


                    new AsyncTask<String, Boolean, Boolean>() {

                        @Override
                        protected void onPreExecute() {
                            buttonLogin.setEnabled(false);
                            progressBar.setVisibility(View.VISIBLE);

                            super.onPreExecute();
                        }

                        @Override
                        protected Boolean doInBackground(String... params) {
                            if (AppConstants.IS_IO_SIMULATED) {
                                return true;
                            }
                            //  if (BCrypt.checkpw(params[0], currentUser.getPassword())
                            //        || params[0].equals("master@certocloud")) {
                            return true;

                            //}
                            //return false;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            buttonLogin.setEnabled(true);
                            progressBar.setVisibility(View.GONE);

                            if (result) {
                                Toasty.success(LoginActivity.this,
                                        getString(R.string.login_successful),
                                        Toast.LENGTH_LONG, true).show();
                                Intent intent = new Intent(LoginActivity.this,
                                        ApplicationActivity.class);
                                startActivity(intent);
                            } else {
                                Toasty.warning(getApplicationContext(),
                                        R.string.password_not_correct,
                                        Toast.LENGTH_LONG, true).show();

                            }
                            super.onPostExecute(result);
                        }

                    }.execute(editTextPassword.getText().toString());


                }

            }
        });

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        /*  if (!getCertificateSHA1Fingerprint().equals(AppConstants.SIGNATURE) && !AppConstants.IS_IO_SIMULATED *//*&& !macAddress.equals("a0:2c:36:85:48:45")*//*) {
            finish();
        }*/

    }


    private String getCertificateSHA1Fingerprint() {
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }


    @Override
    protected void onResume() {


        ReadAndParseSerialService.getInstance();
        GraphService.getInstance();
        Log.e("LoginActivity", "onresume called");
        super.onResume();
        navigationbar.setButtonEventListener(this);
        progressBar.setVisibility(View.INVISIBLE);
        Scale.getInstance();
        editTextPassword.setText("");


        fillDatabaseIfEmpty();


        refreshUI();

    }

    /*
     * TODO: Outsource logic parts of this function
     */
    private void refreshUI() {

        // if IO is simulated, then fill Database with dummy data and set
        // connected controller to Autoclave Model class.


        buttonLogin.setEnabled(true);
        DatabaseService databaseService = new DatabaseService(this);

        listUsers = databaseService.getUsers();


//        if (getDefaultSharedPreferences(this).getBoolean(
//                getString(R.string.preferences_device_snchronization), false) == true) {
//            textViewNotification.setVisibility(View.GONE);
//        } else {
//            textViewNotification.setVisibility(View.GONE);
//        }

        if (listUsers == null) {
            spinner.setEnabled(false);
            editTextPassword.setEnabled(false);
            buttonLogin.setEnabled(false);
        } else if (listUsers.size() == 0) {
            spinner.setEnabled(false);
            editTextPassword.setEnabled(false);
            buttonLogin.setEnabled(false);
        } else {
            spinner.setEnabled(true);
            editTextPassword.setEnabled(true);
            buttonLogin.setEnabled(true);
        }


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Fill Spinner with Emailaddresses of users
        adapterUserDropdown.clear();

        for (User user : listUsers) {
            adapterUserDropdown.add(user);
        }

        adapterUserDropdown.notifyDataSetChanged();
        try {
            int lastLoggedInUserId = getDefaultSharedPreferences(this).getInt(AppConstants.PREFERENCE_LAST_LOGGED_IN_USER_ID, 0);
            if (lastLoggedInUserId != 0) {
                for (User user : listUsers) {
                    if (user.getUserId() == lastLoggedInUserId) {
                        spinner.setSelection(adapterUserDropdown.getPosition(user));
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    private void fillDatabaseIfEmpty() {
        Log.e("LoginActivity", "fillDbIfEmpty");

        try {
            DatabaseService databaseService = new DatabaseService(this);
            if (databaseService.getUsers() != null) {
                if (databaseService.getUsers().size() > 0) {
                    if(databaseService.getUserByUsername("Admin")==null){
                        User user1 = new User("", "", "", "Admin", "", "", "", "", "", BCrypt.hashpw("admin", BCrypt.gensalt()), new Date(), true, true);
                        databaseService.insertUser(user1);
                    }
                    return;
                }
            }

            User user1 = new User("", "", "", "Admin", "", "", "", "", "", BCrypt.hashpw("admin", BCrypt.gensalt()), new Date(), true, true);

            List<RecipeEntry> entries = new ArrayList<RecipeEntry>();
            entries.add(new RecipeEntry("Tare", 0d, 1, " ", " ", "Please put a bottle with at least 20ml volume onto the pan and press TARE", 0d));
            entries.add(new RecipeEntry("Calcium", 0.10, 2, "1300232", "g", "Please put 0.1 gram calcium onto the pan", 0d));
            entries.add(new RecipeEntry("Water", 9.90, 3, " ", "g", "Please fill in 9.9 gram water into the bottle", 0d));

            Date date = new Date();
            //databaseService.insertRecipe(new Recipe("","Calcium recipe",entries));
            Recipe recipe = new Recipe("", "Calcium sol.", entries, ((Long) date.getTime()).toString(), Scale.getInstance().getSafetyKey(), "private", user1.getEmail());
            databaseService.insertRecipe(recipe);


            databaseService.insertItem(new Item("Item 1", 12.0282d, 33.21, "article2332", ((Long) date.getTime()).toString(), "Item description", Scale.getInstance().getSafetyKey(), "g", "", "private"));
            databaseService.insertItem(new Item("Item 2", 12.0282d, 33.21, "article2332", ((Long) date.getTime()).toString(), "Item description", Scale.getInstance().getSafetyKey(), "g", "", "private"));
            Library library = new Library(user1.getEmail(), ScaleApplication.PART_COUNTING.ordinal(), "", 0, "Default", 0.0f, 10.0f, 5, 1, 1, 10, 10, 20, 10, 100, 0, 0, new Date(), true, 10.0f, 0f, 10f, 10f, 1f, 1f, 24f, 1f, 10f, 2f, 20f, 10f, 1f, 2f, 1f, 2f, 10f, 24f, 1f, 1f, 1f, 2);

            // Max regierstriert sich
            databaseService.insertUser(user1);
            int result = databaseService.insertLibrary(library);
            Log.e("return insertLibrary: ", " " + result);

            Unit unit = new Unit(0d, 1d, "gram", Unit.UNIT_GRAM, "", true, false); // 1g = 1*10^0 g
            result = databaseService.insertUnit(unit);
            Log.e("return insertUnit: ", " " + result);
            unit = new Unit(0d, 0.35274d, "Unze", Unit.UNIT_OUNCE, "", true, false); //1g = 35274*10^-5 oz
            databaseService.insertUnit(unit);
            unit = new Unit(-3d, 1d, "kilogram", Unit.UNIT_KILOGRAM, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(3d, 1d, "milligram", Unit.UNIT_MILLIGRAM, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-3d, 0.157473d, "stone", Unit.UNIT_STONE, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-2d, 0.220462d, "pound", Unit.UNIT_POUND, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(1d, 0.5d, "metric carat", Unit.UNIT_METRIC_CARAT, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-1d, 0.32150746d, "ounce troy", Unit.UNIT_OUNCE_TROY, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(0d, 0.643015d, "pennyweight", Unit.UNIT_PENNYWEIGHT, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(2d, 0.154324d, "grain", Unit.UNIT_GRAIN, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-2d, 0.980665d, "Newton", Unit.UNIT_NEWTON, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(0d, 0.264555d, "momme", Unit.UNIT_MOMME, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(0d, 0.217391304d, "mesghal", Unit.UNIT_MESGHAL, "", true, false); //nochmal überprüfen
            databaseService.insertUnit(unit);
            unit = new Unit(-1d, 0.26659557d, "Tael (HK)", Unit.UNIT_TAEL_HK, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-1d, 0.26455470d, "Tael (SG)", Unit.UNIT_TAEL_SG, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-1d, 0.60975609d, "tical (Asia)", Unit.UNIT_TICAL_ASIA, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-1d, 0.85735260d, "tola", Unit.UNIT_TOLA, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(-1d, 0.66666666d, "baht (Thailand)", Unit.UNIT_BAHT, "", true, false);
            databaseService.insertUnit(unit);
            unit = new Unit(0d, 1d, "Custom unit 1", "c1", "", true, true);
            databaseService.insertUnit(unit);
            unit = new Unit(0d, 1d, "Custom unit 2", "c2", "", true, true);
            databaseService.insertUnit(unit);
            unit = new Unit(0d, 1d, "Custom unit 3", "c3", "", true, true);
            databaseService.insertUnit(unit);


        } catch (Exception e) {
            Log.e("LoginActivity", "Database broken");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        navigationbar.removeNavigationbarListener(this);
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

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
        return ret;
    }


    @Override
    public void onBackPressed() {
        Log.e("LoginActivity", "Hardware Button Back disabled");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            Log.e("LoginActivity", "Home Button disabled");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTaskFinished(int responseCode) {
        progressBar.setVisibility(View.GONE);

        Log.e("LoginActivity", "onTaskFinished - statusCode: " + responseCode);
        buttonLogin.setEnabled(true);
        switch (responseCode) {
            case PostUtil.RETURN_OK:
                Toasty.success(LoginActivity.this,
                        getResources().getString(R.string.login_successful),
                        Toast.LENGTH_LONG, true).show();
                try {
                    if (Scale.getInstance().getUser().getIsLocal() == true) {
                        DatabaseService databaseService = new DatabaseService(
                                LoginActivity.this);
                        databaseService.updateUserIsLocal(Scale.getInstance()
                                .getUser().getEmail(), false);
                    }
                } catch (Exception e) {
                }
                Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
                startActivity(intent);

                new AsyncTask<Boolean, Boolean, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Boolean... params) {
                        // TODO add device if it is not added yet

                        try {
                            PostUtil postUtil = new PostUtil();
                            JSONObject jsonDevice = new JSONObject();
                            jsonDevice.put("devicekey", Scale.getInstance().getSafetyKey());
                            postUtil.postToCertocloud(
                                    jsonDevice.toString(),
                                    CertocloudConstants.SERVER_URL
                                            + CertocloudConstants.REST_API_POST_DEVICE,
                                    true);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;

                        }

                        return true;
                    }
                }.execute();

                return; //break;

            case PostUtil.RETURN_UNKNOWN:
            case PostUtil.RETURN_ERROR:
                loginFailedMessage = getString(R.string.an_error_occured_during_login_please_try_again_later_);
                break;
            case PostUtil.RETURN_ERROR_TIMEOUT:
                loginFailedMessage = getString(R.string.timout_during_login_please_check_internet_availability_);
                break;
            case PostUtil.RETURN_ERROR_UNAUTHORISED_PASSWORD:
                loginFailedMessage = getString(R.string.password_not_correct_);
                break;
            case PostUtil.RETURN_ERROR_UNAUTHORISED_MAIL:
                loginFailedMessage = getString(R.string.email_does_not_exist_please_create_a_certocloud_account_with_this_email_first_);
                break;
            case PostUtil.RETURN_ERROR_UNKNOWN_HOST:
                loginFailedMessage = getString(R.string.not_able_to_connect_to_certocloud_);
                break;
            case PostUtil.RETURN_ERROR_ACCOUNT_NOT_ACTIVATED:
                Intent intent2 = new Intent(LoginActivity.this, ActivateCloudAccountActivity.class);
                startActivity(intent2);
                return;
        }

        mHandler.post(mShowCloudLoginFailed);

    }

    private void showCreateAccountDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_add_create);
        dialog.setTitle(R.string.register_new_user);
        dialog.setCanceledOnTouchOutside(true);

        Button buttonCreateLocal = (Button) dialog
                .findViewById(R.id.dialogButtonCreateLocal);
        Button buttonAddExisting = (Button) dialog
                .findViewById(R.id.dialogButtonAddExisting);
        buttonAddExisting.setEnabled(false);
        Button buttonCreateCloud = (Button) dialog
                .findViewById(R.id.dialogButtonCreateNew);
        buttonCreateCloud.setEnabled(false);

        buttonCreateLocal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs =
                        getDefaultSharedPreferences(LoginActivity.this);
                Editor editor = prefs.edit();
                editor.putBoolean(getString(R.string.preferences_device_snchronization),
                        false);
                editor.commit();


                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        buttonAddExisting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationController.getInstance().isNetworkAvailable()) {
                    SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this);
                    Editor editor = prefs.edit();
                    editor.putBoolean(getString(R.string.preferences_device_snchronization), true);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, AddCloudAccountActivity.class);
                    startActivity(intent);
                } else {
                    showNotificationForNetworkNavigation();
                }
                dialog.dismiss();
            }
        });

        buttonCreateCloud.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationController.getInstance().isNetworkAvailable()) {

                    Intent i = new Intent(LoginActivity.this, RegisterCloudAccountActivity.class);
                    startActivity(i);
                    dialog.dismiss();
                } else {
                    showNotificationForNetworkNavigation();
                }
                dialog.dismiss();
            }

        });

        dialog.show();

    }


    Dialog dialogAdminLogin;
    private class CheckAdminPassword extends AsyncTask<Object, Void, Boolean> {
        private boolean isAdminLogin;
        private ProgressDialog dialogProgress;

        protected Boolean doInBackground(Object... params) {
            User user = (User) params[0];
            isAdminLogin = (boolean) params[2];
            if(user!=null && BCrypt.checkpw(params[1].toString(),user.getPassword())){
                Scale.getInstance().setUser(user);
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress = ProgressDialog.show(LoginActivity.this, "",
                    getString(R.string.checking), true);
        }

        protected void onPostExecute(Boolean result) {
            dialogProgress.dismiss();
            if (result) {
                if(dialogAdminLogin!=null)
                    dialogAdminLogin.dismiss();

                if(isAdminLogin) {
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                }else{
                    final Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(R.string.register_new_user);
                    dialog.setCanceledOnTouchOutside(true);
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    ImageView image = (ImageView) dialog
                            .findViewById(R.id.dialog_image);
                    image.setVisibility(View.GONE);
                    // set the custom dialog components - text, image and button

                    text.setText(getText(R.string.do_you_really_want_to) + " "
                            + getString(R.string.create_an_account_) + "\n");
                    Button dialogButton = (Button) dialog
                            .findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            showCreateAccountDialog();
                        }
                    });

                    Button dialogButtonNo = (Button) dialog
                            .findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }else{
                Toasty.error(getApplicationContext(),
                        R.string.password_not_correct, Toast.LENGTH_LONG, true).show();
            }
        }
    }

    private void askForAdminLogin() {

        dialogAdminLogin = new Dialog(LoginActivity.this);
        dialogAdminLogin.setContentView(R.layout.dialog_admin_password);
        dialogAdminLogin.setTitle(R.string.please_enter_admin_password);
        dialogAdminLogin.setCanceledOnTouchOutside(true);

        final EditText editTextPassword = (EditText) dialogAdminLogin.findViewById(R.id.editTextPassword);

        dialogAdminLogin
                .findViewById(R.id.dialogButtonLogin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseService databaseService = new DatabaseService(LoginActivity.this);
                User user = databaseService.getUserByUsername("Admin");
                new CheckAdminPassword().execute(user,editTextPassword.getText().toString(), true);
            }
        });

        dialogAdminLogin
                .findViewById(R.id.dialogButtonCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdminLogin.dismiss();
            }
        });

        dialogAdminLogin.show();

    }

    private void askForAdminLoginToRegisterANewUser() {

        dialogAdminLogin = new Dialog(LoginActivity.this);
        dialogAdminLogin.setContentView(R.layout.dialog_admin_password);
        dialogAdminLogin.setTitle(R.string.please_enter_admin_password);
        dialogAdminLogin.setCanceledOnTouchOutside(true);

        final EditText editTextPassword = (EditText) dialogAdminLogin.findViewById(R.id.editTextPassword);

        dialogAdminLogin
                .findViewById(R.id.dialogButtonLogin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseService databaseService = new DatabaseService(LoginActivity.this);
                User user = databaseService.getUserByUsername("Admin");
                new CheckAdminPassword().execute(user,editTextPassword.getText().toString(), false);
            }
        });

        dialogAdminLogin
                .findViewById(R.id.dialogButtonCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdminLogin.dismiss();
            }
        });

        dialogAdminLogin.show();

    }


    private void showNotificationForNetworkNavigation() {
        try {
            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.dialog_yes_no);
            dialog.setTitle(getString(R.string.network_connection_required));
            dialog.setCanceledOnTouchOutside(true);
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText(getString(R.string.please_connect_to_a_network_via_lan_or_wifi_));
            text.append(getString(R.string.do_you_want_to_open_wifi_settings_));
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);
            dialogButton.setText(getString(R.string.show_wifi_settings));
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(
                            WifiManager.ACTION_PICK_WIFI_NETWORK));
                    dialog.dismiss();
                }
            });

            Button dialogButtonNo = (Button) dialog
                    .findViewById(R.id.dialogButtonNO);
            dialogButtonNo.setText(getString(R.string.switch_to_offline_mode));
            dialogButtonNo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    SharedPreferences prefs =
                            getDefaultSharedPreferences(LoginActivity.this);
                    Editor editor = prefs.edit();
                    editor.putBoolean(getString(R.string.preferences_device_snchronization),
                            false);
                    editor.commit();
                    dialog.dismiss();
                    refreshUI();
                }
            });

            dialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
        switch (buttonId) {

            case ActionButtonbarFragment.BUTTON_ADD:
               askForAdminLoginToRegisterANewUser();
                break;
            case ActionButtonbarFragment.BUTTON_SETTINGS_DEVICE:
                askForAdminLogin();


        }
    }

    private KeyPair generateKeyPair() {


        KeyPair keyPair = null;
        try {
            // get instance of rsa cipher
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);            // initialize key generator
            keyPair = keyGen.generateKeyPair(); // generate pair of keys

        } catch (GeneralSecurityException e) {
            System.out.println(e);
        }

        return keyPair;

    }


}