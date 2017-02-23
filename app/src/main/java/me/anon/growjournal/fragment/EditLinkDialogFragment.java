package me.anon.growjournal.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import lombok.Setter;
import me.anon.growjournal.R;

/**
 * // TODO: Add class description
 */
public class EditLinkDialogFragment extends DialogFragment
{
	public interface OnEditLinkListener
	{
		public void onLinkEdited(String text, String url);
	}

	private EditText titleEdit;
	private EditText urlEdit;
	private String url = "";

	@Setter private OnEditLinkListener onEditLinkListener;

	public EditLinkDialogFragment(String url)
	{
		this.url = url;
	}

	public EditLinkDialogFragment()
	{
	}

	@Override public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.popup_link_view, null, false);
		titleEdit = (EditText)view.findViewById(R.id.text);
		urlEdit = (EditText)view.findViewById(R.id.url);

		if (url != null)
		{
			titleEdit.setVisibility(View.GONE);
			urlEdit.setText(url);
		}

		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
			.setTitle("Link")
			.setView(view)
			.setPositiveButton("Ok", null)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					dialog.dismiss();
				}
			}).create();

		alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
		{
			@Override public void onShow(DialogInterface dialogInterface)
			{
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
				{
					@Override public void onClick(View view)
					{
						if (onEditLinkListener != null)
						{
							onEditLinkListener.onLinkEdited(titleEdit.getText().toString(), urlEdit.getText().toString());
						}

						alertDialog.dismiss();
					}
				});
			}
		});

		return alertDialog;
	}
}
