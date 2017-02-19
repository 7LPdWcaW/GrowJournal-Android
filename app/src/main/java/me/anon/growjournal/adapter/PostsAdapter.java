package me.anon.growjournal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import lombok.Getter;
import me.anon.growjournal.R;
import me.anon.growjournal.model.Post;
import me.anon.growjournal.view.holder.PostViewHolder;

/**
 * // TODO: Add class description
 */
public class PostsAdapter extends RecyclerView.Adapter<PostViewHolder>
{
	@Getter private ArrayList<Post> items = new ArrayList<>();

	public void setItems(ArrayList<Post> items)
	{
		this.items.clear();
		this.items.addAll(items);
	}

	@Override public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
	}

	@Override public void onBindViewHolder(PostViewHolder holder, int position)
	{
		holder.populate(items.get(getItemCount() - position));
	}

	@Override public int getItemCount()
	{
		return items.size();
	}
}
