package com.yanyiyun.baseutils.library.function.selectImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yanyiyun.baseutils.library.R;

import java.util.List;

public class SelectFileAdapter extends BaseAdapter {
    private Context mContext;
    private List<Floder> data;
    private LayoutInflater inflater;
    private int type;

    public SelectFileAdapter(Context mContext, List<Floder> data,int type) {
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
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.select_file_item,null);
            holder.image_iv=convertView.findViewById(R.id.image_iv);
            holder.name_tv=convertView.findViewById(R.id.name_tv);
            holder.number_tv=convertView.findViewById(R.id.number_tv);
            holder.select_iv=convertView.findViewById(R.id.select_iv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Floder imageFloder=data.get(position);
        RequestOptions options = new RequestOptions().error(R.drawable.image);
        Glide.with(mContext)
                .load(imageFloder.getFirstImagePath())
                .apply(options)
                .into(holder.image_iv);
        holder.name_tv.setText(imageFloder.getName());
        String end;
        if(type==1){
            end=mContext.getResources().getString(R.string.video_number);
        }else {
            end=mContext.getResources().getString(R.string.image_number_);
        }
        holder.number_tv.setText(imageFloder.getCount()+end);
        if(imageFloder.isSelect()){
            holder.select_iv.setImageResource(R.drawable.icon_select);
        }else {
            holder.select_iv.setImageResource(R.drawable.icon_unselect);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView image_iv,select_iv;
        TextView name_tv,number_tv;
    }
}
