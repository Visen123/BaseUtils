package com.yanyiyun.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.yanyiyun.R;
import com.yanyiyun.tool.UnitConversionTool;

public class CircleImageView extends AppCompatImageView {

    private static final ScaleType SCALE_TYPE=ScaleType.CENTER_CROP;
    /**
     * 默认的边界宽度
     */
    private static final int DEFAULT_BORDER_WIDTH = 0;
    /**
     * 默认的边界颜色
     */
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    /**
     * 圆的默认背景颜色
     */
    private static final int DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT;
    /**
     * 默认边界是否覆盖圆
     */
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    /**
     * 边界宽度
     */
    private int mBorderWidth=DEFAULT_BORDER_WIDTH;
    /**
     * 边界颜色
     */
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    /**
     * 圆背景颜色
     */
    private int mCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR;

    private Bitmap mBitmap;

    private boolean mReady;
    /**
     * 边界是否覆盖圆
     */
    private boolean mBorderOverlay;
    private boolean mSetupPending;

    private final RectF mBorderRect = new RectF();
    private final RectF mDrawableRect = new RectF();

    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final Paint mCircleBackgroundPaint = new Paint();

    private float mDrawableRadius;
    private float mBorderRadius;

    private ColorFilter mColorFilter;

    /**
     * 位图着色器
     */
    private BitmapShader mBitmapShader;
    private final Matrix mShaderMatrix = new Matrix();

    /**
     * 位图的宽高
     */
    private int mBitmapHeight,mBitmapWidth;

