package com.certoclav.certoscale.model;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;



public class ActionButtonbar {

	public ActionButtonbar(Activity activity){
		mActivity = activity;
		
	}

	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_SETTINGS = 2;
	public static final int BUTTON_TARA = 3;
	public static final int BUTTON_CAL = 4;
	public static final int BUTTON_PRINT = 5;
	
	private Button buttonTara = null;
	private Button buttonCal = null;
	private Button buttonPrint= null;
	private Activity mActivity = null;	
	private Spinner spinnerMode = null;
	private Spinner spinnerLib = null;
	private ArrayList<ButtonEventListener> navigationbarListeners = new ArrayList<ButtonEventListener>();

public void setButtonEventListener (ButtonEventListener listener){
	this.navigationbarListeners.add(listener);
}

public void removeButtonEventListener(ButtonEventListener listener) {
	this.navigationbarListeners.remove(listener);
	
	
}


public void onCreate(){
	
	buttonPrint = (Button) mActivity.findViewById(R.id.actionbar_button_middle);
	buttonPrint.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_PRINT,false);
			}
			
		}
	});
	
	buttonCal = (Button) mActivity.findViewById(R.id.actionbar_button_right);
	buttonCal.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_CAL,false);
			}
			
		}
	});
	
	buttonTara = (Button) mActivity.findViewById(R.id.actionbar_button_left);
	buttonTara.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_TARA,false);
			}
			
		}
	});
}


}