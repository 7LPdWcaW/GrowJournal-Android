package me.anon.growjournal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import me.anon.growjournal.R;
import me.anon.growjournal.fragment.PostEditorFragment;
import me.anon.growjournal.helper.BundleHelper;
import me.anon.growjournal.model.Page;

/**
 * // TODO: Add class description
 */
public class ManagePageActivity extends AppCompatActivity
{
	private ViewPager viewPager;
	private Page page = null;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.manage_post_view);

		page = BundleHelper.getInstance().retrieve(getClass(), Page.class);

		viewPager = (ViewPager)findViewById(R.id.view_pager);
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override public Fragment getItem(int position)
			{
				switch (position)
				{
					case 0:
						return PostEditorFragment.newInstance(page);
				}

				return new Fragment();
			}

			@Override public int getCount()
			{
				return 1;
			}
		});
	}
}
