package com.dreamz.samtaBhratruMandal.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ResponseLogin;
import com.dreamz.samtaBhratruMandal.Models.ResponsePayment;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotPaidScreen extends AppCompatActivity implements PaymentResultListener {


    TextView payNow, tvWelcome, tvMemberId, tvCharges;
    JsonObject jsonObjectPayment, jsonObjectLogin;
    UserSessionManager session;
    ShowLoader showLoader;
    int candidId;
    String charges, email, password, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_paid_screen);

        tvWelcome = findViewById(R.id.tv_welcome);
        payNow = findViewById(R.id.btn_payNow);
        tvMemberId = findViewById(R.id.tv_memberId);
        tvCharges = findViewById(R.id.tv_charges);

        showLoader = ShowLoader.getProgressDialog(this);

        session = new UserSessionManager(this);

      /*  session = new UserSessionManager(this);
        HashMap<String, Object> user = session.getUserDetails();
        candidId = (String) user.get(UserSessionManager.CANDIDID);*/

        candidId=getIntent().getIntExtra("CandidateId",0);

        if (getIntent().getStringExtra("name") != null)
            tvWelcome.setText("Welcome, " + getIntent().getStringExtra("name"));
        else
            tvWelcome.setText("Welcome");
        if (getIntent().getStringExtra("appId") != null)
            tvMemberId.setText(getResources().getString(R.string.logged_in_as_free_member) + " "+getIntent().getStringExtra("appId"));
        if (getIntent().getStringExtra("charges") != null) {
            charges = getIntent().getStringExtra("charges");
            tvCharges.setText("₹: " + getIntent().getStringExtra("charges")+"/-");
        }
        if (getIntent().getStringExtra("email") != null) {
            email = getIntent().getStringExtra("email");
        }
        if (getIntent().getStringExtra("password") != null) {
            password = getIntent().getStringExtra("password");
        }
        if (getIntent().getStringExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
        }


        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPayment(getIntent().getStringExtra("charges"));

            }
        });
    }

    public void startPayment(String amount) {

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Samta Bhratru Mandal");
            options.put("description", "Lifetime Payment");
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", "INR");
            // String payment = amount.getText().toString();
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("phone", phone);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String paymentId) {

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

        //submitPaymentData(paymentId, String.valueOf(149), CommonUtil.getSubscriptionDate());
        ApiCall(jsonObjectPayment(paymentId));
    }

    private JsonObject jsonObjectPayment(String paymentId) {
        jsonObjectPayment = new JsonObject();
        jsonObjectPayment.addProperty("candidateId", candidId);
        jsonObjectPayment.addProperty("transactionId", paymentId);
        jsonObjectPayment.addProperty("paymentStatus", "Success");
        jsonObjectPayment.addProperty("amount", 1000);
        return jsonObjectPayment;
    }

    private JsonObject jsonObjectLogin(String email, String password) {
        jsonObjectLogin = new JsonObject();
        jsonObjectLogin.addProperty("email", email);
        jsonObjectLogin.addProperty("password", password);
        return jsonObjectLogin;
    }

    private void ApiCall(JsonObject jsonObject) {
        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ResponsePayment> call = api.putPaymentDone(jsonObject);
            call.enqueue(new Callback<ResponsePayment>() {
                @Override
                public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body().getResult().equals("Success")) {

                            //hit login api
                            loginApi(jsonObjectLogin(email,password));

                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //finish();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponsePayment> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginApi(JsonObject jsonLogin) {
        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ResponseLogin> call = api.login(jsonLogin);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body().getResult().equals("Success")) {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            finishAffinity();

                            if (response.body().getProfileImagePath() != null) {

                                session.createUserLoginSession(String.valueOf(response.body().getUserId()), String.valueOf(response.body().getCandidateId()),
                                        response.body().getBirthDate(), response.body().getBirthHrsForKundali(), response.body().getBirthMinsForKundali(),
                                        response.body().getBirthPlace(),
                                        "https://staging.samatabhratrumandal.com/" +response.body().getProfileImagePath().toString().substring(2),
                                        response.body().getEmail(), response.body().getMobileNo(), response.body().getGender());

                            } else {
                                session.createUserLoginSession(String.valueOf(response.body().getUserId()), String.valueOf(response.body().getCandidateId()),
                                        response.body().getBirthDate(), response.body().getBirthHrsForKundali(), response.body().getBirthMinsForKundali(),
                                        response.body().getBirthPlace(),
                                        "",
                                        response.body().getEmail(), response.body().getMobileNo(), response.body().getGender());

                            }

                        } else {
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
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        finish();
        Log.e("error", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());

        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}