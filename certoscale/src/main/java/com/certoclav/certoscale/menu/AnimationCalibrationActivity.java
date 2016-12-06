package com.certoclav.certoscale.menu;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.certoclav.certoscale.R;


/**
 * 
 * @author Michael
 * This Activity shows an animation as long as the Scale is calibrating
 * This activity resumes back to previous activity after the animation is done.
 */
public class AnimationCalibrationActivity extends Activity {


    private TextView textProgress = null;
    private final static int DO_UPDATE_TEXT = 0;
    private final static int DO_THAT = 1;
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch(msg.arg1) {
            case DO_UPDATE_TEXT:
            	textProgress.setText("Internal calibration\ncompleted in: " + String.format("%02d seconds", msg.arg2));	
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
		final Thread myThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				int maxI = 45;
				while(i<maxI){
					i++;
					Message msg = new Message();
					msg.arg1 = DO_UPDATE_TEXT;
					msg.arg2 = maxI-i; //seconds left of lifetime of this thread
					myHandler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				finish();
			}
		});
		myThread.start();
		super.onResume();
	}


	/**
	 * By overriding this function, its possible to block (disable) the hardware BACK-button or to do alterntave action instead of navigate to previous activity or home screen.
	 */
	@Override
	public void onBackPressed() {
		//do nothing
	}

}


