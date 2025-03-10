package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Unit;
import com.certoclav.certoscale.view.QuickActionItem;
import com.certoclav.library.application.ApplicationController;

import java.util.ArrayList;
import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data units. <br>
 * ProfileAdapter is also responsible for making a view for each unit in the
 * data set.
 * 
*/
public class UnitAdapter extends ArrayAdapter<Unit> {





	private final Context mContext;
	private QuickActionItem actionUnitDelete;
	private QuickActionItem actionUnitEdit;
	private boolean hideCheckBox = false;

	public interface OnClickButtonListener {
		void onClickButtonDelete(Unit unit);
		void onClickButtonEdit(Unit unit);
	}
	ArrayList<UnitAdapter.OnClickButtonListener> onClickButtonListeners = new ArrayList<UnitAdapter.OnClickButtonListener>();

	public void setOnClickButtonListener(UnitAdapter.OnClickButtonListener listener){
		onClickButtonListeners.add(listener);
	}
	public void removeOnClickButtonListener(ItemAdapter.OnClickButtonListener listener){
		onClickButtonListeners.remove(listener);
	}



	public UnitAdapter(Context context, List<Unit> values) {
		super(context, R.layout.list_element_unit, values);
		this.mContext = context;


	}
	

	/**
	 * Gets a View that displays the data at the specified position in the data
	 * set.The View is inflated it from profile_list_row XML layout file
	 * 
	 * @see Adapter#getView(int, View, ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

		convertView = inflater.inflate(R.layout.list_element_unit, parent, false);
		LinearLayout containerUnits =  (LinearLayout) convertView.findViewById(R.id.list_item_unit_container_button);
			
		actionUnitDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerUnits, false);
		containerUnits.addView(actionUnitDelete);

		actionUnitEdit = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerUnits, false);
		containerUnits.addView(actionUnitEdit);
		
		TextView editTextUnit = (TextView) convertView.findViewById(R.id.list_item_unit_text_unit);
		editTextUnit.setText(getItem(position).getName());

		TextView editName = (TextView) convertView.findViewById(R.id.list_item_unit_text_description);
		editName.setText(getItem(position).getDescription());

		TextView textDescription = (TextView) convertView.findViewById(R.id.list_item_unit_text_calc);
		textDescription.setText("1 g = "+ getItem(position).getFactor()+" * 10^(" + getItem(position).getExponent()+ ") " + getItem(position).getName());

		CheckBox cbEnabled = (CheckBox) convertView.findViewById(R.id.list_item_unit_checkbox);

		if(getItem(position).getCustom() == false){
			actionUnitDelete.setVisibility(View.INVISIBLE);
			actionUnitEdit.setVisibility(View.INVISIBLE);
		}else{
			actionUnitDelete.setVisibility(View.VISIBLE);
			actionUnitEdit.setVisibility(View.VISIBLE);
		}


		if(hideCheckBox){
			cbEnabled.setVisibility(View.GONE);
			actionUnitDelete.setVisibility(View.GONE);
			actionUnitEdit.setVisibility(View.GONE);
		}else{
			cbEnabled.setVisibility(View.VISIBLE);
		}



			cbEnabled.setChecked(getItem(position).getEnabled());


		cbEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DatabaseService db = new DatabaseService(mContext);
				getItem(position).setEnabled(isChecked);
				db.deleteUnit(getItem(position));
				db.insertUnit(getItem(position));
			}
		});

		actionUnitEdit.setChecked(false);
		actionUnitEdit.setImageResource(R.drawable.ic_menu_edit);


		//actionUnitDelete.setText(getContext().getString(R.string.delete));
		actionUnitEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				for(OnClickButtonListener listener : onClickButtonListeners){
					listener.onClickButtonEdit(getItem(position));
				}

				*/

				for(UnitAdapter.OnClickButtonListener listener : onClickButtonListeners){
					listener.onClickButtonEdit(getItem(position));
				}
			}
		});



		actionUnitDelete.setChecked(false);
		actionUnitDelete.setImageResource(R.drawable.ic_menu_bin);

			//actionUnitDelete.setText(getContext().getString(R.string.delete));
			actionUnitDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					for(UnitAdapter.OnClickButtonListener listener : onClickButtonListeners){
						listener.onClickButtonDelete(getItem(position));
					}
				}
			});


		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_weighing_units), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_weighing_units))==true) {

			actionUnitDelete.setEnabled(false);
			actionUnitEdit.setEnabled(false);
			cbEnabled.setEnabled(false);

		}else {
			actionUnitDelete.setEnabled(true);
			actionUnitEdit.setEnabled(true);
			cbEnabled.setEnabled(true);
		}

		
		return convertView;
	}

	public void setHideCheckbox(boolean b) {
		hideCheckBox = b;
	}
}