package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.anon.growjournal.R;
import me.anon.growjournal.adapter.PostsAdapter;
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
	private PostsAdapter adapter;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.posts_view, container, false);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null)
		{
			adapter = new PostsAdapter();
			adapter.setItems(PostsManager.getInstance().getPosts());

			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setAdapter(adapter);
		}
	}
}
