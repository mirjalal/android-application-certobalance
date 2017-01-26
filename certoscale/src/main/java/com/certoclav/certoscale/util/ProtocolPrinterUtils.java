package com.certoclav.certoscale.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.database.SQC;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.Calendar;
import java.util.Map;

import com.certoclav.certoscale.R;
import com.certoclav.library.application.ApplicationController;


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
		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result:"+ " " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Grosss:" + " " + ApplicationManager.getInstance().getLoadInGramAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Net:" +" " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tare:" +" " + ApplicationManager.getInstance().getTareAsStringWithUnit());
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum Weight:" + " " + ApplicationManager.getInstance().getUnderLimitAsStringWithUnit());
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
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum Weight: "+ ApplicationManager.getInstance().getUnderLimitAsStringInGram()+ " g"+"\n");
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
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("APW: "+ ApplicationManager.getInstance().getAveragePieceWeightAsStringInGram() + " g"+"\n");
				}

				String cmode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_counting_mode),"");
				if  (cmode.equals("2")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_under_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_under_limit)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitPiecesAsString()+" PCS"+"\n");
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
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsString()+ " g"+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsString()+ " g"+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_reference),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_reference))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Reference Weight: " +ApplicationManager.getInstance().getReferenceWeightAsStringInGram()+"\n" );
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Reference Adjust: "+ApplicationManager.getInstance().getCurrentLibrary().getReferenceweightAdjustment()+" %" +"\n" );
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ ApplicationManager.getInstance().getDifferenceInGram() +" g"+"\n");
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
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitCheckWeighingAsString() + " g"+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overlimit)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Over Limit: "+ ApplicationManager.getInstance().getOverLimitCheckWeighingAsString() + " g"+"\n");
					}
				}

				if  (cmode_check.equals("2")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("target: "+ ApplicationManager.getInstance().getCheckNominal() + " g"+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance),ApplicationController.getContext(). getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceUnder() + " g"+"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceOver() + " g"+"\n");
					}
				}

				if (cmode_check.equals("3")){
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("target: "+ ApplicationManager.getInstance().getCheckNominal() + " g"+"\n");
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
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Final Weight: "+ApplicationManager.getInstance().getAnimalWeight() + " g"+"\n");
				break;

			case FILLING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Result: " +ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringInGram()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_target),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_target))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: "+ApplicationManager.getInstance().getTargetasString() +" g"+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_differencew),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_differencew))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getDifferenceFilling() +" g"+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_differencep_visible),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_differencep_visible))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: "+ApplicationManager.getInstance().getFillingDifferenceInPercent()+ " %"+"\n");
				}
				break;

			case TOTALIZATION:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getSum())+" g"+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_samples),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_samples))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples: "+Long.toString(ApplicationManager.getInstance().getStatistic().getN())+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_average),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_average))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMean())+" g"+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_standard),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_standard))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getStandardDeviation())+" g"+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_minimum),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_minimum))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMin())+" g"+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_maximum),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_maximum))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum: "+String.format("%.4f g",ApplicationManager.getInstance().getStatistic().getMax())+" g"+"\n");
				}
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range: "+String.format("%.4f g",(ApplicationManager.getInstance().getStatistic().getMax())-ApplicationManager.getInstance().getStatistic().getMin())+" g"+"\n");

				break;

			case FORMULATION:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Name: "+ ApplicationManager.getInstance().getCurrentRecipe().getRecipeName() +"\n");
				double formulationTotal=0;
				double formulationTotalTarget=0;
				double formulationTotalDifference=0;
				int formulationcounter=0;
				while(formulationcounter<ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()){


					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getName() +"\n");

					formulationTotalTarget=formulationTotalTarget+ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight();
					formulationTotal=formulationTotal+ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();
					double currentdifference=Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight()-ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight())/Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight());
					currentdifference=currentdifference*100;

					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Target: "+String.format("%.4f",ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight())+" g\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Actual: "+String.format("%.4f",ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight())+" g\n");
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Diff(%): "+String.format("%.2f",currentdifference) +" %\n");

					formulationcounter++;
				}


				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_target),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_target))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Target: "+String.format("%.4f g",formulationTotalTarget)+" g"+"\n");
				}

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total))==true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Actual: "+String.format("%.4f g",formulationTotal)+" g"+"\n");
				}

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total))==true) {
					formulationTotalDifference= Math.abs(formulationTotal-formulationTotalTarget)/Math.abs(formulationTotalTarget);
					formulationTotalDifference=formulationTotalDifference*100;
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total Diff: "+String.format("%.2f g",formulationTotalDifference)+" %"+"\n");
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
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Initial: " + String.format("%.4f", ApplicationManager.getInstance().getCurrentItem().getWeight()) + " g" + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_final), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_final)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Final: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencew), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencew)) == true) {
						Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Difference: " + ApplicationManager.getInstance().getDifferenceAsStringInGramWithUnit() + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencep), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencep)) == true) {
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

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_peak_print_stableonly), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_peak_print_stableonly)) == true) {
					Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Stable only : yes\n");
				}
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Stable only : no\n");
				break;

			case INGREDIENT_COSTING:
				break;




			default:
				Toast.makeText(ApplicationController.getContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
		}
	}

	public void printSQCBatch(SQC sqc){

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Statistics of "+sqc.getName()+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples "+String.format("%d",sqc.getStatistics().getN())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average "+String.format("%.4f",sqc.getStatistics().getMean())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum "+String.format("%.4f",sqc.getStatistics().getMax())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum "+String.format("%.4f",sqc.getStatistics().getMin())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range "+String.format("%.4f",(sqc.getStatistics().getMax()-sqc.getStatistics().getMin()))+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation "+String.format("%.4f",sqc.getStatistics().getStandardDeviation())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total "+String.format("%.4f",sqc.getStatistics().getSum())+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("\n");

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Nomininal "+String.format("%.4f",sqc.getNominal())+"\n");

		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("+Tolerance1 "+String.format("%d",sqc.getSqcPT1())+ "   " +String.format("%.1f",((double)sqc.getSqcPT1()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("+Tolerance2 "+String.format("%d",sqc.getSqcPT2())+ "   " +String.format("%.1f",((double)sqc.getSqcPT2()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("-Tolerance1 "+String.format("%d",sqc.getSqcNT1())+ "   " +String.format("%.1f",((double)sqc.getSqcNT1()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("-Tolerance2 "+String.format("%d",sqc.getSqcNT2())+ "   " +String.format("%.1f",((double)sqc.getSqcNT2()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");


	}

	public void printStatistics(){


		switch (Scale.getInstance().getScaleApplication()){


			case PART_COUNTING:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Statistics" + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples " + String.format("%d", ApplicationManager.getInstance().getStatistic().getN()) + " \n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total " + String.format("%.0f", ApplicationManager.getInstance().getStatistic().getSum()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average " + String.format("%.0f", ApplicationManager.getInstance().getStatistic().getMean()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation " + String.format("%.0f", ApplicationManager.getInstance().getStatistic().getStandardDeviation()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum " + String.format("%.0f", ApplicationManager.getInstance().getStatistic().getMin()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum " + String.format("%.0f", ApplicationManager.getInstance().getStatistic().getMax()) + " PCS\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range " + String.format("%.0f", (ApplicationManager.getInstance().getStatistic().getMax() - ApplicationManager.getInstance().getStatistic().getMin())) + " PCS\n");

				break;

			default:
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Statistics" + "\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Samples " + String.format("%d", ApplicationManager.getInstance().getStatistic().getN()) + " \n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Total " + String.format("%.4f", ApplicationManager.getInstance().getStatistic().getSum()) + " g\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Average " + String.format("%.4f", ApplicationManager.getInstance().getStatistic().getMean()) + " g\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Standard Deviation " + String.format("%.4f", ApplicationManager.getInstance().getStatistic().getStandardDeviation()) + " g\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Minimum " + String.format("%.4f", ApplicationManager.getInstance().getStatistic().getMin()) + " g\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Maximum " + String.format("%.4f", ApplicationManager.getInstance().getStatistic().getMax()) + " g\n");
				Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Range " + String.format("%.4f", (ApplicationManager.getInstance().getStatistic().getMax() - ApplicationManager.getInstance().getStatistic().getMin())) + " g\n");
				break;
		}
	}




}
			
			
			
		
		
