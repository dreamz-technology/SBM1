package com.dreamz.samtaBhratruMandal.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Adapters.BookMarksAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.BookmarksModel;
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

public class BookMarks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookMarksAdapter bookMarksAdapter;
    private List<BookmarksModel> bookMarksList;
    ShowLoader showLoader;
    UserSessionManager session;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmarks_toolbar);
        toolbar.setTitle("BookMarks Candidate");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        showLoader = ShowLoader.getProgressDialog(BookMarks.this);
        recyclerView = findViewById(R.id.recycler_bookmarks);
        bookMarksList = new ArrayList<>();

        session = new UserSessionManager(this);
        HashMap<String, Object> user = session.getUserDetails();
        userid = String.valueOf(user.get(UserSessionManager.USERID));


        ApiCall(userid);

        setRecyclerView();
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        bookMarksAdapter = new BookMarksAdapter(this, bookMarksList, new BookMarksAdapter.GetUserList() {
            @Override
            public void getList() {
                ApiCall(userid);
            }
        });
        recyclerView.setAdapter(bookMarksAdapter);
    }

    private void ApiCall(String userid) {

        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<BookmarksModel>> call = api.getBookmarksList(userid);
            call.enqueue(new Callback<ArrayList<BookmarksModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BookmarksModel>> call, Response<ArrayList<BookmarksModel>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body() != null && response.body().size() > 0) {
                            bookMarksList.clear();
                            bookMarksList.addAll(response.body());
                            bookMarksAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "No user bookmarked", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BookmarksModel>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (showLoader != null) {
            showLoader.dismiss();
        }
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
}