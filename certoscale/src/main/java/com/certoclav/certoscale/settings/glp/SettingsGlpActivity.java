package com.certoclav.certoscale.settings.glp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.settings.unit.SettingUnitFragment;


public class SettingsGlpActivity extends FragmentActivity implements  ButtonEventListener {

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
		navigationbar.getButtonHome().setText("BACK");
		navigationbar.getSpinnerLib().setVisibility(View.INVISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.INVISIBLE);
		navigationbar.getButtonSettings().setVisibility(View.GONE);
		navigationbar.getTextTitle().setText("GLP and GMP data".toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		


	}
	
	


	@Override
	protected void onPause() {
		navigationbar.removeNavigationbarListener(this);
		super.onPause();
	}




	@Override
	protected void onResume() {
		navigationbar.setButtonEventListener(this);
		super.onResume();
		int index = 0;
		try{
			index = getIntent().getIntExtra(INTENT_EXTRA_SUBMENU, 0);
		}catch(Exception e){
			
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, new SettingsGlpFragment()).commit();
	}





	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		if(buttonId == navigationbar.BUTTON_HOME){
			finish();
		}
		
	}
}
