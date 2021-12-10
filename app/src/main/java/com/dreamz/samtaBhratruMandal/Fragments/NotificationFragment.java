package com.dreamz.samtaBhratruMandal.Fragments;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.GetAllNotificationsList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Adapters.NotificationAdapter;
import com.dreamz.samtaBhratruMandal.Models.NotificationModel;
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


public class NotificationFragment extends Fragment {
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationModelList;
    private RecyclerView recyclerView;
    private ShowLoader showLoader;
    private UserSessionManager session;
    private String userid;
    public NotificationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationAdapter = new NotificationAdapter(getContext());
        notificationModelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.notification_recycler);
        showLoader = ShowLoader.getProgressDialog(getContext());
        session = new UserSessionManager(getContext());
        HashMap<String, Object> user = session.getUserDetails();
        userid = String.valueOf(user.get(UserSessionManager.USERID));

        API_Call();

        return view;
    }

    private void API_Call() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params;
        params = new RequestParams();
        showLoader.show();
        client.get(GetAllNotificationsList+userid,
                new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        showLoader.hide();
                        JSONArray array = null;
                        try {
                            array = new JSONArray(responseString);
                            Log.d("TAG", " Response is:-" + responseString);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                notificationModelList.add(new NotificationModel(
                                        product.getString("id"),
                                        product.getString("title"),
                                        product.getString("message")));
                                recyclerView.setAdapter(notificationAdapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                notificationAdapter.addItem(notificationModelList);
                                notificationModelList.clear();
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