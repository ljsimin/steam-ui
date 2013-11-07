package net.shiny.steamui.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.shiny.steamui.dto.GameDetails;
import net.shiny.steamui.strategy.GameStrategy;
import net.shiny.steamui.strategy.impl.ExtractGameDetailsStrategy;
import net.shiny.steamui.strategy.impl.ExtractGenresStrategy;
import net.shiny.steamui.util.HttpUtil;
import net.shiny.steamui.util.XmlUtil;

import com.google.appengine.api.datastore.Entity;

/**
 * DAO for accessing the game information stored in
 * TheGamesDB.net database
 * @author shiny
 *
 */
public final class TheGamesDbDao {
	private static final Logger LOG = Logger.getLogger(TheGamesDbDao.class.getName());
	private static final String DB_URL = "http://thegamesdb.net/api/GetGame.php?name=";
	private static final String ROOT_NODE = "Games";
	private static Map<String, List<String>> cachedGenres = new HashMap<String, List<String>>();
	
	/**
	 * Returns the list of strings representing game genres
	 * for a given game name.
	 * @param gameNameLocal
	 * @param onlyFromCache - whether or not to skip fetching the genres from internet if they are missing
	 * @return
	 */
	public static List<String> getGenresByGameName(String gameName, Boolean onlyFromCache) {
		String gameNameLocal = gameName.trim();
		if (cachedGenres.containsKey(gameNameLocal) && cachedGenres.get(gameNameLocal) != null) {
//			LOG.info("Loading game genres from cache for game " + gameName);
			return cachedGenres.get(gameNameLocal);
		}
		//fetching from datastore
		Entity genre = DatastoreDao.getEntity("genre", gameNameLocal);
		if (genre != null && (List<String>) genre.getProperty("genres") != null) {
			saveInCache(gameNameLocal, (List<String>) genre.getProperty("genres"));
		} else {
			LOG.info("Genres not found in the datastore: " + gameNameLocal);
			if (!onlyFromCache) {
				List<String> genres = getGenresFromInternet(gameNameLocal);
				saveInCache(gameNameLocal, genres);
				genre = DatastoreDao.createEntity("genre", gameNameLocal);
				genre.setProperty("genres", genres);
				DatastoreDao.persistEntity(genre);
			} else {
				LOG.info("Genres not found in the datastore, will not look them up online " + gameNameLocal);
			}
		}
		return cachedGenres.get(gameNameLocal);
	}
	
	/**
	 * Gets the game datails for the game with a given name
	 * @param gameName
	 * @return
	 */
	public static GameDetails getGameDetails(String gameName) {
		LOG.info("Fetching game details from the internet for game " + gameName);
		String xml = HttpUtil.getGetResponse(buildUrl(gameName));
		GameDetails gameDetails = (GameDetails) XmlUtil.extractElements(xml, new ExtractGameDetailsStrategy());
		if (gameDetails != null) {
			gameDetails.setGenres(getGenresByGameName(gameName, true));
		}
		return gameDetails;
	}

	private static List<String> getGenresFromInternet(String gameName) {
		LOG.info("Fetching genres from the internet: " + gameName);
		String xml = HttpUtil.getGetResponse(buildUrl(gameName));
		GameStrategy strategy =  new ExtractGenresStrategy();
		List<String> genres = (List<String>) XmlUtil.extractElements(xml, strategy);
		if (genres == null || genres.isEmpty()) {
			genres = Arrays.asList("unknown");
		}
		return genres;
	}

	private static void saveInCache(String gameName, List<String> genres) {
//		LOG.info("Storing game genres in cache for game " + gameName + " : " + genres);
		cachedGenres.put(gameName, genres);
	}
	
	private static String buildUrl(String gameName) {
		try {
			return DB_URL+URLEncoder.encode(gameName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.severe("UnsupportedEncodingException " + e.getMessage());
		}
		return "";
	}

}
