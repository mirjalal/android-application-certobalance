package com.certoclav.certoscale;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.listener.SensorDataListener;
import com.certoclav.certoscale.model.ActionButtonbar;
import com.certoclav.certoscale.model.Navigationbar;
import com.certoclav.certoscale.model.ReferenceField;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.SettingsActivity;
import com.certoclav.certoscale.util.LabelPrinterUtils;

import java.util.ArrayList;
import java.util.List;


public class MenuMainActivity extends Activity implements SensorDataListener, ButtonEventListener {
private TextView textValue = null;
private Navigationbar navigationbar = new Navigationbar(this);
private ActionButtonbar actionButtonbar = new ActionButtonbar(this);
private FrameLayout barload = null;
private float tara = 0;
private TextView textTara = null;
private TextView textInstruction = null;
private TextView textSum = null;

private List<ReferenceField> listReferenceFields = new ArrayList<ReferenceField>();


	@Override
protected void onResume() {
	Scale.getInstance().setOnSensorDataListener(this);
	navigationbar.onCreate();
	navigationbar.setButtonEventListener(this);
	actionButtonbar.onCreate();
	actionButtonbar.setButtonEventListener(this);
	
	super.onResume();
}



@Override
protected void onPause() {
	Scale.getInstance().removeOnSensorDataListener(this);
	navigationbar.removeNavigationbarListener(this);
	actionButtonbar.removeButtonEventListener(this);
	super.onPause();
}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_menu_main);
		super.onCreate(savedInstanceState);
		
		//8 Textfields for showing data of interest
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_1),(TextView) findViewById(R.id.reference_val_1)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_2),(TextView) findViewById(R.id.reference_val_2)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_3),(TextView) findViewById(R.id.reference_val_3)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_4),(TextView) findViewById(R.id.reference_val_4)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_5),(TextView) findViewById(R.id.reference_val_5)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_6),(TextView) findViewById(R.id.reference_val_6)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_7),(TextView) findViewById(R.id.reference_val_7)));
		listReferenceFields.add(new ReferenceField((TextView)findViewById(R.id.reference_text_8),(TextView) findViewById(R.id.reference_val_8)));
		for(ReferenceField refField : listReferenceFields){
			refField.getTextName().setText("");
			refField.getTextValue().setText("");
		}
		
		//Access to views from menu_main xml file
		barload = (FrameLayout) findViewById(R.id.menu_main_bar_load);
	    textInstruction = (TextView) findViewById(R.id.menu_main_text_instruction);
	    textSum = (TextView) findViewById(R.id.menu_main_text_information);
		textValue = (TextView) findViewById(R.id.menu_main_text_value);
	    
		//start parse serial data output every second. TODO: This function call should be moved into a State Machine class
	    Scale.getInstance().getReadAndParseSerialService().startParseSerialThread();

	}



	@Override
	public void onSensorDataChange(Float value, String unit) {
		textValue.setText(String.format("%.4f", value - Scale.getInstance().getTara()) + " " + unit);
		LayoutParams params = (LayoutParams) barload.getLayoutParams();
		int width = (int) (value*7.0);
		if(width<0){
			width = 0;
		}
		if(width > 700){
			width = 700;
		}
		params.width = width;
		barload.setLayoutParams(params);
		if(value > 100){
			//todo color bar red
		}
		
		//TODO: define fixed indexes instead of hardcoding index positions.
		//TODO: show only data which is enabled in settings menu
		
		listReferenceFields.get(0).getTextName().setText("TARA");
		listReferenceFields.get(0).getTextValue().setText((String.format("%.4f",Scale.getInstance().getTara()) + " g"));
		
		listReferenceFields.get(1).getTextName().setText("SUM");
		listReferenceFields.get(1).getTextValue().setText(String.format("%.4f",Scale.getInstance().getScaleValue()) + " g");
		
		listReferenceFields.get(2).getTextName().setText("LOAD");
		listReferenceFields.get(2).getTextValue().setText(String.format("%d", Math.round(Scale.getInstance().getScaleValue())) + " %");
			
		if(Scale.getInstance().getScaleValue() - Scale.getInstance().getTara() == 0){
			textInstruction.setText("Please place item");
		}else{
			textInstruction.setText("");
		}
		textSum.setText("SUM: " + String.format("%.4f",Scale.getInstance().getScaleValue()) + " g");
	}



	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {
		Log.e("MenuMainActivity", "onclickhome");
		if(buttonId == Navigationbar.BUTTON_HOME){
			//do nothing
		}
	
		if(buttonId == ActionButtonbar.BUTTON_TARA){
			//Tara the measured scale value
			Scale.getInstance().setTara(Scale.getInstance().getScaleValue());
		}
		
		if(buttonId == ActionButtonbar.BUTTON_CAL){
			//send command for calibration to the scale
			if(Scale.getInstance().getScaleValue() <= 5){
			
				Scale.getInstance().getReadAndParseSerialService().sendCalibrationCommand();
				
				Intent intent = new Intent(MenuMainActivity.this,AnimationCalibrationActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(MenuMainActivity.this, "Please remove item from pan first", Toast.LENGTH_LONG).show();
			}
		}
		
		if(buttonId == ActionButtonbar.BUTTON_PRINT){
			Toast.makeText(MenuMainActivity.this, "Printed: "+ String.format("%.4f",Scale.getInstance().getScaleValue()) + " g", Toast.LENGTH_LONG).show();
			LabelPrinterUtils.printText(""+ String.format("%.4f",Scale.getInstance().getScaleValue()) + " g",1);
		}
		
		if(buttonId == Navigationbar.BUTTON_SETTINGS){
			Intent intent = new Intent(MenuMainActivity.this, SettingsActivity.class);
			intent.putExtra(SettingsActivity.INTENT_EXTRA_SUBMENU, navigationbar.getSpinnerMode().getSelectedItemPosition());
			startActivity(intent);
		}
	}
}
