package com.certoclav.certoscale.model;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;

import java.util.ArrayList;


public class ActionButtonbar {

	public ActionButtonbar(Activity activity){
		mActivity = activity;
		
	}

	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_SETTINGS = 2;
	public static final int BUTTON_TARA = 3;
	public static final int BUTTON_CAL = 4;
	public static final int BUTTON_PRINT = 5;
	public static final int BUTTON_APP_SETTINGS = 6;
	public static final int BUTTON_STATISTICS = 15;
	public static final int BUTTON_ACCUMULATE = 16;
	
	private Button buttonTara = null;
	private Button buttonCal = null;
	private Button buttonPrint= null;



	public Button getButtonStatistics() {
		return buttonStatistics;
	}

	public void setButtonStatistics(Button buttonStatistics) {
		this.buttonStatistics = buttonStatistics;
	}

	public Button getButtonAccumulate() {
		return buttonAccumulate;
	}

	public void setButtonAccumulate(Button buttonAccumulate) {
		this.buttonAccumulate = buttonAccumulate;
	}

	private Button buttonStatistics = null;
	private Button buttonAccumulate = null;

	public Button getButtonTara() {
		return buttonTara;
	}

	public void setButtonTara(Button buttonTara) {
		this.buttonTara = buttonTara;
	}

	public Button getButtonCal() {
		return buttonCal;
	}

	public void setButtonCal(Button buttonCal) {
		this.buttonCal = buttonCal;
	}

	public Button getButtonPrint() {
		return buttonPrint;
	}

	public void setButtonPrint(Button buttonPrint) {
		this.buttonPrint = buttonPrint;
	}

	public Button getButtonAppSettings() {
		return buttonAppSettings;
	}

	public void setButtonAppSettings(Button buttonAppSettings) {
		this.buttonAppSettings = buttonAppSettings;
	}


	private Button buttonAppSettings = null;
	private Activity mActivity = null;
	private ArrayList<ButtonEventListener> navigationbarListeners = new ArrayList<ButtonEventListener>();

public void setButtonEventListener (ButtonEventListener listener){
	this.navigationbarListeners.add(listener);
}

public void removeButtonEventListener(ButtonEventListener listener) {
	this.navigationbarListeners.remove(listener);
	
	
}


public void onCreate(){


	buttonPrint = (Button) mActivity.findViewById(R.id.actionbar_button_print);
	buttonPrint.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_PRINT,false);
			}
			
		}
	});


	buttonStatistics = (Button) mActivity.findViewById(R.id.actionbar_button_statistics);
	buttonStatistics.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_STATISTICS,false);
			}

		}
	});

	buttonAccumulate = (Button) mActivity.findViewById(R.id.actionbar_button_accumulate);
	buttonAccumulate.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_ACCUMULATE,false);
			}

		}
	});
	buttonCal = (Button) mActivity.findViewById(R.id.actionbar_button_cal);
	buttonCal.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_CAL,false);
			}
			
		}
	});
	
	buttonTara = (Button) mActivity.findViewById(R.id.actionbar_button_tara);
	buttonTara.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_TARA,false);
			}
			
		}
	});

	buttonAppSettings = (Button) mActivity.findViewById(R.id.actionbar_button_settings);
	buttonAppSettings.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_APP_SETTINGS,false);
			}

		}
	});



}

	public void hideAllButtons(){
		buttonAppSettings.setVisibility(View.GONE);
		buttonCal.setVisibility(View.GONE);
		buttonPrint.setVisibility(View.GONE);
		buttonTara.setVisibility(View.GONE);
	}

}