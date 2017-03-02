package com.certoclav.certoscale.menu;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ScaleStateListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;


/**
 * 
 * @author Michael
 * This Activity shows an animation as long as the Scale is calibrating
 * This activity resumes back to previous activity after the animation is done.
 */
public class AnimationCalibrationActivity extends Activity implements ScaleStateListener{


    private TextView textProgress = null;
    private final static int DO_UPDATE_TEXT = 0;
    private final static int DO_THAT = 1;
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch(msg.arg1) {
            case DO_UPDATE_TEXT:
            	textProgress.setText(getString(R.string.Internal_Calibration)+"\n"+getString(R.string.completed_in) + String.format("%02d seconds", msg.arg2));
            	break;
            case DO_THAT: break;
            }
        }
    };



	
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_animation_calibration_activity);

		textProgress = (TextView) findViewById(R.id.menu_save_eeprom_text_progress);

           }







	@Override
	protected void onResume() {
		Scale.getInstance().setOnScaleStateListener(this);
		if(Scale.getInstance().getState() != ScaleState.ON_AND_CALIBRATING){
			finish();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Scale.getInstance().removeOnScaleStateListener(this);
	}

	/**
	 * By overriding this function, its possible to block (disable) the hardware BACK-button or to do alterntave action instead of navigate to previous activity or home screen.
	 */
	@Override
	public void onBackPressed() {
		//do nothing
	}

	@Override
	public void onScaleStateChange(ScaleState state) {
		if(state != ScaleState.ON_AND_CALIBRATING){
			finish();
		}
	}
}


