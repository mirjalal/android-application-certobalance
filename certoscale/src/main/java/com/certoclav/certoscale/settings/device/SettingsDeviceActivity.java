package com.certoclav.certoscale.settings.device;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SettingsUserListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link SettingsUserListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class SettingsDeviceActivity extends FragmentActivity implements  ButtonEventListener {

	public static String INTENT_EXTRA_SUBMENU = "submenu";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_device_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText(getString(R.string.balance_setup).toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);
		


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
		getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, new SettingDevice()).commit();
	}





	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		if(buttonId == ActionButtonbarFragment.BUTTON_HOME){
			finish();
		}
		
	}
}
