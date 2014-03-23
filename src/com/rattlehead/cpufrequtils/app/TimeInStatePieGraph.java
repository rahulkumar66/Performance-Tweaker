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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
import com.rattlehead.cpufrequtils.app.utils.TimeInStateReader;
import com.rattlehead.cpufrequtils.app.utils.TimeUtils;

public class TimeInStatePieGraph extends SherlockFragmentActivity {
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("sd",""+Toast.LENGTH_SHORT);
		TimeInStateReader reader = new TimeInStateReader();
		_states = reader.getCpuStateTime(true);

		setContentView(R.layout.piechart);
		mRenderer.setLabelsTextSize((float) 21.00);
		mRenderer.setDisplayValues(true);
		mRenderer.setInScroll(true);
		mRenderer.setClickEnabled(true);
		mRenderer.setShowLegend(false);
		mRenderer.setPanEnabled(false);
		mRenderer.setStartAngle(0);
		mRenderer.setDisplayValues(false);

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

		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		mChartView = ChartFactory.getPieChartView(getBaseContext(), mSeries,
				mRenderer);
		layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		mChartView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SeriesSelection seriesSelection = mChartView
						.getCurrentSeriesAndPoint();
				if (mChartView != null) {
					if (seriesSelection != null) {
						int index = seriesSelection.getPointIndex();
						if (_states.get(index).getFrequency() == 0) {
							TimeUtils timeUtils = new TimeUtils();
							long timeInSeconds = _states.get(index).getTime();
							timeUtils.calculateTime(timeInSeconds);
							Toast.makeText(
									getBaseContext(),
									"Time Spent in " + "Deep Sleep : "
											+ timeUtils.getHours() + "h "
											+ timeUtils.getminutes() + "m "
											+ timeUtils.getSeconds() + "s ",
									Toast.LENGTH_SHORT).show();
						} else {
							TimeUtils timeUtils = new TimeUtils();
							long timeInSeconds = _states.get(index).getTime();
							timeUtils.calculateTime(timeInSeconds);

							Toast.makeText(
									getBaseContext(),
									"Time Spent in " + timeInSeconds / 1000
											+ " Mhz:" + timeUtils.getHours()
											+ "h " + timeUtils.getminutes()
											+ "m " + timeUtils.getSeconds()
											+ "s ", Toast.LENGTH_SHORT).show();
						}
					}
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
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return false;
	}

}
