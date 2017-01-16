package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.view.QuickActionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class ItemAdapter extends ArrayAdapter<Item> {

	public interface OnClickButtonListener {
		 void onClickButtonDelete(Item item);
		 void onClickButtonEdit(Item item);
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



	public ItemAdapter(Context context, List<Item> values) {
		super(context, R.layout.menu_main_item_element, values);
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
		

		convertView = inflater.inflate(R.layout.menu_main_item_element, parent, false);
		LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);
			
		actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
		containerItems.addView(actionItemDelete);

		actionItemEdit = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
		containerItems.addView(actionItemEdit);

		TextView editTextArticleNumber = (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_artnumber);
		editTextArticleNumber.setText(getItem(position).getItemArticleNumber());

		TextView editName = (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_name);
		editName.setText(getItem(position).getName());

		TextView editUnitCost =  (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_unit_cost);
		editUnitCost.setText(String.format(Locale.US,"%.2f",getItem(position).getCost()));



		TextView editWeight = (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_weight);
		editWeight.setText(String.format(Locale.US,"%.4f",getItem(position).getWeight()));




		actionItemEdit.setChecked(false);
		actionItemEdit.setImageResource(R.drawable.ic_menu_edit);

		//actionItemDelete.setText(getContext().getString(R.string.delete));
		actionItemEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(OnClickButtonListener listener : onClickButtonListeners){
					listener.onClickButtonEdit(getItem(position));
				}



			}
		});


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

		
		return convertView;
	}
}