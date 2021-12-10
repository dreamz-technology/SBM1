package com.dreamz.samtaBhratruMandal.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Activities.PopUpAvailableTimeSlot;
import com.dreamz.samtaBhratruMandal.Activities.ViewProfileActivity;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.ApprovedListDTO;
import com.dreamz.samtaBhratruMandal.Models.ResponseMatchMaking;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApprovedListAdapter extends RecyclerView.Adapter<ApprovedListAdapter.basicClass> {

    List<ApprovedListDTO> AList = new ArrayList<>();
    private Context context;
    UserSessionManager session;
    int userid;
    int UserCandidateId;
    String userGender;
    String userDob, selectedDob;
    String userHr;
    String userMin;
    // private ShowLoader showLoader;

    StringEntity entity;

    public ApprovedListAdapter(Context context, List<ApprovedListDTO> approvedList) {
        this.context = context;
        this.AList = approvedList;
        //showLoader = ShowLoader.getProgressDialog(context);
    }

   /* public void addItem(List<ApprovedListDTO> sliderItem) {
        this.AList.addAll(sliderItem);
        notifyDataSetChanged();

    }*/
//    public void delete(int position){
//        msgList.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public ApprovedListAdapter.basicClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_layout, null);

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
    public void onBindViewHolder(ApprovedListAdapter.basicClass holder, @SuppressLint("RecyclerView") int position) {
        if (AList.get(position).getProfileImage() != null && !AList.get(position).getProfileImage().equals("")) {

            Glide.with(context)
                    .load(serverConstants.imageBaseUrl + AList.get(position).getProfileImage().toString())//"https://staging.samatabhratrumandal.com/"+
                    .centerCrop()
                    .placeholder(R.drawable.noimg)
                    .into(holder.userProf);
        } else
            holder.userProf.setImageResource(R.drawable.noimg);


        holder.fname.setText(AList.get(position).getFullName());
        holder.color.setText(context.getResources().getString(R.string.color) + ": " + AList.get(position).getComplexion());
        holder.height.setText(context.getResources().getString(R.string.height) + ": " + AList.get(position).getHeightInFeet() + "\'" + AList.get(position).getHeightInInches() + "\"");
        holder.age.setText("Age: " + AList.get(position).getAge() + " yrs");
        holder.occupation.setText(context.getResources().getString(R.string.education) + ": " + AList.get(position).getEducation());

        selectedDob = AList.get(position).getBirthdate().toString();

        holder.meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopUpAvailableTimeSlot.class);
                intent.putExtra("meetingWithCandidId", AList.get(position).getCandidateId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://api.whatsapp.com/send?phone=" + "+91"+AList.get(position).getMobileNumber();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                //Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("CandidateId", String.valueOf(AList.get(position).getCandidateId()));
                intent.putExtra("UserId", String.valueOf(AList.get(position).getUserId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        //date month year hour mins 19.9975,73.7898,5.5   21.0077,75.5626

        //if(gender male  ){
        // (preferenes se 8 valuees,,Alistse 8 values)}
        //if(gender female){
        // (Alist 8 values,, preferenes se 8 values)

        holder.viewKundli.setOnClickListener(new View.OnClickListener() {
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
                                Integer.parseInt(AList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(AList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f,

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
                                Integer.parseInt(userHr), Integer.parseInt(userMin), 21.0077f, 75.5626f, 5.5f,

                                serverConstants.getDay(selectedDob),
                                serverConstants.getMonth(selectedDob),
                                serverConstants.getYear(selectedDob),
                                Integer.parseInt(AList.get(position).getBirthHrsForKundali()),
                                Integer.parseInt(AList.get(position).getBirthMinsForKundali()), 19.9975f, 73.7898f, 5.5f
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
                /*if (serverConstants.isConnectingToInternet(context)) {
                 //   showLoader.show();
                    APIClient api = ApiFactory.kundli();
                    Call<ResponseMatchMaking> call = api.getMatchingPoints(serverConstants.getAuthToken(),
                            19, 4, 1990, 16, 00, 30.7046f, 76.7179f, 5.5f,
                            28,5,1992,6,13,26.1197f,85.3910f,5.5f);
                    call.enqueue(new Callback<ResponseMatchMaking>() {
                        @Override
                        public void onResponse(Call<ResponseMatchMaking> call, Response<ResponseMatchMaking> response) {
                     //       showLoader.hide();
                           Log.e("TAG",response.toString());

                        }

                        @Override
                        public void onFailure(Call<ResponseMatchMaking> call, Throwable t) {
                         //   showLoader.hide();
                            t.printStackTrace();
                        }
                    });


                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    private String subString(String string, int i) {
        return string.substring(i);
    }

    @Override
    public int getItemCount() {
        return AList.size();
    }

    public class basicClass extends RecyclerView.ViewHolder {
        TextView fname, color, height, age, occupation, viewProfile, viewKundli;
        ImageView userProf, whatsApp, meeting;

        public basicClass(View itemView) {
            super(itemView);
            userProf = itemView.findViewById(R.id.Aimage_search_layout);
            fname = itemView.findViewById(R.id.Atxt_search_layout_name);
            color = itemView.findViewById(R.id.tv_color);
            height = itemView.findViewById(R.id.tv_height);
            age = itemView.findViewById(R.id.Atxt_search_layout_age);
            occupation = itemView.findViewById(R.id.tv_occupation);
            viewProfile = itemView.findViewById(R.id.Atxt_view_profile);
            whatsApp = itemView.findViewById(R.id.btn_whats_app);
            meeting = itemView.findViewById(R.id.btn_meeting);
            viewKundli = itemView.findViewById(R.id.Aview_kundli);


        }
    }
}
