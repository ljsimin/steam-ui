package net.shiny.steamui.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TagDao {
	private static Map<String, Map<String, List<String>>> tags = new HashMap<String, Map<String,List<String>>>();
	
	private static final Logger LOG = Logger.getLogger(TagDao.class.getName());
	
	public static Map<String, List<String>> getAllTagsForPlayer(String playerSteamId) {
		return tags.get(playerSteamId);
	}
	
	public static List<String> getTagsForGame(String playerSteamId, String gameName) {
		Map<String, List<String>> playerTags = tags.get(playerSteamId);
		
		if (playerTags != null) {
			return playerTags.get(gameName);
		}
		LOG.warning(String.format("No tags for a given steamId %s and game %s", playerSteamId, gameName));
		return null;
	}
	
	public static List<String> addTagToGame(String playerSteamId, String gameName, String tag) {
		Map<String, List<String>> playerTags;
		if (!tags.containsKey(playerSteamId)) {
			LOG.info(String.format("%s does not yet have any tags", playerSteamId));
			tags.put(playerSteamId, new HashMap<String, List<String>>());
		}
		playerTags = tags.get(playerSteamId);
		List<String> gameTags;
		if (!playerTags.containsKey(gameName)) {
			LOG.info(String.format("%s does not yet have tags for game %s", playerSteamId, gameName));
			playerTags.put(gameName, new ArrayList<String>());
		}
		gameTags = playerTags.get(gameName);
		LOG.info(String.format("Adding tag %s to game %s for player %s", tag, gameName, playerSteamId));
		gameTags.add(tag);
		return getTagsForGame(playerSteamId, gameName);
	}
	
}
