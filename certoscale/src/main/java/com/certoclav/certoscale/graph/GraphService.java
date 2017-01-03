package com.certoclav.certoscale.graph;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.certoclav.certoscale.listener.WeightListener;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.graph.LineGraph;
import com.certoclav.library.graph.Point;

import org.achartengine.GraphicalView;

public class GraphService implements WeightListener{

	private GraphicalView view;
	private LineGraph runningGraph = new LineGraph();

	private long nanoTimeAtStart = 0;
	private long nanoTimeSinceStart = 0;


	// Added for mean Value calculation
	public double counter=0;
	public double sum=0;

	private static GraphService instance = new GraphService();

	public static synchronized GraphService getInstance() {
		return instance;

	}

	private Handler mGuiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			view.invalidate();
			view.repaint();
			view.invalidate();
		}

		;
	};


	private GraphService() {
		Scale.getInstance().setOnWeightListener(this);
		runningGraph.getRendererForSeriesSteam().setColor(Color.GREEN);

	}


	public GraphicalView getCurrentGraph(Context context) {

		view = runningGraph.getView(context);
		return view;
	}

	@Override
	public void onWeightChanged(Double weight, String unit) {
		try {
			switch (Scale.getInstance().getScaleApplication()) {
				case ANIMAL_WEIGHING_CALCULATING:

					nanoTimeSinceStart = System.nanoTime() - nanoTimeAtStart;
					int timeStampInMilliSeconds = (int) (nanoTimeSinceStart/1000000L);

					Point p = new Point((double) timeStampInMilliSeconds, ApplicationManager.getInstance().getTaredValueInGram());
					runningGraph.addNewPoints(p, LineGraph.TYPE_STEAM);

					counter++;
					sum=sum+ApplicationManager.getInstance().getTaredValueInGram();

					Log.e("GraphService", "add new point: " + p.getX() + " "+ p.getY());
					double[] range = new double[4];
					range[0] = 0;
					range[1] = (int)timeStampInMilliSeconds;
					range[2] = 0;
					range[3] = 150;
					runningGraph.setRange(range);

					mGuiHandler.sendEmptyMessage(0);
					break;
				default:

					nanoTimeAtStart = System.nanoTime();
					runningGraph.clearAllPoints();
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}