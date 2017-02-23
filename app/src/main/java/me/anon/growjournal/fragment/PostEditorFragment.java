package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.commonsware.cwac.richedit.Effect;
import com.commonsware.cwac.richedit.RichEditText;

import java.util.List;

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

		editor.setOnSelectionChangedListener(new RichEditText.OnSelectionChangedListener()
		{
			@Override public void onSelectionChanged(int start, int end, List<Effect<?>> effects)
			{
				toggleButtonsOff();

				if (effects.size() > 0)
				{
					for (Effect<?> effect : effects)
					{
						if (effect == RichEditText.BOLD)
						{
							formatBold.setSelected(true);
						}
						else if (effect == RichEditText.ITALIC)
						{
							formatItalic.setSelected(true);
						}
						else if (effect == RichEditText.UNDERLINE)
						{
							formatUnderline.setSelected(true);
						}
						else if (effect == RichEditText.URL)
						{
							formatLink.setSelected(true);
						}
						else if (effect == RichEditText.BULLET)
						{
							formatBullet.setSelected(true);
						}
					}
				}
			}
		});
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

		if (v == formatBold)
		{
			editor.toggleEffect(RichEditText.BOLD);
		}
		else if (v == formatItalic)
		{
			editor.toggleEffect(RichEditText.ITALIC);
		}
		else if (v == formatUnderline)
		{
			editor.toggleEffect(RichEditText.UNDERLINE);
		}
		else if (v == formatLink)
		{
			boolean isLink = editor.hasEffect(RichEditText.URL);
			String url = editor.getEffectValue(RichEditText.URL);

			EditLinkDialogFragment dialogFragment = new EditLinkDialogFragment(url);
			dialogFragment.setOnEditLinkListener(new EditLinkDialogFragment.OnEditLinkListener()
			{
				@Override public void onLinkEdited(String text, String url)
				{
					if (!TextUtils.isEmpty(text))
					{
						editor.getText().insert(editor.getSelectionStart(), text);
						editor.setSelection(editor.getSelectionStart(), text.length());
					}

					editor.applyEffect(RichEditText.URL, url);
				}
			});
			dialogFragment.show(getChildFragmentManager(), null);
		}
		else if (v == formatBullet)
		{
			editor.toggleEffect(RichEditText.BULLET);
		}
	}
}
