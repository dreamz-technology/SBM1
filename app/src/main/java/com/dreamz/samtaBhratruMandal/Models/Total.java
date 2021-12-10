package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class Total{

	@SerializedName("received_points")
	private Object receivedPoints;

	@SerializedName("total_points")
	private int totalPoints;

	@SerializedName("minimum_required")
	private int minimumRequired;

	public Object getReceivedPoints(){
		return receivedPoints;
	}

	public int getTotalPoints(){
		return totalPoints;
	}

	public int getMinimumRequired(){
		return minimumRequired;
	}
}