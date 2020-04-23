package com.yanyiyun.function.selectImage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanyiyun.R;
import com.yanyiyun.function.dialog.product.PermissionHintDialog;
import com.yanyiyun.function.permission.Action;
import com.yanyiyun.function.permission.AndPermission;
import com.yanyiyun.function.permission.Rationale;
import com.yanyiyun.function.permission.RequestExecutor;
import com.yanyiyun.tool.CameraTool;
import com.yanyiyun.tool.LocalDataUitlTool;
import com.yanyiyun.tool.ScreenUitl;
import com.yanyiyun.tool.comparator.FileModityTimeComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 从本地选择图片
 */
public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG_IMAGE_NUMBER="image_number";
    public static final String TYPE="select_type";
    /**
     * 选择图片
     */
    public static final int TAG_SELECT_IMAGE=0;
    /**
     * 选择视频
     */
    public static final int TAG_SELECT_VIDEO=1;
    /**
     * 拍照照片
     */
    public static final int TAG_TAKE_IMAGE=2;
    /**
     * 拍照视频
     */
    public static final int TAG_TAKE_VIDEO=3;

    private int SELECT_TYPE=TAG_SELECT_IMAGE;

    private TextView right_tv,filename_tv,title_tv;
    private GridView show_image_gv;
    private RelativeLayout bottom_rl;
    private ProgressDialog mProgressDialog;

    private Context mContext;
    /**
     * 要选择的图片数量
     */
    private int IMAGE_NUMBER=0;
    /**
     * 存储有图片的文件夹
     */
    private List<Floder> mImageFloders = new ArrayList<Floder>();
    /**
     * 已选择的
     */
    private List<IBaseItemEntity> selectItem=new ArrayList<>();

    private SelectImageAdapter adapter;
    private List<IBaseItemEntity> data=new ArrayList<>();
    List<ItemEntity> list;

    private SelectFileAdapter fileAdapter;

    /**
     * 选择文件夹对话框
     */
    private PopupWindow mPopupWindow;
    private LocalDataUitlTool localDataUitlTool;

    private String path;

    private int VIDOE_MAX_CAPACITY=25;
    /**
     * 设置权限返回
     */
    private int SET_PERMISSION_BACK=26;

    /**
     * 是否显示拍摄照片
     */
    private boolean IS_SHOW_TAKE=true;

    /**
     * 列表列数
     */
    private int COLUMN_NUMBER=3;
    /**
     * 页面标题
     */
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_activity);
        mContext=this;
        localDataUitlTool=LocalDataUitlTool.getInstance(mContext);
        initview();
    }

    private void initview() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            IMAGE_NUMBER=bundle.getInt(TAG_IMAGE_NUMBER,0);
            SELECT_TYPE=bundle.getInt(TYPE,TAG_SELECT_IMAGE);
            VIDOE_MAX_CAPACITY=bundle.getInt("videolimit",25);
            IS_SHOW_TAKE=bundle.getBoolean("show_take",true);
            COLUMN_NUMBER=bundle.getInt("column_number",3);
            title=bundle.getString("title");
        }

        findViewById(R.id.left_iv).setOnClickListener(this);

        right_tv=findViewById(R.id.right_tv);
        right_tv.setText(getResources().getString(R.string.finish)+" 0/"+IMAGE_NUMBER);
        right_tv.setOnClickListener(this);

        title_tv=findViewById(R.id.title_tv);
        if(title==null){
            if(SELECT_TYPE==TAG_SELECT_VIDEO){
                title_tv.setText(R.string.select_video);
            }else {
                title_tv.setText(R.string.select_image);
            }
        }else {
            title_tv.setText(title);
        }


        show_image_gv=findViewById(R.id.show_image_gv);

        filename_tv=findViewById(R.id.filename_tv); //文件名称
        filename_tv.setOnClickListener(this);

        bottom_rl=findViewById(R.id.bottom_rl);

        adapter=new SelectImageAdapter(mContext,data,selectItem);
        show_image_gv.setNumColumns(COLUMN_NUMBER);
        show_image_gv.setAdapter(adapter);

        setListener();

        checkPermission();
    }

    /**
     * 设置监听器
     */
    private void setListener(){
        adapter.setOnAdapterProcessListener(new SelectImageAdapter.OnAdapterProcessListener() {
            @Override
            public void taking() {
                if(SELECT_TYPE==TAG_SELECT_VIDEO){  //拍摄视频
                    path=CameraTool.getInstance().takeVideo(SelectImageActivity.this, TAG_SELECT_VIDEO,
                            new CameraTool.PermissionFailListener() {
                                @Override
                                public void failure() {
                                    Toast.makeText(mContext,"获取拍照权限失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {  //拍摄照片
                    path=CameraTool.getInstance().takePicture(SelectImageActivity.this, TAG_SELECT_IMAGE,
                            new CameraTool.PermissionFailListener() {
                                @Override
                                public void failure() {
                                    Toast.makeText(mContext,"获取拍照权限失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            /**
             * 选择的图片数量发生改变
             */
            @Override
            public void selectNumberChange() {
                right_tv.setText(getResources().getString(R.string.finish)+" "+selectItem.size()+"/"+IMAGE_NUMBER);
            }

            @Override
            public int getMaxSelectNumber() {
                return IMAGE_NUMBER;
            }

            @Override
            public int getVideoLimit() {
                return VIDOE_MAX_CAPACITY;
            }

            @Override
            public int getColumn() {
                return COLUMN_NUMBER;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            ArrayList<IBaseItemEntity> items=new ArrayList<>();
            IBaseItemEntity itemEntity=new ItemEntity();
            itemEntity.setPath(path);
            items.add(itemEntity);
            //通知相册更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
            //拍摄照片返回
            if (requestCode==TAG_SELECT_IMAGE){
                if(path!=null){
                    bundle.putParcelableArrayList(AndSelectImage.SELECT_IMAGE,items);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }else if(requestCode==TAG_SELECT_VIDEO){  //拍摄视频返回
                if(path!=null){
                    File file=new File(path);
                    if(file.length()>VIDOE_MAX_CAPACITY*1024*1024){
                        Toast.makeText(mContext,"选择的视频不能大于"+VIDOE_MAX_CAPACITY+"M",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        bundle.putParcelableArrayList(AndSelectImage.SELECT_VIDEO,items);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
            }
        }

        if(requestCode==SET_PERMISSION_BACK){
            checkPermission();
        }
    }

    /**
     * 检查权限
     */
    private void checkPermission(){
        AndPermission.with(this)
                .permission(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action() {  //权限失败
                    @Override
                    public void onAction(List<String> permissions) {
                        AndPermission.defaultSettingDialog(SelectImageActivity.this,
                                SET_PERMISSION_BACK)
                                .setNegativeButton(new PermissionHintDialog.ButtonClickListener() {
                                    @Override
                                    public void onClick() {
                                        finish();
                                    }
                                }).show();
                    }
                })
                .onGranted(new Action() {  //获取权限成功
                    @Override
                    public void onAction(List<String> permissions) {
                        getImage();
                    }
                })
                .rationale(new Rationale() {  //有被拒绝的权限
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        AndPermission.rationaleDialog(mContext,executor)
                                .setNegativeButton( new PermissionHintDialog.ButtonClickListener() {
                                    @Override
                                    public void onClick() {
                                        finish();
                                    }
                                }).show();
                    }
                })
                .start();
    }

    /**
     * 获取图片或者视频
     */
    private void getImage(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(mContext,getResources().getString(R.string.not_storage),Toast.LENGTH_SHORT).show();
        }

        mProgressDialog=ProgressDialog.show(mContext,null,getResources().getString(R.string.loading));

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(SELECT_TYPE==TAG_SELECT_VIDEO){
                    list=localDataUitlTool.getVideotList();  //获取视频
                }else {
                    list=localDataUitlTool.getImageList();  //获取图片
                }
                mImageFloders.clear();
                mImageFloders.addAll(localDataUitlTool.getImageFloders());
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Handler.Callback callback=new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mProgressDialog.dismiss();
            if(mImageFloders.size()==0){
                Toast.makeText(mContext,getResources().getString(R.string.no_image),Toast.LENGTH_SHORT).show();
            }else {
                selectFilePopupWindow();
                updateCurrentListImage(0);
            }
            return false;
        }
    };
    private Handler mHandler=new Handler(callback);

    /**
     *更新当前列表的图片
     * @param position
     */
    private void updateCurrentListImage(int position){
        Floder imageFloder=mImageFloders.get(position);
        filename_tv.setText(imageFloder.getName());
        imageFloder.setSelect(true);
        data.clear();
        if(SELECT_TYPE==TAG_SELECT_VIDEO){  //选择视频
            if(imageFloder.isAll()){
                data.addAll(list);
            }else {
                data.addAll(getFloderVideo(imageFloder));
            }
        }else if(SELECT_TYPE==TAG_SELECT_IMAGE){  //选择照片
            if(imageFloder.isAll()){
                for(int i=1;i<mImageFloders.size();i++){
                    Floder ifloder=mImageFloders.get(i);
                    data.addAll(localDataUitlTool.getFolderImages(ifloder));
                }
            }else {
                data.addAll(localDataUitlTool.getFolderImages(imageFloder));
            }
        }
        Collections.sort(data,new FileModityTimeComparator());
        //设置类型
        for(int i=0;i<data.size();i++){
            IBaseItemEntity ibie=data.get(i);
            ibie.setType(SELECT_TYPE);
        }

        //如果要在第一个Item显示拍照获取照片或者视频 在这里添加数据
        if(IS_SHOW_TAKE){
            ItemEntity ie=new ItemEntity();
            if(SELECT_TYPE==TAG_SELECT_IMAGE){
                ie.setType(TAG_TAKE_IMAGE);
            }else {
                ie.setType(TAG_TAKE_VIDEO);
            }
            data.add(0,ie);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 获得 指定文件夹下的视频列表
     * @param floder
     * @return
     */
    private List<ItemEntity> getFloderVideo(Floder floder){
        List<ItemEntity> l=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            ItemEntity itemEntity=list.get(i);
            if(floder.getDir().equals(new File(itemEntity.getPath()).getParent())){
                l.add(itemEntity);
            }
        }
        return l;
    }

    private View view;
    private ListView listView;
    /**
     * 选择文件夹
     */
    private void selectFilePopupWindow(){
        mPopupWindow=new PopupWindow(mContext);
        view= LayoutInflater.from(mContext).inflate(R.layout.select_file_pop,null);
        listView=view.findViewById(R.id.list_lv);
        fileAdapter=new SelectFileAdapter(mContext,mImageFloders,SELECT_TYPE);
        listView.setAdapter(fileAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();
                for(int i=0;i<mImageFloders.size();i++){
                    Floder mif=mImageFloders.get(i);
                    if(i==position){
                        mif.setSelect(true);
                    }else {
                        mif.setSelect(false);
                    }
                }
                updateCurrentListImage(position);
            }
        });

        mPopupWindow.setContentView(view);
        mPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
        mPopupWindow.setWidth(ScreenUitl.getScreenWidth(mContext));
        mPopupWindow.setHeight((int) (ScreenUitl.getScreenHeight(mContext)*0.7));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.left_iv){  //返回
            finish();
        }else if(v.getId()==R.id.right_tv){  //完成
            if(selectItem.size()>0){
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                ArrayList<ItemEntity> items=new ArrayList<>();
                Iterator iterator=selectItem.iterator();
                while (iterator.hasNext()){
                    items.add((ItemEntity) iterator.next());
                }
                if(SELECT_TYPE==TAG_SELECT_VIDEO){
                    bundle.putParcelableArrayList(AndSelectImage.SELECT_VIDEO,items);
                }else {
                    bundle.putParcelableArrayList(AndSelectImage.SELECT_IMAGE,items);
                }
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }else {
                Toast.makeText(mContext,getResources().getString(R.string.please_select_image),Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.filename_tv){ //选择文件
            if(mImageFloders.size()>0){
                mPopupWindow.showAsDropDown(bottom_rl,0,0);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }else {
                Toast.makeText(mContext,getResources().getString(R.string.no_image),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
