package net.shiny.steamui.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlUtil {
	private static final Logger LOG = Logger.getLogger(XmlUtil.class.getName());
	
	/**
	 * Returns the list of Strings containing text subelements
	 * for a given parent node
	 * @param xml
	 * @param parentElementName
	 * @return
	 */
	public static List<String> getTextChildNodes(String xml, String parentElementName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            org.w3c.dom.Document doc = builder.parse(is);
            Node element = doc.getElementsByTagName(parentElementName).item(0);
            if (element != null) {
	            NodeList list = element.getChildNodes();
	            List<String> result = new ArrayList<String>();
	            for (int i = 0; i < list.getLength(); i++) {
	            	if (!result.contains(list.item(i).getTextContent()))
	            		result.add(list.item(i).getTextContent());
	            }
	            return result;
            }
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
