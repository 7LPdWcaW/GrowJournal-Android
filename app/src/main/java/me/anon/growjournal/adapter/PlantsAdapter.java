package me.anon.growjournal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.anon.growjournal.R;
import me.anon.growjournal.manager.PlantManager;
import me.anon.growjournal.view.holder.PlantViewHolder;

/**
 * // TODO: Add class description
 */
public class PlantsAdapter extends RecyclerView.Adapter<PlantViewHolder>
{
	@Override public PlantViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new PlantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false));
	}

	@Override public void onBindViewHolder(PlantViewHolder holder, int position)
	{
		holder.populate(PlantManager.getInstance().getPlants().get(position));
	}

	@Override public int getItemCount()
	{
		return PlantManager.getInstance().getPlants().size();
	}
}
