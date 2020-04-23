package com.yanyiyun.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.yanyiyun.tool.log;

import java.util.Timer;
import java.util.TimerTask;

public class ScrollViewContainer extends RelativeLayout {

    private Context mContext;

    /**
     * 自动上滑
     */
    public static final int AUTO_UP = 0;
    /**
     * 自动下滑
     */
    public static final int AUTO_DOWN = 1;
    /**
     * 动画完成
     */
    public static final int DONE = 2;
    private int state = DONE;

    /**
     * 是否是第一次测量
     */
    private boolean isFirstMeasure=true;

    /**
     * 0当前子ScrollView向下滑动到顶端  上拉
     * 1当前子ScrollView向上滑动到底端  下拉
     * 2子ScrollView未到顶端或者底端
     */
    private final int UP=0,DOWN=1,NORMAL=2;
    /**
     * 当前显示的子ScrollView的状态
     */
    private int CURRENT_SHOW_CHILD_STATUE=NORMAL;

    /**
     * 当前显示的子ScrollView的下标
     */
    private int CURRENT_CHILD_INDEX;

    private float mLastY;

    /**
     * 视图需要移动的距离
     */
    private float mMove;
    /**
     * 动画速度
     */
    public static final float SPEED = 6.5f;

    private VelocityTracker mVelocityTracker;

    private MyTimer mTimer;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(mMove!=0){
                if(state==AUTO_UP){
                    mMove-=SPEED;
                    if(CURRENT_SHOW_CHILD_STATUE==UP){
                        if(mMove<0){
                            mMove=0;
                            mTimer.cancel();
                        }
                    }else if(CURRENT_SHOW_CHILD_STATUE==DOWN){
                        if(mMove<-getMeasuredHeight()){
                            mMove=-getMeasuredHeight();
                            CURRENT_CHILD_INDEX++;
//                            CURRENT_SHOW_CHILD_STATUE=UP;
                            mTimer.cancel();
                        }
                    }
                }else if(state==AUTO_DOWN){
                    mMove+=SPEED;
                    if(CURRENT_SHOW_CHILD_STATUE==UP){
                        if(mMove>getMeasuredHeight()){
                            mMove=getMeasuredHeight();
                            CURRENT_CHILD_INDEX--;
//                            CURRENT_SHOW_CHILD_STATUE=DOWN;
                            mTimer.cancel();
                        }
                    }else if(CURRENT_SHOW_CHILD_STATUE==DOWN){
                        if(mMove>0){
                            mMove=0;
                            mTimer.cancel();
                        }
                    }
                }
            }
            requestLayout();
            return false;
        }
    });

    public ScrollViewContainer(Context context) {
        this(context,null);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initview();
    }

    private void initview() {
        mTimer=new MyTimer(handler);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       if(isFirstMeasure){

           isFirstMeasure=false;

           int count=getChildCount();
           for(int i=0;i<count;i++){
               getChildAt(i).setOnTouchListener(new myOnTouchListener(i));
           }
       }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i=0;i<getChildCount();i++){
            View child=getChildAt(i);
            child.layout(0,getMeasuredHeight()*i+(int)mMove,getMeasuredWidth(),
                    getMeasuredHeight()*(i+1)+(int)mMove);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mLastY=ev.getY();
                if(mVelocityTracker==null){
                    mVelocityTracker=VelocityTracker.obtain();
                }else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);
                break;
                //
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                if(CURRENT_SHOW_CHILD_STATUE==UP&&CURRENT_CHILD_INDEX!=0){
                    float temp=ev.getY()-mLastY;
                    mMove+=temp;
                    log.e("temp:"+temp+",mMove:"+mMove);
                    if(mMove>getMeasuredHeight()){
                        mMove=getMeasuredHeight();
                    }
                    if(temp>=0){
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }else if(CURRENT_SHOW_CHILD_STATUE==DOWN&&CURRENT_CHILD_INDEX!=getChildCount()-1){
                    float temp=ev.getY()-mLastY;
                    mMove+=temp;

                    if(mMove<-getMeasuredHeight()){
                        mMove=-getMeasuredHeight();
                    }

                    if(mMove>0){
                        mMove=0;
                    }
                    if(temp<=0){
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }
                mLastY=ev.getY();
                requestLayout();
                break;
                //
            case MotionEvent.ACTION_UP:
                mLastY=ev.getY();
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(700);

                if(CURRENT_SHOW_CHILD_STATUE==UP&&CURRENT_CHILD_INDEX!=0){
                    if(mMove<=getMeasuredHeight()/2){
                        state=AUTO_UP;
                    }else {
                        state=AUTO_DOWN;
                    }
                }else if(CURRENT_SHOW_CHILD_STATUE==DOWN&&CURRENT_CHILD_INDEX!=getChildCount()-1){
                    if(mMove<=-getMeasuredHeight()/2){
                        state=AUTO_UP;
                    }else {
                        state=AUTO_DOWN;
                    }
                }
                mTimer.schedule(2);
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    private class myOnTouchListener implements OnTouchListener{

        private int position;

        public myOnTouchListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView sv= (ScrollView) getChildAt(position);
            log.e("position:"+position+",ScrollY:"+sv.getScrollY()+",mMove:"+mMove+",CURRENT_CHILD_INDEX:"+CURRENT_CHILD_INDEX);
            if(sv.getScrollY()==0&&CURRENT_CHILD_INDEX==position){
                CURRENT_SHOW_CHILD_STATUE=UP;  //上拉
            }else if(sv.getScrollY()==sv.getChildAt(0).getMeasuredHeight()-sv.getMeasuredHeight()
                    &&CURRENT_CHILD_INDEX==position){
                CURRENT_SHOW_CHILD_STATUE=DOWN;  //下拉
            }else {
                CURRENT_SHOW_CHILD_STATUE=NORMAL;  //什么也不做
            }
            return false;
        }
    }

    class MyTimer{
        private Handler handler;
        private Timer timer;
        private TimerTask task;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer=new Timer();
        }

        public void schedule(long period){
            if(task!=null){
                task.cancel();
                task=null;
            }
            task=new MyTask(handler);
            timer.schedule(task,0,period);
        }

        public void cancel(){
            if(task!=null){
                task.cancel();
                task=null;
            }
        }

        class MyTask extends TimerTask{
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }
        }
    }
}
