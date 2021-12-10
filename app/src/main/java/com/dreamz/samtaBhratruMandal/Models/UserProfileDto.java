package com.dreamz.samtaBhratruMandal.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserProfileDto{

	@SerializedName("candidateType")
	private String candidateType;

	@SerializedName("birthName")
	private String birthName;

	@SerializedName("birthPlace")
	private String birthPlace;

	@SerializedName("birthDate")
	private String birthDate;

	@SerializedName("birthTime")
	private Object birthTime;

	@SerializedName("altParentalEmail")
	private Object altParentalEmail;

	@SerializedName("mamkul")
	private String mamkul;

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("sisUnmarried")
	private String sisUnmarried;

	@SerializedName("altParentalContactNo2")
	private Object altParentalContactNo2;

	@SerializedName("userImages")
	private List<UserImagesItem> userImages;

	@SerializedName("altParentalContactNo1")
	private String altParentalContactNo1;

	@SerializedName("profileImage")
	private String profileImage;

	@SerializedName("bloodGroup")
	private String bloodGroup;

	@SerializedName("parentalContactNo1")
	private String parentalContactNo1;

	@SerializedName("parentalContactNo2")
	private String parentalContactNo2;

	@SerializedName("homeTown")
	private String homeTown;

	@SerializedName("fatherGurdianTitle")
	private String fatherGurdianTitle;

	@SerializedName("educationLevel")
	private String educationLevel;

	@SerializedName("birthHrs")
	private String birthHrs;

	@SerializedName("nameOfFatherGuardian")
	private String nameOfFatherGuardian;

	@SerializedName("altFatherGurdianTitle")
	private String altFatherGurdianTitle;

	@SerializedName("company")
	private String company;

	@SerializedName("expectations")
	private String expectations;

	@SerializedName("monthlyIncome")
	private String monthlyIncome;

	@SerializedName("parentalAddress")
	private String parentalAddress;

	@SerializedName("birthMins")
	private String birthMins;

	@SerializedName("gotra")
	private String gotra;

	@SerializedName("heightInFeet")
	private String heightInFeet;

	@SerializedName("complexion")
	private String complexion;

	@SerializedName("broUnmarried")
	private String broUnmarried;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("mobileNo")
	private String mobileNo;

	@SerializedName("userId")
	private int userId;

	@SerializedName("parentalEmail")
	private String parentalEmail;

	@SerializedName("altParentalAddress")
	private String altParentalAddress;

	@SerializedName("broMarried")
	private String broMarried;

	@SerializedName("sisMarried")
	private String sisMarried;

	@SerializedName("qualification")
	private String qualification;

	@SerializedName("heightInInches")
	private String heightInInches;

	@SerializedName("jobLocation")
	private String jobLocation;

	@SerializedName("mBirthDate")
	private String mBirthDate;

	@SerializedName("taluka")
	private String taluka;

	@SerializedName("district")
	private String district;

	@SerializedName("altNameOfFatherGuardian")
	private String altNameOfFatherGuardian;

	@SerializedName("candidateId")
	private int candidateId;

	@SerializedName("applicationId")
	private String applicationId;

	@SerializedName("age")
	private int age;

	public String getCandidateType(){
		return candidateType;
	}

	public Object getAltParentalEmail(){
		return altParentalEmail;
	}

	public String getMamkul(){
		return mamkul;
	}

	public String getOccupation(){
		return occupation;
	}

	public String getSisUnmarried(){
		return sisUnmarried;
	}

	public Object getAltParentalContactNo2(){
		return altParentalContactNo2;
	}

	public List<UserImagesItem> getUserImages(){
		return userImages;
	}

	public String getAltParentalContactNo1(){
		return altParentalContactNo1;
	}

	public Object getBirthTime(){
		return birthTime;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public String getBirthName(){
		return birthName;
	}

	public String getBirthPlace(){
		return birthPlace;
	}

	public String getBloodGroup(){
		return bloodGroup;
	}

	public String getParentalContactNo1(){
		return parentalContactNo1;
	}

	public String getParentalContactNo2(){
		return parentalContactNo2;
	}

	public String getHomeTown(){
		return homeTown;
	}

	public String getFatherGurdianTitle(){
		return fatherGurdianTitle;
	}

	public String getEducationLevel(){
		return educationLevel;
	}

	public String getBirthHrs(){
		return birthHrs;
	}

	public String getNameOfFatherGuardian(){
		return nameOfFatherGuardian;
	}

	public String getAltFatherGurdianTitle(){
		return altFatherGurdianTitle;
	}

	public String getCompany(){
		return company;
	}

	public String getExpectations(){
		return expectations;
	}

	public String getMonthlyIncome(){
		return monthlyIncome;
	}

	public String getParentalAddress(){
		return parentalAddress;
	}

	public String getBirthMins(){
		return birthMins;
	}

	public String getGotra(){
		return gotra;
	}

	public String getHeightInFeet(){
		return heightInFeet;
	}

	public String getComplexion(){
		return complexion;
	}

	public String getBroUnmarried(){
		return broUnmarried;
	}

	public String getFullName(){
		return fullName;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public int getUserId(){
		return userId;
	}

	public String getBirthDate(){
		return birthDate;
	}

	public String getParentalEmail(){
		return parentalEmail;
	}

	public String getAltParentalAddress(){
		return altParentalAddress;
	}

	public String getBroMarried(){
		return broMarried;
	}

	public String getSisMarried(){
		return sisMarried;
	}

	public String getQualification(){
		return qualification;
	}

	public String getHeightInInches(){
		return heightInInches;
	}

	public String getJobLocation(){
		return jobLocation;
	}

	public String getMBirthDate(){
		return mBirthDate;
	}

	public String getTaluka(){
		return taluka;
	}

	public String getDistrict(){
		return district;
	}

	public String getAltNameOfFatherGuardian(){
		return altNameOfFatherGuardian;
	}

	public int getCandidateId(){
		return candidateId;
	}

	public String getApplicationId(){
		return applicationId;
	}

	public int getAge(){
		return age;
	}
}