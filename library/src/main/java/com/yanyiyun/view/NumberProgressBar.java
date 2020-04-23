package com.yanyiyun.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yanyiyun.R;
import com.yanyiyun.tool.UnitConversionTool;

public class NumberProgressBar extends View {

    private Context mContext;

    /**
     * 到达进度条矩形区域
     */
    private RectF reachedRectF=new RectF(0f,0f,0f,0f);
    /**
     * 是否绘制到达进度条  当进度为0时不绘制
     */
    private boolean isDrawReached;
    /**
     * 到达进度条的颜色值
     */
    private int reachedColor;
    /**
     * 到达进度条的高度
     */
    private float reachedHeight;
    /**
     * 到达进度条画笔
     */
    private Paint reachedPaint;


    /**
     * 文本开始位置
     */
    private float textStart;
    /**
     * 文本基线
     */
    private float textBaseline;
    /**
     * 文本宽度
     */
    private float textWidth;
    /**
     * 文本大小
     */
    private float textSize;
    /**
     * 文本颜色
     */
    private int textColor;
    /**
     * 文本画笔
     */
    private Paint textPaint;
    /**
     * 文本后缀
     */
    private String subfix="%";

    /**
     * 文本前缀
     */
    private String prefix="";
    /**
     * 是否需要绘制文本
     */
    private boolean isDrawText;
    /**
     * 文本和进度条的间隔
     */
    private float textOffet;
    /**
     * 当前要绘制的文本
     */
    private String drawtext;


    /**
     * 未到达进度条颜色
     */
    private int unReachedColor;
    /**
     * 未到达进度条的区域
     */
    private RectF unReachedRectF=new RectF(0f,0f,0f,0f);
    /**
     * 未到达进度条画笔
     */
    private Paint unReachedPaint;
    /**
     * 未到达进度条高度
     */
    private float unReachedHeight;
    /**
     * 是否绘制未到达进度条
     */
    private boolean isDrawUnReached;


    /**
     * 当前进度
     */
    private int mCurrentProgress;
    /**
     * 最大进度
     */
    private int maxProgress;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height";
    private static final String INSTANCE_REACHED_BAR_COLOR = "reached_bar_color";
    private static final String INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height";
    private static final String INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_TEXT_VISIBILITY = "text_visibility";

    public NumberProgressBar(Context context) {
        this(context,null);
    }

    public NumberProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public NumberProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;

        TypedArray ta=context.getTheme().obtainStyledAttributes(attrs,R.styleable.NumberProgressBar,
                defStyleAttr,0);

        reachedColor=ta.getColor(R.styleable.NumberProgressBar_reachedColor,
                Color.rgb(66, 145, 241));
        reachedHeight=ta.getDimension(R.styleable.NumberProgressBar_reachedHeight,
                UnitConversionTool.dp2px(mContext,1.5f));

        unReachedColor=ta.getColor(R.styleable.NumberProgressBar_unreachedColor,
                Color.rgb(204, 204, 204));
        unReachedHeight=ta.getDimension(R.styleable.NumberProgressBar_unreachedHeight,
                UnitConversionTool.dp2px(mContext,1.0f));

        textSize= ta.getDimension(R.styleable.NumberProgressBar_drawtextsize,
                UnitConversionTool.sp2px(mContext,10f));
        textColor=ta.getColor(R.styleable.NumberProgressBar_drawtextcolor,
                Color.rgb(66, 145, 241));
        textOffet=ta.getDimension(R.styleable.NumberProgressBar_textoffet,
                UnitConversionTool.dp2px(mContext,3.0f));
        isDrawText=ta.getBoolean(R.styleable.NumberProgressBar_progress_text_visible,true);

        setProgress(ta.getInt(R.styleable.NumberProgressBar_curent_progress,0));
        setMaxProgress(ta.getInt(R.styleable.NumberProgressBar_max_progress,100));

