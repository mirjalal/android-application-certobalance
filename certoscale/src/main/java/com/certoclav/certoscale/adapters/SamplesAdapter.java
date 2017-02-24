package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.certoscale.view.QuickActionItem;

import java.util.ArrayList;
import java.util.List;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 *
 */
public class SamplesAdapter extends ArrayAdapter<Double> {

    public interface OnClickButtonListener {
        void onClickButtonDelete(Double d);
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



    public SamplesAdapter(Context context, List<Double> values) {
        super(context, R.layout.menu_main_samples, values);
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


        convertView = inflater.inflate(R.layout.menu_main_samples, parent, false);


        //actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
        //containerItems.addView(actionItemDelete);


        TextView text = (TextView) convertView.findViewById(R.id.menu_main_samples_edit_text);

        text.setText("Item "+String.format("%02d",position)+":    "+ApplicationManager.getInstance().getTransformedWeightAsStringWithUnit(ApplicationManager.getInstance().getStats().getSamples().get(position)));





        return convertView;
    }
}