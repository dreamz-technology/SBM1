package com.dreamz.samtaBhratruMandal.config;

import android.content.Context;

import com.google.firebase.FirebaseApp;

import androidx.multidex.MultiDexApplication;
//import com.zoho.salesiqembed.ZohoSalesIQ;

public class App extends MultiDexApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        FirebaseApp.initializeApp(context);
    }

    public static Context getContext() {
        return context;
    }

}
