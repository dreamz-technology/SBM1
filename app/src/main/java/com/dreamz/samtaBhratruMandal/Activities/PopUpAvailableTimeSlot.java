package com.dreamz.samtaBhratruMandal.Activities;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.ScheduleMeeting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ResponseLogin;
import com.dreamz.samtaBhratruMandal.Models.ResponsePayment;
import com.dreamz.samtaBhratruMandal.Models.TimeSlotsDto;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopUpAvailableTimeSlot extends AppCompatActivity {

    String CandidateId;
    StringEntity entity;
    Gson gson = new Gson();
    String date;
    DatePicker picker;
    TextView Send, Submit;
    private ShowLoader showLoader;
    Spinner spinTimeSlots;
    List<String> timeSlotList = new ArrayList<>();
    List<String> tempTimeSlotList = new ArrayList<>();
    String selectedTime = "";
    JsonObject obj;
    boolean isTimeSlotChecked = false;
    String url = "https://staging.samatabhratrumandal.com/RestAPI/api/Meetings/GetAvailableTimeSlots";

    UserSessionManager session;
    int UserCandidateId;
    ArrayAdapter aa0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_available_time_slot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.available_toolbar);
        toolbar.setTitle("Schedule Meeting");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        showLoader = ShowLoader.getProgressDialog(PopUpAvailableTimeSlot.this);

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user1 = session.getUserId();
        UserCandidateId = Integer.parseInt(user1.get(UserSessionManager.CANDIDID));

        Send = findViewById(R.id.btn_send);

        spinTimeSlots = findViewById(R.id.spinn_time_slots);

        aa0 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, timeSlotList);
        aa0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Bundle intentData = getIntent().getExtras();
        //CandidateId = intentData.getString("meetingWithCandidId");
        CandidateId = String.valueOf(getIntent().getIntExtra("meetingWithCandidId", 0));
        picker = (DatePicker) findViewById(R.id.datePicker);
        Submit = findViewById(R.id.btn_submit);

        picker.setMinDate(System.currentTimeMillis() - 1000);

        picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                isTimeSlotChecked = false;
                timeSlotList.clear();
                aa0.notifyDataSetChanged();
                selectedTime = "";
            }
        });

//        displayDate=(Button)findViewById(R.id.button1);
        setClickEvents();

        getReminingTime();

    }

    private void setClickEvents() {

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedTime.equals("")) {
                    date = getCurrentDate().toString();

                    //todo: Schedule Meeting code
                    AsyncHttpClient client = new AsyncHttpClient();
                    // send parameters in json body
                    JsonObject jsonParams = new JsonObject();
                    try {
                        jsonParams.addProperty("meetingWithCandidateId", Integer.valueOf(CandidateId));
                        jsonParams.addProperty("candidateId", UserCandidateId);
                        jsonParams.addProperty("meetingDate", date.toString());
                        jsonParams.addProperty("timeSlot", selectedTime.toString());
                        jsonParams.addProperty("meetingURL", CandidateId + UserCandidateId);

                        entity = new StringEntity(jsonParams.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    API_Call(jsonParams);
                } else {
                    Toast.makeText(PopUpAvailableTimeSlot.this, "Please select avalaible slot time", Toast.LENGTH_SHORT).show();
                }

            }

        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeSlots(jsonObject());
            }
        });

        spinTimeSlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedTime = timeSlotList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private JsonObject jsonObject() {
        obj = new JsonObject();
        obj.addProperty("meetingWithCandidateId", CandidateId);
        obj.addProperty("meetingDate", getCurrentDate().toString());
        return obj;
    }

    private void getTimeSlots(JsonObject jsonObject) {

        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<TimeSlotsDto> call = api.getTimeSlots(jsonObject);
            call.enqueue(new Callback<TimeSlotsDto>() {
                @Override
                public void onResponse(Call<TimeSlotsDto> call, Response<TimeSlotsDto> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body().getAvailableTimeSlots().size() > 0) {
                            timeSlotList.clear();
                            tempTimeSlotList.clear();
                            tempTimeSlotList.addAll(response.body().getAvailableTimeSlots());

                            for (int i = 0; i < tempTimeSlotList.size(); i++) {
                                if (getDayByPicker().equals(getCurrentDay())) {
                                    if (!isTimeSlotChecked) {
                                        if (tempTimeSlotList.get(i).contains(getReminingTime().toUpperCase())) {
                                            timeSlotList.add(tempTimeSlotList.get(i));
                                            isTimeSlotChecked = true;
                                        }
                                    } else {
                                        timeSlotList.add(tempTimeSlotList.get(i));
                                    }
                                } else {
                                    timeSlotList.add(tempTimeSlotList.get(i));
                                }

                            }

                            timeSlotList.add(0, getResources().getString(R.string.time_slot));
                            aa0.notifyDataSetChanged();
                            spinTimeSlots.setSelection(0);
                            spinTimeSlots.setAdapter(aa0);
                        } else {
                            Toast.makeText(getApplicationContext(), "No time slot available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TimeSlotsDto> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(picker.getYear() + "-");
        builder.append((picker.getMonth() + 1) + "-");//month is 0 based
        String day = String.valueOf(picker.getDayOfMonth());
        if (day.length() == 1) {
            day = 0 + day;
        }
        builder.append(day);
        return builder.toString();
    }

    public String getDayByPicker() {
        StringBuilder builder = new StringBuilder();
        builder.append(picker.getYear() + "-");
        builder.append((picker.getMonth() + 1) + "-");//month is 0 based
        String day = String.valueOf(picker.getDayOfMonth());
        if (day.length() == 1) {
            day = 0 + day;
        }
        builder.append(day);
        return day;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void API_Call(JsonObject jsonObject) {
        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            //hidekeyboard();//pending
            APIClient api = ApiFactory.create();
            Call<ResponsePayment> call = api.scheduleMeeting(jsonObject);
            call.enqueue(new Callback<ResponsePayment>() {
                @Override
                public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body().getResult().equals("Success")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (response.body().getResult().equals("Failed")) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<ResponsePayment> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connecton", Toast.LENGTH_SHORT).show();
        }

    }

    private String getReminingTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        String delegate = "hh aaa";
        return (String) DateFormat.format(delegate, calendar.getTime());
    }

    private String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(calendar.getTime());
    }
}