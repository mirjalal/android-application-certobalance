package com.certoclav.certoscale.settings.unit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.ItemAdapter;
import com.certoclav.certoscale.adapters.UnitAdapter;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.listener.ButtonEventListener;
import com.certoclav.certoscale.model.Navigationbar;


public class SettingsUnitActivity extends FragmentActivity{ //implements UnitAdapter.OnClickButtonListener, ButtonEventListener{



	private Navigationbar navigationbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_unit_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setText("Weighing Units".toUpperCase());
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);
		navigationbar.getButtonAdd().setVisibility(View.VISIBLE);


	}
	
	


	@Override
	protected void onPause() {
		super.onPause();
	}

/*
	@Override
	public void onClickButtonDelete( final Unit unit) {
		try {

			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_yes_no);
			dialog.setTitle("Confirm deletion");

			// set the custom dialog components - text, image and button
			TextView text = (TextView) dialog.findViewById(R.id.text);
			text.setText(getString(R.string.do_you_really_want_to_delete) + " " + unit.getName());
			Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
			dialogButtonNo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DatabaseService db = new DatabaseService(SettingsUnitActivity.this);
					db.deleteUnit(unit);
					dialog.dismiss();
				}
			});

			dialog.show();


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}




	}


	@Override
	public void onClickButtonEdit(Unit unit) {


	}

	@Override
	public void onClickNavigationbarButton(int buttonId, boolean isLongClick) {}

*/

	@Override
	protected void onResume() {
		getSupportFragmentManager().beginTransaction().replace(R.id.settings_unit_container_fragment, new SettingsUnitFragment()).commit();


		super.onResume();

	}



}
