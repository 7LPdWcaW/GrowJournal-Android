package me.anon.growjournal;

import android.app.Application;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import lombok.Getter;
import me.anon.growjournal.manager.GitManager;
import me.anon.growjournal.manager.PageManager;
import me.anon.growjournal.manager.PlantManager;
import me.anon.growjournal.manager.PostsManager;

/**
 * // TODO: Add class description
 */
public class MainApplication extends Application
{
	@Getter private static DisplayImageOptions displayImageOptions;

	@Override public void onCreate()
	{
		super.onCreate();

		if (PreferenceManager.getDefaultSharedPreferences(this).contains("setup"))
		{
			initialise();
		}

		displayImageOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.showImageOnLoading(R.drawable.ic_image)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this)
			.threadPoolSize(6)
			.build());
	}

	public void initialise()
	{
		GitManager.getInstance().initialise(this);

		PostsManager.folderPath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_posts/";
		PlantManager.filePath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_data/plants.json";
		PlantManager.imagesPath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/assets/";
		PlantManager.pagesPath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_pages/";
		PageManager.folderPath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_pages/";
		PlantManager.getInstance().load();
		PostsManager.getInstance().load();
		PageManager.getInstance().load();
	}
}
