package com.certoclav.certoscale.model;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.SpinnerModeAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.ArrayList;


public class Navigationbar {

	public TextView getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(TextView textTitle) {
		this.textTitle = textTitle;
	}

	public Navigationbar(Activity activity) {
		mActivity = activity;

	}


	private int spinnerModeOnClickCounter = 0;

	public ImageButton getButtonLogout() {
		return buttonLogout;
	}

	public void setButtonLogout(ImageButton buttonLogout) {
		this.buttonLogout = buttonLogout;
	}

	private ImageButton buttonLogout = null;
	private ImageButton buttonHome = null;

	public ImageButton getButtonMore() {
		return buttonMore;
	}

	public void setButtonMore(ImageButton buttonMore) {
		this.buttonMore = buttonMore;
	}

	public ImageButton getButtonSettingsDevice() {
		return buttonSettingsDevice;
	}

	public void setButtonSettingsDevice(ImageButton buttonSettingsDevice) {
		this.buttonSettingsDevice = buttonSettingsDevice;
	}

	private ImageButton buttonMore = null;

	public ImageButton getButtonCompanyLogo() {
		return buttonCompanyLogo;
	}

	public void setButtonCompanyLogo(ImageButton buttonCompanyLogo) {
		this.buttonCompanyLogo = buttonCompanyLogo;
	}

	private ImageButton buttonSettingsDevice = null;


	public SpinnerModeAdapter getArrayAdapterMode() {
		return arrayAdapterMode;
	}

	public void setArrayAdapterMode(SpinnerModeAdapter arrayAdapterMode) {
		this.arrayAdapterMode = arrayAdapterMode;
	}

	private SpinnerModeAdapter arrayAdapterMode = null;

	public ArrayAdapter<String> getArrayAdapterLibrary() {
		return arrayAdapterLibrary;
	}

	public void setArrayAdapterLibrary(ArrayAdapter<String> arrayAdapterLibrary) {
		this.arrayAdapterLibrary = arrayAdapterLibrary;
	}

	private ArrayAdapter<String> arrayAdapterLibrary = null;

	public ImageButton getButtonBack() {
		return buttonBack;
	}

	public void setButtonBack(ImageButton buttonBack) {
		this.buttonBack = buttonBack;
	}

	private ImageButton buttonBack = null;
	private ImageButton buttonCompanyLogo =null;

	public ImageButton getButtonDelete() {return buttonDelete;}

	public void setButtonDelete(ImageButton buttonDelete) {this.buttonDelete = buttonDelete;}

	private ImageButton buttonDelete=null;

	public ImageButton getButtonHome() {
		return buttonHome;
	}

	public void setButtonHome(ImageButton buttonHome) {
		this.buttonHome = buttonHome;
	}

	public ImageButton getButtonSettings() {
		return buttonSettings;
	}

	public void setButtonSettings(ImageButton buttonSettings) {
		this.buttonSettings = buttonSettings;
	}

	public Spinner getSpinnerMode() {
		return spinnerMode;
	}



	public Spinner getSpinnerLib() {
		return spinnerLib;
	}

	public void setSpinnerLib(Spinner spinnerLib) {
		this.spinnerLib = spinnerLib;
	}

	private ImageButton buttonSettings = null;
	private Activity mActivity = null;
	private Spinner spinnerMode = null;
	private Spinner spinnerLib = null;
	private TextView textTitle = null;

	public ImageButton getButtonSave() {
		return buttonSave;
	}

	public void setButtonSave(ImageButton buttonSave) {
		this.buttonSave = buttonSave;
	}

	private ImageButton buttonSave = null;
	private ArrayList<ButtonEventListener> navigationbarListeners = new ArrayList<ButtonEventListener>();

	public ImageButton getButtonAdd() {
		return buttonAdd;
	}

	public void setButtonAdd(ImageButton buttonAdd) {
		this.buttonAdd = buttonAdd;
	}

	private ImageButton buttonAdd = null;

	public void setButtonEventListener(ButtonEventListener listener) {
		this.navigationbarListeners.add(listener);
	}

	public void removeNavigationbarListener(ButtonEventListener listener) {
		this.navigationbarListeners.remove(listener);


	}


