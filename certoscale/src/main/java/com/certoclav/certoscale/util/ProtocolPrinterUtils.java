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
private ESCPos posPrinter = null;

public ProtocolPrinterUtils() {
posPrinter = new ESCPos();
	}


	public  void printProtocol(){
		printTop();
		printApplicationData();
		printBottom();
	}

	public void printText(String text){
		posPrinter.printString(text);
	}
	public String getProtocolFooter(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
		StringBuilder sb = new StringBuilder();

		if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_signature),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_signature))==true) {
			sb.append("\n");
			sb.append("Signature:_______________" + "\n");
			sb.append("Verified by:_______________" + "\n");
		}

		sb.append("\n");
		sb.append("\n");
		sb.append("\n");

		return sb.toString();

	}
	public String getProtocolHeader(){

		StringBuilder  sb = new StringBuilder();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());

			//Print GLP and GMP Data which is independent of the application
			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_header),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_header))==true) {

				sb.append(prefs.getString("preferences_glp_header","") + "\n");
			}

			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_date),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_date))==true) {
				sb.append(Calendar.getInstance().getTime().toGMTString() + "\n");
			}

			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_balance_id),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_balance_id))==true) {
				sb.append("Serialnumber: "+Scale.getInstance().getSerialnumber() + "\n");

			}

			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_balance_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_balance_name))==true) {
				String balanceName=prefs.getString("preferences_glp_balance_name","");
				sb.append("Balance Name: " + " "+balanceName + "\n");
			}

			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_user_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_user_name))==true) {
				sb.append("User Name: " + " "+ Scale.getInstance().getUser().getFirstName() + " " +Scale.getInstance().getUser().getLastName() + "\n");

			}
			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_project_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_project_name))==true) {
				String projectName=prefs.getString("preferences_glp_project_name","");
				sb.append("Project Name: "+projectName+ "\n");
			}

			if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_application_name),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_application_name))==true) {
				switch (Scale.getInstance().getScaleApplication()){
					case PIPETTE_ADJUSTMENT_1_HOME:
					case PIPETTE_ADJUSTMENT_2_ACCEPT_ALL_SAMPLES:
					case PIPETTE_ADJUSTMENT_3_FINISHED:
						sb.append("Application: PIPETTE ADJUSTMENT\n");
						break;

				}

			}
		return sb.toString();


	}


	public String getPipetteAdjustmentProtocol(){
		StringBuilder sb = new StringBuilder();
		sb.append(getProtocolHeader());
		sb.append("Pipette Name: "+ApplicationManager.getInstance().getPipette_name() + "\n");
		sb.append("Pipette Number: "+ApplicationManager.getInstance().getPipette_number()+ "\n");

		sb.append("Nominal Volume: "+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal()+ "\n");
		sb.append("Water Temp: "+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp()+ "\n");
		sb.append("Pressure: "+ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure()+ "\n");


		sb.append("\n");
		sb.append("Inaccuracy: \n");

		double meanError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getMean()-ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
		sb.append("Mean Error: "+String.format("%.4f",meanError)+" ml\n");
		double meanErrorPercent=Math.abs(meanError/ApplicationManager.getInstance().getStats().getStatistic().getMean())*100;
		sb.append("Mean Error: "+String.format("%.4f",meanErrorPercent)+" %\n");
		sb.append("Limit: "+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy())+" %\n");


		sb.append("\n");
		sb.append("Imprecision:\n");
		sb.append("Standard Deviation: "+String.format("%.4f",ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation())+" ml\n");
		double standardError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()/ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())*100;
		sb.append("Error CS: "+String.format("%.4f",standardError)+" %\n");
		sb.append("Limit CV: "+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())+" %\n");

		if (meanErrorPercent<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy() && standardError<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()){
			sb.append("\n");
			sb.append("Result: Pass");
			sb.append("\n");
		}else{
			sb.append("\n");
			sb.append("Result: Fail");
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("Number of Samples: "+ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()+"\n");

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

		sb.append("> +2s: "+pstandard2+"\n");
		sb.append("> +2s: "+pstandard1+"\n");
		sb.append("*+1S > Mean > –1S: "+(ApplicationManager.getInstance().getStats().getSamples().size()-pstandard1-pstandard2-nstandard1-nstandard2)+"\n");
		sb.append("- +2s: "+nstandard1+"\n");
		sb.append("- +2s: "+nstandard2+"\n");

		sb.append("\n");


		sb.append("---Sample Data---\n");
		for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
			sb.append("Item "+String.format("%02d: ",i)+" "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i))+"\n");

		}

		sb.append(getProtocolFooter());
		return sb.toString();
	}
	public void printBottom(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
	if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_print_signature),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_print_signature))==true) {
		posPrinter.printString("\n");
		printSignature();
	}

		posPrinter.printString("\n");
		posPrinter.printString("\n");
		posPrinter.printString("\n");
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

	posPrinter.printString(header);

}

	public void printDate(){
		posPrinter.printString(Calendar.getInstance().getTime().toString() + "\n");
	}

	public void printBalanceId(){
		posPrinter.printString("Serialnumber: "+Scale.getInstance().getSerialnumber() + "\n");
	}

	public void printBalanceName(String balanceName){
		posPrinter.printString("Balance Name:" + " "+balanceName + "\n");
	}

	public void printUserName(){
		posPrinter.printString("User Name:" + " "+ Scale.getInstance().getUser().getFirstName() + " " +Scale.getInstance().getUser().getLastName() + "\n");
	}

	public void printProjectName(String projectName){
		posPrinter.printString("Project Name:"+projectName+ "\n");
	}

	public void printApplicationName(){
		posPrinter.printString("Application:"+" "+Scale.getInstance().getScaleApplication().toString().replace("_", " ")+"\n");
	}

	public void printResults(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());


		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING:

				posPrinter.printString("Result:"+ " " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
				posPrinter.printString("Grosss:" + " " + ApplicationManager.getInstance().getSumAsStringWithUnit());
				posPrinter.printString("Net:" +" " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
				posPrinter.printString("Tare:" +" " + ApplicationManager.getInstance().getTareAsStringWithUnit());

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_weigh_tara_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_weigh_tara_visible)) == true) {
					posPrinter.printString("Minimum Weight:" + " " + ApplicationManager.getInstance().getUnderLimitAsStringWithUnit());
				}
				break;
			default:
				posPrinter.printString("Todo: printResults()"); //TODO
		}
	}

	public void printSignature(){
		posPrinter.printString("Signature:_______________" + "\n");
		posPrinter.printString("Verified by:_______________" + "\n");
	}

	public void printApplicationData(){

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
		switch (Scale.getInstance().getScaleApplication()){
			case WEIGHING:
				posPrinter.printString("Result: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit() +"\n");
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_weigh_print_min),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_weigh_print_min))==true) {
					posPrinter.printString("Minimum Weight: "+ ApplicationManager.getInstance().getUnderLimitAsStringWithUnit()+"\n");
				}
				break;

			case PART_COUNTING:

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_sample_size),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_sample_size))==true) {
					posPrinter.printString("Result: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit() +"\n");
				}
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_apw),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_apw))==true){
					posPrinter.printString("APW: "+ ApplicationManager.getInstance().getAveragePieceWeightAsStringWithUnit() +"\n");
				}

				String cmode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_counting_mode),"");
				if  (cmode.equals("2")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_under_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_under_limit)) == true) {
						posPrinter.printString("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitPiecesAsString()+" pcs"+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_over_limit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_over_limit)) == true) {
						posPrinter.printString("Under Limit: "+ ApplicationManager.getInstance().getOverlimitPiecesAsString()+" PCS"+"\n");
					}
				}
				if  (cmode.equals("3")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_print_target)) == true) {
						posPrinter.printString("Target: "+ ApplicationManager.getInstance().getTargetPiecesAsString()+" PCS"+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_counting_difference_visible), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_counting_difference_visible)) == true) {
						posPrinter.printString("Difference: "+ ApplicationManager.getInstance().getDifferenceAsString()+" PCS"+"\n");
					}
				}
				break;

			case PERCENT_WEIGHING:
				posPrinter.printString("Percentage: " +ApplicationManager.getInstance().getPercent()+ " %"+"\n" );
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");

				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_reference),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_reference))==true) {
					posPrinter.printString("Reference Weight: " +ApplicationManager.getInstance().getReferenceWeightAsStringWithUnit()+"\n" );
					posPrinter.printString("Reference Adjust: "+ApplicationManager.getInstance().getCurrentLibrary().getReferenceweightAdjustment()+" %" +"\n" );
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference))==true) {
					posPrinter.printString("Difference: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDifferenceInGram()) +"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_percent_print_difference_percent),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_percent_print_difference_percent))==true) {
					posPrinter.printString("Difference: "+ApplicationManager.getInstance().getDifferenceInPercent()+ " %"+"\n");
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

				if (current<under){posPrinter.printString("Result: Under"+"\n");}
				if (current>over){posPrinter.printString("Result: Over"+"\n");}
				if (current>=under && current<=over){posPrinter.printString("Result: Accept"+"\n");}

				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");

				if  (cmode_check.equals("1")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_underlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_underlimit)) == true) {
						posPrinter.printString("Under Limit: "+ ApplicationManager.getInstance().getUnderLimitCheckWeighingAsStringWithUnit()+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overlimit), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overlimit)) == true) {
						posPrinter.printString("Over Limit: "+ ApplicationManager.getInstance().getOverLimitCheckWeighingAsStringWithUnit()+"\n");
					}
				}

				if  (cmode_check.equals("2")) {
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
						posPrinter.printString("target: "+ ApplicationManager.getInstance().getCheckNominalAsStringWithUnit()+"\n");
					}
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance),ApplicationController.getContext(). getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
						posPrinter.printString("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceUnderAsStringWithUnit()+"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
						posPrinter.printString("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceOverAsStringWithUnit()+"\n");
					}
				}

				if (cmode_check.equals("3")){
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_target)) == true) {
						posPrinter.printString("target: "+ ApplicationManager.getInstance().getCheckNominalAsStringWithUnit() +"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_undertolerance),ApplicationController.getContext(). getResources().getBoolean(R.bool.preferences_check_print_undertolerance)) == true) {
						posPrinter.printString("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceUnderPercent() + " %"+"\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_check_print_overtolerance), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_check_print_overtolerance)) == true) {
						posPrinter.printString("Under Tolerance: "+ApplicationManager.getInstance().getCheckNominalToleranceOverPercent() + " %"+"\n");
					}


				}
				break;

			case ANIMAL_WEIGHING:
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_animal_print_measuring),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_animal_print_measuring))==true) {
					posPrinter.printString("Averaging Time: "+ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime() + " s"+"\n");
				}
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");
				posPrinter.printString("Final Weight: "+ApplicationManager.getInstance().getAnimalWeightAsStringWithUnit()+"\n");
				break;

			case FILLING:
				posPrinter.printString("Result: " +ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_target),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_target))==true) {
					posPrinter.printString("Target: "+ApplicationManager.getInstance().getTargetAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_print_differencew),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_print_differencew))==true) {
					posPrinter.printString("Difference: "+ApplicationManager.getInstance().getDifferenceFillingAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_filling_differencep_visible),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_filling_differencep_visible))==true) {
					posPrinter.printString("Difference: "+ApplicationManager.getInstance().getFillingDifferenceInPercent()+ " %"+"\n");
				}
				break;

			case TOTALIZATION:
				posPrinter.printString("Total: "+ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit()+"\n");
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_samples),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_samples))==true) {
					posPrinter.printString("Samples: "+Long.toString(ApplicationManager.getInstance().getStats().getStatistic().getN())+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_average),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_average))==true) {
					posPrinter.printString("Average: "+ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_standard),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_standard))==true) {
					posPrinter.printString("Standard Deviation: "+ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_minimum),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_minimum))==true) {
					posPrinter.printString("Minimum: "+ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit()+"\n");
				}
				if  (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_totalization_print_maximum),ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_totalization_print_maximum))==true) {
					posPrinter.printString("Maximum: "+ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit()+"\n");
				}
				posPrinter.printString("Range: "+ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit()+"\n");


				posPrinter.printString("---Sample Data---\n");
				for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
					posPrinter.printString("Item "+String.format("%d",i)+" "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i))+"\n");

				}

				break;

			case FORMULATION:
				if(ApplicationManager.getInstance().getCurrentRecipeEntry() != null) {
					posPrinter.printString("Name: " + ApplicationManager.getInstance().getCurrentRecipe().getRecipeName() + "\n");
					double formulationTotal = 0;
					double formulationTotalTarget = 0;
					double formulationTotalDifference = 0;
					int formulationcounter = 0;
					while (formulationcounter < ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().size()) {


						posPrinter.printString(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getName() + "\n");

						formulationTotalTarget = formulationTotalTarget + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight();
						formulationTotal = formulationTotal + ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight();
						double currentdifference = Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight() - ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight()) / Math.abs(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight());
						currentdifference = currentdifference * 100;

						posPrinter.printString("Target: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getWeight()) + "\n");
						posPrinter.printString("Actual: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentRecipe().getRecipeEntries().get(formulationcounter).getMeasuredWeight()) + "\n");
						posPrinter.printString("Diff(%): " + String.format("%.2f", currentdifference) + " %\n");

						formulationcounter++;
					}


					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_target), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_target)) == true) {
						posPrinter.printString("Total Target: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotalTarget) + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
						posPrinter.printString("Total Actual: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(formulationTotal) + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_formulation_print_total), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_formulation_print_total)) == true) {
						formulationTotalDifference = Math.abs(formulationTotal - formulationTotalTarget) / Math.abs(formulationTotalTarget);
						formulationTotalDifference = formulationTotalDifference * 100;
						posPrinter.printString("Total Diff: " + String.format("%.2f", formulationTotalDifference) + " %" + "\n");
					}
				}else{

					Toast.makeText(ApplicationController.getContext(), "No results to print", Toast.LENGTH_LONG).show();
				}


				break;

			case DIFFERENTIAL_WEIGHING:
				posPrinter.printString("Result: " +ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");

				if(ApplicationManager.getInstance().getCurrentItem() != null) {
					posPrinter.printString("Item Name: " + ApplicationManager.getInstance().getCurrentItem().getName() + "\n");
					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_initial), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_initial)) == true) {
						posPrinter.printString("Initial: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getCurrentItem().getWeight()) +  "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_final), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_final)) == true) {
						posPrinter.printString("Final: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencew), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencew)) == true) {
						posPrinter.printString("Difference: " + ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getDifferenceInGram()) + "\n");
					}

					if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_differential_print_differencep), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_differential_print_differencep)) == true) {
						posPrinter.printString("Difference: " + ApplicationManager.getInstance().getDifferenceToInitialInPercentAsString() + "\n");
					}
				}
				break;
			case DENSITY_DETERMINATION:
				String densityliquidtype = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_density_liquidtyp), "");
				String densitymode = prefs.getString(ApplicationController.getContext().getString(R.string.preferences_density_mode), "");


				if (densitymode.equals("1")||densitymode.equals("2")){
					posPrinter.printString("Solid");
				}else if (densitymode.equals("3")){
					posPrinter.printString("Liquid");
				}

					if (densityliquidtype.equals("1")) {
						ApplicationManager.getInstance().getCurrentLibrary().setDensityLiquidDensity(ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()));
					}

					posPrinter.printString("Denisty Determ: " + String.format("%.4f", ApplicationManager.getInstance().getDensity()) + " g/cm3" + "\n");


					posPrinter.printString("Brutto: " + ApplicationManager.getInstance().getSumAsStringWithUnit() + "\n");
					posPrinter.printString("Tara: " + ApplicationManager.getInstance().getTareAsStringWithUnit() + "\n");
					posPrinter.printString("Netto: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + "\n");

					posPrinter.printString("Weight in Air: " + String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_air()) + "\n");
					posPrinter.printString("Weight in Liquid: " + String.format("%.4f", ApplicationManager.getInstance().getDensity_weight_liquid()) + "\n");

					if (densityliquidtype.equals("1")) {
						posPrinter.printString("Water Temp: " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp()) + "\n");
					}

					posPrinter.printString("Liquid Density: " + String.format("%.4f", ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity()) + " g/cm3" + "\n");





				break;
			case PEAK_HOLD_STARTED:
			case PEAK_HOLD:
				posPrinter.printString("Peak Weight: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getPeakHoldMaximum())+"\n");
				posPrinter.printString("Brutto: "+ ApplicationManager.getInstance().getSumAsStringWithUnit()+"\n");
				posPrinter.printString("Tara: "+  ApplicationManager.getInstance().getTareAsStringWithUnit()+"\n");
				posPrinter.printString("Netto: "+ ApplicationManager.getInstance().getTaredValueAsStringWithUnit()+"\n");

				if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_peak_print_stableonly), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_peak_print_stableonly)) == true) {
					posPrinter.printString("Stable only : yes\n");
				}
				posPrinter.printString("Stable only : no\n");
				break;

			case INGREDIENT_COSTING:

				List<Item> costList=ApplicationManager.getInstance().getIngrediantCostList();



				for(int i=0; i<costList.size();i++){
					posPrinter.printString( costList.get(i).getName()+"\n");
					posPrinter.printString("Item weight: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(costList.get(i).getWeight())+"\n");
					posPrinter.printString("Item cost: "+ ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(costList.get(i).getCost())+"\n");
				}


				posPrinter.printString("------------------\n");
				posPrinter.printString("Total Items: " +costList.size()+"\n");
				posPrinter.printString("Total weight: " +ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getIngrediantTotalWeight())+"\n");
				posPrinter.printString("Total cost: " +ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getIngrediantTotalCost())+"\n");


				break;

			case PIPETTE_ADJUSTMENT_1_HOME:
				posPrinter.printString("Pipette Name:"+ApplicationManager.getInstance().getPipette_name());
				posPrinter.printString("Pipette Number:"+ApplicationManager.getInstance().getPipette_number());

				posPrinter.printString("Nominal Volume: "+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
				posPrinter.printString("Water Temp: "+ ApplicationManager.getInstance().getCurrentLibrary().getPipetteWaterTemp());
				posPrinter.printString("Pressure"+ApplicationManager.getInstance().getCurrentLibrary().getPipettePressure());


				posPrinter.printString("\n");
				posPrinter.printString("Inaccuracy: \n");

				double meanError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getMean()-ApplicationManager.getInstance().getCurrentLibrary().getPipetteNominal());
				posPrinter.printString("Mean Error:"+String.format("%.4f",meanError)+" ml\n");
				double meanErrorPercent=Math.abs(meanError/ApplicationManager.getInstance().getStats().getStatistic().getMean())*100;
				posPrinter.printString("Mean Error %:"+String.format("%.4f",meanErrorPercent)+" %\n");
				posPrinter.printString("Limit %:"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy())+" %\n");


				posPrinter.printString("\n");
				posPrinter.printString("Impreccision:\n");
				posPrinter.printString("Standard Deviation:"+String.format("%.4f",ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation())+" ml\n");
				double standardError=Math.abs(ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()/ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())*100;
				posPrinter.printString("Error CS%:"+String.format("%.4f",standardError)+" %\n");
				posPrinter.printString("Limit CV:"+String.format("%.4f",ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision())+" %\n");

				if (meanErrorPercent<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteInaccuracy() && standardError<=ApplicationManager.getInstance().getCurrentLibrary().getPipetteImprecision()){
					posPrinter.printString("\n");
					posPrinter.printString("Result: Pass");
					posPrinter.printString("\n");
				}else{
					posPrinter.printString("\n");
					posPrinter.printString("Result: Fail");
					posPrinter.printString("\n");
				}

				posPrinter.printString("\n");
				posPrinter.printString("Number of Samples"+ApplicationManager.getInstance().getCurrentLibrary().getPipetteNumberofSamples()+"\n");

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

				posPrinter.printString("> +2s:"+pstandard2+"\n");
				posPrinter.printString("> +2s:"+pstandard1+"\n");
				posPrinter.printString("*+1S > Mean > –1S:"+(ApplicationManager.getInstance().getStats().getSamples().size()-pstandard1-pstandard2-nstandard1-nstandard2)+"\n");
				posPrinter.printString("- +2s:"+nstandard1+"\n");
				posPrinter.printString("- +2s:"+nstandard2+"\n");

				posPrinter.printString("\n");


				posPrinter.printString("---Sample Data---\n");
				for(int i=0;i<ApplicationManager.getInstance().getStats().getSamples().size();i++){
					posPrinter.printString("Item "+String.format("%d",i)+" "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(i))+"\n");

				}

				break;




			default:
				Toast.makeText(ApplicationController.getContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
		}
	}

	public void printSQCBatch(SQC sqc){

		posPrinter.printString("Statistics of "+sqc.getName()+"\n");
		posPrinter.printString("Samples "+String.format("%d",sqc.getStatistics().getN())+"\n");
		posPrinter.printString("Average "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMean())+"\n");
		posPrinter.printString("Maximum "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax())+"\n");
		posPrinter.printString("Minimum "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMin())+"\n");
		posPrinter.printString("Range "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getMax()-sqc.getStatistics().getMin()) +"\n");
		posPrinter.printString("Standard Deviation "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getStandardDeviation())+"\n");
		posPrinter.printString("Total "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getStatistics().getSum())+"\n");
		posPrinter.printString("\n");

		posPrinter.printString("Nomininal "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(sqc.getNominal())+"\n");

		posPrinter.printString("+Tolerance1 "+String.format("%d",sqc.getSqcPT1())+ "   " +String.format("%.1f",((double)sqc.getSqcPT1()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		posPrinter.printString("+Tolerance2 "+String.format("%d",sqc.getSqcPT2())+ "   " +String.format("%.1f",((double)sqc.getSqcPT2()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		posPrinter.printString("-Tolerance1 "+String.format("%d",sqc.getSqcNT1())+ "   " +String.format("%.1f",((double)sqc.getSqcNT1()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");
		posPrinter.printString("-Tolerance2 "+String.format("%d",sqc.getSqcNT2())+ "   " +String.format("%.1f",((double)sqc.getSqcNT2()/(double)sqc.getStatistics().getN())*100 )+ "%"+"\n");


	}

	public void printStatistics(){


		switch (Scale.getInstance().getScaleApplication()){


			case PART_COUNTING:
				posPrinter.printString("Statistics" + "\n");
				posPrinter.printString("Samples " + String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()) + " \n");
				posPrinter.printString("Total " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getSum()) + " PCS\n");
				posPrinter.printString("Average " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMean()) + " PCS\n");
				posPrinter.printString("Standard Deviation " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getStandardDeviation()) + " PCS\n");
				posPrinter.printString("Minimum " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMin()) + " PCS\n");
				posPrinter.printString("Maximum " + String.format("%.0f", ApplicationManager.getInstance().getStats().getStatistic().getMax()) + " PCS\n");
				posPrinter.printString("Range " + String.format("%.0f", (ApplicationManager.getInstance().getStats().getStatistic().getMax() - ApplicationManager.getInstance().getStats().getStatistic().getMin())) + " PCS\n");

				break;

			default:
				posPrinter.printString("Statistics" + "\n");
				posPrinter.printString("Samples " + String.format("%d", ApplicationManager.getInstance().getStats().getStatistic().getN()) + " \n");
				posPrinter.printString("Total " + ApplicationManager.getInstance().getStatisticsSumAsStringWithUnit() + "\n");
				posPrinter.printString("Average " + ApplicationManager.getInstance().getStatisticsMeanAsStringWithUnit() + "\n");
				posPrinter.printString("Standard Deviation " +ApplicationManager.getInstance().getStatisticsStandardDeviationAsStringWithUnit() + "\n");
				posPrinter.printString("Minimum " + ApplicationManager.getInstance().getStatisticsMinAsStringWithUnit() + "\n");
				posPrinter.printString("Maximum " + ApplicationManager.getInstance().getStatisticsMaxAsStringWithUnit() + "\n");
				posPrinter.printString("Range " + ApplicationManager.getInstance().getStatisticsRangeAsStringWithUnit() + "\n");
				break;
		}
	}




}
			
			
			
		
		
