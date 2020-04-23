package com.yanyiyun.baseutils.library.view.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.OverScroller;

public class PhotoViewAttacher implements View.OnTouchListener,View.OnLayoutChangeListener{

    /**
     * 默认的最大缩放倍数
     */
    private static float DEFAULT_MAX_SCALE=3.0f;
    /**
     * 默认的中等缩放倍数
     */
    private static float DEFAULT_MID_SCALE=1.75f;
    /**
     * 默认的最小缩放倍数
     */
    private static float DEFAULT_MIN_SCALE=1.0f;
    /**
     * 默认的缩放持续时间
     */
    private static int DEFAULT_ZOOM_DURATION=200;
    /**
     * 缩放持续时间
     */
    private int mZoomDuration=DEFAULT_ZOOM_DURATION;
    /**
     * 最小缩放倍数
     */
    private float mMinScale=DEFAULT_MIN_SCALE;
    /**
     * 中等缩放倍数
     */
    private float mMidScale=DEFAULT_MID_SCALE;
    /**
     * 最大缩放倍数
     */
    private float mMaxScale=DEFAULT_MAX_SCALE;

    private static final int EDGE_NONE = -1;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_RIGHT = 1;
    private static final int EDGE_BOTH = 2;
    private static int SINGLE_TOUCH = 1;

    private int mScrollEdge = EDGE_BOTH;

    private static final int SIXTY_FPS_INTERVAL = 1000 / 60;

    private boolean mAllowParentInterceptOnEdge = true;
    private boolean mBlockParentIntercept = false;

    private float mBaseRotation;

    private ImageView mImageView;

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Matrix mBaseMatrix=new Matrix();
    private final Matrix mDrawMatrix=new Matrix();
    private final Matrix mSuppMatrix=new Matrix();
    private final RectF mDisplayRect=new RectF();
    private final float[] mMatrixValues=new float[9];

    private OnScaleChangedListener mScaleChangeListener;
    private OnMatrixChangedListener mMatrixChangedListener;
    private OnViewDragListener mOnViewDragListener;
    private View.OnLongClickListener mOnLongClickListener;
    private OnSingleFlingListener mOnSingleFlingListener;
    private View.OnClickListener mOnClickListener;
    private OnViewTapListener mOnViewTapListener;
    private OnPhotoTapListener mOnPhotoTapListener;
    private OnOutsidePhotoTapListener mOnOutsidePhotoTapListener;

    private FlingRunnable mCurrentFlingRunnable;

    /**
     * 缩放类型
     */
    private ImageView.ScaleType mScaleType= ImageView.ScaleType.FIT_CENTER;
    /**
     * 是否支持缩放
     */
    private boolean mZoomEnabled=true;

    private CustomGestureDetector mScaleDragDetector;
    private GestureDetector mGestureDetector;

