package com.dreamz.samtaBhratruMandal.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ResponsePayment;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;

public class FullImageShowActivity extends AppCompatActivity {

    private ImageView imageView, ivDelete;
    private String imageUrl,documentId;
    private ShowLoader showLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_show);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.iv_user_image);
        ivDelete = findViewById(R.id.iv_delete);

        showLoader = ShowLoader.getProgressDialog(FullImageShowActivity.this);

        if (getIntent() != null) {
            imageUrl = getIntent().getStringExtra("imageUrl");
            documentId=getIntent().getStringExtra("imageId");
        }

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .fitCenter()
                .placeholder(R.drawable.samta_logo)
                .into(imageView);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDocument(documentId);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void deleteDocument(String documentId) {
        if (serverConstants.isConnectingToInternet(FullImageShowActivity.this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ResponsePayment> call = api.deleteUserDocument(documentId);
            call.enqueue(new Callback<ResponsePayment>() {
                @Override
                public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(FullImageShowActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                        Toast.makeText(FullImageShowActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePayment> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(FullImageShowActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}