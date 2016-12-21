package com.certoclav.certoscale.menu;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.model.ActionButtonbar;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.LabelPrinterUtils;

import java.util.List;


public class ApplicationActivity extends FragmentActivity implements  ButtonEventListener ,ScaleApplicationListener, SharedPreferences.OnSharedPreferenceChangeListener{

private Navigationbar navigationbar = new Navigationbar(this);
private ActionButtonbar actionButtonbar = new ActionButtonbar(this);
	private boolean appSettingsVisible = false;



	@Override
protected void onResume() {


		PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this).registerOnSharedPreferenceChangeListener(this);
		navigationbar.setButtonEventListener(this);
		actionButtonbar.setButtonEventListener(this);
		navigationbar.getSpinnerLib().setVisibility(View.VISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.VISIBLE);
		Scale.getInstance().setOnApplicationListener(this);

		String[] applicationNamesArray = getResources().getStringArray(R.array.navigationbar_entries);

        navigationbar.getArrayAdapterMode().clear();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this);
		if (prefs.getBoolean(getString(R.string.preferences_weigh_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_counting_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PART_COUNTING);
		}if (prefs.getBoolean(getString(R.string.preferences_percent_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PERCENT_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_check_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.CHECK_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_animal_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.ANIMAL_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_filling_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.FILLING);
		}if (prefs.getBoolean(getString(R.string.preferences_totalization_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.TOTALIZATION);
		}if (prefs.getBoolean(getString(R.string.preferences_formulation_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.FORMULATION);
		}if (prefs.getBoolean(getString(R.string.preferences_differential_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.DIFFERENTIAL_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_density_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.DENSITIY_DETERMINATION);
		}if (prefs.getBoolean(getString(R.string.preferences_peak_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PEAK_HOLD);
		}if (prefs.getBoolean(getString(R.string.preferences_ingrediant_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.INGREDIENT_COSTING);
		}if (prefs.getBoolean(getString(R.string.preferences_pipette_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PIPETTE_ADJUSTMENT);
		}if (prefs.getBoolean(getString(R.string.preferences_statistic_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.STATISTICAL_QUALITY_CONTROL);
		}




		else{
			//do nothing
		}



		navigationbar.getArrayAdapterMode().notifyDataSetChanged();

		actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
		if (ApplicationManager.getInstance().getStatisticsArray().size()==0){
			actionButtonbar.getButtonStatistics().setEnabled(false);
		}else {
			actionButtonbar.getButtonStatistics().setEnabled(true);
		}

		super.onResume();
}



@Override
protected void onPause() {
	PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this).unregisterOnSharedPreferenceChangeListener(this);
	navigationbar.removeNavigationbarListener(this);
	actionButtonbar.removeButtonEventListener(this);
	Scale.getInstance().removeOnApplicationListener(this);
	super.onPause();
}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.menu_application_activity);
		super.onCreate(savedInstanceState);
		navigationbar.onCreate();
		actionButtonbar.onCreate();

		//start parse serial data output every second. TODO: This function call should be moved into a State Machine class
	    Scale.getInstance().getReadAndParseSerialService().startParseSerialThread();
		getSupportFragmentManager().beginTransaction().add(R.id.menu_application_container_display, new ApplicationFragmentWeight()).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.menu_application_container_table,  new ApplicationFragmentTable()).commit();

	}



	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		Log.e("ApplicationActivity", "onclickhome");
		if(buttonId == Navigationbar.BUTTON_HOME){
			Intent intent = new Intent(ApplicationActivity.this,MenuActivity.class);
			startActivity(intent);
		}
	
		if(buttonId == ActionButtonbar.BUTTON_TARA){
			ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
		}
		if(buttonId == ActionButtonbar.BUTTON_STATISTICS){
			ApplicationManager.getInstance().showStatisticsNotification(ApplicationActivity.this, new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
					if (ApplicationManager.getInstance().getStatisticsArray().size()==0){
						actionButtonbar.getButtonStatistics().setEnabled(false);
					}else {
						actionButtonbar.getButtonStatistics().setEnabled(true);
					}
				}
			});

		}
		if(buttonId == ActionButtonbar.BUTTON_ACCUMULATE){
			ApplicationManager.getInstance().accumulateStatistics();
			actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
			if (ApplicationManager.getInstance().getStatisticsArray().size()==0){
				actionButtonbar.getButtonStatistics().setEnabled(false);
			}else {
				actionButtonbar.getButtonStatistics().setEnabled(true);
			}
		}

		if(buttonId == ActionButtonbar.BUTTON_APP_SETTINGS){
			if(appSettingsVisible == true) {
				getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();
				actionButtonbar.getButtonAppSettings().setText("SETTINGS");
				actionButtonbar.getButtonCal().setEnabled(true);
				actionButtonbar.getButtonPrint().setEnabled(true);
				actionButtonbar.getButtonTara().setEnabled(true);
				actionButtonbar.getButtonStatistics().setEnabled(true);
				actionButtonbar.getButtonAccumulate().setEnabled(true);
				appSettingsVisible = false;
			}else{
				actionButtonbar.getButtonCal().setEnabled(false);
				actionButtonbar.getButtonPrint().setEnabled(false);
				actionButtonbar.getButtonStatistics().setEnabled(false);
				actionButtonbar.getButtonAccumulate().setEnabled(false);
				//actionButtonbar.getButtonTara().setEnabled(false);
				switch (Scale.getInstance().getScaleApplication()){
					case PART_COUNTING:
						getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPartCounting()).commit();
						actionButtonbar.getButtonAppSettings().setText("RESULTS");
						appSettingsVisible = true;
						break;
					case WEIGHING:
						getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsWeighing()).commit();
						actionButtonbar.getButtonAppSettings().setText("RESULTS");
						appSettingsVisible = true;
						break;
					case PERCENT_WEIGHING:
						getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPercentWeighing()).commit();
						actionButtonbar.getButtonAppSettings().setText("RESULTS");
						appSettingsVisible = true;
						break;
					default:
						Toast.makeText(this,"TODO: Implement Actions",Toast.LENGTH_SHORT).show();
				}



			}
		}


		if(buttonId == ActionButtonbar.BUTTON_CAL){
			//send command for calibration to the scale
			if(Scale.getInstance().getWeightInGram() <= 5){
			
				Scale.getInstance().getReadAndParseSerialService().sendCalibrationCommand();
				
				Intent intent = new Intent(ApplicationActivity.this,AnimationCalibrationActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(ApplicationActivity.this, "Please remove item from pan first", Toast.LENGTH_LONG).show();
			}
		}
		
		if(buttonId == ActionButtonbar.BUTTON_PRINT){
			Toast.makeText(ApplicationActivity.this, "Printed: "+ String.format("%.4f",Scale.getInstance().getWeightInGram()) + " g", Toast.LENGTH_LONG).show();
			LabelPrinterUtils.printText(""+ String.format("%.4f",Scale.getInstance().getWeightInGram()) + " g",1);
		}
		
		if(buttonId == Navigationbar.BUTTON_SETTINGS){
			Intent intent = new Intent(ApplicationActivity.this, SettingsActivity.class);
			intent.putExtra(SettingsActivity.INTENT_EXTRA_SUBMENU, navigationbar.getSpinnerMode().getSelectedItemPosition());
			startActivity(intent);
		}
		if(buttonId == Navigationbar.SPINNER_LIBRARY){

		}
	}

	@Override
	public void onApplicationChange(ScaleApplication application) {
		ApplicationManager.getInstance().clearStatistics();
		actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
		if (ApplicationManager.getInstance().getStatisticsArray().size()==0){
			actionButtonbar.getButtonStatistics().setEnabled(false);
		}else {
			actionButtonbar.getButtonStatistics().setEnabled(true);
		}
		try {
			DatabaseService db = new DatabaseService(ApplicationActivity.this);
			List<Library> libraries = db.getLibraries();
			navigationbar.getArrayAdapterLibrary().clear();
			if (libraries.size() == 0) {
				navigationbar.getArrayAdapterLibrary().add("empty");
			}
			for (Library library : libraries) {
				navigationbar.getArrayAdapterLibrary().add(library.getName());
			}
			navigationbar.getArrayAdapterLibrary().notifyDataSetChanged();
		}catch (Exception e){
			Log.e("ApplicationActivity", e.toString());
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.e("<Navigationbar", "INFO: CALLBACK FUNCTION HAS BEEN CALLED");
		navigationbar.getArrayAdapterMode().clear();
		String[] applicationNamesArray = getResources().getStringArray(R.array.navigationbar_entries);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this);
		if (prefs.getBoolean(getString(R.string.preferences_weigh_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_counting_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PART_COUNTING);
		}if (prefs.getBoolean(getString(R.string.preferences_percent_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PERCENT_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_check_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.CHECK_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_animal_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.ANIMAL_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_filling_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.FILLING);
		}if (prefs.getBoolean(getString(R.string.preferences_totalization_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.TOTALIZATION);
		}if (prefs.getBoolean(getString(R.string.preferences_formulation_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.FORMULATION);
		}if (prefs.getBoolean(getString(R.string.preferences_differential_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.DIFFERENTIAL_WEIGHING);
		}if (prefs.getBoolean(getString(R.string.preferences_density_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.DENSITIY_DETERMINATION);
		}if (prefs.getBoolean(getString(R.string.preferences_peak_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PEAK_HOLD);
		}if (prefs.getBoolean(getString(R.string.preferences_ingrediant_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.INGREDIENT_COSTING);
		}if (prefs.getBoolean(getString(R.string.preferences_pipette_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PIPETTE_ADJUSTMENT);
		}if (prefs.getBoolean(getString(R.string.preferences_statistic_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.STATISTICAL_QUALITY_CONTROL);
		}

		navigationbar.getArrayAdapterMode().notifyDataSetChanged();
	}

}
