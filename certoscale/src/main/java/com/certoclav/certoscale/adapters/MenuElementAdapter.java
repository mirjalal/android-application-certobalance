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

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.menu.VideoActivity;
import com.certoclav.certoscale.model.MenuElement;
import com.certoclav.certoscale.settings.application.SettingsActivity;
import com.certoclav.certoscale.settings.calibration.SettingsCalibrationActivity;
import com.certoclav.certoscale.settings.communication.SettingsCommunicationActivity;
import com.certoclav.certoscale.settings.device.SettingsDeviceActivity;
import com.certoclav.certoscale.settings.glp.SettingsGlpActivity;
import com.certoclav.certoscale.settings.item.MenuItemActivity;
import com.certoclav.certoscale.settings.labels.MenuLabelPrinterActivity;
import com.certoclav.certoscale.settings.library.MenuLibraryActivity;
import com.certoclav.certoscale.settings.lockout.SettingsLockoutActivity;
import com.certoclav.certoscale.settings.protocol.MenuProtocolActivity;
import com.certoclav.certoscale.settings.recipe.MenuRecipeActivity;
import com.certoclav.certoscale.settings.reset.SettingsFactoryResetActivity;
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
		ImageView ImageView = (ImageView) convertView.findViewById(R.id.menu_main_element_image);
		ImageView.setImageResource(getItem(position).getImageResId());



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
					case MENU_ITEM_LOCKOUT:
						intent = new Intent(mContext, SettingsLockoutActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_RESET:
						intent = new Intent(mContext, SettingsFactoryResetActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_LIBRARY:
						intent = new Intent(mContext, MenuLibraryActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_RECIPES:
						intent = new Intent(mContext, MenuRecipeActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_ITEMS:
						intent = new Intent(mContext, MenuItemActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_WEIGHING_UNITS:
						intent = new Intent(mContext, SettingsUnitActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_CALIBRATION:
						intent = new Intent(mContext, SettingsCalibrationActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_GLP:
						intent = new Intent(mContext, SettingsGlpActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_COMMUNICATION:
						intent = new Intent(mContext, SettingsCommunicationActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_VIDEO:
						intent = new Intent(mContext, VideoActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_PROTOCOLS:
						intent = new Intent(mContext,MenuProtocolActivity.class);
						mContext.startActivity(intent);
						break;
					case MENU_ITEM_LABELS:
						intent = new Intent(mContext, MenuLabelPrinterActivity.class);
						mContext.startActivity(intent);
						break;

				}
			}
		});
		//start animationThread

		return convertView;
	}







}