package me.anon.growjournal.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Map;

import me.anon.growjournal.R;
import me.anon.growjournal.manager.GitManager;

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
		private Map siteSettings;
    	private SharedPreferences.OnSharedPreferenceChangeListener listener;

		@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);

			addPreferencesFromResource(R.xml.settings);

			loadSettings();
			populateSummaries();

			createListener();
		}

		@Override public void onDestroy()
		{
			super.onDestroy();

			saveSettings();
			GitManager.getInstance().commitChanges();
		}

		private void populateSummaries()
		{
			findPreference("site_title").setSummary((String)siteSettings.get("title"));
			findPreference("site_description").setSummary((String)siteSettings.get("description"));
			findPreference("site_base_url").setSummary((String)siteSettings.get("baseurl"));
			findPreference("site_url").setSummary((String)siteSettings.get("url"));

			if (getPreferenceManager().getSharedPreferences().getString("git_url", null) != null)
			{
				findPreference("git_url").setSummary(getPreferenceManager().getSharedPreferences().getString("git_url", ""));
			}

			findPreference("git_username").setSummary(getPreferenceManager().getSharedPreferences().getString("git_username", ""));
			findPreference("committer_name").setSummary(getPreferenceManager().getSharedPreferences().getString("commiter_name", ""));
			findPreference("committer_email").setSummary(getPreferenceManager().getSharedPreferences().getString("commiter_email", ""));
		}

		private void loadSettings()
		{
			try
			{
				Yaml yaml = new Yaml();
				siteSettings = (Map)yaml.load(new FileInputStream(new File(GitManager.getInstance().getLocalRepo(), "_config.yml" )));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Toast.makeText(getActivity(), "Failed to load settings, ensure your _config.yml is correct", Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}

		private void saveSettings()
		{
			try
			{
				Yaml yaml = new Yaml();
				yaml.dump(siteSettings, new FileWriter(new File(GitManager.getInstance().getLocalRepo(), "_config.yml" )));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Toast.makeText(getActivity(), "Failed to save settings, ensure your _config.yml is correct", Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}

		private void createListener()
		{
			listener = new SharedPreferences.OnSharedPreferenceChangeListener()
			{
				@Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
				{
					if (key.equals("site_title"))
					{
						siteSettings.put("title", sharedPreferences.getString("site_title", ""));
					}
					else if (key.equals("site_description"))
					{
						siteSettings.put("description", sharedPreferences.getString("description", ""));
					}
					else if (key.equals("site_base_url"))
					{
						siteSettings.put("baseurl", sharedPreferences.getString("site_base_url", ""));
					}
					else if (key.equals("site_url"))
					{
						siteSettings.put("url", sharedPreferences.getString("site_url", ""));
					}

					populateSummaries();
				}
			};

			PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(listener);
		}
	}
}
