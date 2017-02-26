package me.anon.growjournal.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
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
		v.setSelected(!v.isSelected());

		if (v == formatImage)
		{
			SpannableString imageString = new SpannableString("abc");
			Drawable test = getResources().getDrawable(R.mipmap.ic_launcher);
			test.setBounds(0, 0, test.getIntrinsicWidth(), test.getIntrinsicHeight());
			ImageSpan image = new ImageSpan(test, DynamicDrawableSpan.ALIGN_BASELINE);
			imageString.setSpan(image, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			editor.getText().insert(editor.getSelectionStart(), imageString);
		}
		else if (v == formatBold)
		{

		}
		else if (v == formatItalic)
		{

		}
		else if (v == formatUnderline)
		{

		}
		else if (v == formatLink)
		{
			EditLinkDialogFragment dialogFragment = new EditLinkDialogFragment(null);
			dialogFragment.setOnEditLinkListener(new EditLinkDialogFragment.OnEditLinkListener()
			{
				@Override public void onLinkEdited(String text, String url)
				{

				}
			});
			dialogFragment.show(getChildFragmentManager(), null);
		}
		else if (v == formatBullet)
		{

		}
		else if (v == formatHeading)
		{
			headingSizes.setVisibility(headingSizes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			formatHeading.setSelected(headingSizes.getVisibility() == View.VISIBLE);
		}
		else if (v == heading1)
		{

		}
		else if (v == heading2)
		{

		}
		else if (v == heading3)
		{

		}
		else if (v == heading4)
		{

		}
		else if (v == save)
		{
			savePost();
			getActivity().finish();
		}
		else if (v == formatNumber)
		{

		}
	}

	private void savePost()
	{
		boolean newPost = post == null;
		if (newPost)
		{
			post = new Post();
		}

		post.setTitle(title.getText().toString());
		String body = new SpannedXhtmlGenerator().toXhtml(editor.getText());

		post.setBody(body);
		post.setPublishStatus(Post.PublishStatus.DRAFT);

		if (newPost)
		{
			PostsManager.getInstance().addPost(post);
		}

		PostsManager.getInstance().save();
	}
}
