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

import java.util.LinkedHashMap;

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

	private LinkedHashMap<Effect, Integer[]> effects = new LinkedHashMap<>();

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
			int beforeLength = 0;
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				beforeLength = s.length();
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				int diff = s.length() - beforeLength;
				if (diff < 0) return;

				applyStyles(diff);
			}

			@Override public void afterTextChanged(Editable s)
			{

			}
		});
	}

	@Override public void onClick(View v)
	{
		v.setSelected(!v.isSelected());

		if (v == formatBold)
		{
			if (effects.keySet().contains(RichEditText.BOLD))
			{
				effects.remove(RichEditText.BOLD);
			}
			else
			{
				effects.put(RichEditText.BOLD, new Integer[]{editor.getSelectionStart(), editor.length()});
			}
		}
	}

	private void applyStyles(int count)
	{
		if (count <= 0) return;

		for (Effect effect : effects.keySet())
		{
			int start = effects.get(effect)[0];
			int originalLength = effects.get(effect)[1];
			int diff = editor.length() - originalLength;

			editor.setSelection(start, start + diff);
			editor.applyEffect(effect, true);

			editor.setSelection(start + diff);
		}
	}
}
