package com.yanyiyun.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dengjinwen on 2018/2/22.
 */
public class HtmlSpanned {
    private Context mContext;
    private static HtmlSpanned instance;

    private HtmlSpanned(){}

    public static HtmlSpanned getInstance(){
        if(instance==null){
            synchronized (HtmlSpanned.class){
                instance=new HtmlSpanned();
            }
        }
        return instance;
    }

    /**
     *
     * @param context
     * @param html  html字符串
     * @param textView  用于显示的textview
     */
    public  void setHtml(Context context,String html, TextView textView){
        mContext=context;
        //消息的内容解析html
        NetworkImageGetter imageGetter = new NetworkImageGetter(textView);
        Spanned spanned;
        if(Build.VERSION.SDK_INT<24){
            spanned= Html.fromHtml(html,imageGetter, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

                }
            });
        }else {
            spanned= Html.fromHtml(html, 0, imageGetter, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

                }
            });
        }
        textView.setText(spanned);
    }

    private class NetworkImageGetter implements Html.ImageGetter {

        TextView textView;

        public NetworkImageGetter(TextView textView) {
            this.textView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            LevelListDrawable d = new LevelListDrawable();
            new LoadImage(textView).execute(source, d);
            return d;
        }
    }

    /**** 异步加载图片 **/
    private final class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;
        private TextView textView;

        public LoadImage(TextView textView) {
            this.textView=textView;
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];

            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
//                log.e("width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
//                log.e("screenwidht:"+ScreenUtils.getScreenWidth(mContext));
//                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                 mDrawable.setBounds(0, 0, ScreenUitl.getScreenWidth(mContext),bitmap.getHeight());
                mDrawable.setLevel(1);
                CharSequence t = textView.getText();
                textView.setText(t);
            }
        }
    }


}
