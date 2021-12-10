package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin{

	@SerializedName("birthMins")
	private String birthMins;

	@SerializedName("gender")
	private String gender;

	@SerializedName("birthMinsForKundali")
	private String birthMinsForKundali;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("mobileNo")
	private String mobileNo;

	@SerializedName("message")
	private String message;

	@SerializedName("userId")
	private int userId;

	@SerializedName("birthDate")
	private String birthDate;

	@SerializedName("hasPaid")
	private boolean hasPaid;

	@SerializedName("birthHrsForKundali")
	private String birthHrsForKundali;

	@SerializedName("result")
	private String result;

	@SerializedName("birthPlace")
	private String birthPlace;

	@SerializedName("paymentRequired")
	private int paymentRequired;

	@SerializedName("birthHrs")
	private String birthHrs;

	@SerializedName("profileImagePath")
	private String profileImagePath;

	@SerializedName("candidateId")
	private int candidateId;

	@SerializedName("applicationId")
	private String applicationId;

	@SerializedName("email")
	private String email;

	public String getBirthMins(){
		return birthMins;
	}

	public String getGender(){
		return gender;
	}

	public String getBirthMinsForKundali(){
		return birthMinsForKundali;
	}

	public String getFullName(){
		return fullName;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public String getMessage(){
		return message;
	}

	public int getUserId(){
		return userId;
	}

	public String getBirthDate(){
		return birthDate;
	}

	public boolean isHasPaid(){
		return hasPaid;
	}

	public String getBirthHrsForKundali(){
		return birthHrsForKundali;
	}

	public String getResult(){
		return result;
	}

	public String getBirthPlace(){
		return birthPlace;
	}

	public int getPaymentRequired(){
		return paymentRequired;
	}

	public String getBirthHrs(){
		return birthHrs;
	}

	public String getProfileImagePath(){
		return profileImagePath;
	}

	public int getCandidateId(){
		return candidateId;
	}

	public String getApplicationId(){
		return applicationId;
	}

	public String getEmail(){
		return email;
	}
}