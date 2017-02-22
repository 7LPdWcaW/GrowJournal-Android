package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonsware.cwac.richedit.RichEditText;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.R;
import me.anon.growjournal.model.Post;

/**
 * // TODO: Add class description
 */
public class PostEditorFragment extends Fragment
{
	public static PostEditorFragment newInstance(Post post)
	{
		PostEditorFragment fragment = new PostEditorFragment();
		fragment.setPost(post);

		return fragment;
	}

	@Getter @Setter private Post post;

	private RichEditText editor;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.post_editor_view, container, false);
		editor = (RichEditText)view.findViewById(R.id.editor);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
}
