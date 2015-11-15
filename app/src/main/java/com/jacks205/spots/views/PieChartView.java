package com.jacks205.spots.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jacks205.spots.model.Segment;

/**
 * Created by markjackson on 11/14/15.
 */
public class PieChartView extends View {

    private Segment[] pieSegments;
    private int segmentTotalAll;

    public PieChartView(Context context, AttributeSet attrs){
        super(context, attrs);
        setPieSegments(
                new Segment[]{new Segment(50, 100), new Segment(25, 100), new Segment(50, 50), new Segment(10, 100)}
        );
    }

    public void setPieSegments(Segment[] pieSegments) throws IllegalArgumentException{
        if(pieSegments == null || pieSegments.length < 1) throw new IllegalArgumentException("pieSegments must be a length greater than 1.");
        this.pieSegments = pieSegments;
        segmentTotalAll = 0;
        for (Segment segment : pieSegments){
            segmentTotalAll += segment.total;
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
        basePaint.setColor(Color.BLACK);
        float baseRadius = width / 2;
        canvas.drawCircle(center.x, center.y, baseRadius, basePaint);

        float centerRadius = (float)(width * 0.45) / 2;

        //Semi-circles
        float lastAngle = 0;
        Paint semiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for(Segment segment : pieSegments) {
            double percentFull = segment.value / segment.total;
            if(percentFull >= 0.85){
                semiPaint.setColor(Color.RED);
            }else if(percentFull >= 0.55){
                semiPaint.setColor(Color.YELLOW);
            }else{
                semiPaint.setColor(Color.GREEN);
            }
            double percentDiff = 1 - percentFull;
            RectF semi = new RectF(0, 0, width, height);
            float dx = (float) (width * percentDiff) / 4;
            float dy = (float) (height * percentDiff) / 4;
            semi.inset(dx, dy);
            float startAngle = lastAngle;
            float endAngle = lastAngle + (float)segment.total / segmentTotalAll * 360;
            canvas.drawArc(semi, startAngle, endAngle, true, semiPaint);
            lastAngle = endAngle;
        }

        //Center Circle
        Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setColor(Color.WHITE);
        canvas.drawCircle(center.x ,center.y , centerRadius, centerPaint);
    }
}
