package net.shiny.steamui.endpoint;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shiny.steamui.enums.TagAction;
import net.shiny.steamui.service.SteamService;
import net.shiny.steamui.service.impl.SteamServiceImpl;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class TagEndpoint extends HttpServlet {
private static final Logger LOG = Logger.getLogger(TagEndpoint.class.getName());
	
	private static SteamService steamService = SteamServiceImpl.getInstance();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String actionString = request.getParameter("action");
		String playerSteamIdString = request.getParameter("steamId");
		String gameNameString = request.getParameter("gameName");
		String tagString = request.getParameter("tag");
//		LOG.info("Requesting game details for " + gameName);
		
		TagAction action = TagAction.valueOf(actionString);
		
		Gson gson = new Gson();
		List<String> tags;
		switch (action) {
		case GET:
			tags = steamService.getTagsForGameName(playerSteamIdString, gameNameString);
			response.getWriter().append(gson.toJson(tags));
			break;
		case ADD:
			tags = steamService.addTagToGame(playerSteamIdString, gameNameString, tagString);
			response.getWriter().append(gson.toJson(tags));
			break;
		default:
			break;
		}
		response.setStatus(HttpURLConnection.HTTP_OK);
		return;
	}
}
