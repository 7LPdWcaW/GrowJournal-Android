package me.anon.growjournal;

import android.app.Application;

import me.anon.growjournal.manager.GitManager;
import me.anon.growjournal.manager.PlantManager;
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
		PlantManager.filePath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_data/plants.json";
		PlantManager.pagesPath = GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_pages/";
		PlantManager.getInstance().load();
		PostsManager.getInstance().load();
	}
}
