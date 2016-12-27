package com.certoclav.certoscale.model;

import android.app.Activity;
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

	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_SETTINGS = 2;
	public static final int BUTTON_ADD = 13;
	public static final int BUTTON_BACK = 14;
	public static final int SPINNER_LIBRARY = 20;
	public static final int BUTTON_GO_TO_APPLICATION = 31;
	public static final int BUTTON_LOGOUT = 32;
	public static final int BUTTON_SAVE = 33;
	public static final int BUTTON_SETTINGS_DEVICE = 34;
	public static final int BUTTON_MORE = 35;
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

	public void setSpinnerMode(Spinner spinnerMode) {
		this.spinnerMode = spinnerMode;
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
/*
		ArrayList<ScaleApplication> scaleApplications = new ArrayList<ScaleApplication>();
		scaleApplications.add(ScaleApplication.WEIGHING);
		scaleApplications.add(ScaleApplication.PART_COUNTING);
		scaleApplications.add(ScaleApplication.PERCENT_WEIGHING);
		scaleApplications.add(ScaleApplication.CHECK_WEIGHING);
		scaleApplications.add(ScaleApplication.ANIMAL_WEIGHING);
		scaleApplications.add(ScaleApplication.FILLING);
		scaleApplications.add(ScaleApplication.TOTALIZATION);
		scaleApplications.add(ScaleApplication.FORMULATION);
		scaleApplications.add(ScaleApplication.DIFFERENTIAL_WEIGHING);
		scaleApplications.add(ScaleApplication.DENSITIY_DETERMINATION);
		scaleApplications.add(ScaleApplication.PEAK_HOLD);
		scaleApplications.add(ScaleApplication.INGREDIENT_COSTING);
		scaleApplications.add(ScaleApplication.PIPETTE_ADJUSTMENT);
		scaleApplications.add(ScaleApplication.STATISTICAL_QUALITY_CONTROL);
*/

		arrayAdapterMode = new SpinnerModeAdapter(mActivity, new ArrayList<ScaleApplication>());
		spinnerMode.setAdapter(arrayAdapterMode);
		//spinnerMode.setAdapter(spinnerModeAdapter);


		spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//ignore the first click on the	spinner item because this callback function has been called only because of initialization
				spinnerModeOnClickCounter++;
				if(spinnerModeOnClickCounter>1) {
					Scale.getInstance().setScaleApplication(arrayAdapterMode.getItem(position));
				}
					    Log.e("NavigationBar", "OnItemSelected ModeSpinner " + position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinnerMode.setSelection(Scale.getInstance().getScaleApplication().ordinal());

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
						listener.onClickNavigationbarButton(SPINNER_LIBRARY, false);
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
					listener.onClickNavigationbarButton(BUTTON_SETTINGS_DEVICE, false);
				}
			}
		});

		buttonMore = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_more);
		buttonMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_MORE, false);
				}
			}
		});
		
		buttonSettings = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_settings);
		buttonSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_SETTINGS, false);
				}

			}
		});

		buttonAdd = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_add);
		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_ADD, false);
				}
			}
		});

		buttonBack = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_back);
		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_BACK, false);
				}
				//if(navigationbarListeners.isEmpty()){
				mActivity.finish();
				//}
			}
		});

		buttonHome = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_home);
		buttonHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_HOME, false);
				}

			}
		});
		buttonHome.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_HOME, true);
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
					listener.onClickNavigationbarButton(BUTTON_LOGOUT, false);
				}
			}
		});

		buttonSave = (ImageButton) mActivity.findViewById(R.id.naviagationbar_button_save);
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ButtonEventListener listener : navigationbarListeners) {
					listener.onClickNavigationbarButton(BUTTON_SAVE, false);
				}
			}
		});
	}



}
