package net.shiny.steamui.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public final class HttpUtil {
	/**
	 * Returns a String representing the HTTP GET
	 * response for a given URL
	 * @param urlToRead
	 * @return
	 */
	public static String getGetResponse(String urlToRead) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	   }
	
	/**
	 * Encodes the given string so it can be safely used as a part of URL.
	 * Assumes UTF-8 encoding. 
	 */
	public static String encode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {			
		}
		return s;
	}
}
