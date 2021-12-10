package com.dreamz.samtaBhratruMandal.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Adapters.UserInfoAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.UploadImageSuccessDTO;
import com.dreamz.samtaBhratruMandal.Models.UserImageDTO;
import com.dreamz.samtaBhratruMandal.Models.UserProfileDto;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    //Image
    int PICK_IMAGE_MULTIPLE = 1;
    boolean islast = false;

    int count = 0;
    private UserSessionManager session;
    private ShowLoader showLoader;
    private String userid;
    private String userImageUrl;

    private AppCompatButton btUpload;
    private ImageView userImage, ivUserImages, ivPrevious, ivNext;
    private TextView tvPersonalInfo, tvEducation, tvFamily, tvPhoto, tvContact;
    private ImageView ivPersonal, ivEducation, ivPhoto, ivFamily, ivContact;
    private TextView tvName, tvAge, tvHeight, tvUserid, tvCandidateId;
    private LinearLayout llPeronalInfo, llOcupation, llPhoto, llFamily, llContact;
    private RecyclerView recyclerView;
    private UserInfoAdapter userInfoAdapter;
    private RelativeLayout rlPhoto;
    private LinkedHashMap<String, String> personalinfoList = new LinkedHashMap<>();
    private LinkedHashMap<String, String> familyInfoList = new LinkedHashMap<>();
    private LinkedHashMap<String, String> occupationInfoList = new LinkedHashMap<>();
    private LinkedHashMap<String, String> contactInfoList = new LinkedHashMap<>();
    private ArrayList<String> imageDTOArrayList = new ArrayList<>();
    private ArrayList<UserImageDTO> userImageDTOArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        setContentView(R.layout.activity_new_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        toolbar.setTitle("User Profile");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
        //ApiCall();
        //showImages();
        getProfileData(userid);
        getUserImages(userid);
        tvPersonalInfo.setTextColor(getResources().getColor(R.color.light_blue));


        llPeronalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                rlPhoto.setVisibility(View.GONE);
                userInfoAdapter = new UserInfoAdapter(personalinfoList, ProfileActivity.this);
                recyclerView.setAdapter(userInfoAdapter);
                //ivPersonal.setImageDrawable(getResources().getDrawable(R.drawable.whit));

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
                userInfoAdapter = new UserInfoAdapter(occupationInfoList, ProfileActivity.this);
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
                userInfoAdapter = new UserInfoAdapter(familyInfoList, ProfileActivity.this);
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
                userInfoAdapter = new UserInfoAdapter(contactInfoList, ProfileActivity.this);
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
                    if (imageDTOArrayList.size() > 0) {
                        userImageUrl = serverConstants.shareUrl + imageDTOArrayList.get(count);
                        Glide.with(getApplicationContext())
                                .load(serverConstants.shareUrl + imageDTOArrayList.get(count))
                                .fitCenter()
                                .placeholder(R.drawable.noimg)
                                .into(ivUserImages);
                    }
                }
            }
        });

        ivUserImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userImageDTOArrayList.size()>0){
                    Intent intent = new Intent(ProfileActivity.this, FullImageShowActivity.class);
                    intent.putExtra("imageUrl", userImageUrl);
                    intent.putExtra("imageId", userImageDTOArrayList.get(count).getId().toString());
                    startActivity(intent);
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
                    if (imageDTOArrayList.size() > 0) {
                        userImageUrl = serverConstants.shareUrl + imageDTOArrayList.get(count);
                        Glide.with(getApplicationContext())
                                .load(serverConstants.shareUrl + imageDTOArrayList.get(count))
                                .fitCenter()
                                .placeholder(R.drawable.noimg)
                                .into(ivUserImages);
                    }
                }

            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialising intent
                if (imageDTOArrayList.size() < 5) {
                    Intent intent = new Intent();

                    // setting type to select to be image
                    intent.setType("image/*");

                    // allowing multiple image to be selected
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                } else {
                    Toast.makeText(ProfileActivity.this, "Upload only maximum 5 images", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init() {
        showLoader = ShowLoader.getProgressDialog(ProfileActivity.this);
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
        llContact = findViewById(R.id.ll_Contact);

        rlPhoto = findViewById(R.id.rl_photo);
        ivPrevious = findViewById(R.id.iv_previous);
        ivNext = findViewById(R.id.iv_next);
        btUpload = findViewById(R.id.bt_upload);

        tvPersonalInfo = findViewById(R.id.tv_personalInfo);
        tvEducation = findViewById(R.id.tv_education);
        tvFamily = findViewById(R.id.tv_family);
        tvPhoto = findViewById(R.id.tv_photo);
        tvContact = findViewById(R.id.tv_contact);

        ivPersonal = findViewById(R.id.iv_personalInfo);
        ivEducation = findViewById(R.id.iv_education);
        ivFamily = findViewById(R.id.iv_family);
        ivPhoto = findViewById(R.id.iv_photo);
        ivContact = findViewById(R.id.iv_contact);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfileActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        session = new UserSessionManager(this);
        HashMap<String, Object> user = session.getUserDetails();
        userid = String.valueOf(user.get(UserSessionManager.USERID));
        //recyclerView.setAdapter(adminPannelAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

            if (data != null) {
                uploadPhoto(userid, data.getData());
            }
            // Get the Image from data
            /*if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);

                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
                uploadPhoto(userid, mArrayUri.get(0));
            } else {
                Uri imageurl = data.getData();
                *//*mArrayUri.add(imageurl);
                ivUserImages.setImageURI(mArrayUri.get(0));
                position = 0;*//*
                uploadPhoto(userid, data.getData());
            }*/
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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

    public void getProfileData(String userId) {
        if (serverConstants.isConnectingToInternet(ProfileActivity.this)) {
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
                    }
                    if (response.isSuccessful()) {
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
                            if (response.body().getBirthHrs() != null && !response.body().getBirthHrs().equals("") && response.body().getBirthMins() != null && !response.body().getBirthMins().equals("")) {
                                personalinfoList.put(getResources().getString(R.string.birth_time) + "(24 Hrs)" + ": ", response.body().getBirthHrs().toString().trim() + ":" + response.body().getBirthMins().toString().trim());
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
                                familyInfoList.put(getResources().getString(R.string.brothers) + ": ", getResources().getString(R.string.married) + " " + response.body().getBroMarried().trim() + ", " + getResources().getString(R.string.unmarried) + " " + response.body().getBroUnmarried().trim());
                            } else {
                                familyInfoList.put(getResources().getString(R.string.brothersMarried) + ": ", getResources().getString(R.string.married) + "--");
                            }
                            /*if (response.body().getBroUnmarried() != null) {
                                familyInfoList.put(getResources().getString(R.string.brothers) + ": ", getResources().getString(R.string.unmarried) + " " + response.body().getBroUnmarried().trim());
                            } else {
                                familyInfoList.put(getResources().getString(R.string.brothers) + ": ", getResources().getString(R.string.unmarried) + "--");
                            }*/
                            if (response.body().getSisMarried() != null) {
                                familyInfoList.put(getResources().getString(R.string.sisters) + ": ", getResources().getString(R.string.married) + " " + response.body().getSisMarried().trim() + ", " + getResources().getString(R.string.unmarried) + " " + response.body().getSisUnmarried().trim());
                            } else {
                                familyInfoList.put(getResources().getString(R.string.sistersMarried) + ": ", getResources().getString(R.string.married) + "--");
                            }
                           /* if (response.body().getSisUnmarried() != null) {
                                familyInfoList.put(getResources().getString(R.string.sisters) + ": ", getResources().getString(R.string.unmarried) + " " + response.body().getSisUnmarried().trim());
                            } else {
                                familyInfoList.put(getResources().getString(R.string.sisters) + ": ", getResources().getString(R.string.unmarried) + "--");
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
                                    familyInfoList.put(getResources().getString(R.string.telephone) + ": ", response.body().getParentalContactNo1().toString().trim() + ", " + response.body().getParentalContactNo2().toString().trim());
                                } else {
                                    familyInfoList.put(getResources().getString(R.string.telephone) + ": ", response.body().getParentalContactNo1().toString().trim());
                                }
                            }
                            if (response.body().getParentalEmail() != null && !response.body().getParentalEmail().equals("")) {
                                familyInfoList.put("Email id: ", response.body().getParentalEmail().toString().trim());
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
                                    contactInfoList.put(getResources().getString(R.string.telephone1) + ": ", response.body().getParentalContactNo1() + ", " + response.body().getParentalContactNo2());
                                } else {
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
                                    contactInfoList.put(getResources().getString(R.string.telephone2) + ": ", response.body().getAltParentalContactNo1() + "," + response.body().getAltParentalContactNo2());
                                } else {
                                    contactInfoList.put(getResources().getString(R.string.telephone2) + ": ", response.body().getAltParentalContactNo1());
                                }
                            }

                            userInfoAdapter = new UserInfoAdapter(personalinfoList, ProfileActivity.this);
                            recyclerView.setAdapter(userInfoAdapter);

                        } else {
                            Toast.makeText(getApplicationContext(), "No information found", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<UserProfileDto> call, Throwable t) {
                    showLoader.hide();
                }
            });

        } else {
            Toast.makeText(ProfileActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void getUserImages(String userId) {
        if (serverConstants.isConnectingToInternet(ProfileActivity.this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<UserImageDTO>> call = api.getUserImages(userId, "images");
            call.enqueue(new Callback<ArrayList<UserImageDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<UserImageDTO>> call, Response<ArrayList<UserImageDTO>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                if (imageDTOArrayList.size() > 0) {
                                    imageDTOArrayList.clear();
                                    userImageDTOArrayList.clear();
                                }
                                userImageDTOArrayList.addAll(response.body());
                                for (int i = 0; i < response.body().size(); i++) {
                                    imageDTOArrayList.add(response.body().get(i).getFilePath());
                                }

                                if (!islast) {
                                    userImageUrl = serverConstants.shareUrl + imageDTOArrayList.get(0);

                                    Glide.with(getApplicationContext())
                                            .load(serverConstants.shareUrl + imageDTOArrayList.get(0))//"https://staging.samatabhratrumandal.com/"+
                                            .fitCenter()
                                            .placeholder(R.drawable.noimg)
                                            .into(ivUserImages);
                                    count = 0;
                                } else {
                                    userImageUrl = serverConstants.shareUrl + imageDTOArrayList.get(imageDTOArrayList.size() - 1);

                                    Glide.with(getApplicationContext())
                                            .load(serverConstants.shareUrl + imageDTOArrayList.get(imageDTOArrayList.size() - 1))//"https://staging.samatabhratrumandal.com/"+
                                            .fitCenter()
                                            .placeholder(R.drawable.noimg)
                                            .into(ivUserImages);
                                    count = imageDTOArrayList.size() - 1;
                                }
                            } else {
                                imageDTOArrayList.clear();
                                userImageDTOArrayList.clear();
                               // Toast.makeText(getApplicationContext(), "No images found", Toast.LENGTH_SHORT).show();
                                Glide.with(getApplicationContext())
                                        .load(R.drawable.noimg)//"https://staging.samatabhratrumandal.com/"+
                                        .fitCenter()
                                        .placeholder(R.drawable.noimg)
                                        .into(ivUserImages);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<UserImageDTO>> call, Throwable t) {
                    showLoader.hide();
                }
            });

        } else {
            Toast.makeText(ProfileActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadPhoto(String userId, Uri fileUri) {
        if (serverConstants.isConnectingToInternet(ProfileActivity.this)) {

            InputStream iStream = null;
            byte[] bytes = null;
            try {
                iStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
                bytes = getBytes(iStream);
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                String format = s.format(new Date());
                MultipartBody.Part pdfFile = MultipartBody.Part.createFormData("file", format + ".jpg", fileBody);
                  /*  File file = new File(fileUri.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);*/
                showLoader.show();
                APIClient api = ApiFactory.create();
                Call<UploadImageSuccessDTO> call = api.uploadPhoto(userId, pdfFile);
                call.enqueue(new Callback<UploadImageSuccessDTO>() {
                    @Override
                    public void onResponse(Call<UploadImageSuccessDTO> call, Response<UploadImageSuccessDTO> response) {
                        showLoader.hide();
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getUserImages(userId);
                                islast = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadImageSuccessDTO> call, Throwable t) {
                        showLoader.hide();
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024 * 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
            System.gc();
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserImages(userid);
    }
}