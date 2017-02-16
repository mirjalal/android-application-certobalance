package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.ArrayList;

import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;
import static com.certoclav.certoscale.model.ScaleApplication.DENSITIY_DETERMINATION;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING_CALC_TARGET;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.INGREDIENT_COSTING;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING_CALC_AWP;
import static com.certoclav.certoscale.model.ScaleApplication.PEAK_HOLD;
import static com.certoclav.certoscale.model.ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE;
import static com.certoclav.certoscale.model.ScaleApplication.PIPETTE_ADJUSTMENT;
import static com.certoclav.certoscale.model.ScaleApplication.STATISTICAL_QUALITY_CONTROL;
import static com.certoclav.certoscale.model.ScaleApplication.TOTALIZATION;


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

	public void setButtonStart(Button buttonStart) {
		this.buttonStart = buttonStart;
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
		if (ApplicationManager.getInstance().getSqc_state()==1){
			buttonNewBatch.setText("End\n" + ApplicationManager.getInstance().getBatchName());
		}
		if(ApplicationManager.getInstance().getSqc_state()==0){
			buttonNewBatch.setText("New Batch");
		}
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
				if (Scale.getInstance().getScaleApplication()==DENSITIY_DETERMINATION) {
					ApplicationManager.getInstance().setDensity_step_counter(1);
					buttonAccept.setEnabled(true);
					buttonStart.setEnabled(false);
				}

				if(Scale.getInstance().getScaleApplication()==PEAK_HOLD){
					try{
						//Make sure that only one Button is clickable
						buttonStart.setEnabled(false);
						buttonEnd.setEnabled(true);

						//Start PeakHold Measurement
						ApplicationManager.getInstance().setPeakHoldActivated(true);

					}
					catch (Exception e)
					{
						e.printStackTrace();
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
					//Make sure that only one Button is clickable
					buttonStart.setEnabled(true);
					buttonEnd.setEnabled(false);

					// End PeakHold Measurenment
					ApplicationManager.getInstance().setPeakHoldActivated(false);

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

				if(Scale.getInstance().getScaleApplication()==STATISTICAL_QUALITY_CONTROL){
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

				if(Scale.getInstance().getScaleApplication()==PIPETTE_ADJUSTMENT){
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
						buttonAccumulate.setText("Accept");

						ApplicationManager.getInstance().setPipetteCalculatedML(-pipetteML);

						ApplicationManager.getInstance().setPipette_current_sample(ApplicationManager.getInstance().getPipette_current_sample() + 1);
						if (ApplicationManager.getInstance().getPipette_current_sample() == ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples() + 1) {
							ApplicationManager.getInstance().setPipette_current_sample(0);
							buttonAccumulate.setEnabled(false);
							buttonTara.setEnabled(true);
						}

						ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());

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

				if (Scale.getInstance().getScaleApplication()==PIPETTE_ADJUSTMENT) {
					ApplicationManager.getInstance().setPipette_current_sample(1);
					ApplicationManager.getInstance().getStats().getStatistic().clear();
					ApplicationManager.getInstance().getStats().getSamples().clear();

					updateStatsButtonUI();
					buttonTara.setEnabled(false);
					buttonAccumulate.setEnabled(true);
				}
			}
		});

		buttonAppSettings = (Button) rootView.findViewById(R.id.actionbar_button_settings);
		buttonAppSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
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
				if (Scale.getInstance().getScaleApplication()==INGREDIENT_COSTING){
					buttonAccept.setEnabled(true);
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

					}
				}
				if (Scale.getInstance().getScaleApplication()==DENSITIY_DETERMINATION){
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					String densitymode = prefs.getString(getString(R.string.preferences_density_mode),"");

					if (ApplicationManager.getInstance().getDensity_step_counter()==2){
						ApplicationManager.getInstance().setDensity_weight_liquid(ApplicationManager.getInstance().getTaredValueInGram());
						ApplicationManager.getInstance().setDensity_step_counter(3);
						buttonAccept.setEnabled(false);
						buttonStart.setEnabled(true);
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




				}
			}
		});

		buttonIngrediantList = (Button) rootView.findViewById(R.id.actionbar_button_Ingredient_List);
		buttonIngrediantList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_INGREDIANTLIST,false);
				}

			}
		});


		buttonNewBatch = (Button) rootView.findViewById(R.id.actionbar_button_new_batch);
		buttonNewBatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (ApplicationManager.getInstance().getSqc_state()==0) {



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

								ApplicationManager.getInstance().setSqc_state(1);

								dialog.dismiss();
								onResume();


							}
						});

						dialog.show();

					} catch (Exception e) {
						e.printStackTrace();
					}



				}
				if (ApplicationManager.getInstance().getSqc_state()==1){

					//Data from one Batch
					//SummaryStatistics currentStatistics = new SummaryStatistics();
					//ApplicationManager.getInstance().getStatistic().copy();
					//currentStatistics=ApplicationManager.getInstance().getStatistic();

					SQC currentBatch= new SQC(ApplicationManager.getInstance().getStats().getStatistic().copy(),ApplicationManager.getInstance().getBatchName(),
							ApplicationManager.getInstance().getCurrentLibrary().getSQCNominal(),
							ApplicationManager.getInstance().getSqcPT1(),ApplicationManager.getInstance().getSqcPT2(),ApplicationManager.getInstance().getSqcNT1(),
							ApplicationManager.getInstance().getSqcNT2());
					//currentBatch.setName(ApplicationManager.getInstance().getBatchName());
					//currentBatch.setStatistics(ApplicationManager.getInstance().getStatistic());

					ApplicationManager.getInstance().getBatchList().add(currentBatch);
					ApplicationManager.getInstance().getStats().getStatistic().clear();


					buttonNewBatch.setText("New Batch");
					buttonAccumulate.setEnabled(false);
					ApplicationManager.getInstance().setSqc_state(0);
					ApplicationManager.getInstance().setSqcPT1(0);
					ApplicationManager.getInstance().setSqcPT2(0);
					ApplicationManager.getInstance().setSqcNT1(0);
					ApplicationManager.getInstance().setSqcNT2(0);

				}


			}
		});

		buttonShowBatch = (Button) rootView.findViewById(R.id.actionbar_button_view_batch);
		buttonShowBatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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

		buttonPrint.setEnabled(true);

		switch (Scale.getInstance().getScaleApplication()){

			case WEIGHING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;

			case PART_COUNTING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;

			case PERCENT_WEIGHING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;

			case CHECK_WEIGHING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;



			case ANIMAL_WEIGHING_CALCULATING:
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(false);
				break;
			case ANIMAL_WEIGHING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAccumulate.setText("ADD TO STATS");

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(true);
				break;

			case FILLING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;

			case TOTALIZATION:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;

			case FORMULATION:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonPrint.setEnabled(false);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.VISIBLE);
				buttonShowBatch.setText("Show Results");

				if (ApplicationManager.getInstance().getCurrentRecipe() != null) {
					buttonStart.setEnabled(true);
					buttonStart.setVisibility(View.VISIBLE);
				} else {
					buttonStart.setEnabled(false);
					buttonStart.setVisibility(View.VISIBLE);
				}
				break;
			case FORMULATION_RUNNING:
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(false);
				buttonCal.setEnabled(false);
				buttonPrint.setEnabled(false);
				buttonTara.setEnabled(false);
				buttonAppSettings.setEnabled(false);
				break;


			case DIFFERENTIAL_WEIGHING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAccumulate.setVisibility(View.VISIBLE);

				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				break;

			case INGREDIENT_COSTING:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);


				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.VISIBLE);
				buttonAccept.setEnabled(true);
				buttonIngrediantList.setVisibility(View.VISIBLE);

				buttonAccumulate.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);


				break;

			case DENSITIY_DETERMINATION:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonAppSettings.setVisibility(View.VISIBLE);

				buttonAccept.setVisibility(View.VISIBLE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.VISIBLE);
				buttonStart.setVisibility(View.VISIBLE);


				buttonIngrediantList.setVisibility(View.GONE);


				break;
			case PEAK_HOLD:
				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);

				buttonStart.setVisibility(View.VISIBLE);



				buttonStatistics.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonAppSettings.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);


				buttonEnd.setVisibility(View.VISIBLE);
				//get PeakHoldMode
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				String PeakHoldMode = prefs.getString(getString(R.string.preferences_peak_mode),"");
				//Semi Automatic Mode
				if(PeakHoldMode.equals("2")){

					buttonStart.setEnabled(false);
					buttonEnd.setEnabled(true);
					//Start PeakHold Measurement
					ApplicationManager.getInstance().setPeakHoldActivated(true);
				}

				//Automatic Mode
				if(PeakHoldMode.equals("3")){

					buttonStart.setEnabled(false);
					buttonEnd.setEnabled(false);
					//Start PeakHold Measurement
					ApplicationManager.getInstance().setPeakHoldActivated(true);
				}


				break;

			case PIPETTE_ADJUSTMENT:

				buttonTara.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonStatistics.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonNewBatch.setVisibility(View.GONE);
				buttonShowBatch.setVisibility(View.GONE);

				ApplicationManager.getInstance().setPipette_current_sample(0);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);
				buttonAccumulate.setText("Accept");
				buttonAccumulate.setEnabled(false);
				break;


			case STATISTICAL_QUALITY_CONTROL:
				buttonTara.setVisibility(View.VISIBLE);
				buttonZero.setVisibility(View.VISIBLE);
				buttonPrint.setVisibility(View.GONE);


				buttonAccumulate.setVisibility(View.VISIBLE);
				buttonAppSettings.setVisibility(View.VISIBLE);
				buttonIngrediantList.setVisibility(View.GONE);

				buttonNewBatch.setVisibility(View.VISIBLE);
				buttonShowBatch.setVisibility(View.VISIBLE);
				buttonShowBatch.setText("Show Batch \n List");

				buttonAccumulate.setEnabled(false);
				buttonStart.setVisibility(View.GONE);


				buttonIngrediantList.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);

				buttonStatistics.setVisibility(View.GONE);




				break;




			default:
				buttonStart.setVisibility(View.GONE);
				buttonAccept.setVisibility(View.GONE);
				buttonIngrediantList.setVisibility(View.GONE);



		}


		//do not react on subApplications
		if(application == PART_COUNTING_CALC_AWP ||
				application == PERCENT_WEIGHING_CALC_REFERENCE ||
				application == ANIMAL_WEIGHING_CALCULATING ||
				application == FILLING_CALC_TARGET ||
				application == FORMULATION_RUNNING){
			ApplicationManager.getInstance().setReturnFromSubMenu(true);
			return;
		}else{
			if (ApplicationManager.getInstance().isReturnFromSubMenu()==true){
				ApplicationManager.getInstance().setReturnFromSubMenu(false);
				return;
			}


		}


		ApplicationManager.getInstance().clearStatistics();


		getButtonAppSettings().setText("SETTINGS");
		getButtonCal().setEnabled(true);
		getButtonPrint().setEnabled(true);
		getButtonTara().setEnabled(true);
		if (application!=PIPETTE_ADJUSTMENT){
		getButtonAccumulate().setEnabled(true);

		}
		getButtonAppSettings().setEnabled(true);

		if(application == TOTALIZATION){
			getButtonAppSettings().setEnabled(false);
		}

		if(application != PEAK_HOLD){
			buttonEnd.setVisibility(View.GONE);
		}

		//handle Statistic Button visibiltiy (visible or gone)
		if(application == ScaleApplication.FORMULATION||
				application == ScaleApplication.FORMULATION_RUNNING || application==ScaleApplication.INGREDIENT_COSTING ||
				application==ScaleApplication.DENSITIY_DETERMINATION || application==ScaleApplication.PEAK_HOLD ){
			getButtonStatistics().setVisibility(View.GONE);
			buttonAccumulate.setVisibility(View.GONE);
		}else{
			buttonStatistics.setVisibility(View.VISIBLE);
			buttonAccumulate.setVisibility(View.VISIBLE);
		}

		if(application != ScaleApplication.STATISTICAL_QUALITY_CONTROL && application!=FORMULATION){
			buttonNewBatch.setVisibility(View.GONE);
			buttonShowBatch.setVisibility(View.GONE);

		}else{
			buttonStatistics.setVisibility(View.GONE);
			if (ApplicationManager.getInstance().getSqc_state()==0){
				buttonAccumulate.setEnabled(false);
			}

		}




		updateStatsButtonUI();

		
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
}


