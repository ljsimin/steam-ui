package net.shiny.steamui.strategy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class GameStrategy {
	/**
	 * For a given NodeList extract string values of interest
	 */
	public abstract Object extract(NodeList nodeList);

	public abstract String getRootElement();
	
	protected Node findPcGame(NodeList gameList) {
		//loop through all games
		for (int i = 0; i < gameList.getLength(); i++) {
			Node game = gameList.item(i);
			NodeList subElements = game.getChildNodes();
			//loop through all game properties
			for (int j = 0; j < subElements.getLength(); j++) {
				Node subElement = subElements.item(j);
				if (subElement.getNodeName().equals("Platform")) {
					if (subElement.getTextContent().equals("PC")) {
						//found a PC game
						return game;							
					}
				}
				
			}
		}
		return null;
	}
}
