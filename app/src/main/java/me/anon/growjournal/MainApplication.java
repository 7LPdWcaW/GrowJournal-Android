package me.anon.growjournal;

import android.app.Application;

import java.io.File;

import me.anon.growjournal.manager.PostsManager;

/**
 * // TODO: Add class description
 */
public class MainApplication extends Application
{
	@Override public void onCreate()
	{
		super.onCreate();

		PostsManager.folderPath = getFilesDir().getAbsolutePath() + "/posts/";

		if (!new File(PostsManager.folderPath).exists())
		{
			new File(PostsManager.folderPath).mkdirs();
		}

		PostsManager.getInstance().load();
	}
}
