package com.commonsware.cwac.richedit;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/**
 * // TODO: Add class description
 */
public class NonAndroidUnderlineSpan extends CharacterStyle implements UpdateAppearance, ParcelableSpan
{
	public NonAndroidUnderlineSpan()
	{
	}

	public NonAndroidUnderlineSpan(Parcel src)
	{
	}

	public int getSpanTypeId()
	{
		return getSpanTypeIdInternal();
	}

	/**
	 * @hide
	 */
	public int getSpanTypeIdInternal()
	{
		return 6;
	}

	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags)
	{
		writeToParcelInternal(dest, flags);
	}

	/**
	 * @hide
	 */
	public void writeToParcelInternal(Parcel dest, int flags)
	{
	}

	@Override
	public void updateDrawState(TextPaint ds)
	{
		ds.setUnderlineText(true);
	}
}
