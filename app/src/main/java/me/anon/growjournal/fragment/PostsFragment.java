package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.anon.growjournal.R;

/**
 * // TODO: Add class description
 */
public class PostsFragment extends Fragment
{
	public static PostsFragment newInstance()
	{
		return new PostsFragment();
	}

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.posts_view, container, false);

		return view;
	}
}
