package com.dreamz.samtaBhratruMandal.Adapters;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.AddToBookMarks;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Activities.ViewProfileActivity;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ResponseMatchMaking;
import com.dreamz.samtaBhratruMandal.Models.SentInterestDTO;
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

public class SendInterestAdapter extends RecyclerView.Adapter<SendInterestAdapter.VH> {

    List<SentInterestDTO> sentInterestList;
    Context context;
    UserSessionManager session;
    int userid, candidId;
    StringEntity entity;
    String userGender, userDob, userHr, userMin, selectedDob;
    private GetListAgainAfteBookmark getListAgainAfteBookmark;


    public SendInterestAdapter(Context context, ArrayList<SentInterestDTO> sentInterestList, GetListAgainAfteBookmark getListAgainAfteBookmark) {
        this.context = context;
        this.sentInterestList = sentInterestList;
        this.getListAgainAfteBookmark = getListAgainAfteBookmark;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_interest_model, null);

        session = new UserSessionManager(context);
        HashMap<String, Object> user = session.getUserDetails();
        userid = Integer.parseInt(user.get(UserSessionManager.USERID).toString());
        candidId = Integer.parseInt(user.get(UserSessionManager.CANDIDID).toString());

        userGender = user.get(UserSessionManager.GENDER).toString();
        userDob = user.get(UserSessionManager.BIRTH_DATE).toString();
        userHr = user.get(UserSessionManager.BIRTH_HRS).toString();
        userMin = user.get(UserSessionManager.BIRTH_MINS).toString();

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
        if (sentInterestList.get(position).getProfileImage() != null && !sentInterestList.get(position).getProfileImage().equals("")) {
            Glide.with(context)
                    .load(serverConstants.imageBaseUrl + sentInterestList.get(position).getProfileImage().toString())//"https://staging.samatabhratrumandal.com/"+
                    .fitCenter()
                    .placeholder(R.drawable.noimg)
                    .into(holder.ivProfile);
        } else {
            holder.ivProfile.setImageResource(R.drawable.noimg);
        }