        //初始化画笔
       initPaint();
    }

    @Override
    public int getMinimumHeight() {
        return (int) Math.max(textSize,Math.max(reachedHeight,unReachedHeight));
    }

    @Override
    public int getMinimumWidth() {
        return (int) textSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       setMeasuredDimension(measure(widthMeasureSpec,true),measure(heightMeasureSpec,false));
    }

    private int measure(int measureSpec,boolean isWidth){
        int result=0;
        int mode=MeasureSpec.getMode(measureSpec);
        int size=MeasureSpec.getSize(measureSpec);
        int padding=isWidth? getPaddingLeft()+getPaddingRight():getPaddingBottom()+getPaddingTop();
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else {
            result=isWidth? getMinimumWidth() : getMinimumHeight();
            result+=padding;
            if(mode==MeasureSpec.AT_MOST){
                if(isWidth){
                    result=Math.max(result,size);
                }else {
                    result=Math.min(result,size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isDrawText){
            calculateDrawRectF();
        }else {
            calculateDrawRectFWithoutProgressText();
        }

        if(isDrawReached){
            canvas.drawRect(reachedRectF,reachedPaint);
        }

        if(isDrawUnReached){
            canvas.drawRect(unReachedRectF,unReachedPaint);
        }

        if(isDrawText){
            canvas.drawText(drawtext,textStart,textBaseline,textPaint);
        }
    }

    private void initPaint(){
        reachedPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        reachedPaint.setColor(reachedColor);

        unReachedPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        unReachedPaint.setColor(unReachedColor);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    /**
     * 计算文本绘制区域  有进度文本
     */
    private void calculateDrawRectF(){
        drawtext=String.format("%d",getProgress()*100/getMaxProgress());
        drawtext=prefix+drawtext+subfix;
        textWidth=textPaint.measureText(drawtext);

        if(getProgress()==0){
            isDrawReached=false;
            textStart=getPaddingLeft();
        }else {
            isDrawReached=true;

            reachedRectF.left=getPaddingLeft();
            reachedRectF.top=getHeight()/2.0f-reachedHeight/2.0f;
            reachedRectF.right=(getWidth()-getPaddingLeft()-getPaddingRight())/getMaxProgress()*getProgress();
            reachedRectF.bottom=getHeight()/2.0f+reachedHeight/2.0f;

            textStart= reachedRectF.right+textOffet;
        }

        textBaseline= getHeight()/2.0f-((textPaint.ascent()+textPaint.descent())/2.0f);

        if(textStart+textWidth>=getWidth()-getPaddingRight()){
            textStart=getWidth()-getPaddingRight()-textWidth;
            reachedRectF.right=textStart-textOffet;
        }

        float unreachedStart=textStart+textWidth+textOffet;
        if(unreachedStart>=getWidth()-getPaddingRight()){
            isDrawUnReached=false;
        }else {
            isDrawUnReached=true;
            unReachedRectF.left=unreachedStart;
            unReachedRectF.top=getHeight()/2.0f-unReachedHeight/2.0f;
            unReachedRectF.right=getWidth()-getPaddingRight();
            unReachedRectF.bottom=getHeight()/2.0f+unReachedHeight/2.0f;
        }
    }

    /**
     * 计算文本绘制区域  没有进度文本
     */
    private void calculateDrawRectFWithoutProgressText(){
        reachedRectF.left=getPaddingLeft();
        reachedRectF.top=getHeight()/2.0f-reachedHeight/2.0f;
        reachedRectF.right=(getWidth()-getPaddingRight()-getPaddingLeft())/getMaxProgress()*getProgress();
        reachedRectF.bottom=getHeight()/2.0f+reachedHeight/2.0f;

        unReachedRectF.left=reachedRectF.right;
        unReachedRectF.top=getHeight()/2.0f-unReachedHeight/2.0f;
        unReachedRectF.right=getWidth()-getPaddingRight();
        unReachedRectF.bottom=getHeight()/2.0f+unReachedHeight/2.0f;
    }


    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress){
        if(progress<=maxProgress&&progress>=0){
            mCurrentProgress=progress;
            invalidate();
        }
    }

    /**
     * 获得当前进度值
     * @return
     */
    public int getProgress(){
        return mCurrentProgress;
    }

    /**
     * 设置最大进度值
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress){
        if(maxProgress>0){
            this.maxProgress=maxProgress;
            invalidate();
        }
    }

    /**
     * 获得最大进度值
     * @return
     */
    public int getMaxProgress(){
        return maxProgress;
    }

    /**
     * 获得文本颜色
     * @return
     */
    public int getTextColor(){
        return textColor;
    }

    /**
     * 获得文本大小
     * @return
     */
    public float getProgressTextSize(){
        return textSize;
    }

    public int getUnreachBarColor(){
        return unReachedColor;
    }

    public int getReachedColor(){
        return reachedColor;
    }

    public float getReachedHeight(){
        return reachedHeight;
    }

    public float getUnReachedHeight(){
        return unReachedHeight;
    }

    public void setProgressTextSize(float size){
        textSize=size;
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setProGressTextColor(int color){
        textColor=color;
        textPaint.setColor(textColor);
        invalidate();
    }

    public void setUnReachedColor(int color){
        unReachedColor=color;
        unReachedPaint.setColor(unReachedColor);
        invalidate();
    }

    public void setReachedColor(int color){
        reachedColor=color;
        reachedPaint.setColor(reachedColor);
        invalidate();
    }

    public void setReachedHeight(float height){
        reachedHeight=height;
    }

    public void setUnReachedHeight(float height){
        unReachedHeight=height;
    }

    public void setSuffix(String suffix){
        if(suffix==null){
            this.subfix="";
        }else {
            this.subfix=suffix;
        }
    }

    public String getSuffix(){
        return subfix;
    }

    public void setPrefix(String prefix){
        if(prefix==null){
            this.prefix="";
        }else {
            this.prefix=prefix;
        }
    }

    public String getPrefix(){
        return prefix;
    }

    /**
     * 进度添加
     * @param temp
     */
    public void incrementProgressBy(int temp){
        if(temp>0){
            setProgress(getProgress()+temp);
        }

        if(mListener!=null){
            mListener.onProgressChange(getProgress(),getMaxProgress());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, getReachedHeight());
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, getUnReachedHeight());
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, getReachedColor());
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, getUnreachBarColor());
        bundle.putInt(INSTANCE_MAX, getMaxProgress());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, isDrawText);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            reachedHeight = bundle.getFloat(INSTANCE_REACHED_BAR_HEIGHT);
            unReachedHeight = bundle.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT);
            textColor = bundle.getInt(INSTANCE_REACHED_BAR_COLOR);
            unReachedColor = bundle.getInt(INSTANCE_UNREACHED_BAR_COLOR);
            initPaint();
            setMaxProgress(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            isDrawText=bundle.getBoolean(INSTANCE_TEXT_VISIBILITY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public interface  OnProgressBarListener{
        void  onProgressChange(int current,int max);
    }
    private OnProgressBarListener mListener;
    public void setOnProgressBarListener(OnProgressBarListener listener){
        mListener=listener;
    }
}
