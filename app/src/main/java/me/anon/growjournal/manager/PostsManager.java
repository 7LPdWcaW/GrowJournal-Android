package me.anon.growjournal.manager;

import java.util.ArrayList;

import me.anon.growjournal.model.Post;

/**
 * // TODO: Add class description
 */
public class PostsManager
{
	private static final PostsManager instance = new PostsManager();

	public static PostsManager getInstance()
	{
		return instance;
	}

	private ArrayList<Post> posts = new ArrayList<>();

	public void addPost(Post post)
	{
		posts.add(post);
	}

	public void deletePost(Post post)
	{
		posts.remove(post);
	}
}
