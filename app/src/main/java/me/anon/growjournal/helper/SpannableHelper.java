package me.anon.growjournal.helper;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

/**
 * // TODO: Add class description
 */
public class SpannableHelper
{
	/**
	 * Converts a given spannable to markdown
	 *
	 * @param span The input span
	 *
	 * @return The parsed markdown
	 */
	public static String toMarkdown(Spannable span)
	{
		StringBuilder out = new StringBuilder();
		withinMarkdown(out, span, 0, span.length());

		return out.toString();
	}

//	private static void withinMarkdown(StringBuilder out, Spanned text, int start, int end)
//	{
//		int next;
//		for (int i = start; i < end; i = next)
//		{
//			next = text.nextSpanTransition(i, end, QuoteSpan.class);
//			withinBlockquoteConsecutive(out, text, i, next);
//		}
//	}

	private static void withinMarkdown(StringBuilder out, Spanned text, int start, int end)
	{
		int next;
		for (int i = start; i < end; i = next)
		{
			next = TextUtils.indexOf(text, '\n', i, end);
			if (next < 0)
			{
				next = end;
			}

			int nl = 0;

			while (next < end && text.charAt(next) == '\n')
			{
				nl++;
				next++;
			}

			withinParagraph(out, text, i, next - nl);

			if (nl == 1)
			{
				out.append("\n\n");
			}
			else
			{
				for (int j = 2; j < nl; j++)
				{
					out.append("\n");
				}
				if (next != end)
				{
					/* Paragraph should be closed and reopened */
					out.append("\n\n");
				}
			}
		}
	}

	private static void withinParagraph(StringBuilder out, Spanned text, int start, int end)
	{
		int next;
		for (int i = start; i < end; i = next)
		{
			next = text.nextSpanTransition(i, end, CharacterStyle.class);
			CharacterStyle[] style = text.getSpans(i, next, CharacterStyle.class);

			for (int j = 0; j < style.length; j++)
			{
				if (style[j] instanceof StyleSpan)
				{
					int s = ((StyleSpan)style[j]).getStyle();

					if ((s & Typeface.BOLD) != 0)
					{
						out.append("**");
					}
					if ((s & Typeface.ITALIC) != 0)
					{
						out.append("*");
					}
				}

				if (style[j] instanceof UnderlineSpan)
				{
					out.append("_");
				}

				if (style[j] instanceof StrikethroughSpan)
				{
					out.append("~~");
				}

				if (style[j] instanceof URLSpan)
				{
					out.append("(");
					out.append(((URLSpan)style[j]).getURL());
					out.append(")");
				}

				if (style[j] instanceof ImageSpan)
				{
					out.append("![image](");
					out.append(((ImageSpan)style[j]).getSource());
					out.append(")");

					// Don't output the dummy character underlying the image.
					i = next;
				}

				if (style[j] instanceof AbsoluteSizeSpan)
				{
					AbsoluteSizeSpan s = ((AbsoluteSizeSpan)style[j]);
					int sizeDip = s.getSize();

					String header = "";
					switch (sizeDip)
					{
						case 42:
							header = "#";
							break;

						case 36:
							header = "##";
							break;

						case 32:
							header = "###";
							break;

						case 28:
							header = "####";
							break;
					}

					// px in CSS is the equivalance of dip in Android
					out.append(header);
				}
			}

			withinStyle(out, text, i, next);

			for (int j = style.length - 1; j >= 0; j--)
			{
				if (style[j] instanceof URLSpan)
				{
					out.append("</a>");
				}

				if (style[j] instanceof StrikethroughSpan)
				{
					out.append("~~");
				}

				if (style[j] instanceof UnderlineSpan)
				{
					out.append("_");
				}

				if (style[j] instanceof StyleSpan)
				{
					int s = ((StyleSpan)style[j]).getStyle();

					if (s == Typeface.BOLD)
					{
						out.append("**");
					}
					else if (s == Typeface.ITALIC)
					{
						out.append("*");
					}
				}
			}
		}
	}

	private static void withinStyle(StringBuilder out, CharSequence text, int start, int end)
	{
		for (int i = start; i < end; i++)
		{
			char c = text.charAt(i);

			if (c == '<')
			{
				out.append("&lt;");
			}
			else if (c == '>')
			{
				out.append("&gt;");
			}
			else if (c == '&')
			{
				out.append("&amp;");
			}
			else if (c >= 0xD800 && c <= 0xDFFF)
			{
				if (c < 0xDC00 && i + 1 < end)
				{
					char d = text.charAt(i + 1);
					if (d >= 0xDC00 && d <= 0xDFFF)
					{
						i++;
						int codepoint = 0x010000 | (int)c - 0xD800 << 10 | (int)d - 0xDC00;
						out.append("&#").append(codepoint).append(";");
					}
				}
			}
			else if (c > 0x7E || c < ' ')
			{
				out.append("&#").append((int)c).append(";");
			}
			else if (c == ' ')
			{
				while (i + 1 < end && text.charAt(i + 1) == ' ')
				{
					out.append("&nbsp;");
					i++;
				}

				out.append(' ');
			}
			else
			{
				out.append(c);
			}
		}
	}
}
