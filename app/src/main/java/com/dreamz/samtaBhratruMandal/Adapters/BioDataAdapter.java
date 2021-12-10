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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Activities.ViewProfileActivity;
import com.dreamz.samtaBhratruMandal.Activities.WebViewActivity;
import com.dreamz.samtaBhratruMandal.BuildConfig;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.RecievedIntrestDTO;
import com.dreamz.samtaBhratruMandal.Models.ResponseMatchMaking;
import com.dreamz.samtaBhratruMandal.Models.ResponsePayment;
import com.dreamz.samtaBhratruMandal.Models.UserImageDTO;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.ApprovedInterestList;

public class BioDataAdapter extends RecyclerView.Adapter<BioDataAdapter.VH> {

    ArrayList<UserImageDTO> userImageDTOArrayList;
    Context context;
    private GetListListner getListListner;
    ShowLoader showLoader;


    public BioDataAdapter(Context context, ArrayList<UserImageDTO> userImageDTOArrayList, GetListListner getListListner) {
        this.context = context;
        this.userImageDTOArrayList = userImageDTOArrayList;
        this.getListListner = getListListner;
        showLoader = ShowLoader.getProgressDialog(context);
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bio_data_item_layout, null);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
        UserImageDTO userImageDTO = userImageDTOArrayList.get(position);

        holder.tvSrNo.setText(position + 1 + ".");
        holder.tvFileName.setText(userImageDTO.getFileName());

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, BuildConfig.SHARE_URL + userImageDTO.getFilePath());
                context.startActivity(Intent.createChooser(shareIntent, "Share using"));
            }
        });

        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(BuildConfig.SHARE_URL + userImageDTO.getFilePath()));
                context.startActivity(intent);
                /*Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", BuildConfig.SHARE_URL + userImageDTO.getFilePath());
                context.startActivity(intent);*/
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDocument(userImageDTO.getId().toString());
            }
        });

    }

    @Override
    public int getItemCount() {

        return userImageDTOArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvSrNo, tvFileName;
        ImageView ivDelete, ivShare;
        RelativeLayout rlContainer;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tv_sr_no);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivShare = itemView.findViewById(R.id.iv_share);
            rlContainer = itemView.findViewById(R.id.rl_container);
        }
    }

    public interface GetListListner {
        public void getList();
    }

    private void deleteDocument(String documentId) {
        if (serverConstants.isConnectingToInternet(context)) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ResponsePayment> call = api.deleteUserDocument(documentId);
            call.enqueue(new Callback<ResponsePayment>() {
                @Override
                public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(context.getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        getListListner.getList();
                        Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePayment> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
