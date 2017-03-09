package me.anon.growjournal.helper;

import android.support.annotation.Nullable;

import java.util.WeakHashMap;

import lombok.Getter;

/**
 * Singleton class used for passing large data sets around without having to serialise.
 * This class should <b>not</b> reference any kind of context without implicitly destroying
 * the object when it is done to prevent Context leaks.
 */
public class BundleHelper
{
	@Getter(lazy = true) private static final BundleHelper instance = new BundleHelper();

	private WeakHashMap<String, Object> dataStore = new WeakHashMap<>();

	/**
	 * Stores object data against a class name. Operation is destructive, if {@param tag} already exists in the data store map.
	 *
	 * @param tag The tag to store against, use the same tag name to retrieve the data back.
	 * @param data The data to temporarily store.
	 */
	public void store(Class tag, @Nullable Object data)
	{
		store(tag.getName(), data);
	}

	/**
	 * Stores object data against a class name. Operation is destructive, if {@param tag} already exists in the data store map.
	 *
	 * @param tag The tag to store against, use the same tag name to retrieve the data back.
	 * @param data The data to temporarily store.
	 */
	public void store(String tag, @Nullable Object data)
	{
		dataStore.put(tag, data);
	}

	/**
	 * Gets the stored data object from a given tag, or null if nothing was stored. Operation is destructive,
	 * object will be removed from data store once retrieved.
	 *
	 * @param tag The tag used to originally store the data
	 * @return The found object or null.
	 */
	@Nullable
	public Object retrieve(Class tag)
	{
		return retrieve(tag.getName(), Object.class);
	}

	/**
	 * Gets the stored data object from a given tag, or null if nothing was stored. Operation is destructive,
	 * object will be removed from data store once retrieved.
	 *
	 * @param tag The tag used to originally store the data
	 * @return The found object or null.
	 */
	@Nullable
	public Object retrieve(String tag)
	{
		return retrieve(tag, Object.class);
	}

	/**
	 * Gets the stored data object from a given tag, or null if nothing was stored. Operation is destructive,
	 * object will be removed from data store once retrieved.
	 *
	 * @param tag The tag used to originally store the data
	 * @param classType The class type to force cast to
	 * @return The found object or null.
	 */
	@Nullable
	public <T> T retrieve(Class tag, Class<T> classType)
	{
		return (T)classType.cast(dataStore.remove(tag.getName()));
	}

	/**
	 * Gets the stored data object from a given tag, or null if nothing was stored. Operation is destructive,
	 * object will be removed from data store once retrieved.
	 *
	 * @param tag The tag used to originally store the data
	 * @param classType The class type to force cast to
	 * @return The found object or null.
	 */
	@Nullable
	public <T> T retrieve(String tag, Class<T> classType)
	{
		return (T)classType.cast(dataStore.remove(tag));
	}

	/**
	 * Force destroys the data store to remove all references
	 */
	public void clear()
	{
		dataStore = new WeakHashMap<>();
	}
}
