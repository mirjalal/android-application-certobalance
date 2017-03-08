package com.certoclav.certoscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;
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
public class RecipeResultElementAdapter extends ArrayAdapter<RecipeEntry> {

    public interface OnClickButtonListener {
        void onClickButtonDelete(RecipeEntry recipe);
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



    public RecipeResultElementAdapter(Context context, List<RecipeEntry> values) {
        super(context, R.layout.menu_main_recipe_edit_element, values);
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


        convertView = inflater.inflate(R.layout.menu_main_recipe_result_element, parent, false);
        LinearLayout containerItems =  (LinearLayout) convertView.findViewById(R.id.user_list_element_container_button);

        //actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
        //containerItems.addView(actionItemDelete);


        TextView editName = (TextView) convertView.findViewById(R.id.menu_main_recipe_edit_element_name);
        editName.setText(getItem(position).getDescription());


        TextView editWeight = (TextView) convertView.findViewById(R.id.menu_main_recipe_edit_element_weight);
        editWeight.setText(String.format("%.4f",getItem(position).getWeight()));

        TextView editmeasuredWeight = (TextView) convertView.findViewById(R.id.menu_main_recipe_edit_element_measured_weight);
        editmeasuredWeight.setText(String.format("%.4f",getItem(position).getMeasuredWeight()));

        double d=Math.abs(getItem(position).getMeasuredWeight()-getItem(position).getWeight())/Math.abs(getItem(position).getWeight());
        d=d*100;
        TextView difference = (TextView) convertView.findViewById(R.id.menu_main_recipe_edit_element_difference);
        difference.setText(String.format("%.2f",d) + " %");



        return convertView;
    }
}