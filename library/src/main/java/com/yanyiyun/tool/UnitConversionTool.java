package com.yanyiyun.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.yanyiyun.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 单位换算
 */
public class UnitConversionTool {

    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    /**
     * dip  转px
     * @param context
     * @param dpValue
     * @return
     */
    public static float dip2px(Context context,float dpValue){
        return dp2px(context,dpValue);
    }

    /**
     * dp 转 px
     * @param context
     * @param dpValue
     * @return
     */
    public static float dp2px(Context context,float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return dpValue*scale+0.5f;
    }

    /**
     * px  转  dip
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2dip(Context context, float pxValue){
        return px2dp(context,pxValue);
    }

    /**
     * px 转 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2dp(Context context,float pxValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return pxValue/scale+0.5f;
    }

    /**
     * sp  装px
     * @param context
     * @param spValue
     * @return
     */
    public static float sp2px(Context context,float spValue){
        final float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return spValue*fontScale+0.5f;
    }

    /**
     * px 转sp
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2sp(Context context,float pxValue){
        final float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue/fontScale+0.5f;
    }

    /**
     * Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable){
        if(drawable==null){
            return null;
        }

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if(drawable instanceof ColorDrawable){
                bitmap=Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,COLORDRAWABLE_DIMENSION,BITMAP_CONFIG);
            }else {
                bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),BITMAP_CONFIG);
            }
            Canvas canvas=new Canvas(bitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap转drawable
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Context context,Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 字符串  转  InputStream
     * @param s
     * @return
     */
    public static InputStream convertStringToInputStream(String s){
        InputStream is=new ByteArrayInputStream(s.getBytes());
        return is;
    }

    /**
     *
     * @param is  InputStream  转  String
     * @return
     * @throws IOException
     */
    public static String convertInputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int i=-1;
        while ((i=is.read())!=-1){
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     *  InputStream  转  String
     * @param is
     * @return
     */
    public static String convertInputStreamToStringByBR(InputStream is){
        String buf;
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(is,"utf-8"));
            StringBuilder sb=new StringBuilder();
            String line="";
            while ((line=reader.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            buf=sb.toString();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 本地文件转换为字符数组
     * @param filePath
     * @return
     */
    public static byte[] file2Byte(String filePath){
        byte[] buffer=null;
        try {
            File file=new File(filePath);
            FileInputStream fis=new FileInputStream(file);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            byte[] b=new byte[1024];
            int n;
            while ((n=fis.read(b))!=-1){
                bos.write(b,0,n);
            }
            fis.close();
            bos.close();
            buffer=bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    /**
     * 字节数组转换为本地文件
     * @param buf
     * @param filePath
     * @param fileName
     */
    public static void byte2File(byte[] buf,String filePath,String fileName){
        BufferedOutputStream bos=null;
        FileOutputStream fos=null;
        File file=null;

        try {
            File dir=new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){
                dir.mkdirs();
            }

            file=new File(filePath+File.separator+fileName);

            fos=new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * bitmap 转 ByteArray
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmap2ByteArray(Bitmap bitmap,boolean needRecycle){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
        if(needRecycle){
            bitmap.recycle();
        }

        byte[] result=bos.toByteArray();
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 网络url转Bitmap
     * @param url
     * @return
     */
    public static Bitmap url2Bitmap(Context context, String url){
        Bitmap bitmap = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            http.setRequestProperty("Accept-Encoding", "identity");
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, length);
            bitmap = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null){
                    is.close();// 关闭流
                }
                if(bis!=null){
                    bis.close();
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.image);
        }
        return bitmap;
    }

    /**
     * 网络url转Bitmap
     * @param url
     * @param callBack
     */
    public static void url2Bitmap(String url, final url2BitmapCallBack callBack){
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body()!=null){
                    byte[] bytes=response.body().bytes();
                    if(bytes!=null){
                        callBack.success(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                    }else {
                        callBack.fail();
                    }
                }else {
                    callBack.fail();
                }
            }
        });
    }

    /**
     * url转换Bytes
     * @param url
     * @param callBack
     */
    public static void url2Bytes(String url, final url2BytesCallBack callBack){
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body()!=null){
                    byte[] bytes=response.body().bytes();
                    if(bytes!=null){
                        callBack.success(bytes);
                    }else {
                        callBack.fail();
                    }
                }else {
                    callBack.fail();
                }
            }
        });
    }

    public interface url2BitmapCallBack{
        void success(Bitmap bitmap);
        void fail();
    }

    public interface url2BytesCallBack{
        void success(byte[] bytes);
        void fail();
    }
}
