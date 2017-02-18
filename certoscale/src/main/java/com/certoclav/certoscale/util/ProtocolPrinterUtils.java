package com.certoclav.certoscale.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;

import java.util.Calendar;
import java.util.List;


public class ProtocolPrinterUtils {


public ProtocolPrinterUtils() {

	}


	public  void printProtocol(){
		printStatistics();
		printApplicationData();
		printTop();
		printBottom();
		printHeader("");
		printDate();
		printBalanceId();
		printBalanceName("");
		printUserName();
		printProjectName("");
		printApplicationName();
		printResults();
		printSignature();
	}


	public void printBottom(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
	if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_signature),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_signature))==true) {
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
		printSignature();
	}

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
	}



	public void printTop() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());

		//Print GLP and GMP Data which is independent of the application
		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_header),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_header))==true) {

			String header=prefs.getString("preferences_glp_header","");
			printHeader(header+"\n");
		}

		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_date),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_date))==true) {
			printDate();
		}

		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_balance_id),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_balance_id))==true) {
			printBalanceId();
		}

		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_balance_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_balance_name))==true) {
			String balanceName=prefs.getString("preferences_glp_balance_name","");
			printBalanceName(balanceName);
		}

		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_user_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_user_name))==true) {
			printUserName();
		}
		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_project_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_project_name))==true) {
			String projectName=prefs.getString("preferences_glp_project_name","");
			printProjectName(projectName);
		}

		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_application_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_application_name))==true) {
			printApplicationName();
		}

	}

