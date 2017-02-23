package com.certoclav.certoscale.settings.application;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends FragmentActivity implements ItemListFragment.Callbacks, ButtonEventListener {

	public static String INTENT_EXTRA_SUBMENU = "submenu";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
    private List<String> settingsEntriesList = null;
	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity); //ItemListFragment, sont nix
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		//navigationbar.getButtonHome().setText("BACK");
		navigationbar.getSpinnerLib().setVisibility(View.INVISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.INVISIBLE);
		navigationbar.getTextTitle().setText("Application settings".toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);
		settingsEntriesList = new ArrayList<String>();
		settingsEntriesList.addAll(Arrays.asList(getResources().getStringArray(R.array.navigationbar_entries)));


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
		//always open settings of current app
		int index = 0;
		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING: index = 0; break;
			case PART_COUNTING: index = 1; break;
			case PERCENT_WEIGHING: index = 2; break;
			case CHECK_WEIGHING: index = 3; break;
			case ANIMAL_WEIGHING: index = 4; break;
			case FILLING: index = 5; break;
			case TOTALIZATION: index = 6; break;
			case FORMULATION: index = 7; break;
			case DIFFERENTIAL_WEIGHING: index = 8; break;
			case DENSITY_DETERMINATION: index = 9; break;
			case PEAK_HOLD: index = 10; break;
			case INGREDIENT_COSTING: index = 11; break;
			case PIPETTE_ADJUSTMENT: index = 12; break;
			case STATISTICAL_QUALITY_CONTROL_1_HOME: index = 13; break;
		}
		onItemSelected(index);
	}




	@Override
	public void onItemSelected(long id) {
		try {
			navigationbar.getTextTitle().setText(settingsEntriesList.get((int) id).toUpperCase() + " SETTINGS");
		}catch (Exception e){

		}
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
		if(buttonId == ActionButtonbarFragment.BUTTON_HOME){
			finish();
		}
		
	}
}
