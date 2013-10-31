package net.shiny.steamui.service;

import java.util.List;

import net.shiny.steamui.dto.Game;
import net.shiny.steamui.dto.GameDetails;



public interface SteamService {
	public List<Game> getAllGames(String steamId);

	public List<Game> getGames(String steamId, Integer offset);

	public GameDetails getGameDetails(String gameName);
	
	public String getPlayerName(String steamId);
}
