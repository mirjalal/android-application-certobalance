package com.certoclav.certoscale.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.util.ProtocolPrinterUtils;
import com.certoclav.certoscale.view.QuickActionItem;

import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 * 
*/
public class ProtocolAdapter extends ArrayAdapter<Protocol> {


	private final Context mContext;

	private QuickActionItem actionItemPrint;
	private QuickActionItem actionItemView;
	private QuickActionItem actionItemDelete;

	public ProtocolAdapter(Context context, List<Protocol> values) {
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
		

			convertView = inflater.inflate(R.layout.list_element_protocol, parent, false);
			LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.container_button);
			containerItems.removeAllViews();



		TextView firstLine = (TextView) convertView.findViewById(R.id.first_line);
		firstLine.setText(getItem(position).getName());


		TextView secondLine = (TextView) convertView.findViewById(R.id.second_line);
		secondLine.setText(getItem(position).getDate());

		actionItemPrint = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
		containerItems.addView(actionItemPrint);

		actionItemView = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
		containerItems.addView(actionItemView);

		actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
		containerItems.addView(actionItemDelete);

		actionItemDelete.setChecked(false);
		actionItemDelete.setImageResource(R.drawable.ic_menu_bin);
		actionItemDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{



					final Dialog dialog = new Dialog(mContext);
					dialog.setContentView(R.layout.dialog_yes_no);
					dialog.setTitle("Confirm deletion");

					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.text);
					text.setText(mContext.getString(R.string.do_you_really_want_to_delete) + " " + getItem(position).getName());
					Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
					dialogButtonNo.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							DatabaseService db = new DatabaseService(mContext);
							db.deleteProtocol(getItem(position));
							remove(getItem(position));
							notifyDataSetChanged();
							dialog.dismiss();
						}
					});

					dialog.show();


				}
				catch (Exception e)
				{
					e.printStackTrace();
				}


			}
		});

		actionItemPrint.setChecked(false);
		actionItemPrint.setImageResource(R.drawable.ic_menu_print);

		//actionItemDelete.setText(getContext().getString(R.string.delete));
		actionItemPrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProtocolPrinterUtils protocolPrinterUtils = new ProtocolPrinterUtils();
				protocolPrinterUtils.printText(getItem(position).getContent());
				Toast.makeText(mContext,"Protocol printed", Toast.LENGTH_LONG).show();
			}
		});

		actionItemView.setChecked(false);
		actionItemView.setImageResource(R.drawable.ic_menu_view);
		actionItemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					final Dialog dialog = new Dialog(mContext);
					dialog.setContentView(R.layout.dialog_protocol);
					dialog.setTitle("Protocol " + getItem(position).getName());
					TextView textView = (TextView) dialog.findViewById(R.id.dialog_protocol_text);
					textView.setText(getItem(position).getContent());


					Button dialogButtonClose = (Button) dialog.findViewById(R.id.dialog_button_close);
					dialogButtonClose.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.show();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return convertView;
	}
}