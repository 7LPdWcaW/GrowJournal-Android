package me.anon.growjournal.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.anon.growjournal.R;

/**
 * // TODO: Add class description
 */
public class SettingsActivity extends AppCompatActivity
{
	@Override protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actionbar_activity_view);
		setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
		setTitle("Settings");

		getFragmentManager().beginTransaction()
			.replace(R.id.fragment_holder, new SettingsFragment())
			.commit();
	}

	public static class SettingsFragment extends PreferenceFragment
	{
		@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);

			addPreferencesFromResource(R.xml.settings);
		}
	}
}
