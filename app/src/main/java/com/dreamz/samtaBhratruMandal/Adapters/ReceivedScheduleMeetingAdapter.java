package com.dreamz.samtaBhratruMandal.Adapters;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.AcceptMeeting;
import static com.dreamz.samtaBhratruMandal.Server.serverConstants.CancelMeeting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Activities.ViewProfileActivity;
import com.dreamz.samtaBhratruMandal.Models.ScheduleMettingModel;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class ReceivedScheduleMeetingAdapter extends RecyclerView.Adapter<ReceivedScheduleMeetingAdapter.basicClass> {

    List<ScheduleMettingModel> sInterList = new ArrayList<>();
    UserSessionManager session;
    int userid;
    int UserCandidateId;
    StringEntity entity;
    private Context context;
    private ShowLoader showLoader;
    private OnClickRefresh onClickRefresh;

    public ReceivedScheduleMeetingAdapter(Context context, OnClickRefresh onClickRefresh) {
        this.context = context;
        showLoader = ShowLoader.getProgressDialog(context);
        this.onClickRefresh = onClickRefresh;
    }

    public void addItem(List<ScheduleMettingModel> sliderItem) {
        sInterList.clear();
        this.sInterList.addAll(sliderItem);
        notifyDataSetChanged();

    }
//    public void delete(int position){
//        msgList.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public ReceivedScheduleMeetingAdapter.basicClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.receive_schedule_metting_layout, null);
        session = new UserSessionManager(context);
        HashMap<String, Object> user = session.getUserDetails();
        userid = Integer.parseInt(String.valueOf(user.get(UserSessionManager.USERID)));

        HashMap<String, String> user1 = session.getUserId();
        UserCandidateId = Integer.parseInt(user1.get(UserSessionManager.CANDIDID));

        return new basicClass(inflate);
    }

    @Override
    public void onBindViewHolder(ReceivedScheduleMeetingAdapter.basicClass holder, @SuppressLint("RecyclerView") int position) {

        if (sInterList.get(position).getProfileImage() != null && !sInterList.get(position).getProfileImage().equals("")) {
            Glide.with(context)
                    .load(serverConstants.imageBaseUrl + (sInterList.get(position).getProfileImage()))
                    .fitCenter()
                    .placeholder(R.drawable.noimg)
                    .into(holder.userProf);
        } else {
            holder.userProf.setImageResource(R.drawable.noimg);
        }


        try {
            // object creation of JitsiMeetConferenceOptions
            // class by the name of options
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(sInterList.get(position).getMeetingURL()))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (sInterList.get(position).getFullName() != null && !sInterList.get(position).getFullName().equals("")) {
            holder.fname.setText(sInterList.get(position).getFullName().toString());
        } else {
            holder.fname.setText("-");
        }

        holder.age.setText("Age: " + String.valueOf(sInterList.get(position).getAge()) + " yrs");


        if (sInterList.get(position).getEducation() != null && !sInterList.get(position).getEducation().equals("")) {
            holder.qualification.setText(context.getResources().getString(R.string.education) + ": " + sInterList.get(position).getEducation());
        }

        if (sInterList.get(position).getMobileNumber() != null && !sInterList.get(position).getMobileNumber().equals("")) {
            holder.number.setText(sInterList.get(position).getMobileNumber().toString());
        }

        if (sInterList.get(position).getMeetingAccepted() != null && !sInterList.get(position).getMeetingAccepted().equals("")) {
            if (sInterList.get(position).getMeetingAccepted().equals("Yes")) {
                holder.meetingLayout.setVisibility(View.GONE);
                holder.btJoinMeeting.setVisibility(View.VISIBLE);
            } else {
                holder.btJoinMeeting.setVisibility(View.GONE);
                holder.meetingLayout.setVisibility(View.VISIBLE);
            }
        }

        holder.btJoinMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(sInterList.get(position).getMeetingURL())
                        .build();
                JitsiMeetActivity.launch(context, options);
            }
        });

        holder.btAcceptMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Accept_API_Call(position);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.btRecectMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DELETE_API_Call(position);
            }
        });

