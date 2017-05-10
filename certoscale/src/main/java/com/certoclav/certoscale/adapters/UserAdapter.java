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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.User;
import com.certoclav.certoscale.model.Scale;
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
public class UserAdapter extends ArrayAdapter<User> {
	
	public interface OnClickButtonListener {
		 void onClickButtonDelete(User user);
		 void onClickButtonEdit(User user);
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


	/**
	 * Constructor
	 * 
	 * @param context
	 *            context of calling activity
	 * @param values
	 * {@link List}<{@link Profile}> containing the data to populate the list
	 */
	public UserAdapter(Context context, List<User> values) {
		super(context, R.layout.list_element_user, values);
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
		

			convertView = inflater.inflate(R.layout.list_element_user, parent, false);
			LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);
			
			actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			containerItems.addView(actionItemDelete);
			
			actionItemEdit = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			containerItems.addView(actionItemEdit);


			
		TextView firstLine = (TextView) convertView.findViewById(R.id.first_line);
		firstLine.setText(getItem(position).getEmail());

		final TextView secondLine = (TextView) convertView.findViewById(R.id.second_line);
		secondLine.setText(getItem(position).getIsAdmin()?  "Admin account":"Standard account");

		ImageView imageCloud = (ImageView) convertView.findViewById(R.id.list_element_user_image_cloud);
		if(getItem(position).getIsLocal() == false) {
			imageCloud.setImageResource(R.drawable.cloud_ok_white);
		}else{
			imageCloud.setImageResource(R.drawable.cloud_no_white);
		}


		//make only the own account editable
		if(getItem(position).getUserId() != Scale.getInstance().getUser().getUserId()){
			actionItemDelete.setVisibility(View.INVISIBLE);
			actionItemEdit.setVisibility(View.INVISIBLE);
		}else{
			actionItemDelete.setVisibility(View.VISIBLE);
			actionItemEdit.setVisibility(View.VISIBLE);
		}



		//if admin is logged in, then make all accounts editable
		if(Scale.getInstance().getUser().getIsAdmin()){
			actionItemEdit.setVisibility(View.VISIBLE);
			actionItemDelete.setVisibility(View.VISIBLE);
		}

		//admin account is never deleteable
		if(getItem(position).getIsAdmin()){
			actionItemDelete.setVisibility(View.INVISIBLE);
		}




		actionItemDelete.setChecked(false);

		actionItemDelete.setImageResource(R.drawable.ic_menu_bin);

			//actionItemDelete.setText(getContext().getString(R.string.delete));
			actionItemDelete.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {



						for (OnClickButtonListener listener : onClickButtonListeners) {
							listener.onClickButtonDelete(getItem(position));
						}



				}
			});


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

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		if (prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_user_settings), ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_user_settings))==true) {

			actionItemDelete.setEnabled(false);
			actionItemEdit.setEnabled(false);
		}else {
			actionItemDelete.setEnabled(true);
			actionItemEdit.setEnabled(true);
		}
		
		return convertView;
	}
}