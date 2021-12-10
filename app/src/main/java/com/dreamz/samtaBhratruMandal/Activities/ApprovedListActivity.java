package com.dreamz.samtaBhratruMandal.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Adapters.ApprovedListAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ApprovedListDTO;
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

//Developed by Ankush 04/12/2021

public class ApprovedListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApprovedListAdapter approvedListAdapter;
    private List<ApprovedListDTO> approvedList;
    private ShowLoader showLoader;
    UserSessionManager session ;
    String candidId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.approved_toolbar);
        toolbar.setTitle("Approved Candidate");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
        ApiCall();

        setRecycler();
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        approvedListAdapter = new ApprovedListAdapter(ApprovedListActivity.this,approvedList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(approvedListAdapter);
    }

    private void ApiCall() {
        if(serverConstants.isConnectingToInternet(this)){
            showLoader.show();
            APIClient api= ApiFactory.create();
            Call<ArrayList<ApprovedListDTO>> call=api.getApprovedList(candidId);
            call.enqueue(new Callback<ArrayList<ApprovedListDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<ApprovedListDTO>> call, Response<ArrayList<ApprovedListDTO>> response) {
                    showLoader.hide();
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), response.code()+"", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        if(response.body().size()>0){
                            approvedList.clear();
                            approvedList.addAll(response.body());
                            approvedListAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getApplicationContext(), "No approved user found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ApprovedListDTO>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void init() {
        showLoader = ShowLoader.getProgressDialog(ApprovedListActivity.this);
        session = new UserSessionManager(getApplicationContext());

        HashMap<String, Object> user = session.getUserDetails();
        candidId = String.valueOf(user.get(UserSessionManager.CANDIDID));

        recyclerView = findViewById(R.id.approved_recycler);
        approvedList = new ArrayList<>();

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}