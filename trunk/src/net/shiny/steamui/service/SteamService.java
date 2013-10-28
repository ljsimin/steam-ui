package net.shiny.steamui.service;

import java.util.List;

import net.shiny.steamui.dto.Game;



public interface SteamService {
	public List<Game> getAllGames(String steamId);

	public List<Game> getGames(String steamId, Integer offset);
}
