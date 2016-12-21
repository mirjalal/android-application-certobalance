package com.certoclav.certoscale.menu;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.adapters.VideoAdapter;
import com.certoclav.certoscale.model.Video;
import com.certoclav.library.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends Activity {

	private GridView programGrid;


	



	
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_fragment_information_video_activity);

		programGrid = (GridView) findViewById(R.id.video_gridlayout);
		
		
		List<Video> videos = new ArrayList<Video>();
		FileUtils fileUtils = new FileUtils();
		for(File file : fileUtils.getVideosFromDownloadDirectory()){
			videos.add(new Video(file.getAbsolutePath(), file.getName().replace("_", " ").replace(".mp4", ""), ""));
		}

		if(videos != null){
				VideoAdapter videoAdapter = new VideoAdapter(this, videos);
				
				programGrid.setAdapter(videoAdapter);
				
				programGrid.setOnItemClickListener(null);
		}

	}
public void onClickImageShare(View view){
	finish();
}
}


