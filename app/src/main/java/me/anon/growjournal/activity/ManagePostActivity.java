package me.anon.growjournal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import me.anon.growjournal.R;
import me.anon.growjournal.fragment.PostEditorFragment;
import me.anon.growjournal.model.Post;

/**
 * // TODO: Add class description
 */
public class ManagePostActivity extends AppCompatActivity
{
	private ViewPager viewPager;
	private Post post = new Post();

	@Override protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.manage_post_view);

		viewPager = (ViewPager)findViewById(R.id.view_pager);
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override public Fragment getItem(int position)
			{
				switch (position)
				{
					case 0:
						return PostEditorFragment.newInstance(post);
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
