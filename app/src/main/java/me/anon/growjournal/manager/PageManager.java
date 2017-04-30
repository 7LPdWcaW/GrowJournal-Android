package me.anon.growjournal.manager;

import java.io.File;
import java.util.ArrayList;

import lombok.Getter;
import me.anon.growjournal.model.Page;

/**
 * // TODO: Add class description
 */
public class PageManager
{
	public static String folderPath;

	private static final PageManager instance = new PageManager();

	public static PageManager getInstance()
	{
		return instance;
	}

	@Getter private ArrayList<Page> pages = new ArrayList<>();

	public void addPage(Page page)
	{
		pages.add(page);
	}

	public void deletePage(Page page)
	{
		pages.remove(page);
	}

	public void load()
	{
		File folder = new File(folderPath);
		for (String s : folder.list())
		{
			// plants.md shouldn't be editable
			if (s.endsWith("plants.md"))
			{
				continue;
			}

			Page page = Page.loadFrom(folder.getAbsolutePath() + "/" + s);

			if (page != null)
			{
				pages.add(page);
			}
		}
	}

	public void save(Page page)
	{
		Page.saveTo(page, folderPath);
		GitManager.getInstance().commitChanges();
	}

	public void saveAll()
	{
		for (Page page : pages)
		{
			Page.saveTo(page, folderPath);
		}

		GitManager.getInstance().commitChanges();
	}
}
