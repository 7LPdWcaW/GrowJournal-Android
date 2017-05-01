package me.anon.growjournal.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import me.anon.growjournal.R;
import me.anon.growjournal.adapter.PlantsAdapter;
import me.anon.growjournal.data.ProgressListener;
import me.anon.growjournal.event.InvalidatePlantEvent;
import me.anon.growjournal.helper.BusHelper;
import me.anon.growjournal.manager.GitManager;
import me.anon.growjournal.manager.PlantManager;
import me.anon.growjournal.receiver.GrowTrackerReceiver;

/**
 * // TODO: Add class description
 */
public class PlantsFragment extends Fragment
{
	public static PlantsFragment newInstance()
	{
		return new PlantsFragment();
	}

	private static final int REQUEST_IMPORT= 0x1;

	private View empty;
	private FloatingActionButton reimport;
	private RecyclerView recyclerView;
	private PlantsAdapter adapter;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.plants_view, container, false);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
		empty = view.findViewById(R.id.empty);
		reimport = (FloatingActionButton)view.findViewById(R.id.reimport);

		view.findViewById(R.id.request).setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				requestImport();
			}
		});

		reimport.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				requestImport();
			}
		});

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		BusHelper.getInstance().register(this);

		if (savedInstanceState == null)
		{
			initialiseAdapter();
		}
	}

	@Override public void onDestroy()
	{
		super.onDestroy();
		BusHelper.getInstance().unregister(this);
	}

	@Subscribe public void onPlantInvalidated(InvalidatePlantEvent event)
	{
		initialiseAdapter();
	}

	private void initialiseAdapter()
	{
		adapter = new PlantsAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(adapter);

		checkState();
	}

	private void checkState()
	{
		if (adapter.getItemCount() == 0)
		{
			empty.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
			reimport.setVisibility(View.GONE);
		}
		else
		{
			empty.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			reimport.setVisibility(View.VISIBLE);
		}
	}

	private void requestImport()
	{
		Intent request = new Intent("me.anon.grow.ACTION_REQUEST_PLANTS");
		request.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		startActivityForResult(Intent.createChooser(request, "Import from"), REQUEST_IMPORT);
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_IMPORT && resultCode == Activity.RESULT_OK
		&& data.getExtras() != null && data.getExtras().containsKey("me.anon.grow.PLANT_LIST"))
		{
			if (data.getExtras().getBoolean("me.anon.grow.ENCRYPTED", false))
			{
				Toast.makeText(getActivity(), "Unable to import encrypted data", Toast.LENGTH_SHORT).show();
				return;
			}

			new GrowTrackerReceiver().onReceive(getActivity(), data);
			PlantManager.getInstance().importImages(new ProgressListener()
			{
				private ProgressDialog progressDialog = null;

				@Override public void onProgressUpdated(final int progress)
				{
					if (progress == 100)
					{
						PlantManager.getInstance().regeneratePages();
						GitManager.getInstance().commitChanges();
					}

					getActivity().runOnUiThread(new Runnable()
					{
						@Override public void run()
						{
							if (getActivity() == null) return;

							if (progressDialog == null)
							{
								progressDialog = new ProgressDialog(getActivity());
								progressDialog.setMessage("Importing plant data");
								progressDialog.setProgress(0);
								progressDialog.setMax(100);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								progressDialog.show();
							}

							if (progressDialog != null)
							{
								progressDialog.setProgress(progress);

								if (progress == 100)
								{
									progressDialog.dismiss();
									Toast.makeText(getActivity(), "Import complete", Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
				}
			});

			adapter.notifyDataSetChanged();
			checkState();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
