package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class SentInterestDTO{

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("education")
	private String education;

	@SerializedName("birthMins")
	private String birthMins;

	@SerializedName("mobileNumber")
	private Object mobileNumber;

	@SerializedName("birthMinsForKundali")
	private String birthMinsForKundali;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("profileImage")
	private Object profileImage;

	@SerializedName("userId")
	private int userId;

	@SerializedName("birthHrsForKundali")
	private String birthHrsForKundali;

	@SerializedName("birthPlace")
	private String birthPlace;

	@SerializedName("birthHrs")
	private String birthHrs;

	@SerializedName("interestAccepted")
	private String interestAccepted;

	@SerializedName("candidateId")
	private int candidateId;

	@SerializedName("age")
	private int age;

	@SerializedName("isBookMarked")
	private boolean isBookMarked;

	public String getBirthdate(){
		return birthdate;
	}

	public String getEducation(){
		return education;
	}

	public String getBirthMins(){
		return birthMins;
	}

	public Object getMobileNumber(){
		return mobileNumber;
	}

	public String getBirthMinsForKundali(){
		return birthMinsForKundali;
	}

	public String getFullName(){
		return fullName;
	}

	public Object getProfileImage(){
		return profileImage;
	}

	public int getUserId(){
		return userId;
	}

	public String getBirthHrsForKundali(){
		return birthHrsForKundali;
	}

	public String getBirthPlace(){
		return birthPlace;
	}

	public String getBirthHrs(){
		return birthHrs;
	}

	public String getInterestAccepted(){
		return interestAccepted;
	}

	public int getCandidateId(){
		return candidateId;
	}

	public int getAge(){
		return age;
	}

	public boolean getIsBookMarked() {
		return isBookMarked;
	}

	public void setIsBookMarked(boolean isBookMarked) {
		this.isBookMarked = isBookMarked;
	}
}