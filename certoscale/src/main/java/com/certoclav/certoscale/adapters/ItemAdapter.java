package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.view.QuickActionItem;

import java.util.ArrayList;
import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class ItemAdapter extends ArrayAdapter<Item> {

	public interface OnClickButtonListener {
		 void onClickButtonDelete(Item item);
		 void onClickButtonSave(Item item);
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
	private QuickActionItem actionItemSave;



	public ItemAdapter(Context context, List<Item> values) {
		super(context, R.layout.menu_main_item_edit_element, values);
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
		

			convertView = inflater.inflate(R.layout.menu_main_item_edit_element, parent, false);
			LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);
			
			actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
			containerItems.addView(actionItemDelete);

		actionItemSave = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
		containerItems.addView(actionItemSave);

		EditText editTextArticleNumber = (EditText) convertView.findViewById(R.id.menu_main_item_edit_element_artnumber);
		


		EditText editName = (EditText) convertView.findViewById(R.id.menu_main_item_edit_element_name);
		editName.setText(getItem(position).getName());
		editName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				getItem(position).setName(s.toString());
			}
		});

		EditText editWeight = (EditText) convertView.findViewById(R.id.menu_main_item_edit_element_weight);
		editWeight.setText(String.format("%.4f",getItem(position).getWeight()));

		editWeight.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					getItem(position).setWeight(Double.parseDouble(s.toString()));
				}catch (Exception e){
					getItem(position).setWeight(0d);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


		actionItemSave.setChecked(false);
		actionItemSave.setImageResource(R.drawable.ic_menu_save);

		//actionItemDelete.setText(getContext().getString(R.string.delete));
		actionItemSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(OnClickButtonListener listener : onClickButtonListeners){
					listener.onClickButtonSave(getItem(position));
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