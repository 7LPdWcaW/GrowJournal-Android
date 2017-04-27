package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import me.anon.growjournal.manager.PostsManager;
import me.anon.growjournal.model.Post;

/**
 * // TODO: Add class description
 */
public class PostEditorFragment extends PageEditorFragment
{
	public static PostEditorFragment newInstance(Post post)
	{
		PostEditorFragment fragment = new PostEditorFragment();
		fragment.setPage(post);

		return fragment;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		permalink.setVisibility(View.GONE);
	}

	@Override protected boolean save()
	{
		if (TextUtils.isEmpty(title.getText()))
		{
			title.setError("Title required");
			return false;
		}

		boolean newPost = getPage() == null;
		if (newPost)
		{
			setPage(new Post());
			PostsManager.getInstance().addPost((Post)getPage());
		}

		if (!TextUtils.isEmpty(getPage().getTitle()) && !getPage().getTitle().equalsIgnoreCase(title.getText().toString()))
		{
			Post.delete(getPage(), PostsManager.folderPath);
		}

		getPage().setTitle(title.getText().toString());
		String body = editor.getText().toString();

		getPage().setBody(body);
		((Post)getPage()).setPublishStatus(Post.PublishStatus.DRAFT);

		PostsManager.getInstance().save((Post)getPage());

		return true;
	}
}
