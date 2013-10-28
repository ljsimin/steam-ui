package net.shiny.steamui.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.shiny.steamui.config.ConfigurationHolder;
import net.shiny.steamui.dto.Game;
import net.shiny.steamui.dto.GetOwnedGames;
import net.shiny.steamui.dto.OwnedGames;
import net.shiny.steamui.util.HttpUtil;

import com.google.gson.Gson;

public class SteamDao {
	private static final Logger LOG = Logger.getLogger(SteamDao.class.getName());
	private final static String STEAM_API_KEY;
	private static final String STEAM_API_URL;
	
	private static Map<String, List<Game>> cachedGames = new HashMap<String, List<Game>>();

	static {
		STEAM_API_KEY = ConfigurationHolder.properties.getProperty("steam.api.key");
		STEAM_API_URL = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+STEAM_API_KEY+"&format=json&&include_appinfo=1";
	}
	
	public static List<Game> getAllGamesBySteamId(String steamId) {
		 if (cachedGames.containsKey(steamId)) {
				LOG.info("Loading games from cache for steamId " + steamId);
				return cachedGames.get(steamId);
			}
			String json = HttpUtil.getGetResponse(STEAM_API_URL + "&steamid=" + steamId);
			
			Gson gson = new Gson();
			GetOwnedGames response = gson.fromJson(json, GetOwnedGames.class);
			OwnedGames games = response.getResponse();
			LOG.info("Adding games to cache for steamId " + steamId);
			cachedGames.put(steamId, games.getGames());
			return cachedGames.get(steamId);
	 }
}
