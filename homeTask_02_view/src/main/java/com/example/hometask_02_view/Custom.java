package com.example.hometask_02_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;


public class Custom extends View {
    public Custom(Context context) {
        super(context);
    }

    private Paint paint = new Paint();
    private int centerX;
    private int centerY;
    private int clickedX;
    private int clickedY;
    final int radiusArk = 500;
    final int radiusCircle = 150;
    private int cube ;
    private int modX, modY ;
    private RectF oval = new RectF();
    private Random rnd = new Random();
    private int[][] colors = new int[5][3];



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        oval.set(centerX - radiusArk, centerY - radiusArk, centerX + radiusArk, centerY + radiusArk);
        getAllNewColors();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setARGB(255, colors[0][0], colors[0][1], colors[0][2]);
        canvas.drawArc(oval, 0, 90, true, paint);
        paint.setARGB(255, colors[1][0], colors[1][1], colors[1][2]);
        canvas.drawArc(oval, 90, 90, true, paint);
        paint.setARGB(255, colors[2][0], colors[2][1], colors[2][2]);
        canvas.drawArc(oval, 180, 90, true, paint);
        paint.setARGB(255, colors[3][0], colors[3][1], colors[3][2]);
        canvas.drawArc(oval, 270, 90, true, paint);
        paint.setARGB(255, colors[4][0], colors[4][1], colors[4][2]);
        canvas.drawCircle(centerX, centerY, radiusCircle, paint);
    }

    private void getNewColor(int i) {
        for (int j = 0; j < 3; j++) {
            colors[i][j] = rnd.nextInt(256);
        }
    }

    private void getAllNewColors() {
        for (int i = 0; i <= 4; i++) {
            getNewColor(i);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickedX = (int) event.getX() ;
        clickedY = (int) event.getY() ;
        modX = Math.abs(centerX-clickedX) ;
        modY = Math.abs(centerY-clickedY);
        cube = (int) Math.pow((modX*modX + modY*modY), 0.5) ;
        if (radiusCircle >= cube)
        {getAllNewColors();
        invalidate();}
        if (clickedX<centerX && clickedY > centerY && radiusArk >= cube)
        {getNewColor(1);
        invalidate();}
        if (clickedX>centerX && clickedY > centerY && radiusArk >= cube)
        {getNewColor(0);
            invalidate();}
        if (clickedX>centerX && clickedY < centerY && radiusArk >= cube)
        {getNewColor(3);
            invalidate();}
        if (clickedX<centerX && clickedY < centerY&& radiusArk >= cube)
        {getNewColor(2);
            invalidate();}
        return super.onTouchEvent(event);
    }
}
