package com.dreamz.samtaBhratruMandal.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dreamz.samtaBhratruMandal.Models.BannerDTO;
import com.dreamz.samtaBhratruMandal.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>
{

    Context context;
    ArrayList<BannerDTO> sliderItem=new ArrayList<>();

    public SliderAdapterExample(Context context, ArrayList<BannerDTO> sliderItem) {
        this.context = context;
        this.sliderItem=sliderItem;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterVH(inflate);
    }


    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position)
    {

        Glide.with(context)
                .load("https://staging.samatabhratrumandal.com/RestAPI"+sliderItem.get(position).getImageURL())
                .optionalCenterCrop()
                .into(viewHolder.imageViewBackground);
        ///viewHolder.heading.setText(sliderItem.get(position).getHeading());

    }

    @Override
    public int getCount()
    {
        return sliderItem.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder
    {
        ImageView imageViewBackground;
        TextView heading;


        public SliderAdapterVH(View itemView)
        {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            heading = itemView.findViewById(R.id.tv_auto_image_slider);
        }
    }

}
