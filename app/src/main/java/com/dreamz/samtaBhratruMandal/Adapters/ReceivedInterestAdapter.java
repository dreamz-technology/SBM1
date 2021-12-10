package com.dreamz.samtaBhratruMandal.Adapters;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.ApprovedInterestList;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Activities.ViewProfileActivity;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.RecievedIntrestDTO;
import com.dreamz.samtaBhratruMandal.Models.ResponseMatchMaking;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivedInterestAdapter extends RecyclerView.Adapter<ReceivedInterestAdapter.VH> {

    List<RecievedIntrestDTO> recievedIntrestList;
    Context context;
    UserSessionManager session;
    int userid, candidateId;
    StringEntity entity;
    String userGender,userDob,userHr,userMin,selectedDob;
    private GetListListner getListListner;


    public ReceivedInterestAdapter(Context context, List<RecievedIntrestDTO> receivedInterestList,GetListListner getListListner) {
        this.context = context;
        this.recievedIntrestList = receivedInterestList;
        this.getListListner=getListListner;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_interest_layout, null);

        session = new UserSessionManager(context);
        HashMap<String, Object> user = session.getUserDetails();
        userid = Integer.parseInt(user.get(UserSessionManager.USERID).toString());
        candidateId = Integer.parseInt(user.get(UserSessionManager.CANDIDID).toString());

        userGender = user.get(UserSessionManager.GENDER).toString();
        userDob = user.get(UserSessionManager.BIRTH_DATE).toString();
        userHr = user.get(UserSessionManager.BIRTH_HRS).toString();
        userMin = user.get(UserSessionManager.BIRTH_MINS).toString();


        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {

        if(recievedIntrestList.get(position).getProfileImage()!=null && !recievedIntrestList.get(position).getProfileImage().equals("")) {

            Glide.with(context)
                    .load(serverConstants.imageBaseUrl
                            + recievedIntrestList.get(position).getProfileImage().toString())//"https://staging.samatabhratrumandal.com/"+
                    .centerCrop()
                    .placeholder(R.drawable.noimg)
                    .into(holder.userProf);
        }
        else
            holder.userProf.setImageResource(R.drawable.noimg);

        selectedDob=recievedIntrestList.get(position).getBirthdate().toString();

        holder.fname.setText(recievedIntrestList.get(position).getFullName());
        holder.age.setText("Age: "+recievedIntrestList.get(position).getAge() + " yrs");
        holder.qualification.setText(context.getResources().getString(R.string.education)+": "+recievedIntrestList.get(position).getEducation());

        if(recievedIntrestList.get(position).getInterestAccepted()!=null){
            if(recievedIntrestList.get(position).getInterestAccepted().equals("Yes")){
                holder.accept.setVisibility(View.GONE);
            }
        }


        //accept Button click
            holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();

                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("interestAcceptorCandidateId", candidateId);
                    jsonParams.put("interestRecieverCandidateId",recievedIntrestList.get(position).getCandidateId());
                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(context, ApprovedInterestList, entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (response.getString("result").equals("Success")) {
                                //holder.accept.setVisibility(View.GONE);
                                getListListner.getList();
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();

                            } else
                            {
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
        //send button click
             /*   holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("senderCandidateId", userid);
                    jsonParams.put("recieverCandidateId",String.valueOf(recievedIntrestList.get(position).getCandidateId()));
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
        //view profile button click
        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                //Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("CandidateId",String.valueOf(recievedIntrestList.get(position).getCandidateId()));
                intent.putExtra("UserId",String.valueOf(recievedIntrestList.get(position).getUserId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        //kundali button click
        holder.tvKundli.setOnClickListener(new View.OnClickListener() {
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
                                Integer.parseInt(recievedIntrestList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(recievedIntrestList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f,

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
                                Integer.parseInt(recievedIntrestList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(recievedIntrestList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f
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

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncHttpClient client = new AsyncHttpClient();

                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("senderCandidateId", recievedIntrestList.get(position).getCandidateId());
                    jsonParams.put("recieverCandidateId",candidateId);
                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();

                }
                client.post(context, serverConstants.RemoveInterest, entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (response.getString("result").equals("Success")) {
                                Toast.makeText(context, response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();

                                recievedIntrestList.remove(position);
                                notifyDataSetChanged();

                            } else
                            {
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

    }

    @Override
    public int getItemCount() {

        return recievedIntrestList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView fname, age, qualification, viewProfile,tvKundli;
        ImageView userProf;
        AppCompatButton accept, reject;

        public VH(@NonNull View itemView) {
            super(itemView);
            userProf = itemView.findViewById(R.id.Rimage_search_layout);
            fname = itemView.findViewById(R.id.Rtxt_search_layout_name);
            age = itemView.findViewById(R.id.Rtxt_search_layout_age);
            qualification = itemView.findViewById(R.id.Rtxt_search_layout_education);
            tvKundli=itemView.findViewById(R.id.tv_viewKundali);
            viewProfile = itemView.findViewById(R.id.Rtxt_view_profile);
            reject = itemView.findViewById(R.id.btn_accept_interest);
            accept = itemView.findViewById(R.id.btn_reject_interest);

        }
    }

    public interface GetListListner{
        public void getList();
    }
}
