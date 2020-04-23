package com.yanyiyun.baseutils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.library.tool.ScreenUitl;

import java.io.File;
import java.util.ArrayList;

public class SelectImageAndAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> data;
    private LayoutInflater inflater;
    private int type;

    public SelectImageAndAdapter(Context mContext, ArrayList<String> data,int type) {
        this.mContext = mContext;
        this.data = data;
        this.type=type;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.select_image_item,null);
            holder=new ViewHolder();
            holder.image_iv=convertView.findViewById(R.id.image_iv);
            holder.select_iv=convertView.findViewById(R.id.select_iv);
            holder.item_rl=convertView.findViewById(R.id.item_rl);
            holder.taking_rl=convertView.findViewById(R.id.taking_rl);
            holder.hint_tv=convertView.findViewById(R.id.hint_tv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
//        if(position==0){
//            holder.taking_rl.setVisibility(View.VISIBLE);
//            holder.item_rl.setVisibility(View.GONE);
//            if(type==SelectImageActivity.TAG_SELECT_VIDEO){
//                holder.hint_tv.setText(R.string.taking_video);
//            }else {
//                holder.hint_tv.setText(R.string.taking_pictures);
//            }
//        }else {
//            holder.taking_rl.setVisibility(View.GONE);
//            holder.item_rl.setVisibility(View.VISIBLE);
//
//        }

        holder.taking_rl.setVisibility(View.GONE);
        holder.item_rl.setVisibility(View.VISIBLE);
        holder.image_iv.getLayoutParams().width= ScreenUitl.getScreenWidth(mContext)/3;
        holder.image_iv.getLayoutParams().height=ScreenUitl.getScreenWidth(mContext)/3;
        File file=new File(data.get(position));
        RequestOptions options = new RequestOptions().error(R.mipmap.image).placeholder(R.mipmap.image);
        Glide.with(mContext)
                .load(file)
                .apply(options)
                .into(holder.image_iv);
        holder.select_iv.setVisibility(View.GONE);
        return convertView;
    }

    private class ViewHolder{
        ImageView image_iv,select_iv;
        RelativeLayout item_rl,taking_rl;
        TextView hint_tv;
    }
}
