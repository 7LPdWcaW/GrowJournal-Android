package me.anon.growjournal.manager;

import java.io.File;
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

	public void load()
	{
		File folder = new File(folderPath);
		for (String s : folder.list())
		{
			Post post = Post.loadFrom(folder.getAbsolutePath() + "/" + s);

			if (post != null)
			{
				posts.add(post);
			}
		}
	}

	public void save(Post post)
	{
		Post.saveTo(post, folderPath);
		GitManager.getInstance().commitChanges();
	}

	public void saveAll()
	{
		for (Post post : posts)
		{
			Post.saveTo(post, folderPath);
		}

		GitManager.getInstance().commitChanges();
	}
}
