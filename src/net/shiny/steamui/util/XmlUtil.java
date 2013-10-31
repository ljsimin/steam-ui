package net.shiny.steamui.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.shiny.steamui.strategy.GameStrategy;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlUtil {
	private static final Logger LOG = Logger.getLogger(XmlUtil.class.getName());
	
	/**
	 * Returns the object representing the results 
	 * for an extraction strategy
	 * @param xml
	 * @param parentElementName
	 * @return
	 */
	public static Object extractElements(String xml, GameStrategy strategy) {
		System.out.println(xml);
		System.out.println(xml.contains("Developer"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            org.w3c.dom.Document doc = builder.parse(is);
            NodeList elementList = doc.getElementsByTagName(strategy.getRootElement());
            return strategy.extract(elementList);
        } catch (ParserConfigurationException e) {
        	LOG.severe("Exception while parsing " + xml + " " + e.getMessage());
        } catch (SAXException e) {
        	LOG.severe("Exception while parsing " + xml + " " + e.getMessage());
        } catch (IOException e) {
        	LOG.severe("Exception while parsing " + xml + " " + e.getMessage());
        }
        return null;
	}
}