//        holder.bookmarks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AsyncHttpClient client = new AsyncHttpClient();
//                JSONObject jsonParams = new JSONObject();
//                try {
//                    jsonParams.put("userId", userid);
//                    jsonParams.put("bookMarkedCandidateId",Integer.parseInt(sInterList.get(position).CandidId));
//                    entity = new StringEntity(jsonParams.toString());
//                } catch (JSONException | UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                client.post(context, AddToBookMarks, entity, "application/json", new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//
//                        try {
//                            if (response.getString("result").equals("Success")) {
//                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
//                            } else
//                            {
//                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
////                        showLoader.dismissDialog();
//
//                    }
//                });
//
//            }
//        });

//        holder.send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AsyncHttpClient client = new AsyncHttpClient();
//                JSONObject jsonParams = new JSONObject();
//                try {
//                    jsonParams.put("senderCandidateId", UserCandidateId);
//                    jsonParams.put("recieverCandidateId",Integer.parseInt(sInterList.get(position).CandidId));
//                    entity = new StringEntity(jsonParams.toString());
//                } catch (JSONException | UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                client.post(context, SendInterest, entity, "application/json", new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//
//                        try {
//                            if (response.getString("result").equals("Success")) {
//                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
//                            } else
//                            {
//                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//
//
//                    }
//                });
//
//            }
//        });

        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                // Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("CandidateId", String.valueOf(sInterList.get(position).getMeetingWithCandidateId()));
                intent.putExtra("UserId",String.valueOf(sInterList.get(position).getUserId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



       /* holder.imgDeleteInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("senderCandidateId", UserCandidateId);
                    jsonParams.put("recieverCandidateId", Integer.parseInt(sInterList.get(position).CandidId));
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
                            } else {
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


    }

    @Override
    public int getItemCount() {
        return sInterList.size();
    }

    private void DELETE_API_Call(int posititon) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params;
        params = new RequestParams();
        showLoader.show();
        client.post(CancelMeeting + sInterList.get(posititon).getMeetingId(),
                new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        showLoader.hide();
                        onClickRefresh.onRefresh();
                        JSONArray array = null;
                        try {
                            array = new JSONArray(responseString);
                            Log.d("TAG", " Response is:-" + responseString);
                            onClickRefresh.onRefresh();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                String message = product.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                sInterList.remove(posititon);
                                notifyDataSetChanged();
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
                            Toast.makeText(context,
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(context,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(
                                    context,
                                    "Please connect to Internet and try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void Accept_API_Call(int posititon) throws UnsupportedEncodingException, JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        // send parameters in json body
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("meetingAcceptorCandidateId", sInterList.get(posititon).getMeetingWithCandidateId());
        jsonParams.put("meetingRequesterCandidateId", sInterList.get(posititon).getCandidateId());
        jsonParams.put("meetingDate", sInterList.get(posititon).getMeetingDate());
        jsonParams.put("timeSlot", sInterList.get(posititon).getTimeSlot());
        entity = new StringEntity(jsonParams.toString());
        showLoader.show();
        client.post(context, AcceptMeeting, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                showLoader.hide();
                try {
                    if (response.getString("result").equals("Success")) {
                        onClickRefresh.onRefresh();
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
                showLoader.hide();
            }
        });
    }

    public interface OnClickRefresh {
        public void onRefresh();
    }

    public class basicClass extends RecyclerView.ViewHolder {
        TextView fname, age, qualification, viewProfile, number;
        LinearLayout bookmarks, send, meetingLayout;
        ImageView userProf, imgDeleteInterest;
        AppCompatButton btRecectMeeting, btAcceptMeeting, btJoinMeeting;

        public basicClass(View itemView) {
            super(itemView);
            userProf = itemView.findViewById(R.id.Iimage_search_layout);
            fname = itemView.findViewById(R.id.Itxt_search_layout_name);
            age = itemView.findViewById(R.id.Itxt_search_layout_age);
            qualification = itemView.findViewById(R.id.Itxt_search_layout_education);

            viewProfile = itemView.findViewById(R.id.Itxt_view_profile);
            number = itemView.findViewById(R.id.Itxt_search_layout_number);
//            bookmarks = itemView.findViewById(R.id.Ilayout_bookmarks);
//            send = itemView.findViewById(R.id.Ilayout_invitations);
            meetingLayout = itemView.findViewById(R.id.Ilayout_kundli);
            //imgDeleteInterest = itemView.findViewById(R.id.img_dlt_interest);
            btAcceptMeeting = itemView.findViewById(R.id.btn_accept_meeting);
            btRecectMeeting = itemView.findViewById(R.id.btn_reject_meeting);
            btJoinMeeting = itemView.findViewById(R.id.btn_join_meeting);


        }
    }
}
