package com.and2long.facecompare.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 取景框
 */
public class FrameView extends View {

    //获取屏幕的宽和高。根据屏幕的宽和高来算取景框的位置
    private int width, height;

    public FrameView(Context context) {
        super(context, null);
    }

    public FrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public FrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private static final String TAG = "FrameView";

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(250);
        paint.setStrokeWidth(3);

        // 下面是取景框的8条线
        // xy的算法是：把屏幕横着(逆时针旋转90度的屏幕)，从左到右是x轴，从上到下是y轴
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //左上
            canvas.drawLine(200, 150, 300, 150, paint);
            canvas.drawLine(200, 150, 200, 250, paint);
            //左下
            canvas.drawLine(200, height - 150, 300, height - 150, paint);
            canvas.drawLine(200, height - 150, 200, height - 250, paint);
            //
            canvas.drawLine(width - 355, 150, width - 455, 150, paint);
            canvas.drawLine(width - 355, 150, width - 355, 250, paint);

            canvas.drawLine(width - 355, height - 150, width - 455, height - 150, paint);
            canvas.drawLine(width - 355, height - 150, width - 355, height - 250, paint);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            canvas.drawLine(200, 150, 300, 150, paint);
            canvas.drawLine(200, 150, 200, 200, paint);

            canvas.drawLine(200, height - 150, 300, height - 150, paint);
            canvas.drawLine(200, height - 150, 200, height - 200, paint);

            canvas.drawLine(width - 355, 150, width - 455, 150, paint);
            canvas.drawLine(width - 355, 150, width - 355, 200, paint);

            canvas.drawLine(width - 355, height - 150, width - 455, height - 150, paint);
            canvas.drawLine(width - 355, height - 150, width - 355, height - 200, paint);
        }

    }
}