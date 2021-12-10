
package com.dreamz.samtaBhratruMandal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.dreamz.samtaBhratruMandal.Fragments.AboutUsFragment;
import com.dreamz.samtaBhratruMandal.Fragments.HomeFragment;
import com.dreamz.samtaBhratruMandal.Fragments.InterestFragment;
import com.dreamz.samtaBhratruMandal.Fragments.NotificationFragment;
import com.dreamz.samtaBhratruMandal.Fragments.SearchFilterFragment;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.LogoutDTO;
import com.dreamz.samtaBhratruMandal.Models.ResponseLogin;
import com.dreamz.samtaBhratruMandal.Models.ResponsePayment;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.dreamz.samtaBhratruMandal.dialog.DialogConfirmYesNo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Toolbar toolbar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private UserSessionManager session;
    private ShowLoader showLoader;
    String candidateId, deviceToken,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //For FireBase Messaging
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete( Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        Toast.makeText(MainActivity.this, "Message Error", Toast.LENGTH_SHORT).show();
//                    }
//                });

        init();
        setDrawer();
    }

    private void init() {
        showLoader = ShowLoader.getProgressDialog(MainActivity.this);
        session = new UserSessionManager(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        HashMap<String, Object> user = session.getUserDetails();
        candidateId = String.valueOf(user.get(UserSessionManager.CANDIDID));
        deviceToken = String.valueOf(user.get(UserSessionManager.DEVICE_TOKEN));
        userId = String.valueOf(user.get(UserSessionManager.USERID));

    }

    private void setDrawer() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitle("DashBoard");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Create Home Fragment object
        HomeFragment homeFragment = new HomeFragment();

        setMyFragment(homeFragment);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            setMyFragment(homeFragment);
            toolbar.setTitle("Samata Bhratru Mandal");
        } /*else if(id==R.id.profile){
            ProfileActivity profileActivity= new ProfileActivity();
            setMyFragment(profileActivity);
            toolbar.setTitle("Profile");
        }*/ else if (id == R.id.notification) {
            NotificationFragment notificationFragment = new NotificationFragment();
            setMyFragment(notificationFragment);
            toolbar.setTitle("Messages");
        } else if (id == R.id.search) {
            SearchFilterFragment searchFragment = new SearchFilterFragment();
            setMyFragment(searchFragment);
            toolbar.setTitle("Search");
        } else if (id == R.id.interest) {
            InterestFragment interestFragment = new InterestFragment();
            setMyFragment(interestFragment);
            toolbar.setTitle("Account Interest");
        } else if (id == R.id.biodata) {
            Intent intent = new Intent(MainActivity.this, UploadBioDataActivity.class);
            startActivity(intent);
        } else if (id == R.id.profile_visible) {
            openProfileVisibleDialog();
        } else if (id == R.id.about_us) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            setMyFragment(aboutUsFragment);
            toolbar.setTitle("About");
        } else if (id == R.id.signout) {
            session.logoutUser();
            API_Call(jsonBody());

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setMyFragment(Fragment fragment) {
        //get current fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        fragmentTransaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private JsonObject jsonBody() {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("candidateId", candidateId);
        jsonObj.addProperty("deviceToken", deviceToken);
        return jsonObj;
    }

    private void API_Call(JsonObject jsonObject) {
        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            //hidekeyboard();//pending
            APIClient api = ApiFactory.create();
            Call<LogoutDTO> call = api.logout(jsonObject);
            call.enqueue(new Callback<LogoutDTO>() {
                @Override
                public void onResponse(Call<LogoutDTO> call, Response<LogoutDTO> response) {
                    showLoader.hide();


                }

                @Override
                public void onFailure(Call<LogoutDTO> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connecton", Toast.LENGTH_SHORT).show();
        }

    }

    public void openProfileVisibleDialog() {
        final DialogConfirmYesNo dialog = new DialogConfirmYesNo();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Are you sure want to deactivate your account?");
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "");
        dialog.setOnClickListener(new DialogConfirmYesNo.OnClickListener() {
            @Override
            public void onClickYes() {
                deactivateAccount(userId);
            }

            @Override
            public void onClickNo() {
                dialog.dismiss();
            }
        });
    }

    private void deactivateAccount(String userId) {
        if (serverConstants.isConnectingToInternet(MainActivity.this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ResponsePayment> call = api.deActivateAccount(userId);
            call.enqueue(new Callback<ResponsePayment>() {
                @Override
                public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        API_Call(jsonBody());
                        session.logoutUser();
                        finishAffinity();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePayment> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}