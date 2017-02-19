package me.anon.growjournal.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * // TODO: Add class description
 */
public class Post
{
	@Getter @Setter private String id = UUID.randomUUID().toString();
	@Getter @Setter private String title;
	@Getter @Setter private String body;
	@Getter @Setter private long publishDate;
	private PublishStatus publishStatus;

	public static enum PublishStatus
	{
		PUBLISHED,
		DRAFT;
	}
}
