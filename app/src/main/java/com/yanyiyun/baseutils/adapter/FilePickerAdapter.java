package com.yanyiyun.baseutils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.library.tool.file.FileTypeUtil;

import java.io.File;
import java.util.List;

public class FilePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<File> data;
    private LayoutInflater inflater;
    private onClickListener listener;

    public FilePickerAdapter(Context mContext, List<File> data) {
        this.mContext = mContext;
        this.data = data;
        inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.file_picker_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h= (ViewHolder) holder;
        final File file=data.get(position);
        FileTypeUtil.FileType fileType=FileTypeUtil.getInstance().getFileType(file);
        h.icon_iv.setImageResource(fileType.getIcon());
        h.name_tv.setText(file.getName());
        h.type_tv.setText(fileType.getDescription());
        if(file.isDirectory()){
            h.number_tv.setVisibility(View.VISIBLE);
            h.number_tv.setText("包含"+file.listFiles().length+"个文件");
        }else {
            h.number_tv.setVisibility(View.GONE);
        }

        h.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface onClickListener{
        public void onClick(File file);
    }

    public void setonClickListener(onClickListener l){
        listener=l;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon_iv;
        private TextView name_tv,type_tv,number_tv;
        private LinearLayout item_ll;

        public ViewHolder(View itemView) {
            super(itemView);
            item_ll=itemView.findViewById(R.id.item_ll);
            icon_iv=itemView.findViewById(R.id.icon_iv);
            name_tv=itemView.findViewById(R.id.name_tv);
            type_tv=itemView.findViewById(R.id.type_tv);
            number_tv=itemView.findViewById(R.id.number_tv);
        }
    }
}
