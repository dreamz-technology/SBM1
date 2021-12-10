package com.dreamz.samtaBhratruMandal.Fragments;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.GetAllScheduledMeetingsByLoggedInUser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Adapters.SendScheduleMeetingAdapter;
import com.dreamz.samtaBhratruMandal.Models.ScheduleMettingModel;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class SendScheduleMettingFragment extends Fragment {
    String userId;
    private RecyclerView recyclerView;
    private SendScheduleMeetingAdapter sendInterestAdapter;
    private List<ScheduleMettingModel> sendInterestModelList;
    private ShowLoader showLoader;
    private UserSessionManager session;

    public SendScheduleMettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receive_schedule_metting, container, false);
        recyclerView = view.findViewById(R.id.recycler_received_interest);
        sendInterestAdapter = new SendScheduleMeetingAdapter(getContext(), new SendScheduleMeetingAdapter.OnClickRefresh() {
            @Override
            public void onRefresh() {
                sendInterestModelList.clear();
                API_Call();
            }
        });
        sendInterestModelList = new ArrayList<>();
        showLoader = ShowLoader.getProgressDialog(getContext());

        session = new UserSessionManager(getContext());
        //    HashMap<String, String> user = session.getUserId();
        //   UserCandidateId = Integer.parseInt(user.get(UserSessionManager.KEY_CANDIDID));

        HashMap<String, Object> user = session.getUserDetails();
        userId = String.valueOf(user.get(UserSessionManager.CANDIDID));


        API_Call();

        return view;
    }

    private void API_Call() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params;
        params = new RequestParams();
        showLoader.show();
        client.get(GetAllScheduledMeetingsByLoggedInUser + userId,
                new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        showLoader.hide();
                        JSONArray array = null;
                        try {
                            array = new JSONArray(responseString);
                            Log.d("TAG", " Response is:-" + responseString);
                            sendInterestModelList.clear();
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject product = array.getJSONObject(i);
                                    sendInterestModelList.add(new ScheduleMettingModel(
                                            product.getInt("meetingId"),
                                            product.getInt("candidateId"),
                                            product.getInt("userId"),
                                            product.getInt("meetingWithCandidateId"),
                                            product.getString("meetingDate"),
                                            product.getString("timeSlot"),
                                            product.getString("meetingURL"),
                                            product.getString("meetingAccepted"),
                                            product.getString("meetingAcceptedDate"),
                                            product.getString("fullName"),
                                            product.getString("birthdate"),
                                            product.getString("education"),
                                            product.getString("profileImage"),
                                            product.getInt("age"),
                                            product.getString("mobileNumber")));
                                    recyclerView.setAdapter(sendInterestAdapter);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(layoutManager);

                                }
                                sendInterestAdapter.addItem(sendInterestModelList);
                            }else{
                                Toast.makeText(getContext(), "No Schedule Meeting Sent", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        showLoader.hide();
                        Log.d("TAG", "Error Response:-" + responseString);
                        if (statusCode == 404) {
                            Toast.makeText(getContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    "Please connect to Internet and try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}