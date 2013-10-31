package net.shiny.steamui.dto;

import java.util.List;

public class PlayerSummaries {
	private Players response;

	public static class Players {
		List<Player> players;
		
		public List<Player> getPlayers() {
			return players;
		}
	}
	
	public static class Player {
		String personaname;
		
		public String getPersonaname() {
			return personaname;
		}
	}
	
	public Players getResponse() {
		return response;
	}
}
