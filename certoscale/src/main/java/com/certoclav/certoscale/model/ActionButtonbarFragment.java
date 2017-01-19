package com.certoclav.certoscale.model;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.database.SQC;

import java.util.ArrayList;

import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;
import static com.certoclav.certoscale.model.ScaleApplication.DENSITIY_DETERMINATION;
import static com.certoclav.certoscale.model.ScaleApplication.FILLING_CALC_TARGET;
import static com.certoclav.certoscale.model.ScaleApplication.FORMULATION_RUNNING;
import static com.certoclav.certoscale.model.ScaleApplication.INGREDIENT_COSTING;
import static com.certoclav.certoscale.model.ScaleApplication.PART_COUNTING_CALC_AWP;
import static com.certoclav.certoscale.model.ScaleApplication.PERCENT_WEIGHING_CALC_REFERENCE;
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
			buttonNewBatch.setText("End\n");
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

		View rootView = inflater.inflate(R.layout.actionbar,container, false);

		buttonStart = (Button) rootView.findViewById(R.id.actionbar_button_start);
		buttonStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Scale.getInstance().getScaleApplication()==DENSITIY_DETERMINATION) {
					ApplicationManager.getInstance().setDensity_step_counter(1);
					buttonAccept.setEnabled(true);
					buttonStart.setEnabled(false);
				}
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_START,false);
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

			}
		});
		buttonCal = (Button) rootView.findViewById(R.id.actionbar_button_cal);
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


						Item measuredItem= new Item("",ApplicationManager.getInstance().getCurrentItem().getItemJson());


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

					final String[] name = {""};

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
								name[0] = editText.getText().toString();
								//								ApplicationManager.getInstance().getCurrentLibrary().setUnderLimit(Double.parseDouble(((EditText) dialog.findViewById(R.id.dialog_edit_number_edittext)).getText().toString()));

								SQC Batch = new SQC(ApplicationManager.getInstance().getStatistic(), name[0]);
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

					buttonNewBatch.setText("New Batch");
					ApplicationManager.getInstance().setSqc_state(0);
				}


			}
		});

		buttonShowBatch = (Button) rootView.findViewById(R.id.actionbar_button_view_batch);
		buttonShowBatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for(ButtonEventListener listener : navigationbarListeners){
					listener.onClickNavigationbarButton(BUTTON_INGREDIANTLIST,false);
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


		switch (Scale.getInstance().getScaleApplication()){
			case ANIMAL_WEIGHING_CALCULATING:
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(false);
				break;
			case ANIMAL_WEIGHING:
				buttonStart.setVisibility(View.VISIBLE);
				buttonStart.setEnabled(true);
				break;
			case FORMULATION:
				if (Scale.getInstance().getCurrentRecipe() != null) {
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

			case INGREDIENT_COSTING:
				buttonAccept.setVisibility(View.VISIBLE);
				buttonAccept.setEnabled(true);
				buttonIngrediantList.setVisibility(View.VISIBLE);

				buttonAccumulate.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);
				buttonStart.setVisibility(View.GONE);


				break;

			case DENSITIY_DETERMINATION:
				buttonAccept.setVisibility(View.VISIBLE);
				buttonStart.setVisibility(View.VISIBLE);


				buttonIngrediantList.setVisibility(View.GONE);
				buttonAccumulate.setVisibility(View.GONE);
				buttonStatistics.setVisibility(View.GONE);

				break;

			case STATISTICAL_QUALITY_CONTROL:
				buttonNewBatch.setVisibility(View.VISIBLE);
				buttonShowBatch.setVisibility(View.VISIBLE);


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
			return;
		}


		ApplicationManager.getInstance().clearStatistics();
		getButtonAppSettings().setText("SETTINGS");
		getButtonCal().setEnabled(true);
		getButtonPrint().setEnabled(true);
		getButtonTara().setEnabled(true);
		getButtonAccumulate().setEnabled(true);
		getButtonAppSettings().setEnabled(true);

		if(application == TOTALIZATION){
			getButtonAppSettings().setEnabled(false);
		}


		//handle Statistic Button visibiltiy (visible or gone)
		if(application == ScaleApplication.FORMULATION||
				application == ScaleApplication.FORMULATION_RUNNING || application==ScaleApplication.INGREDIENT_COSTING || application==ScaleApplication.DENSITIY_DETERMINATION){
			getButtonStatistics().setVisibility(View.GONE);
			buttonAccumulate.setVisibility(View.GONE);
		}else{
			buttonStatistics.setVisibility(View.VISIBLE);
			buttonAccumulate.setVisibility(View.VISIBLE);
		}


		if(application != ScaleApplication.STATISTICAL_QUALITY_CONTROL){
			buttonNewBatch.setVisibility(View.GONE);
			buttonShowBatch.setVisibility(View.GONE);
		}else{
			buttonStatistics.setVisibility(View.GONE);
		}




		updateStatsButtonUI();

		
	}

	public void updateStatsButtonUI() {
		getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatistic().getN() + ")");
		if (ApplicationManager.getInstance().getStatistic().getN()==0){
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


