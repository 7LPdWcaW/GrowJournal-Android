package me.anon.growjournal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.anon.growjournal.R;
import me.anon.growjournal.fragment.PlantFragment;
import me.anon.growjournal.helper.BundleHelper;
import me.anon.growjournal.model.tracker.Plant;

/**
 * // TODO: Add class description
 */
public class PlantPageActivity extends AppCompatActivity
{
	private Plant plant;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view);
		setTitle("Plant");

		if (savedInstanceState == null)
		{
			plant = BundleHelper.getInstance().retrieve(getClass(), Plant.class);

			getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_holder, PlantFragment.newInstance(plant))
				.commit();
		}
	}
}
