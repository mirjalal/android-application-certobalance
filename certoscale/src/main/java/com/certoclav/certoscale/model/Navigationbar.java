package com.certoclav.certoscale.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.settings.SettingsActivity;

import java.util.ArrayList;


public class Navigationbar {

	public TextView getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(TextView textTitle) {
		this.textTitle = textTitle;
	}

	public Navigationbar(Activity activity){
		mActivity = activity;
		
	}

	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_SETTINGS = 2;
	
	private Button buttonHome = null;
	public Button getButtonHome() {
		return buttonHome;
	}

	public void setButtonHome(Button buttonHome) {
		this.buttonHome = buttonHome;
	}

	public Button getButtonSettings() {
		return buttonSettings;
	}

	public void setButtonSettings(Button buttonSettings) {
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

	private Button buttonSettings = null;
	private Activity mActivity = null;	
	private Spinner spinnerMode = null;
	private Spinner spinnerLib = null;
	private TextView textTitle = null;
	private ArrayList<ButtonEventListener> navigationbarListeners = new ArrayList<ButtonEventListener>();

public void setButtonEventListener (ButtonEventListener listener){
	this.navigationbarListeners.add(listener);
}

public void removeNavigationbarListener(ButtonEventListener listener) {
	this.navigationbarListeners.remove(listener);
	
	
}


public void onCreate(){
	
	spinnerMode= (Spinner) mActivity.findViewById(R.id.naviagationbar_spinner_mode);
	ArrayAdapter<CharSequence> spinnerModeAdapter = ArrayAdapter.createFromResource(
	            mActivity, R.array.navigationbar_entries, R.layout.spinner);
	spinnerModeAdapter.setDropDownViewResource(R.layout.spinner);
	spinnerMode.setAdapter(spinnerModeAdapter);

	spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			Log.e("Navigationbar", "setScaleAppl.: " + ScaleApplication.values()[position]);
			Scale.getInstance().setScaleApplication(ScaleApplication.values()[position]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	});

	
	spinnerLib= (Spinner) mActivity.findViewById(R.id.naviagationbar_spinner_lib);
	ArrayAdapter<CharSequence> spinnerLibAdapter = ArrayAdapter.createFromResource(
	            mActivity, R.array.navigationbar_bib_entries, R.layout.spinner);
	spinnerLibAdapter.setDropDownViewResource(R.layout.spinner);
	spinnerLib.setAdapter(spinnerLibAdapter);
	buttonSettings = (Button) mActivity.findViewById(R.id.naviagationbar_button_settings);
	buttonSettings.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mActivity, SettingsActivity.class);
			mActivity.startActivity(intent);
			
		}
	});
	
	buttonHome = (Button) mActivity.findViewById(R.id.naviagationbar_button_home);
	buttonHome.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_HOME,false);
			}
			
		}
	});
	buttonHome.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_HOME,true);
			}
			return false;
		}
	});
	textTitle = (TextView) mActivity.findViewById(R.id.naviagationbar_text_title);
	textTitle.setVisibility(View.GONE);
}


}