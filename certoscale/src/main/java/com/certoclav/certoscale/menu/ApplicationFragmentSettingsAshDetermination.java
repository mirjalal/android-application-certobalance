package com.certoclav.certoscale.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.certoclav.certoscale.R;


public class ApplicationFragmentSettingsAshDetermination extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button buttonChooseItem = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_ash_determination,container, false);

        buttonChooseItem = (Button) rootView.findViewById(R.id.application_settings_ash_determination_button_item_name);


        buttonChooseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //   Intent intent = new Intent(getActivity(), MenuItemActivity.class);
           //     intent.putExtra(AppConstants.INTENT_EXTRA_PICK_ON_CLICK,true);
           //     getActivity().startActivity(intent);
            }
        });




        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {

            buttonChooseItem.setText("unbelegt");



        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
