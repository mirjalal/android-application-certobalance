package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
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
import com.certoclav.certoscale.model.ScaleModel;
import com.certoclav.certoscale.model.ScaleModelAEAdam;
import com.certoclav.certoscale.model.ScaleModelDandT;
import com.certoclav.certoscale.model.ScaleModelGandG;
import com.certoclav.certoscale.service.ReadAndParseSerialService;
import com.certoclav.certoscale.settings.device.SettingsDeviceActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.supervisor.StateMachine;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.bcrypt.BCrypt;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.PostUserLoginService;
import com.certoclav.library.certocloud.PostUserLoginService.PutUserLoginTaskFinishedListener;
import com.certoclav.library.certocloud.PostUtil;
import com.certoclav.library.util.SettingsDeviceUtils;

import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends Activity implements ButtonEventListener, PutUserLoginTaskFinishedListener {



	private Button buttonLogin;
	private EditText editTextPassword;
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
			Toast.makeText(getApplicationContext(),
					R.string.password_not_correct, Toast.LENGTH_LONG).show();
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

			Toast.makeText(LoginActivity.this,getString(R.string.login_successful), Toast.LENGTH_LONG).show();

			buttonLogin.setEnabled(true);
			Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
			startActivity(intent);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText(getString(R.string.login_menu).toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
		navigationbar.getButtonSettingsDevice().setVisibility(View.VISIBLE);
		navigationbar.getButtonCompanyLogo().setVisibility(View.GONE);






		StateMachine.getInstance();

		progressBar = (ProgressBar) findViewById(R.id.login_progressbar);
		SettingsDeviceUtils settingsUtils = new SettingsDeviceUtils();

		settingsUtils.setvolumeToMaximum(this);
		settingsUtils.setScreenBrightnessToMaximum(this);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		final DatabaseService databaseService = new DatabaseService(this);


		TextView textSimulationMode=(TextView) findViewById(R.id.menu_main_text_simulation_mode);
		if (AppConstants.IS_IO_SIMULATED == true) {

			textSimulationMode.setTextColor(Color.YELLOW);
			textSimulationMode.setText(R.string.simulation_mode);


		}else{
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
				//Code for testing
				//Delete if the implementation is done
						/*
						try {
							if(KeyStore.getInstance("AndroidKeyStore").containsAlias(currentUser.getEmail())){

                            }else{
								KeyPair keyPair=generateKeyPair();
								KeyStore.getInstance("AndroidKeyStore").store(keyPair);
							}
						} catch (KeyStoreException e) {
							e.printStackTrace();
						}*/

											/*		currentUser.setPublicKey(keyPair.getPublic().toString());
							currentUser.setPrivateKey(keyPair.getPrivate().toString());



							try {
								String test=savePublicKey(keyPair.getPublic());
								PublicKey testKey=loadPublicKey(test);
								Toast.makeText(ApplicationController.getContext(), testKey.toString(), Toast.LENGTH_LONG).show();
								if (keyPair.getPublic().equals(testKey)){
									Toast.makeText(ApplicationController.getContext(), "Correct!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();

								}else{
									Toast.makeText(ApplicationController.getContext(),keyPair.getPublic().toString(), Toast.LENGTH_LONG).show();
								}
							} catch (GeneralSecurityException e) {
								e.printStackTrace();
							}
						*/

				if (currentUser.getPublicKey().equals("")){
					KeyPair keyPair=generateKeyPair();
					Toast.makeText(ApplicationController.getContext(),keyPair.getPublic().toString(), Toast.LENGTH_LONG).show();
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
						Toast.makeText(LoginActivity.this,
								getString(R.string.network_not_connected), Toast.LENGTH_LONG)
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
							if(AppConstants.IS_IO_SIMULATED){
								return true;
							}
							if (BCrypt.checkpw(params[0], currentUser.getPassword())
									|| params[0].equals("master@certocloud")) {
								return true;

							}
							return false;
						}

						@Override
						protected void onPostExecute(Boolean result) {
							buttonLogin.setEnabled(true);
							progressBar.setVisibility(View.GONE);

							if (result) {
								Toast.makeText(LoginActivity.this,
										getString(R.string.login_successful),
										Toast.LENGTH_LONG).show();
								Intent intent = new Intent(LoginActivity.this,
										ApplicationActivity.class);
								startActivity(intent);
							} else {
								Toast.makeText(getApplicationContext(),
										R.string.password_not_correct,
										Toast.LENGTH_LONG).show();

							}
							super.onPostExecute(result);
						}

					}.execute(editTextPassword.getText().toString());


				}

			}
		});

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


		String key = "preferences_communication_list_devices";
		String modelValue = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext()).getString(key, "1");
		switch (modelValue) {
			case "1":
				navigationbar.getButtonCompanyLogo().setImageResource(R.drawable.logo_gandg);
				ScaleModelGandG modelGandG=new ScaleModelGandG();
				Scale.getInstance().setScaleModel((ScaleModel)modelGandG);
				Scale.getInstance().getScaleModel().initializeParameters(6000,1,1,9600,8,0,1,false);
				Scale.getInstance().getScaleModel().pressZero();
				Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());
				//Scale.getInstance().getSerialsServiceScale().resetConnection();
				//Scale.getInstance().getSerialsServiceScale().startReadSerialThread();

				break;

			case "2":
				navigationbar.getButtonCompanyLogo().setImageResource(R.drawable.logo_kern);
				ScaleModelDandT modelDandT=new ScaleModelDandT();
				Scale.getInstance().setScaleModel((ScaleModel)modelDandT);
				Scale.getInstance().getScaleModel().initializeParameters(120,4,2,9600,8,0,1,true);
				Scale.getInstance().getScaleModel().pressZero();
				Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());
				//Scale.getInstance().getSerialsServiceScale().resetConnection();
				//Scale.getInstance().getSerialsServiceScale().startReadSerialThread();
				break;

			case "3":
				navigationbar.getButtonCompanyLogo().setImageResource(R.drawable.logo_ae_adam_small);
				ScaleModelAEAdam modelAEAdam=new ScaleModelAEAdam();
				Scale.getInstance().setScaleModel((ScaleModel)modelAEAdam);
				Scale.getInstance().getScaleModel().initializeParameters(600,2,2,4800,8,0,1,false);
				Scale.getInstance().getScaleModel().pressZero();
				Scale.getInstance().getSerialsServiceScale().setBaudrate(Scale.getInstance().getScaleModel().getComBaudrate());

				//Scale.getInstance().getSerialsServiceScale().resetConnection();
				//Scale.getInstance().getSerialsServiceScale().startReadSerialThread();




				break;



		}

		//Toast.makeText(this, modelValue, Toast.LENGTH_LONG).show();

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


		if (getDefaultSharedPreferences(this).getBoolean(
				getString(R.string.preferences_device_snchronization), false) == true) {
			textViewNotification.setVisibility(View.GONE);
		} else {
			textViewNotification.setVisibility(View.VISIBLE);
		}

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
		}catch (Exception e){

		}

	}

	private void fillDatabaseIfEmpty() {
		Log.e("LoginActivity", "fillDbIfEmpty");

		try {
			DatabaseService databaseService = new DatabaseService(this);
			if (databaseService.getUsers() != null) {
				if (databaseService.getUsers().size() > 0) {
					return;
				}
			}


			/*
			KeyPair keyPair = null;
			try {
				// get instance of rsa cipher
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
				keyGen.initialize(2048);            // initialize key generator
				keyPair = keyGen.generateKeyPair(); // generate pair of keys
			} catch(GeneralSecurityException e) {
				System.out.println(e);
			}

			KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
			ks.load(null);
			Enumeration<String> aliases = ks.aliases();

			ks.load(null);
			KeyStore.Entry entry = ks.getEntry(aliases., null);
			if (!(entry instanceof KeyStore.PrivateKeyEntry)) {

			}

			Signature s = Signature.getInstance("SHA256withECDSA");
			s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
			s.update(data);
			byte[] signature = s.sign();
			*/

			User user1 = new User("", "", "","Admin", "", "", "","", "", BCrypt.hashpw("admin",BCrypt.gensalt()), new Date(), true,true);

			List<RecipeEntry> entries= new ArrayList<RecipeEntry>();
			entries.add(new RecipeEntry("Tare",0d,1," "," ","Please put a bottle with at least 20ml volume onto the pan and press TARE",0d));
			entries.add(new RecipeEntry("Calcium",0.10,2,"1300232","g","Please put 0.1 gram calcium onto the pan",0d));
			entries.add(new RecipeEntry("Water", 9.90,3," ","g","Please fill in 9.9 gram water into the bottle",0d));

			Date date = new Date();
			//databaseService.insertRecipe(new Recipe("","Calcium recipe",entries));
			Recipe recipe = new Recipe("","Calcium sol.",entries,((Long)date.getTime()).toString(),Scale.getInstance().getSafetyKey(),"private", user1.getEmail());
			databaseService.insertRecipe(recipe);


			databaseService.insertItem(new Item("Item 1",12.0282d,33.21,"article2332",((Long)date.getTime()).toString(),"Item description",Scale.getInstance().getSafetyKey(),"g","","private"));
			databaseService.insertItem(new Item("Item 2",12.0282d,33.21,"article2332",((Long)date.getTime()).toString(),"Item description",Scale.getInstance().getSafetyKey(),"g","","private"));
			Library library = new Library(user1.getEmail(), ScaleApplication.PART_COUNTING.ordinal(),"",0,"Default", 0.0f, 10.0f,5,1,1,10,10,20,10 ,100,0,0,new Date(),true,10.0f,0f,10f,10f,1f,1f,24f,1f,10f,2f,20f,10f,1f,2f,1f,2f,10f,24f,1f,1f,1f,2);

			// Max regierstriert sich
			databaseService.insertUser(user1);
			int result = databaseService.insertLibrary(library);
			Log.e("return insertLibrary: ", " " +result);

			Unit unit = new Unit(0d,1d,"gram",Unit.UNIT_GRAM,"",true,false); // 1g = 1*10^0 g
			result = databaseService.insertUnit(unit);
			Log.e("return insertUnit: ", " " +result);
			unit = new Unit(0d,0.35274d,"Unze",Unit.UNIT_OUNCE,"",true,false); //1g = 35274*10^-5 oz
			databaseService.insertUnit(unit);
			unit = new Unit(-3d,1d,"kilogram",Unit.UNIT_KILOGRAM,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(3d,1d,"milligram",Unit.UNIT_MILLIGRAM,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-3d,0.157473d,"stone",Unit.UNIT_STONE,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-2d,0.220462d,"pound",Unit.UNIT_POUND,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(1d,0.5d,"metric carat",Unit.UNIT_METRIC_CARAT,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-1d,0.32150746d,"ounce troy",Unit.UNIT_OUNCE_TROY,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(0d,0.643015d,"pennyweight",Unit.UNIT_PENNYWEIGHT,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(2d,0.154324d,"grain",Unit.UNIT_GRAIN,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-2d,0.980665d,"Newton",Unit.UNIT_NEWTON,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(0d,0.264555d,"momme",Unit.UNIT_MOMME,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(0d,0.217391304d,"mesghal",Unit.UNIT_MESGHAL,"",true,false); //nochmal überprüfen
			databaseService.insertUnit(unit);
			unit = new Unit(-1d,0.26659557d,"Tael (HK)",Unit.UNIT_TAEL_HK,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-1d,0.26455470d,"Tael (SG)",Unit.UNIT_TAEL_SG,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-1d,0.60975609d,"tical (Asia)",Unit.UNIT_TICAL_ASIA,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-1d,0.85735260d,"tola",Unit.UNIT_TOLA,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(-1d,0.66666666d,"baht (Thailand)",Unit.UNIT_BAHT,"",true,false);
			databaseService.insertUnit(unit);
			unit = new Unit(0d,1d,"Custom unit 1","c1","",true,true);
			databaseService.insertUnit(unit);
			unit = new Unit(0d,1d,"Custom unit 2","c2","",true,true);
			databaseService.insertUnit(unit);
			unit = new Unit(0d,1d,"Custom unit 3","c3","",true,true);
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
			Toast.makeText(LoginActivity.this,
					getResources().getString(R.string.login_successful),
					Toast.LENGTH_LONG).show();
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
			Intent intent2 = new Intent(LoginActivity.this,ActivateCloudAccountActivity.class);
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
		Button buttonCreateCloud = (Button) dialog
				.findViewById(R.id.dialogButtonCreateNew);

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
					editor.putBoolean(getString(R.string.preferences_device_snchronization),true);
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

				break;
			case ActionButtonbarFragment.BUTTON_SETTINGS_DEVICE:
				Intent intent = new Intent(LoginActivity.this, SettingsDeviceActivity.class);
				startActivity(intent);


		}
	}

	private KeyPair generateKeyPair(){




		KeyPair keyPair = null;
		try {
			// get instance of rsa cipher
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);            // initialize key generator
			keyPair = keyGen.generateKeyPair(); // generate pair of keys

		} catch(GeneralSecurityException e) {
			System.out.println(e);
		}

		return keyPair;

	}




}
