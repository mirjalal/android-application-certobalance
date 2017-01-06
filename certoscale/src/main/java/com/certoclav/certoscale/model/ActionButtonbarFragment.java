package com.certoclav.certoscale.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;

import java.util.ArrayList;


public class ActionButtonbarFragment extends Fragment {


	public ActionButtonbarFragment() {
	}

	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_SETTINGS = 2;
	public static final int BUTTON_TARA = 3;
	public static final int BUTTON_CAL = 4;
	public static final int BUTTON_PRINT = 5;
	public static final int BUTTON_APP_SETTINGS = 6;
	public static final int BUTTON_STATISTICS = 15;
	public static final int BUTTON_ACCUMULATE = 16;
	public static final int BUTTON_ANIMAL_START_MEASUREMENT=17;


	private Button buttonTara = null;
	private Button buttonCal = null;
	private Button buttonPrint= null;
	private Button buttonStatistics = null;
	private Button buttonAccumulate = null;
	private Button buttonAnimalStart = null;
	private Button buttonAppSettings = null;
	private ArrayList<ButtonEventListener> navigationbarListeners = new ArrayList<ButtonEventListener>();


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

	public Button getButtonAnimalStart() {
		return buttonAnimalStart;
	}

	public void getButtonAnimalStart(Button buttonAnimalStart) {
		this.buttonAnimalStart = buttonAnimalStart;
	}



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




public void setButtonEventListener (ButtonEventListener listener){
	this.navigationbarListeners.add(listener);
}

public void removeButtonEventListener(ButtonEventListener listener) {
	this.navigationbarListeners.remove(listener);


}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.actionbar,container, false);

		buttonPrint = (Button) rootView.findViewById(R.id.actionbar_button_print);
		buttonPrint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_PRINT,false);
				}

			}
		});


		buttonStatistics = (Button) rootView.findViewById(R.id.actionbar_button_statistics);
		buttonStatistics.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_STATISTICS,false);
				}

			}
		});

		buttonAccumulate = (Button) rootView.findViewById(R.id.actionbar_button_accumulate);
		buttonAccumulate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_ACCUMULATE,false);
				}

			}
		});
		buttonCal = (Button) rootView.findViewById(R.id.actionbar_button_cal);
		buttonCal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_CAL,false);
				}

			}
		});

		buttonTara = (Button) rootView.findViewById(R.id.actionbar_button_tara);
		buttonTara.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_TARA,false);
				}

			}
		});

		buttonAppSettings = (Button) rootView.findViewById(R.id.actionbar_button_settings);
		buttonAppSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_APP_SETTINGS,false);
				}

			}
		});


		return rootView;
	}
	
	



	public void disbalbeAllButtons(){
		buttonAppSettings.setEnabled(false);
		buttonCal.setEnabled(false);
		buttonPrint.setEnabled(false);
		buttonTara.setEnabled(false);
	}

}


