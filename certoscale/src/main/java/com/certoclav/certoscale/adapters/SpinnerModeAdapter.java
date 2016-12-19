package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;


import com.certoclav.certoscale.R;
import com.certoclav.certoscale.model.ScaleApplication;


import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class SpinnerModeAdapter extends ArrayAdapter<ScaleApplication> {


	private final Context mContext;




	public SpinnerModeAdapter(Context context, List<ScaleApplication> values) {
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
		ImageView imageApp = (ImageView) convertView.findViewById(R.id.login_dopdown_image_cloud);

		switch (getItem(position)){
			case WEIGHING:
				textMail.setText("Weighing");
				imageApp.setImageResource(R.drawable.application_icon_weighing);
				break;
			case PART_COUNTING:
				textMail.setText("Part Counting");
				imageApp.setImageResource(R.drawable.ic_launcher);
				break;
			case PERCENT_WEIGHING:
				textMail.setText("Percent Weiging");
				break;
			case CHECK_WEIGHING:
				textMail.setText("Check Weiging");
				break;
			case ANIMAL_WEIGHING:
				textMail.setText("Animal Weiging");
				break;
			case FILLING:
				textMail.setText("Filling");
				break;
			case TOTALIZATION:
				textMail.setText("Totalization");
				break;
			case FORMULATION:
				textMail.setText("Formulation");
				break;
			case DIFFERENTIAL_WEIGHING:
				textMail.setText("Differential Weighing");
				break;
			case DENSITIY_DETERMINATION:
				textMail.setText("Density Determination");
				break;
			case PEAK_HOLD:
				textMail.setText("Peak Hold");
				break;
			case INGREDIENT_COSTING:
				textMail.setText("Ingrediant Costing");
				break;
			case PIPETTE_ADJUSTMENT:
				textMail.setText("Pipette Adjustment");
				break;
			case STATISTICAL_QUALITY_CONTROL:
				textMail.setText("Statistical Quality Control");
				break;

			default:
				textMail.setText("Not weighing");
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