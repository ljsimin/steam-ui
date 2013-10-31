package net.shiny.steamui.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.shiny.steamui.config.ConfigurationHolder;
import net.shiny.steamui.dto.Game;
import net.shiny.steamui.dto.GetOwnedGames;
import net.shiny.steamui.dto.OwnedGames;
import net.shiny.steamui.dto.PlayerSummaries;
import net.shiny.steamui.dto.PlayerSummaries.Player;
import net.shiny.steamui.dto.PlayerSummaries.Players;
import net.shiny.steamui.util.HttpUtil;

import com.google.gson.Gson;

public class SteamDao {
	private static final Logger LOG = Logger.getLogger(SteamDao.class.getName());
	private final static String STEAM_API_KEY;
	private static final String STEAM_API_GAMES_URL;
	private static final String STEAM_API_PLAYER_URL;

	
	private static Map<String, List<Game>> cachedGames = new HashMap<String, List<Game>>();
	private static Map<String, String> cachedPlayerNames = new HashMap<String, String>();

	static {
		STEAM_API_KEY = ConfigurationHolder.api.getProperty("steam.api.key");
		STEAM_API_GAMES_URL = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+STEAM_API_KEY+"&format=json&&include_appinfo=1";
		STEAM_API_PLAYER_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="+STEAM_API_KEY+"&format=json";
	}
	
	public static List<Game> getAllGamesBySteamId(String steamId) {
		if (cachedGames.containsKey(steamId)) {
				LOG.info("Loading games from cache for steamId " + steamId);
				return cachedGames.get(steamId);
		}
		String json = HttpUtil.getGetResponse(STEAM_API_GAMES_URL + "&steamid=" + steamId);
		
		Gson gson = new Gson();
		GetOwnedGames response = gson.fromJson(json, GetOwnedGames.class);
		OwnedGames games = response.getResponse();
		LOG.info("Adding games to cache for steamId " + steamId);
		cachedGames.put(steamId, games.getGames());
		return cachedGames.get(steamId);
	 }

	public static String getPlayerName(String steamId) {
		if (cachedPlayerNames.containsKey(steamId)) {
			return cachedPlayerNames.get(steamId);
	}
	String json = HttpUtil.getGetResponse(STEAM_API_PLAYER_URL + "&steamids=" + steamId);
	
	Gson gson = new Gson();
	PlayerSummaries response = gson.fromJson(json, PlayerSummaries.class);
	Players players = response.getResponse();
	Player player = players.getPlayers().get(0);
	String playerName;
	if (player != null) {
		playerName = player.getPersonaname();
		cachedPlayerNames.put(steamId, playerName);		
	}
	return cachedPlayerNames.get(steamId);
	}
}
