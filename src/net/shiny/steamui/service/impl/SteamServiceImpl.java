package net.shiny.steamui.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.shiny.steamui.config.ConfigurationHolder;
import net.shiny.steamui.dao.SteamDao;
import net.shiny.steamui.dao.TheGamesDbDao;
import net.shiny.steamui.dto.Game;
import net.shiny.steamui.service.SteamService;

/**
 * Service for manipulating the list of Steam games
 * @author shiny
 *
 */
public class SteamServiceImpl implements SteamService {
	private static final int FRAME_SIZE;
	private static final Logger LOG = Logger.getLogger(SteamServiceImpl.class.getName());
	
	private static class Holder {
        public static SteamService service = new SteamServiceImpl();
    }

    public static SteamService getInstance() {
        return Holder.service;
    }
    
    private SteamServiceImpl() {
    	//hiding default constructor
    }
    
    static {
    	FRAME_SIZE = Integer.parseInt(ConfigurationHolder.configuration.getProperty("steamService.frameSize"));
    }

    /**
     * Returns all games for a given steamId
     */
	@Override
	public List<Game> getAllGames(String steamId) {
		List<Game> games = SteamDao.getAllGamesBySteamId(steamId);	
		List<Game> result = new ArrayList<Game>();
		for (int i = 0; i< games.size(); i++) {
			Game game = games.get(i);
			List<String> genres = TheGamesDbDao.getGenresByGameName(game.getName());			
			game.setGenres(genres != null ? genres : Arrays.asList("unknown"));
			game.updateSearchField();
			result.add(game);
		}
		return result;		
	}

	/**
	 * Returns {@value #FRAME_SIZE} games for a given steamId, with a given offset
	 */
	@Override
	public List<Game> getGames(String steamId, Integer offset) {
		List<Game> games = SteamDao.getAllGamesBySteamId(steamId);	
		List<Game> result = new ArrayList<Game>();
		int counter = FRAME_SIZE;
		for (int i = offset; i< games.size(); i++) {
			if (--counter < 0) 
				break;
			Game game = games.get(i);
			List<String> genres = TheGamesDbDao.getGenresByGameName(game.getName());			
			game.setGenres(genres != null ? genres : Arrays.asList("unknown"));
			game.updateSearchField();
			result.add(game);
		}
		return result;
	}
	
	
}
