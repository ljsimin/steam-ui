package net.shiny.steamui.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
	private static final String GENRES = "Genres";
	private static Map<String, List<String>> cachedGenres = new HashMap<String, List<String>>();
	
	/**
	 * Returns the list of strings representing game genres
	 * for a given game name.
	 * @param gameName
	 * @return
	 */
	public static List<String> getGenresByGameName(String gameName) {
		if (cachedGenres.containsKey(gameName) && cachedGenres.get(gameName) != null) {
//			LOG.info("Loading game genres from cache for game " + gameName);
			return cachedGenres.get(gameName);
		}
		//fetching from datastore
		Entity genre = DatastoreDao.getEntity("genre", gameName);
		if (genre != null) {
			List<String> genres = (List<String>) genre.getProperty("genres");
	//		if (genres == null || genres.contains("unknown")) {
	//			LOG.info("Genres empty or null, refetching");
	//			genres = getGenresFromInternet(gameName);
	//			persistEntity(gameName, key, genres);
	//		}
			saveInCache(gameName, genres);
		} else {
			LOG.info("Genre not found in the datastore: " + gameName);
			List<String> genres = getGenresFromInternet(gameName);
			saveInCache(gameName, genres);
			genre = DatastoreDao.createEntity("genre", gameName);
			genre.setProperty("genres", genres);
			DatastoreDao.persistEntity(genre);
		}
		return cachedGenres.get(gameName);
	}

	private static List<String> getGenresFromInternet(String gameName) {
		LOG.info("Fetching genres from the internet: " + gameName);
		String xml = HttpUtil.getGetResponse(buildUrl(gameName));
		List<String> genres = XmlUtil.getTextChildNodes(xml, GENRES);
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
