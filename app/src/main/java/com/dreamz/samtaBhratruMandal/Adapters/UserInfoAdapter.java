package com.dreamz.samtaBhratruMandal.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.basicClass> {

    LinkedHashMap<String, String> userInfoList = new LinkedHashMap<>();
    private Context context;

    public UserInfoAdapter(LinkedHashMap<String, String> userInfoList, Context context) {
        this.context = context;
        this.userInfoList = userInfoList;
    }


    @Override
    public UserInfoAdapter.basicClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info_layout, null);
        return new basicClass(inflate);
    }

    @Override
    public void onBindViewHolder(UserInfoAdapter.basicClass holder, @SuppressLint("RecyclerView") int position) {
        /*if (getHashMapKeyFromIndex(userInfoList, position).equals("sistersMarried: ") || getHashMapKeyFromIndex(userInfoList, position).equals("brothersMarried: ")) {
            if (getHashMapKeyFromIndex(userInfoList, position).equals("sistersMarried: ")) {
                holder.tvTitle.setText(context.getResources().getText(R.string.sisters));
            } else {
                holder.tvTitle.setText(context.getResources().getText(R.string.brothers));
            }
        } else {
            holder.tvTitle.setText(getHashMapKeyFromIndex(userInfoList, position));
        }*/
        holder.tvTitle.setText(getHashMapKeyFromIndex(userInfoList, position));
        holder.tvValue.setText(userInfoList.get(getHashMapKeyFromIndex(userInfoList, position)));

    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    public class basicClass extends RecyclerView.ViewHolder {
        TextView tvTitle, tvValue;

        public basicClass(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);

        }
    }

    public static String getHashMapKeyFromIndex(HashMap hashMap, int index) {

        String key = null;
        HashMap<String, String> hs = hashMap;
        int pos = 0;
        for (Map.Entry<String, String> entry : hs.entrySet()) {
            if (index == pos) {
                key = entry.getKey();
            }
            pos++;
        }
        return key;

    }
}