    private OnGestureListener onGestureListener=new OnGestureListener() {
        @Override
        public void onDrag(float dx, float dy) {
            if(mScaleDragDetector.isScaling()){
                return;
            }

            if(mOnViewDragListener!=null){
                mOnViewDragListener.onDrag(dx,dy);
            }
            mSuppMatrix.postTranslate(dx,dy);
            checkAndDisplayMatrix();

            ViewParent parent=mImageView.getParent();
            if(mAllowParentInterceptOnEdge&&!mScaleDragDetector.isScaling()&&!mBlockParentIntercept){
                if(mScrollEdge==EDGE_BOTH||(mScrollEdge==EDGE_LEFT&&dx>=1f)||(mScrollEdge==EDGE_RIGHT&&dx<=-1f)){
                    if(parent!=null){
                        parent.requestDisallowInterceptTouchEvent(false);
                    }
                }
            }else {
                if(parent!=null){
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
        }

        @Override
        public void onFling(float startX, float startY, float velocityX, float velocityY) {
            mCurrentFlingRunnable=new FlingRunnable(mImageView.getContext());
            mCurrentFlingRunnable.fling(getImageViewWidth(mImageView),getImageViewHeight(mImageView),
                    (int)velocityX,(int)velocityY);
            mImageView.post(mCurrentFlingRunnable);
        }

        @Override
        public void onScale(float scaleFactor, float focusX, float focusY) {
            if((getScale()<mMaxScale||scaleFactor<1f)&&(getScale()>mMinScale||scaleFactor>1f)){
                if(mScaleChangeListener!=null){
                    mScaleChangeListener.onScaleChange(scaleFactor,focusX,focusY);
                }
                mSuppMatrix.postScale(scaleFactor,scaleFactor,focusX,focusY);
                checkAndDisplayMatrix();
            }
        }
    };

    public PhotoViewAttacher(final ImageView mImageView) {
        this.mImageView = mImageView;
        mImageView.setOnTouchListener(this);
        mImageView.addOnLayoutChangeListener(this);
        if(mImageView.isInEditMode()){
            return;
        }

        mScaleDragDetector=new CustomGestureDetector(mImageView.getContext(),onGestureListener);
        mGestureDetector=new GestureDetector(mImageView.getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                if(mOnLongClickListener!=null){
                    mOnLongClickListener.onLongClick(mImageView);
                }
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(mScaleChangeListener!=null){
                    if(getScale()>DEFAULT_MIN_SCALE){
                        return false;
                    }
                    if(e1.getPointerCount()>SINGLE_TOUCH||e2.getPointerCount()>SINGLE_TOUCH){
                        return false;
                    }
                    return mOnSingleFlingListener.onFling(e1,e2,velocityX,velocityY);
                }
                return false;
            }
        });
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(mOnClickListener!=null){
                    mOnClickListener.onClick(mImageView);
                }
                final  RectF displayRect=getDisplayRect();
                final float x=e.getX(),y=e.getY();
                if(mOnViewTapListener!=null){
                    mOnViewTapListener.onViewTap(mImageView,x,y);
                }

                if(displayRect.contains(x,y)){
                    float xResult=(x-displayRect.left)/displayRect.width();
                    float yResult=(y-displayRect.top)/displayRect.height();
                    if(mOnPhotoTapListener!=null){
                        mOnPhotoTapListener.onPhotoTap(mImageView,xResult,yResult);
                    }
                    return true;
                }else {
                    if(mOnOutsidePhotoTapListener!=null){
                        mOnOutsidePhotoTapListener.onOutsidePhotoTap(mImageView);
                    }
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                try {
                    float scale=getScale();
                    float x=e.getX();
                    float y=e.getY();
                    if(scale<getMediumScale()){
                        setScale(getMediumScale(),x,y,true);
                    }else if(scale<=getMediumScale()&&scale<getMaximumScale()){
                        setScale(getMaximumScale(),x,y,true);
                    }else {
                        setScale(getMinimumScale(),x,y,true);
                    }
                }catch (ArrayIndexOutOfBoundsException ec){

                }
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handled=false;
        if(mZoomEnabled&&hasDrawable((ImageView) v)){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    ViewParent parent=v.getParent();
                    if(parent!=null){
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    cancelFling();
                    break;
                    //
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(getScale()<mMinScale){
                        RectF rectF=getDisplayRect();
                        if(rectF!=null){
                            v.post(new AnimatedZoomRunnable(rectF.centerX(),rectF.centerY(),
                                    getScale(),mMinScale));
                            handled=true;
                        }
                    }else if(getScale()>mMaxScale){
                        RectF rect=getDisplayRect();
                        if(rect!=null){
                            v.post(new AnimatedZoomRunnable(rect.centerX(),rect.centerY(),
                                    getScale(),mMaxScale));
                            handled=true;
                        }
                    }
                    break;
            }

            if(mScaleDragDetector!=null){
                boolean wasScaling = mScaleDragDetector.isScaling();
                boolean wasDragging = mScaleDragDetector.isDragging();
                handled=mScaleDragDetector.onTouchEvent(event);
                boolean didntScale = !wasScaling && !mScaleDragDetector.isScaling();
                boolean didntDrag = !wasDragging && !mScaleDragDetector.isDragging();
                mBlockParentIntercept = didntScale && didntDrag;
            }

            if(mGestureDetector!=null&&mGestureDetector.onTouchEvent(event)){
                handled=true;
            }
        }
        return handled;
    }

    private boolean hasDrawable(ImageView v){
        return v.getDrawable()!=null;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {
        if(left!=oldLeft||top!=oldTop||right!=oldRight||bottom!=oldBottom){
            updateBaseMatrix(mImageView.getDrawable());
        }
    }

    /**
     * 根据ImageView和图片的实际大小和缩放模式，缩放和移动图片
     * @param drawable
     */
    private void updateBaseMatrix(Drawable drawable){
        if(drawable==null){
            return;
        }

        final float viewWidth=getImageViewWidth(mImageView);
        final float viewHeight=getImageViewHeight(mImageView);
        final float drawableWidth=drawable.getIntrinsicWidth();
        final float drawableHeight=drawable.getIntrinsicHeight();

        mBaseMatrix.reset();

        final float widthScale=viewWidth/drawableWidth;
        final float heightScale=viewHeight/drawableHeight;
        if(mScaleType== ImageView.ScaleType.CENTER){
            mBaseMatrix.postTranslate((viewWidth-drawableWidth)/2,
                    (viewHeight-drawableHeight)/2);
        }else if(mScaleType== ImageView.ScaleType.CENTER_CROP){
            float scale=Math.max(widthScale,heightScale);
            mBaseMatrix.postScale(scale,scale);
            mBaseMatrix.postTranslate((viewWidth-drawableWidth*scale)/2,
                    (viewHeight-drawableHeight*scale)/2);
        }else if(mScaleType== ImageView.ScaleType.CENTER_INSIDE){
            float scale=Math.min(1.0f,Math.min(widthScale,heightScale));
            mBaseMatrix.postScale(scale,scale);
            mBaseMatrix.postTranslate((viewWidth-drawableWidth*scale)/2,
                    (viewHeight-drawableHeight*scale)/2);
        }else {
            RectF mTempSrc=new RectF(0,0,drawableWidth,drawableHeight);
            RectF mTempDst=new RectF(0,0,viewWidth,viewHeight);

            if((int)mBaseRotation%180!=0){
                mTempSrc=new RectF(0,0,drawableHeight,drawableWidth);
            }

            switch (mScaleType){
                case FIT_CENTER:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst, Matrix.ScaleToFit.CENTER);
                    break;
                    //
                case FIT_START:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst, Matrix.ScaleToFit.START);
                    break;
                    //
                case FIT_END:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst, Matrix.ScaleToFit.END);
                    break;
                    //
                case FIT_XY:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst, Matrix.ScaleToFit.FILL);
                    break;
                    default:
                        break;
            }
        }
        resetMatrix();
    }


    /**
     * 重置Matrix
     */
    private void resetMatrix(){
        mSuppMatrix.reset();
        setRotationBy(mBaseRotation);
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }

    /**
     * 设置旋转度数
     * @param degress
     */
    public void setRotationBy(float degress){
        mSuppMatrix.postRotate(degress%360);
        checkAndDisplayMatrix();
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener){
        mGestureDetector.setOnDoubleTapListener(newOnDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangeListener){
        this.mScaleChangeListener=onScaleChangeListener;
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener){
        mOnSingleFlingListener=onSingleFlingListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener mMatrixChangedListener) {
        this.mMatrixChangedListener = mMatrixChangedListener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mOnPhotoTapListener = listener;
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener mOutsidePhotoTapListener) {
        mOnOutsidePhotoTapListener = mOutsidePhotoTapListener;
    }

    public void setOnViewTapListener(OnViewTapListener listener) {
        mOnViewTapListener = listener;
    }

    public void setOnViewDragListener(OnViewDragListener listener) {
        mOnViewDragListener = listener;
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAllowParentInterceptOnEdge = allow;
    }

    public void setMinimumScale(float minimumScale) {
        checkZoomLevels(minimumScale, mMidScale, mMaxScale);
        mMinScale = minimumScale;
    }

    public void setMediumScale(float mediumScale) {
        checkZoomLevels(mMinScale, mediumScale, mMaxScale);
        mMidScale = mediumScale;
    }

    public void setMaximumScale(float maximumScale) {
        checkZoomLevels(mMinScale, mMidScale, maximumScale);
        mMaxScale = maximumScale;
    }

    @Deprecated
    public boolean isZoomEnabled() {
        return mZoomEnabled;
    }

    static void checkZoomLevels(float minZoom, float midZoom,
                                float maxZoom) {
        if (minZoom >= midZoom) {
            throw new IllegalArgumentException(
                    "Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value");
        } else if (midZoom >= maxZoom) {
            throw new IllegalArgumentException(
                    "Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value");
        }
    }

    /**
     * 获得Imageview的实际Matrix
     * @return
     */
    private Matrix getDrawMatrix(){
        mDrawMatrix.set(mBaseMatrix);
        mDrawMatrix.postConcat(mSuppMatrix);
        return mDrawMatrix;
    }

    public Matrix getImageMatrix() {
        return mDrawMatrix;
    }

    public void setZoomTransitionDuration(int milliseconds) {
        this.mZoomDuration = milliseconds;
    }

    /**
     * 检查Matrix,并且把Matrix设置到ImageView中
     */
    private void checkAndDisplayMatrix(){
        if(checkMatrixBounds()){
            setImageViewMatrix(getDrawMatrix());
        }
    }

    /**
     * 位图进行缩放后，检查他的偏移量，并且把他移动到正常的位置
     * @return
     */
    private boolean checkMatrixBounds(){
        //获得当前图片所占的范围
        final  RectF rect=getDisplayRect(getDrawMatrix());
        if(rect==null){
            return false;
        }
        final float height=rect.height();
        final float width=rect.width();
        float deltaX=0,deltaY=0;
        final int viewHeight=getImageViewHeight(mImageView);
        if(height<=viewHeight){
            switch (mScaleType){
                case FIT_START:
                    deltaY=-rect.top;
                    break;
                    //
                case FIT_END:
                    deltaY=viewHeight-height-rect.top;
                    break;
                    //
                default:
                    deltaY=(viewHeight-height)/2-rect.top;
                    break;
            }
        }else if(rect.top>0){
            deltaY=-rect.top;
        }else if(rect.bottom<viewHeight){
            deltaY=viewHeight-rect.bottom;
        }

        int viewWidth=getImageViewWidth(mImageView);
        if(width<viewWidth){
            switch (mScaleType){
                case FIT_START:
                    deltaX=-rect.left;
                    break;
                    //
                case FIT_END:
                    deltaX=viewWidth-width-rect.left;
                    break;
                    //
                default:
                    deltaX=(viewWidth-width)/2-rect.left;
                    break;
            }
            mScrollEdge=EDGE_BOTH;
        }else if(rect.left>0){
            deltaX=-rect.left;
            mScrollEdge=EDGE_LEFT;
        }else if(rect.right<viewWidth){
            deltaX=viewWidth-rect.right;
            mScrollEdge=EDGE_RIGHT;
        }else {
            mScrollEdge=EDGE_NONE;
        }
        mSuppMatrix.postTranslate(deltaX,deltaY);
        return true;

    }

    /**
     * 把Matrix设置到Imageview中
     * @param matrix
     */
    private void setImageViewMatrix(Matrix matrix){
        mImageView.setImageMatrix(matrix);
        if(mMatrixChangedListener!=null){
            RectF displayRect=getDisplayRect(matrix);
            if(displayRect!=null){
                mMatrixChangedListener.onMatrixChanged(displayRect);
            }
        }
    }

    public void setZoomInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (isSupportedScaleType(scaleType) && scaleType != mScaleType) {
            mScaleType = scaleType;
            update();
        }
    }

    public boolean isZoomable() {
        return mZoomEnabled;
    }

    public void setZoomable(boolean zoomable) {
        mZoomEnabled = zoomable;
        update();
    }

    public void getDisplayMatrix(Matrix matrix) {
        matrix.set(getDrawMatrix());
    }

    public void getSuppMatrix(Matrix matrix) {
        matrix.set(mSuppMatrix);
    }

    public void update() {
        if (mZoomEnabled) {
            // Update the base matrix using the current drawable
            updateBaseMatrix(mImageView.getDrawable());
        } else {
            // Reset the Matrix...
            resetMatrix();
        }
    }

    private boolean isSupportedScaleType(final ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            return false;
        }
        switch (scaleType) {
            case MATRIX:
                throw new IllegalStateException("Matrix scale type is not supported");
        }
        return true;
    }

    /**
     * 获取连点直线的缩放值
     * @return
     */
    public float getScale(){
        return (float) Math.sqrt((float)Math.pow(getValue(mSuppMatrix,Matrix.MSCALE_X),2)+
                (float)Math.pow(getValue(mSuppMatrix,Matrix.MSCALE_Y),2));
    }

    public void setScale(float scale) {
        setScale(scale, false);
    }

    public void setScale(float scale, boolean animate) {
        setScale(scale,
                (mImageView.getRight()) / 2,
                (mImageView.getBottom()) / 2,
                animate);
    }

    public void setScale(float scale,float focalX,float focalY,boolean animate){
        if(scale<mMinScale||scale>mMaxScale){
            throw new IllegalArgumentException("Scale must be within the range of minScale and maxScale");
        }
        if(animate){
            mImageView.post(new AnimatedZoomRunnable(getScale(),scale,focalX,focalY));
        }else {
            mSuppMatrix.setScale(scale,scale,focalX,focalY);
            checkAndDisplayMatrix();
        }
    }

    /**
     * 获取矩阵中的某个值
     * @param matrix
     * @param whichValue
     * @return
     */
    private float getValue(Matrix matrix,int whichValue){
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    public void setRotationTo(float degrees) {
        mSuppMatrix.setRotate(degrees % 360);
        checkAndDisplayMatrix();
    }

    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        checkZoomLevels(minimumScale, mediumScale, maximumScale);
        mMinScale = minimumScale;
        mMidScale = mediumScale;
        mMaxScale = maximumScale;
    }

    /**
     * 获得图片所在的范围
     * @param matrix
     * @return
     */
    private RectF getDisplayRect(Matrix matrix){
        Drawable d=mImageView.getDrawable();
        if(d!=null){
            mDisplayRect.set(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            matrix.mapRect(mDisplayRect);
            return mDisplayRect;
        }
        return null;
    }

    public RectF getDisplayRect(){
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    public boolean setDisplayMatrix(Matrix finalMatrix) {
        if (finalMatrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if (mImageView.getDrawable() == null) {
            return false;
        }
        mSuppMatrix.set(finalMatrix);
        checkAndDisplayMatrix();
        return true;
    }

    private void cancelFling(){
        if (mCurrentFlingRunnable != null) {
            mCurrentFlingRunnable.cancelFling();
            mCurrentFlingRunnable = null;
        }
    }

    public float getMinimumScale(){
        return mMinScale;
    }

    public float getMediumScale(){
        return mMidScale;
    }

    public float getMaximumScale(){
        return mMaxScale;
    }

    private int getImageViewWidth(ImageView imageView){
        return imageView.getWidth()-imageView.getPaddingLeft()-imageView.getPaddingRight();
    }

    private int getImageViewHeight(ImageView imageView){
        return imageView.getHeight()-imageView.getPaddingTop()-imageView.getPaddingBottom();
    }

    /**
     * 当ACTION_UP 和ACTION_CANCEL时用户结束的操作视图，如果用户的此处操作大于mMaxScale或者小于mMidScale，则
     * 要自动给他缩放到mMidScale或者mMaxScale。
     */
    public class AnimatedZoomRunnable implements Runnable {
        /**
         * 缩放的中点点坐标
         */
        private final float mFocalX, mFocalY;
        /**
         * 动画开始的时间
         */
        private final long mStartTime;
        /**
         * 动画开始和结束的缩放值
         */
        private final float mZoomStart, mZoomEnd;


        public AnimatedZoomRunnable(float mFocalX, float mFocalY, float mZoomStart,float mZoomEnd) {
            this.mFocalX = mFocalX;
            this.mFocalY = mFocalY;
            this.mStartTime = System.currentTimeMillis();
            this.mZoomStart = mZoomStart;
            this.mZoomEnd = mZoomEnd;
        }

        @Override
        public void run() {
            float t=interpolate();
            float scale=mZoomStart+t*(mZoomEnd-mZoomStart);
            float deltaScale=scale/getScale();
            onGestureListener.onScale(deltaScale,mFocalX,mFocalY);

            //当t<1时，动画还未结束在调用次Runnable继续执行
            if(t<1f){
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
                    mImageView.postOnAnimation(this);
                }else {
                    mImageView.postDelayed(this,SIXTY_FPS_INTERVAL);
                }
            }
        }

        /**
         * 获得当前时间完成动画进度的百分比
         * @return
         */
        private float interpolate(){
            float t=1f*(System.currentTimeMillis()-mStartTime)/mZoomDuration;
            t=Math.min(1f,t);
            t=mInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class FlingRunnable implements Runnable{

        private final OverScroller mScroller;
        private int mCurrentX,mCurrentY;

        public FlingRunnable(Context context) {
            mScroller=new OverScroller(context);
        }

        public void cancelFling(){
            mScroller.forceFinished(true);
        }

        public void fling(int viewWidth,int viewHeight,int veloctiyX,int velocityY){
            final RectF rect=getDisplayRect();
            if(rect==null){
                return;
            }
            final int startX=Math.round(-rect.left);
            final int minX,maxX,minY,maxY;
            if(viewWidth<rect.width()){
                minX=0;
                maxX=Math.round(rect.width()-viewWidth);
            }else {
                minX=maxX=startX;
            }

            final int startY=Math.round(-rect.top);
            if(viewHeight<rect.height()){
                minY=0;
                maxY=Math.round(rect.height()-viewHeight);
            }else {
                minY=maxY=startY;
            }

            mCurrentX=startX;
            mCurrentY=startY;
            if(startX!=maxX||startY!=maxY){
                mScroller.fling(startX,startY,veloctiyX,velocityY,minX,maxX,minY,maxY,0,0);
            }
        }

        @Override
        public void run() {
            if(mScroller.isFinished()){
                return;
            }

            if(mScroller.computeScrollOffset()){
                final int newX=mScroller.getCurrX();
                final int newY=mScroller.getCurrY();
                mSuppMatrix.postTranslate(mCurrentX-newX,mCurrentY-newY);
                checkAndDisplayMatrix();
                mCurrentX=newX;
                mCurrentY=newY;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImageView.postOnAnimation(this);
                } else {
                    mImageView.postDelayed(this, SIXTY_FPS_INTERVAL);
                }
            }
        }
    }
}
