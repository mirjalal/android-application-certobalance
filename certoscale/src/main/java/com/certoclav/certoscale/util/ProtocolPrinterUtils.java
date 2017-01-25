package com.certoclav.certoscale.util;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.view.*;

import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.Calendar;

import com.certoclav.certoscale.R;


public class ProtocolPrinterUtils {/*extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{//extends ApplicationActivity {


	private static final String key_Header = "preferences_glp_header";
	private static final String key_Name ="preferences_glp_balance_name";
	private static final String key_project_name="preferences_glp_project_name";

	private static EditTextPreference header = null ;
	private static EditTextPreference balance_Name= null;
	private static EditTextPreference project_Name= null;


	private static EditTextPreference tt=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		addPreferencesFromResource(R.xml.preferences_glp);
		header = (EditTextPreference) findPreference(key_Header);
		balance_Name = (EditTextPreference) findPreference(key_Name);
		project_Name = (EditTextPreference) findPreference(key_project_name);
	}

	@Override
	public void onResume(){
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		updatePreference();

	}

	@Override
	public void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
										  String key) {
		updatePreference();
	}


	private void updatePreference(){

		header = (EditTextPreference) findPreference(key_Header);
		balance_Name = (EditTextPreference) findPreference(key_Name);
		project_Name = (EditTextPreference) findPreference(key_project_name);

	}





*/


public ProtocolPrinterUtils() {

	}


	public static void printProtocol(){
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

public static void  printHeader(  String header){

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

	public static void printDate(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(Calendar.getInstance().getTime().toString() + "\n");
	}

	public static void printBalanceId(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Serialnumber: "+Scale.getInstance().getSerialnumber() + "\n");
	}

	public static void printBalanceName(String balanceName){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Balance Name:" + " "+balanceName + "\n");
	}

	public static void printUserName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("User Name:" + " "+ Scale.getInstance().getUser().getFirstName() + " " +Scale.getInstance().getUser().getLastName() + "\n");
	}

	public static void printProjectName(String projectName){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Project Name:"+projectName+ "\n");
	}

	public static void printApplicationName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Application:"+" "+Scale.getInstance().getScaleApplication().toString().replace("_", " ")+"\n");
	}

	public static void printResults(){
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

	public static void printSignature(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Signature:_______________" + "\n");
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Verified by:_______________" + "\n");
	}

}
			
			
			
		
		
