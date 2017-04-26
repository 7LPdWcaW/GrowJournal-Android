package me.anon.growjournal.manager;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import lombok.Getter;
import me.anon.growjournal.helper.JekyllUtils;
import me.anon.growjournal.model.tracker.Plant;

/**
 * // TODO: Add class description
 */
public class PlantManager
{
	public static String filePath;
	public static String pagesPath;
	public static String imagesPath;
	@Getter private ArrayList<Plant> plants = new ArrayList<>();

	private static final PlantManager instance = new PlantManager();

	public static PlantManager getInstance()
	{
		return instance;
	}

	public void load()
	{
		try
		{
			plants = new Gson().fromJson(new FileReader(new File(filePath)), new TypeToken<ArrayList<Plant>>(){}.getType());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (plants == null)
		{
			plants = new ArrayList<>();
		}
	}

	public void writeImage(String imagePath)
	{
		String[] folders = imagePath.split("/");
		String filePath = folders[folders.length - 2] + "/" + folders[folders.length - 1];
		String thumbPath = folders[folders.length - 2] + "/thumb/" + folders[folders.length - 1];

		new File(imagesPath, folders[folders.length - 2] + "/").mkdirs();
		new File(imagesPath, folders[folders.length - 2] + "/thumb/").mkdirs();

		try
		{
			// resize and compress image
			Bitmap highRes = BitmapUtils.getBitmap(imagePath, 1200000);

			if (highRes != null)
			{
				highRes.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(imagesPath + filePath));
				highRes.recycle();
			}

			Bitmap thumb = BitmapUtils.getBitmap(imagePath, 60000);

			if (thumb != null)
			{
				thumb.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(imagesPath + thumbPath));
				thumb.recycle();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void deleteImage(String imagePath)
	{
		String[] folders = imagePath.split("/");
		String filePath = folders[folders.length - 2] + "/" + folders[folders.length - 1];
		String thumbPath = folders[folders.length - 2] + "/thumb/" + folders[folders.length - 1];

		new File(imagesPath, filePath).delete();
		new File(imagesPath, thumbPath).delete();
	}

	public void regeneratePages()
	{
		try
		{
			JsonArray plants = (JsonArray)new JsonParser().parse(new FileReader(filePath));

			if (plants != null)
			{
				for (JsonElement plantItem : plants)
				{
					JsonObject plant = plantItem.getAsJsonObject();

					StringBuilder plantPage = new StringBuilder("---\r\n");
					plantPage.append("layout: plant\r\n");
					plantPage.append("title: ").append(plant.get("name").getAsString()).append("\r\n");
					plantPage.append("permalink: \"/plants/").append(JekyllUtils.urlCase(plant.get("name").getAsString())).append("/\"\r\n");
					plantPage.append("data: ").append(plant.toString()).append("\"\r\n---\r\n");

					FileManager.getInstance().writeFile(pagesPath + JekyllUtils.urlCase(plant.get("name").getAsString()) + ".md", plantPage.toString());
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
