package net.shiny.steamui.strategy.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.shiny.steamui.strategy.GameStrategy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class ExtractGenresStrategy extends GameStrategy{

	private final static String ROOT_ELEMENT = "Game";
	
	/**
	 * Looks for the <b>first</b> game on PC platform and returns
	 * list of its genres
	 */
	@Override
	public List<String> extract(NodeList gameList) {
		if (gameList != null) {
			Node game = findPcGame(gameList);
			
			//loop through all game properties
			if (game != null) {
				List<String> result = new ArrayList<String>();
				NodeList subElements = game.getChildNodes();
				for (int j = 0; j < subElements.getLength(); j++) {
					Node subElement = subElements.item(j);
					if (subElement.getNodeName().equals("Genres")) {
						NodeList genres = subElement.getChildNodes();
						for (int k = 0; k < genres.getLength(); k++) {
							addGenre(result, genres.item(k).getTextContent());
						}
					}
					if (subElement.getNodeName().equals("Co-op")) {
						if (subElement.getTextContent().equals("Yes")) {
							addGenre(result, "Co-op");
						}
					}
				}
				return result;
			}
			
        }
		return Arrays.asList("unknown");
	}
	
	@Override
	public String getRootElement() {
		return ROOT_ELEMENT;
	}

	private void addGenre(List<String> result, String genre) {
		if (!result.contains(genre)) {
			result.add(genre);
		}
	}
}
