package com.yanyiyun.baseutils.library.view.photoview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class CustomGestureDetector {

    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mActivePointerIndex = 0;

    private Context mContext;
    private OnGestureListener mListener;

    private final ScaleGestureDetector mDetector;

    private VelocityTracker mVelocityTracker;
    private boolean mIsDragging;
    private float mLastTouchX;
    private float mLastTouchY;

    /**
     * 超过这个值则执行拖拽动作
     */
    private final float mTouchSlop;
    private final float mMinimumVelocity;

    public CustomGestureDetector(Context mContext, final OnGestureListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;

        ViewConfiguration configuration=ViewConfiguration.get(mContext);
        mMinimumVelocity=configuration.getScaledMinimumFlingVelocity();
        mTouchSlop=configuration.getScaledTouchSlop();

        ScaleGestureDetector.OnScaleGestureListener mScaleListener=new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor=detector.getScaleFactor();

                if(Float.isNaN(scaleFactor)||Float.isInfinite(scaleFactor)){
                    return false;
                }
                mListener.onScale(scaleFactor,detector.getFocusX(),detector.getFocusY());
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        };

        mDetector=new ScaleGestureDetector(mContext,mScaleListener);
    }

    public boolean onTouchEvent(MotionEvent ev){
        mDetector.onTouchEvent(ev);
        return processTouchEvent(ev);
    }

    private boolean processTouchEvent(MotionEvent ev){
        final int action=ev.getAction();
        switch (action&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mActivePointerId=ev.getPointerId(0);

                mVelocityTracker=VelocityTracker.obtain();
                if(null!=mVelocityTracker){
                    mVelocityTracker.addMovement(ev);
                }

                mLastTouchX=getActiveX(ev);
                mLastTouchY=getActiveY(ev);
                mIsDragging=false;
                break;
                //
            case MotionEvent.ACTION_MOVE:
                final float x=getActiveX(ev);
                final float y=getActiveY(ev);
                final float dx=x-mLastTouchX,dy=y-mLastTouchY;

                if(!isDragging()){
                    mIsDragging=Math.sqrt((dx*dx)+(dy*dy))>=mTouchSlop;
                }

                if(mIsDragging){
                    mListener.onDrag(dx,dy);
                    mLastTouchX=x;
                    mLastTouchY=y;

                    if(null!=mVelocityTracker){
                        mVelocityTracker.addMovement(ev);
                    }
                }
                break;
                //
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId=INVALID_POINTER_ID;
                if(null!=mVelocityTracker){
                    mVelocityTracker.recycle();
                    mVelocityTracker=null;
                }
                break;
                //
            case MotionEvent.ACTION_UP:
                mActivePointerId=INVALID_POINTER_ID;
                if(mIsDragging){
                    mLastTouchX=getActiveX(ev);
                    mLastTouchY=getActiveY(ev);

                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000);

                    final float vX=mVelocityTracker.getXVelocity();
                    final float vY=mVelocityTracker.getYVelocity();

                    if(Math.max(Math.abs(vX),Math.abs(vY))>=mMinimumVelocity){
                        mListener.onFling(mLastTouchX,mLastTouchY,-vX,-vY);
                    }
                }

                if(null!=mVelocityTracker){
                    mVelocityTracker.recycle();
                    mVelocityTracker=null;
                }
                break;
                //
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex=getPointerIndex(ev.getAction());
                final int pointerId=ev.getPointerId(pointerIndex);

                if(pointerId==mActivePointerId){
                    final int newPointerIndex=pointerIndex==0?1:0;
                    mActivePointerId=ev.getPointerId(newPointerIndex);
                    mLastTouchX=ev.getX(newPointerIndex);
                    mLastTouchY=ev.getY(newPointerIndex);
                }
                break;
        }
        return true;
    }

    /**
     * 获得点的X轴坐标
     * @param ev
     * @return
     */
    private float getActiveX(MotionEvent ev){
        try {
            return ev.getX(mActivePointerIndex);
        }catch (Exception e){
            return ev.getX();
        }
    }

    private float getActiveY(MotionEvent ev){
        try {
            return ev.getY(mActivePointerIndex);
        }catch (Exception e){
            return ev.getY();
        }
    }

    public boolean isScaling(){
        return mDetector.isInProgress();
    }

    public boolean isDragging(){
        return mIsDragging;
    }

    private int getPointerIndex(int action) {
        return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }
}
