package net.shiny.steamui.dao;

import java.util.Set;

import net.shiny.steamui.util.CollectionUtil;

/**
 * Keeps the static set of games that support local coop
 * @author shiny
 *
 */
public class LocalCoopDao {
	private static final Set<String> localCoopGames = CollectionUtil.newHashSet(
			"awesomenauts",
			"the baconing",
			"castle crashers",
			"dead pixels",
			"jamestown",
			"lara croft and the guardian of light",
			"magicka",
			"rayman origins",
			"shatter",
			"trine",
			"trine 2",
			"worms reloaded",
			"left 4 dead",
			"left 4 dead 2"
			); 
	
	public static Boolean supportsLocalCoop(String gameName) {
		return localCoopGames.contains(gameName.toLowerCase());
	}
}
