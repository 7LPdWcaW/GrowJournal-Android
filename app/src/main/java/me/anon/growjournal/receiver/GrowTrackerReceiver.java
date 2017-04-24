package me.anon.growjournal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

import me.anon.growjournal.manager.FileManager;
import me.anon.growjournal.manager.GitManager;
import me.anon.growjournal.manager.PlantManager;

/**
 * // TODO: Add class description
 */
public class GrowTrackerReceiver extends BroadcastReceiver
{
	@Override public void onReceive(Context context, Intent intent)
	{
		if (intent.getExtras().containsKey("me.anon.grow.PLANT_LIST"))
		{
			String plantData = intent.getExtras().getString("me.anon.grow.PLANT_LIST", "");

			new File(GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_data/").mkdirs();
			FileManager.getInstance().writeFile(GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_data/plants.json", plantData);
			PlantManager.getInstance().regeneratePages();
			GitManager.getInstance().commitChanges();
		}
	}
}
