package com.dreamz.samtaBhratruMandal.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseLocation{

	@SerializedName("homeTown")
	private List<String> homeTown;

	@SerializedName("taluka")
	private List<String> taluka;

	@SerializedName("district")
	private List<String> district;

	public List<String> getHomeTown(){
		return homeTown;
	}

	public List<String> getTaluka(){
		return taluka;
	}

	public List<String> getDistrict(){
		return district;
	}
}