package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.Item;
import com.certoclav.certoscale.database.SQC;
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
public class SQCAdapter extends ArrayAdapter<SQC> {

    public interface OnClickButtonListener {
        void onClickButtonDelete(SQC sqc);
        void onClickButtonEdit(SQC sqc);
    }
    ArrayList<OnClickButtonListener> onClickButtonListeners = new ArrayList<OnClickButtonListener>();

    public void setOnClickButtonListener(OnClickButtonListener listener){
        onClickButtonListeners.add(listener);
    }
    public void removeOnClickButtonListener(OnClickButtonListener listener){
        onClickButtonListeners.remove(listener);
    }



    private final Context mContext;




    public SQCAdapter(Context context, List<SQC> values) {
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


        convertView = inflater.inflate(R.layout.menu_main_sqc, parent, false);
        LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);



        TextView editTextName = (TextView) convertView.findViewById(R.id.menu_main_sqc_edit_element_name);
        editTextName.setText(getItem(position).getName());

        TextView editTextMaximum = (TextView) convertView.findViewById(R.id.menu_main_sqc_edit_element_maximum);
        editTextMaximum.setText(String.format("%.4f",getItem(position).getStatistics().getMax())+" g");


        TextView editTextMinimum =  (TextView) convertView.findViewById(R.id.menu_main_sqc_edit_element_minimum);
        editTextMinimum.setText(String.format(Locale.US,"%.4f",getItem(position).getStatistics().getMin())+" g");

        TextView editTextAverage =  (TextView) convertView.findViewById(R.id.menu_main_sqc_edit_element_average);
        editTextAverage.setText(String.format(Locale.US,"%.4f",getItem(position).getStatistics().getMean())+ " g");


        TextView editTextStandardDeviation = (TextView) convertView.findViewById(R.id.menu_main_sqc_edit_element_standard);
        editTextStandardDeviation.setText(String.format(Locale.US,"%.4f",getItem(position).getStatistics().getStandardDeviation())+ " g");








        return convertView;
    }
}