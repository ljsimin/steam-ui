package net.shiny.steamui.endpoint;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shiny.steamui.service.SteamService;
import net.shiny.steamui.service.impl.SteamServiceImpl;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class PlayerDetailEndpoint extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(PlayerDetailEndpoint.class.getName());
	
	private static SteamService steamService = SteamServiceImpl.getInstance();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String steamId = request.getParameter("id");
		String playerName = null;
		if (steamId != null && steamId.trim().length() > 0) {
			playerName = steamService.getPlayerName(steamId.trim());
		}
		Gson gson = new Gson();
		response.getWriter().append(gson.toJson(playerName));
		response.setStatus(HttpURLConnection.HTTP_OK);
	}
}
