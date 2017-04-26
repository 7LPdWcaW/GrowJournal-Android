package me.anon.growjournal.helper;

/**
 * // TODO: Add class description
 */
public class JekyllUtils
{
	public static String urlCase(String url)
	{
		return url.toLowerCase().replaceAll("[^0-9a-z]", "-");
	}
}
