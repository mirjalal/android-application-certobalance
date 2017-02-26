package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemMeasuredAdapter;
import com.certoclav.certoscale.adapters.RecipeResultElementAdapter;
import com.certoclav.certoscale.adapters.SQCAdapter;
import com.certoclav.certoscale.adapters.SamplesAdapter;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.List;

import static com.certoclav.certoscale.model.ScaleApplication.DENSITY_DETERMINATION;
import static com.certoclav.certoscale.model.ScaleApplication.DENSITY_DETERMINATION_STARTED;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING;
import static com.certoclav.certoscale.model.ScaleApplication.PEAK_HOLD;
import static com.certoclav.certoscale.model.ScaleApplication.PEAK_HOLD_STARTED;
import static com.certoclav.certoscale.model.ScaleApplication.PIPETTE_ADJUSTMENT_1_HOME;
import static com.certoclav.certoscale.model.ScaleApplication.PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES;
import static com.certoclav.certoscale.model.ScaleApplication.PIPETTE_ADJUSTMENT_3_FINISHED;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL_1_HOME;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED;


public class ActionButtonbarFragment extends Fragment implements ScaleApplicationListener, WeightListener{


	public ActionButtonbarFragment() {
	}



	public static final int BUTTON_TARA = 3;
	public static final int BUTTON_CAL = 4;
	public static final int BUTTON_PRINT = 5;
	public static final int BUTTON_APP_SETTINGS = 6;
	public static final int BUTTON_STATISTICS = 7;
	public static final int BUTTON_ACCUMULATE = 8;
	public static final int BUTTON_START =9;
	public static final int BUTTON_ZERO = 10;
	public static final int BUTTON_ACCEPT=11;
	public static final int BUTTON_INGREDIANTLIST=12;
	public static final int BUTTON_NEWBATCH=30;
	public static final int BUTTON_SHOWBATCH=31;
	public static final int BUTTON_HOME = 13;
	public static final int BUTTON_SETTINGS = 14;
	public static final int BUTTON_ADD = 15;
	public static final int BUTTON_BACK = 16;
	public static final int SPINNER_LIBRARY = 17;
	public static final int BUTTON_GO_TO_APPLICATION = 18;
	public static final int BUTTON_LOGOUT = 19;
	public static final int BUTTON_SAVE = 20;
	public static final int BUTTON_SETTINGS_DEVICE = 21;
	public static final int BUTTON_MORE = 22;
	public static final int BUTTON_END_BATCH=23;





	private Button buttonTara = null;
	private Button buttonCal = null;
	private Button buttonPrint= null;
	private Button buttonStatistics = null;
	private Button buttonAccumulate = null;
	private Button buttonAnimalStart = null;
	private Button buttonAppSettings = null;
	private Button buttonZero = null;
	private Button buttonAccept = null;
	private Button buttonIngrediantList = null;
	private Button buttonEnd = null;
	private Button buttonEndBatch = null;

	private Button buttonNewBatch = null;
	private Button buttonShowBatch = null;



	public Button getButtonZero() {
		return buttonZero;
	}

	public void setButtonZero(Button buttonZero) {
		this.buttonZero = buttonZero;
	}



	public Button getButtonStart() {
		return buttonStart;
	}


	private Button buttonStart = null;
	private ArrayList<ButtonEventListener> navigationbarListeners = new ArrayList<ButtonEventListener>();


	public Button getButtonStatistics() {
		return buttonStatistics;
	}

	public void setButtonStatistics(Button buttonStatistics) {
		this.buttonStatistics = buttonStatistics;
	}

	public Button getButtonAccumulate() {
		return buttonAccumulate;
	}

	public void setButtonAccumulate(Button buttonAccumulate) {
		this.buttonAccumulate = buttonAccumulate;
	}

	public Button getButtonAnimalStart() {
		return buttonAnimalStart;
	}

	public void getButtonAnimalStart(Button buttonAnimalStart) {
		this.buttonAnimalStart = buttonAnimalStart;
	}



	public Button getButtonTara() {
		return buttonTara;
	}

	public void setButtonTara(Button buttonTara) {
		this.buttonTara = buttonTara;
	}

	public Button getButtonCal() {
		return buttonCal;
	}

	public void setButtonCal(Button buttonCal) {
		this.buttonCal = buttonCal;
	}

	public Button getButtonPrint() {
		return buttonPrint;
	}

	public void setButtonPrint(Button buttonPrint) {
		this.buttonPrint = buttonPrint;
	}

	public Button getButtonAppSettings() {
		return buttonAppSettings;
	}

	public void setButtonAppSettings(Button buttonAppSettings) {
		this.buttonAppSettings = buttonAppSettings;
	}




public void setButtonEventListener (ButtonEventListener listener){
	this.navigationbarListeners.add(listener);
}

public void removeButtonEventListener(ButtonEventListener listener) {
	this.navigationbarListeners.remove(listener);


}

	@Override
	public void onResume() {



		super.onResume();
		Scale.getInstance().setOnApplicationListener(this);
		Scale.getInstance().setOnWeightListener(this);
		onApplicationChange(Scale.getInstance().getScaleApplication());




		//get PeakHoldMode
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String PeakHoldMode = prefs.getString(getString(R.string.preferences_peak_mode),"");



	}

