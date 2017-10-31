package com.example.administrator.imagetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dada on 2017/10/31.
 */

public class ScratchCardView extends View{
    private Path mPath;//手刮动的path，过程
    private Paint mOutterPaint;//绘制mPath的画笔
    private Canvas mCanvas;//临时画布
    private Bitmap mBitmap;//临时图片

    //记录用户path每次的开始坐标值
    private int mLastX;
    private int mLastY;

    private Bitmap mOutterBitmap;//图片遮罩，就是手刮动，要擦掉的那张图

    public ScratchCardView(Context context) {
        this(context, null);
    }

    public ScratchCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得控件的宽高
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //初始化bitmap
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //设置画笔属性
        setupOutPaint();
        mCanvas.drawColor(Color.parseColor("#c0c0c0"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//Mode.DST_OUT改模式就类似橡皮檫，这个属性设置是关键
        canvas.drawBitmap(mOutterBitmap, 0, 0, null);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN://按下
                //记录按下的时候的X和Y值，以便于之后移动的时候绘制
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE://移动
                //拿到用户移动的X绝对值，Y轴绝对值
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);
                //用户滑动超过3像素才会改变，这个可以不做，做只是为了避免很频繁的响应而已。
                if (dx > 3 || dy > 3) {
                    mPath.lineTo(x, y);
                }
                mLastX = x;
                mLastY = y;
                break;
        }
        invalidate();//刷新UI
        return true;
    }

    /**
     * 绘制path（也就是手刮动的path来绘制） 的画笔属性
     * 类似橡皮擦
     */
    private void setupOutPaint() {
        mOutterPaint.setColor(Color.RED);
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND);//设置圆角
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutterPaint.setStyle(Paint.Style.FILL);
        mOutterPaint.setStrokeWidth(60);//设置画笔宽度
    }
    /**
     * 初始化信息
     */
    private void init() {
        mOutterPaint = new Paint();
        mPath = new Path();
        mOutterBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.test);
    }
}
