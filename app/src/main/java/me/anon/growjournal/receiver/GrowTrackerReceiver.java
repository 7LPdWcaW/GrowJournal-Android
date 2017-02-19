package me.anon.growjournal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.anon.growjournal.manager.FileManager;

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
			FileManager.getInstance().writeFile(context.getFilesDir().getPath() + "plants.json", plantData);
		}
	}
}