public void  printHeader(  String header){

	//Log.e("Print Header", header.getText().toString());

	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(header);


/*
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 1\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 2\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 3\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 4\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 5\n");
*/
}

	public void printDate(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(Calendar.getInstance().getTime().toString() + "\n");
	}

	public void printBalanceId(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Serialnumber: "+Scale.getInstance().getSerialnumber() + "\n");
	}

	public void printBalanceName(String balanceName){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Balance Name:" + " "+balanceName + "\n");
	}

	public void printUserName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("User Name:" + " "+ Scale.getInstance().getUser().getFirstName() + " " +Scale.getInstance().getUser().getLastName() + "\n");
	}

	public void printProjectName(String projectName){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Project Name:"+projectName+ "\n");
	}

	public void printApplicationName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Application:"+" "+Scale.getInstance().getScaleApplication().toString().replace("_", " ")+"\n");
	}

	public void printResults(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());


		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING:

				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result:"+ " " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Grosss:" + " " + ApplicationManager.getInstance().getLoadInGramAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Net:" +" " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tare:" +" " + ApplicationManager.getInstance().getTareAsStringWithUnit());

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_weigh_tara_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_weigh_tara_visible)) == true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum Weight:" + " " + ApplicationManager.getInstance().getUnderLimitAsStringWithUnit());
				}
				break;
			default:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Todo: printResults()"); //TODO
		}
	}

	public void printSignature(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Signature:_______________" + "\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Verified by:_______________" + "\n");
	}

	public void printApplicationData(){

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit() +"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_weigh_print_min),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_weigh_print_min))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum Weight: "+ ApplicationManager.getInstance().getUnderLimitAsStringWithUnit()+"\n");
				}
				break;

			case PART_COUNTING:

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_sample_size),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_sample_size))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram() +"\n");
				}
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_apw),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_apw))==true){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("APW: "+ ApplicationManager.getInstance().getAveragePieceWeightAsStringWithUnit() +"\n");
				}

				String cmode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_counting_mode),"");
				if  (cmode.equals("2")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_under_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_under_limit)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitPiecesAsString()+" pcs"+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_over_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_over_limit)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getOverlimitPiecesAsString()+" PCS"+"\n");
					}
				}
				if  (cmode.equals("3")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_target)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: "+ ApplicationManager.getInstance().getTargetPiecesAsString()+" PCS"+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_difference_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_difference_visible)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ ApplicationManager.getInstance().getDifferenceAsString()+" PCS"+"\n");
					}
				}
				break;

			case PERCENT_WEIGHING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Percentage: " +ApplicationManager.getInstance().getPercent()+ " %"+"\n" );
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_reference),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_reference))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Reference Weight: " +ApplicationManager.getInstance().getReferenceWeightAsStringInGram()+"\n" );
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Reference Adjust: "+ApplicationManager.getInstance().getCurrentLibrary().getReferenceweightAdjustment()+" %" +"\n" );
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ ApplicationManager.getInstance().getDifferenceAsStringInGram() +"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference_percent),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference_percent))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getDifferenceInPercent()+ " %"+"\n");
				}
				break;

			case CHECK_WEIGHING:
				String cmode_check = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_check_limitmode),"");
				String checklimitmode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_check_limitmode),"");
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
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_underlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_underlimit)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitCheckWeighingAsStringWithUnit()+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overlimit)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Over Limit: "+ ApplicationManager.getInstance().getOverLimitCheckWeighingAsStringWithUnit()+"\n");
					}
				}

				if  (cmode_check.equals("2")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("target: "+ ApplicationManager.getInstance().getCheckNominalAsStringWithUnit()+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance),ApplicationController.getContext(). getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceUnderAsStringWithUnit()+"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceOverAsStringWithUnit()+"\n");
					}
				}

				if (cmode_check.equals("3")){
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("target: "+ ApplicationManager.getInstance().getCheckNominalAsStringWithUnit() +"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance),ApplicationController.getContext(). getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceUnderPercent() + " %"+"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceOverPercent() + " %"+"\n");
					}


				}
				break;

			case ANIMAL_WEIGHING:
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_animal_print_measuring),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_animal_print_measuring))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Averaging Time: "+ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime() + " s"+"\n");
				}
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Final Weight: "+ApplicationManager.getInstance().getAnimalWeightAsStringWithUnit()+"\n");
				break;

			case FILLING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: " +ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_target),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_target))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: "+ApplicationManager.getInstance().getTargetAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_differencew),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_differencew))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getDifferenceFillingAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_differencep_visible),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_differencep_visible))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getFillingDifferenceInPercent()+ " %"+"\n");
				}
				break;

			case TOTALIZATION:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total: "+ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_samples),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_samples))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples: "+Long.toString(ApplicationManager.getInstance().getStats().getStatistic().getN())+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_average),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_average))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average: "+ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_standard),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_standard))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation: "+ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_minimum),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_minimum))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum: "+ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_maximum),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_maximum))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum: "+ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit()+"\n");
				}
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range: "+ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit()+"\n");


				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("---Sample Data---\n");
				for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Item "+String.format("%d",i)+" "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i))+"\n");

				}

				break;

			case FORMULATION:
				if(ApplicationManager.getInstance().getCurrentRecipeEntry() != null) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Name: " + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName() + "\n");
					double formulationTotal = 0;
					double formulationTotalTarget = 0;
					double formulationTotalDifference = 0;
					int formulationcounter = 0;
					while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {


						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getName() + "\n");

						formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight();
						formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();
						double currentdifference = Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight() - ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight()) / Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight());
						currentdifference = currentdifference * 100;

						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight()) + "\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Actual: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight()) + "\n");
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Diff(%): " + String.format("%.2f", currentdifference) + " %\n");

						formulationcounter++;
					}


					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_target)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Target: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotalTarget) + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Actual: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotal) + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
						formulationTotalDifference = Math.abs(formulationTotal - formulationTotalTarget) / Math.abs(formulationTotalTarget);
						formulationTotalDifference = formulationTotalDifference * 100;
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Diff: " + String.format("%.2f", formulationTotalDifference) + " %" + "\n");
					}
				}else{

					Toast.makeText(ApplicationController.getContext(), "No results to print", Toast.LENGTH_LONG).show();
				}


				break;

			case DIFFERENTIAL_WEIGHING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: " +ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

				if(ApplicationManager.getInstance().getCurrentItem() != null) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Item Name: " + ApplicationManager.getInstance().getCurrentItem().getName() + "\n");
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_initial), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_initial)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Initial: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentItem().getWeight()) +  "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_final), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_final)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Final: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencew), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencew)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: " + ApplicationManager.getInstance().getDifferenceAsStringInGramWithUnit() + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencep), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencep)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: " + ApplicationManager.getInstance().getDifferenceToInitialInPercentAsString() + "\n");
					}
				}
				break;
			case DENSITY_DETERMINATION:
				String densityliquidtype = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_density_liquidtyp), "");
				String densitymode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_density_mode), "");


				if (densitymode.equals("1")||densitymode.equals("2")){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Solid");
				}else if (densitymode.equals("3")){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Liquid");
				}

					if (densityliquidtype.equals("1")) {
						ApplicationManager.getInstance().getCurrentLibrary().setDensityLiquidDensity(ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()));
					}

					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Denisty Determ: " + String.format("%.4f", ApplicationManager.getInstance().getDensity()) + " g/cm3" + "\n");


					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: " + ApplicationManager.getInstance().getTaredValueAsStringInGram() + "\n");

					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Weight in Air: " + String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_air()) + "\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Weight in Liquid: " + String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_liquid()) + "\n");

					if (densityliquidtype.equals("1")) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Water Temp: " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()) + "\n");
					}

					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Liquid Density: " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity()) + " g/cm3" + "\n");





				break;
			case PEAK_HOLD_STARTED:
			case PEAK_HOLD:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Peak Weight: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getPeakHoldMaximum())+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_peak_print_stableonly), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_peak_print_stableonly)) == true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Stable only : yes\n");
				}
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Stable only : no\n");
				break;

			case INGREDIENT_COSTING:

				List<Item> costList=ApplicationManager.getInstance().getIngrediantCostList();



				for(int i=0; i<costList.size();i++){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage( costList.get(i).getName()+"\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Item weight: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(costList.get(i).getWeight())+"\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Item cost: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(costList.get(i).getCost())+"\n");
				}


				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("------------------\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Items: " +costList.size()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total weight: " +ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getIngrediantTotalWeight())+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total cost: " +ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getIngrediantTotalCost())+"\n");


				break;

			case PIPETTE_ADJUSTMENT:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Pipette Name:"+ApplicationManager.getInstance().getPipette_name());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Pipette Number:"+ApplicationManager.getInstance().getPipette_number());

				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Nominal Volume: "+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Water Temp: "+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Pressure"+ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure());


				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Inaccuracy: \n");

				double meanError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getMean()-ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Mean Error:"+String.format("%.4f",meanError)+" ml\n");
				double meanErrorPercent=Math.abs(meanError/ApplicationManager.getInstance().getStats().getStatistic().getMean())*100;
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Mean Error %:"+String.format("%.4f",meanErrorPercent)+" %\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Limit %:"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy())+" %\n");


				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Impreccision:\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation:"+String.format("%.4f",ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation())+" ml\n");
				double standardError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()/ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())*100;
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Error CS%:"+String.format("%.4f",standardError)+" %\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Limit CV:"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())+" %\n");

				if (meanErrorPercent<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy() && standardError<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: Pass");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				}else{
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: Fail");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				}

				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Number of Samples"+ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()+"\n");

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

				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("> +2s:"+pstandard2+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("> +2s:"+pstandard1+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("*+1S > Mean > –1S:"+(ApplicationManager.getInstance().getStats().getSamples().size()-pstandard1-pstandard2-nstandard1-nstandard2)+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("- +2s:"+nstandard1+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("- +2s:"+nstandard2+"\n");

				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");


				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("---Sample Data---\n");
				for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Item "+String.format("%d",i)+" "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i))+"\n");

				}

				break;




			default:
				Toast.makeText(ApplicationController.getContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
		}
	}

	public void printSQCBatch(SQC sqc){

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Statistics of "+sqc.getName()+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples "+String.format("%d",sqc.getStatistics().getN())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMean())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMin())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax()-sqc.getStatistics().getMin()) +"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getStandardDeviation())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getSum())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Nomininal "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getNominal())+"\n");

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("+Tolerance1 "+String.format("%d",sqc.getSqcPT1())+ "   " +String.format("%.1f",((double)sqc.getSqcPT1()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("+Tolerance2 "+String.format("%d",sqc.getSqcPT2())+ "   " +String.format("%.1f",((double)sqc.getSqcPT2()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("-Tolerance1 "+String.format("%d",sqc.getSqcNT1())+ "   " +String.format("%.1f",((double)sqc.getSqcNT1()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("-Tolerance2 "+String.format("%d",sqc.getSqcNT2())+ "   " +String.format("%.1f",((double)sqc.getSqcNT2()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");


	}

	public void printStatistics(){


		switch (Scale.getInstance().getScaleApplication()){


			case PART_COUNTING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Statistics" + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples " + String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()) + " \n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getSum()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMean()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMin()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMax()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range " + String.format("%.0f", (ApplicationManager.getInstance().getStats().getStatistic().getMax() - ApplicationManager.getInstance().getStats().getStatistic().getMin())) + " PCS\n");

				break;

			default:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Statistics" + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples " + String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()) + " \n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total " + ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit() + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average " + ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit() + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation " +ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit() + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum " + ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit() + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum " + ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit() + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range " + ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit() + "\n");
				break;
		}
	}




}
			
			
			
		
		
