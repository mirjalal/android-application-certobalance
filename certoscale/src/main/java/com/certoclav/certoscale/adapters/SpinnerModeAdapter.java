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
				textMail.setText(R.string.app_weighing);
				imageApp.setImageResource(R.drawable.application_icon_weighing);
				break;
			case PART_COUNTING:
				textMail.setText(R.string.app_part_counting);
				imageApp.setImageResource(R.drawable.application_icon_partcounting);
				break;
			case PERCENT_WEIGHING:
				textMail.setText(R.string.app_percent_weighing);
				imageApp.setImageResource(R.drawable.application_icon_percent);
				break;
			case CHECK_WEIGHING:
				textMail.setText(R.string.app_check_weighing);
				imageApp.setImageResource(R.drawable.application_icon_check);
				break;
			case ANIMAL_WEIGHING:
				textMail.setText(R.string.app_animal_weighing);
				imageApp.setImageResource(R.drawable.application_icon_animal);
				break;
			case FILLING:
				textMail.setText(R.string.app_filling);
				imageApp.setImageResource(R.drawable.application_icon_filling);
				break;
			case TOTALIZATION:
				textMail.setText(R.string.app_totalization);
				imageApp.setImageResource(R.drawable.application_icon_totalization);
				break;
			case FORMULATION:
				textMail.setText(R.string.app_formulation);
				imageApp.setImageResource(R.drawable.application_icon_formula2);
				break;
			case DIFFERENTIAL_WEIGHING:
				textMail.setText(R.string.app_differential_weighing);
				imageApp.setImageResource(R.drawable.application_icon_differential);
				break;
			case DENSITY_DETERMINATION:
				textMail.setText(R.string.app_density_determination);
				imageApp.setImageResource(R.drawable.application_icon_density);
				break;
			case PEAK_HOLD:
				textMail.setText(R.string.app_peak_hold);
				imageApp.setImageResource(R.drawable.application_icon_peak_hold);
				break;
			case INGREDIENT_COSTING:
				textMail.setText(R.string.app_ingrediant_costing);
				imageApp.setImageResource(R.drawable.application_icon_costing);
				break;
			case PIPETTE_ADJUSTMENT_1_HOME:
				textMail.setText(R.string.app_pipette_adjustment);
				imageApp.setImageResource(R.drawable.application_icon_pipette);
				break;
			case STATISTICAL_QUALITY_CONTROL_1_HOME:
				textMail.setText(R.string.app_statistical_quality_control);
				imageApp.setImageResource(R.drawable.application_icon_statistic2);
				break;
			case ASH_DETERMINATION_HOME:
				textMail.setText(R.string.app_ash_determination);
				imageApp.setImageResource(R.drawable.application_icon_differential);
				break;

			default:
				textMail.setText(R.string.app_not_weighing);
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