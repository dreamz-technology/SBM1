package com.dreamz.samtaBhratruMandal.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResultDTO{

	@SerializedName("result")
	private String result;

	@SerializedName("totalResults")
	private int totalResults;

	@SerializedName("message")
	private String message;

	@SerializedName("searchResults")
	private List<SearchResultsItem> searchResults;

	public String getResult(){
		return result;
	}

	public int getTotalResults(){
		return totalResults;
	}

	public String getMessage(){
		return message;
	}

	public List<SearchResultsItem> getSearchResults(){
		return searchResults;
	}
}