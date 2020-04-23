package com.yanyiyun.baseutils.library.tool;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Xml;
import android.widget.Toast;

import com.yanyiyun.baseutils.library.R;
import com.yanyiyun.baseutils.library.function.selectImage.Floder;
import com.yanyiyun.baseutils.library.function.selectImage.ImageFolderComparator;
import com.yanyiyun.baseutils.library.function.selectImage.ItemEntity;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 获取本地数据工具类  如获取本地图片，本地视频
 */
public class LocalDataUitlTool {

    private List<Floder> mImageFloders=new ArrayList<>();
    private HashSet<String> mFileCache=new HashSet<>();
    private Context mContext;
    private static LocalDataUitlTool instance;

    private LocalDataUitlTool(Context mContext) {
        this.mContext = mContext;
    }

    public static LocalDataUitlTool getInstance(Context context){
        if(instance==null){
            synchronized (LocalDataUitlTool.class){
                if(instance==null){
                    instance=new LocalDataUitlTool(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取本地的视频列表
     * @return
     */
    public  List<ItemEntity> getVideotList(){

        mImageFloders.clear();
        List<ItemEntity> sysVideoList=new ArrayList<>();
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns={MediaStore.Video.Thumbnails.DATA,MediaStore.Video.Thumbnails.VIDEO_ID};
        // 视频其他信息的查询条件
        String[] mediaColumns={MediaStore.Video.Media._ID,MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION};

        Cursor cursor=mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns,null,null,null);
        if(cursor==null){
            return sysVideoList;
        }

        if(cursor.moveToFirst()){
            do{
                ItemEntity info=new ItemEntity();
                int id=cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor=mContext.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns,MediaStore.Video.Thumbnails.VIDEO_ID+"="+id,null,null);
                if(thumbCursor!=null){
                    if(thumbCursor.moveToFirst()){
                        info.setThumb(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                    }
                }
                thumbCursor.close();
                info.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                info.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                sysVideoList.add(info);

                File parentFile=new File(info.getPath()).getParentFile();
                if(parentFile==null){
                    continue;
                }
                String parentPath=parentFile.getAbsolutePath();
                if(mFileCache.contains(parentPath)){
                    for(int i=0;i<mImageFloders.size();i++){
                        Floder floder=mImageFloders.get(i);
                        if(floder.getDir().equals(parentPath)){
                            floder.setCount(floder.getCount()+1);
                        }
                    }
                    continue;
                }else {
                    mFileCache.add(parentPath);
                    addVideoFloder(info.getThumb(),parentFile);
                }
            }while (cursor.moveToNext());
        }
        mFileCache.clear();
        cursor.close();
        Collections.sort(mImageFloders,new ImageFolderComparator());
        addAllImageFolder(mContext.getResources().getString(R.string.all_video));
        mImageFloders.get(0).setCount(sysVideoList.size());
        return sysVideoList;
    }

    /**
     * 获取本地所有图片链接 并且会获取图片存储的文件夹列表
     * @return
     */
    public  List<ItemEntity> getImageList(){
        List<ItemEntity> data=new ArrayList<>();
        Cursor cursor=getImageCursor(mContext);
        mImageFloders.clear();
        while (cursor.moveToNext()){
            String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            ItemEntity ie=new ItemEntity();
            ie.setPath(path);
            data.add(ie);

            File parentFile=new File(path).getParentFile();
            if(parentFile==null){
                continue;
            }
            String parentPath=parentFile.getAbsolutePath();
            if(mFileCache.contains(parentPath)){
                continue;
            }else {
                mFileCache.add(parentPath);
                addImageFloder(path,parentFile);
            }
        }

        mFileCache.clear();
        Collections.sort(mImageFloders,new ImageFolderComparator());
        addAllImageFolder(mContext.getResources().getString(R.string.all_image));

        return data;
    }

    /**
     * 获取视频或者图片文件夹列表
     * 在调用此方法前必须先调用getVideotList或者getImageList
     * @return
     */
    public List<Floder> getImageFloders(){
        return mImageFloders;
    }

    /**
     * 检查有没外包存储
     */
    public  void checkStorage(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(mContext,mContext.getResources().getString(R.string.not_storage),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加所有图片或者视频文件夹
     */
    private void addAllImageFolder(String name){
        Floder imageFloder=new Floder();
        imageFloder.setAll(true);
        imageFloder.setName(name);
        if(mImageFloders.size()>0){
            imageFloder.setFirstImagePath(mImageFloders.get(0).getFirstImagePath());
        }
        mImageFloders.add(0,imageFloder);
    }

    /**
     * 添加图片文件夹
     */
    private void addImageFloder(String path,File parentFile){
        Floder imageFloder=new Floder();
        imageFloder.setFirstImagePath(path);
        imageFloder.setDir(parentFile.getAbsolutePath());
        imageFloder.setName(parentFile.getName());
        String[] childs=getImagesArray(parentFile.getAbsolutePath());
        if(childs!=null&&childs.length>0){
            imageFloder.setCount(childs.length);
        }
        mImageFloders.add(imageFloder);
    }

    private void addVideoFloder(String path,File parentFile){
        Floder imageFloder=new Floder();
        imageFloder.setFirstImagePath(path);
        imageFloder.setDir(parentFile.getAbsolutePath());
        imageFloder.setName(parentFile.getName());
        imageFloder.setCount(1);
        mImageFloders.add(imageFloder);
    }

    /**
     * 获得指定文件夹下的照片
     * @param floder
     * @return
     */
    public List<ItemEntity> getFolderImages(Floder floder){
        List<ItemEntity> list=new ArrayList<>();
        File parentFile=new File(floder.getDir());
        String[] images=getImagesArray(parentFile.getAbsolutePath());
        for(int i=0;i<images.length;i++){
            String thumb=parentFile.getAbsolutePath()+"/"+images[i];
            ItemEntity itemEntity=new ItemEntity();
            itemEntity.setPath(thumb);
            list.add(itemEntity);
        }
        return list;
    }


    /**
     * 获得指定文件夹下的所有图片
     * @param parentPath
     * @return
     */
    public String[] getImagesArray(String parentPath){
        File parentFile=new File(parentPath);
        String[] childs=parentFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".png")||name.endsWith(".jpg")||name.endsWith(".jpeg")){
                    return true;
                }
                return false;
            }
        });
        if(childs==null){
            childs=new String[]{};
        }
        return childs;
    }

    /**
     * 获取本地图片的Cursor
     * @param context
     * @return
     */
    private   Cursor getImageCursor(Context context){
        //查找手机中的jpeg png 格式的图片
        Uri imageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver=context.getContentResolver();
        Cursor cursor=contentResolver.query(imageUri,null,MediaStore.Images.Media.MIME_TYPE+"=? or " +
                MediaStore.Images.Media.MIME_TYPE+"=?",new String[]{"image/jpeg","image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        return cursor;
    }

    /**
     * 读取Assets文件夹下面的文件
     * @param context
     * @param fileName
     * @return
     */
    public String readAssets(Context context,String fileName){
        InputStream is=null;
        String content=null;
        try {
            is= context.getAssets().open(fileName);
            if(is!=null){
                byte[] buffer=new byte[1024];
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while (true){
                    int readLength=is.read(buffer);
                    if(readLength==-1){
                        break;
                    }
                    baos.write(buffer,0,readLength);
                }
                is.close();
                baos.close();
                content=new String(baos.toByteArray());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
            content = null;
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content;
    }

    /**
     * 获取所有的手机联系人
     *
     * @return
     */
    public List<HashMap<String, String>> getAllContacts() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        // 1.获取内容解析者
        ContentResolver resolver = mContext.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts 表的地址 :raw_contacts
        // view_data 表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询 raw_contacts,查询 contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        try {
            // 5.解析 cursor
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 6.获取查询的数据
                    String contact_id = cursor.getString(0);
                    // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
                    // : 查询字段在 cursor 中索引值,一般都是用在查询字段比较多的时候
                    // 判断 contact_id 是否为空
                    if (!TextUtils.isEmpty(contact_id)) {//null   ""
                        // 7.根据 contact_id 查询 view_data 表中的数据
                        // selection : 查询条件
                        // selectionArgs :查询条件的参数
                        // sortOrder : 排序
                        // 空指针: 1.null.方法 2.参数为 null
                        Cursor c = resolver.query(date_uri, new String[]{"data1",
                                        "mimetype"}, "raw_contact_id=?",
                                new String[]{contact_id}, null);
                        HashMap<String, String> map = new HashMap<>();
                        // 8.解析 c
                        if (c != null) {
                            while (c.moveToNext()) {
                                // 9.获取数据
                                String data1 = c.getString(0);
                                String mimetype = c.getString(1);
                                // 10.根据类型去判断获取的 data1 数据并保存
                                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                                    // 电话
                                    map.put("phone", data1);
                                } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                                    // 姓名
                                    map.put("name", data1);
                                }
                            }
                        }
                        // 11.添加到集合中数据
                        list.add(map);
                        // 12.关闭 cursor
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            }
        } finally {
            // 12.关闭 cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取手机短信并保存到xml中
     */
    @SuppressLint("SdCardPath")
    public String getAllSMS() {
        // 1.获取短信
        // 1.1 获取内容解析者
        ContentResolver resolver = mContext.getContentResolver();
        // 1.2 获取内容提供者地址   sms,sms 表的地址:null  不写
        // 1.3 获取查询路径
        Uri uri = Uri.parse("content://sms");
        // 1.4.查询操作
        // projection : 查询的字段
        // selection : 查询的条件
        // selectionArgs : 查询条件的参数
        // sortOrder : 排序
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        // 设置最大进度
        int count = cursor.getCount();//获取短信的个数
        // 2.备份短信
        // 2.1 获取 xml 序列器
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringBuilder resultStr = new StringBuilder();
        try {
            // 2.2 设置 xml 文件保存的路径
            // os : 保存的位置
            // encoding : 编码格式
            xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
            // 2.3 设置头信息
            // standalone : 是否独立保存
            xmlSerializer.startDocument("utf-8", true);
            // 2.4 设置根标签
            xmlSerializer.startTag(null, "smss");
            // 1.5.解析 cursor
            while (cursor.moveToNext()) {
                SystemClock.sleep(1000);
                // 2.5 设置短信的标签
                xmlSerializer.startTag(null, "sms");
                // 2.6 设置文本内容的标签
                xmlSerializer.startTag(null, "address");
                String address = cursor.getString(0);
                // 2.7 设置文本内容
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");
                xmlSerializer.startTag(null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");
                xmlSerializer.startTag(null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");
                xmlSerializer.startTag(null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");
                resultStr.append("address:").append(address)
                        .append("   date:").append(date)
                        .append("  type:").append(type)
                        .append("  body:").append(body)
                        .append("\n");
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            // 2.8 将数据刷新到文件中
            xmlSerializer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return resultStr.toString();
    }
}
