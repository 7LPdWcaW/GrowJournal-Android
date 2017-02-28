package me.anon.growjournal.model;

import java.io.File;
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
	@Getter @Setter private long publishDate;
	@Getter @Setter private PublishStatus publishStatus;

	public static enum PublishStatus
	{
		PUBLISHED,
		DRAFT;
	}

	public static Post loadFrom(String filePath)
	{
		String postBody = FileManager.getInstance().readFileAsString(filePath);

		Post post = new Post();
		post.setBody(postBody);
		post.setTitle(new File(filePath).getName());
		post.setPublishDate(new File(filePath).lastModified());

		return post;
	}
}
