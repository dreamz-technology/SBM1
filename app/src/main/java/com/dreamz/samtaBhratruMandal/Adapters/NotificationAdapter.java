package com.dreamz.samtaBhratruMandal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Models.NotificationModel;
import com.dreamz.samtaBhratruMandal.R;

import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.basicClass> {

    List<NotificationModel> msgList = new ArrayList<>();
    private Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    public void addItem(List<NotificationModel> sliderItem) {
        this.msgList.addAll(sliderItem);
        notifyDataSetChanged();

    }
//    public void delete(int position){
//        msgList.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public NotificationAdapter.basicClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, null);
        return new basicClass(inflate);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.basicClass holder, int position) {
        holder.title.setText(msgList.get(position).title);
        holder.msg.setText(msgList.get(position).message);
        holder.id.setText(msgList.get(position).ID);
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class basicClass extends RecyclerView.ViewHolder {
        TextView title, msg, id;

        public basicClass(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.msg_title);
            msg = itemView.findViewById(R.id.msg_body);
            id = itemView.findViewById(R.id.msg_id);
        }
    }
}
