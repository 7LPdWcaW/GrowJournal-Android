package me.anon.growjournal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.anon.growjournal.R;
import me.anon.growjournal.activity.ManagePageActivity;
import me.anon.growjournal.activity.ManagePostActivity;
import me.anon.growjournal.adapter.PagesAdapter;
import me.anon.growjournal.event.InvalidatePostEvent;
import me.anon.growjournal.helper.BusHelper;
import me.anon.growjournal.manager.PageManager;
import me.anon.growjournal.manager.PostsManager;
import me.anon.growjournal.model.Page;

/**
 * // TODO: Add class description
 */
public class PagesFragment extends Fragment
{
	public static PagesFragment newInstance()
	{
		return new PagesFragment();
	}

	private RecyclerView recyclerView;
	private View empty;
	private PagesAdapter adapter;
	private FloatingActionMenu actionMenu;
	private FloatingActionButton addPost;
	private FloatingActionButton addPage;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.pages_view, container, false);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
		empty = view.findViewById(R.id.empty);
		actionMenu = (FloatingActionMenu)view.findViewById(R.id.menu);
		addPost = (FloatingActionButton)view.findViewById(R.id.new_post);
		addPage = (FloatingActionButton)view.findViewById(R.id.new_page);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		BusHelper.getInstance().register(this);

		if (savedInstanceState == null)
		{
			adapter = new PagesAdapter();

			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setAdapter(adapter);

			resetAdapter();
		}

		addPost.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				actionMenu.close(true);
				Intent newPost = new Intent(v.getContext(), ManagePostActivity.class);
				startActivity(newPost);
			}
		});

		addPage.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				actionMenu.close(true);
				Intent newPage = new Intent(v.getContext(), ManagePageActivity.class);
				startActivity(newPage);
			}
		});
	}

	@Override public void onResume()
	{
		super.onResume();
		resetAdapter();
	}

	@Override public void onDestroy()
	{
		super.onDestroy();
		BusHelper.getInstance().unregister(this);
	}

	public void resetAdapter()
	{
		if (adapter != null)
		{
			adapter.clearItems();
			adapter.addItem("Posts");
			adapter.addItems(PostsManager.getInstance().getPosts());

			ArrayList<Page> pages = new ArrayList<>(PageManager.getInstance().getPages());
			Collections.sort(pages, new Comparator<Page>()
			{
				@Override public int compare(Page o1, Page o2)
				{
					return o1.getPermalink().compareTo(o2.getPermalink());
				}
			});

			adapter.addItem("Pages");
			adapter.addItems(pages);
			adapter.notifyDataSetChanged();

			checkEmpty();
		}
	}

	private void checkEmpty()
	{
		if (adapter.getItemCount() == 2)
		{
			recyclerView.setVisibility(View.GONE);
			empty.setVisibility(View.VISIBLE);
		}
		else
		{
			recyclerView.setVisibility(View.VISIBLE);
			empty.setVisibility(View.GONE);
		}
	}

	@Subscribe public void onPostInvalidated(InvalidatePostEvent event)
	{
		resetAdapter();
	}
}
