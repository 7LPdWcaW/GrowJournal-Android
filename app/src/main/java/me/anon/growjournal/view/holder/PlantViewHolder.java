package me.anon.growjournal.view.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import me.anon.growjournal.MainApplication;
import me.anon.growjournal.R;
import me.anon.growjournal.activity.PlantPageActivity;
import me.anon.growjournal.helper.BundleHelper;
import me.anon.growjournal.manager.PlantManager;
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

			String[] folders = model.getImages().get(model.getImages().size() - 1).split("/");
			String thumbPath = folders[folders.length - 2] + "/thumb/" + folders[folders.length - 1];
			ImageLoader.getInstance().displayImage("file://" + PlantManager.imagesPath + thumbPath, imageAware, MainApplication.getDisplayImageOptions());
		}
		else
		{
			image.setImageResource(R.drawable.default_plant);
		}

		itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				Intent plantPage = new Intent(v.getContext(), PlantPageActivity.class);
				BundleHelper.getInstance().store(PlantPageActivity.class, model);
				v.getContext().startActivity(plantPage);
			}
		});
	}
}
