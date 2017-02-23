package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
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
		super(context, R.layout.navigationbar_mode_spinner_item, values);

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
			convertView = inflater.inflate(R.layout.navigationbar_mode_spinner_item, parent, false);
		}
		TextView textMail = (TextView) convertView.findViewById(R.id.login_dopdown_text_mail);
		ImageView imageApp = (ImageView) convertView.findViewById(R.id.login_dopdown_image_cloud);

		switch (getItem(position)){
			case WEIGHING:
				textMail.setText("Weighing");
				imageApp.setImageResource(R.drawable.ic_menu_weighing);
				break;
			case PART_COUNTING:
				textMail.setText("Part Counting");
				imageApp.setImageResource(R.drawable.application_icon_partcounting);
				break;
			case PERCENT_WEIGHING:
				textMail.setText("Percent Weighing");
				imageApp.setImageResource(R.drawable.application_icon_percent);
				break;
			case CHECK_WEIGHING:
				textMail.setText("Check Weighing");
				imageApp.setImageResource(R.drawable.application_icon_check);
				break;
			case ANIMAL_WEIGHING:
				textMail.setText("Animal Weighing");
				imageApp.setImageResource(R.drawable.application_icon_animal);
				break;
			case FILLING:
				textMail.setText("Filling");
				imageApp.setImageResource(R.drawable.application_icon_filling);
				break;
			case TOTALIZATION:
				textMail.setText("Totalization");
				imageApp.setImageResource(R.drawable.application_icon_totalization);
				break;
			case FORMULATION:
				textMail.setText("Formulation");
				imageApp.setImageResource(R.drawable.application_icon_formula2);
				break;
			case DIFFERENTIAL_WEIGHING:
				textMail.setText("Differential Weighing");
				imageApp.setImageResource(R.drawable.application_icon_differential);
				break;
			case DENSITY_DETERMINATION:
				textMail.setText("Density Determination");
				imageApp.setImageResource(R.drawable.application_icon_density);
				break;
			case PEAK_HOLD:
				textMail.setText("Peak Hold");
				imageApp.setImageResource(R.drawable.application_icon_peak_hold);
				break;
			case INGREDIENT_COSTING:
				textMail.setText("Ingrediant Costing");
				imageApp.setImageResource(R.drawable.application_icon_costing);
				break;
			case PIPETTE_ADJUSTMENT:
				textMail.setText("Pipette Adjustment");
				imageApp.setImageResource(R.drawable.application_icon_pipette);
				break;
			case STATISTICAL_QUALITY_CONTROL_1_HOME:
				textMail.setText("Statistical Quality Control");
				imageApp.setImageResource(R.drawable.application_icon_statistic2);
				break;

			default:
				textMail.setText("Not weighing");
				imageApp.setImageResource(R.drawable.ic_launcher);
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