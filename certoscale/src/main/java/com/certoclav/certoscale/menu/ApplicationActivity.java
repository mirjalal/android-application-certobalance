package com.certoclav.certoscale.menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.ActionButtonbar;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.SettingsActivity;
import com.certoclav.certoscale.util.LabelPrinterUtils;


public class ApplicationActivity extends FragmentActivity implements  ButtonEventListener {

private Navigationbar navigationbar = new Navigationbar(this);
private ActionButtonbar actionButtonbar = new ActionButtonbar(this);



	@Override
protected void onResume() {


		navigationbar.onCreate();
		navigationbar.setButtonEventListener(this);
		actionButtonbar.onCreate();
		actionButtonbar.setButtonEventListener(this);




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
			//Tara the measured scale value
			Scale.getInstance().setWeightTara(Scale.getInstance().getWeightRaw());
		}
		
		if(buttonId == ActionButtonbar.BUTTON_CAL){
			//send command for calibration to the scale
			if(Scale.getInstance().getWeightRaw() <= 5){
			
				Scale.getInstance().getReadAndParseSerialService().sendCalibrationCommand();
				
				Intent intent = new Intent(ApplicationActivity.this,AnimationCalibrationActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(ApplicationActivity.this, "Please remove item from pan first", Toast.LENGTH_LONG).show();
			}
		}
		
		if(buttonId == ActionButtonbar.BUTTON_PRINT){
			Toast.makeText(ApplicationActivity.this, "Printed: "+ String.format("%.4f",Scale.getInstance().getWeightRaw()) + " g", Toast.LENGTH_LONG).show();
			LabelPrinterUtils.printText(""+ String.format("%.4f",Scale.getInstance().getWeightRaw()) + " g",1);
		}
		
		if(buttonId == Navigationbar.BUTTON_SETTINGS){
			Intent intent = new Intent(ApplicationActivity.this, SettingsActivity.class);
			intent.putExtra(SettingsActivity.INTENT_EXTRA_SUBMENU, navigationbar.getSpinnerMode().getSelectedItemPosition());
			startActivity(intent);
		}
	}
}
