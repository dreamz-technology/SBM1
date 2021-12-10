package com.dreamz.samtaBhratruMandal.Server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dreamz.samtaBhratruMandal.BuildConfig;
import com.loopj.android.http.Base64;

import java.io.UnsupportedEncodingException;

import androidx.core.os.BuildCompat;

public class serverConstants {

    //live
   // public static final String Base_url=
    public static final String shareUrl= BuildConfig.SHARE_URL;
    public static final String Base_url= BuildConfig.SERVER_URL;
    public static final String imageBaseUrl=BuildConfig.IMAGE_SERVER_URL;
    //public static final String imageBaseUrl="https://samatabhratrumandal.com";
    //public static final String Base_url="https://samatabhratrumandal.com/RestAPI/tabhratrumandal.com/RestAPI/api/";//"https://staging.samatabhratrumndal.com/RestAPI/api/";
    //https://api/";
    //    //public static final String Base_url = "https://staging.samastaging.samatabhratrumandal.com/RestAPI/api/

    public static final String Banner_url = Base_url + "Banners/GetAllBanners/"; //Base_url+"Banners/GetAllBaners";
    public static final String AppVErsion = Base_url + "APIVersion/GetAppVersion";

    public static final String GetBasicDetailsByCandidateId = Base_url + "candidate/GetCandidatesProfileDetailsByCandidateId/";
    public static final String GetAllCandidateSearchResult = Base_url + "candidate/GetAllCandidatesSearchResult/";
    public static final String UserLogin = Base_url + "user/Login/"; //Base_url+"user/Login/";
    public static final String ForgotPass = Base_url + "user/ChangePassword/";
    public static final String GetUserByEmail = Base_url + "user/GetUserByEmail/";
    public static final String GetBasicDetailsByUserId = Base_url + "candidate/GetCandidatesProfileDetailsByUserId/";
    public static final String GetUserByUserId = Base_url + "user/GetUserByUserId/";
    public static final String GetFilteredResult = Base_url + "candidate/GetCandidatesSearchResultByFilterByCandidateType/";

    //BookMarks
    public static final String AddToBookMarks = Base_url + "BookMark/AddCandidateBookMark/";
    public static final String GetAllBookMarksList = Base_url + "BookMark/GetBookMarkedCandidatesByUserId/";
    public static final String DeleteFromBookMarks = Base_url + "BookMark/DeleteCandidateBookMark/";

    //Astrology Url
    public static final String AstroUrl = "https://api.vedicrishiastro.com/v1/advanced_panchang";
    // https://github.com/astrologyapi/astro-api-client  for testing regardes response and params

    //Spinner Item List Urls
    public static final String GetAllLocationList = Base_url + "general/GetLocationList/";
    public static final String GetAllGotraList = Base_url + "general/GetGotraList";// Banner_url+"general/GetGotraList;
    public static final String GetAllProfessionList = Base_url + "general/GetProfessionList/";
    public static final String GetAllBloodGroupList = Base_url + "general/GetBloodGroupList/";
    public static final String GetAllEducationList = Base_url + "general/GetEducationList/";
    public static final String GetAllBirthYearList = Base_url + "general/GetBirthYearList/";
    public static final String GetAllHeightList = Base_url + "general/GetHeightList/";

    //Notification Urls
    public static final String AddNotificationToServer = Base_url + "Notifications/AddNotification/";
    public static final String GetAllNotificationsList = Base_url + "Notifications/GetAllNotificationsByUserId/";

    //Interest Urls
    public static final String SendInterest = Base_url + "CandidateInterest/AddCandidateInterest/";
    public static final String RemoveInterest = Base_url + "CandidateInterest/RemoveCandidateInterest/";
    public static final String SendInterestList = Base_url + "CandidateInterest/GetInterestsByCandidateId/";
    public static final String ReceiveInterestList = Base_url + "CandidateInterest/GetRecievedInterestsByCandidateId/";
    public static final String ApprovedInterestList = Base_url + "CandidateInterest/AcceptCandidateInterest/";
    public static final String ApprovedLIst = Base_url + "CandidateInterest/GetInterestApprovedListByCandidateId/";

    //TimeSchedule...
    public static final String GetAvailableTimeSlots = Base_url + "Meetings/GetAvailableTimeSlots/";
    public static final String GetAllScheduledMeetingsByCandidateiD = Base_url + "Meetings/GetAllScheduledMeetingsByCandidateiD/";
    public static final String GetAllRecievedMeetingInvitationByCandidateiD = Base_url + "Meetings/GetAllRecievedMeetingInvitationByCandidateId/";
    public static final String ScheduleMeeting = Base_url + "Meetings/ScheduleMeeting/";
    public static final String AcceptMeeting = Base_url + "Meetings/AcceptMeeting/";
    public static final String CancelMeeting = Base_url + "Meetings/CancelMeeting/";
    public static final String GetAllScheduledMeetingsByLoggedInUser = Base_url + "Meetings/GetAllScheduledMeetingsByLoggedInUser/";
    private static final String API_USER_NAME = "617966";
    private static final String API_PASSWORD = "aeca785821a1a7040303a51d0b1045f8";
    public static final String apiAstrologyUrl = "https://json.astrologyapi.com";

    public static Boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    public static int getDay(String date) {

        String[] dateParts = date.split("-");
        return Integer.parseInt(dateParts[0]);
    }

    public static int getMonth(String date) {
        String[] dateParts = date.split("-");
        return Integer.parseInt(dateParts[1]);
    }

    public static int getYear(String date) {
        String[] dateParts = date.split("-");
        return Integer.parseInt(dateParts[2]);
    }

    public static String getAuthToken() {
        byte[] data = new byte[0];
        try {
            data = (serverConstants.API_USER_NAME + ":" + serverConstants.API_PASSWORD).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
