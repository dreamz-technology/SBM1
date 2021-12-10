package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class Bhakut{

	@SerializedName("male_koot_attribute")
	private String maleKootAttribute;

	@SerializedName("received_points")
	private Object receivedPoints;

	@SerializedName("total_points")
	private int totalPoints;

	@SerializedName("description")
	private String description;

	@SerializedName("female_koot_attribute")
	private String femaleKootAttribute;

	public String getMaleKootAttribute(){
		return maleKootAttribute;
	}

	public Object getReceivedPoints(){
		return receivedPoints;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public String getDescription(){
		return description;
	}

	public String getFemaleKootAttribute(){
		return femaleKootAttribute;
	}
}