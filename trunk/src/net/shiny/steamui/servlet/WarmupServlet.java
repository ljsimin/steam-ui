package net.shiny.steamui.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.shiny.steamui.dto.Game;
import net.shiny.steamui.service.SteamService;
import net.shiny.steamui.service.impl.SteamServiceImpl;

/**
 * Fills up application cache
 * @author shiny
 *
 */
public class WarmupServlet implements Servlet {
	private final SteamService steamService = SteamServiceImpl.getInstance();
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		List<Game> games = steamService.getGames("76561197998389524", 0); //Danilo
		for (Game game : games) {
			steamService.getGenresForGameName(game.getName());
		}
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
}
