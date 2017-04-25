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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Library;
import com.certoclav.certoscale.view.QuickActionItem;
import com.certoclav.library.application.ApplicationController;

import java.util.ArrayList;
import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class LibraryAdapter extends ArrayAdapter<Library> {

	public interface OnClickButtonListener {
		 void onClickButtonDelete(Library library);
		 void onClickButtonEdit(Library library);
		}
	ArrayList<OnClickButtonListener> onClickButtonListeners = new ArrayList<OnClickButtonListener>();

	public void setOnClickButtonListener(OnClickButtonListener listener){
		onClickButtonListeners.add(listener);
	}
	public void removeOnClickButtonListener(OnClickButtonListener listener){
		onClickButtonListeners.remove(listener);
	}

	private final Context mContext;
	private QuickActionItem actionItemDelete;
	private QuickActionItem actionItemEdit;



	public LibraryAdapter(Context context, List<Library> values) {
		super(context, R.layout.list_element_library, values);
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
		

			convertView = inflater.inflate(R.layout.list_element_library, parent, false);
			LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);
			
			actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			containerItems.addView(actionItemDelete);
			
			actionItemEdit = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			//containerItems.addView(actionItemEdit);


			
		TextView firstLine = (TextView) convertView.findViewById(R.id.first_line);
		firstLine.setText(getItem(position).getName());

		StringBuilder sb = new StringBuilder();
		sb.append("Created on:").append("\t\t").append("\n");
		sb.append("Created by:").append("\t\t").append("\n");

		final TextView secondLine = (TextView) convertView.findViewById(R.id.second_line);
		secondLine.setText(sb.toString());

		sb = new StringBuilder();
		sb.append(getItem(position).getDate()).append("\n");
		sb.append(getItem(position).getUserEmail()).append("\n");

		final TextView thirdLine = (TextView) convertView.findViewById(R.id.third_line);
		thirdLine.setText(sb.toString());



		actionItemDelete.setChecked(false);
		actionItemDelete.setImageResource(R.drawable.ic_menu_bin);

			//actionItemDelete.setText(getContext().getString(R.string.delete));
			actionItemDelete.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					for(OnClickButtonListener listener : onClickButtonListeners){
						listener.onClickButtonDelete(getItem(position));
					}



				}
			});

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
			if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_library), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_library))==true) {
				actionItemDelete.setEnabled(false);

			}else {
				actionItemDelete.setEnabled(true);
;
			}

		
			actionItemEdit.setChecked(false);
			actionItemEdit.setImageResource(R.drawable.ic_menu_edit);

				//actionItemEdit.setText(getContext().getString(R.string.edit));
				actionItemEdit.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						for(OnClickButtonListener listener : onClickButtonListeners){
							listener.onClickButtonEdit(getItem(position));
						}



					}
				});
		
		return convertView;
	}
}