package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.bcrypt.BCrypt;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.PostUserLoginService;
import com.certoclav.library.certocloud.PostUserLoginService.PutUserLoginTaskFinishedListener;
import com.certoclav.library.certocloud.PostUtil;
import com.certoclav.library.util.SettingsDeviceUtils;

import org.json.JSONObject;

import java.util.Date;
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
			dialog.setTitle("Login failed");
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
					editor.putBoolean(AppConstants.PREFERENCE_KEY_ONLINE_MODE,
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
			Scale.getInstance().setState(ScaleState.READY);
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText("LOGIN");
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
		navigationbar.getButtonSettings().setVisibility(View.GONE);
		navigationbar.getButtonBack().setVisibility(View.GONE);

		progressBar = (ProgressBar) findViewById(R.id.login_progressbar);
		SettingsDeviceUtils settingsUtils = new SettingsDeviceUtils();

		settingsUtils.setvolumeToMaximum(this);
		settingsUtils.setScreenBrightnessToMaximum(this);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		final DatabaseService databaseService = new DatabaseService(this);




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
								AppConstants.PREFERENCE_KEY_ONLINE_MODE, true);
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
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					Scale.getInstance().setUser(listUsers.get(position));
					currentUser = listUsers.get(position);
					editTextPassword.setText("");
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
					if (getDefaultSharedPreferences(
						LoginActivity.this).getBoolean(
						AppConstants.PREFERENCE_KEY_ONLINE_MODE, false) == true) {
					if (ApplicationController.getInstance().isNetworkAvailable()) {

						buttonLogin.setEnabled(false);
						progressBar.setVisibility(View.VISIBLE);

						postUserLoginService = new PostUserLoginService();
						postUserLoginService.setOnTaskFinishedListener(LoginActivity.this);
						postUserLoginService.loginUser(currentUser.getEmail(),
								editTextPassword.getText().toString(),
								"01234567891234567S");
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
							if (BCrypt.checkpw(params[0], currentUser.getPassword())
									|| AppConstants.IS_IO_SIMULATED
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
								Scale.getInstance().setState(
										ScaleState.READY);
								Intent intent = new Intent(LoginActivity.this,
										HomeActivity.class);
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
		Log.e("LoginActivity", "onresume called");
		super.onResume();
		navigationbar.setButtonEventListener(this);
		progressBar.setVisibility(View.INVISIBLE);
		Scale.getInstance();
		editTextPassword.setText("");

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
				AppConstants.PREFERENCE_KEY_ONLINE_MODE, false) == true) {
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

			User user1 = new User("Michael", "", "Dirix","michaeldirix@mytum.de", "mobile", "landline", "Certoclav","companyphone", "companyWebsite", BCrypt.hashpw("1234",BCrypt.gensalt()), new Date(), true,true);


			// Max regierstriert sich
			int result = databaseService.insertUser(user1);
			Log.e("return of insertUser: ", Integer.toString(result));

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
			Scale.getInstance().setState(ScaleState.READY);
			try {
				if (Scale.getInstance().getUser().getIsLocal() == true) {
					DatabaseService databaseService = new DatabaseService(
							LoginActivity.this);
					databaseService.updateUserIsLocal(Scale.getInstance()
							.getUser().getEmail(), false);
				}
			} catch (Exception e) {
			}
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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
				editor.putBoolean(AppConstants.PREFERENCE_KEY_ONLINE_MODE,
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
					editor.putBoolean(AppConstants.PREFERENCE_KEY_ONLINE_MODE,true);
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
					editor.putBoolean(AppConstants.PREFERENCE_KEY_ONLINE_MODE,
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

			case Navigationbar.BUTTON_ADD:
				final Dialog dialog = new Dialog(LoginActivity.this);
				dialog.setContentView(R.layout.dialog_yes_no);
				dialog.setTitle(R.string.register_new_user);
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


		}
	}
}
