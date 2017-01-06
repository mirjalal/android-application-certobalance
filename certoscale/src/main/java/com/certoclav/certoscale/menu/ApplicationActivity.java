package com.certoclav.certoscale.menu;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.certoclav.certoscale.service.ReadAndParseSerialService;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.LabelPrinterUtils;
import com.certoclav.certoscale.util.ProtocolPrinterUtils;

import java.util.List;

import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING_CALC_TARGET;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING_CALC_AWP;
import static com.certoclav.certoscale.model.ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE;


public class ApplicationActivity extends FragmentActivity implements  ButtonEventListener ,ScaleApplicationListener, SharedPreferences.OnSharedPreferenceChangeListener{

private Navigationbar navigationbar = new Navigationbar(this);

private ActionButtonbar actionButtonbar = new ActionButtonbar();
	private boolean appSettingsVisible = false;



	@Override
protected void onResume() {

		refreshSpinnerLibrary();

		PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this).registerOnSharedPreferenceChangeListener(this);
		navigationbar.setButtonEventListener(this);
		navigationbar.getButtonHome().setVisibility(View.VISIBLE);
		navigationbar.getButtonSave().setVisibility(View.VISIBLE);
		navigationbar.getButtonSettings().setVisibility(View.VISIBLE);
		navigationbar.getButtonMore().setVisibility(View.VISIBLE);
		navigationbar.getSpinnerLib().setVisibility(View.VISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.VISIBLE);
		actionButtonbar.setButtonEventListener(this);

		Scale.getInstance().setOnApplicationListener(this);

		updateSpinnerMode();
		updateStatsButtonUI();



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


		getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_display, new ApplicationFragmentWeight()).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table,  new ApplicationFragmentTable()).commit();

	}



	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		Log.e("ApplicationActivity", "onclickhome");

		switch (buttonId){
			case Navigationbar.BUTTON_MORE:
				Toast.makeText(ApplicationActivity.this, "Calculator, Stopwatch, Voice control, Units",Toast.LENGTH_LONG).show();
				break;
			case Navigationbar.BUTTON_HOME:
				Intent intent = new Intent(ApplicationActivity.this,MenuActivity.class);
				startActivity(intent);
				break;
			case ActionButtonbar.BUTTON_TARA:
				ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
				break;
			case ActionButtonbar.BUTTON_STATISTICS:
				ApplicationManager.getInstance().showStatisticsNotification(ApplicationActivity.this, new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						updateStatsButtonUI();
					}
				});
				break;
			case ActionButtonbar.BUTTON_ACCUMULATE:
				ApplicationManager.getInstance().accumulateStatistics();
				updateStatsButtonUI();
				break;
			case ActionButtonbar.BUTTON_APP_SETTINGS:
				if(appSettingsVisible == true) {
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();
					actionButtonbar.getButtonAppSettings().setText("SETTINGS");
					actionButtonbar.getButtonCal().setEnabled(true);
					actionButtonbar.getButtonPrint().setEnabled(true);
					actionButtonbar.getButtonTara().setEnabled(true);
					updateStatsButtonUI();
					actionButtonbar.getButtonAccumulate().setEnabled(true);
					appSettingsVisible = false;
				}else{
					actionButtonbar.getButtonCal().setEnabled(false);
					actionButtonbar.getButtonPrint().setEnabled(false);
					updateStatsButtonUI();
					actionButtonbar.getButtonAccumulate().setEnabled(false);

					switch (Scale.getInstance().getScaleApplication()){
						case PART_COUNTING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPartCounting()).commit();
							actionButtonbar.getButtonAppSettings().setText("RETURN TO APPLICATION");
							appSettingsVisible = true;
							break;
						case WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsWeighing()).commit();
							actionButtonbar.getButtonAppSettings().setText("RETURN TO APPLICATION");
							appSettingsVisible = true;
							break;
						case PERCENT_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPercentWeighing()).commit();
							actionButtonbar.getButtonAppSettings().setText("RETURN TO APPLICATION");
							appSettingsVisible = true;
							break;
						case ANIMAL_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsAnimalWeighing()).commit();
							actionButtonbar.getButtonAppSettings().setText("RETURN TO APPLICATION");
							appSettingsVisible = true;
							break;
						case FILLING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsFilling()).commit();
							actionButtonbar.getButtonAppSettings().setText("RETURN TO APPLICATION");
							appSettingsVisible = true;
							break;
						default:
							Toast.makeText(this,"TODO: Implement Actions",Toast.LENGTH_SHORT).show();
					}



				}
				break;
			case ActionButtonbar.BUTTON_CAL:
				//send command for calibration to the scale
				if(Scale.getInstance().getWeightInGram() <= 5){

					ReadAndParseSerialService.getInstance().getCommandQueue().add("C\r\n");

					Intent intent3 = new Intent(ApplicationActivity.this,AnimationCalibrationActivity.class);
					startActivity(intent3);
				}else{
					Toast.makeText(ApplicationActivity.this, "Please remove item from pan first", Toast.LENGTH_LONG).show();
				}
				break;
			case ActionButtonbar.BUTTON_PRINT:
				Toast.makeText(ApplicationActivity.this, "Protool printed: ", Toast.LENGTH_LONG).show();
				Toast.makeText(ApplicationActivity.this, "Label printed: ", Toast.LENGTH_LONG).show();
				//Print whole protocol to protocol printer connected on COM 1
				ProtocolPrinterUtils.printProtocol();
				//Print current weight to label printer connected on COM 2
				LabelPrinterUtils.printText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit(),1);

				break;
			case Navigationbar.BUTTON_SETTINGS:
				Intent intent2 = new Intent(ApplicationActivity.this, SettingsActivity.class);
				intent2.putExtra(SettingsActivity.INTENT_EXTRA_SUBMENU, navigationbar.getSpinnerMode().getSelectedItemPosition());
				startActivity(intent2);
				break;
			case Navigationbar.BUTTON_SAVE:
				try{
					final Dialog dialog = new Dialog(ApplicationActivity.this);
					dialog.setContentView(R.layout.dialog_edit_text);
					dialog.setTitle("Please enter a name for the Partcounting library entry");

					Button dialogButtonCansel = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
					dialogButtonCansel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					Button dialogButtonSave = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
					// if button is clicked, close the custom dialog
					dialogButtonSave.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String name = ((EditText)dialog.findViewById(R.id.dialog_edit_text_edittext)).getText().toString();
							DatabaseService db = new DatabaseService(ApplicationActivity.this);
							ApplicationManager.getInstance().getCurrentLibrary().setName(name);
							int retval = db.insertLibrary(ApplicationManager.getInstance().getCurrentLibrary());
							if(retval == 1){
								Toast.makeText(ApplicationActivity.this,"Library " + name + " successfully saved" + retval,Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(ApplicationActivity.this,"Library could not be saved" + retval,Toast.LENGTH_LONG).show();
							}
							refreshSpinnerLibrary();
							try {
								navigationbar.getSpinnerLib().setSelection(navigationbar.getArrayAdapterLibrary().getPosition(name)); //buggy?
							}catch(Exception e){

							}
							dialog.dismiss();


						}
					});

					dialog.show();

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				break;

		}

	}

	@Override
	public void onApplicationChange(ScaleApplication application) {

		//do not react on subApplications
		if(application == PART_COUNTING_CALC_AWP ||
				application == PERCENT_WEIGHING_CALC_REFERENCE ||
				application == ANIMAL_WEIGHING_CALCULATING ||
				application == FILLING_CALC_TARGET){
			return;
		}
		ApplicationManager.getInstance().clearStatistics();
		if(application == ScaleApplication.ANIMAL_WEIGHING){
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentAnimalWeighing()).commit();
		}else {
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();
		}
		actionButtonbar.getButtonAppSettings().setText("SETTINGS");
		actionButtonbar.getButtonCal().setEnabled(true);
		actionButtonbar.getButtonPrint().setEnabled(true);
		actionButtonbar.getButtonTara().setEnabled(true);
		actionButtonbar.getButtonAccumulate().setEnabled(true);
		appSettingsVisible = false;

		//TOTALIZATION HAS NO SETTINGS BUTTON
		if(application == ScaleApplication.TOTALIZATION){
			actionButtonbar.getButtonAppSettings().setEnabled(false);
		}else{
			actionButtonbar.getButtonAppSettings().setEnabled(true);
		}

		updateStatsButtonUI();
		refreshSpinnerLibrary();
	}

	private void refreshSpinnerLibrary() {
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
			updateSpinnerMode();
	}




	private void updateSpinnerMode(){
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



	private void updateStatsButtonUI() {
		actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatistic().getN() + ")");
		if (ApplicationManager.getInstance().getStatistic().getN()==0){
			actionButtonbar.getButtonStatistics().setEnabled(false);
		}else {
			actionButtonbar.getButtonStatistics().setEnabled(true);
		}
	}

}
