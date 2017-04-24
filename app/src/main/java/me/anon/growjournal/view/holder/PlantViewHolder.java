package me.anon.growjournal.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import me.anon.growjournal.MainApplication;
import me.anon.growjournal.R;
import me.anon.growjournal.model.tracker.Plant;

/**
 * // TODO: Add class description
 */
public class PlantViewHolder extends RecyclerView.ViewHolder
{
	private TextView name;
	private ImageView image;

	public PlantViewHolder(View itemView)
	{
		super(itemView);
		name = (TextView)itemView.findViewById(R.id.name);
		image = (ImageView)itemView.findViewById(R.id.image);
	}

	public void populate(final Plant model)
	{
		name.setText(model.getStrain() + " - " + model.getName());
		ImageLoader.getInstance().cancelDisplayTask(image);
		if (model.getImages() != null && model.getImages().size() > 0)
		{
			ImageAware imageAware = new ImageViewAware(image, true);
			ImageLoader.getInstance().displayImage("file://" + model.getImages().get(model.getImages().size() - 1), imageAware, MainApplication.getDisplayImageOptions());
		}
		else
		{
			image.setImageResource(R.drawable.default_plant);
		}
	}
}
