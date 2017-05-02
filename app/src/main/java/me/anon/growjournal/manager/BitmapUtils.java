package me.anon.growjournal.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * // TODO: Add class description
 */
public class BitmapUtils
{
	/**
	 * Gets a bitmap from a given path and resizes it to the provided maxSize (in megapixels)
	 * @param path The path to the image
	 * @param maxWidth
	 * @param maxHeight
	 *
	 * @return The bitmap, or null
	 */
	@Nullable
	public static Bitmap getBitmap(String path, int maxWidth, int maxHeight)
	{
		InputStream in = null;

		try
		{
			Bitmap bitmap = null;
			in = new FileInputStream(path);

			int scale = recursiveSample(path, maxWidth, maxHeight);
			if (scale > 1)
			{
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = scale;
				bitmap = BitmapFactory.decodeStream(in, null, options);
			}
			else
			{
				bitmap = BitmapFactory.decodeStream(in);
			}

			in.close();

			return bitmap;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	/**
	 * Recursivly samples an image to below or equal the max width/height
	 *
	 * @param path
	 *            The path to the image
	 * @param maxWidth
	 *            The maximum width the image can be
	 * @param maxHeight
	 *            The maximum height the image can be
	 * @return The scale size of the image to use
	 */
	public static int recursiveSample(String path, int maxWidth, int maxHeight)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		int scale = 1;
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;

		while (imageWidth > maxWidth || imageHeight > maxHeight)
		{
			imageWidth /= 2;
			imageHeight /= 2;
			scale *= 2;
		}

		if (scale < 1)
		{
			scale = 1;
		}

		return scale;
	}
}
