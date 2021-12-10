package com.dreamz.samtaBhratruMandal.Models;

public class ApprovedModel {
    public String ACandidId;
    public String AImageUrl;
    public String AFirstName;
    public String AQualification;
    public String Ayear;
    public String ANumber;

    public ApprovedModel(String ACandidId, String AImageUrl, String AFirstName, String AQualification, String ayear, String ANumber) {
        this.ACandidId = ACandidId;
        this.AImageUrl = AImageUrl;
        this.AFirstName = AFirstName;
        this.AQualification = AQualification;
        Ayear = ayear;
        this.ANumber = ANumber;
    }
}
