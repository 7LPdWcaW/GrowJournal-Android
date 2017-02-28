package me.anon.growjournal;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.Html;

import com.commonsware.cwac.richedit.RichEditText;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.anon.growjournal.helper.SpannableHelper;

/**
 * // TODO: Add class description
 */
@RunWith(AndroidJUnit4.class)
public class RichEditTest extends TestCase
{
	private Context context;

	@Before
	public void setUp() throws Exception
	{
		context = InstrumentationRegistry.getContext();
	}

	@Test
	public void testBoldExport() throws Exception
	{
		RichEditText text = new RichEditText(context);

		text.getText().clear();
		text.getText().append("bold text");
		text.setSelection(0, text.getText().length());
		text.applyEffect(RichEditText.BOLD, true);

		String boldExport = SpannableHelper.toMarkdown(text.getText());

		Assert.assertEquals("**bold text**", boldExport);
	}

	@Test
	public void testItalicExport() throws Exception
	{
		RichEditText text = new RichEditText(context);

		text.getText().clear();
		text.getText().append("italic text");
		text.setSelection(0, text.getText().length());
		text.applyEffect(RichEditText.ITALIC, true);

		String italic = SpannableHelper.toMarkdown(text.getText());

		Assert.assertEquals("*italic text*", italic);
	}

	@Test
	public void testComplexExport() throws Exception
	{
		RichEditText text = new RichEditText(context);

		text.getText().clear();
		text.getText().append("italic text");
		text.setSelection(0, text.getText().length());
		text.applyEffect(RichEditText.ITALIC, true);

		text.setSelection(0, "italic".length());
		text.applyEffect(RichEditText.BOLD, true);

		String html = Html.toHtml(text.getText());
		String italic = SpannableHelper.toMarkdown(text.getText());

		Assert.assertEquals("***italic** text*", italic);
	}
}
