package net.shiny.steamui.endpoint;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shiny.steamui.dto.GameDetails;
import net.shiny.steamui.service.SteamService;
import net.shiny.steamui.service.impl.SteamServiceImpl;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class GameDetailEndpoint extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(GameDetailEndpoint.class.getName());
	
	private static SteamService steamService = SteamServiceImpl.getInstance();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String gameName = request.getParameter("name");
		LOG.info("Requesting game details for " + gameName);
		GameDetails gameDetails = null;
		if (gameName != null && gameName.trim().length() > 0) {
			gameDetails = steamService.getGameDetails(gameName.trim());
		}
		Gson gson = new Gson();
		response.getWriter().append(gson.toJson(gameDetails));
		response.setStatus(HttpURLConnection.HTTP_OK);
	}
}
