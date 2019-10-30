package com.yingguohuo.loadingvew.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yingguohuo.loadingvew.R;


/**
 * Description: 根据UI要求,圆环转动的时候，第一个point从显示到隐藏，第二个point在第一个point运行一段时间后，慢慢显示，再慢慢隐藏，
 * 第三个point在第二个point运行一段时间后也从隐藏到显示，再慢慢隐藏。
 * <p>
 * Author: yingguohuo
 * Date: 1619-10-28 15:56
 */
public class LoadingView extends View {
    private static final String TAG = "LoadingView";
    Context mContext;
    private float mAngle;
    //    private int getWidth();
    //    private int pointSize;
    private int mFromAngle;//从哪个角度开始

    private RectF mRectF;
    private Paint mCriclePaint;
    private Paint pointOnePaint;
    private Paint pointTwoPaint;
    private Paint pointThreePaint;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingViewStyle);
        //直接跟着view的框高走，这样更统一
//        getWidth() = DensityUtil.dip2px(mContext, typedArray.getDimensionPixelSize(R.styleable.LoadingViewStyle_cricle_size, 17));
//        pointSize = DensityUtil.dip2px(mContext, typedArray.getDimensionPixelSize(R.styleable.LoadingViewStyle_cricle_inner_size, 2));
        mFromAngle = typedArray.getInteger(R.styleable.LoadingViewStyle_begin_angle, 0);
        init();
    }

    public void init() {
        //外围大环paint
        mCriclePaint = new Paint();
        mCriclePaint.setAntiAlias(true);//抗锯齿
        mCriclePaint.setStyle(Paint.Style.STROKE);
        mCriclePaint.setStrokeCap(Paint.Cap.ROUND);//stock形状，默认方形--这样更好看
        mCriclePaint.setColor(mContext.getColor(R.color.white));

        //第一个圆点paint
        pointOnePaint = new Paint();
        pointOnePaint.setAntiAlias(true);//抗锯齿
        pointOnePaint.setStyle(Paint.Style.FILL);
        pointOnePaint.setColor(mContext.getColor(R.color.white));//先设置颜色,再设置alpha

        // 第二个圆点paint
        pointTwoPaint = new Paint();
        pointTwoPaint.setAntiAlias(true);//抗锯齿
        pointTwoPaint.setStyle(Paint.Style.FILL);
        pointTwoPaint.setColor(mContext.getColor(R.color.white));

        // 第三个圆点paint
        pointThreePaint = new Paint();
        pointThreePaint.setAntiAlias(true);//抗锯齿
        pointThreePaint.setStyle(Paint.Style.FILL);
        pointThreePaint.setColor(mContext.getColor(R.color.white));

    }

    /**
     * ObjcetAnimator 回调属性接口
     * UI给的效果是从90度开始，则需要加初始值90，可以由view自定义属性获取
     *
     * @param angle
     */
    public void setRadius(int angle) {
        this.mAngle = (angle + mFromAngle) % 360;//从90度开始
        invalidate();
    }

    /**
     * 主要是三个圆点的角度转换，三个圆点显示隐藏需要根据外环绘制进度来控制
     * 1 外环绘制stroke如果按view大小走，则会有一半stoke在外面，会显示不完全,处理方式：
     * drawArc的Rect上下左右各缩进1/2的storkeWidth
     * <p>
     * 2 UI效果要求 圆环转动的时候，第一个point从显示到隐藏，第二个point在第一个point运行一段时间后，慢慢显示，再慢慢隐藏，
     * 第三个point在第二个point运行一段时间后也从隐藏到显示，再慢慢隐藏
     * <p>
     * 所有view尊从0～360度为运行周期
     *
     * 3 宽度分成16等份，具体分析见图
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        Logger.d(TAG, "radius:" + radius);
        if (mRectF == null) {
            mRectF = new RectF(0 + (1 * (getWidth() / 16)), (1 * (getWidth() / 16)),
                    getWidth() - (1 * (getWidth() / 16)), getHeight() - (1 * (getWidth() / 16)));
        }
        mCriclePaint.setStrokeWidth(2 * (getWidth() / 16));
        canvas.drawArc(mRectF, mFromAngle, mAngle, false, mCriclePaint);

        int onePF = 255 - (255 * (int) (mAngle * 100 / 360)) / 100;//这个得走完隐藏流程360->0度
//        Log.d(TAG, "onePF:" + onePF);
        pointOnePaint.setAlpha(onePF);
        canvas.drawCircle(5 * (getWidth() / 16), getHeight() / 2, 1 * (getWidth() / 16), pointOnePaint);


        //        0~80 80～220 220~360
        float size;
        if (mAngle < 80) {
            size = 0;
        } else if (mAngle < 220) {
            size = (mAngle - 80) / 140 * 360;//这个得走完显示流程0->360度
        } else {
            size = (360 - mAngle) / 140 * 360;//这个得走完隐藏流程360->0度
        }
        int twoPF = 255 * ((int) (size * 100 / 360)) / 100;

        pointTwoPaint.setAlpha(twoPF);
        canvas.drawCircle(8 * (getWidth() / 16), getHeight() / 2, 1 * (getWidth() / 16), pointTwoPaint);


        //        0~120 120～240 240~360
        float sizeThree;
        if (mAngle < 120) {//开始不显示
            sizeThree = 0;
        } else if (mAngle < 240) {//这个得走完显示流程0->360度
            sizeThree = (mAngle - 120) / 120 * 360;
        } else {
            sizeThree = (360 - mAngle) / 120 * 360;//这个得走完隐藏流程360->0度
        }
        int threePF = 255 * ((int) (sizeThree * 100 / 360)) / 100;
        pointThreePaint.setAlpha(threePF);
        canvas.drawCircle(11 * (getWidth() / 16), getHeight() / 2, 1 * (getWidth() / 16), pointThreePaint);
    }
}
