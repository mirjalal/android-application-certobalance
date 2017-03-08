package com.certoclav.certoscale.adapters;

import android.content.Context;
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
import com.certoclav.certoscale.database.Recipe;
import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.view.QuickActionItem;

import java.util.ArrayList;
import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class RecipeAdapter extends ArrayAdapter<Recipe> {

	public interface OnClickButtonListener {
		 void onClickButtonDelete(Recipe recipe);
		 void onClickButtonEdit(Recipe recipe);
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



	public RecipeAdapter(Context context, List<Recipe> values) {
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
		

			convertView = inflater.inflate(R.layout.menu_main_recipe_element, parent, false);
			LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);
			
			actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			containerItems.addView(actionItemDelete);
			
			actionItemEdit = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			containerItems.addView(actionItemEdit);


		ImageView imageCloud = (ImageView) convertView.findViewById(R.id.list_element_recipe_image_cloud);
		if(getItem(position).getCloudId().isEmpty()){
			imageCloud.setImageResource(R.drawable.cloud_no_white);
		}else {
			imageCloud.setImageResource(R.drawable.cloud_ok_white);
		}
			
		TextView firstLine = (TextView) convertView.findViewById(R.id.first_line);
		firstLine.setText(getItem(position).getRecipeName());

		final TextView secondLine = (TextView) convertView.findViewById(R.id.second_line);
		StringBuilder sb = new StringBuilder();
		sb.append("Formation: ");
		for(RecipeEntry entry : getItem(position).getRecipeEntries()){
			sb.append(entry.getWeight()).append(" g").append(" ").append(entry.getDescription()).append(", ");
		}
		secondLine.setText(sb.toString());

	
		
		
		
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