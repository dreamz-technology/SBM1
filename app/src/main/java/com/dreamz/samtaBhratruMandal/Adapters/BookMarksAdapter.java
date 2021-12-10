package com.dreamz.samtaBhratruMandal.Adapters;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.DeleteFromBookMarks;
import static com.dreamz.samtaBhratruMandal.Server.serverConstants.SendInterest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Activities.ViewProfileActivity;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.BookmarksModel;
import com.dreamz.samtaBhratruMandal.Models.ResponseMatchMaking;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookMarksAdapter extends RecyclerView.Adapter<BookMarksAdapter.basicClass> {

    List<BookmarksModel> muserList = new ArrayList<>();
    private Context context;
    private String date;
    StringEntity entity;
    UserSessionManager session;
    int UserCandidateId;
    int userid;
    String userGender, userDob, userHr, userMin, selectedDob;
    private GetUserList getUserList;

    public BookMarksAdapter(Context context, List<BookmarksModel> bookMarksList,GetUserList getUserList) {
        this.context = context;
        this.muserList = bookMarksList;
        this.getUserList=getUserList;
    }

    public void addItem(List<BookmarksModel> sliderItem) {
        this.muserList.addAll(sliderItem);
        notifyDataSetChanged();

    }

    @Override
    public BookMarksAdapter.basicClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_mark_layout, null);
        session = new UserSessionManager(context);
        HashMap<String, Object> user = session.getUserDetails();
        userid = Integer.parseInt(String.valueOf(user.get(UserSessionManager.USERID)));

        userGender = user.get(UserSessionManager.GENDER).toString();
        userDob = user.get(UserSessionManager.BIRTH_DATE).toString();
        userHr = user.get(UserSessionManager.BIRTH_HRS).toString();
        userMin = user.get(UserSessionManager.BIRTH_MINS).toString();

        HashMap<String, String> user1 = session.getUserId();
        UserCandidateId = Integer.parseInt(user1.get(UserSessionManager.CANDIDID));

        return new basicClass(inflate);
    }

    @Override
    public void onBindViewHolder(BookMarksAdapter.basicClass holder, @SuppressLint("RecyclerView") int position) {

        if (muserList.get(position).getProfileImage() != null && !muserList.get(position).equals("")) {
            Glide.with(context)
                    .load(serverConstants.imageBaseUrl + (muserList.get(position).getProfileImage().toString()))//"https://staging.samatabhratrumandal.com/"+
                    .fitCenter()
                    .placeholder(R.drawable.noimg)
                    .into(holder.userProf);
        } else {
            holder.userProf.setImageResource(R.drawable.noimg);
        }

       /* if(muserList.get(position).isBookMarked()){
            holder.ivHeart.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            holder.tvBookmark.setText("Unmark");
        }else
        {
            holder.ivHeart.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_heart));
            holder.tvBookmark.setText("Bookmark");
        }*/

        holder.fname.setText(muserList.get(position).getFullName());

        selectedDob = muserList.get(position).getBirthdate().toString();

        if (!String.valueOf(muserList.get(position).getAge()).equals(""))
            holder.age.setText("Age: " + String.valueOf(muserList.get(position).getAge()) + " yrs");

        holder.qualification.setText(context.getResources().getString(R.string.education) + ": " + muserList.get(position).getEducation());

        if (muserList.get(position).isInviteSent()) {
            holder.tvIsInvite.setText("Invite Sent");
        }

        holder.viewProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                // Intent intent = new Intent(view.getContext(), UserDetailsActivity.class);
                intent.putExtra("CandidateId", String.valueOf(muserList.get(position).getCandidateId()));
                intent.putExtra("UserId", String.valueOf(muserList.get(position).getUserId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.imgSendInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (muserList.get(position).isInviteSent()) {

                } else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("senderCandidateId", UserCandidateId);
                        jsonParams.put("recieverCandidateId", muserList.get(position).getCandidateId());
                        entity = new StringEntity(jsonParams.toString());
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    client.post(context, SendInterest, entity, "application/json", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                if (response.getString("result").equals("Success")) {

                                    Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            throwable.printStackTrace();

                        }
                    });
                }
            }
        });

        holder.btnUnmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("userId", userid);
                    jsonParams.put("bookMarkedCandidateId", muserList.get(position).getCandidateId());
                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(context, DeleteFromBookMarks, entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (response.getString("result").equals("Success")) {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                muserList.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });

        holder.imgKundli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userGender.equals("Female")) {
                    if (serverConstants.isConnectingToInternet(context)) {
                        //   showLoader.show();
                        APIClient api = ApiFactory.kundli();
                        Call<ResponseMatchMaking> call = api.getMatchingPoints(serverConstants.getAuthToken(),
                                serverConstants.getDay(selectedDob),
                                serverConstants.getMonth(selectedDob),
                                serverConstants.getYear(selectedDob),
                                Integer.parseInt(muserList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(muserList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f,

                                serverConstants.getDay(userDob),
                                serverConstants.getMonth(userDob),
                                serverConstants.getYear(userDob),
                                Integer.parseInt(userHr), Integer.parseInt(userMin), 21.0077f, 75.5626f, 5.5f);

                        call.enqueue(new Callback<ResponseMatchMaking>() {
                            @Override
                            public void onResponse(Call<ResponseMatchMaking> call, Response<ResponseMatchMaking> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                } else {

                                    Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.kundali_dialog);
                                    TextView msg = dialog.findViewById(R.id.tvScore);
                                    TextView okay = dialog.findViewById(R.id.tv_ok);
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    msg.setText(response.body().getTotal().getReceivedPoints() + " / " + response.body().getTotal().getTotalPoints());
                                    okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.create();
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMatchMaking> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                if (userGender.equals("Male")) {
                    if (serverConstants.isConnectingToInternet(context)) {
                        //   showLoader.show();
                        APIClient api = ApiFactory.kundli();
                        Call<ResponseMatchMaking> call = api.getMatchingPoints(serverConstants.getAuthToken(),
                                serverConstants.getDay(userDob),
                                serverConstants.getMonth(userDob),
                                serverConstants.getYear(userDob),
                                Integer.parseInt(userHr),
                                Integer.parseInt(userMin), 21.0077f, 75.5626f, 5.5f,

                                serverConstants.getDay(selectedDob),
                                serverConstants.getMonth(selectedDob),
                                serverConstants.getYear(selectedDob),
                                Integer.parseInt(muserList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(muserList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f
                        );

                        call.enqueue(new Callback<ResponseMatchMaking>() {
                            @Override
                            public void onResponse(Call<ResponseMatchMaking> call, Response<ResponseMatchMaking> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                } else {
                                    Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.kundali_dialog);
                                    TextView msg = dialog.findViewById(R.id.tvScore);
                                    TextView okay = dialog.findViewById(R.id.tv_ok);
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    msg.setText(response.body().getTotal().getReceivedPoints() + " / " + response.body().getTotal().getTotalPoints());
                                    okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.create();
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMatchMaking> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return muserList.size();
    }

    public class basicClass extends RecyclerView.ViewHolder {
        TextView fname, age, qualification, viewProf, imgKundli, tvBookmark, tvIsInvite;
        ImageView userProf, imgDelete, ivHeart;
        LinearLayout imgSendInterest, btnUnmark;

        public basicClass(View itemView) {
            super(itemView);
            userProf = itemView.findViewById(R.id.Bimage_search_layout);

            fname = itemView.findViewById(R.id.Btxt_search_layout_name);
            age = itemView.findViewById(R.id.Btxt_search_layout_age);
            qualification = itemView.findViewById(R.id.Btxt_search_layout_education);

            viewProf = itemView.findViewById(R.id.tv_view_profile);

            btnUnmark = itemView.findViewById(R.id.layout_unmark);
            imgSendInterest = itemView.findViewById(R.id.layout_invitations);
            imgKundli = itemView.findViewById(R.id.layout_kundli);
            tvBookmark = itemView.findViewById(R.id.tv_bookmark);
            ivHeart = itemView.findViewById(R.id.iv_heart);
            tvIsInvite = itemView.findViewById(R.id.tv_is_invite);

        }
    }

    public interface GetUserList{
        public void getList();
    }

}
