package com.dreamz.samtaBhratruMandal.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TimeSlotsDto{

	@SerializedName("availableTimeSlots")
	private List<String> availableTimeSlots;

	public List<String> getAvailableTimeSlots(){
		return availableTimeSlots;
	}
}