package me.anon.growjournal.helper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;

import me.anon.growjournal.model.tracker.Action;
import me.anon.growjournal.model.tracker.Plant;

/**
 * Helper class used for generating statistics for plant
 */
public class StatsHelper
{
	private static ValueFormatter formatter = new ValueFormatter()
	{
		@Override public String getFormattedValue(float value)
		{
			return String.format("%.2f", value);
		}
	};

	/**
	 * Generates and sets the input watering data from the given plant
	 *
	 * @param plant The plant
	 * @param chart The chart to set the data
	 * @param additionalRef Pass-by-reference value for min/max/ave for the generated values. Must be length of 3 if not null
	 */
	public static void setInputData(Plant plant, LineChart chart, String[] additionalRef)
	{
		ArrayList<Entry> inputVals = new ArrayList<>();
		ArrayList<Entry> runoffVals = new ArrayList<>();
		ArrayList<Entry> averageVals = new ArrayList<>();
		ArrayList<String> xVals = new ArrayList<>();
		LineData data = new LineData();
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		float totalIn = 0;
		float totalOut = 0;
		float ave = 0;

		int index = 0;
		for (Action action : plant.getActions())
		{
			if (action.getType().equalsIgnoreCase("water"))
			{
				if (action.getPh() != null)
				{
					inputVals.add(new Entry(action.getPh().floatValue(), index));
					min = Math.min(min, action.getPh().floatValue());
					max = Math.max(max, action.getPh().floatValue());

					totalIn += action.getPh().floatValue();
				}

				if (action.getRunoff() != null)
				{
					runoffVals.add(new Entry(action.getRunoff().floatValue(), index));
					min = Math.min(min, action.getRunoff().floatValue());
					max = Math.max(max, action.getRunoff().floatValue());

					totalOut += action.getRunoff().floatValue();
				}

				xVals.add("");

				float aveIn = totalIn;
				float aveOut = totalOut;
				if (index > 0)
				{
					aveIn /= (float)index;
					aveOut /= (float)index;
				}

				index++;
			}
		}

		if (chart != null)
		{
			LineDataSet dataSet = new LineDataSet(inputVals, "Input PH");
			dataSet.setDrawCubic(true);
			dataSet.setLineWidth(2.0f);
			dataSet.setDrawCircleHole(false);
			dataSet.setCircleColor(0x8f4bc0c0);
			dataSet.setColor(0x8f4bc0c0);
			dataSet.setDrawValues(false);
			dataSet.setCircleSize(2.0f);

			LineDataSet runoffDataSet = new LineDataSet(runoffVals, "Runoff PH");
			runoffDataSet.setDrawCubic(true);
			runoffDataSet.setLineWidth(2.0f);
			runoffDataSet.setDrawCircleHole(false);
			runoffDataSet.setColor(0x8fFF0c0c);
			runoffDataSet.setCircleColor(0x8fFF0c0c);
			runoffDataSet.setDrawValues(false);
			runoffDataSet.setCircleSize(2.0f);

			ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
			dataSets.add(dataSet);
			dataSets.add(runoffDataSet);

			LineData lineData = new LineData(xVals, dataSets);
			lineData.setValueFormatter(formatter);

			chart.setBackgroundColor(0xffffff);
			chart.setGridBackgroundColor(0xff8e7cc3);
			chart.setDrawGridBackground(false);
			chart.setHighlightEnabled(false);
			chart.getLegend().setEnabled(true);
			chart.getAxisLeft().setTextColor(0xff666666);
			chart.getXAxis().setDrawGridLines(false);
			chart.getAxisRight().setEnabled(false);
			chart.getAxisLeft().setXOffset(8.0f);
			chart.getAxisLeft().setValueFormatter(formatter);
			chart.getAxisLeft().setAxisMinValue(min - 0.5f);
			chart.getAxisLeft().setAxisMaxValue(max + 0.5f);
			chart.getAxisLeft().setStartAtZero(false);
			chart.setTouchEnabled(false);
			chart.setScaleYEnabled(false);
			chart.setDescription("");
			chart.setPinchZoom(false);
			chart.setDoubleTapToZoomEnabled(false);

			chart.setData(lineData);
		}

		if (additionalRef != null)
		{
			additionalRef[0] = String.valueOf(min);
			additionalRef[1] = String.valueOf(max);
			additionalRef[2] = String.format("%1$,.2f", (totalIn / (double)index));
		}
	}

