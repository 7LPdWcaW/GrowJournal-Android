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
import me.anon.growjournal.adapter.PlantsAdapter;

/**
 * // TODO: Add class description
 */
public class PlantsFragment extends Fragment
{
	public static PlantsFragment newInstance()
	{
		return new PlantsFragment();
	}

	private RecyclerView recyclerView;
	private PlantsAdapter adapter;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.plants_view, container, false);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null)
		{
			initialiseAdapter();
		}
	}

	private void initialiseAdapter()
	{
		adapter = new PlantsAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(adapter);
	}
}
