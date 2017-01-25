package com.certoclav.certoscale.menu;


import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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
import com.certoclav.certoscale.service.ReadAndParseSerialService;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.LabelPrinterUtils;
import com.certoclav.certoscale.util.ProtocolPrinterUtils;

import java.util.List;

import static com.certoclav.certoscale.R.string.preferences_totalization_AutoSampleMode;
import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING_CALC_TARGET;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING_CALC_AWP;
import static com.certoclav.certoscale.model.ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE;
import static com.certoclav.certoscale.model.ScaleApplication.PIPETTE_ADJUSTMENT;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL;
import static com.certoclav.certoscale.model.ScaleApplication.TOTALIZATION;
import static com.certoclav.certoscale.util.ProtocolPrinterUtils.*;


public class ApplicationActivity extends FragmentActivity implements  ButtonEventListener ,ScaleApplicationListener, SharedPreferences.OnSharedPreferenceChangeListener,StableListener{

private Navigationbar navigationbar = new Navigationbar(this);


	private ActionButtonbarFragment actionButtonbarFragment = null;
	private boolean appSettingsVisible = false;
	private LinearLayout containerMore = null;
	private ImageButton imageButtonSidebarBack = null;
	private ImageButton imageButtonTimer = null;
	private ImageButton imageButtonCalculator = null;
	private ImageButton imageButtonCalibration = null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.menu_application_activity);
		super.onCreate(savedInstanceState);
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
			}
		});

		imageButtonCalibration = (ImageButton) findViewById(R.id.menu_application_sidebar_button_calibration);
		imageButtonCalibration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ReadAndParseSerialService.getInstance().getCommandQueue().add("C\r\n");
			}
		});
		imageButtonTimer = (ImageButton) findViewById(R.id.menu_application_sidebar_button_timer);
		imageButtonTimer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("org.ilumbo.ovo","org.ilumbo.ovo.TimerActivity"));
				startActivity(intent);
			}
		});
		imageButtonSidebarBack = (ImageButton) findViewById(R.id.menu_application_sidebar_button_back);
		imageButtonSidebarBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


					containerMore.setVisibility(View.GONE);

			}
		});

	}



	@Override
