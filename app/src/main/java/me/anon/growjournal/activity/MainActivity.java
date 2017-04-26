package me.anon.growjournal.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import me.anon.growjournal.R;
import me.anon.growjournal.fragment.PlantsFragment;
import me.anon.growjournal.fragment.PostsFragment;
import me.anon.growjournal.manager.GitManager;
import me.anon.growjournal.manager.PlantManager;
import me.anon.growjournal.view.PagerSlidingTabStrip;

/**
 * // TODO: Add class description
 */
public class MainActivity extends AppCompatActivity
{
	@Override protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_view);
		setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
		setTitle("Grow Journal");

		if (savedInstanceState == null)
		{
			setupPages();
		}
	}

	private void setupPages()
	{
		ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override public Fragment getItem(int position)
			{
				switch (position)
				{
					case 0:
						return PostsFragment.newInstance();
					case 1:
						return PlantsFragment.newInstance();
				}

				return new Fragment();
			}

			@Override public CharSequence getPageTitle(int position)
			{
				switch (position)
				{
					case 0:
						return "Posts";

					case 1:
						return "Plants";
				}

				return "";
			}

			@Override public int getCount()
			{
				return isGrowTrackerInstalled() || PlantManager.getInstance().getPlants().size() > 0 ? 2 : 1;
			}
		});

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
		tabs.setViewPager(pager);

		if (pager.getAdapter().getCount() == 1)
		{
			tabs.setVisibility(View.GONE);
		}
		else
		{
			getSupportActionBar().setElevation(0f);
		}
	}

	private boolean isGrowTrackerInstalled()
	{
		boolean installed = false;
		try
		{
			getPackageManager().getPackageInfo("me.anon.grow", 0);
			installed = true;
		}
		catch (PackageManager.NameNotFoundException e){}

		try
		{
			getPackageManager().getPackageInfo("me.anon.grow.debug", 0);
			installed = true;
		}
		catch (PackageManager.NameNotFoundException e){}

		return installed;
	}

	@Override public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.settings)
		{
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		else if (item.getItemId() == R.id.publish)
		{
			Toast.makeText(MainActivity.this, "Pushing changes", Toast.LENGTH_SHORT).show();
			GitManager.getInstance().pushChanges(new Runnable()
			{
				@Override public void run()
				{
					if (Looper.getMainLooper().getThread() != Thread.currentThread())
					{
						runOnUiThread(this);
						return;
					}

					Toast.makeText(MainActivity.this, "All changes pushed", Toast.LENGTH_SHORT).show();
				}
			});

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
