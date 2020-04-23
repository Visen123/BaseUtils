package com.yanyiyun.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yanyiyun.R;

public class IconView extends View {

    private Context mContext;
    private Paint mPaint;
    private Bitmap mBitmap;
    private String str;
    private float mTextSize;
    private int mTextColor;

    public IconView(Context context) {
        super(context);
        mContext=context;
        initview(null,0);
    }

    public IconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initview(attrs,0);
    }

    public IconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initview(attrs,defStyleAttr);
    }

    private void initview(AttributeSet attrs, int defStyleAttr){
        TypedArray ta=mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.iconview,defStyleAttr,0);

        mBitmap= BitmapFactory.decodeResource(mContext.getResources(),
                ta.getResourceId(R.styleable.iconview_icon_id,0));
        str=ta.getString(R.styleable.iconview_text);
        mTextSize=ta.getDimensionPixelSize(R.styleable.iconview_textsize,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
        mTextColor=ta.getColor(R.styleable.iconview_textcolor,0x333333);

        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG|Paint.LINEAR_TEXT_FLAG);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap,getWidth()/2-mBitmap.getWidth()/2,
                getHeight()/2-mBitmap.getHeight()/2,null);
        canvas.drawText(str,getWidth()/2,mBitmap.getHeight()+
                getHeight()/2-mBitmap.getHeight()/2-mPaint.ascent(),mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureSize(widthMeasureSpec,Ratio.WIDTH),
                getMeasureSize(heightMeasureSpec,Ratio.HEIGHT));
    }

    private enum Ratio{
        WIDTH,HEIGHT
    }

    private int getMeasureSize(int measureSpec,Ratio ratio){

        int result=0;

        int mode=MeasureSpec.getMode(measureSpec);
        int size=MeasureSpec.getSize(measureSpec);

        switch (mode){
            case MeasureSpec.EXACTLY:
                result=size;
                break;

            default:
                if(ratio==Ratio.WIDTH){
                    float textWidth=mPaint.measureText(str);
                    result= textWidth>=mBitmap.getWidth() ? (int) textWidth :
                            mBitmap.getWidth()+getPaddingLeft()+getPaddingRight();
                }else if(ratio==Ratio.HEIGHT){
                    result= (int) ((mPaint.descent()-mPaint.ascent())*2+mBitmap.getHeight()+getPaddingTop()
                                                +getPaddingBottom());
                }

                if(mode==MeasureSpec.AT_MOST){
                    result=Math.min(result,size);
                }
                break;
        }

        return result;
    }
}
