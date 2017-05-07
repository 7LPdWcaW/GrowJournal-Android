package me.anon.growjournal.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.data.DefaultHashMap;
import me.anon.growjournal.helper.JekyllUtils;
import me.anon.growjournal.manager.FileManager;

/**
 * // TODO: Add class description
 */
public class Page
{
	@Getter @Setter private String id = UUID.randomUUID().toString();
	@Getter @Setter private String title = "";
	@Getter @Setter private String body = "";
	@Getter @Setter private String data = "";
	@Getter @Setter private String layout = "page";
	@Getter @Setter private String permalink = "";

	@Nullable
	public static Page loadFrom(String filePath)
	{
		try
		{
			String pageBody = FileManager.getInstance().readFileAsString(filePath);

			Page page = new Page();

			int start = pageBody.indexOf("---", 0);
			int end = pageBody.indexOf("---", start + 3);

			DefaultHashMap configYaml = new DefaultHashMap();

			if (start >= 0 && end >= 0)
			{
				String configBlock = pageBody.substring(start, end);
				configYaml = new DefaultHashMap((Map)new Yaml().load(configBlock));
			}

			page.setId((String)configYaml.getString("id", UUID.randomUUID().toString()));
			page.setTitle((String)configYaml.getString("title", ""));
			page.setLayout((String)configYaml.getString("layout", "page"));
			page.setPermalink((String)configYaml.getString("permalink", ""));
			page.setData((String)configYaml.getString("data", ""));
			page.setBody(pageBody.substring(end > -1 ? (end + 3) : 0).trim());

			return page;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void saveTo(Page page, String filePath)
	{
		StringBuilder body = new StringBuilder("---");
		body.append("\r\nid: ").append(page.getId());
		body.append("\r\ntitle: ").append(page.getTitle());
		body.append("\r\npermalink: ").append(page.getPermalink());
		body.append("\r\nlayout: ").append(page.getLayout());
		body.append("\r\ntags: page");

		if (!TextUtils.isEmpty(page.getData()))
		{
			body.append("\r\ndata: ").append(page.getData());
		}

		body.append("\r\n---\r\n");
		body.append(page.getBody());
		String bodyStr = body.toString().trim();
		FileManager.getInstance().writeFile(filePath + "/" + JekyllUtils.urlCase(page.getTitle()) + ".md", bodyStr);
	}

	public static void deletePage(Page page, String folderPath)
	{
		FileManager.getInstance().deleteRecursive(new File(folderPath, JekyllUtils.urlCase(page.getTitle()) + ".md"));
	}
}
