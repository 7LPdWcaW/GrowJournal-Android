package me.anon.growjournal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

import me.anon.growjournal.event.InvalidatePlantEvent;
import me.anon.growjournal.helper.BusHelper;
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
		boolean isEncrypted = intent.getExtras().getBoolean("me.anon.grow.ENCRYPTED", false);
		if (isEncrypted) return;

		if (intent.getExtras().containsKey("me.anon.grow.PLANT_LIST"))
		{
			String plantData = intent.getExtras().getString("me.anon.grow.PLANT_LIST", "");

			new File(GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_data/").mkdirs();
			FileManager.getInstance().writeFile(GitManager.getInstance().getLocalRepo().getAbsolutePath() + "/_data/plants.json", plantData);

			PlantManager.getInstance().load();
			PlantManager.getInstance().regeneratePages();

			GitManager.getInstance().commitChanges();
			BusHelper.getInstance().post(new InvalidatePlantEvent());
		}
		else if (intent.getExtras().containsKey("me.anon.grow.IMAGE_ADDED"))
		{
			String imagePath = intent.getExtras().getString("me.anon.grow.IMAGE_ADDED", "");
			PlantManager.getInstance().writeImage(imagePath);

			GitManager.getInstance().commitChanges();
			BusHelper.getInstance().post(new InvalidatePlantEvent());
		}
		else if (intent.getExtras().containsKey("me.anon.grow.IMAGE_DELETED"))
		{
			String imagePath = intent.getExtras().getString("me.anon.grow.IMAGE_ADDED", "");
			PlantManager.getInstance().deleteImage(imagePath);

			GitManager.getInstance().commitChanges();
			BusHelper.getInstance().post(new InvalidatePlantEvent());
		}
	}
}
