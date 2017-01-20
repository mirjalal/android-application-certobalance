package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.User;

import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class UserDropdownAdapter extends ArrayAdapter<User> {
	

	private final Context mContext;




	public UserDropdownAdapter(Context context, List<User> values) {
		super(context, R.layout.login_spinner_item, R.id.login_dopdown_text_mail, values);
		//super(context, R.layout.spinner_dropdown_item_large, values);
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
			convertView = inflater.inflate(R.layout.login_spinner_item, parent, false);
		}

		CheckedTextView textMail = (CheckedTextView) convertView.findViewById(R.id.login_dopdown_text_mail);
		textMail.setText(getItem(position).getEmail());
		
		
		ImageView imageCloud = (ImageView) convertView.findViewById(R.id.login_dopdown_image_cloud);
		if(getItem(position).getIsLocal() == true){
			imageCloud.setImageResource(R.drawable.icon_cloud_point);
		}else{
			imageCloud.setImageResource(R.drawable.icon_cloud_ok);
		}
		
		return convertView;
	}
	
	
    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        return(getView(position, convertView,parent));

       // return label;
    }


    
}