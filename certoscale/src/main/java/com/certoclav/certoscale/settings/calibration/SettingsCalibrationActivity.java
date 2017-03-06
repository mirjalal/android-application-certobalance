package com.certoclav.certoscale.settings.calibration;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Navigationbar;


public class SettingsCalibrationActivity extends FragmentActivity {

	public static String INTENT_EXTRA_SUBMENU = "submenu";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_unit_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText(getString(R.string.calibration_menu).toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);
		


	}
	
	


	@Override
	protected void onPause() {

		super.onPause();
	}




	@Override
	protected void onResume() {

		super.onResume();
		int index = 0;
		try{
			index = getIntent().getIntExtra(INTENT_EXTRA_SUBMENU, 0);
		}catch(Exception e){
			
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCalibrationFragment()).commit();
	}




}