	@Override
	public void onPause() {
		Scale.getInstance().removeOnApplicationListener(this);
		Scale.getInstance().setOnWeightListener(this);
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.actionbar,container, false);

		buttonStart = (Button) rootView.findViewById(R.id.actionbar_button_start);
		buttonStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Scale.getInstance().getScaleApplication()==DENSITY_DETERMINATION) {

					ApplicationManager.getInstance().setDensity_step_counter(1);
					buttonAccept.setEnabled(true);
					Scale.getInstance().setScaleApplication(DENSITY_DETERMINATION_STARTED);
					buttonStart.setEnabled(false);

				}

				//somebody starts the peak hold mode manually
				if(Scale.getInstance().getScaleApplication()==PEAK_HOLD){
					try{

						//Start PeakHold Measurement
						Scale.getInstance().setScaleApplication(PEAK_HOLD_STARTED);


					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

				}

				if(Scale.getInstance().getScaleApplication()==PIPETTE_ADJUSTMENT_1_HOME || Scale.getInstance().getScaleApplication() == PIPETTE_ADJUSTMENT_3_FINISHED){
					if (ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()==0){
						Toast.makeText(getActivity(), "Please Enter the number of samples first", Toast.LENGTH_SHORT).show();
					}else{
						//start at pipette sample one and reset all statistics
						ApplicationManager.getInstance().setPipette_current_sample(1);
						ApplicationManager.getInstance().getStats().getStatistic().clear();
						ApplicationManager.getInstance().getStats().getSamples().clear();
						Scale.getInstance().setScaleApplication(PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES);
						updateStatsButtonUI();

					}
				}




				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_START,false);
				}


			}
		});

		buttonEnd = (Button) rootView.findViewById(R.id.actionbar_button_end);
		buttonEnd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try{


					// End PeakHold Measurenment
					ApplicationManager.getInstance().setPeakHoldActivated(false);
					Scale.getInstance().setScaleApplication(PEAK_HOLD);


				}
				catch (Exception e)
				{
					e.printStackTrace();
				}


			}
		});


		buttonZero = (Button) rootView.findViewById(R.id.actionbar_button_zero);
		buttonZero.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_ZERO,false);
				}
			}
		});

		buttonPrint = (Button) rootView.findViewById(R.id.actionbar_button_print);
		buttonPrint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_PRINT,false);
				}

			}
		});


		buttonStatistics = (Button) rootView.findViewById(R.id.actionbar_button_statistics);
		buttonStatistics.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			switch (Scale.getInstance().getScaleApplication()){
				default:
					showStatisticsNotification(getActivity(), new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							updateStatsButtonUI();
						}
					});
					break;
				case PIPETTE_ADJUSTMENT_3_FINISHED:
					showPipetteResults(getActivity(), new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							updateStatsButtonUI();
						}
					});
					break;
				case TOTALIZATION:
					showStatisticsTotalization(getActivity(), new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							updateStatsButtonUI();
						}
					});
					break;
			}


			for(ButtonEventListener listener : navigationbarListeners){
				listener.onClickNavigationbarButton(BUTTON_STATISTICS,false);
			}

			}
		});

		buttonAccumulate = (Button) rootView.findViewById(R.id.actionbar_button_accumulate);
		buttonAccumulate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_ACCUMULATE,false);
				}

				if(Scale.getInstance().getScaleApplication()==STATISTICAL_QUALITY_CONTROL_1_HOME || Scale.getInstance().getScaleApplication()==STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED ){
					double sqcNominal=ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal();
					if (ApplicationManager.getInstance().getTaredValueInGram()>(sqcNominal+ ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance1())){
						ApplicationManager.getInstance().setSqcPT1(ApplicationManager.getInstance().getSqcPT1()+1);


					}
					if (ApplicationManager.getInstance().getTaredValueInGram()>(sqcNominal+ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance2())){
						ApplicationManager.getInstance().setSqcPT2(ApplicationManager.getInstance().getSqcPT2()+1);
					}

					if (ApplicationManager.getInstance().getTaredValueInGram()<(sqcNominal+ApplicationManager.getInstance().getCurrentLibrary().getSQCnTolerance1())){
						ApplicationManager.getInstance().setSqcNT1(ApplicationManager.getInstance().getSqcNT1()+1);
					}

					if (ApplicationManager.getInstance().getTaredValueInGram()<(sqcNominal-sqcNominal+ApplicationManager.getInstance().getCurrentLibrary().getSQCpTolerance2())){
						ApplicationManager.getInstance().setSqcPT1(ApplicationManager.getInstance().getSqcPT1()+1);
					}

				}





			}
		});
		buttonCal = (Button) rootView.findViewById(R.id.actionbar_button_cal);
		buttonCal.setVisibility(View.GONE);
		buttonCal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_CAL,false);
				}

			}
		});

		buttonTara = (Button) rootView.findViewById(R.id.actionbar_button_tara);
		buttonTara.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_TARA,false);
				}


			}
		});

		buttonAppSettings = (Button) rootView.findViewById(R.id.actionbar_button_settings);
		buttonAppSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(buttonAppSettings.getText().toString().equals("RETURN TO APPLICATION")){
					getButtonAppSettings().setText("SETTINGS");

					switch (Scale.getInstance().getScaleApplication()){

						case PART_COUNTING_CALC_AWP:
							Scale.getInstance().setScaleApplication(PART_COUNTING);
							break;
						case STATISTICAL_QUALITY_CONTROL_1_HOME:
							break;

					}

				}else{
					getButtonAppSettings().setText("RETURN TO APPLICATION");
				}



				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_APP_SETTINGS,false);
				}

			}
		});


		//Ingrediant Costing Calculations
		buttonAccept = (Button) rootView.findViewById(R.id.actionbar_button_accept);
		buttonAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateStatsButtonUI();

				switch (Scale.getInstance().getScaleApplication()){
					case INGREDIENT_COSTING:

						if (ApplicationManager.getInstance().getCurrentItem()==null) {
							Toast.makeText(getActivity(), "Please Choose Item first", Toast.LENGTH_LONG).show();
						}else {
							double Cost = ApplicationManager.getInstance().getCurrentItem().getCost();
							double unitWeight = ApplicationManager.getInstance().getCurrentItem().getWeight();
							double currentWeight = ApplicationManager.getInstance().getTaredValueInGram();

							double unitCost = (Cost * currentWeight) / unitWeight;
							ApplicationManager.getInstance().setIngrediantUnitCost(unitCost);

							double totalWeight = ApplicationManager.getInstance().getIngrediantTotalWeight();
							ApplicationManager.getInstance().setIngrediantTotalWeight(totalWeight + currentWeight);

							double totalCost = ApplicationManager.getInstance().getIngrediantTotalCost();
							ApplicationManager.getInstance().setIngrediantTotalCost(unitCost + totalCost);


							Item measuredItem = new Item("",ApplicationManager.getInstance().getCurrentItem().getItemJson());


							measuredItem.setWeight(currentWeight);
							measuredItem.setCost(unitCost);
							ApplicationManager.getInstance().getIngrediantCostList().add(measuredItem);
							updateIngrediantButtonUI();

						}
						break;
					case DENSITY_DETERMINATION_STARTED:
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
						String densitymode = prefs.getString(getString(R.string.preferences_density_mode),"");

						if (ApplicationManager.getInstance().getDensity_step_counter()==2){
							ApplicationManager.getInstance().setDensity_weight_liquid(ApplicationManager.getInstance().getTaredValueInGram());
							ApplicationManager.getInstance().setDensity_step_counter(3);
							buttonAccept.setEnabled(false);
							buttonStart.setEnabled(true);
							Scale.getInstance().setScaleApplication(DENSITY_DETERMINATION);
						}

						if(ApplicationManager.getInstance().getDensity_step_counter()==4){
							ApplicationManager.getInstance().getCurrentLibrary().setOiledWeight(ApplicationManager.getInstance().getTaredValueInGram());
							ApplicationManager.getInstance().setDensity_step_counter(2);
						}

						if (ApplicationManager.getInstance().getDensity_step_counter()==1){
							ApplicationManager.getInstance().setDensity_weight_air(ApplicationManager.getInstance().getTaredValueInGram());
							if (densitymode.equals("4")){
								ApplicationManager.getInstance().setDensity_step_counter(4);
							}else{
								ApplicationManager.getInstance().setDensity_step_counter(2);
							}
						}

					case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:

						if (ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()==0){
							Toast.makeText(getActivity(), "Please Enter the number of samples first", Toast.LENGTH_SHORT).show();
							ApplicationManager.getInstance().setPipette_current_sample(0);
							ApplicationManager.getInstance().getStats().getStatistic().clear();
							updateStatsButtonUI();

						}else {


							//Equation according to http://www.wissenschaft-technik-ethik.de/wasser_dichte.html
							double pipetteDensity = ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp());
							pipetteDensity = pipetteDensity + (ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure() - 1) * 0.046;
							double pipetteML = ApplicationManager.getInstance().getTaredValueInGram() / pipetteDensity;

							ApplicationManager.getInstance().setPipetteCalculatedML(-pipetteML);
							ApplicationManager.getInstance().accumulateStatistics();
							updateStatsButtonUI();
							ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());

							ApplicationManager.getInstance().setPipette_current_sample(ApplicationManager.getInstance().getPipette_current_sample() + 1);
							if (ApplicationManager.getInstance().getPipette_current_sample() == ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples() + 1) {
								Scale.getInstance().setScaleApplication(PIPETTE_ADJUSTMENT_3_FINISHED);
							}




						}
						break;




				}

			}
		});

		buttonIngrediantList = (Button) rootView.findViewById(R.id.actionbar_button_Ingredient_List);
		buttonIngrediantList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showIngrediantNotification(getActivity(), new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						updateIngrediantButtonUI();
					}
				});
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_INGREDIANTLIST,false);
				}

			}
		});


		buttonNewBatch = (Button) rootView.findViewById(R.id.actionbar_button_new_batch);
		buttonNewBatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {




				try {
					final Dialog dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.dialog_edit_text);
					dialog.setTitle("Please enter the batch name");

					// set the custom dialog components - text, image and button

					Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_edit_text_button_cancel);
					dialogButtonNo.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					Button dialogButton = (Button) dialog.findViewById(R.id.dialog_edit_text_button_save);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							EditText editText = (EditText) dialog.findViewById(R.id.dialog_edit_text_edittext);
							ApplicationManager.getInstance().setBatchName(editText.getText().toString());
							//								ApplicationManager.getInstance().getCurrentLibrary().setUnderLimit(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

							ApplicationManager.getInstance().setBatchName(editText.getText().toString());
							buttonAccumulate.setEnabled(true);

							Scale.getInstance().setScaleApplication(STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED);

							dialog.dismiss();
							onResume();


						}
					});

					dialog.show();

				} catch (Exception e) {
					e.printStackTrace();
				}








			}
		});

		buttonEndBatch = (Button) rootView.findViewById(R.id.actionbar_button_end_batch);
		buttonEndBatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SQC currentBatch= new SQC(ApplicationManager.getInstance().getStats().getStatistic().copy(),ApplicationManager.getInstance().getBatchName(),
						ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal(),
						ApplicationManager.getInstance().getSqcPT1(),ApplicationManager.getInstance().getSqcPT2(),ApplicationManager.getInstance().getSqcNT1(),
						ApplicationManager.getInstance().getSqcNT2());
				//currentBatch.setName(ApplicationManager.getInstance().getBatchName());
				//currentBatch.setStatistics(ApplicationManager.getInstance().getStatistic());

				ApplicationManager.getInstance().getBatchList().add(currentBatch);
				ApplicationManager.getInstance().getStats().getStatistic().clear();


				buttonNewBatch.setText("NEW BATCH");
				buttonAccumulate.setEnabled(false);
				buttonShowBatch.setEnabled(true);
				updateBatchListButtonText();

				ApplicationManager.getInstance().setSqcPT1(0);
				ApplicationManager.getInstance().setSqcPT2(0);
				ApplicationManager.getInstance().setSqcNT1(0);
				ApplicationManager.getInstance().setSqcNT2(0);
				Scale.getInstance().setScaleApplication(STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED);
			}
		});


		buttonShowBatch = (Button) rootView.findViewById(R.id.actionbar_button_view_batch);
		buttonShowBatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Scale.getInstance().getScaleApplication() == STATISTICAL_QUALITY_CONTROL_1_HOME || Scale.getInstance().getScaleApplication() == STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED ||Scale.getInstance().getScaleApplication() == STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED){
					showBatchList(getActivity(),null);
				}

				if (Scale.getInstance().getScaleApplication()==FORMULATION){
					showRecipeResults(getActivity(), new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							updateStatsButtonUI();
						}
					});
				}

				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_SHOWBATCH,false);
				}

			}
		});


		return rootView;
	}




	public void disbalbeAllButtons(){
		buttonAppSettings.setEnabled(false);
		buttonCal.setEnabled(false);
		buttonPrint.setEnabled(false);
		buttonTara.setEnabled(false);
	}

	@Override
	public void onApplicationChange(ScaleApplication application) {
		updateBatchListButtonText();
		updateStatsButtonUI();
		updateIngrediantButtonUI();
		buttonPrint.setEnabled(true);
		//get PeakHoldMode
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String PeakHoldMode = prefs.getString(getString(R.string.preferences_peak_mode),"");

		Toast.makeText(getContext(), Scale.getInstance().getScaleApplication().toString(), Toast.LENGTH_SHORT).show();

		switch (Scale.getInstance().getScaleApplication()){

			case WEIGHING:

				//Buttons used in this application
				buttonTara.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");
				buttonAccumulate.setEnabled(true);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonAppSettings.setText("SETTINGS");



				//unused Buttons
				buttonEndBatch.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;

			case PART_COUNTING:

				//Button used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);

				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(true);
				buttonAccumulate.setText("ADD TO STATS");
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonAppSettings.setText("SETTINGS");


				//unused Buttons
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;

			case PART_COUNTING_CALC_AWP:
				//Button used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonPrint.setEnabled(false);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);

				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(false);
				buttonAccumulate.setText("ADD TO STATS");
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);


				//unused Buttons
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;

			case PERCENT_WEIGHING:

				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");


				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				//unused Buttons
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;

			case CHECK_WEIGHING:
				//Buttons used by the application

				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);

				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				// unused Buttons
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;



			case ANIMAL_WEIGHING_CALCULATING:
				buttonStart.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(false);
				buttonStart.setEnabled(false);
				buttonEndBatch.setVisibility(View.GONE);
				break;
			case ANIMAL_WEIGHING:

				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(true);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				//unused Buttons
				buttonEndBatch.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(true);
				buttonEnd.setVisibility(View.GONE);
				break;

			case FILLING:

				//Buttons used by the applications
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);


				//unused Buttons
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;

			case TOTALIZATION:
				// Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(true);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(false);

				//unused Buttons
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);

				break;

			case FORMULATION:

				// Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonPrint.setEnabled(true);
				buttonZero.setVisibility(View.VISIBLE);


				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonAppSettings.setText("SETTINGS");

				if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
					buttonStart.setEnabled(true);
					buttonStart.setVisibility(View.VISIBLE);
				} else {
					buttonStart.setEnabled(false);
					buttonStart.setVisibility(View.VISIBLE);
				}

				buttonShowBatch.setEnabled(true);
				buttonShowBatch.setVisibility(View.VISIBLE);
				buttonShowBatch.setText("RESULTS");

				//unused Buttons
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);




				break;
			case FORMULATION_RUNNING:
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(true);
				buttonCal.setEnabled(false);
				buttonPrint.setEnabled(false);
				//buttonTara.setEnabled(false);
				buttonAppSettings.setEnabled(false);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonShowBatch.setEnabled(false);
				buttonEndBatch.setVisibility(View.GONE);
				break;


			case DIFFERENTIAL_WEIGHING:
				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonPrint.setEnabled(true);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);

				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(true);

				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				//unused Buttons
				buttonEndBatch.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				break;

			case INGREDIENT_COSTING:
				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				if(ApplicationManager.getInstance().getCurrentItem() == null) {
					buttonAccept.setEnabled(false); // only enabled after item has been choosed
				}

				buttonIngrediantList.setVisibility(View.VISIBLE);
				buttonAccept.setVisibility(View.VISIBLE);
				buttonAccept.setEnabled(true);


				//unused Buttons
				buttonEndBatch.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);


				break;

			case DENSITY_DETERMINATION:
				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				buttonAccept.setVisibility(View.VISIBLE);
				buttonStart.setVisibility(View.VISIBLE);


				//unused Buttons
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				buttonAccept.setEnabled(false);
				buttonStart.setEnabled(true);
				break;

			case DENSITY_DETERMINATION_STARTED:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				buttonAccept.setVisibility(View.VISIBLE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.VISIBLE);
				buttonEndBatch.setVisibility(View.GONE);


				buttonIngrediantList.setVisibility(View.GONE);

				buttonAccept.setEnabled(true);
				buttonStart.setEnabled(false);
				break;

			case PEAK_HOLD:
				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStart.setVisibility(View.VISIBLE);
				buttonEnd.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(true);



				//Manual Mode
				if(PeakHoldMode.equals("1")){
					buttonEnd.setEnabled(false);

				}

				//Semi Automatic Mode
				if(PeakHoldMode.equals("2")){
					buttonEnd.setEnabled(true);
					//Start PeakHold Measurement
					Scale.getInstance().setScaleApplication(PEAK_HOLD_STARTED);
				}

				//Automatic Mode
				if(PeakHoldMode.equals("3")){
					buttonEnd.setEnabled(false);
					//Start PeakHold Measurement
					Scale.getInstance().setScaleApplication(PEAK_HOLD_STARTED);
				}



				//unused Buttons
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonAppSettings.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);



				break;
			case PEAK_HOLD_STARTED:

				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStart.setVisibility(View.VISIBLE);
				buttonEnd.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(false);




				//Manual Mode
				if(PeakHoldMode.equals("1")){
					buttonEnd.setEnabled(true);

				}

				//Semi Automatic Mode
				if(PeakHoldMode.equals("2")){
					buttonEnd.setEnabled(true);
					//Start PeakHold Measurement
				}

				//Automatic Mode
				if(PeakHoldMode.equals("3")){
					buttonEnd.setEnabled(false);
					//Start PeakHold Measurement
				}



				//unused Buttons
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonAppSettings.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);

				break;
			case PIPETTE_ADJUSTMENT_3_FINISHED:
			case PIPETTE_ADJUSTMENT_1_HOME:
				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonAppSettings.setText("SETTINGS");
				buttonAccept.setVisibility(View.VISIBLE);
				buttonAccept.setEnabled(false);
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(true);





				//unused Buttons
				buttonEndBatch.setVisibility(View.GONE);
				ApplicationManager.getInstance().setPipette_current_sample(0);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);

				break;

			case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:
				//Buttons used by the application
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonStart.setVisibility(View.VISIBLE);
				buttonAccept.setVisibility(View.VISIBLE);
				buttonAccept.setEnabled(true);
				buttonStart.setEnabled(false);


				//Buttons used by the application
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonEndBatch.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);



				break;


			case STATISTICAL_QUALITY_CONTROL_1_HOME:
				//Buttons used by the Applicaiton

				//This functions clears the batchlists
				resetSqc();

				buttonTara.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				buttonNewBatch.setVisibility(View.VISIBLE);
				buttonNewBatch.setText("NEW BATCH");
				buttonShowBatch.setVisibility(View.VISIBLE);

				buttonAccumulate.setEnabled(false);


				buttonShowBatch.setEnabled(true);
				updateBatchListButtonText();
				buttonAccumulate.setEnabled(false);

				//unused Buttons
				buttonPrint.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);

				buttonEndBatch.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);


				break;


			case STATISTICAL_QUALITY_CONTROL_2_BATCH_STARTED:
				//Buttons used by the Applicaiton
				buttonTara.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setEnabled(true);
				buttonNewBatch.setVisibility(View.VISIBLE);
				buttonNewBatch.setText("END BATCH\n" + ApplicationManager.getInstance().getBatchName());
				buttonShowBatch.setVisibility(View.VISIBLE);
				buttonAccumulate.setEnabled(true);
				buttonShowBatch.setEnabled(false);
				buttonEndBatch.setVisibility(View.VISIBLE);

				//unused Buttons
				buttonPrint.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);







				break;

			case STATISTICAL_QUALITY_CONTROL_3_BATCH_FINISHED:
				//Buttons used by the Applicaiton
				buttonTara.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAppSettings.setText("SETTINGS");
				buttonAppSettings.setEnabled(true);

				buttonNewBatch.setVisibility(View.VISIBLE);
				buttonNewBatch.setText("NEW BATCH");
				buttonShowBatch.setVisibility(View.VISIBLE);

				buttonAccumulate.setEnabled(false);



				buttonShowBatch.setEnabled(true);
				updateBatchListButtonText();
				buttonAccumulate.setEnabled(false);

				//unused Buttons
				buttonPrint.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonEnd.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);

				buttonEndBatch.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);







				break;



			default:
				buttonStart.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);



		}


		updateStatsButtonUI();

		
	}


	public void updateBatchListButtonText() {

		buttonShowBatch.setText("BATCH LIST\n(" + ApplicationManager.getInstance().getBatchList().size() + ")");
		//if there is no batch to show, hide the button
		if(ApplicationManager.getInstance().getBatchList().size() == 0){
			buttonShowBatch.setEnabled(false);
		}

	}

	public void updateIngrediantButtonUI() {

		buttonIngrediantList.setText("COST LIST\n(" + ApplicationManager.getInstance().getIngrediantCostList().size() + ")");
		if (ApplicationManager.getInstance().getIngrediantCostList().size() == 0){
			buttonIngrediantList.setEnabled(false);
		}else {
			buttonIngrediantList.setEnabled(true);
		}
	}


	public void updateStatsButtonUI() {

		getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStats().getStatistic().getN() + ")");
		if (ApplicationManager.getInstance().getStats().getStatistic().getN()==0){
			getButtonStatistics().setEnabled(false);
		}else {
			getButtonStatistics().setEnabled(true);
		}
	}

	@Override
	public void onWeightChanged(Double weight, String unit) {
		if(ApplicationManager.getInstance().getTareInGram() != 0){
			buttonZero.setEnabled(true);
		}else{
			buttonZero.setEnabled(false);
		}
	}




	public void showBatchList(final Context eContext, DialogInterface.OnDismissListener listener) {
		try {
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_batchlist);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Batch List");



			ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_batch_List);



			// This is the array adapter, it takes the context of the activity as a
			// first parameter, the type of list view as a second parameter and your
			// array as a third parameter.
			SQCAdapter arrayAdapter = new SQCAdapter(eContext,new ArrayList<SQC>());


			arrayAdapter.setOnClickListener(new SQCAdapter.OnClickListener() {
				@Override
				public void onEntryClick(SQC sqc) {
					showStatisticsSQC(getContext(),sqc);
				}
			});

			listView.setAdapter(arrayAdapter);
			for(SQC sqc : ApplicationManager.getInstance().getBatchList()){
				arrayAdapter.add(sqc);
			}



			//arrayAdapter.add(new Item(ApplicationManager.getInstance().getCurrentItem().getItemArticleNumber(),"ssdd"));






			Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_clear);
			dialogButtonClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					resetSqc();
					dialog.dismiss();
				}
			});

			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public void showPipetteResults(final Context eContext, DialogInterface.OnDismissListener listener) {
		try {
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_pipette_results);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Pipette Adjustment Results");



			ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_pipette_listview);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(eContext,R.layout.dialog_pipette_row,R.id.menu_main_pipette_edit_text);
			listView.setAdapter(adapter);

			adapter.add("Inaccuracy: \n");

			double meanError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getMean()-ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
			adapter.add("Mean Error: "+String.format("%.4f",meanError)+" ml\n");
			double meanErrorPercent=Math.abs(meanError/ApplicationManager.getInstance().getStats().getStatistic().getMean())*100;
			adapter.add("Mean Error %: "+String.format("%.4f",meanErrorPercent)+" %\n");
			adapter.add("Limit %: "+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy())+" %\n");


			adapter.add("\n");
			adapter.add("Impreccision:\n");
			adapter.add("Standard Deviation: "+String.format("%.4f",ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation())+" ml\n");
			double standardError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()/ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())*100;
			adapter.add("Error CS%: "+String.format("%.4f",standardError)+" %\n");
			adapter.add("Limit CV: "+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())+" %\n");

			if (meanErrorPercent<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy() && standardError<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()){
				adapter.add("\n");
				adapter.add("Result: Pass");
				adapter.add("\n");
			}else{
				adapter.add("\n");
				adapter.add("Result: Fail");
				adapter.add("\n");
			}

			adapter.add("\n");
			adapter.add("Number of Samples"+ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()+"\n");

			double standardDeviation=ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation();
			double mean=ApplicationManager.getInstance().getStats().getStatistic().getMean();
			int nstandard1=0;
			int nstandard2=0;
			int pstandard1=0;
			int pstandard2=0;
			double pcurrent=0;

			for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
				pcurrent=mean-ApplicationManager.getInstance().getStats().getSamples().get(i);
				if (pcurrent<0){
					if (Math.abs(pcurrent)>standardDeviation){
						if (Math.abs(pcurrent)>2*standardDeviation){
							nstandard2++;
						}else{
							nstandard1++;
						}
					}
				}else {
					if (Math.abs(pcurrent)>standardDeviation){
						if (Math.abs(pcurrent)>2*standardDeviation){
							pstandard2++;
						}else{
							pstandard1++;
						}
					}

				}
			}

			adapter.add("> +2s:"+pstandard2+"\n");
			adapter.add("> +2s:"+pstandard1+"\n");
			adapter.add("*+1S > Mean > –1S:"+(ApplicationManager.getInstance().getStats().getSamples().size()-pstandard1-pstandard2-nstandard1-nstandard2)+"\n");
			adapter.add("< -2s:"+nstandard1+"\n");
			adapter.add("< -2s:"+nstandard2+"\n");

			adapter.add("\n");


			adapter.add("---Sample Data---\n");
			for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
				adapter.add("PipetteSample "+String.format("%d",i)+" "+String.format("%.4f",ApplicationManager.getInstance().getStats().getSamples().get(i))+" g\n");

			}



			Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_pipette_button_clear);
			dialogButtonClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ApplicationManager.getInstance().clearStatistics();
					//ApplicationManager.getInstance().setBatchName("");
					//getBatchList().clear();


					dialog.dismiss();
				}
			});

			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_pipette_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showStatisticsTotalization(final Context eContext, DialogInterface.OnDismissListener listener) {
		try {


			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_statistics_totalization);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Statistics:");



			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_sample_number)).setText("" + ApplicationManager.getInstance().getStats().getStatistic().getN());
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_average)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getMean()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_maximum)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getMax()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_minimum)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getMin()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_range)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getVariance()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_stdev)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_total)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getSum()));




			ListView listView = (ListView) dialog.findViewById(R.id.dialog_sample_list);

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = ApplicationManager.getInstance().getStats().getSamples().size()*50;
			listView.setLayoutParams(params);
			listView.requestLayout();

			SamplesAdapter sAdapter= new SamplesAdapter(eContext,new ArrayList<Double>());
			listView.setAdapter(sAdapter);

			for(Double sample:ApplicationManager.getInstance().getStats().getSamples()){
				sAdapter.add(sample);
			}



			Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_statistics_button_clear);
			dialogButtonClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ApplicationManager.getInstance().clearStatistics();
					dialog.dismiss();

				}
			});


			Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_print);
			dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {






				}
			});
			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showStatisticsSQC(final Context eContext, final SQC sqc) {
		try {

			SummaryStatistics statistic =sqc.getStatistics();
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_statistics_sqc);
			dialog.setTitle("Statistics of "+sqc.getName());
			//           statistic = new SummaryStatistics();
			//           for (Double value : statisticsArray) {
			//               statistic.addValue(value);
			//           }
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_sample_number)).setText("" + statistic.getN());
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_average)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(statistic.getMean()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_maximum)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(statistic.getMax()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_minimum)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(statistic.getMin()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_range)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(statistic.getVariance()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_stdev)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(statistic.getStandardDeviation()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_total)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(statistic.getSum()));


			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_nominal)).setText(String.format("%.4f",sqc.getNominal()));

			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ptolerance1)).setText(String.format("%d",sqc.getSqcPT1())+ "   " +String.format("%.1f",((double)sqc.getSqcPT1()/(double)statistic.getN())*100 )+ "%");
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ptolerance2)).setText(String.format("%d",sqc.getSqcPT2())+ "   " +String.format("%.1f",((double)sqc.getSqcPT2()/(double)statistic.getN())*100 )+ "%");
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ntolerance1)).setText(String.format("%d",sqc.getSqcNT1())+ "   " +String.format("%.1f",((double)sqc.getSqcNT1()/(double)statistic.getN())*100 )+ "%");
			((TextView) dialog.findViewById(R.id.dialog_statistics_sqc_text_ntolerance2)).setText(String.format("%d",sqc.getSqcNT2())+ "   " +String.format("%.1f",((double)sqc.getSqcNT2()/(double)statistic.getN())*100 )+ "%");


			// set the custom dialog components - text, image and button


			Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_print);
			dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					ApplicationManager.getInstance().getProtocolPrinter().printTop();

					ApplicationManager.getInstance().getProtocolPrinter().printSQCBatch(sqc);

					ApplicationManager.getInstance().getProtocolPrinter().printBottom();

				}
			});
			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_sqc_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showStatisticsNotification(final Context eContext, final DialogInterface.OnDismissListener listener) {
		try {
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_statistics);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Statistics");
			//           statistic = new SummaryStatistics();
			//           for (Double value : statisticsArray) {
			//               statistic.addValue(value);
			//           }
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_sample_number)).setText("" + ApplicationManager.getInstance().getStats().getStatistic().getN());
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_average)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getMean()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_maximum)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getMax()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_minimum)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getMin()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_range)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getVariance()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_stdev)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()));
			((TextView) dialog.findViewById(R.id.dialog_statistics_text_total)).setText(ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getStatistic().getSum()));

			// set the custom dialog components - text, image and button

			Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_statistics_button_clear);
			dialogButtonClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ApplicationManager.getInstance().clearStatistics();

					dialog.dismiss();

				}
			});
			Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_statistics_button_print);
			dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {


					ApplicationManager.getInstance().getProtocolPrinter().printTop();
					ApplicationManager.getInstance().getProtocolPrinter().printStatistics();
					ApplicationManager.getInstance().getProtocolPrinter().printBottom();

					Toast.makeText(eContext, "Statistics printed", Toast.LENGTH_LONG).show();
				}
			});


			Button dialogButtonSamples = (Button) dialog.findViewById(R.id.dialog_statistics_button_samples);
			dialogButtonSamples.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showStatisticsSamples(eContext,listener);

				}
			});

			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showStatisticsSamples(final Context eContext, DialogInterface.OnDismissListener listener) {
		try {
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_statistics_samples);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Statistics");
			//           statistic = new SummaryStatistics();
			//           for (Double value : statisticsArray) {
			//               statistic.addValue(value);
			//           }

			ListView listView = (ListView) dialog.findViewById(R.id.dialog_sample_list);

            /*ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = ApplicationManager.getInstance().getStats().getSamples().size()*50;
            listView.setLayoutParams(params);
            listView.requestLayout();
*/
			SamplesAdapter sAdapter= new SamplesAdapter(eContext,new ArrayList<Double>());
			listView.setAdapter(sAdapter);

			for(Double sample:ApplicationManager.getInstance().getStats().getSamples()){
				sAdapter.add(sample);
			}

            /*
            Button dialogShowGraph = (Button) dialog.findViewById(R.id.dialog_statistics_samples_button_showgraph);
            dialogShowGraph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });*/


			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_statistics_samples_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void showIngrediantNotification(final Context eContext, DialogInterface.OnDismissListener listener) {
		try {
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_ingrediantcosts);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Ingredient Costs");
			//           statistic = new SummaryStatistics();
			//           for (Double value : statisticsArray) {
			//               statistic.addValue(value);
			//

			// set the custom dialog components - text, image and button


			ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_ingrediants_List);



			// This is the array adapter, it takes the context of the activity as a
			// first parameter, the type of list view as a second parameter and your
			// array as a third parameter.
			ItemMeasuredAdapter arrayAdapter = new ItemMeasuredAdapter(eContext,new ArrayList<Item>());

			listView.setAdapter(arrayAdapter);
			for(Item item : ApplicationManager.getInstance().getIngrediantCostList()){
				arrayAdapter.add(item);
			}

			//arrayAdapter.add(new Item(ApplicationManager.getInstance().getCurrentItem().getItemArticleNumber(),"ssdd"));




			//arrayAdapter.add("Atricle No.    Name       Cost           Weight    Unit");
			//arrayAdapter.add("Text von Listenelement 2");



			Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_clear);
			dialogButtonClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ApplicationManager.getInstance().setIngrediantTotalWeight(0);
					ApplicationManager.getInstance().setIngrediantUnitCost(0);
					ApplicationManager.getInstance().setIngrediantTotalCost(0);
					ApplicationManager.getInstance().getIngrediantCostList().clear();
					dialog.dismiss();
				}
			});
			Button dialogButtonPrint = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_print);
			dialogButtonPrint.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ApplicationManager.getInstance().getProtocolPrinter().printTop();
					ApplicationManager.getInstance().getProtocolPrinter().printApplicationData();
					ApplicationManager.getInstance().getProtocolPrinter().printBottom();

					Toast.makeText(eContext, "Statistics printed", Toast.LENGTH_LONG).show();
				}
			});
			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_ingrediant_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showRecipeResults(final Context eContext, DialogInterface.OnDismissListener listener) {
		try {
			final Dialog dialog = new Dialog(eContext);
			dialog.setContentView(R.layout.dialog_recipe_results);
			dialog.setOnDismissListener(listener);
			dialog.setTitle("Result of Recipe: ");// + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName());



			ListView listView = listView = (ListView) dialog.findViewById(R.id.dialog_batch_List);



			// This is the array adapter, it takes the context of the activity as a
			// first parameter, the type of list view as a second parameter and your
			// array as a third parameter.

			List<RecipeEntry> entryList=new ArrayList<RecipeEntry>();
			//List<String> entryList= new ArrayList<String>();



			//double formulationTotal = 0;
			double formulationTotalTarget = 0;
			double formulationTotalDifference = 0;
			int formulationcounter = 0;
			while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {
				// formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight();
				// formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();

				entryList.add(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter));


				formulationcounter++;
			}





			RecipeResultElementAdapter recipeAdapter = new RecipeResultElementAdapter(eContext,new ArrayList<RecipeEntry>());
			listView.setAdapter(recipeAdapter);
			for(RecipeEntry recipeEntry:entryList){
				recipeAdapter.add(recipeEntry);
			}



			Button dialogButtonClear = (Button) dialog.findViewById(R.id.dialog_recipe_button_print);
			dialogButtonClear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {


					ApplicationManager.getInstance().getProtocolPrinter().printTop();
					//Printing the application data
					ApplicationManager.getInstance().getProtocolPrinter().printApplicationData();
					//Print Signature liness
					ApplicationManager.getInstance().getProtocolPrinter().printBottom();
					dialog.dismiss();
				}
			});

			Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_recipe_button_close);
			dialogButtonClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

 private void resetSqc(){
	 ApplicationManager.getInstance().setBatchName("");
	 ApplicationManager.getInstance().getBatchList().clear();

	 updateBatchListButtonText();



 }
}


