package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.commonsware.cwac.richtextutils.SpannedXhtmlGenerator;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.R;
import me.anon.growjournal.manager.PostsManager;
import me.anon.growjournal.model.Post;

import static me.anon.growjournal.R.id.done;
import static me.anon.growjournal.R.id.italic;
import static me.anon.growjournal.R.id.link;

/**
 * // TODO: Add class description
 */
public class PostEditorFragment extends Fragment implements View.OnClickListener
{
	public static PostEditorFragment newInstance(Post post)
	{
		PostEditorFragment fragment = new PostEditorFragment();
		fragment.setPost(post);

		return fragment;
	}

	@Getter @Setter private Post post;

	private ImageButton formatBold;
	private ImageButton formatItalic;
	private ImageButton formatUnderline;
	private ImageButton formatLink;
	private ImageButton formatImage;
	private ImageButton formatAttach;
	private ImageButton formatHeading;
	private ImageButton formatBullet;
	private ImageButton formatNumber;
	private Button heading1;
	private Button heading2;
	private Button heading3;
	private Button heading4;

	private ImageButton back;
	private ImageButton save;

	private View headingSizes;
	private EditText title;
	private EditText editor;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.post_editor_view, container, false);
		title = (EditText)view.findViewById(R.id.title);
		editor = (EditText)view.findViewById(R.id.editor);
		headingSizes = view.findViewById(R.id.heading_sizes);
		back = (ImageButton)view.findViewById(R.id.finish);
		save = (ImageButton)view.findViewById(done);

		formatBold = (ImageButton)view.findViewById(R.id.bold);
		formatItalic = (ImageButton)view.findViewById(italic);
		formatUnderline = (ImageButton)view.findViewById(R.id.underline);
		formatLink = (ImageButton)view.findViewById(link);
		formatImage = (ImageButton)view.findViewById(R.id.image);
		formatAttach = (ImageButton)view.findViewById(R.id.attach);
		formatHeading = (ImageButton)view.findViewById(R.id.heading);
		heading1 = (Button)view.findViewById(R.id.heading_1);
		heading2 = (Button)view.findViewById(R.id.heading_2);
		heading3 = (Button)view.findViewById(R.id.heading_3);
		heading4 = (Button)view.findViewById(R.id.heading_4);
		formatBullet = (ImageButton)view.findViewById(R.id.bullet_list);
		formatNumber = (ImageButton)view.findViewById(R.id.number_list);

		back.setOnClickListener(this);
		save.setOnClickListener(this);

		formatBold.setOnClickListener(this);
		formatItalic.setOnClickListener(this);
		formatUnderline.setOnClickListener(this);
		formatLink.setOnClickListener(this);
		formatImage.setOnClickListener(this);
		formatAttach.setOnClickListener(this);
		formatHeading.setOnClickListener(this);
		formatBullet.setOnClickListener(this);
		heading1.setOnClickListener(this);
		heading2.setOnClickListener(this);
		heading3.setOnClickListener(this);
		heading4.setOnClickListener(this);
		formatNumber.setOnClickListener(this);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	private void toggleButtonsOff()
	{
		formatBold.setSelected(false);
		formatItalic.setSelected(false);
		formatUnderline.setSelected(false);
		formatLink.setSelected(false);
		formatImage.setSelected(false);
		formatAttach.setSelected(false);
		formatHeading.setSelected(false);
		formatBullet.setSelected(false);
		formatNumber.setSelected(false);
	}

	@Override public void onClick(View v)
	{
		boolean multiSelected = editor.getSelectionEnd() > editor.getSelectionStart();

		if (v == formatImage)
		{

		}
		else if (v == formatBold)
		{
			editor.getText().insert(editor.getSelectionStart(), "**");
			editor.getText().insert(Math.min(editor.getSelectionEnd() + 2, editor.getText().length()), "**");
			editor.setSelection(editor.getSelectionStart() - 2);
		}
		else if (v == formatItalic)
		{
			editor.getText().insert(editor.getSelectionStart(), "*");
			editor.getText().insert(Math.min(editor.getSelectionEnd() + 1, editor.getText().length()), "*");
			editor.setSelection(editor.getSelectionStart() - 1);
		}
		else if (v == formatUnderline)
		{
			editor.getText().insert(editor.getSelectionStart(), "_");
			editor.getText().insert(Math.min(editor.getSelectionEnd() + 1, editor.getText().length()), "_");
			editor.setSelection(editor.getSelectionStart() - 1);
		}
		else if (v == formatLink)
		{
			String text = "";
			if (multiSelected)
			{
				text = editor.getText().subSequence(editor.getSelectionStart(), editor.getSelectionEnd()).toString();
			}

			EditLinkDialogFragment dialogFragment = new EditLinkDialogFragment(text);
			dialogFragment.setOnEditLinkListener(new EditLinkDialogFragment.OnEditLinkListener()
			{
				@Override public void onLinkEdited(String text, String url)
				{
					editor.getText().insert(editor.getSelectionStart(), "[" + text + "](" + url + ")");
				}
			});
			dialogFragment.show(getChildFragmentManager(), null);
		}
		else if (v == formatBullet)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n - ");
		}
		else if (v == formatHeading)
		{
			headingSizes.setVisibility(headingSizes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			formatHeading.setSelected(headingSizes.getVisibility() == View.VISIBLE);
		}
		else if (v == heading1)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n#");
		}
		else if (v == heading2)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n##");
		}
		else if (v == heading3)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n###");
		}
		else if (v == heading4)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n####");
		}
		else if (v == save)
		{
			savePost();
			getActivity().finish();
		}
		else if (v == formatNumber)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n 1. ");
		}
	}

	private void savePost()
	{
		if (TextUtils.isEmpty(title.getText()))
		{
			title.setError("Title required");
			return;
		}

		boolean newPost = post == null;
		if (newPost)
		{
			post = new Post();
			PostsManager.getInstance().addPost(post);
		}

		post.setTitle(title.getText().toString());
		String body = new SpannedXhtmlGenerator().toXhtml(editor.getText());

		post.setBody(body);
		post.setPublishStatus(Post.PublishStatus.DRAFT);

		PostsManager.getInstance().save();
	}
}
