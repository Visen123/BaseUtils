package com.yanyiyun.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.yanyiyun.R;

public class CircleTextView extends AppCompatTextView {

    private RectF mBackgroundRect=new RectF();

    private float mBackgroundRadius;

    private Paint mBackgroundPaint=new Paint();
    private Paint mTextPaint=new Paint();

    private float mTextLeft;
    private float mTextBaseY;
    private int mBackgroundColor;

    public CircleTextView(Context context) {
        super(context);
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.CircleTextView,defStyleAttr,0);
        mBackgroundColor=a.getColor(R.styleable.CircleTextView_background_color,Color.RED);
        init();
    }


    private void init(){
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(getCurrentTextColor());
        mTextPaint.setTextSize(getTextSize());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        String text=getText().toString();
        Rect bound=new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),bound);
        if(widthMode!=MeasureSpec.EXACTLY){
            widthSize=getPaddingLeft()+bound.width()+getPaddingRight();
        }
        if(heightMode!=MeasureSpec.EXACTLY){
            heightSize=getPaddingBottom()+bound.height()+getPaddingTop();
        }
        int size=Math.max(widthSize,heightSize);

        mBackgroundRadius=size/2;
        mBackgroundRect.set(0,0,size,size);

        float textWidth=mTextPaint.measureText(getText().toString());
        mTextLeft=(size-textWidth)/2;

        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mBackgroundRect.centerX(),mBackgroundRect.centerY(),
                mBackgroundRadius,mBackgroundPaint);
        mTextBaseY=canvas.getHeight()/2-((mTextPaint.descent()+mTextPaint.ascent())/2);
        canvas.drawText(getText().toString(),mTextLeft,mTextBaseY,mTextPaint);
    }
}
