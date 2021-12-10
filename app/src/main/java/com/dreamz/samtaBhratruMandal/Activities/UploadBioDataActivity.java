package com.dreamz.samtaBhratruMandal.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Adapters.BioDataAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.UploadImageSuccessDTO;
import com.dreamz.samtaBhratruMandal.Models.UserImageDTO;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.dreamz.samtaBhratruMandal.config.App.getContext;

public class UploadBioDataActivity extends AppCompatActivity {
    private TextView tvBrowse, tvShare;
    ImageView ivBrowse;
    private Intent intent;
    private ShowLoader showLoader;
    private String userid;
    private UserSessionManager session;
    private String userDocumentUrl = null;
    private RecyclerView rvBioData;
    private ArrayList<UserImageDTO> userImageDTOArrayList = new ArrayList<>();
    private BioDataAdapter bioDataAdapter;
    private int listSize;

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bio_data);

        Toolbar toolbar = findViewById(R.id.upload_photo_toolbar);
        toolbar.setTitle("BioData");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvBrowse = findViewById(R.id.tv_browse);
        tvShare = findViewById(R.id.tv_share);
        ivBrowse = findViewById(R.id.iv_browse);
        rvBioData = findViewById(R.id.rv_bio_data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvBioData.setLayoutManager(linearLayoutManager);

        showLoader = ShowLoader.getProgressDialog(UploadBioDataActivity.this);

        session = new UserSessionManager(this);
        HashMap<String, Object> user = session.getUserDetails();
        userid = String.valueOf(user.get(UserSessionManager.USERID));

        getUserImages(userid);

        ivBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSize < 5) {
                    startActivityForResult(getFileChooserIntent(), 3);
                } else {
                    Toast.makeText(UploadBioDataActivity.this, "You can upload upto 5 files", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listSize <= 5) {
                    startActivityForResult(getFileChooserIntent(), 3);
                } else {
                    Toast.makeText(UploadBioDataActivity.this, "You can upload upto 5 files", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, userDocumentUrl);
                startActivity(Intent.createChooser(shareIntent, "Share using"));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    File mediaFile = new File(data.getData().getPath().toString());
                    long fileSizeInBytes = mediaFile.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (fileSizeInMB > 5) {
                        Toast.makeText(this, "Video files lesser than 5MB are allowed", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        uploadBioData(userid, data.getData());
                    }
                }
            }
        }
    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        long maxVideoSize = 5 * 1024 * 1024; // 10 MB
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, maxVideoSize);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }

    public void uploadBioData(String userId, Uri fileUri) {
        if (serverConstants.isConnectingToInternet(UploadBioDataActivity.this)) {

            InputStream iStream = null;
            byte[] bytes = null;
            try {
                iStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
                bytes = getBytes(iStream);
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                //String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                String format = s.format(new Date());
                MultipartBody.Part pdfFile = MultipartBody.Part.createFormData("file", format + filePath.substring(filePath.lastIndexOf(".")), fileBody);
                  /*  File file = new File(fileUri.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);*/
                showLoader.show();
                APIClient api = ApiFactory.create();
                Call<UploadImageSuccessDTO> call = api.uploadBioData(userId, pdfFile);
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
                                getUserImages(userid);
                                Toast.makeText(UploadBioDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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

    public void getUserImages(String userId) {
        if (serverConstants.isConnectingToInternet(UploadBioDataActivity.this)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<UserImageDTO>> call = api.getUserImages(userId, "documents");
            call.enqueue(new Callback<ArrayList<UserImageDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<UserImageDTO>> call, Response<ArrayList<UserImageDTO>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (response.body() != null) {
                            listSize = response.body().size();
                            if (response.body().size() > 0) {
                                if (userImageDTOArrayList.size() > 0) {
                                    userImageDTOArrayList.clear();
                                }
                                userImageDTOArrayList.addAll(response.body());
                                bioDataAdapter = new BioDataAdapter(UploadBioDataActivity.this, userImageDTOArrayList, new BioDataAdapter.GetListListner() {
                                    @Override
                                    public void getList() {
                                        getUserImages(userId);
                                    }
                                });
                                rvBioData.setAdapter(bioDataAdapter);
                               /* userDocumentUrl = "https://samatabhratrumandal.com/RestAPI" + response.body().get(response.body().size() - 1).getFilePath();
                                if (userDocumentUrl != null) {
                                    tvShare.setVisibility(View.VISIBLE);
                                }*/


                            } else {
                                Toast.makeText(getApplicationContext(), "No images found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(UploadBioDataActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}