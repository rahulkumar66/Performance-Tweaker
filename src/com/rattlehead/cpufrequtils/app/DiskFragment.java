package com.rattlehead.cpufrequtils.app;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
import com.rattlehead.cpufrequtils.app.utils.TimeInStateReader;

public class DiskFragment extends SherlockFragment {
	View mView;
	ArrayList<CpuState> _states = new ArrayList<CpuState>();

	private static int[] COLORS = new int[] { Color.GREEN, Color.YELLOW,
			Color.BLUE, Color.MAGENTA, Color.DKGRAY, Color.CYAN, Color.RED };

	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TimeInStateReader reader = new TimeInStateReader();
		_states = reader.getCpuStateTime(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.cpu_info_fragment, container, false);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRenderer.setLabelsTextSize((float) 20.00);
		mRenderer.setDisplayValues(true);
		mRenderer.setInScroll(true);
		mRenderer.setClickEnabled(true);
		mRenderer.setShowLegend(false);
		mRenderer.setPanEnabled(false);
		mRenderer.setStartAngle(0);

		for (int i = 0; i < _states.size(); i++) {
			if (_states.get(i).getFrequency() == 0) {
				mSeries.add("Deep Sleep ", _states.get(i).getTime());
			} else {
				mSeries.add(((_states.get(i).getFrequency()) / 1000) + " Mhz",
						_states.get(i).getTime());
			}
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
					% COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);

		}

		LinearLayout layout = (LinearLayout) mView.findViewById(R.id.chart);
		mChartView = ChartFactory.getPieChartView(mView.getContext(), mSeries,
				mRenderer);
		layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		mChartView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SeriesSelection seriesSelection = mChartView
						.getCurrentSeriesAndPoint();
				if (mChartView != null) {
					for (int i = 0; i < mSeries.getItemCount(); i++) {
						if (seriesSelection == null) {
						} else {
							mRenderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());
						}
					}
					mChartView.repaint();

				}
			}
		});

	}

}
