package net.shiny.steamui.strategy.impl;

import net.shiny.steamui.dto.GameDetails;
import net.shiny.steamui.strategy.GameStrategy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExtractGameDetailsStrategy extends GameStrategy {

	@Override
	public Object extract(NodeList gameList) {
		if (gameList != null) {
			Node game = findPcGame(gameList);
			//loop through all game properties
			if (game != null) {
				GameDetails details = new GameDetails();
				NodeList subElements = game.getChildNodes();
				for (int i = 0; i < subElements.getLength(); i++) {
					Node subElement = subElements.item(i);
					String nodeName = subElement.getNodeName();
					switch (nodeName) {
						case "GameTitle" : {
							details.setGameTitle(subElement.getTextContent());
							break;
						}
						case "ReleaseDate" : {
							details.setReleaseDate(subElement.getTextContent());
							break;
						}
						case "Overview" : {
							details.setOverview(subElement.getTextContent());
							break;
						}
						case "Players" : {
							details.setPlayers(subElement.getTextContent());
							break;
						}
						case "Publisher" : {
							details.setPublisher(subElement.getTextContent());
							break;
						}
						case "Developer" : {
							details.setDeveloper(subElement.getTextContent());
							break;
						}
						case "Rating" : {
							details.setRating(subElement.getTextContent());
							break;
						}
						case "Images" : {
							String image = subElement.getFirstChild().getTextContent();
							details.setImageUrl("http://thegamesdb.net/banners/"+image);
							break;
						}
					}
				}
				return details;
			}
			
        }
		return null;
	}

	/**
	 * Returns the text value of the element with a given name
	 */
	private String getValue(Node node, String elementName) {
		if (node.getNodeName().equals(elementName)) {
			return node.getTextContent();
		}
		return "";
	}

	@Override
	public String getRootElement() {
		return "Game";
	}

}
