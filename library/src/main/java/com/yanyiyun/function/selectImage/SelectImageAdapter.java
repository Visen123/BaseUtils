package com.yanyiyun.function.selectImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yanyiyun.R;
import com.yanyiyun.tool.ScreenUitl;

import java.io.File;
import java.util.List;

public class SelectImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<IBaseItemEntity> data;
    private LayoutInflater inflater;
    private List<IBaseItemEntity> selectImages;
    public  OnAdapterProcessListener listener;

    public SelectImageAdapter(Context mContext, List<IBaseItemEntity> data,
                              List<IBaseItemEntity> selectImages) {
        this.mContext = mContext;
        this.data = data;
        this.selectImages=selectImages;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        final IBaseItemEntity itemEntity=data.get(position);
        int type=itemEntity.getType();
        if(type==SelectImageActivity.TAG_TAKE_IMAGE||type==SelectImageActivity.TAG_TAKE_VIDEO){  //拍照
            pressTake(holder,itemEntity);
        }else {  //显示照片或者视频
            pressImageOrVideo(holder,itemEntity);
        }
        return convertView;
    }

    /**
     * type为选择照片或者视频时的操作
     * @param holder
     * @param itemEntity
     */
    private void pressImageOrVideo(ViewHolder holder,final IBaseItemEntity itemEntity){

        int width= ScreenUitl.getScreenWidth(mContext)/listener.getColumn()-20;

        holder.item_rl.setVisibility(View.VISIBLE);
        holder.taking_rl.setVisibility(View.GONE);
        holder.image_iv.getLayoutParams().height=width;

        final File file=new File(itemEntity.getPath());
        Glide.with(mContext).load(file).into(holder.image_iv);
        if(itemEntity.isSelect()){
            holder.select_iv.setImageResource(R.drawable.icon_select);
        }else {
            holder.select_iv.setImageResource(R.drawable.icon_unselect);
        }


        final ImageView select=holder.select_iv;
        holder.select_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是选择视频，则需要限制视频大小
                if(itemEntity.getType()==SelectImageActivity.TAG_SELECT_VIDEO){
                    if(file.length()>listener.getVideoLimit()*1024*1024){
                        Toast.makeText(mContext,"选择的视频不能大于"+listener.getVideoLimit()+"M",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(listener.getMaxSelectNumber()>1){  //多选
                    multiSelect(select,itemEntity);
                }else if(listener.getMaxSelectNumber()==1){  //单选
                    theRadio(itemEntity);
                }


                listener.selectNumberChange();
            }
        });
    }

    /**
     * 多选
     * @param select
     * @param itemEntity
     */
    private void multiSelect(ImageView select,IBaseItemEntity itemEntity){
        if(itemEntity.isSelect()){
            selectImages.remove(itemEntity);
            itemEntity.setSelect(false);
            select.setImageResource(R.drawable.icon_unselect);
        }else {
            if(selectImages.size()<listener.getMaxSelectNumber()){
                itemEntity.setSelect(true);
                select.setImageResource(R.drawable.icon_select);
                selectImages.add(itemEntity);
            }else {
                Toast.makeText(mContext,"您最多可选数量为"+listener.getMaxSelectNumber(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 单选
     * @param itemEntity
     */
    private void theRadio(IBaseItemEntity itemEntity){
        if(!itemEntity.isSelect()){
            itemEntity.setSelect(true);
            selectImages.add(itemEntity);

            for(int i=0;i<data.size();i++){
                IBaseItemEntity ib=data.get(i);
                if(ib!=itemEntity&&ib.isSelect()){
                    ib.setSelect(false);
                    selectImages.remove(ib);
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * type为拍照时的操作
     * @param holder
     */
    private void pressTake(ViewHolder holder,final IBaseItemEntity itemEntity){
        int width= ScreenUitl.getScreenWidth(mContext)/listener.getColumn()-20;

        holder.item_rl.setVisibility(View.GONE);
        holder.taking_rl.setVisibility(View.VISIBLE);
        holder.taking_rl.getLayoutParams().height=width;

        if(itemEntity.getType()==SelectImageActivity.TAG_SELECT_VIDEO){
            holder.hint_tv.setText(R.string.taking_video);
        }else {
            holder.hint_tv.setText(R.string.taking_pictures);
        }
        holder.taking_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.taking();
            }
        });
    }

    public void setOnAdapterProcessListener(OnAdapterProcessListener l){
        listener=l;
    }
    public interface OnAdapterProcessListener{
        /**
         * 拍照
         */
        public void taking();

        /**
         * 选择数量发生变化
         */
        public void selectNumberChange();

        /**
         * 获得最大选择数量
         * @return
         */
        public int getMaxSelectNumber();

        /**
         * 获取视频大小限制 单位是MB
         * @return
         */
        public int getVideoLimit();

        /**
         * 获得列数
         * @return
         */
        public int getColumn();
    }

    private class ViewHolder{
        ImageView image_iv,select_iv;
        RelativeLayout item_rl,taking_rl;
        TextView hint_tv;
    }
}
