package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class SearchResultsItem{

	@SerializedName("birthHrsForKundali")
	private String birthHrsForKundali;

	@SerializedName("birthPlace")
	private String birthPlace;

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("education")
	private String education;

	@SerializedName("birthMins")
	private String birthMins;

	@SerializedName("birthHrs")
	private String birthHrs;

	@SerializedName("birthMinsForKundali")
	private String birthMinsForKundali;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("profileImage")
	private String profileImage;

	@SerializedName("candidateId")
	private int candidateId;

	@SerializedName("userId")
	private int userId;

	@SerializedName("age")
	private int age;

	@SerializedName("isBookMarked")
	private boolean isBookMarked;

	@SerializedName("isInviteSent")
	private boolean isInviteSent;

	public String getBirthHrsForKundali(){
		return birthHrsForKundali;
	}

	public String getBirthPlace(){
		return birthPlace;
	}

	public String getBirthdate(){
		return birthdate;
	}

	public String getEducation(){
		return education;
	}

	public String getBirthMins(){
		return birthMins;
	}

	public String getBirthHrs(){
		return birthHrs;
	}

	public String getBirthMinsForKundali(){
		return birthMinsForKundali;
	}

	public String getFullName(){
		return fullName;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public int getCandidateId(){
		return candidateId;
	}

	public int getUserId(){
		return userId;
	}

	public int getAge(){
		return age;
	}

	public boolean isBookMarked() {
		return isBookMarked;
	}

	public boolean isInviteSent() {
		return isInviteSent;
	}
}