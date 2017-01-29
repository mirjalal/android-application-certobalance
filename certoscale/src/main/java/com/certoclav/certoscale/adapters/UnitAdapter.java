package com.certoclav.certoscale.adapters;

import android.content.Context;
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

		CheckBox cbEnabled = (CheckBox) convertView.findViewById(R.id.list_item_unit_checkbox);


		if(getItem(position).getCustom() == false){
			actionUnitDelete.setVisibility(View.INVISIBLE);
			actionUnitEdit.setVisibility(View.INVISIBLE);
		}else{
			actionUnitDelete.setVisibility(View.VISIBLE);
			actionUnitEdit.setVisibility(View.VISIBLE);
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

			}
		});



		actionUnitDelete.setChecked(false);
		actionUnitDelete.setImageResource(R.drawable.ic_menu_bin);

			//actionUnitDelete.setText(getContext().getString(R.string.delete));
			actionUnitDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});


		
		return convertView;
	}
}