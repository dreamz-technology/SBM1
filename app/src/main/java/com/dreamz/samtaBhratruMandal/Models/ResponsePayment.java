package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class ResponsePayment{

	@SerializedName("result")
	private String result;

	@SerializedName("message")
	private String message;

	public String getResult(){
		return result;
	}

	public String getMessage(){
		return message;
	}
}