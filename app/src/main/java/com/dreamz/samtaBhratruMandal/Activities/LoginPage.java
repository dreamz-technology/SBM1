package com.dreamz.samtaBhratruMandal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ResponseLogin;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;

import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    private EditText userEmail, password;
    private AppCompatButton Login, NewUser;
    private TextView ForgotPass;
    private ShowLoader showLoader;
    StringEntity entity;
    JsonObject jsonObj;
    private String deviceToken = null;

    UserSessionManager session;
    AsyncHttpClient client = new AsyncHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Init();
        ClickListener();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                try {
                    deviceToken = task.getResult();
                    session.deviceToken(task.getResult());
                    Log.d("token", "Refreshed token: " + task.getResult());
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

            }
        });
    }

    private void Init() {
        showLoader = ShowLoader.getProgressDialog(LoginPage.this);

        session = new UserSessionManager(this);
        userEmail = findViewById(R.id.login_userName);
        password = findViewById(R.id.login_password);
        Login = findViewById(R.id.login_button);
        ForgotPass = findViewById(R.id.txt_forgot_pass);
        NewUser = findViewById(R.id.txt_new_user);

    }


    private void ClickListener() {
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(userEmail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {

                    API_Call(jsonBody());

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your email password", Toast.LENGTH_LONG);
                }
            }
        });

        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });
        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterPage.class));
            }
        });

    }

    private JsonObject jsonBody() {
        jsonObj = new JsonObject();
        jsonObj.addProperty("email", userEmail.getText().toString().trim());
        jsonObj.addProperty("password", password.getText().toString().trim());
        jsonObj.addProperty("deviceTokenId", deviceToken);
        return jsonObj;
    }

    private void API_Call(JsonObject jsonObject) {
        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            //hidekeyboard();//pending
            APIClient api = ApiFactory.create();
            Call<ResponseLogin> call = api.login(jsonObject);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body().getResult().equals("Success")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            if (!response.body().isHasPaid()) {
                                Intent intent = new Intent(getApplicationContext(), NotPaidScreen.class);
                                if (response.body().getFullName() != null)
                                    intent.putExtra("name", response.body().getFullName());
                                if (response.body().getMobileNo() != null)
                                    intent.putExtra("mobile", response.body().getMobileNo());
                                if (response.body().getEmail() != null)
                                    intent.putExtra("email", response.body().getEmail());
                                if (response.body().getApplicationId() != null)
                                    intent.putExtra("appId", response.body().getApplicationId());
                                if (password.getText().length() > 0)
                                    intent.putExtra("password", password.getText().toString().trim());
                                if (!String.valueOf(response.body().getPaymentRequired()).equals(""))
                                    intent.putExtra("charges", String.valueOf(response.body().getPaymentRequired()));
                                if (response.body().getCandidateId() > 0) {
                                    intent.putExtra("CandidateId", response.body().getCandidateId());
                                }

                                startActivity(intent);
                                // finish();
                                // finishAffinity();

                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                finishAffinity();

                                if (response.body().getProfileImagePath() != null) {

                                    session.createUserLoginSession(String.valueOf(response.body().getUserId()), String.valueOf(response.body().getCandidateId()),
                                            response.body().getBirthDate(), response.body().getBirthHrsForKundali(), response.body().getBirthMinsForKundali(),
                                            response.body().getBirthPlace(),
                                            "https://staging.samatabhratrumandal.com/" + subString(response.body().getProfileImagePath(), 2),
                                            response.body().getEmail(), response.body().getMobileNo(), response.body().getGender());

                                } else {
                                    session.createUserLoginSession(String.valueOf(response.body().getUserId()), String.valueOf(response.body().getCandidateId()),
                                            response.body().getBirthDate(), response.body().getBirthHrsForKundali(), response.body().getBirthMinsForKundali(),
                                            response.body().getBirthPlace(),
                                            "",
                                            response.body().getEmail(), response.body().getMobileNo(), response.body().getGender());

                                }
                            }

                        }
                        if (response.body().getResult().equals("Failed")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connecton", Toast.LENGTH_SHORT).show();
        }

    }

    private String subString(String profileImagePath, int i) {
        return profileImagePath.substring(i);
    }

}