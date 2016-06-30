package com.example.administrator.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.example.administrator.test.R;


/**
 * Created by glp on 2016/6/28.
 */

public class DotSeekBar extends SeekBar {

    //结点数量
    int mDotCount;
    //进度条左侧绘制
    Drawable mDrawDotLeft;
    //进度条右侧绘制
    Drawable mDrawDotRight;
    //步长
    int mStep = 0;

    //结点Y坐标
    int mDotY;
    //结点宽度 px
    int mDotWidth;
    //结点高度 px
    int mDotHeight;

    //是否只能落在结点
    boolean mDotOnly;
    //是否精确一点。 默认不精确，有利于用户点击
    boolean mDotExact;

    public final static String Tag = "DotSeekBar";

    public DotSeekBar(Context context) {
        super(context);
    }

    public DotSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        onDotAttr(context,attrs,0);
    }

    public DotSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onDotAttr(context,attrs,defStyleAttr);
    }

    void onDotAttr(Context context,AttributeSet attrs,int defStyleAttr){

        TypedArray a = context.obtainStyledAttributes(attrs
                , R.styleable.DotSeekBar,defStyleAttr, 0);
        try {
            mDotCount = a.getInteger(R.styleable.DotSeekBar_dotCount, 0);
            mDrawDotLeft = a.getDrawable(R.styleable.DotSeekBar_dotLeft);
            mDrawDotRight = a.getDrawable(R.styleable.DotSeekBar_dotRight);
            mDotWidth = a.getDimensionPixelOffset(R.styleable.DotSeekBar_dotWidth,10);
            mDotHeight = a.getDimensionPixelOffset(R.styleable.DotSeekBar_dotHeight,10);
            mDotOnly = a.getBoolean(R.styleable.DotSeekBar_dotOnly,true);
            mDotExact = a.getBoolean(R.styleable.DotSeekBar_dotExact,false);
        } finally {
            a.recycle();
        }

        mStep = getMax() / (mDotCount-1);

    }

    public void setDotCount(int count){
        mDotCount = count;
        mStep = getMax() / (mDotCount-1);
        invalidate();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.e(Tag,"onMeasure widthMeasureSpec="+widthMeasureSpec+",heightMeasureSpec="+heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //Log.e(Tag,"onLayout changed="+(changed?"true":"false")+";left="+left+",top="+top+",right="+right+",bottom="+bottom);

        if(changed)
        {
            mDotY = (bottom+top)/2-top;
        }
    }

    /***
     * 重写设置函数
     * @param progress
     * @param fromUser
     */
    @Override
    public synchronized void setProgress(int progress, boolean fromUser) {

        if(mStep == 0 || !mDotOnly || !fromUser)
            super.setProgress(progress,fromUser);
        else
        {
            int diff;
            if(progress >= getProgress())
            {
                if(!mDotExact && progress-mStep/2 <= getProgress()
                        || mDotExact && progress-mStep < getProgress())
                    return ;
                diff = progress - getProgress()+mStep/2;
            }
            else
            {
                if(!mDotExact && progress+mStep/2 >= getProgress()
                        || mDotExact && progress+mStep > getProgress())
                    return ;
                diff = getProgress() - progress+mStep/2;
                diff = -diff;
            }

            diff = diff/mStep*mStep;
            super.setProgress(getProgress() + diff,fromUser);
        }

    }

    /***
     * 重写绘制函数
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {

        //描绘结点
        if(mDotCount > 0)
        {

            int width = getRight() - getLeft()-getPaddingLeft()-getPaddingRight();
            int splitCount = mDotCount - 1;
            int step = width/splitCount;//结点步长
            float curPercent = getProgress()*1.0f/getMax();//当前进度
            for(int i=0;i<mDotCount;i++)
            {
                float temp = (i*1.0f/splitCount);
                Drawable drawable = curPercent >= temp ? mDrawDotLeft : mDrawDotRight;
                drawDrawable(drawable,canvas,getPaddingLeft()+step*i-mDotWidth/2,mDotY-mDotHeight/2);
            }

        }

        super.onDraw(canvas);

    }

    void drawDrawable(Drawable d,Canvas canvas){
        drawDrawable(d,canvas,0,0);
    }

    void drawDrawable(Drawable d,Canvas canvas,int left){
        drawDrawable(d,canvas,left,0);
    }

    void drawDrawable(Drawable d,Canvas canvas,int left,int top){
        if(d == null || canvas == null)
            return ;

        d.setBounds(left,top,left+mDotWidth,top+mDotHeight);
        //d.getPadding();
        //d.setState();
        //d.setLevel();
        d.draw(canvas);
    }
}
