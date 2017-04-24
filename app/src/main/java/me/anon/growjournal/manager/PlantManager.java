package me.anon.growjournal.manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
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
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		if (plants == null)
		{
			plants = new ArrayList<>();
		}
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
