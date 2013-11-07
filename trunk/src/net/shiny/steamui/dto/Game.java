package net.shiny.steamui.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public final class Game implements Comparable<Game> {
	private String appid;
	private String name;
	@SerializedName("playtime_forever") 
	private Integer playtimeForever;
	@SerializedName("img_icon_url") 
	private String imgIconUrl;
	private List<String> genres;
	private String searchField;
	private String enrichUrl;
	
	public String getAppid() {
		return appid;
	}
	public String getName() {
		return name;
	}
	public Integer getPlaytimeForever() {
		return playtimeForever;
	}
	public String getImgIconUrl() {
		return imgIconUrl;
	}
	public List<String> getGenres() {
		return genres;
	}
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	
	public void updateSearchField() {		
		this.searchField = new StringBuilder().append(name + " ").append(genres != null ? genres.toString() : "").toString(); 
	}
	
	public String getEnrichUrl() {
		return enrichUrl;
	}
	public void setEnrichUrl(String enrichUrl) {
		this.enrichUrl = enrichUrl;
	}
	@Override
	public int compareTo(Game o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (o instanceof Game) {
			return o.getName().compareTo(this.getName()) * -1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
