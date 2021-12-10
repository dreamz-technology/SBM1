package com.dreamz.samtaBhratruMandal.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamz.samtaBhratruMandal.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogConfirmYesNo extends BaseAppDialogFragment {

    private OnClickListener onClickListener;

    private TextView tvYes, tvNo, tvMessage;
    private String message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_apply_yes_no, container, false);
        tvYes = view.findViewById(R.id.tv_yes);
        tvNo = view.findViewById(R.id.tv_no);
        tvMessage = view.findViewById(R.id.tv_message);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            message = getArguments().getString("message");
        }

        tvMessage.setText(message);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickYes();
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickNo();
            }
        });
    }


    public void setOnClickListener(OnClickListener onClickHandleListener) {
        this.onClickListener = onClickHandleListener;
    }

    public interface OnClickListener {
        void onClickYes();

        void onClickNo();
    }
}
