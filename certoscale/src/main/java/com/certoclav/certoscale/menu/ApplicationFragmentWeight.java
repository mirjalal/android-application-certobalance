package com.certoclav.certoscale.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.listener.StableListener;
import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.settings.unit.SettingsUnitActivity;
import com.certoclav.certoscale.supervisor.ApplicationManager;



public class ApplicationFragmentWeight extends Fragment implements WeightListener, StableListener {
    private FrameLayout barload = null;
    private FrameLayout barloadbackground = null;
    private TextView textInstruction = null;
    private TextView textSum = null;
    private TextView textValue = null;
    private ImageView imageStable = null;

    private static final int WIDTH_LOADING_BAR_TOTAL = 700;




    //Peak Hold Variables

    private long ctime_first=0;
    //private boolean PHfirst=false;
    private double pholdWeight=0;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_application_fragment_weight_display,container, false);
        //Access to views from menu_main xml file
        barload = (FrameLayout) rootView.findViewById(R.id.menu_main_bar_load);
        barloadbackground =(FrameLayout) rootView.findViewById(R.id.menu_main_countdown_bar_background);


        textInstruction = (TextView) rootView.findViewById(R.id.menu_main_text_instruction);
        textSum = (TextView) rootView.findViewById(R.id.menu_main_text_information);
        textValue = (TextView) rootView.findViewById(R.id.menu_main_text_value);
        imageStable = (ImageView) rootView.findViewById(R.id.menu_main_image_stable);

        textValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsUnitActivity.class);
                getActivity().startActivity(intent);
            }
        });




        return rootView;//inflater.inflate(R.layout.article_view, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        Scale.getInstance().setOnWeightListener(this);
        Scale.getInstance().setOnStableListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        Scale.getInstance().removeOnWeightListener(this);
        Scale.getInstance().removeOnStableListener(this);
    }





    @Override
    public void onWeightChanged(Double weight, String unit) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        boolean loadingbarnormal=true;
        switch (Scale.getInstance().getScaleApplication()){
            case ANIMAL_WEIGHING_CALCULATING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText("calculating...");
                textSum.setText("SUM: " + String.format("%.4f",ApplicationManager.getInstance().getTaredValueInGram()));


                break;

            case PERCENT_WEIGHING_CALC_REFERENCE:
                textValue.setTextColor(Color.WHITE);
                textValue.setText( String.format("%.4f",ApplicationManager.getInstance().getTaredValueInGram())+" g");

            case ANIMAL_WEIGHING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getAnimalWeight() + " g");
                textSum.setText("SUM: " + String.format("%.4f",ApplicationManager.getInstance().getTaredValueInGram())+ " g");
                break;
            case PART_COUNTING_CALC_AWP:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getAwpCalcSampleSize() + " pcs");
                textSum.setText("TARED WEIGHT: " + ApplicationManager.getInstance().getTaredValueAsStringInGram());

                break;
            case PART_COUNTING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());

                break;
            case FORMULATION:
                textInstruction.setText("");
            case FORMULATION_RUNNING:
                textInstruction.setText("");
            case DIFFERENTIAL_WEIGHING:
                textInstruction.setText("");
            case WEIGHING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);

                if (ApplicationManager.getInstance().getTaredValueInGram()<ApplicationManager.getInstance().getUnderLimitValueInGram() ) {
                    if (prefs.getBoolean(getString(R.string.preferences_weigh_minimum), getResources().getBoolean(R.bool.preferences_weigh_minimum)) == true) {
                        textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                        textValue.setTextColor(Color.YELLOW);
                    }
                }
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case PERCENT_WEIGHING:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);

                textValue.setText(ApplicationManager.getInstance().getPercent()+ " %");
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsString()+ " g");
                break;

            case CHECK_WEIGHING:
                textInstruction.setText("");

                String cmode = prefs.getString(getString(R.string.preferences_check_displayoptions),"");
                String checklimitmode = prefs.getString(getString(R.string.preferences_check_limitmode),"");
                double current = ApplicationManager.getInstance().getTaredValueInGram();
                double under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                double over = ApplicationManager.getInstance().getOverLimitCheckWeighing();


                if(checklimitmode.equals("1")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getUnderLimitCheckWeighing();
                     over = ApplicationManager.getInstance().getOverLimitCheckWeighing();
                }
                if(checklimitmode.equals("2") || checklimitmode.equals("3")) {
                    current = ApplicationManager.getInstance().getTaredValueInGram();
                    under = ApplicationManager.getInstance().getCheckNominaldouble()-ApplicationManager.getInstance().getCheckNominalToleranceUnderdouble();
                    over = ApplicationManager.getInstance().getCheckNominaldouble()+ApplicationManager.getInstance().getCheckNominalToleranceOverdouble();
                }




                if(cmode.equals("1")) {
                    textInstruction.setText("");
                    textValue.setTextColor(Color.WHITE);


                    textInstruction.setText("");

                    if (current<under){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("↓   "+ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    }

                    if (current>over){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("↑   "+ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    }

                    if (current>=under && current<=over){
                        textValue.setTextColor(Color.GREEN);
                        textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());;
                    }


                    textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                if(cmode.equals("2")) {

                    textInstruction.setText("");


                    if (current<under){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("Value too low");
                    }

                    if (current>over){
                        textValue.setTextColor(Color.RED);
                        textValue.setText("Value too high");
                    }

                    if (current>=under && current<=over){
                        textValue.setTextColor(Color.GREEN);
                        textValue.setText("OK");
                    }


                    textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                }
                break;

            case DENSITIY_DETERMINATION:
                textInstruction.setText("");

                String densityliquidtype = prefs.getString(getString(R.string.preferences_density_liquidtyp),"");
                String densitymode = prefs.getString(getString(R.string.preferences_density_mode),"");

                if (ApplicationManager.getInstance().getDensity_step_counter()==0){
                    textValue.setTextColor(Color.WHITE);
                    textValue.setText("Press Start");

                }
                if(ApplicationManager.getInstance().getDensity_step_counter()==1) {
                    textSum.setText("");
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    //textSum.setTextColor(Color.YELLOW);

                    textInstruction.setText("Weigh sample in air and press accept");
                    if (densitymode.equals("2") || densitymode.equals("3")){
                        textInstruction.setText("Weigh sinker in air and press accept");
                    }
                }

                if(ApplicationManager.getInstance().getDensity_step_counter()==2) {
                    textSum.setText("");
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    //textSum.setTextColor(Color.YELLOW);
                    textInstruction.setText("Weigh sample in liquid and press accept");
                    if (densitymode.equals("2")){
                        textInstruction.setText("Weigh sample in liquid (push the sample under water) and press accept");
                    }

                    if(densitymode.equals("3")){
                        textInstruction.setText("Weigh the sinker in liquid and press accept");
                    }
                }
                if (ApplicationManager.getInstance().getDensity_step_counter()==4){
                    textSum.setText("");
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    textInstruction.setText("Weigh sinker in liquid and press accept");

                }


                if(ApplicationManager.getInstance().getDensity_step_counter()==3) {


                    double Sinkervolume=ApplicationManager.getInstance().getCurrentLibrary().getSinkerVolume();


                    textSum.setText("");

                    //Equation according to http://www.hs-lausitz.de/fileadmin/user_upload/public/fak/fak2/pdf/Physiklabor/M01_Dichtebestimmung.pdf
                    double density=0;
                    double mk=ApplicationManager.getInstance().getDensity_weight_air();
                    double pf=0;
                    double dm=ApplicationManager.getInstance().getDensity_weight_liquid();
                    if (densitymode.equals("1")) {
                        if (densityliquidtype.equals("1")) {
                            pf = ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp());
                        } else {
                            pf = ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity();
                        }
                        density=(mk*pf)/(mk-dm);
                    }

                    if (densitymode.equals("2")){

                        //Equations according to Page 3 from http://www.hs-lausitz.de/fileadmin/user_upload/public/fak/fak2/pdf/Physiklabor/M01_Dichtebestimmung.pdf
                        if (densityliquidtype.equals("1")) {
                            pf = ApplicationManager.getInstance().WaterTempInDensity(ApplicationManager.getInstance().getCurrentLibrary().getWaterTemp());
                        } else {
                            pf = ApplicationManager.getInstance().getCurrentLibrary().getLiquidDensity();
                        }
                        double Vk=dm/pf;
                        density=mk/Vk;
                    }

                    if (densitymode.equals("3")) {

                        //Equations according http://www.kern-sohn.com/manuals/files/German/ABT-A01-BA-d-0710.pdf  Seite 9
                        density= (mk-dm)/Sinkervolume+0.0012;

                    }

                    textValue.setText(String.format("%.4f",density)+" g/cm³");
                    ApplicationManager.getInstance().setDensity(density);
                    //textSum.setTextColor(Color.YELLOW);
                    if (densitymode.equals("1")) {
                        textInstruction.setText("Density calculated");
                    }
                    if (densitymode.equals("2")) {
                        textInstruction.setText("Density of the liquid calculated");
                    }
                }
                break;

            case FILLING:
                textInstruction.setText("");
                textSum.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());

                textInstruction.setText("Fill Status: " + ApplicationManager.getInstance().getPercentFilling()+" %");

                loadingbarnormal=false;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) barload.getLayoutParams();
                FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) barloadbackground.getLayoutParams();
                params.height=15;
                params2.height=15;

                double filling_width=(ApplicationManager.getInstance().getTaredValueInGram()/ApplicationManager.getInstance().getTarget())*WIDTH_LOADING_BAR_TOTAL;

                //double filling_width=(ApplicationManager.getInstance().getTarget()/ApplicationManager.getInstance().getTaredValueInGram())*700;

                if(filling_width<0){
                    filling_width = 0;
                }
                if(filling_width > 700){
                    filling_width = 700;
                }

                params.width= (int) filling_width;
                barload.setLayoutParams(params);
                barloadbackground.setLayoutParams(params2);
                barload.setBackgroundColor(Color.CYAN);


                break;

            case FILLING_CALC_TARGET:
                textInstruction.setText("");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case TOTALIZATION:

                textInstruction.setText("Place sample on the pan. Press Accept to add to the total.");
                textValue.setTextColor(Color.WHITE);
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());
                break;

            case PEAK_HOLD:
                textInstruction.setText("");
                String PeakHoldMode = prefs.getString(getString(R.string.preferences_peak_mode),"");
                double PHcurrentvalue=ApplicationManager.getInstance().getTaredValueInGram();

                boolean PHstable= Scale.getInstance().isStable();
                boolean stableonly=false;

                if  (prefs.getBoolean(getString(R.string.preferences_peak_stableonly),getResources().getBoolean(R.bool.preferences_peak_stableonly))==true) {
                    stableonly=true;
                }



                if (PeakHoldMode.equals("3")) {
                    if (ApplicationManager.getInstance().getSumInGram()<0.02f && pholdWeight>0.02f){
                        ctime_first = System.nanoTime();

                    }
                   // final String TAG = getClass().getSimpleName();
                   // Log.e(TAG, String.format("%d",(System.nanoTime() - ctime_first)));

                    if(ApplicationManager.getInstance().getTaredValueInGram() < 0.02f && (System.nanoTime() - ctime_first) >= 10000000000l){
                       ApplicationManager.getInstance().setPeakHoldMaximum(0);



                    }
                }

                pholdWeight=ApplicationManager.getInstance().getTaredValueInGram();


                //Semi Automatic
                if (ApplicationManager.getInstance().getPeakHoldMaximum()==0 && (PeakHoldMode.equals("2") || PeakHoldMode.equals("3") )){
                    //Start PeakHold Measurement
                    ApplicationManager.getInstance().setPeakHoldActivated(true);
                }

                //Display the Maximum Value if PeakHold is activated
                if (ApplicationManager.getInstance().getPeakHoldActivated()==true){
                    if (PHcurrentvalue>=ApplicationManager.getInstance().getPeakHoldMaximum() && (stableonly==false || PHstable==true)  ){
                        ApplicationManager.getInstance().setPeakHoldMaximum(PHcurrentvalue);
                    }

                    textValue.setTextColor(Color.WHITE);
                    textValue.setText(String.format("%.4f",ApplicationManager.getInstance().getPeakHoldMaximum())+ " g");
                    textSum.setText("Curent Weight: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                }else{
                    textValue.setTextColor(Color.WHITE);
                    textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    textSum.setText("Curent Weight: " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                    ApplicationManager.getInstance().setPeakHoldMaximum(0);


                }

                break;

            case INGREDIENT_COSTING:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText("SUM: " + ApplicationManager.getInstance().getSumAsStringWithUnit());


                break;

            case STATISTICAL_QUALITY_CONTROL:
                textInstruction.setText("");
                textValue.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());
                textValue.setTextColor(Color.WHITE);
                textSum.setText("");

                break;

            case PIPETTE_ADJUSTMENT:
                textInstruction.setText("");

                if (ApplicationManager.getInstance().getPipette_current_sample()==0) {
                    textInstruction.setText("Place Container on the pan. Press Tare");
                }else{
                    textInstruction.setText("Dispense sample "+String.format("%d",ApplicationManager.getInstance().getPipette_current_sample())+ " and press Accept");
                }

                textValue.setText(String.format("%.4f",ApplicationManager.getInstance().getPipetteCalculatedML())+ " ml");
                textValue.setTextColor(Color.WHITE);
                textSum.setText(ApplicationManager.getInstance().getTaredValueAsStringWithUnit());



                break;


            default:
                textValue.setTextColor(Color.WHITE);
                textValue.setText("not implemented");
                break;

        }

    if (loadingbarnormal==true){
        //Update Loading bar
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) barload.getLayoutParams();
            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) barloadbackground.getLayoutParams();
            int fwidth = (int) (((Scale.getInstance().getWeightInGram()/ AppConstants.WEIGHT_MAX_IN_GRAM) * WIDTH_LOADING_BAR_TOTAL));
            if(fwidth<0){
                fwidth = 0;
            }
            if(fwidth > WIDTH_LOADING_BAR_TOTAL){
                fwidth = WIDTH_LOADING_BAR_TOTAL;
            }
            params.width = fwidth;
            params.height=15;
            barload.setLayoutParams(params);

            params2.height=15;
            barloadbackground.setLayoutParams(params2);


            if(ApplicationManager.getInstance().getSumInGram() > 100){
                barload.setBackgroundColor(Color.RED);

            }else{
                barload.setBackgroundColor(Color.GREEN);
            }

        }
    }


    @Override
    public void onStableChanged(boolean isStable)
    {
        if(isStable){
            imageStable.setVisibility(View.VISIBLE);


        }else {
            imageStable.setVisibility(View.INVISIBLE);
        }
    }
}
