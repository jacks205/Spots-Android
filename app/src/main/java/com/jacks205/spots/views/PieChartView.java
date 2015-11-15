package com.jacks205.spots.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jacks205.spots.model.ParkingLevel;
import com.jacks205.spots.model.Segment;

/**
 * Created by markjackson on 11/14/15.
 */
public class PieChartView extends View {

    private ParkingLevel[] parkingLevels;
    private int levelsTotalAll;

    private static int BLACK = Color.parseColor("#16D8D8D8");
    private static int GREEN = Color.parseColor("#FF1ABC9C");
    private static int YELLOW = Color.parseColor("#FFFFDA3F");
    private static int RED = Color.parseColor("#FFF05A52");

    public PieChartView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setLevelSegments(ParkingLevel[] parkingLevels) throws IllegalArgumentException{
        if(parkingLevels == null || parkingLevels.length < 1) throw new IllegalArgumentException("pieSegments must be a length greater than 1.");
        this.parkingLevels = parkingLevels;
        levelsTotalAll = 0;
        for (ParkingLevel level : parkingLevels){
            levelsTotalAll += level.getTotal();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Point center = new Point(width / 2, height / 2);

        //Base Circle
        Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setColor(BLACK);
        float baseRadius = width / 2;
        canvas.drawCircle(center.x, center.y, baseRadius, basePaint);

        float centerRadius = (float)(width * 0.45) / 2;

        //Semi-circles
        float lastAngle = 0;
        Paint semiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for(ParkingLevel level : parkingLevels) {
            double percentFull = (double)level.getAvailable() / level.getTotal();
            double percentDiff = 1 - percentFull;
            if(percentDiff >= 0.85){
                semiPaint.setColor(RED);
            }else if(percentDiff >= 0.55){
                semiPaint.setColor(YELLOW);
            }else {
                semiPaint.setColor(GREEN);
            }
            RectF semi = new RectF(0, 0, width, height);
            float dx = (float) (width * percentDiff) / 4;
            float dy = (float) (height * percentDiff) / 4;
            semi.inset(dx, dy);
            float startAngle = lastAngle + 2f;
            float endAngle = (float)level.getTotal() / levelsTotalAll * 360;
            canvas.drawArc(semi, startAngle, endAngle, true, semiPaint);
            lastAngle += endAngle;
        }

        //Center Circle
        Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setColor(Color.WHITE);
        canvas.drawCircle(center.x ,center.y , centerRadius, centerPaint);
    }
}