        if (sentInterestList.get(position).getIsBookMarked()) {
            holder.ivHeart.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            holder.tvBookmark.setText("Unmark");
        } else {
            holder.ivHeart.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_heart));
            holder.tvBookmark.setText("Bookmark");
        }

        holder.tvName.setText(sentInterestList.get(position).getFullName());
        holder.tvAge.setText("Age: " + String.valueOf(sentInterestList.get(position).getAge()) + " yrs");
        holder.tvEducation.setText(context.getResources().getString(R.string.education) + ": " + sentInterestList.get(position).getEducation());
        selectedDob = sentInterestList.get(position).getBirthdate().toString();

        //bookmark buttonclick
        holder.llBoookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sentInterestList.get(position).getIsBookMarked()) {

                    AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject jsonParams = new JSONObject();

                    try {
                        jsonParams.put("userId", userid);
                        jsonParams.put("bookMarkedCandidateId", String.valueOf(sentInterestList.get(position).getCandidateId()));
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
                                    getListAgainAfteBookmark.getList();
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

                } else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject jsonParams = new JSONObject();

                    try {
                        jsonParams.put("userId", userid);
                        jsonParams.put("bookMarkedCandidateId", String.valueOf(sentInterestList.get(position).getCandidateId()));
                        entity = new StringEntity(jsonParams.toString());
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    client.post(context, AddToBookMarks, entity, "application/json", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                if (response.getString("result").equals("Success")) {
                                    getListAgainAfteBookmark.getList();
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
        //reinvite button click

        holder.llReinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("senderCandidateId", candidId);
                    jsonParams.put("recieverCandidateId", String.valueOf(sentInterestList.get(position).getCandidateId()));
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
        });
        //view Profile button click
        holder.tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                // Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("CandidateId", String.valueOf(sentInterestList.get(position).getCandidateId()));
                intent.putExtra("UserId", String.valueOf(sentInterestList.get(position).getUserId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        //delete Interest
        /*  holder.imgDeleteInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("senderCandidateId", UserCandidateId);
                    jsonParams.put("recieverCandidateId",sentInterestList.get(position).getCandidateId());
                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(context, RemoveInterest, entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (response.getString("result").equals("Success")) {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                sentInterestList.remove(position);
                                notifyDataSetChanged();
                            } else
                            {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_LONG).show();
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

        });*/
        //kundali
        holder.tvKundali.setOnClickListener(new View.OnClickListener() {
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
                                Integer.parseInt(sentInterestList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(sentInterestList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f,

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
                                Integer.parseInt(sentInterestList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(sentInterestList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f
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

        return sentInterestList.size();
    }


    public class VH extends RecyclerView.ViewHolder {

        TextView tvName, tvAge, tvEducation, tvViewProfile, tvKundali, tvBookmark;
        LinearLayout llReinvite, llBoookmarks;
        ImageView ivProfile, ivHeart;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_fname);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvEducation = itemView.findViewById(R.id.tv_education);
            tvKundali = itemView.findViewById(R.id.tv_kundli);
            tvViewProfile = itemView.findViewById(R.id.tv_viewProfile);

            llBoookmarks = itemView.findViewById(R.id.ll_bookmarks);
            llReinvite = itemView.findViewById(R.id.ll_reInvite);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvBookmark = itemView.findViewById(R.id.tv_bookmark);
            ivHeart = itemView.findViewById(R.id.iv_heart);
        }
    }

    public interface GetListAgainAfteBookmark {
        public void getList();
    }
}
        /*extends RecyclerView.Adapter<SendInterestAdapter.basicClass> {

    List<SentInterestDTO> sentInterestList = new ArrayList<>();
    Context context;
    UserSessionManager session ;
    int userid,candidId;

    public SendInterestAdapter(Context context, ArrayList<SentInterestDTO> sentInterestList) {
        this.context = context;
        this.sentInterestList=sentInterestList;


    }

    public void addItem(List<SendInterestModel> sliderItem)
    {
        this.sentInterestList.addAll(sliderItem);
        notifyDataSetChanged();

    }
   public void delete(int position){
        msgList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SendInterestAdapter.basicClass onCreateViewHolder( ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_interest_model, null);

        session = new UserSessionManager(context);
        HashMap<String, Object> user = session.getUserDetails();
        userid = Integer.parseInt(user.get(UserSessionManager.USERID).toString());
        candidId=Integer.parseInt(user.get(UserSessionManager.CANDIDID).toString());

        return new basicClass(inflate);
    }

    @Override
    public void onBindViewHolder(SendInterestAdapter.basicClass holder, @SuppressLint("RecyclerView") int position) {

        /*if(sentInterestList.get(position).getProfileImage()!=null && !sentInterestList.get(position).getProfileImage().equals("")) {
            Glide.with(context)
                    .load("https://staging.samatabhratrumandal.com/"+sentInterestList.get(position).getProfileImage().toString().substring(2))//"https://staging.samatabhratrumandal.com/"+
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivProfile);
        }
        else {
            holder.ivProfile.setImageResource(R.mipmap.ic_launcher);
        }*/
        /*    holder.tvName.setText(sentInterestList.get(position).getFullName());
        holder.tvAge.setText(String.valueOf(sentInterestList.get(position).getAge()));
        holder.tvEducation.setText(sentInterestList.get(position).getEducation());

        /*   holder.llBoookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("userId", userid);
                    jsonParams.put("bookMarkedCandidateId",Integer.parseInt(sentInterestList.get(position).CandidId));
                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(context, AddToBookMarks, entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (response.getString("result").equals("Success")) {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                            } else
                            {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        showLoader.dismissDialog();

                    }
                });

            }
        });*/
        /*     holder.llReinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("senderCandidateId", UserCandidateId);
                    jsonParams.put("recieverCandidateId",Integer.parseInt(sentInterestList.get(position).CandidId));
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
                            } else
                            {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_LONG).show();
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
        });*/
        /* holder.tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
               // Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("CandidateId",String.valueOf(sentInterestList.get(position).getCandidateId()));
                intent.putExtra("UserId",String.valueOf(sentInterestList.get(position).getUserId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
        /*
    @Override
    public int getItemCount() {
        return sentInterestList.size();
    }

    public class basicClass extends RecyclerView.ViewHolder {

        TextView tvName, tvAge,tvEducation,tvViewProfile,tvKundali;
        LinearLayout llBoookmarks,llReinvite;
        ImageView ivProfile;
        public basicClass( View itemView) {
            super(itemView);
            tvName= itemView.findViewById(R.id.tv_fname);
            tvAge=itemView.findViewById(R.id.tv_age);
            tvEducation=itemView.findViewById(R.id.tv_education);
            tvKundali=itemView.findViewById(R.id.tv_kundli);
            tvViewProfile=itemView.findViewById(R.id.tv_view_profile);

            llBoookmarks=itemView.findViewById(R.id.ll_bookmarks);
            llReinvite=itemView.findViewById(R.id.ll_reInvite);
            ivProfile=itemView.findViewById(R.id.iv_profile);

        }
    }
}*/
