package com.dreamz.samtaBhratruMandal.Activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Utils.ProjectUtils;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;

public class SplashScreen extends AppCompatActivity {

    private UserSessionManager session;
    private ImageView splashLogo1,splashGanpati,splashHeart;
    private LottieAnimationView animationView;
    private ShowLoader showLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Init();

        new CountDownTimer(5000,1000) {

            /** This method will be invoked on finishing or expiring the timer */
            @Override
            public void onFinish() {

                if (ProjectUtils.isConnectingToInternet(SplashScreen.this)) {
                    /** Creates an intent to start new activity */
                    session.checkLogin();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Connect To Internet And Try Again Later",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }


    private void Init() {
        showLoader = ShowLoader.getProgressDialog(SplashScreen.this);
        session = new UserSessionManager(this);
        splashLogo1 = findViewById(R.id.splash_logo);
        splashGanpati = findViewById(R.id.splash_ganpati);
        splashHeart = findViewById(R.id.image_heart);
    }
}