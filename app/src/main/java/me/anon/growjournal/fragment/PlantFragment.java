package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.R;
import me.anon.growjournal.adapter.ActionAdapter;
import me.anon.growjournal.model.tracker.Plant;

/**
 * // TODO: Add class description
 */
public class PlantFragment extends Fragment
{
	public static PlantFragment newInstance(Plant plant)
	{
		PlantFragment plantFragment = new PlantFragment();
		plantFragment.setPlant(plant);

		return plantFragment;
	}

	@Getter @Setter private Plant plant;

	private TextView name;
	private TextView strain;
	private TextView summary;
	private RecyclerView photos;
	private RecyclerView updates;

	private ActionAdapter updateAdapter;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.plant_page_view, container, false);

		name = (TextView)view.findViewById(R.id.name);
		strain = (TextView)view.findViewById(R.id.strain);
		summary = (TextView)view.findViewById(R.id.summary);
		photos = (RecyclerView)view.findViewById(R.id.photos_recycler);
		updates = (RecyclerView)view.findViewById(R.id.updates_recycler);

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
		name.setText(plant.getName());
		strain.setText(plant.getStrain());

		StringBuilder summaryStr = new StringBuilder();
		summaryStr.append("<b> • medium:</b> ").append(plant.getMedium().getPrintString());
		summaryStr.append("<br /><b> • medium details</b> ").append(plant.getMediumDetails());
		summaryStr.append("<br /><b> • planted:</b> ").append(plant.getPlantDate());

		summary.setText(Html.fromHtml(summaryStr.toString()));

		updateAdapter = new ActionAdapter();
		updateAdapter.setActions(plant, plant.getActions());
		updates.setLayoutManager(new LinearLayoutManager(getActivity()));
		updates.setAdapter(updateAdapter);
	}
}
