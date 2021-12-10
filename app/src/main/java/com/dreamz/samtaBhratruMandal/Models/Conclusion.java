package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class Conclusion{

	@SerializedName("report")
	private String report;

	@SerializedName("status")
	private boolean status;

	public String getReport(){
		return report;
	}

	public boolean isStatus(){
		return status;
	}
}