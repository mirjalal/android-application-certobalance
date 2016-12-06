package com.certoclav.certoscale.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
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
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class SettingsActivity extends FragmentActivity implements ItemListFragment.Callbacks, ButtonEventListener {

	public static String INTENT_EXTRA_SUBMENU = "submenu";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity); //ItemListFragment, sont nix
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getButtonHome().setText("BACK");
		navigationbar.getSpinnerLib().setVisibility(View.INVISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.INVISIBLE);
		navigationbar.getButtonSettings().setVisibility(View.GONE);
		navigationbar.getTextTitle().setText("Settings");
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
		onItemSelected(index);
	}




	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(long id) {
		
		if(id == 0){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, new SettingsWeighing()).commit(); 	
		}else if(id == 1){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingsCounting()).commit();
		}else if(id == 2){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingsPercentWeighing()).commit();
		}else if(id == 3){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingsCheckWeighing()).commit();
		}else if(id == 4){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingAnimalWeighing()).commit();
		}else if(id == 5){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingFilling()).commit();
		}else if(id == 6){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingTotalization()).commit();
		}else if(id == 7){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingFormulation()).commit();
		}else if(id == 8){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingDifferentialWeighing()).commit();
		}else if(id == 9){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingDensityDetermination()).commit();
		}else if(id == 10){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingPeakHold()).commit();
		}else if(id == 11){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingIngredientCosting()).commit();
		}else if(id == 12){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingPipetteAdjustment()).commit();
		}else if(id == 13){
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,  new SettingStatisticalQualityControl()).commit();
		}




	}




	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		if(buttonId == navigationbar.BUTTON_HOME){
			finish();
		}
		
	}
}
