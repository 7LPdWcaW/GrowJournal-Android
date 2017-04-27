package me.anon.growjournal.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.anon.growjournal.R;

/**
 * // TODO: Add class description
 */
public class DividerViewHolder extends RecyclerView.ViewHolder
{
	private TextView divider;

	public DividerViewHolder(View itemView)
	{
		super(itemView);

		divider = (TextView)itemView.findViewById(R.id.divider);
	}

	public void populate(final String model)
	{
		divider.setText(model);
	}
}
