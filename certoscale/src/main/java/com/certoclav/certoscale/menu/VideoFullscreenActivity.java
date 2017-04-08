package com.certoclav.certoscale.menu;


import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Navigationbar;


public class VideoFullscreenActivity extends Activity  {

	

Navigationbar navigationbar = null;

	
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_fragment_information_video_fullscreen_activity);
		navigationbar = new Navigationbar(this);
		navigationbar.onCreate();
		navigationbar.getTextTitle().setVisibility(View.VISIBLE);
		navigationbar.getButtonBack().setVisibility(View.VISIBLE);
		navigationbar.getTextTitle().setText("VIDEO");




	}

	@Override
	protected void onResume() {

		try{
			String videoPath = getIntent().getExtras().getString(AppConstants.INTENT_EXTRA_VIDEOFULLSCREENACTIVITY_VIDEO_PATH);
			String[] videoNames = videoPath.split("/");
			String videoName = videoNames[videoNames.length-1];
			videoName = videoName.replace("_", " ");
			videoName = videoName.replace(".mp4", " ");
			navigationbar.getTextTitle().setText(videoName);
		}catch (Exception e){

		}

		VideoView videoView = (VideoView) findViewById(R.id.information_video_fullscreen_videoView);
		String videoPath = getIntent().getExtras().getString(AppConstants.INTENT_EXTRA_VIDEOFULLSCREENACTIVITY_VIDEO_PATH);


		videoView.setVideoPath(videoPath);

		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);

			}
		});

		videoView.start();


		super.onResume();
	}

	public void onClickImageShare(View view){
	finish();
}
}


