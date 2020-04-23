package com.yanyiyun.baseutils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.entity.MainItem;

import java.util.ArrayList;

public class MainAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<MainItem> data;

    public MainAdapter(Context mContext, ArrayList<MainItem> data) {
        this.mContext = mContext;
        this.data = data;
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
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.main_item,null);
            holder.title_tv= (TextView) convertView.findViewById(R.id.title_tv);
            holder.desc_tv= (TextView) convertView.findViewById(R.id.desc_tv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        MainItem mi=data.get(position);
        holder.title_tv.setText(mi.getTitle());
        holder.desc_tv.setText(mi.getDesc());
        return convertView;
    }

    private class ViewHolder{
        TextView title_tv,desc_tv;
    }
}
