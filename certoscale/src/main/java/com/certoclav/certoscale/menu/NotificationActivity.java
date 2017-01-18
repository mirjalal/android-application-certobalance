package com.certoclav.certoscale.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.listener.ScaleStateListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.supervisor.StateMachine;


public class NotificationActivity extends Activity implements ScaleStateListener {



	private LinearLayout notificationContainer = null;
	
	private LinearLayout notificationHeadContainer = null;
	private TextView textNotificationHead = null;
	private Button buttonOk = null;
	private Button buttonCancel = null;
	private VideoView videoView = null;


	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor_notification_activity);


		notificationHeadContainer = (LinearLayout) findViewById(R.id.menu_notification_headtext_background);
		textNotificationHead = (TextView) findViewById(R.id.menu_notification_headtext);
		buttonOk = (Button) findViewById(R.id.menu_btn_ok);
		buttonOk.setVisibility(View.GONE);
		buttonCancel = (Button) findViewById(R.id.menu_btn_cancel);
		buttonCancel.setText("Ignore".toUpperCase());
		textNotificationHead.setText(R.string.warning);
		videoView = (VideoView) findViewById(R.id.menu_notification_video);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StateMachine.getInstance().setIgnoreErrors(true);
				finish();
			}
		});

		//notificationHeadContainer.setBackgroundResource(R.drawable.background_error);

		final TextView tv = (TextView) findViewById(R.id.text_message);

		switch (Scale.getInstance().getState()){
			case ON_AND_CALIBRATING:
				textNotificationHead.setText("NOTIFICATION");
				tv.setText("Calibrating...");
				break;
			case CABLE_NOT_CONNECTED:
				textNotificationHead.setText("WARNING");
				tv.setText("Please plug in the touchscreen and power on the balance.");
				break;
			case ON_AND_MODE_NOT_GRAM:
				textNotificationHead.setText("NOTIFICATION");
				tv.setText("Selftest");
				break;
			case DISCONNECTED:
				textNotificationHead.setText("WARNING");
				tv.setText("Touchscreen disconnected");
				break;
			case OFF:
				textNotificationHead.setText("WARNING");
				tv.setText("Balance is powered OFF");
				break;
			default:
				finish();
		}




		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					switch (Scale.getInstance().getState()){
						case ON_AND_MODE_GRAM:
							finish();
							break;
						default:

					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		
	}




	@Override
	public void onResume(){


	//	videoView.setVideoPath("path to video");
	//	videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
	//		@Override
	//		public void onPrepared(MediaPlayer mp) {
				//mp.setLooping(true);
	//		}
	//	});
		//	videoView.start();
		super.onResume();

	}




	@Override
	public void onScaleStateChange(ScaleState state) {


		if(state != ScaleState.CABLE_NOT_CONNECTED){
			finish();
		}



	}
}

