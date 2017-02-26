package me.anon.growjournal.manager;

import java.util.ArrayList;

import lombok.Getter;
import me.anon.growjournal.model.Post;

/**
 * // TODO: Add class description
 */
public class PostsManager
{
	public static String folderPath;

	private static final PostsManager instance = new PostsManager();

	public static PostsManager getInstance()
	{
		return instance;
	}

	@Getter private ArrayList<Post> posts = new ArrayList<>();

	public void addPost(Post post)
	{
		posts.add(post);
	}

	public void deletePost(Post post)
	{
		posts.remove(post);
	}

	public void save()
	{
		for (Post post : posts)
		{
			String title = post.getTitle().toLowerCase();
			title = title.replaceAll("[^a-zA-Z0-9]+", "-");

			FileManager.getInstance().writeFile(folderPath + title + ".md", post.getBody());
		}
	}
}
