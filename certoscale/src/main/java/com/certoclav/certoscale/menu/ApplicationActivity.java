package com.certoclav.certoscale.menu;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.supervisor.ProtocolManager;
import com.certoclav.library.application.ApplicationController;

import java.util.List;

import static com.certoclav.certoscale.constants.AppConstants.INTERNAL_TARA_ZERO_BUTTON;
import static com.certoclav.certoscale.constants.AppConstants.IS_IO_SIMULATED;
import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_ENTER_NAME_SAMPLE;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_ENTER_NAME_BEAKER;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_WEIGH_BEAKER;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_WEIGHING_SAMPLE;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_WAIT_FOR_GLOWING;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_WEIGHING_GLOWED_SAMPLE;
import static com.certoclav.certoscale.model.ScaleApplication.ASH_DETERMINATION_CHECK_DELTA_WEIGHT;
import static com.certoclav.certoscale.model.ScaleApplication.CHECK_WEIGHING;
import static com.certoclav.certoscale.model.ScaleApplication.DENSITY_DETERMINATION;
import static com.certoclav.certoscale.model.ScaleApplication.DENSITY_DETERMINATION_STARTED;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING_CALC_TARGET;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_FREE_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING_CALC_AWP;
import static com.certoclav.certoscale.model.ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE;
import static com.certoclav.certoscale.model.ScaleApplication.PIPETTE_ADJUSTMENT_1_HOME;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL_1_HOME;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED;
import static com.certoclav.certoscale.model.ScaleApplication.TOTALIZATION;
import static com.certoclav.certoscale.model.ScaleApplication.WEIGHING;


public class ApplicationActivity extends FragmentActivity implements  ButtonEventListener ,ScaleApplicationListener, SharedPreferences.OnSharedPreferenceChangeListener,StableListener{

private Navigationbar navigationbar = new Navigationbar(this);
private ProtocolManager protocolPrinter= new ProtocolManager();


	public ActionButtonbarFragment actionButtonbarFragment = null;
	private boolean appSettingsVisible = false;
	private LinearLayout containerMore = null;
	private ImageButton imageButtonSidebarBack = null;
	private ImageButton imageButtonTimer = null;
	private ImageButton imageButtonCalculator = null;
	private ImageButton imageButtonVideo = null;
	private ImageButton imageButtonCalibration = null;

	private Toast toast=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setComponent(new ComponentName("com.estrongs.android.pop","com.estrongs.android.pop.ftp.ESFtpShortcut"));
		startActivity(intent);

		setContentView(R.layout.menu_application_activity);

		navigationbar.onCreate();
	    actionButtonbarFragment = new ActionButtonbarFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_actionbar, actionButtonbarFragment).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_display, new ApplicationFragmentWeight()).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table,  new ApplicationFragmentTable()).commit();

		containerMore = (LinearLayout) findViewById(R.id.menu_application_container_more);

		imageButtonCalculator = (ImageButton) findViewById(R.id.menu_application_sidebar_button_calculator);
		imageButtonCalculator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.moblynx.calculatorjb","com.android.calculator2.Calculator"));
				startActivity(intent);
				imageButtonSidebarBack.performClick();
			}
		});


		imageButtonVideo = (ImageButton) findViewById(R.id.menu_application_sidebar_button_video);
		imageButtonVideo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageButtonSidebarBack.performClick();
				Intent intent = new Intent(ApplicationActivity.this, VideoActivity.class);
				startActivity(intent);
			}
		});

		imageButtonCalibration = (ImageButton) findViewById(R.id.menu_application_sidebar_button_calibration);
		imageButtonCalibration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this);
 				if(prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_calibration), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_calibration))==true) {
					Toast.makeText(ApplicationActivity.this, R.string.the_calibration_has_been_locked_by_the_admin, Toast.LENGTH_SHORT).show();
				}else{
					Scale.getInstance().getScaleModel().externelCalibration(ApplicationActivity.this);
					imageButtonSidebarBack.performClick();
				}



			}
		});
		imageButtonTimer = (ImageButton) findViewById(R.id.menu_application_sidebar_button_timer);
		imageButtonTimer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("org.ilumbo.ovo","org.ilumbo.ovo.TimerActivity"));
				startActivity(intent);
				imageButtonSidebarBack.performClick();
			}
		});
		imageButtonSidebarBack = (ImageButton) findViewById(R.id.menu_application_sidebar_button_back);
		imageButtonSidebarBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(ApplicationActivity.this, R.anim.righttoleft);
				hyperspaceJumpAnimation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						containerMore.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				containerMore.startAnimation(hyperspaceJumpAnimation);

			}
		});

	}



	@Override