    /**
     * 是否不显示圆形转换  默认显示
     */
    private boolean mDisableCircularTransformation;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleImageView,defStyleAttr,
                0);

        mBorderWidth=a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width,DEFAULT_BORDER_WIDTH);
        mBorderColor=a.getColor(R.styleable.CircleImageView_civ_border_color,DEFAULT_BORDER_COLOR);
        mBorderOverlay=a.getBoolean(R.styleable.CircleImageView_civ_border_overlay,DEFAULT_BORDER_OVERLAY);

        if(a.hasValue(R.styleable.CircleImageView_civ_circle_background_color)){
            mCircleBackgroundColor=a.getColor(R.styleable.CircleImageView_civ_circle_background_color
                    ,DEFAULT_CIRCLE_BACKGROUND_COLOR);
        }else if(a.hasValue(R.styleable.CircleImageView_civ_fill_color)){
            mCircleBackgroundColor=a.getColor(R.styleable.CircleImageView_civ_fill_color,
                    DEFAULT_CIRCLE_BACKGROUND_COLOR);
        }

        a.recycle();

        init();
    }

    private void init(){
        super.setScaleType(SCALE_TYPE);

        mReady=true;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            setOutlineProvider(new OutlineProvider());
        }

        if(mSetupPending){
            setup();
            mSetupPending=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mDisableCircularTransformation){
            super.onDraw(canvas);
            return;
        }

        if(mBitmap==null){
            return;
        }

        if(mCircleBackgroundColor!=Color.TRANSPARENT){
            canvas.drawCircle(mDrawableRect.centerX(),mDrawableRect.centerY(),mDrawableRadius,
                    mCircleBackgroundPaint);
        }
        canvas.drawCircle(mDrawableRect.centerX(),mDrawableRect.centerY(),mDrawableRadius,mBitmapPaint);
        if(mBorderWidth>0){
            canvas.drawCircle(mBorderRect.centerX(),mBorderRect.centerY(),mBorderRadius,mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public boolean isDisableCircularTransformation(){
        return mDisableCircularTransformation;
    }

    /**
     * 设置是否不显示圆形转换
     * @param disableCircularTransformation
     */
    public void setDisableCircularTransformation(boolean disableCircularTransformation){
        if(mDisableCircularTransformation==disableCircularTransformation){
            return;
        }
        mDisableCircularTransformation=disableCircularTransformation;
        initializeBitmap();
    }

    /**
     * 初始化位图
     */
    private void initializeBitmap(){
        if(mDisableCircularTransformation){
            mBitmap=null;
        }else {
            mBitmap= UnitConversionTool.drawableToBitmap(getDrawable());
        }
        setup();
    }

    private void setup(){
        if(getWidth()==0&&getHeight()==0){
            return;
        }

        if(mBitmap==null){
            invalidate();
            return;
        }

        mBitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mCircleBackgroundPaint.setStyle(Paint.Style.FILL);
        mCircleBackgroundPaint.setColor(mCircleBackgroundColor);
        mCircleBackgroundPaint.setAntiAlias(true);

        mBitmapHeight=mBitmap.getHeight();
        mBitmapWidth=mBitmap.getWidth();

        mBorderRect.set(calculateBounds());
        mBorderRadius=Math.min((mBorderRect.height()-mBorderWidth)/2,
                (mBorderRect.width()-mBorderWidth)/2);

        mDrawableRect.set(mBorderRect);
        if(!mBorderOverlay&&mBorderWidth>0){
            mDrawableRect.inset(mBorderWidth-1.0f,mBorderWidth-1.0f);
        }
        mDrawableRadius=Math.min(mDrawableRect.height()/2.0f,mDrawableRect.width()/2.0f);

        applyColorFilter();
        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix(){
        float scale;
        float dx=0;
        float dy=0;

        mShaderMatrix.set(null);

        /**
         * mDrawableRect.height()/(float)mBitmapHeight mDrawableRect.width()/(float)mBitmapWidth取
         * 比值更大的作为缩放比
         */
        if(mBitmapWidth*mDrawableRect.height()>mDrawableRect.width()*mBitmapHeight){
            scale=mDrawableRect.height()/(float)mBitmapHeight;
            //以高度的缩放比进行缩放，X轴方向需要偏移
            dx=(mDrawableRect.width()-mBitmapWidth*scale)*0.5f;
        }else {
            scale=mDrawableRect.width()/(float)mBitmapWidth;
            dy=(mDrawableRect.height()-mBitmapHeight*scale)*0.5f;
        }

        mShaderMatrix.setScale(scale,scale);
        mShaderMatrix.postTranslate((int)(dx+0.5f)+mDrawableRect.left,
                (int)(dy+0.5f)+mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    private void applyColorFilter(){
        if(mBitmapPaint!=null){
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    /**
     * 计算位图放置的区域
     * @return
     */
    private RectF calculateBounds(){
        int availableWidth=getWidth()-getPaddingLeft()-getPaddingRight();
        int availableHeight=getHeight()-getPaddingBottom()-getPaddingTop();

        int sideLength=Math.min(availableHeight,availableWidth);

        float left=getPaddingLeft()+(availableWidth-sideLength)/2;
        float top=getPaddingTop()+(availableHeight-sideLength)/2;
        return new RectF(left,top,left+sideLength,top+sideLength);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return inTouchableArea(event.getX(),event.getY())&&super.onTouchEvent(event);
    }

    /**
     * 触摸点是否在区域内
     * @param x
     * @param y
     * @return
     */
    private boolean inTouchableArea(float x,float y){
        return Math.pow(x-mBorderRect.centerX(),2)+Math.pow(y-mBorderRect.centerY(),2)<=Math.pow(mBorderRadius,2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class OutlineProvider extends ViewOutlineProvider{

        @Override
        public void getOutline(View view, Outline outline) {
            Rect bounds=new Rect();
            mBorderRect.roundOut(bounds);
            outline.setRoundRect(bounds,bounds.width()/2.0f);
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if(cf==mColorFilter){
            return;
        }
        mColorFilter=cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    public boolean isBorderOverlay(){
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay){
        if(borderOverlay==mBorderOverlay){
            return;
        }
        mBorderOverlay=borderOverlay;
        setup();
    }

    public int getBorderWidth(){
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth){
        if(borderWidth==mBorderWidth){
            return;
        }
        mBorderWidth=borderWidth;
        setup();
    }

    public int getCircleBackgroundColor() {
        return mCircleBackgroundColor;
    }

    public void setCircleBackgroundColor(@ColorInt int circleBackgroundColor) {
        if (circleBackgroundColor == mCircleBackgroundColor) {
            return;
        }

        mCircleBackgroundColor = circleBackgroundColor;
        mCircleBackgroundPaint.setColor(circleBackgroundColor);
        invalidate();
    }

    public void setCircleBackgroundColorResource(@ColorRes int circleBackgroundRes) {
        setCircleBackgroundColor(getContext().getResources().getColor(circleBackgroundRes));
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

}
