package com.example.fengjiening.xxt_app.layout;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

    private Paint paint;
    private point startPoint;
    private point endPoint;
    private float R = 40;//圆角半径
    private Path path;

    public MyLinearLayout(Context context) {
        super(context, null);
        init();
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        //初始化画笔
        paint = new Paint();
        paint.reset();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置填充
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置防抖动
        paint.setDither(true);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        startPoint = new point(20, 20);
        endPoint = new point(getWidth()-20, getHeight()-20);
        paint.setColor(Color.WHITE);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
        paint.setShadowLayer(15F,1F,1F, Color.GRAY);

        //canvas.drawRect(10, 10, getWidth()-10, getHeight()-10, paint);


        path.moveTo(startPoint.getX(), startPoint.getY() + R);
        RectF rectF1 = new RectF(startPoint.getX(), startPoint.getY(), startPoint.getX() + 2*R, startPoint.getY() + 2*R);
        path.arcTo(rectF1, 180, 90,false);

        path.lineTo(endPoint.getX()-R, startPoint.getY());
        RectF rectF2 = new RectF(endPoint.getX() - 2*R, startPoint.getY(), endPoint.getX(), startPoint.getY() + 2*R);
        path.arcTo(rectF2, 270, 90,false);

        path.lineTo(endPoint.getX(), endPoint.getY() - R);
        RectF rectF3 = new RectF(endPoint.getX() - 2*R, endPoint.getY() - 2*R, endPoint.getX(), endPoint.getY());
        path.arcTo(rectF3, 0, 90,false);

        path.lineTo(startPoint.getX()+R, endPoint.getY());
        RectF rectF4 = new RectF(startPoint.getX(), endPoint.getY() - 2*R, startPoint.getX() + 2*R, endPoint.getY());
        path.arcTo(rectF4, 90, 90,false);

        path.lineTo(startPoint.getX(), startPoint.getY() + R);

        canvas.drawPath(path, paint);
    }


    public class point {
        private float x;
        private float y;

        public void setX(float x) {
            this.x = x;
        }

        public float getX() {
            return x;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getY() {
            return y;
        }

        public point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
