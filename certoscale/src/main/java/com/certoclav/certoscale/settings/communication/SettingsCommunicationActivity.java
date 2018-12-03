package com.certoclav.certoscale.settings.communication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.settings.application.PreferenceFragment;


public class SettingsCommunicationActivity extends FragmentActivity {

	public static String INTENT_EXTRA_SUBMENU = "submenu";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	PreferenceFragment preferenceFragment=null;

	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_unit_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText(getString(R.string.communication_menu).toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);

		navigationbar.getButtonBack().setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				String tag=getSupportFragmentManager().findFragmentById(R.id.settings_unit_container_fragment).getTag();

				if(tag!="MainFragment"){
					getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragment(),"MainFragment").commit();
				}else{
					finish();
				}
			}
		});



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
			getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsCommunicationFragment(),"MainFragment").commit();
		}catch(Exception e){

		}
	}




}
