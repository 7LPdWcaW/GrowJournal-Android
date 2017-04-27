package me.anon.growjournal.model;

import android.support.annotation.Nullable;

import org.eclipse.jgit.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
public class Post extends Page
{
	@Getter @Setter private ArrayList<String> categories = new ArrayList<>();
	@Getter @Setter private long publishDate = System.currentTimeMillis();
	@Getter @Setter private long updateDate = System.currentTimeMillis();
	@Getter @Setter private PublishStatus publishStatus = PublishStatus.DRAFT;

	public Post()
	{
		setLayout("page");
	}

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

			DefaultHashMap configYaml = new DefaultHashMap();

			if (start >= 0 && end >= 0)
			{
				String configBlock = postBody.substring(start, end);
				configYaml = new DefaultHashMap((Map)new Yaml().load(configBlock));
			}

			post.setId((String)configYaml.getString("id", UUID.randomUUID().toString()));
			post.setTitle((String)configYaml.getString("title", ""));
			post.setLayout((String)configYaml.getString("layout", "page"));

			String[] categories = ((String)configYaml.getString("categories", "")).split(" ");
			post.setCategories(new ArrayList<String>(Arrays.asList(categories)));

			post.setBody(postBody.substring(end > -1 ? (end + 3) : 0).trim());

			if (configYaml.containsKey("date"))
			{
				long date = new Date().getTime();

				if (configYaml.getString("date", null) == null)
				{
					date = new SimpleDateFormat("yyyy-MM-dd").parse(configYaml.getString("date", "")).getTime();
				}

				post.setPublishDate(date);
			}

			post.setUpdateDate(new File(filePath).lastModified());

			return post;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void saveTo(Post post, String filePath)
	{
		StringBuilder body = new StringBuilder("---");
		body.append("\r\nid: ").append(post.getId());
		body.append("\r\ntitle: ").append(post.getTitle());
		body.append("\r\nlayout: ").append(post.getLayout());
		body.append("\r\ndate: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(post.getPublishDate())));

		if (post.getCategories().size() > 0)
		{
			body.append("\r\ncategories: ").append(StringUtils.join(post.getCategories(), " "));
		}

		body.append("\r\n---\r\n");
		body.append(post.getBody());
		String bodyStr = body.toString().trim();
		FileManager.getInstance().writeFile(filePath + "/" + Post.generateFilename(post) + ".md", bodyStr);
	}

	public static void delete(Post post, String folderPath)
	{
		FileManager.getInstance().deleteRecursive(new File(folderPath, Post.generateFilename(post) + ".md"));
	}

	public static String generateFilename(Post post)
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(post.getPublishDate())) + "-" + JekyllUtils.urlCase(post.getTitle());
	}
}
