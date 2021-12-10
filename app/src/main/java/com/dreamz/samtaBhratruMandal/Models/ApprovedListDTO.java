package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class ApprovedListDTO{

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("education")
	private String education;

	@SerializedName("birthMins")
	private String birthMins;

	@SerializedName("mobileNumber")
	private String mobileNumber;

	@SerializedName("heightInFeet")
	private String heightInFeet;

	@SerializedName("birthMinsForKundali")
	private String birthMinsForKundali;

	@SerializedName("complexion")
	private String complexion;

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

	@SerializedName("heightInInches")
	private String heightInInches;

	@SerializedName("birthHrs")
	private String birthHrs;

	@SerializedName("interestAccepted")
	private String interestAccepted;

	@SerializedName("position")
	private String position;

	@SerializedName("candidateId")
	private int candidateId;

	@SerializedName("age")
	private int age;

	public String getBirthdate(){
		return birthdate;
	}

	public String getEducation(){
		return education;
	}

	public String getBirthMins(){
		return birthMins;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public String getHeightInFeet(){
		return heightInFeet;
	}

	public String getBirthMinsForKundali(){
		return birthMinsForKundali;
	}

	public String getComplexion(){
		return complexion;
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

	public String getHeightInInches(){
		return heightInInches;
	}

	public String getBirthHrs(){
		return birthHrs;
	}

	public String getInterestAccepted(){
		return interestAccepted;
	}

	public String getPosition(){
		return position;
	}

	public int getCandidateId(){
		return candidateId;
	}

	public int getAge(){
		return age;
	}
}