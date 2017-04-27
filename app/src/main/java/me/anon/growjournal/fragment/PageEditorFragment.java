package me.anon.growjournal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import lombok.Getter;
import lombok.Setter;
import me.anon.growjournal.R;
import me.anon.growjournal.manager.PageManager;
import me.anon.growjournal.model.Page;

import static me.anon.growjournal.R.id.done;
import static me.anon.growjournal.R.id.italic;
import static me.anon.growjournal.R.id.link;

/**
 * // TODO: Add class description
 */
public class PageEditorFragment extends Fragment implements View.OnClickListener
{
	public static PageEditorFragment newInstance(Page page)
	{
		PageEditorFragment fragment = new PageEditorFragment();
		fragment.setPage(page);

		return fragment;
	}

	@Getter @Setter private Page page;

	protected ImageButton formatBold;
	protected ImageButton formatItalic;
	protected ImageButton formatUnderline;
	protected ImageButton formatLink;
	protected ImageButton formatImage;
	protected ImageButton formatAttach;
	protected ImageButton formatHeading;
	protected ImageButton formatBullet;
	protected ImageButton formatNumber;
	protected Button heading1;
	protected Button heading2;
	protected Button heading3;
	protected Button heading4;

	protected ImageButton back;
	protected ImageButton save;

	protected View headingSizes;
	protected EditText title;
	protected EditText permalink;
	protected EditText editor;

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.page_editor_view, container, false);
		title = (EditText)view.findViewById(R.id.title);
		permalink = (EditText)view.findViewById(R.id.permalink);
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

		if (savedInstanceState == null)
		{
			populateUi();
		}
	}

	private void populateUi()
	{
		if (page != null)
		{
			title.setText(page.getTitle());
			permalink.setText(page.getPermalink());
			editor.setText(page.getBody());
			editor.requestFocus();
			editor.requestFocusFromTouch();
		}

		permalink.addTextChangedListener(new TextWatcher()
		{
			private int selection = -1;

			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				selection = permalink.getSelectionStart();

				permalink.removeTextChangedListener(this);
				String out = s.toString();

				if (!out.startsWith("/"))
				{
					out = "/" + out;
					selection++;
				}

				if (!out.endsWith("/"))
				{
					out += "/";
				}

				out = out.toLowerCase().replaceAll("[^0-9a-z/_]", "_");
				permalink.setText(out);
				permalink.addTextChangedListener(this);
			}

			@Override public void afterTextChanged(Editable s)
			{
				if (selection != -1)
				{
					permalink.setSelection(selection);
					selection = -1;
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
			if (save())
			{
				getActivity().finish();
			}
		}
		else if (v == formatNumber)
		{
			editor.getText().insert(editor.getSelectionStart(), "\n 1. ");
		}
	}

	protected boolean save()
	{
		if (TextUtils.isEmpty(title.getText()))
		{
			title.setError("Title required");
			return false;
		}

		if (TextUtils.isEmpty(permalink.getText()))
		{
			permalink.setError("Permalink is required");
			return false;
		}

		boolean newPage = page == null;
		if (newPage)
		{
			page = new Page();
			PageManager.getInstance().addPage(page);
		}

		page.setPermalink(permalink.getText().toString());
		page.setTitle(title.getText().toString());
		String body = editor.getText().toString();

		page.setBody(body);
		PageManager.getInstance().save(page);

		return true;
	}
}
