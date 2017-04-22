package me.anon.growjournal;

import android.app.Application;

import me.anon.growjournal.manager.GitManager;
import me.anon.growjournal.manager.PostsManager;

/**
 * // TODO: Add class description
 */
public class MainApplication extends Application
{
	@Override public void onCreate()
	{
		super.onCreate();

		GitManager.getInstance().initialise(this);

		PostsManager.folderPath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_posts/";
		PostsManager.getInstance().load();
	}
}
