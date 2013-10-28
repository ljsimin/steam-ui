package net.shiny.steamui.endpoint;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shiny.steamui.dto.Game;
import net.shiny.steamui.service.SteamService;
import net.shiny.steamui.service.impl.SteamServiceImpl;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class SteamUiEndpoint extends HttpServlet {
	private static SteamService steamService = SteamServiceImpl.getInstance();
	
	private static final Logger LOG = Logger.getLogger(SteamUiEndpoint.class.getName());
	
	/**
	 * Returns JSON serialized list of games for a given steamId and offset.
	 * Amount of games returned depends on the implementation of the service method.
	 * <p/>
	 * It can be assumed that not all games will be returned, but a subset of them.
	 * Calling this method subsequently while increasing the offset appropriately 
	 * will result in a complete list of games.
	 * <p/>
	 * It is important to measure the size of the returned list to calculate the offset
	 * correctly. The list is complete when result for a given offset is empty. 
	 * 
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String steamId = request.getParameter("id");
		Integer offset = Integer.parseInt(request.getParameter("offset").trim());
		LOG.info("Requesting games for steamId " + steamId + " and offset " + offset);
		List<Game> games = new ArrayList<>();
		if (steamId != null && steamId.trim().length() > 0) {
			games = steamService.getGames(steamId.trim(), offset);
		}
		Gson gson = new Gson();
		response.getWriter().append(gson.toJson(games));
		response.setStatus(HttpURLConnection.HTTP_OK);
	}
}
