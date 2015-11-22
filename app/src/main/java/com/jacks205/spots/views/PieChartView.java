package com.jacks205.spots.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jacks205.spots.model.ParkingLevel;

/**
 * Created by markjackson on 11/14/15.
 */
public class PieChartView extends View {

    private ParkingLevel[] parkingLevels = new ParkingLevel[]{
            new ParkingLevel("Level 1", 202, 401),
            new ParkingLevel("Level 2", 439, 470)};
    private int levelsTotalAll = 871;

    private static int BLACK = Color.parseColor("#16D8D8D8");
    private static int GRAY = Color.parseColor("#FFEAEAEA");
    private static int GREEN = Color.parseColor("#FF1ABC9C");
    private static int YELLOW = Color.parseColor("#FFFFDA3F");
    private static int RED = Color.parseColor("#FFF05A52");

    private Paint basePaint;
    private Paint semiPaint;
    private Paint centerPaint;
    private Paint linePaint;

    public PieChartView(Context context, AttributeSet attrs){
        super(context, attrs);

        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setColor(BLACK);

        semiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setColor(Color.WHITE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(GRAY);
        linePaint.setStrokeWidth(2);
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
        float baseRadius = width / 2;
        canvas.drawCircle(center.x, center.y, baseRadius, basePaint);

        float centerRadius = (float)(width * 0.45) / 2;

        //Semi-circles
        float lastAngle = 0;
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
        canvas.drawCircle(center.x, center.y, centerRadius, centerPaint);

        //Center Line
        double lineLength = width / 3 * 0.5;
        canvas.drawLine(center.x - (float)lineLength / 2, center.y, center.x + (float)lineLength / 2, center.y, linePaint);
    }
}
