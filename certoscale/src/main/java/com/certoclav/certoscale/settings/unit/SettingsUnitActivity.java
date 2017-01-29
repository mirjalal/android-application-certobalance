package com.certoclav.certoscale.settings.unit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Navigationbar;


public class SettingsUnitActivity extends FragmentActivity {


	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_unit_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText("Weighing Units".toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);


	}
	
	


	@Override
	protected void onPause() {
		super.onPause();
	}




	@Override
	protected void onResume() {
		getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsUnitFragment()).commit();
		super.onResume();

	}



}
