package com.dreamz.samtaBhratruMandal.Models;

public class ScheduleMettingModel {
    private Integer meetingId;
    private Integer candidateId;
    private Integer meetingWithCandidateId;
    private String meetingDate;
    private String timeSlot;
    private String meetingURL;
    private String meetingAccepted;
    private String meetingAcceptedDate;
    private String fullName;
    private String birthdate;
    private String education;
    private String profileImage;
    private int age;
    private String mobileNumber;
    private Integer userId;


    public ScheduleMettingModel(Integer meetingId, Integer candidateId,Integer userId, Integer meetingWithCandidateId, String meetingDate, String timeSlot, String meetingURL, String meetingAccepted, String meetingAcceptedDate, String fullName, String birthdate, String education, String profileImage, int age, String mobileNumber) {
        this.meetingId = meetingId;
        this.candidateId = candidateId;
        this.userId=userId;
        this.meetingWithCandidateId = meetingWithCandidateId;
        this.meetingDate = meetingDate;
        this.timeSlot = timeSlot;
        this.meetingURL = meetingURL;
        this.meetingAccepted = meetingAccepted;
        this.meetingAcceptedDate = meetingAcceptedDate;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.education = education;
        this.profileImage = profileImage;
        this.age = age;
        this.mobileNumber = mobileNumber;
    }

    public ScheduleMettingModel(Integer meetingId, Integer candidateId, Integer meetingWithCandidateId, String meetingDate, String timeSlot, String meetingURL, String meetingAccepted, String meetingAcceptedDate) {
        this.meetingId = meetingId;
        this.candidateId = candidateId;
        this.meetingWithCandidateId = meetingWithCandidateId;
        this.meetingDate = meetingDate;
        this.timeSlot = timeSlot;
        this.meetingURL = meetingURL;
        this.meetingAccepted = meetingAccepted;
        this.meetingAcceptedDate = meetingAcceptedDate;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getMeetingWithCandidateId() {
        return meetingWithCandidateId;
    }

    public void setMeetingWithCandidateId(Integer meetingWithCandidateId) {
        this.meetingWithCandidateId = meetingWithCandidateId;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getMeetingURL() {
        return meetingURL;
    }

    public void setMeetingURL(String meetingURL) {
        this.meetingURL = meetingURL;
    }

    public String getMeetingAccepted() {
        return meetingAccepted;
    }

    public void setMeetingAccepted(String meetingAccepted) {
        this.meetingAccepted = meetingAccepted;
    }

    public String getMeetingAcceptedDate() {
        return meetingAcceptedDate;
    }

    public void setMeetingAcceptedDate(String meetingAcceptedDate) {
        this.meetingAcceptedDate = meetingAcceptedDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