	/**
	 * Generates and sets the ppm watering data from the given plant
	 *
	 * @param plant The plant
	 * @param chart The chart to set the data
	 * @param additionalRef Pass-by-reference value for min/max/ave for the generated values. Must be length of 3 if not null
	 */
	public static void setPpmData(Plant plant, LineChart chart, String[] additionalRef)
	{
		ArrayList<Entry> vals = new ArrayList<>();
		ArrayList<String> xVals = new ArrayList<>();
		LineData data = new LineData();

		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		long ave = 0;

		int index = 0;
		for (Action action : plant.getActions())
		{
			if (action.getType().equalsIgnoreCase("water") && action.getPpm() != null)
			{
				vals.add(new Entry(action.getPpm().floatValue(), index++));
				xVals.add("");

				min = Math.min(min, action.getPpm().longValue());
				max = Math.max(max, action.getPpm().longValue());
				ave += action.getPpm();
			}
		}

		if (chart != null)
		{
			LineDataSet dataSet = new LineDataSet(vals, "PPM");
			dataSet.setDrawCubic(true);
			dataSet.setLineWidth(2.0f);
			dataSet.setDrawCircleHole(false);
			dataSet.setCircleColor(0x8f8e7cc3);
			dataSet.setCircleSize(2.0f);
			dataSet.setValueTextSize(0f);
			dataSet.setColor(0x8f8e7cc3);

			ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
			dataSets.add(dataSet);

			LineData lineData = new LineData(xVals, dataSets);
			lineData.setValueFormatter(formatter);

			chart.setBackgroundColor(0xffffff);
			chart.setGridBackgroundColor(0xff8e7cc3);
			chart.setDrawGridBackground(false);
			chart.setHighlightEnabled(false);
			chart.getLegend().setEnabled(true);
			chart.getAxisLeft().setTextColor(0xff666666);
			chart.getXAxis().setDrawGridLines(false);
			chart.getAxisRight().setEnabled(false);
			chart.getAxisLeft().setXOffset(8.0f);
			chart.setScaleYEnabled(false);
			chart.setTouchEnabled(false);
			chart.setDescription("");
			chart.setPinchZoom(false);
			chart.setDoubleTapToZoomEnabled(false);
			chart.setData(lineData);
		}

		if (additionalRef != null)
		{
			additionalRef[0] = String.valueOf(min);
			additionalRef[1] = String.valueOf(max);
			additionalRef[2] = String.format("%1$,.2f", (ave / (double)index));
		}
	}

	/**
	 * Generates and sets the temperature data from the given plant
	 *
	 * @param plant The plant
	 * @param chart The chart to set the data
	 * @param additionalRef Pass-by-reference value for min/max/ave for the generated values. Must be length of 3 if not null
	 */
	public static void setTempData(Plant plant, LineChart chart, String[] additionalRef)
	{
		ArrayList<Entry> vals = new ArrayList<>();
		ArrayList<String> xVals = new ArrayList<>();
		LineData data = new LineData();
		float min = -100f;
		float max = 100f;
		float ave = 0;

		int index = 0;
		for (Action action : plant.getActions())
		{
			if (action.getType().equalsIgnoreCase("water") && action.getTemp() != null)
			{
				vals.add(new Entry(action.getTemp().floatValue(), index++));
				xVals.add("");

				min = Math.min(min, action.getTemp().floatValue());
				max = Math.max(max, action.getTemp().floatValue());
				ave += action.getTemp().floatValue();
			}
		}

		if (chart != null)
		{
			LineDataSet dataSet = new LineDataSet(vals, "Temperature");
			dataSet.setDrawCubic(true);
			dataSet.setLineWidth(2.0f);
			dataSet.setDrawCircleHole(false);
			dataSet.setCircleColor(0x8f7C8EC3);
			dataSet.setColor(0x8f7C8EC3);
			dataSet.setDrawValues(false);
			dataSet.setCircleSize(2.0f);

			ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
			dataSets.add(dataSet);

			LineData lineData = new LineData(xVals, dataSets);
			lineData.setValueFormatter(formatter);

			chart.setBackgroundColor(0xffffff);
			chart.setGridBackgroundColor(0xff8e7cc3);
			chart.setDrawGridBackground(false);
			chart.setHighlightEnabled(false);
			chart.setTouchEnabled(false);
			chart.getAxisLeft().setTextColor(0xff666666);
			chart.getXAxis().setDrawGridLines(false);
			chart.getAxisRight().setEnabled(false);
			chart.getAxisLeft().setValueFormatter(formatter);
			chart.getAxisLeft().setXOffset(8.0f);
			chart.getAxisLeft().setAxisMinValue(min - 5f);
			chart.getAxisLeft().setAxisMaxValue(max + 5f);
			chart.getAxisLeft().setStartAtZero(false);
			chart.setScaleYEnabled(false);
			chart.setDescription("");
			chart.setPinchZoom(false);
			chart.setDoubleTapToZoomEnabled(false);
			chart.setData(lineData);
		}

		if (additionalRef != null)
		{
			additionalRef[0] = String.valueOf(min);
			additionalRef[1] = String.valueOf(max);
			additionalRef[2] = String.format("%1$,.2f", (ave / (double)index));
		}
	}
}
