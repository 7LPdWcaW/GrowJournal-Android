package me.anon.growjournal.data;

import java.util.HashMap;
import java.util.Map;

public class DefaultHashMap<K, V> extends HashMap<K, V>
{
	public DefaultHashMap()
	{
	}

	public DefaultHashMap(Map load)
	{
		super(load);
	}

	public V get(Object k, V d)
	{
		return containsKey(k) ? get(k) : d;
	}

	public String getString(Object k, String d)
	{
		return containsKey(k) ? (String)get(k) : d;
	}

	public int getInt(Object k, int d)
	{
		return containsKey(k) ? (Integer)get(k) : d;
	}
}
