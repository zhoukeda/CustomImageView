package com.example.administrator.imagetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dada on 2017/10/31.
 */

public class ScratchCardView extends View {
    private Canvas mCanvas;//遮罩图层
    private Paint paint;//画笔
    private Bitmap inBitmap, outBitmap;//底层和顶层bitmap
    private Path path;

    public ScratchCardView(Context context) {
        super(context);
        initData();
    }

    public ScratchCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }


    private void initData() {
        path = new Path();
        inBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        outBitmap = Bitmap.createBitmap(inBitmap.getWidth(),
                inBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(outBitmap);
        paint = new Paint();
        paint.setColor(0xffc0c0c0);
        mCanvas.drawRect(0, 0, inBitmap.getWidth(), inBitmap.getHeight(), paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(60);//设置画笔宽度
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(inBitmap, 0, 0, null);
        canvas.drawBitmap(outBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mCanvas.drawPath(path, paint);
        postInvalidate();//刷新UI

        return true;
    }

    /**
     * 相当于用遮罩的颜色重新绘制一遍路径
     */
    public void returnFirst() {
        paint.setXfermode(null);
        paint.setStrokeWidth(paint.getStrokeWidth()+5);
        mCanvas.drawPath(path, paint);
        postInvalidate();//刷新UI
        path.reset();
        paint.setStrokeWidth(paint.getStrokeWidth()-5);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
}
