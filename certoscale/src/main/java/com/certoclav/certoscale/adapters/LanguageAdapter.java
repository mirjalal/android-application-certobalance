package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.certoclav.certoscale.R;

import java.util.List;
import java.util.Locale;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class LanguageAdapter extends ArrayAdapter<Locale> {
	private final Context mContext;


	public LanguageAdapter(Context context, List<Locale> values) {
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
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.settings_language_row, parent, false);
		}
		TextView language = (TextView) convertView.findViewById(R.id.language_first_line);
		
	
		language.setText(getItem(position).getDisplayLanguage(getItem(position)));
		TextView country = (TextView) convertView.findViewById(R.id.language_second_line);
        country.setText(getItem(position).getDisplayCountry(getItem(position)));
        
	
		
		return convertView;
	}
	


    
}