protected void onResume() {
		Scale.getInstance().setOnStableListener(this);

		refreshSpinnerLibrary();

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

		if(Scale.getInstance().getScaleApplication() == ScaleApplication.FORMULATION) {
			if (Scale.getInstance().getCurrentRecipe() != null) {
				actionButtonbarFragment.getButtonStart().setEnabled(true);
			} else {
				actionButtonbarFragment.getButtonStart().setEnabled(false);
			}
		}

		if(appSettingsVisible){
			actionButtonbarFragment.getButtonStart().setEnabled(false);
		}

		if(appSettingsVisible) {
			actionButtonbarFragment.getButtonAppSettings().performClick();
		}else{
			actionButtonbarFragment.getButtonAppSettings().setEnabled(true);
		}
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







	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		Log.e("ApplicationActivity", "onclickhome");

		switch (buttonId){
			case ActionButtonbarFragment.BUTTON_START:
				if(Scale.getInstance().getScaleApplication() == ScaleApplication.ANIMAL_WEIGHING) {
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentAnimalWeighing()).commit();
				}
				if(Scale.getInstance().getScaleApplication() == ScaleApplication.FORMULATION) {
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentFormulation()).commit();
				}
				break;
			case ActionButtonbarFragment.BUTTON_MORE:

				if(containerMore.getVisibility() == View.GONE){
					containerMore.setVisibility(View.VISIBLE);
				}else{
					containerMore.setVisibility(View.GONE);
				}
				break;
			case ActionButtonbarFragment.BUTTON_HOME:
				Intent intent = new Intent(ApplicationActivity.this,MenuActivity.class);
				startActivity(intent);
				break;
			case ActionButtonbarFragment.BUTTON_ZERO:
				ApplicationManager.getInstance().setTareInGram(0d);
				break;

			case ActionButtonbarFragment.BUTTON_TARA:
				ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
				break;
			case ActionButtonbarFragment.BUTTON_STATISTICS:
				ApplicationManager.getInstance().showStatisticsNotification(ApplicationActivity.this, new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						actionButtonbarFragment.updateStatsButtonUI();
					}
				});
				break;

			case ActionButtonbarFragment.BUTTON_INGREDIANTLIST:
				ApplicationManager.getInstance().showIngrediantNotification(ApplicationActivity.this, new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						actionButtonbarFragment.updateStatsButtonUI();
					}
				});
				break;
			case ActionButtonbarFragment.BUTTON_SHOWBATCH:
				ApplicationManager.getInstance().showBatchList(ApplicationActivity.this, new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						actionButtonbarFragment.updateStatsButtonUI();
					}
				});

				break;
			case ActionButtonbarFragment.BUTTON_ACCUMULATE:
				ApplicationManager.getInstance().accumulateStatistics();
				actionButtonbarFragment.updateStatsButtonUI();
				break;
			case ActionButtonbarFragment.BUTTON_APP_SETTINGS:
				if(appSettingsVisible == true) {
					getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();
					actionButtonbarFragment.getButtonAppSettings().setText("SETTINGS");
					actionButtonbarFragment.getButtonCal().setEnabled(true);
					actionButtonbarFragment.getButtonPrint().setEnabled(true);
					actionButtonbarFragment.getButtonStart().setEnabled(true);
					actionButtonbarFragment.getButtonTara().setEnabled(true);
					actionButtonbarFragment.updateStatsButtonUI();
					actionButtonbarFragment.getButtonAccumulate().setEnabled(true);
					appSettingsVisible = false;

					if(Scale.getInstance().getScaleApplication()==PIPETTE_ADJUSTMENT){
						if (ApplicationManager.getInstance().getPipette_current_sample()==0){
							actionButtonbarFragment.getButtonAccumulate().setEnabled(false);
							actionButtonbarFragment.updateStatsButtonUI();
							actionButtonbarFragment.getButtonTara().setEnabled(true);
						}else{
							actionButtonbarFragment.getButtonAccumulate().setEnabled(true);
							actionButtonbarFragment.getButtonTara().setEnabled(false);

						}
					}
				}else{
					actionButtonbarFragment.getButtonCal().setEnabled(false);
					actionButtonbarFragment.getButtonPrint().setEnabled(false);
					actionButtonbarFragment.getButtonStart().setEnabled(false);
					actionButtonbarFragment.updateStatsButtonUI();
					actionButtonbarFragment.getButtonAccumulate().setEnabled(false);

					switch (Scale.getInstance().getScaleApplication()){
						case PART_COUNTING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPartCounting()).commit();
							break;
						case CHECK_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsCheckWeighing()).commit();
							break;
						case WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsWeighing()).commit();
							break;
						case PERCENT_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPercentWeighing()).commit();
							break;
						case ANIMAL_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsAnimalWeighing()).commit();
							break;
						case FILLING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsFilling()).commit();
							break;
						case FORMULATION:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsFormulation()).commit();
							break;
						case DIFFERENTIAL_WEIGHING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsDifferentialWeighing()).commit();
							break;
						case DENSITIY_DETERMINATION:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsDensityDetermination()).commit();
							break;
						case PEAK_HOLD:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPeakHold()).commit();
							break;
						case INGREDIENT_COSTING:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsIngrediantCosting()).commit();
							break;
						case STATISTICAL_QUALITY_CONTROL:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsSQC()).commit();
							break;
						case PIPETTE_ADJUSTMENT:
							getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPipetteAdjustment()).commit();

							break;


						default:
							Toast.makeText(this,"TODO: Implement Actions",Toast.LENGTH_SHORT).show();
					}
					actionButtonbarFragment.getButtonAppSettings().setText("RETURN TO APPLICATION");
					appSettingsVisible = true;



				}
				break;
			case ActionButtonbarFragment.BUTTON_CAL:
				//send command for calibration to the scale


				break;
			case ActionButtonbarFragment.BUTTON_PRINT:
				//Print whole protocol to protocol printer connected on COM 1

				//Print GLP and GMP Data which is independent of the application
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				if  (prefs.getBoolean(getString(R.string.preferences_print_header),getResources().getBoolean(R.bool.preferences_print_header))==true) {

					String header=prefs.getString("preferences_glp_header","");
					printHeader(header+"\n");
				}

				if  (prefs.getBoolean(getString(R.string.preferences_print_date),getResources().getBoolean(R.bool.preferences_print_date))==true) {
					printDate();
				}

				if  (prefs.getBoolean(getString(R.string.preferences_print_balance_id),getResources().getBoolean(R.bool.preferences_print_balance_id))==true) {
					printBalanceId();
				}

				if  (prefs.getBoolean(getString(R.string.preferences_print_balance_name),getResources().getBoolean(R.bool.preferences_print_balance_name))==true) {
					String balanceName=prefs.getString("preferences_glp_balance_name","");
					printBalanceName(balanceName);
				}

				if  (prefs.getBoolean(getString(R.string.preferences_print_user_name),getResources().getBoolean(R.bool.preferences_print_user_name))==true) {
					printUserName();
				}
				if  (prefs.getBoolean(getString(R.string.preferences_print_project_name),getResources().getBoolean(R.bool.preferences_print_project_name))==true) {
					String projectName=prefs.getString("preferences_glp_project_name","");
					printProjectName(projectName);
				}

				if  (prefs.getBoolean(getString(R.string.preferences_print_application_name),getResources().getBoolean(R.bool.preferences_print_application_name))==true) {
					printApplicationName();
				}

				//Printing the application data
				switch (Scale.getInstance().getScaleApplication()){
					case WEIGHING:
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit() +"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

						if (prefs.getBoolean(getString(R.string.preferences_weigh_print_min),getResources().getBoolean(R.bool.preferences_weigh_print_min))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum Weight: "+ ApplicationManager.getInstance().getUnderLimitAsStringInGram()+ " g"+"\n");
							}
						break;

					case PART_COUNTING:

						if  (prefs.getBoolean(getString(R.string.preferences_counting_print_sample_size),getResources().getBoolean(R.bool.preferences_counting_print_sample_size))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram() +"\n");
						}
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
						if  (prefs.getBoolean(getString(R.string.preferences_counting_print_apw),getResources().getBoolean(R.bool.preferences_counting_print_apw))==true){
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("APW: "+ ApplicationManager.getInstance().getAveragePieceWeightAsStringInGram() + " g"+"\n");
						}

						String cmode = prefs.getString(getString(R.string.preferences_counting_mode),"");
						if  (cmode.equals("2")) {
							if (prefs.getBoolean(getString(R.string.preferences_counting_print_under_limit), getResources().getBoolean(R.bool.preferences_counting_print_under_limit)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitPiecesAsString()+" PCS"+"\n");
							}
							if (prefs.getBoolean(getString(R.string.preferences_counting_print_over_limit), getResources().getBoolean(R.bool.preferences_counting_print_over_limit)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getOverlimitPiecesAsString()+" PCS"+"\n");
							}
						}
						if  (cmode.equals("3")) {
							if (prefs.getBoolean(getString(R.string.preferences_counting_print_target), getResources().getBoolean(R.bool.preferences_counting_print_target)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: "+ ApplicationManager.getInstance().getTargetPiecesAsString()+" PCS"+"\n");
							}
							if (prefs.getBoolean(getString(R.string.preferences_counting_difference_visible), getResources().getBoolean(R.bool.preferences_counting_difference_visible)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ ApplicationManager.getInstance().getDifferenceAsString()+" PCS"+"\n");
							}
						}
						break;

					case PERCENT_WEIGHING:
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Percentage: " +ApplicationManager.getInstance().getPercent()+ " %"+"\n" );
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsString()+ " g"+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsString()+ " g"+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

						if  (prefs.getBoolean(getString(R.string.preferences_percent_print_reference),getResources().getBoolean(R.bool.preferences_percent_print_reference))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Reference Weight: " +ApplicationManager.getInstance().getReferenceWeightAsStringInGram()+"\n" );
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Reference Adjust: "+ApplicationManager.getInstance().getCurrentLibrary().getReferenceweightAdjustment()+" %" +"\n" );
						}
						if  (prefs.getBoolean(getString(R.string.preferences_percent_print_difference),getResources().getBoolean(R.bool.preferences_percent_print_difference))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ ApplicationManager.getInstance().getDifferenceInGram() +" g"+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_percent_print_difference_percent),getResources().getBoolean(R.bool.preferences_percent_print_difference_percent))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getDifferenceInPercent()+ " %"+"\n");
						}
						break;

					case CHECK_WEIGHING:
						String cmode_check = prefs.getString(getString(R.string.preferences_check_limitmode),"");
						String checklimitmode = prefs.getString(getString(R.string.preferences_check_limitmode),"");
						double current = ApplicationManager.getInstance().getTaredValueInGram();
						double under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
						double over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
						if(checklimitmode.equals("1")) {
							current = ApplicationManager.getInstance().getTaredValueInGram();
							under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
							over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
						}
						if(checklimitmode.equals("2")) {
							current = ApplicationManager.getInstance().getTaredValueInGram();
							under = ApplicationManager.getInstance().getCheckNominaldouble()-ApplicationManager.getInstance().getCheckNominalToleranceUnderdouble();
							over = ApplicationManager.getInstance().getCheckNominaldouble()+ApplicationManager.getInstance().getCheckNominalToleranceOverdouble();
						}

						if (current<under){Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: Under"+"\n");}
						if (current>over){Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: Over"+"\n");}
						if (current>=under && current<=over){Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: Accept"+"\n");}

						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

						if  (cmode_check.equals("1")) {
							if (prefs.getBoolean(getString(R.string.preferences_check_print_underlimit), getResources().getBoolean(R.bool.preferences_check_print_underlimit)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitCheckWeighingAsString() + " g"+"\n");
							}
							if (prefs.getBoolean(getString(R.string.preferences_check_print_overlimit), getResources().getBoolean(R.bool.preferences_check_print_overlimit)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Over Limit: "+ ApplicationManager.getInstance().getOverLimitCheckWeighingAsString() + " g"+"\n");
							}
						}

						if  (cmode_check.equals("2")) {
							if (prefs.getBoolean(getString(R.string.preferences_check_print_target), getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("target: "+ ApplicationManager.getInstance().getCheckNominal() + " g"+"\n");
							}
							if (prefs.getBoolean(getString(R.string.preferences_check_print_undertolerance), getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceUnder() + " g"+"\n");
							}

							if (prefs.getBoolean(getString(R.string.preferences_check_print_overtolerance), getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceOver() + " g"+"\n");
							}
						}
						break;

					case ANIMAL_WEIGHING:
						if  (prefs.getBoolean(getString(R.string.preferences_animal_print_measuring),getResources().getBoolean(R.bool.preferences_animal_print_measuring))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Averaging Time: "+ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime() + " s"+"\n");
						}
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Final Weight: "+ApplicationManager.getInstance().getAnimalWeight() + " g"+"\n");
						break;

					case FILLING:
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: " +ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
						if  (prefs.getBoolean(getString(R.string.preferences_filling_print_target),getResources().getBoolean(R.bool.preferences_filling_print_target))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: "+ApplicationManager.getInstance().getTargetasString() +" g"+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_filling_print_differencew),getResources().getBoolean(R.bool.preferences_filling_print_differencew))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getDifferenceFilling() +" g"+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_filling_differencep_visible),getResources().getBoolean(R.bool.preferences_filling_differencep_visible))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getFillingDifferenceInPercent()+ " %"+"\n");
						}
						break;

					case TOTALIZATION:
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getSum())+" g"+"\n");
						if  (prefs.getBoolean(getString(R.string.preferences_totalization_print_samples),getResources().getBoolean(R.bool.preferences_totalization_print_samples))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples: "+Long.toString(ApplicationManager.getInstance().getStatistic().getN())+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_totalization_print_average),getResources().getBoolean(R.bool.preferences_totalization_print_average))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMean())+" g"+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_totalization_print_standard),getResources().getBoolean(R.bool.preferences_totalization_print_standard))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getStandardDeviation())+" g"+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_totalization_print_minimum),getResources().getBoolean(R.bool.preferences_totalization_print_minimum))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMin())+" g"+"\n");
						}
						if  (prefs.getBoolean(getString(R.string.preferences_totalization_print_maximum),getResources().getBoolean(R.bool.preferences_totalization_print_maximum))==true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMax())+" g"+"\n");
						}
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range: "+String.format("%.4f g",(ApplicationManager.getInstance().getStatistic().getMax())-ApplicationManager.getInstance().getStatistic().getMin())+" g"+"\n");

						break;

					case FORMULATION:
						//To Do

						break;

					case DIFFERENTIAL_WEIGHING:
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: " +ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

						if(ApplicationManager.getInstance().getCurrentItem() != null) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Item Name: " + ApplicationManager.getInstance().getCurrentItem().getName() + "\n");
							if (prefs.getBoolean(getString(R.string.preferences_differential_print_initial), getResources().getBoolean(R.bool.preferences_differential_print_initial)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Initial: " + String.format("%.4f", ApplicationManager.getInstance().getCurrentItem().getWeight()) + " g" + "\n");
							}

							if (prefs.getBoolean(getString(R.string.preferences_differential_print_final), getResources().getBoolean(R.bool.preferences_differential_print_final)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Final: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
							}

							if (prefs.getBoolean(getString(R.string.preferences_differential_print_differencew), getResources().getBoolean(R.bool.preferences_differential_print_differencew)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: " + ApplicationManager.getInstance().getDifferenceAsStringInGramWithUnit() + "\n");
							}

							if (prefs.getBoolean(getString(R.string.preferences_differential_print_differencep), getResources().getBoolean(R.bool.preferences_differential_print_differencep)) == true) {
								Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: " + ApplicationManager.getInstance().getDifferenceToInitialInPercentWithUnit() + "\n");
							}
						}
						break;
					case DENSITIY_DETERMINATION:
						//To Do
						break;

					case PEAK_HOLD:
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Peak Weight: "+ ApplicationManager.getInstance().getPeakHoldMaximum()+ " g"+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

						if (prefs.getBoolean(getString(R.string.preferences_peak_print_stableonly), getResources().getBoolean(R.bool.preferences_peak_print_stableonly)) == true) {
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Stable only : yes\n");
						}
							Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Stable only : no\n");
						break;

					case INGREDIENT_COSTING:
						break;







					default:
						Toast.makeText(ApplicationActivity.this, "Not implemented", Toast.LENGTH_LONG).show();
						break;
				}



				if  (prefs.getBoolean(getString(R.string.preferences_print_signature),getResources().getBoolean(R.bool.preferences_print_signature))==true) {
					printSignature();
				}

				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");

				//ProtocolPrinterUtils.printProtocol();

				Toast.makeText(ApplicationActivity.this, "Protool printed: ", Toast.LENGTH_LONG).show();
				//Toast.makeText(ApplicationActivity.this, "Label printed: ", Toast.LENGTH_LONG).show();
				//Print current weight to label printer connected on COM 2
				//LabelPrinterUtils.printText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit(),1);

				break;
			case ActionButtonbarFragment.BUTTON_SETTINGS:
				Intent intent2 = new Intent(ApplicationActivity.this, SettingsActivity.class);
				startActivity(intent2);
				break;
			case ActionButtonbarFragment.BUTTON_SAVE:
				try{
					final Dialog dialog = new Dialog(ApplicationActivity.this);
					dialog.setContentView(R.layout.dialog_edit_text);
					dialog.setTitle("Please enter a name for the library");

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
				application == FILLING_CALC_TARGET ||
				application == FORMULATION_RUNNING){
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


	@Override
	public void onStableChanged(boolean isStable) {

		Log.e("ApplicationActivity", "onStableChanged");

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


		if (isStable==true) {
			if (Scale.getInstance().getScaleApplication() == TOTALIZATION) {
				if (prefs.getBoolean(getString(R.string.preferences_totalization_AutoSampleMode), getResources().getBoolean(R.bool.preferences_totalization_AutoSampleMode)) == true) {
					actionButtonbarFragment.getButtonAccumulate().performClick();
				}
			}

			if (Scale.getInstance().getScaleApplication() == PIPETTE_ADJUSTMENT) {
				if (prefs.getBoolean(getString(R.string.preferences_pipette_autosamplemode), getResources().getBoolean(R.bool.preferences_pipette_autosamplemode)) == true) {
					actionButtonbarFragment.getButtonAccumulate().performClick();
				}
			}

			if (Scale.getInstance().getScaleApplication() == STATISTICAL_QUALITY_CONTROL) {
				if (prefs.getBoolean(getString(R.string.preferences_statistic_mode), getResources().getBoolean(R.bool.preferences_statistic_mode)) == true) {
					actionButtonbarFragment.getButtonAccumulate().performClick();
				}
			}
		}

	}
}
