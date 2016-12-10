package com.certoclav.certoscale.menu;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.ScaleApplicationListener;
import com.certoclav.certoscale.model.ActionButtonbar;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.settings.SettingsActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.util.LabelPrinterUtils;


public class ApplicationActivity extends FragmentActivity implements  ButtonEventListener ,ScaleApplicationListener{

private Navigationbar navigationbar = new Navigationbar(this);
private ActionButtonbar actionButtonbar = new ActionButtonbar(this);
	private boolean appSettingsVisible = false;



	@Override
protected void onResume() {



		navigationbar.setButtonEventListener(this);
		actionButtonbar.setButtonEventListener(this);
		navigationbar.getSpinnerLib().setVisibility(View.VISIBLE);
		navigationbar.getSpinnerMode().setVisibility(View.VISIBLE);

		super.onResume();
}



@Override
protected void onPause() {
	navigationbar.removeNavigationbarListener(this);
	actionButtonbar.removeButtonEventListener(this);
	super.onPause();
}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.menu_application_activity);
		super.onCreate(savedInstanceState);
		navigationbar.onCreate();
		actionButtonbar.onCreate();

		//start parse serial data output every second. TODO: This function call should be moved into a State Machine class
	    Scale.getInstance().getReadAndParseSerialService().startParseSerialThread();
		getSupportFragmentManager().beginTransaction().add(R.id.menu_application_container_display, new ApplicationFragmentWeight()).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.menu_application_container_table,  new ApplicationFragmentTable()).commit();

	}



	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		Log.e("ApplicationActivity", "onclickhome");
		if(buttonId == Navigationbar.BUTTON_HOME){
			Intent intent = new Intent(ApplicationActivity.this,HomeActivity.class);
			startActivity(intent);
		}
	
		if(buttonId == ActionButtonbar.BUTTON_TARA){
			ApplicationManager.getInstance().setTareInGram(Scale.getInstance().getWeightInGram());
		}
		if(buttonId == ActionButtonbar.BUTTON_STATISTICS){
			ApplicationManager.getInstance().showStatisticsNotification(ApplicationActivity.this, new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
				}
			});

		}
		if(buttonId == ActionButtonbar.BUTTON_ACCUMULATE){
			ApplicationManager.getInstance().accumulateStatistics();
			actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
		}

		if(buttonId == ActionButtonbar.BUTTON_APP_SETTINGS){
			if(appSettingsVisible == true) {
				getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentTable()).commit();
				actionButtonbar.getButtonAppSettings().setText("SETTINGS");
				actionButtonbar.getButtonCal().setEnabled(true);
				actionButtonbar.getButtonPrint().setEnabled(true);
				actionButtonbar.getButtonTara().setEnabled(true);
				actionButtonbar.getButtonStatistics().setEnabled(true);
				actionButtonbar.getButtonAccumulate().setEnabled(true);
				appSettingsVisible = false;
			}else{
				actionButtonbar.getButtonCal().setEnabled(false);
				actionButtonbar.getButtonPrint().setEnabled(false);
				actionButtonbar.getButtonStatistics().setEnabled(false);
				actionButtonbar.getButtonAccumulate().setEnabled(false);
				//actionButtonbar.getButtonTara().setEnabled(false);
				switch (Scale.getInstance().getScaleApplication()){
					case PART_COUNTING:
						getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsPartCounting()).commit();
						actionButtonbar.getButtonAppSettings().setText("RESULTS");
						appSettingsVisible = true;
						break;
					case WEIGHING:
						getSupportFragmentManager().beginTransaction().replace(R.id.menu_application_container_table, new ApplicationFragmentSettingsWeighing()).commit();
						actionButtonbar.getButtonAppSettings().setText("RESULTS");
						appSettingsVisible = true;
						break;
					default:
						Toast.makeText(this,"TODO: Implement Actions",Toast.LENGTH_SHORT).show();
				}



			}
		}


		if(buttonId == ActionButtonbar.BUTTON_CAL){
			//send command for calibration to the scale
			if(Scale.getInstance().getWeightInGram() <= 5){
			
				Scale.getInstance().getReadAndParseSerialService().sendCalibrationCommand();
				
				Intent intent = new Intent(ApplicationActivity.this,AnimationCalibrationActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(ApplicationActivity.this, "Please remove item from pan first", Toast.LENGTH_LONG).show();
			}
		}
		
		if(buttonId == ActionButtonbar.BUTTON_PRINT){
			Toast.makeText(ApplicationActivity.this, "Printed: "+ String.format("%.4f",Scale.getInstance().getWeightInGram()) + " g", Toast.LENGTH_LONG).show();
			LabelPrinterUtils.printText(""+ String.format("%.4f",Scale.getInstance().getWeightInGram()) + " g",1);
		}
		
		if(buttonId == Navigationbar.BUTTON_SETTINGS){
			Intent intent = new Intent(ApplicationActivity.this, SettingsActivity.class);
			intent.putExtra(SettingsActivity.INTENT_EXTRA_SUBMENU, navigationbar.getSpinnerMode().getSelectedItemPosition());
			startActivity(intent);
		}
	}

	@Override
	public void onApplicationChange(ScaleApplication application) {
		ApplicationManager.getInstance().clearStatistics();
		actionButtonbar.getButtonStatistics().setText("STATISTICS\n(" + ApplicationManager.getInstance().getStatisticsArray().size() + ")");
	}
}
