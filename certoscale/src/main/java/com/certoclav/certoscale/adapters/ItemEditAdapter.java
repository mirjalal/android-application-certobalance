package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;

import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class ItemEditAdapter extends ArrayAdapter<Item> {

	
	

	private final Context mContext;


	public ItemEditAdapter(Context context, List<Item> values) {
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
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.menu_main_item_edit_element, parent, false);
		}

		EditText editTextArticleNumber = (EditText) convertView.findViewById(R.id.menu_main_item_edit_element_artnumber);
		editTextArticleNumber.setText(getItem(position).getItemArticleNumber());

		editTextArticleNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				getItem(position).setArticleNumber(s.toString());
				Log.e("ItemEditAdapter", "text changed");

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		


		EditText editName = (EditText) convertView.findViewById(R.id.menu_main_item_edit_element_name);
		editName.setText(getItem(position).getName());
		editName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				getItem(position).setName(s.toString());

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



		
		return convertView;
	}
}