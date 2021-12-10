package com.dreamz.samtaBhratruMandal.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dreamz.samtaBhratruMandal.Activities.LoginPage;
import com.dreamz.samtaBhratruMandal.Activities.MainActivity;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;

    Editor editor;

    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";


    public static final String USERID = "userID";
    public static final String CANDIDID = "candidateID";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String BIRTH_DATE = "birthDate";
    public static final String BIRTH_HRS = "birthHrs";
    public static final String BIRTH_MINS = "birthMins";
    public static final String BIRTH_PLACE = "birthPlace";
    public static final String PROFILE_IMG = "profileImg";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String GENDER = "gender";
    // Constructor
    public UserSessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String userId, String candidateId,
                                       String birthDate, String birthHrs, String birthMins,
                                       String birthPlace, String profileImg, String email,
                                       String mobile, String gender){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(USERID,userId);
        editor.putString(CANDIDID,candidateId);
        editor.putString(BIRTH_DATE,birthDate);
        editor.putString(BIRTH_HRS,birthHrs);
        editor.putString(BIRTH_MINS,birthMins);
        editor.putString(BIRTH_PLACE,birthPlace);
        editor.putString(PROFILE_IMG,profileImg);
        editor.putString(EMAIL,email);
        editor.putString(MOBILE,mobile);
        editor.putString(GENDER,gender);

        // commit changes
        editor.commit();
    }

    public void deviceToken(String token) {
        editor.putString(DEVICE_TOKEN,token);
        editor.commit();
    }

    public void createUserCandidateId(String ID) {
        editor.putString(CANDIDID,ID);
        editor.commit();
    }

    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginPage.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);


            return true;
        }
        else {
            context.startActivity(new Intent(context, MainActivity.class));

        }
        return false;
    }

    public HashMap<String, Object> getUserDetails(){
        //Use hashmap to store user credentials
        HashMap<String, Object> user = new HashMap<String, Object>();
        // user name
     //   user.put(NAME, pref.getString(NAME, null));

        user.put(USERID,pref.getString(USERID,""));
        user.put(CANDIDID,pref.getString(CANDIDID,""));
        user.put(BIRTH_DATE,pref.getString(BIRTH_DATE,""));
        user.put(BIRTH_HRS,pref.getString(BIRTH_HRS,""));
        user.put(BIRTH_MINS,pref.getString(BIRTH_MINS,""));
        user.put(BIRTH_PLACE,pref.getString(BIRTH_PLACE,""));
        user.put(PROFILE_IMG,pref.getString(PROFILE_IMG,""));
        user.put(EMAIL, pref.getString(EMAIL, ""));
        user.put(MOBILE, pref.getString(MOBILE, ""));
        user.put(GENDER,pref.getString(GENDER,""));
        user.put(DEVICE_TOKEN,pref.getString(DEVICE_TOKEN,""));

        // return user
        return user;
    }

    public HashMap<String,String> getUserId(){
        HashMap<String, String> candidate = new HashMap<String, String>();
        candidate.put(CANDIDID,pref.getString(CANDIDID,""));
        return candidate;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginPage.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}