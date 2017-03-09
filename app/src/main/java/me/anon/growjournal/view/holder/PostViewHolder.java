package me.anon.growjournal.view.holder;

import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.anon.growjournal.R;
import me.anon.growjournal.activity.ManagePostActivity;
import me.anon.growjournal.helper.BundleHelper;
import me.anon.growjournal.manager.PostsManager;
import me.anon.growjournal.model.Post;

/**
 * // TODO: Add class description
 */
public class PostViewHolder extends RecyclerView.ViewHolder
{
	private TextView title;
	private TextView summary;
	private TextView date;
	private View publishStatus;

	public PostViewHolder(View itemView)
	{
		super(itemView);

		title = (TextView)itemView.findViewById(R.id.title);
		summary = (TextView)itemView.findViewById(R.id.summary);
		date = (TextView)itemView.findViewById(R.id.date);
		publishStatus = itemView.findViewById(R.id.publish_status);
	}

	public void populate(final Post model)
	{
		title.setText(model.getTitle());
		summary.setText(model.getBody());

		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(itemView.getContext());
		DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(itemView.getContext());

		Date updateDate = new Date(model.getUpdateDate());
		Calendar actionCalendar = GregorianCalendar.getInstance();
		actionCalendar.setTime(updateDate);
		date.setText(dateFormat.format(updateDate) + " " + timeFormat.format(updateDate));

		itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				Intent edit = new Intent(v.getContext(), ManagePostActivity.class);
				BundleHelper.getInstance().store(ManagePostActivity.class, model);
				v.getContext().startActivity(edit);
			}
		});

		itemView.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override public boolean onLongClick(final View v)
			{
				PopupMenu popup = new PopupMenu(v.getContext(), v);

				popup.getMenuInflater().inflate(R.menu.post_menu, popup.getMenu());
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
				{
					public boolean onMenuItemClick(MenuItem item)
					{
						switch (item.getItemId())
						{
							case R.id.edit:
								v.callOnClick();
								return true;

							case R.id.delete:
								PostsManager.getInstance().deletePost(model);
								return true;
						}

						return false;
					}
				});

				popup.show();

				return true;
			}
		});
	}
}
