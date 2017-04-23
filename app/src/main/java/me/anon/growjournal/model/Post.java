package me.anon.growjournal.model;

import android.support.annotation.Nullable;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.manager.FileManager;

/**
 * // TODO: Add class description
 */
public class Post
{
	@Getter @Setter private String id = UUID.randomUUID().toString();
	@Getter @Setter private String title;
	@Getter @Setter private String body;
	@Getter @Setter private ArrayList<String> categories = new ArrayList<>();
	@Getter @Setter private long publishDate;
	@Getter @Setter private long updateDate;
	@Getter @Setter private PublishStatus publishStatus;

	public static enum PublishStatus
	{
		PUBLISHED,
		DRAFT;
	}

	@Nullable
	public static Post loadFrom(String filePath)
	{
		try
		{
			String postBody = FileManager.getInstance().readFileAsString(filePath);

			Post post = new Post();

			int start = postBody.indexOf("---", 0);
			int end = postBody.indexOf("---", start + 3);
			String configBlock = postBody.substring(start, end);
			Map configYaml = (Map)new Yaml().load(configBlock);

			post.setTitle((String)configYaml.get("title"));

			String[] categories = ((String)configYaml.get("categories")).split(" ");
			post.setCategories(new ArrayList<String>(Arrays.asList(categories)));

			post.setBody(postBody.substring(end + 3));
			post.setPublishDate(((Date)configYaml.get("date")).getTime());
			post.setUpdateDate(new File(filePath).lastModified());

			return post;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
