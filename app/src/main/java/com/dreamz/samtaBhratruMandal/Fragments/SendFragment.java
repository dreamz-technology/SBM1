package com.dreamz.samtaBhratruMandal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Adapters.SendInterestAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.SentInterestDTO;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendFragment extends Fragment {
    int candidId;
    private RecyclerView recyclerView;
    private SendInterestAdapter adapter;
    private ArrayList<SentInterestDTO> sentInterestList;
    private ShowLoader showLoader;
    private UserSessionManager session;

    public SendFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send, container, false);
        recyclerView = view.findViewById(R.id.send_interest_recycler);
        sentInterestList = new ArrayList<>();
        showLoader = ShowLoader.getProgressDialog(getContext());

        session = new UserSessionManager(getContext());
        HashMap<String, Object> user = session.getUserDetails();
        candidId = Integer.parseInt(user.get(UserSessionManager.CANDIDID).toString());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        API_Call(candidId);

        return view;
    }


    private void API_Call(int candidId) {
        if (serverConstants.isConnectingToInternet(getContext())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<SentInterestDTO>> call = api.getSentInterest(candidId);
            call.enqueue(new Callback<ArrayList<SentInterestDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<SentInterestDTO>> call, Response<ArrayList<SentInterestDTO>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (response.body().size() > 0) {
                            sentInterestList.clear();
                            sentInterestList.addAll(response.body());
                            adapter = new SendInterestAdapter(getContext(), sentInterestList, new SendInterestAdapter.GetListAgainAfteBookmark() {
                                @Override
                                public void getList() {
                                    API_Call(candidId);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No Sent Invite Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SentInterestDTO>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}