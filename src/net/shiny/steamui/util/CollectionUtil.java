package net.shiny.steamui.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtil {
	
	public static <T> Set<T> newHashSet(T... elements) {
	    HashSet<T> set = new HashSet<T>();

	    for (T e : elements) {
	        set.add(e);
	    }
	    return set;
	}

}
