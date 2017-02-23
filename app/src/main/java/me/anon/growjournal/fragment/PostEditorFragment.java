package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.commonsware.cwac.richedit.Effect;
import com.commonsware.cwac.richedit.RichEditText;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.R;
import me.anon.growjournal.model.Post;

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

	private RichEditText editor;

	private ArrayList<Effect> effects = new ArrayList<>();
	private int effectStart = -1;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.post_editor_view, container, false);
		editor = (RichEditText)view.findViewById(R.id.editor);

		formatBold = (ImageButton)view.findViewById(R.id.bold);
		formatItalic = (ImageButton)view.findViewById(R.id.italic);
		formatUnderline = (ImageButton)view.findViewById(R.id.underline);
		formatLink = (ImageButton)view.findViewById(R.id.link);
		formatImage = (ImageButton)view.findViewById(R.id.image);
		formatAttach = (ImageButton)view.findViewById(R.id.attach);
		formatHeading = (ImageButton)view.findViewById(R.id.heading);
		formatBullet = (ImageButton)view.findViewById(R.id.bullet_list);
		formatNumber = (ImageButton)view.findViewById(R.id.number_list);

		formatBold.setOnClickListener(this);
		formatItalic.setOnClickListener(this);
		formatUnderline.setOnClickListener(this);
		formatLink.setOnClickListener(this);
		formatImage.setOnClickListener(this);
		formatAttach.setOnClickListener(this);
		formatHeading.setOnClickListener(this);
		formatBullet.setOnClickListener(this);
		formatNumber.setOnClickListener(this);

		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		editor.addTextChangedListener(new TextWatcher()
		{
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				applyStyles(start, count);
			}

			@Override public void afterTextChanged(Editable s)
			{

			}
		});
	}

	@Override public void onClick(View v)
	{
		if (v == formatBold)
		{
			if (effects.contains(RichEditText.BOLD))
			{
				effects.remove(RichEditText.BOLD);
			}
			else
			{
				effects.add(RichEditText.BOLD);
			}

			effectStart = editor.getSelectionStart();
		}

		if (effects.size() == 0)
		{
			effectStart = -1;
		}
	}

	private void applyStyles(int start, int count)
	{
		for (Effect effect : effects)
		{
			editor.setSelection(start, start + count);
			editor.applyEffect(effect, true);

			editor.setSelection(start + count);
		}
	}
}
