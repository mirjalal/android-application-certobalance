package com.certoclav.certoscale.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.graph.GraphService;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;

import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING;
import static com.certoclav.certoscale.model.ScaleApplication.ANIMAL_WEIGHING_CALCULATING;


public class ApplicationFragmentAnimalWeighing extends Fragment {


    private LinearLayout graphContainer = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_animal_weighing,container, false);

        graphContainer = (LinearLayout) rootView.findViewById(R.id.application_settings_animal_container_graph);

       return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        GraphService.getInstance().counter=0;
        GraphService.getInstance().sum=0;
        graphContainer.removeAllViews();
        graphContainer.addView(GraphService.getInstance().getCurrentGraph(getActivity()));
        Scale.getInstance().setScaleApplication(ANIMAL_WEIGHING_CALCULATING);
        ApplicationManager.getInstance().setAnimalWeight(0d);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                //only set calculated value if application animal_weighing_calculating still active and not closed by user meanwhile
                if(Scale.getInstance().getScaleApplication() == ANIMAL_WEIGHING_CALCULATING) {
                    Scale.getInstance().setScaleApplication(ANIMAL_WEIGHING);
                    double mean = GraphService.getInstance().sum / GraphService.getInstance().counter;
                    ApplicationManager.getInstance().setAnimalWeight(mean);
                }
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    double avtime= (ApplicationManager.getInstance().getCurrentLibrary().getAveragingTime()*1000);
                    Thread.sleep((int) avtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //TODO VISUALIZE COUNTDOWN
                return null;
            }
        }.execute();

        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();


    }




}
