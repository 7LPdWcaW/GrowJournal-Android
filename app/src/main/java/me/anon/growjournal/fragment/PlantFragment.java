package me.anon.growjournal.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.text.DateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.R;
import me.anon.growjournal.adapter.ActionAdapter;
import me.anon.growjournal.adapter.ImageAdapter;
import me.anon.growjournal.helper.StatsHelper;
import me.anon.growjournal.model.tracker.Plant;
import me.anon.growjournal.model.tracker.PlantMedium;

/**
 * // TODO: Add class description
 */
public class PlantFragment extends Fragment implements View.OnClickListener
{
	public static PlantFragment newInstance(Plant plant)
	{
		PlantFragment plantFragment = new PlantFragment();
		plantFragment.setPlant(plant);

		return plantFragment;
	}

	@Getter @Setter private Plant plant;

	protected NestedScrollView scrollView;
	protected ImageButton back;

	private TextView name;
	private TextView strain;
	private TextView summary;
	private RecyclerView photos;
	private RecyclerView updates;

	private ActionAdapter updateAdapter;
	private ImageAdapter photosAdapter;

	private LineChart inputPh;
	private LineChart ppm;
	private LineChart temp;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.plant_page_view, container, false);

		scrollView = (NestedScrollView)view.findViewById(R.id.scroll_view);
		name = (TextView)view.findViewById(R.id.name);
		strain = (TextView)view.findViewById(R.id.strain);
		summary = (TextView)view.findViewById(R.id.summary);
		photos = (RecyclerView)view.findViewById(R.id.photos_recycler);
		updates = (RecyclerView)view.findViewById(R.id.updates_recycler);
		back = (ImageButton)view.findViewById(R.id.finish);

		inputPh = (LineChart)view.findViewById(R.id.input_ph);
		ppm = (LineChart)view.findViewById(R.id.ppm);
		temp = (LineChart)view.findViewById(R.id.temp);

		back.setOnClickListener(this);

		photos.setNestedScrollingEnabled(false);
		updates.setNestedScrollingEnabled(false);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null)
		{
			populateUi();
		}
	}

	private void populateUi()
	{
		scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
		{
			@Override public void onScrollChanged()
			{
				int scrollY = scrollView.getScrollY();

				name.setPivotX(0);
				name.setPivotY(0);
				name.setTextSize(Math.max(32, (float)(42f - (0.01 * scrollY))));

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
				{
					getView().findViewById(R.id.toolbar).setElevation((float)Math.min(6.0, 1.0 + (0.1 * scrollY)));
				}
			}
		});

		name.setText(plant.getName());
		strain.setText(plant.getStrain());

		StringBuilder summaryStr = new StringBuilder();
		summaryStr.append("<b> • medium:</b> ").append(plant.getMedium().getPrintString());
		summaryStr.append("<br /><b> • medium details</b> ").append(plant.getMediumDetails());

		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
		DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getActivity());

		Date actionDate = new Date(plant.getPlantDate());
		String fullDateStr = dateFormat.format(actionDate) + " " + timeFormat.format(actionDate);

		summaryStr.append("<br /><b> • planted:</b> ").append(fullDateStr);

		summary.setText(Html.fromHtml(summaryStr.toString()));

		updateAdapter = new ActionAdapter();
		updateAdapter.setActions(plant, plant.getActions());
		updates.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
		updates.setAdapter(updateAdapter);

		photosAdapter = new ImageAdapter();
		photosAdapter.setImages(plant.getImages());
		photos.setLayoutManager(new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, true));
		photos.setHasFixedSize(true);
		photos.setAdapter(photosAdapter);

		StatsHelper.setInputData(plant, inputPh, new String[3]);
		StatsHelper.setPpmData(plant, ppm, new String[3]);

		if (plant.getMedium() == PlantMedium.HYDRO)
		{
			StatsHelper.setTempData(plant, temp, new String[3]);
			temp.setVisibility(View.VISIBLE);
		}
	}

	@Override public void onClick(View v)
	{
		if (v == back)
		{
			getActivity().finish();
		}
	}
}
