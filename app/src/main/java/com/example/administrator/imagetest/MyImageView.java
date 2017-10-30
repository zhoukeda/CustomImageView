package com.example.administrator.imagetest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by dada on 2017/10/26.
 */


@SuppressLint("AppCompatCustomView")
public class MyImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener
        , ViewTreeObserver.OnGlobalLayoutListener, GestureDetector.OnGestureListener {
    private int mode;//判断当前手触摸屏幕的歌数

    private Matrix matrix;
    private String TAG = "MyImageView";
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean once = true;
    private int MAX_SCALE = 4, MIN_SCALE = 4;//最大放大倍数，最小放大倍数
    private RectF rect;//当前图片的矩形 ps:rect与rectf的差别在于精度，牵着是int型，后者是float型
    private String doubleClick = "bigger";
    private float firstScale = 0;//原图与第一次适配屏幕的倍率
    /**
     * 处理矩阵的9个值
     */
    float[] martixValue = new float[9];

    public MyImageView(Context context) {
        super(context);
        matrix = new Matrix();
        gestureDetector = new GestureDetector(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        rect = new RectF();
        doubleClick();
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        gestureDetector = new GestureDetector(this);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        rect = new RectF();
        doubleClick();
    }

    /**
     * 双击放大缩小
     */
    private void doubleClick() {
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float scale = getScale() / firstScale;

                if (doubleClick.equals("bigger")) {
                    matrix.postScale((float) MAX_SCALE / scale, (float) MAX_SCALE / scale, getWidth() / 2, getHeight() / 2);
                    setImageMatrix(matrix);
                    getMatrixRectF();
                    doubleClick = "smaller";
                } else {
                    matrix.postScale((float) 1 / scale, (float) 1 / scale, getWidth() / 2, getHeight() / 2);
                    setImageMatrix(matrix);
                    getMatrixRectF();
                    doubleClick = "bigger";
                }

                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
    }

    /**
     * 获取两点之间的距离
     *
     * @param event
     * @return
     */
    private double setArea(MotionEvent event) {
        float dx = event.getX(0) - event.getX(1);
        float dy = event.getY(0) - event.getY(1);
        return Math.sqrt(Math.sqrt(dx * dx + dy * dy));
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //这个放大缩小是每次进行细微的变化，通过频繁变化，来改变图片大小
        //通过与imageView本身的宽高进行限制，最大不过4倍，最小不过四分之一
        //放大
        if (detector.getScaleFactor() >= 1) {
            if (Math.min(getMatrixRectF().bottom - getMatrixRectF().top, getMatrixRectF().right - getMatrixRectF().left) / getWidth() <= MAX_SCALE) {
                matrix.postScale(detector.getScaleFactor(), detector.getScaleFactor(), getWidth() / 2, getHeight() / 2);
                setImageMatrix(matrix);
                getMatrixRectF();
            }
        } else {
            //缩小
            if (getWidth() / Math.min(getMatrixRectF().bottom - getMatrixRectF().top, getMatrixRectF().right - getMatrixRectF().left) <= MIN_SCALE) {
                matrix.postScale(detector.getScaleFactor(), detector.getScaleFactor(), getWidth() / 2, getHeight() / 2);
                setImageMatrix(matrix);
                getMatrixRectF();
            }
        }

        return true;
    }

    /**
     * 开始缩放手势
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    /**
     * 结束缩放手势
     *
     * @param detector
     */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || scaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onGlobalLayout() {
        if (once) {
            Drawable d = getDrawable();
            if (d == null)
                return;
            //获取imageview宽高
            int width = getWidth();
            int height = getHeight();

            //获取图片宽高
            int startImageWidth = d.getIntrinsicWidth();
            int startImageHeight = d.getIntrinsicHeight();

            float scale = 1.0f;

            //如果图片的宽或高大于屏幕，缩放至屏幕的宽或者高
            if (startImageWidth > width && startImageHeight <= height) {
                scale = (float) width / startImageWidth;
            }
            if (startImageHeight > height && startImageWidth <= width) {
                scale = (float) height / startImageHeight;
            }
            //如果图片宽高都大于屏幕，按比例缩小
            if (startImageWidth > width && startImageHeight > height) {
                scale = Math.min((float) startImageWidth / width, (float) startImageHeight / height);
            }
            if (startImageWidth < width && startImageHeight < height) {
                scale = Math.min((float) width / startImageWidth, (float) height / startImageHeight);
            }
            //将图片移动至屏幕中心
            matrix.postTranslate((width - startImageWidth) / 2, (height - startImageHeight) / 2);
            //然后再放大
            matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            firstScale = scale;
            getMatrixRectF();
            setImageMatrix(matrix);
            once = false;

        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /**
     * ------------------GestureDetector.OnGestureListener--------------------------
     */
    //用户按下屏幕就会触发
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    //如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，
    // 那么onShowPress就会执行
    @Override
    public void onShowPress(MotionEvent e) {

    }

    //用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    //用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //判断宽度或者高度有一个大于控件宽高的
        if (((getMatrixRectF().right-getMatrixRectF().left) > getWidth() || (getMatrixRectF().bottom-getMatrixRectF().top) > getHeight())) {
            matrix.postTranslate(amendment(-distanceX, -distanceY)[0], amendment(-distanceX, -distanceY)[1]);
        }

        setImageMatrix(matrix);
        return false;
    }

    //用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
    @Override
    public void onLongPress(MotionEvent e) {

    }

    //用户按下触摸屏、快速移动后松开,由1个MotionEvent ACTION_DOWN,
    //多个ACTION_MOVE, 1个ACTION_UP触发
    //e1：第1个ACTION_DOWN MotionEvent
    //e2：最后一个ACTION_MOVE MotionEvent
    //velocityX：X轴上的移动速度，像素/秒
    //velocityY：Y轴上的移动速度，像素/秒
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    /**
     * 滑动前判断滑动是否会造成越界，并对最终滑动距离进行修正
     */
    private float[] amendment(float distanceX, float distanceY) {
        float[] dis = new float[]{distanceX, distanceY};
        if ((getMatrixRectF().bottom-getMatrixRectF().top) > getHeight()) {
            if (getMatrixRectF().top + dis[1] > getTop()) {
                dis[1] = getTop() - getMatrixRectF().top;
            }
            if (getMatrixRectF().bottom + dis[1] < getBottom()) {
                dis[1] = getBottom() - getMatrixRectF().bottom;
            }
        }else{
            dis[1] = 0;
        }
        if ((getMatrixRectF().right-getMatrixRectF().left) > getWidth()) {
            if (getMatrixRectF().left + dis[0] > getLeft()) {
                dis[0] = getLeft() - getMatrixRectF().left;
            }
            if (getMatrixRectF().right + dis[0] < getRight()) {
                dis[0] = getRight() - getMatrixRectF().right;
            }
        }else{
            dis[0] = 0;
        }

        return dis;
    }

    /**
     * 获取当前缩放比例
     */
    public float getScale() {
        matrix.getValues(martixValue);
        return martixValue[Matrix.MSCALE_X];
    }
}
