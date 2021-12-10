package com.dreamz.samtaBhratruMandal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Adapters.UserInfoAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.UserImageDTO;
import com.dreamz.samtaBhratruMandal.Models.UserProfileDto;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {

    boolean islast = false;
    int count = 0;
    String candidateId, userId;
    ShowLoader showLoader;
    RecyclerView recyclerView;
    TextView tvName, tvAge, tvCandidateId, tvHeight, tvUserid;
    private TextView tvPersonalInfo,tvEducation,tvFamily,tvPhoto,tvContact;
    ImageView ivUserImages, ivPrevious, ivNext, userImage;
    LinearLayout llPeronalInfo, llOcupation, llPhoto, llFamily, llContact;
    RelativeLayout rlPhoto;

    private String userImageUrl;

    private UserInfoAdapter userInfoAdapter;

    private LinkedHashMap<String, String> personalinfoList = new LinkedHashMap<>();
    private LinkedHashMap<String, String> familyInfoList = new LinkedHashMap<>();
    private LinkedHashMap<String, String> occupationInfoList = new LinkedHashMap<>();
    private LinkedHashMap<String, String> contactInfoList = new LinkedHashMap<>();
    private ArrayList<String> imageDTOArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        toolbar.setTitle("View Profile");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getIntentData();
        init();
        getProfileDetails(userId);
        getImages(userId);
        tvPersonalInfo.setTextColor(getResources().getColor(R.color.light_blue));


        llPeronalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                rlPhoto.setVisibility(View.GONE);
                userInfoAdapter = new UserInfoAdapter(personalinfoList, ViewProfileActivity.this);
                recyclerView.setAdapter(userInfoAdapter);

                tvPersonalInfo.setTextColor(getResources().getColor(R.color.light_blue));
                tvEducation.setTextColor(getResources().getColor(R.color.white));
                tvFamily.setTextColor(getResources().getColor(R.color.white));
                tvPhoto.setTextColor(getResources().getColor(R.color.white));
                tvContact.setTextColor(getResources().getColor(R.color.white));

            }
        });

        llOcupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                rlPhoto.setVisibility(View.GONE);
                userInfoAdapter = new UserInfoAdapter(occupationInfoList, ViewProfileActivity.this);
                recyclerView.setAdapter(userInfoAdapter);

                tvPersonalInfo.setTextColor(getResources().getColor(R.color.white));
                tvEducation.setTextColor(getResources().getColor(R.color.light_blue));
                tvFamily.setTextColor(getResources().getColor(R.color.white));
                tvPhoto.setTextColor(getResources().getColor(R.color.white));
                tvContact.setTextColor(getResources().getColor(R.color.white));
            }
        });

        llPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                rlPhoto.setVisibility(View.VISIBLE);

                tvPersonalInfo.setTextColor(getResources().getColor(R.color.white));
                tvEducation.setTextColor(getResources().getColor(R.color.white));
                tvFamily.setTextColor(getResources().getColor(R.color.white));
                tvPhoto.setTextColor(getResources().getColor(R.color.light_blue));
                tvContact.setTextColor(getResources().getColor(R.color.white));
            }
        });

        llFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                rlPhoto.setVisibility(View.GONE);
                userInfoAdapter = new UserInfoAdapter(familyInfoList, ViewProfileActivity.this);
                recyclerView.setAdapter(userInfoAdapter);

                tvPersonalInfo.setTextColor(getResources().getColor(R.color.white));
                tvEducation.setTextColor(getResources().getColor(R.color.white));
                tvFamily.setTextColor(getResources().getColor(R.color.light_blue));
                tvPhoto.setTextColor(getResources().getColor(R.color.white));
                tvContact.setTextColor(getResources().getColor(R.color.white));
            }
        });

        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                rlPhoto.setVisibility(View.GONE);
                userInfoAdapter = new UserInfoAdapter(contactInfoList, ViewProfileActivity.this);
                recyclerView.setAdapter(userInfoAdapter);

                tvPersonalInfo.setTextColor(getResources().getColor(R.color.white));
                tvEducation.setTextColor(getResources().getColor(R.color.white));
                tvFamily.setTextColor(getResources().getColor(R.color.white));
                tvPhoto.setTextColor(getResources().getColor(R.color.white));
                tvContact.setTextColor(getResources().getColor(R.color.light_blue));
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count < imageDTOArrayList.size()) {
                    if (count != imageDTOArrayList.size() - 1) {
                        count++;
                    }
                    userImageUrl = "https://samatabhratrumandal.com/RestAPI" + imageDTOArrayList.get(count);

                    Glide.with(getApplicationContext())
                            .load("https://staging.samatabhratrumandal.com/RestAPI" + imageDTOArrayList.get(count))
                            .fitCenter()
                            .placeholder(R.drawable.noimg)
                            .into(ivUserImages);
                }
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count >= 0) {
                    if (count != 0) {
                        count--;
                    }

                    try{
                        userImageUrl = "https://samatabhratrumandal.com/RestAPI" + imageDTOArrayList.get(count);

                        Glide.with(getApplicationContext())
                                .load("https://staging.samatabhratrumandal.com/RestAPI" + imageDTOArrayList.get(count))
                                .fitCenter()
                                .placeholder(R.drawable.noimg)
                                .into(ivUserImages);
                    }catch (Exception e){

                    }

                }

            }
        });

        ivUserImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, FullImageShowActivity.class);
                intent.putExtra("imageUrl",userImageUrl);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIntentData() {

        if (!getIntent().getStringExtra("UserId").equals(""))
            userId = getIntent().getStringExtra("UserId");
    }

    void init() {
        showLoader = ShowLoader.getProgressDialog(ViewProfileActivity.this);
        recyclerView = findViewById(R.id.rv_info);
        tvName = findViewById(R.id.tv_name);
        tvAge = findViewById(R.id.tv_age);
        tvHeight = findViewById(R.id.tv_height);
        tvUserid = findViewById(R.id.tv_userId);
        tvCandidateId = findViewById(R.id.tv_candidateId);
        userImage = findViewById(R.id.iv_user_profile_image);
        ivUserImages = findViewById(R.id.iv_image);

        llPeronalInfo = findViewById(R.id.ll_personal_info);
        llOcupation = findViewById(R.id.ll_occupation);
        llPhoto = findViewById(R.id.ll_photo);
        llFamily = findViewById(R.id.ll_family);
        llContact = findViewById(R.id.ll_kundli);

        tvPersonalInfo=findViewById(R.id.tv_personalInfo);
        tvEducation=findViewById(R.id.tv_education);
        tvFamily=findViewById(R.id.tv_family);
        tvPhoto=findViewById(R.id.tv_photo);
        tvContact=findViewById(R.id.tv_contact);

        rlPhoto = findViewById(R.id.rl_photo);
        ivPrevious = findViewById(R.id.iv_previous);
        ivNext = findViewById(R.id.iv_next);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewProfileActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getProfileDetails(String userId) {
        if (serverConstants.isConnectingToInternet(this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<UserProfileDto> call = api.getUserDetail(userId);
            call.enqueue(new Callback<UserProfileDto>() {
                @Override
                public void onResponse(Call<UserProfileDto> call, Response<UserProfileDto> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body() != null) {
                            if (response.body().getProfileImage() != null) {
                                Glide.with(getApplicationContext())
                                        .load(serverConstants.imageBaseUrl + (response.body().getProfileImage().toString()))//"https://staging.samatabhratrumandal.com/"+
                                        .fitCenter()
                                        .placeholder(R.drawable.noimg)
                                        .into(userImage);
                            } else {
                                userImage.setImageResource(R.drawable.noimg);
                            }

                            if (response.body().getFullName() != null && !response.body().getFullName().equals("")) {
                                tvName.setText(response.body().getFullName().trim());
                            } else {
                                tvName.setText("");
                            }
                            if (!String.valueOf(response.body().getAge()).equals("")) {
                                tvAge.setText("Age: " + String.valueOf(response.body().getAge()) + " yrs");
                            } else {
                                tvAge.setText("");
                            }
                            if (response.body().getUserId() != 0) {
                                tvUserid.setText("Candidate Id: " + response.body().getApplicationId());
                            } else {
                                tvUserid.setText("Candidate Id: ");
                            }
                            if (response.body().getCandidateType() != null && !response.body().getCandidateType().equals("")) {
                                tvCandidateId.setText("Candidate Type: " + response.body().getCandidateType());
                            } else {
                                tvCandidateId.setText("");
                            }


                            if (response.body().getHeightInFeet() != null && !response.body().getHeightInFeet().equals("")) {
                                if (response.body().getHeightInInches() != null && !response.body().getHeightInInches().equals("")) {
                                    tvHeight.setText(getResources().getString(R.string.height) + ": " + response.body().getHeightInFeet().trim() + "\' " + response.body().getHeightInInches().trim() + "\"");
                                } else {
                                    tvHeight.setText(getResources().getString(R.string.height) + ": " + response.body().getHeightInFeet().trim());
                                }
                            } else {
                                tvHeight.setText("");
                            }

                            //personal info list
                            if (response.body().getBirthName() != null && !response.body().getBirthName().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.birth_name) + ": ", response.body().getBirthName().trim());
                            }
                            if (response.body().getBirthPlace() != null && !response.body().getBirthPlace().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.birth_place) + ": ", response.body().getBirthPlace().trim());
                            }
                            if (response.body().getBirthDate() != null && !response.body().getBirthDate().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.birth_date) + ": ", response.body().getBirthDate().trim());
                            }
                            if (response.body().getBirthHrs() != null && !response.body().getBirthHrs().equals("") && response.body().getBirthMins()!=null && !response.body().getBirthMins().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.birth_time)+"(24 Hrs)" + ": ", response.body().getBirthHrs().toString().trim()+":"+response.body().getBirthMins().toString().trim());
                            }
                            if (response.body().getGotra() != null && !response.body().getGotra().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.gotra) + ": ", response.body().getGotra().trim());
                            }
                            if (response.body().getHeightInFeet() != null && !response.body().getHeightInFeet().equals("")) {
                                if (response.body().getHeightInInches() != null && !response.body().getHeightInInches().equals("")) {
                                    tvHeight.setText(getResources().getString(R.string.height) + ": " + response.body().getHeightInFeet().trim() + "\' " + response.body().getHeightInInches().trim() + "\"");
                                } else {
                                    tvHeight.setText(getResources().getString(R.string.height) + ": " + response.body().getHeightInFeet().trim());
                                }
                            }
                            //varna not present

                            if (response.body().getBloodGroup() != null && !response.body().getBloodGroup().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.bloodgroup) + ": ", response.body().getBloodGroup().toString().trim());
                            }
                            if (response.body().getMamkul() != null && !response.body().getMamkul().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.mamkul) + ": ", response.body().getMamkul().trim());
                            }
                            if (response.body().getMobileNo() != null && !response.body().getMobileNo().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.telephone) + ": ", response.body().getMobileNo().toString().trim());
                            }

                            //Occupation info List
                            if (response.body().getEducationLevel() != null && !response.body().getEducationLevel().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.education_level) + ": ", response.body().getEducationLevel().trim());
                            }
                            if (response.body().getQualification() != null && !response.body().getQualification().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.education) + ": ", response.body().getQualification().trim());
                            }
                            if (response.body().getOccupation() != null && !response.body().getOccupation().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.occupation) + ": ", response.body().getOccupation().trim());
                            }
                            if (response.body().getCompany() != null && !response.body().getCompany().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.company) + ": ", response.body().getCompany().trim());
                            }
                            if (response.body().getJobLocation() != null && !response.body().getJobLocation().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.job_location_tv) + ": ", response.body().getJobLocation().trim());
                            }
                            if (response.body().getMonthlyIncome() != null && !response.body().getMonthlyIncome().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.monthly_income) + ": ", response.body().getMonthlyIncome().trim());
                            }
                            if (response.body().getExpectations() != null && !response.body().getExpectations().equals("")) {
                                occupationInfoList.put(getResources().getString(R.string.expectations) + ": ", response.body().getExpectations().trim());
                            }

                            //family Info List
                            if (response.body().getBroMarried() != null) {
                                familyInfoList.put(getResources().getString(R.string.brothers) + ": ", getResources().getString(R.string.married) + " " + response.body().getBroMarried().trim() + ", "+getResources().getString(R.string.unmarried) + " " + response.body().getBroUnmarried().trim());
                            }
                           /* if (response.body().getBroUnmarried() != null) {
                                familyInfoList.put(getResources().getString(R.string.brothers) + ": ", getResources().getString(R.string.unmarried) + " " + response.body().getBroUnmarried().trim());
                            }*/
                            if (response.body().getSisMarried() != null ) {
                                familyInfoList.put(getResources().getString(R.string.sisters) + ": ", getResources().getString(R.string.married) + " " + response.body().getSisMarried().trim()+", "+getResources().getString(R.string.unmarried) + " " + response.body().getSisUnmarried().trim());
                            }
                            /*if (response.body().getSisUnmarried() != null ) {
                                familyInfoList.put(getResources().getString(R.string.sisters) + ": ", getResources().getString(R.string.unmarried) + " " + response.body().getSisUnmarried().trim());
                            }*/
                            if (response.body().getHomeTown() != null && !response.body().getHomeTown().equals("")) {
                                familyInfoList.put(getResources().getString(R.string.hometown_tv) + ": ", response.body().getHomeTown().trim());
                            }
                            if (response.body().getTaluka() != null && !response.body().getTaluka().equals("")) {
                                familyInfoList.put(getResources().getString(R.string.taluka) + ": ", response.body().getTaluka().trim());
                            }
                            if (response.body().getDistrict() != null && !response.body().getDistrict().equals("")) {
                                familyInfoList.put(getResources().getString(R.string.district) + ": ", response.body().getDistrict().trim());
                            }
                            if (response.body().getNameOfFatherGuardian() != null && !response.body().getNameOfFatherGuardian().equals("")) {
                                familyInfoList.put(getResources().getString(R.string.father_guardian_name) + ": ", response.body().getFatherGurdianTitle() + " " + response.body().getNameOfFatherGuardian().trim());
                            }
                            if (response.body().getParentalAddress() != null && !response.body().getParentalAddress().equals("")) {
                                familyInfoList.put(getResources().getString(R.string.address) + ": ", response.body().getParentalAddress().trim());
                            }
                            if (response.body().getParentalContactNo1() != null && !response.body().getParentalContactNo1().equals("")) {
                                if (response.body().getParentalContactNo2() != null && !response.body().getParentalContactNo2().equals("")) {
                                    familyInfoList.put(getResources().getString(R.string.telephone) + ": ", response.body().getParentalContactNo1().toString().trim()+", "+response.body().getParentalContactNo2().toString().trim());
                                }else{
                                    familyInfoList.put(getResources().getString(R.string.telephone) + ": ", response.body().getParentalContactNo1().toString().trim());
                                }

                            }
                            if(response.body().getParentalEmail()!=null && !response.body().getParentalEmail().equals("")){
                                familyInfoList.put("Email id: ",response.body().getParentalEmail().toString().trim());
                            }

                            //contact info list
                            if (response.body().getNameOfFatherGuardian() != null && !response.body().getNameOfFatherGuardian().equals("")) {
                                contactInfoList.put(getResources().getString(R.string.contact_person1) + ": ", response.body().getFatherGurdianTitle() + " " + response.body().getNameOfFatherGuardian().trim());
                            }
                            if (response.body().getParentalAddress() != null && !response.body().getParentalAddress().equals("")) {
                                contactInfoList.put(getResources().getString(R.string.address1) + ": ", response.body().getParentalAddress().trim());
                            }
                            if (response.body().getParentalContactNo1() != null && !response.body().getParentalContactNo1().equals("")) {
                                if (response.body().getParentalContactNo2() != null && !response.body().getParentalContactNo2().equals("")) {
                                    contactInfoList.put(getResources().getString(R.string.telephone1) + ": ", response.body().getParentalContactNo1()+", "+response.body().getParentalContactNo2());
                                }else{
                                    contactInfoList.put(getResources().getString(R.string.telephone1) + ": ", response.body().getParentalContactNo1());
                                }
                            }
                            if (response.body().getAltNameOfFatherGuardian() != null && !response.body().getAltNameOfFatherGuardian().equals("")) {
                                contactInfoList.put(getResources().getString(R.string.contact_person2) + ": ", response.body().getAltFatherGurdianTitle() + " " + response.body().getAltNameOfFatherGuardian().trim());
                            }
                            if (response.body().getAltParentalAddress() != null && !response.body().getAltParentalAddress().equals("")) {
                                contactInfoList.put(getResources().getString(R.string.address2) + ": ", response.body().getAltParentalAddress().trim());
                            }
                            if (response.body().getAltParentalContactNo1() != null && !response.body().getAltParentalContactNo1().equals("")) {
                                if (response.body().getAltParentalContactNo2() != null && !response.body().getAltParentalContactNo2().equals("")) {
                                    contactInfoList.put(getResources().getString(R.string.telephone2) + ": ", response.body().getAltParentalContactNo1()+","+response.body().getAltParentalContactNo2());
                                }else{
                                    contactInfoList.put(getResources().getString(R.string.telephone2) + ": ", response.body().getAltParentalContactNo1());
                                }
                            }

                            userInfoAdapter = new UserInfoAdapter(personalinfoList, ViewProfileActivity.this);
                            recyclerView.setAdapter(userInfoAdapter);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "No information found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<UserProfileDto> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getImages(String userId) {
        if (serverConstants.isConnectingToInternet(ViewProfileActivity.this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<UserImageDTO>> call = api.getUserImages(userId, "images");
            call.enqueue(new Callback<ArrayList<UserImageDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<UserImageDTO>> call, Response<ArrayList<UserImageDTO>> response) {
                    showLoader.hide();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                if (imageDTOArrayList.size() > 0) {
                                    imageDTOArrayList.clear();
                                }
                                for (int i = 0; i < response.body().size(); i++) {
                                    imageDTOArrayList.add(response.body().get(i).getFilePath());
                                }

                                if (!islast) {
                                    userImageUrl = serverConstants.imageBaseUrl + imageDTOArrayList.get(0);

                                    Glide.with(getApplicationContext())
                                            .load(serverConstants.imageBaseUrl + imageDTOArrayList.get(0))//"https://staging.samatabhratrumandal.com/"+
                                            .fitCenter()
                                            .placeholder(R.drawable.noimg)
                                            .into(ivUserImages);
                                    count = 0;
                                } else {
                                    userImageUrl = serverConstants.imageBaseUrl + imageDTOArrayList.get(imageDTOArrayList.size() - 1);

                                    Glide.with(getApplicationContext())
                                            .load(serverConstants.imageBaseUrl + imageDTOArrayList.get(imageDTOArrayList.size() - 1))//"https://staging.samatabhratrumandal.com/"+
                                            .fitCenter()
                                            .placeholder(R.drawable.noimg)
                                            .into(ivUserImages);
                                    count = imageDTOArrayList.size() - 1;
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "no images found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<UserImageDTO>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(ViewProfileActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
}