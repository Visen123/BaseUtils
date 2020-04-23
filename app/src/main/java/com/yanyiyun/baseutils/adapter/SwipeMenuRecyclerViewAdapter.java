package com.yanyiyun.baseutils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;

import java.util.ArrayList;


public class SwipeMenuRecyclerViewAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<String> data;

    public SwipeMenuRecyclerViewAdapter(Context mContext,ArrayList<String> data) {
        this.mContext = mContext;
        this.data=data;
        inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.swipe_menu_recyclerview_item,parent,false);
        ScreenAdapterTools.getInstance().loadView(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.item_tv.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView item_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            item_tv=itemView.findViewById(R.id.item_tv);
        }
    }
}
