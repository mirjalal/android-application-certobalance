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
import com.certoclav.certoscale.database.Unit;

import java.util.List;
import java.util.Locale;

/**
 * Created by Enrico on 24.02.2017.
 */

public class UnitEditAdapter extends ArrayAdapter<Unit> {





    private final Context mContext;


    public UnitEditAdapter(Context context, List<Unit> values) {
        super(context, R.layout.menu_main_unit_element, values);
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
            convertView = inflater.inflate(R.layout.menu_main_unit_element, parent, false);
        }

        EditText editTextName = (EditText) convertView.findViewById(R.id.menu_main_recipe_edit_name);
        editTextName.setText(getItem(position).getName());

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem(position).setName(s.toString());
                Log.e("ItemEditAdapter", "text changed");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        EditText editTextDescripton = (EditText) convertView.findViewById(R.id.menu_main_recipe_edit_description);
        editTextDescripton.setText(getItem(position).getDescription());
        editTextDescripton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem(position).setDescription(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                getItem(position).setName(s.toString());
            }
        });

        //
        EditText editTextFactor = (EditText) convertView.findViewById(R.id.menu_main_recipe_edit_factor);
        editTextFactor.setText(String.format(Locale.US,"%.4f",getItem(position).getFactor()));
        editTextFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    getItem(position).setFactor(Double.parseDouble(s.toString()));
                }catch (Exception e){
                    getItem(position).setFactor(0d);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        EditText editWeight = (EditText) convertView.findViewById(R.id.menu_main_recipe_edit_text_exponent);
        editWeight.setText(String.format(Locale.US,"%.4f",getItem(position).getExponent()));

        editWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    getItem(position).setExponent(Double.parseDouble(s.toString()));
                }catch (Exception e){
                    getItem(position).setExponent(0d);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        return convertView;
    }


}
