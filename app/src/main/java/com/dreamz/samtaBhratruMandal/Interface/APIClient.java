package com.dreamz.samtaBhratruMandal.Interface;

import com.dreamz.samtaBhratruMandal.Models.ApprovedListDTO;
import com.dreamz.samtaBhratruMandal.Models.BannerDTO;
import com.dreamz.samtaBhratruMandal.Models.BookmarksModel;
import com.dreamz.samtaBhratruMandal.Models.LogoutDTO;
import com.dreamz.samtaBhratruMandal.Models.RecievedIntrestDTO;
import com.dreamz.samtaBhratruMandal.Models.ResponseLocation;
import com.dreamz.samtaBhratruMandal.Models.ResponseLogin;
import com.dreamz.samtaBhratruMandal.Models.ResponseMatchMaking;
import com.dreamz.samtaBhratruMandal.Models.ResponsePayment;
import com.dreamz.samtaBhratruMandal.Models.SearchResultDTO;
import com.dreamz.samtaBhratruMandal.Models.SentInterestDTO;
import com.dreamz.samtaBhratruMandal.Models.TimeSlotsDto;
import com.dreamz.samtaBhratruMandal.Models.UploadImageSuccessDTO;
import com.dreamz.samtaBhratruMandal.Models.UserImageDTO;
import com.dreamz.samtaBhratruMandal.Models.UserProfileDto;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIClient {

    @POST("user/Login")
    Call<ResponseLogin> login(
            @Body JsonObject jsonObject);

    @POST("user/Logout")
    Call<LogoutDTO> logout(
            @Body JsonObject jsonObject);

    @GET("Banners/GetAllBanners")
    Call<ArrayList<BannerDTO>> getBanners();

    @GET("general/GetCategoryList/{UserId}")
    Call<ArrayList<String>> getCategory(
            @Path("UserId") int UserId
    );

    @GET("general/GetBirthYearList")
    Call<ArrayList<String>> getBirthYear();

    @GET("general/GetBloodGroupList")
    Call<ArrayList<String>> getBloodGroup();

    @GET("general/GetHeightList")
    Call<ArrayList<String>> getHeight();

    @GET("general/GetEducationList")
    Call<ArrayList<String>> getQualification();

    @GET("general/GetEducationLevelList")
    Call<ArrayList<String>> getEducationLevel();

    @GET("general/GetLocationList/")
    Call<ResponseLocation> getLocation();

    @GET("general/GetGotraList")
    Call<ArrayList<String>> getGotra();

    @GET("general/GetProfessionList")
    Call<ArrayList<String>> getProfession();

    @GET("general/GetVarnaList")
    Call<ArrayList<String>> getVarna();

    @POST("candidate/GetCandidatesSearchResultByFilterByCandidateType")
    Call<SearchResultDTO> getSearchResult(
            @Query("loggedinUserId") int userId,
            @Body JsonObject jsonObject
    );

    @GET("CandidateInterest/GetInterestsByCandidateId/{candidateId}")
    Call<ArrayList<SentInterestDTO>> getSentInterest(
            @Path("candidateId") int candidId
    );

    @GET("CandidateInterest/GetRecievedInterestsByCandidateId/{candidateId}")
    Call<ArrayList<RecievedIntrestDTO>> getRecievedInterest(
            @Path("candidateId") int candidId
    );

    @GET("CandidateInterest/GetInterestApprovedListByCandidateId/{loggedinUserCandidateId}")
    Call<ArrayList<ApprovedListDTO>> getApprovedList(
            @Path("loggedinUserCandidateId") String candidId
    );

    @GET("candidate/GetCandidatesProfileDetailsByUserId/{UserId}")
    Call<UserProfileDto> getUserDetail(
            @Path("UserId") String UserId
    );

    @GET("UserDocument/GetUserDocumentsByCategory")
    Call<ArrayList<UserImageDTO>> getUserImages(
            @Query("UserId") String userId,
            @Query("Category") String category
    );

    @Multipart
    @POST("UserDocument/uploadImages/upload")
    Call<UploadImageSuccessDTO> uploadPhoto(
            @Query("UserId") String userId,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("UserDocument/uploadDocument/upload")
    Call<UploadImageSuccessDTO> uploadBioData(
            @Query("UserId") String userId,
            @Part MultipartBody.Part file
    );

    @POST("CandidatePayment/AddCandidatePaymentDetails")
    Call<ResponsePayment> putPaymentDone(
            @Body JsonObject jsonObject
    );

    @POST("Meetings/GetAvailableTimeSlots")
    Call<TimeSlotsDto> getTimeSlots(
            @Body JsonObject jsonObject
    );


    @GET("BookMark/GetBookMarkedCandidatesByUserId/{userId}")
    Call<ArrayList<BookmarksModel>> getBookmarksList(
            @Path("userId") String userId
    );


    @FormUrlEncoded
    @POST("/v1/match_ashtakoot_points")
    Call<ResponseMatchMaking> getMatchingPoints(
            @Header("Authorization") String authkey,
            @Field("m_day") int m_day,
            @Field("m_month") int m_month,
            @Field("m_year") int m_year,
            @Field("m_hour") int m_hour,
            @Field("m_min") int m_min,
            @Field("m_lat") float m_lat,
            @Field("m_lon") float m_lon,
            @Field("m_tzone") float m_tzone,
            @Field("f_day") int f_day,
            @Field("f_month") int f_month,
            @Field("f_year") int f_year,
            @Field("f_hour") int f_hour,
            @Field("f_min") int f_min,
            @Field("f_lat") float f_lat,
            @Field("f_lon") float f_lon,
            @Field("f_tzone") float f_tzone
    );

    @POST("Meetings/ScheduleMeeting")
    Call<ResponsePayment> scheduleMeeting(
            @Body JsonObject jsonObject);

    @POST("UserDocument/DeleteUserDocument/{documentId}")
    Call<ResponsePayment> deleteUserDocument(
            @Path("documentId") String documentId);

    @POST("user/DeActivateUserAccount/{UserId}")
    Call<ResponsePayment> deActivateAccount(
            @Path("UserId") String UserId);
}

