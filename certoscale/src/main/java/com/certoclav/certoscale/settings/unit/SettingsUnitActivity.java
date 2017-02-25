package com.certoclav.certoscale.settings.unit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;

import static com.certoclav.certoscale.model.ActionButtonbarFragment.BUTTON_ADD;


public class SettingsUnitActivity extends FragmentActivity implements  ButtonEventListener

	{



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
		navigationbar.getButtonAdd().setVisibility(View.VISIBLE);
	}
	
	


	@Override
	protected void onPause() {
		super.onPause();
		navigationbar.removeNavigationbarListener(this);
	}



	@Override
	protected void onResume() {
		navigationbar.setButtonEventListener(this);
		getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsUnitFragment()).commit();


		super.onResume();

	}



		@Override
		public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
			switch (buttonId){
				case BUTTON_ADD:
					Intent intent = new Intent(SettingsUnitActivity.this, SettingsUnitEditActivity.class);
					startActivity(intent);
					break;
			}
		}
	}
