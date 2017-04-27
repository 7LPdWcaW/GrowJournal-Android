package me.anon.growjournal.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.anon.growjournal.R;
import me.anon.growjournal.activity.ManagePageActivity;
import me.anon.growjournal.helper.BundleHelper;
import me.anon.growjournal.manager.PageManager;
import me.anon.growjournal.model.Page;

/**
 * // TODO: Add class description
 */
public class PageViewHolder extends RecyclerView.ViewHolder
{
	private TextView title;
	private TextView summary;

	// temp variables for touch location
	private transient int x, y;

	public PageViewHolder(View itemView)
	{
		super(itemView);

		title = (TextView)itemView.findViewById(R.id.title);
		summary = (TextView)itemView.findViewById(R.id.summary);
	}

	public void populate(final Page model)
	{
		title.setText(model.getTitle());
		summary.setText(model.getBody());

		itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				Intent edit = new Intent(v.getContext(), ManagePageActivity.class);
				BundleHelper.getInstance().store(ManagePageActivity.class, model);
				v.getContext().startActivity(edit);
			}
		});

		itemView.setOnTouchListener(new View.OnTouchListener()
		{
			@Override public boolean onTouch(View v, MotionEvent event)
			{
				final int action = event.getAction();
				switch (action & MotionEvent.ACTION_MASK)
				{
					case MotionEvent.ACTION_DOWN:
					{
						x = (int)event.getX();
						y = (int)event.getY();
						break;
					}
				}

				return false;
			}
		});

		itemView.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override public boolean onLongClick(final View v)
			{
				ViewGroup root = (ViewGroup)((Activity)v.getContext()).getWindow().getDecorView().findViewById(android.R.id.content);

				View view = new View(v.getContext());
				view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
				view.setBackgroundColor(Color.TRANSPARENT);

				root.addView(view);

				int[] viewPos = new int[2];
				v.getLocationInWindow(viewPos);

				view.setX(x);
				view.setY(y + viewPos[1]);

				PopupMenu popup = new PopupMenu(v.getContext(), view, Gravity.CENTER);

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
								PageManager.getInstance().deletePage(model);
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
