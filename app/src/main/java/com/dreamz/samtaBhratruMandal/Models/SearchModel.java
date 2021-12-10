package com.dreamz.samtaBhratruMandal.Models;

public class SearchModel {
    public String sImageUrl;
    public String sFirstName;
    public String sQualification;
    public String syear;
    public String CandidId;

    public SearchModel(String sImageUrl, String sFirstName, String sQualification, String syear, String candidId) {
        this.sImageUrl = sImageUrl;
        this.sFirstName = sFirstName;
        this.sQualification = sQualification;
        this.syear = syear;
        CandidId = candidId;
    }
}
