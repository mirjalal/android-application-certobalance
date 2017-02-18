package com.certoclav.certoscale.menu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certoclav.certoscale.R;

import com.certoclav.certoscale.model.ActionButtonbarFragment;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleApplication;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import java.io.IOException;
import java.text.ParseException;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ApplicationFragmentSettingsPeakHold extends Fragment {

    private LinearLayout containerSettingsButtons = null;
    private Button Start = null;
    private Button End = null;








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_settings_peak_hold,container, false);

        Start =  (Button) rootView.findViewById(R.id.application_settings_peak_hold_Start);
        End =  (Button) rootView.findViewById(R.id.application_settings_peak_hold_End);



        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //Make sure that only one Button is clickable
                    Start.setEnabled(false);
                    End.setEnabled(true);

                    //Start PeakHold Measurement
                    ApplicationManager.getInstance().setPeakHoldActivated(true);
                    Scale.getInstance().setScaleApplication(ScaleApplication.PEAK_HOLD_STARTED);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //Make sure that only one Button is clickable
                    Start.setEnabled(true);
                    End.setEnabled(false);

                    // End PeakHold Measurenment
                    ApplicationManager.getInstance().setPeakHoldActivated(false);
                    Scale.getInstance().setScaleApplication(ScaleApplication.PEAK_HOLD);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });






        End.setEnabled(false);



        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        //get PeakHoldMode
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String PeakHoldMode = prefs.getString(getString(R.string.preferences_peak_mode),"");


        //Semi Automatic Mode
        if(PeakHoldMode.equals("2")){

            Start.setEnabled(false);
            End.setEnabled(true);
            //Start PeakHold Measurement
            ApplicationManager.getInstance().setPeakHoldActivated(true);
            Scale.getInstance().setScaleApplication(ScaleApplication.PEAK_HOLD_STARTED);
        }

        //Automatic Mode
        if(PeakHoldMode.equals("3")){

            Start.setEnabled(false);
            End.setEnabled(false);
            //Start PeakHold Measurement
            ApplicationManager.getInstance().setPeakHoldActivated(true);
            Scale.getInstance().setScaleApplication(ScaleApplication.PEAK_HOLD_STARTED);
        }





        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();

    }




}
