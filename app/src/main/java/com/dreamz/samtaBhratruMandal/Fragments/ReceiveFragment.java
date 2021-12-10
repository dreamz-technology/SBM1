package com.dreamz.samtaBhratruMandal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Adapters.ReceivedInterestAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.RecievedIntrestDTO;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReceiveFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReceivedInterestAdapter receivedInterestAdapter;
    private List<RecievedIntrestDTO> receivedInterestList;
    UserSessionManager session;
    int candidId;
    ShowLoader showLoader;

    public ReceiveFragment() {
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
        View view = inflater.inflate(R.layout.fragment_receive, container, false);

        showLoader = ShowLoader.getProgressDialog(getContext());
        session = new UserSessionManager(getContext());

        HashMap<String, Object> user = session.getUserDetails();
        candidId = Integer.parseInt(user.get(UserSessionManager.CANDIDID).toString());

        recyclerView = view.findViewById(R.id.recycler_received_interest);
        receivedInterestList = new ArrayList<>();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        API_Call(candidId);

        return view;
    }

    private void API_Call(int candidId) {
        if(serverConstants.isConnectingToInternet(getContext())){
            showLoader.show();
            APIClient api= ApiFactory.create();
            Call<ArrayList<RecievedIntrestDTO>> call= api.getRecievedInterest(candidId);
            call.enqueue(new Callback<ArrayList<RecievedIntrestDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<RecievedIntrestDTO>> call, Response<ArrayList<RecievedIntrestDTO>> response) {
                    showLoader.hide();
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), response.code()+"", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if(response.body().size()>0){
                            receivedInterestList.clear();
                            receivedInterestList.addAll(response.body());
                            receivedInterestAdapter = new ReceivedInterestAdapter(getContext(), receivedInterestList, new ReceivedInterestAdapter.GetListListner() {
                                @Override
                                public void getList() {
                                    API_Call(candidId);
                                }
                            });
                            recyclerView.setAdapter(receivedInterestAdapter);
//                            receivedInterestAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "No Interests Received", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RecievedIntrestDTO>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        }else{
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}