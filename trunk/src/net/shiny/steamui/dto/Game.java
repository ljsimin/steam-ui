package net.shiny.steamui.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public final class Game {
	private String appid;
	private String name;
	@SerializedName("playtime_forever") 
	private Integer playtimeForever;
	@SerializedName("img_icon_url") 
	private String imgIconUrl;
	private List<String> genres;
	private String searchField;
	
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
}
