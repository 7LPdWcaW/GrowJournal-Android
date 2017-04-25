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
	 * @param maxSize The max size in MP (eg 1200000 = 12MP)
	 *
	 * @return The bitmap, or null
	 */
	@Nullable
	public static Bitmap getBitmap(String path, int maxSize)
	{
		InputStream in = null;

		try
		{
			in = new FileInputStream(path);

			// Decode image size
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();

			int scale = 1;
			while ((options.outWidth * options.outHeight) * (1 >> scale) > maxSize)
			{
				scale++;
			}

			Bitmap bitmap = null;
			in = new FileInputStream(path);

			if (scale > 1)
			{
				scale--;

				// scale to max possible inSampleSize that still yields an image
				// larger than target
				options = new BitmapFactory.Options();
				options.inSampleSize = scale;
				bitmap = BitmapFactory.decodeStream(in, null, options);

				// resize to desired dimensions
				int height = bitmap.getHeight();
				int width = bitmap.getWidth();
				double y = Math.sqrt(maxSize / (((double)width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int)x, (int)y, true);
				bitmap.recycle();
				bitmap = scaledBitmap;
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
}
