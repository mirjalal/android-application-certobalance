package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.model.MenuElement;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.settings.device.SettingsDeviceActivity;
import com.certoclav.certoscale.settings.glp.SettingsGlpActivity;
import com.certoclav.certoscale.settings.library.MenuLibraryActivity;
import com.certoclav.certoscale.settings.unit.SettingsUnitActivity;
import com.certoclav.certoscale.settings.user.MenuUserActivity;

import java.util.List;




/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class MenuElementAdapter extends ArrayAdapter<MenuElement> {
	private final Context mContext;



	public MenuElementAdapter(Context context, List<MenuElement> values) {
 
		super(context, R.layout.menu_main_element, values);
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
	
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.menu_main_element, parent, false);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.menu_main_element_text);
		textView.setText(getItem(position).getMenuText());
		ImageView imageView = (ImageView) convertView.findViewById(R.id.menu_main_element_image);
		imageView.setImageResource(getItem(position).getImageResId());

		switch (getItem(position).getId()){
			case MENU_ITEM_APPLICATIONS:
				convertView.setBackgroundResource(R.drawable.menu_btn_red);
				break;
			case MENU_ITEM_CALIBRATION:
				convertView.setBackgroundResource(R.drawable.quickaction_slider_btn);
				break;
			case MENU_ITEM_DEVICE:
				convertView.setBackgroundResource(R.drawable.menu_btn_blue);
				break;
			default:
				convertView.setBackgroundResource(R.drawable.menu_btn_purple);
				break;
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = null;
				switch (getItem(position).getId()){
					case MENU_ITEM_APPLICATIONS:
						intent = new Intent(mContext, ApplicationActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_APPLICATION_SETTINGS:
						intent = new Intent(mContext, SettingsActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_USER:
						intent = new Intent(mContext, MenuUserActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_DEVICE:
						intent = new Intent(mContext, SettingsDeviceActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_LIBRARY:
						intent = new Intent(mContext, MenuLibraryActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_WEIGHING_UNITS:
						intent = new Intent(mContext, SettingsUnitActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_CALIBRATION:
						Toast.makeText(mContext,"TODO: Dialog with user choise: AutoCal, InteernalCal, ManualCal",Toast.LENGTH_LONG).show();
						break;
					case MENU_ITEM_GLP:
						intent = new Intent(mContext, SettingsGlpActivity.class);
						mContext.startActivity(intent);
						break;
				}
			}
		});
		//start animationThread

		return convertView;
	}







}