package me.anon.growjournal.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

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

	@Override protected void populateUi()
	{
		super.populateUi();

		if (getPage() != null)
		{
			permalink.setText(Post.generateFilename((Post)getPage()));
			permalink.setEnabled(false);

			title.addTextChangedListener(new TextWatcher()
			{
				@Override public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override public void onTextChanged(CharSequence s, int start, int before, int count)
				{

				}

				@Override public void afterTextChanged(Editable s)
				{
					Post tmp = new Post();
					tmp.setTitle(s.toString().trim());
					permalink.setText(Post.generateFilename(tmp));
				}
			});
		}
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
			Post.deletePost((Post)getPage(), PostsManager.folderPath);
		}

		getPage().setTitle(title.getText().toString());
		String body = editor.getText().toString();

		getPage().setBody(body);
		((Post)getPage()).setPublishStatus(Post.PublishStatus.DRAFT);

		PostsManager.getInstance().save((Post)getPage());

		return true;
	}
}
