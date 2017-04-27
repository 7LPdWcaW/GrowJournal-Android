package me.anon.growjournal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.anon.growjournal.R;
import me.anon.growjournal.activity.ManagePostActivity;
import me.anon.growjournal.adapter.PagesAdapter;
import me.anon.growjournal.manager.PageManager;
import me.anon.growjournal.manager.PostsManager;

/**
 * // TODO: Add class description
 */
public class PostsFragment extends Fragment
{
	public static PostsFragment newInstance()
	{
		return new PostsFragment();
	}

	private RecyclerView recyclerView;
	private PagesAdapter adapter;
	private FloatingActionButton addPost;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.posts_view, container, false);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
		addPost = (FloatingActionButton)view.findViewById(R.id.new_post);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null)
		{
			adapter = new PagesAdapter();

			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setAdapter(adapter);
		}

		addPost.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				Intent newPost = new Intent(v.getContext(), ManagePostActivity.class);
				startActivity(newPost);
			}
		});
	}

	@Override public void onResume()
	{
		super.onResume();

		if (adapter != null)
		{
			adapter.clearItems();
			adapter.addItem("Posts");
			adapter.addItems(PostsManager.getInstance().getPosts());
			adapter.addItem("Pages");
			adapter.addItems(PageManager.getInstance().getPages());
			adapter.notifyDataSetChanged();
		}
	}
}
