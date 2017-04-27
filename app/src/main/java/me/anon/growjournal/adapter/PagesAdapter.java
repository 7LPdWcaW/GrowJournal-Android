package me.anon.growjournal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import lombok.Getter;
import me.anon.growjournal.R;
import me.anon.growjournal.model.Page;
import me.anon.growjournal.model.Post;
import me.anon.growjournal.view.holder.PageViewHolder;
import me.anon.growjournal.view.holder.PostViewHolder;

/**
 * // TODO: Add class description
 */
public class PagesAdapter extends RecyclerView.Adapter
{
	public static final int TYPE_POST = 1;
	public static final int TYPE_PAGE = 2;

	@Getter private ArrayList<? extends Page> items = new ArrayList<>();

	public void clearItems()
	{
		this.items.clear();
	}

	public void addItems(ArrayList items)
	{
		this.items.addAll(items);
	}

	@Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		switch (viewType)
		{
			default:
			case TYPE_PAGE:
				return new PageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false));

			case TYPE_POST:
				return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
		}
	}

	@Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		Page page = items.get(position);
		int type = getItemViewType(position);

		switch (type)
		{
			case TYPE_PAGE:
				((PageViewHolder)holder).populate(page);
				break;

			case TYPE_POST:
				((PostViewHolder)holder).populate((Post)page);
				break;
		}
	}

	@Override public int getItemCount()
	{
		return items.size();
	}

	@Override public int getItemViewType(int position)
	{
		if (getItems().get(position).getClass() == Post.class)
		{
			return TYPE_POST;
		}

		return TYPE_PAGE;
	}
}