protected void onResume() {
		Scale.getInstance().setOnStableListener(this);





		refreshSpinnerLibrary();

		if(Scale.getInstance().getScaleApplication()==DENSITY_DETERMINATION_STARTED){
			Scale.getInstance().setScaleApplication(DENSITY_DETERMINATION);
			ApplicationManager.getInstance().setDensity_step_counter(0);
		}

		PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this).registerOnSharedPreferenceChangeListener(this);
		navigationbar.setButtonEventListener(this);
		navigationbar.getButtonHome().setVisibility(View.VISIBLE);
		navigationbar.getButtonSave().setVisibility(View.VISIBLE);
		navigationbar.getButtonSettings().setVisibility(View.VISIBLE);
		navigationbar.getButtonMore().setVisibility(View.VISIBLE);
		navigationbar.getSpinnerLib().setVisibility(View.VISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.VISIBLE);

		actionButtonbarFragment.setButtonEventListener(this);
		Scale.getInstance().setOnApplicationListener(this);

		updateSpinnerMode();

		actionButtonbarFragment.updateStatsButtonUI();


		super.onResume();
}



	@Override
protected void onPause() {
	PreferenceManager.getDefaultSharedPreferences(ApplicationActivity.this).unregisterOnSharedPreferenceChangeListener(this);
	navigationbar.removeNavigationbarListener(this);
	actionButtonbarFragment.removeButtonEventListener(this);
	Scale.getInstance().removeOnApplicationListener(this);
		Scale.getInstance().removeOnStableListener(this);
	super.onPause();
}





