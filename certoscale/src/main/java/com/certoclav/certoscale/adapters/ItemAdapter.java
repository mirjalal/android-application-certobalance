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
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.util.LabelPrinterUtils;
import com.certoclav.certoscale.view.QuickActionItem;
import com.certoclav.library.certocloud.CertocloudConstants;
import com.certoclav.library.certocloud.DeleteTask;

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

	public Boolean getHideActionButtons() {
		return hideActionButtons;
	}

	public void setHideActionButtons(Boolean hideActionButtons) {
		this.hideActionButtons = hideActionButtons;
	}

	private Boolean hideActionButtons = false;

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
	private QuickActionItem actionItemPrint;



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
		
		actionItemPrint = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems,false);
		containerItems.addView(actionItemPrint);


		TextView editTextArticleNumber = (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_artnumber);
		editTextArticleNumber.setText(getItem(position).getArticleNumber());
		editTextArticleNumber.setSelected(true);

		TextView editName = (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_name);
		editName.setText(getItem(position).getName());

		TextView editUnitCost =  (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_unit_cost);
		editUnitCost.setText(String.format(Locale.US,"%.2f",getItem(position).getCost()));

		ImageView imageCloud = (ImageView)  convertView.findViewById(R.id.menu_main_item_image_cloud);
		if(getItem(position).getCloudId().isEmpty()){
			imageCloud.setImageResource(R.drawable.cloud_no_white);
		}else{
			imageCloud.setImageResource(R.drawable.cloud_ok_white);
		}

		TextView editWeight = (TextView) convertView.findViewById(R.id.menu_main_item_edit_element_weight);
		editWeight.setText(String.format(Locale.US,"%.4f",getItem(position).getWeight()) + " "+ "g");


		if(hideActionButtons){
			actionItemPrint.setVisibility(View.INVISIBLE);
			actionItemDelete.setVisibility(View.INVISIBLE);
			actionItemEdit.setVisibility(View.INVISIBLE);
		}else{
			actionItemPrint.setVisibility(View.VISIBLE);
			actionItemDelete.setVisibility(View.VISIBLE);
			actionItemEdit.setVisibility(View.VISIBLE);
		}

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


		actionItemPrint.setChecked(false);
		actionItemPrint.setImageResource(R.drawable.ic_menu_print);

		//actionItemDelete.setText(getContext().getString(R.string.delete));
		actionItemPrint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LabelPrinterUtils.printItem(getItem(position));
				Toast.makeText(mContext,"Item printed", Toast.LENGTH_LONG).show();
				for(OnClickButtonListener listener : onClickButtonListeners){
				//	listener.onClickButtonPrint(getItem(position));
				}



			}
		});


		actionItemDelete.setChecked(false);
		actionItemDelete.setImageResource(R.drawable.ic_menu_bin);

			//actionItemDelete.setText(getContext().getString(R.string.delete));
			actionItemDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if(getItem(position).getCloudId().isEmpty() == false){
						DeleteTask deleteTask = new DeleteTask();
						deleteTask.execute(CertocloudConstants.SERVER_URL + CertocloudConstants.REST_API_DELETE_ITEM + getItem(position).getCloudId());
					}
					DatabaseService db = new DatabaseService(mContext);
					db.deleteItem(getItem(position));
					remove(getItem(position));
					notifyDataSetChanged();



				}
			});

		
		return convertView;
	}
}