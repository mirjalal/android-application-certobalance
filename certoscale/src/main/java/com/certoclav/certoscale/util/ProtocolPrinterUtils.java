package com.certoclav.certoscale.util;

import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.util.Calendar;


public class ProtocolPrinterUtils {



public ProtocolPrinterUtils() {

	}

	public static void printProtocol(){
		printHeader();
		printDate();
		printBalanceId();
		printBalanceName();
		printUserName();
		printProjectName();
		printApplicationName();
		printResults();
		printSignature();
	}

public static void printHeader(){
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 1\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 2\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 3\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 4\n");
	Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Header line 5\n");
}

	public static void printDate(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(Calendar.getInstance().getTime().toString() + "\n");
	}

	public static void printBalanceId(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage(Scale.getInstance().getSerialnumber() + "\n");
	}

	public static void printBalanceName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Balance Name:" + " "+"CertoBalance" + "\n");
	}

	public static void printUserName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("User Name:" + " "+ Scale.getInstance().getUser().getFirstName() + " " +Scale.getInstance().getUser().getLastName() + "\n");
	}

	public static void printProjectName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Project Name:"+" ");
	}

	public static void printApplicationName(){
		Scale.getInstance().getSerialsServiceProtocolPrinter().sendMessage("Application:"+" "+Scale.getInstance().getScaleApplication().toString().replace("_", " "));
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
			
			
			
		
		
