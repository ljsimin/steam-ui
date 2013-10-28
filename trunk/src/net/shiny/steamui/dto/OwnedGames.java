package net.shiny.steamui.dto;

import java.util.List;

public class OwnedGames {
	private Integer game_count;
	private List<Game> games;
	
	public Integer getGame_count() {
		return game_count;
	}
	public List<Game> getGames() {
		return games;
	}
}
