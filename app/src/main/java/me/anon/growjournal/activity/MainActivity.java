package me.anon.growjournal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.anon.growjournal.R;
import me.anon.growjournal.fragment.PostsFragment;
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

		ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override public Fragment getItem(int position)
			{
				switch (position)
				{
					case 0:
						return PostsFragment.newInstance();
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
				return 2;
			}
		});

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
		tabs.setViewPager(pager);
	}
}
