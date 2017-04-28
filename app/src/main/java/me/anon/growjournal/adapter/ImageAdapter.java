package me.anon.growjournal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import me.anon.growjournal.R;
import me.anon.growjournal.view.holder.ImageHolder;

/**
 * // TODO: Add class description
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageHolder>
{
	@Getter private List<String> images = new ArrayList<>();

	public void setImages(List<String> images)
	{
		this.images.clear();
		this.images.addAll(images);
		Collections.reverse(this.images);
	}

	@Override public ImageHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		return new ImageHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false));
	}

	@Override public void onBindViewHolder(final ImageHolder viewHolder, final int position)
	{
		final String imageUri = images.get(position);
		viewHolder.populate(imageUri);
	}

	@Override public int getItemCount()
	{
		return images.size();
	}
}