//MANAGING CONTENT OF TABLE FRAGMENT
	@SuppressLint("NewApi")
	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		Log.e("ApplicationActivity", "onclickhome");

		switch (buttonId){

			case ActionButtonbarFragment.BUTTON_MEASUREMENT_EXISTING:

				break;

			case ActionButtonbarFragment.BUTTON_MEASUREMENT_NEW:
				if(Scale.getInstance().getScaleApplication() == ScaleApplication.ASH_DETERMINATION_ENTER_NAME_SAMPLE) {

					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentAshDetermination()).commit();

				}
				break;

			case ActionButtonbarFragment.BUTTON_START:
				if(Scale.getInstance().getScaleApplication() == ScaleApplication.ANIMAL_WEIGHING) {
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentAnimalWeighing()).commit();
				}
				if(Scale.getInstance().getScaleApplication() == ScaleApplication.FORMULATION) {

						getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentFormulation()).commit();

				}

				if(Scale.getInstance().getScaleApplication() != ScaleApplication.ASH_DETERMINATION_HOME && Scale.getInstance().getScaleApplication() != ScaleApplication.ASH_DETERMINATION_BATCH_FINISHED) {

					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentAshDetermination()).commit();

				}

				if(Scale.getInstance().getScaleApplication() == ScaleApplication.FORMULATION_FREE){
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentFormulationFree()).commit();

				}


				break;
			case ActionButtonbarFragment.BUTTON_MORE:

				if(containerMore.getVisibility() == View.GONE){
					//funktioniert super
					containerMore.setVisibility(View.VISIBLE);
					Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.lefttoright);
					containerMore.startAnimation(hyperspaceJumpAnimation);


				}else{

					Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
					hyperspaceJumpAnimation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							containerMore.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					containerMore.startAnimation(hyperspaceJumpAnimation);
				}
				break;
			case ActionButtonbarFragment.BUTTON_HOME:
				Intent intent = new Intent(ApplicationActivity.this,MenuActivity.class);
				startActivity(intent);
				break;
			case ActionButtonbarFragment.BUTTON_ZERO:
				if (IS_IO_SIMULATED || INTERNAL_TARA_ZERO_BUTTON) {
					ApplicationManager.getInstance().setTareInGram(0d);
				}else{
					Scale.getInstance().getScaleModel().pressZero();
				}
				break;

			case ActionButtonbarFragment.BUTTON_TARA:
				if (IS_IO_SIMULATED || INTERNAL_TARA_ZERO_BUTTON) {
					ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
				}else{
					ApplicationManager.getInstance().setTareInGram(ApplicationManager.getInstance().getTareInGram()+ApplicationManager.getInstance().getTaredValueInGram());
					Scale.getInstance().getScaleModel().pressTara();
				}
					break;
			case ActionButtonbarFragment.BUTTON_STATISTICS:


				break;

			case ActionButtonbarFragment.BUTTON_INGREDIANTLIST:

				break;
			case ActionButtonbarFragment.BUTTON_SHOWBATCH:





				break;
			case ActionButtonbarFragment.BUTTON_ACCUMULATE:
				if (Scale.getInstance().isStable()) {
					ApplicationManager.getInstance().accumulateStatistics();
					actionButtonbarFragment.updateStatsButtonUI();
				}else {


					//public void showAToast (String message){
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(this, getString(R.string.wait_until_the_weight_is_stable), Toast.LENGTH_SHORT);
					toast.show();
					//}

					//Toast.makeText(this, getString(R.string.wait_until_the_weight_is_stable), Toast.LENGTH_SHORT).show();
				}
				break;
			case ActionButtonbarFragment.BUTTON_APP_SETTINGS:
				if(appSettingsVisible == true) {
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();
					appSettingsVisible = false;

				}else{


					switch (Scale.getInstance().getScaleApplication()){
						case PART_COUNTING:
						case PART_COUNTING_CALC_AWP:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPartCounting()).commit();
							break;
						case CHECK_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsCheckWeighing()).commit();
							break;
						case WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsWeighing()).commit();
							break;
						case PERCENT_WEIGHING:
						case PERCENT_WEIGHING_CALC_REFERENCE:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPercentWeighing()).commit();
							break;
						case ANIMAL_WEIGHING:
						case ANIMAL_WEIGHING_CALCULATING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsAnimalWeighing()).commit();
							break;
						case FILLING:
						case FILLING_CALC_TARGET:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsFilling()).commit();
							break;
						case FORMULATION:
						case FORMULATION_RUNNING:
						case FORMULATION_FREE:
						case FORMULATION_FREE_RUNNING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsFormulation()).commit();
							break;
						case DIFFERENTIAL_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsDifferentialWeighing()).commit();
							break;
						case DENSITY_DETERMINATION:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsDensityDetermination()).commit();
							break;
						case PEAK_HOLD:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPeakHold()).commit();
							break;
						case INGREDIENT_COSTING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsIngrediantCosting()).commit();
							break;
						case STATISTICAL_QUALITY_CONTROL_1_HOME:
						case STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED:
						case STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsSQC()).commit();
							break;
						case PIPETTE_ADJUSTMENT_1_HOME:
						case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:
						case PIPETTE_ADJUSTMENT_3_FINISHED:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPipetteAdjustment()).commit();
							break;
						case ASH_DETERMINATION_HOME:
						case ASH_DETERMINATION_ENTER_NAME_SAMPLE:
						case ASH_DETERMINATION_WEIGH_BEAKER:
						case ASH_DETERMINATION_WEIGHING_SAMPLE:
						case ASH_DETERMINATION_WAIT_FOR_GLOWING:
						case ASH_DETERMINATION_WEIGHING_GLOWED_SAMPLE:
						case ASH_DETERMINATION_CHECK_DELTA_WEIGHT:
						case ASH_DETERMINATION_BATCH_FINISHED:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsAshDetermination()).commit();
						break;
						default:
							Toast.makeText(this,"TODO: Implement Actions",Toast.LENGTH_SHORT).show();
					}


					appSettingsVisible = true;



				}
				break;
			case ActionButtonbarFragment.BUTTON_CAL:
				//send command for calibration to the scale


				break;
			case ActionButtonbarFragment.BUTTON_PROTOCOL:
				//Print whole protocol to protocol printer connected on COM 1

				//Print GLP and GMP Data which is independent of the application
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);



				break;
			case ActionButtonbarFragment.BUTTON_SETTINGS:
				Intent intent2 = new Intent(ApplicationActivity.this, SettingsActivity.class);
				intent2.putExtra(SettingsActivity.INTENT_EXTRA_SUBMENU,Scale.getInstance().getScaleApplication().ordinal());
				startActivity(intent2);

				break;
			case ActionButtonbarFragment.BUTTON_SAVE:
				try{
					final Dialog dialog = new Dialog(ApplicationActivity.this);
					dialog.setContentView(R.layout.dialog_edit_text);
					dialog.setTitle(getString(R.string.please_enter_a_name_for_the_library));

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

							if (name.length()==0){
								Toast.makeText(ApplicationActivity.this, getString(R.string.please_enter_a_name_for_the_library), Toast.LENGTH_LONG).show();

							}else{
								ApplicationManager.getInstance().getCurrentLibrary().setName(name);
								int retval = db.insertLibrary(ApplicationManager.getInstance().getCurrentLibrary());
								if (retval == 1) {
									Toast.makeText(ApplicationActivity.this,getString(R.string.library) + " "+ name+ " " + getString(R.string.saved) , Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(ApplicationActivity.this, getString(R.string.library_could_not_be_saved), Toast.LENGTH_LONG).show();
								}
								refreshSpinnerLibrary();
								try {
									navigationbar.getSpinnerLib().setSelection(navigationbar.getArrayAdapterLibrary().getPosition(name)); //buggy?
								} catch (Exception e) {

								}
								dialog.dismiss();
							}


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
				application == FILLING_CALC_TARGET ||
				application == FORMULATION_RUNNING ||
				application == FORMULATION_FREE_RUNNING ||
				application == ASH_DETERMINATION_ENTER_NAME_SAMPLE ||
				application == ASH_DETERMINATION_ENTER_NAME_BEAKER ||
				application == ASH_DETERMINATION_WEIGH_BEAKER ||
				application == ASH_DETERMINATION_WEIGHING_SAMPLE ||
				application == ASH_DETERMINATION_WAIT_FOR_GLOWING ||
				application == ASH_DETERMINATION_WEIGHING_GLOWED_SAMPLE ||
				application == ASH_DETERMINATION_CHECK_DELTA_WEIGHT){
			return;
		}

		appSettingsVisible = false;
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();


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
			navigationbar.getArrayAdapterMode().add(ScaleApplication.DENSITY_DETERMINATION);
		}if (prefs.getBoolean(getString(R.string.preferences_peak_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PEAK_HOLD);
		}if (prefs.getBoolean(getString(R.string.preferences_ingrediant_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.INGREDIENT_COSTING);
		}if (prefs.getBoolean(getString(R.string.preferences_pipette_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.PIPETTE_ADJUSTMENT_1_HOME);
		}if (prefs.getBoolean(getString(R.string.preferences_statistic_activated),true)==true){
			navigationbar.getArrayAdapterMode().add(ScaleApplication.STATISTICAL_QUALITY_CONTROL_1_HOME);
		}
		navigationbar.getArrayAdapterMode().add(ScaleApplication.ASH_DETERMINATION_HOME);

		navigationbar.getArrayAdapterMode().notifyDataSetChanged();
		try {
			Scale.getInstance().setScaleApplication(navigationbar.getArrayAdapterMode().getItem(navigationbar.getSpinnerMode().getSelectedItemPosition()));
		}catch (Exception e){

		}
	}


	public void updateUI(){
		actionButtonbarFragment.updateStatsButtonUI();
	}




	@Override
	public void onStableChanged(boolean isStable) {

		Log.e("ApplicationActivity", "onStableChanged");

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


		if (isStable==true) {
			if (actionButtonbarFragment.getButtonAccumulate().isEnabled()==true) {
				if (Scale.getInstance().getScaleApplication() == TOTALIZATION) {
					if (prefs.getBoolean(getString(R.string.preferences_totalization_AutoSampleMode), getResources().getBoolean(R.bool.preferences_totalization_AutoSampleMode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}

				if (Scale.getInstance().getScaleApplication() == PIPETTE_ADJUSTMENT_1_HOME) {
					if (prefs.getBoolean(getString(R.string.preferences_pipette_autosamplemode), getResources().getBoolean(R.bool.preferences_pipette_autosamplemode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}

				if (Scale.getInstance().getScaleApplication() == STATISTICAL_QUALITY_CONTROL_1_HOME || Scale.getInstance().getScaleApplication() == STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED) {
					if (prefs.getBoolean(getString(R.string.preferences_statistic_mode), getResources().getBoolean(R.bool.preferences_statistic_mode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}
				if (Scale.getInstance().getScaleApplication() == WEIGHING) {
					if (prefs.getBoolean(getString(R.string.preferences_weigh_auto_mode), getResources().getBoolean(R.bool.preferences_weigh_auto_mode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}

				if (Scale.getInstance().getScaleApplication() == PART_COUNTING) {
					if (prefs.getBoolean(getString(R.string.preferences_counting_auto_mode), getResources().getBoolean(R.bool.preferences_counting_auto_mode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}

				if (Scale.getInstance().getScaleApplication() == CHECK_WEIGHING) {
					if (prefs.getBoolean(getString(R.string.preferences_check_auto_mode), getResources().getBoolean(R.bool.preferences_check_auto_mode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}
				if (Scale.getInstance().getScaleApplication() == FILLING) {
					if (prefs.getBoolean(getString(R.string.preferences_filling_auto_mode), getResources().getBoolean(R.bool.preferences_filling_auto_mode)) == true) {
						actionButtonbarFragment.getButtonAccumulate().performClick();
					}
				}
			}

		}

	}
}
