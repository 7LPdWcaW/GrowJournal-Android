package me.anon.growjournal.view.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import lombok.Data;
import me.anon.growjournal.R;

/**
 * // TODO: Add class description
 */
@Data
public class ActionHolder extends RecyclerView.ViewHolder
{
	private CardView card;
	private TextView name;
	private TextView summary;

	public ActionHolder(View itemView)
	{
		super(itemView);

		card = (CardView)itemView.findViewById(R.id.card);
		name = (TextView)itemView.findViewById(R.id.name);
		summary = (TextView)itemView.findViewById(R.id.summary);
	}
}