	public void onCreate() {


		spinnerMode = (Spinner) mActivity.findViewById(R.id.naviagationbar_spinner_mode);


		arrayAdapterMode = new SpinnerModeAdapter(mActivity, new ArrayList<ScaleApplication>());
		spinnerMode.setAdapter(arrayAdapterMode);



		spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//ignore the first click on the	spinner item because this callback function has been called only because of initialization
				spinnerModeOnClickCounter++;
				ApplicationManager.getInstance().clearStatistics();
				if(spinnerModeOnClickCounter>1) {
					Scale.getInstance().setScaleApplication(arrayAdapterMode.getItem(position));

				}
					    Log.e("NavigationBar", "OnItemSelected ModeSpinner " + position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		if (prefs.getBoolean(mActivity.getString(R.string.preferences_weigh_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.WEIGHING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_counting_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.PART_COUNTING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_percent_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.PERCENT_WEIGHING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_check_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.CHECK_WEIGHING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_animal_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.ANIMAL_WEIGHING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_filling_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.FILLING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_totalization_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.TOTALIZATION);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_formulation_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.FORMULATION);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_differential_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.DIFFERENTIAL_WEIGHING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_density_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.DENSITY_DETERMINATION);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_peak_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.PEAK_HOLD);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_ingrediant_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.INGREDIENT_COSTING);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_pipette_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.PIPETTE_ADJUSTMENT_1_HOME);
		}if (prefs.getBoolean(mActivity.getString(R.string.preferences_statistic_activated),true)==true){
			getArrayAdapterMode().add(ScaleApplication.STATISTICAL_QUALITY_CONTROL_1_HOME);
		}
		    getArrayAdapterMode().add(ScaleApplication.ASH_DETERMINATION_1_HOME);

		int index = 0;
		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING: index = 0; break;

			case PART_COUNTING: index = 1; break;
			case PART_COUNTING_CALC_AWP: index=1; break;

			case PERCENT_WEIGHING: index = 2; break;
			case PERCENT_WEIGHING_CALC_REFERENCE: index=2; break;


			case CHECK_WEIGHING: index = 3; break;


			case ANIMAL_WEIGHING: index = 4; break;
			case ANIMAL_WEIGHING_CALCULATING: index=4; break;


			case FILLING: index = 5; break;
			case FILLING_CALC_TARGET: index=5; break;

			case TOTALIZATION: index = 6; break;

			case FORMULATION: index = 7; break;
			case FORMULATION_RUNNING: index=7;break;

			case DIFFERENTIAL_WEIGHING: index = 8; break;

			case DENSITY_DETERMINATION: index = 9; break;
			case DENSITY_DETERMINATION_STARTED: index=9; break;


			case PEAK_HOLD: index = 10; break;
			case PEAK_HOLD_STARTED: index= 10; break;

			case INGREDIENT_COSTING: index = 11; break;

			case PIPETTE_ADJUSTMENT_1_HOME: index = 12; break;
			case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES: index=12; break;
			case PIPETTE_ADJUSTMENT_3_FINISHED:index=12;break;



			case STATISTICAL_QUALITY_CONTROL_1_HOME: index = 13; break;
			case STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED:index=13;break;
			case STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED:index=13;break;

			case ASH_DETERMINATION_1_HOME:
			case ASH_DETERMINATION_2_BATCH_STARTED:
			case ASH_DETERMINATION_3_TARE_BEAKER:
			case ASH_DETERMINATION_4_WEIGHING_SAMPLE:
			case ASH_DETERMINATION_5_WAIT_FOR_GLOWING:
			case ASH_DETERMINATION_6_WEIGHING_GLOWED_SAMPLE:
			case ASH_DETERMINATION_7_CHECK_DELTA_WEIGHT:
			case ASH_DETERMINATION_8_BATCH_FINISHED:
				index = 14;
				break;


		}

		spinnerMode.setSelection(index);
		/*for(int i = 0; i< arrayAdapterMode.getCount();i++){
			if(Scale.getInstance().getScaleApplication() == arrayAdapterMode.getItem(i)){
				spinnerMode.setSelection(i);
			}
		}*/

		spinnerLib = (Spinner) mActivity.findViewById(R.id.naviagationbar_spinner_lib);

		arrayAdapterLibrary = new ArrayAdapter<String>(mActivity, R.layout.spinner, R.id.spinnerTarget, new ArrayList<String>());
		arrayAdapterLibrary.add("test");
		spinnerLib.setAdapter(arrayAdapterLibrary);

		spinnerLib.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					DatabaseService db = new DatabaseService(mActivity);
					//apply change
					for (Library library : db.getLibraries()) {
						if (library.getName().equals(arrayAdapterLibrary.getItem(position))) {
							ApplicationManager.getInstance().setCurrentLibrary(library);
						}
					}
					//notify listeners
					for (ButtonEventListener listener : navigationbarListeners) {
						listener.onClickNavigationbarButton(ActionButtonbarFragment.SPINNER_LIBRARY, false);
					}
				}catch (Exception e){
					Log.e("Navigationbar", e.toString());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}


		});

		buttonSettingsDevice = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_settings_device);
		buttonSettingsDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_SETTINGS_DEVICE, false);
				}
			}
		});

		buttonMore = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_more);
		buttonMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_MORE, false);
				}
			}
		});
		buttonMore.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				String url = "http://www.google.com";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				mActivity.startActivity(i);
				return false;
			}
		});
		
		buttonSettings = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_settings);
		buttonSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_SETTINGS, false);
				}

			}
		});

		buttonAdd = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_add);
		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_ADD, false);
				}
			}
		});

		buttonBack = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_back);
		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_BACK, false);
				}

				mActivity.finish();

			}
		});

		buttonCompanyLogo=(ImageButton) mActivity.findViewById(R.id.naviagationbar_button_company_logo);
		buttonCompanyLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});


		buttonDelete = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_delete);
		buttonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_DELETE, false);
				}


			}
		});


		buttonHome = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_home);
		buttonHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_HOME, false);
				}

			}
		});
		buttonHome.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_HOME, true);
				}
				return false;
			}
		});
		textTitle = (TextView) mActivity.findViewById(R.id.naviagationbar_text_title);
		textTitle.setVisibility(View.GONE);

		buttonLogout = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_logout);
		buttonLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_LOGOUT, false);
				}
			}
		});

		buttonSave = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_save);
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(ActionButtonbarFragment.BUTTON_SAVE, false);
				}
			}
		});
	}



}
