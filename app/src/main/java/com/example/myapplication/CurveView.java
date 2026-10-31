package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CurveView extends View {

    private final Paint axisPaint = new Paint();
    private final Paint gridPaint = new Paint();
    private final Paint curvePaint = new Paint();
    private final Path path = new Path();

    private ExpressionParser parser = null;
    private double xRange = 10;

    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(4f);

        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1f);

        curvePaint.setColor(Color.parseColor("#D0102A"));
        curvePaint.setStrokeWidth(6f);
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setAntiAlias(true);
    }

    public void setFunction(ExpressionParser parser) {
        this.parser = parser;
        invalidate();
    }

    public void zoomIn() {
        xRange = Math.max(1.25, xRange / 2);
        invalidate();
    }

    public void zoomOut() {
        xRange = Math.min(1000, xRange * 2);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();
        float originX = w / 2;
        float originY = h / 2;
        float scale = (float) (w / (2 * xRange));

        double step = xRange <= 5 ? 0.5 : Math.max(1, Math.floor(xRange / 10));
        for (double g = 0; g <= xRange; g += step) {
            float d = (float) (g * scale);
            canvas.drawLine(originX + d, 0, originX + d, h, gridPaint);
            canvas.drawLine(originX - d, 0, originX - d, h, gridPaint);
            canvas.drawLine(0, originY + d, w, originY + d, gridPaint);
            canvas.drawLine(0, originY - d, w, originY - d, gridPaint);
        }

        canvas.drawLine(0, originY, w, originY, axisPaint);
        canvas.drawLine(originX, 0, originX, h, axisPaint);

        if (parser == null) return;

        path.reset();
        boolean penDown = false;
        float previousPy = 0;

        for (float px = 0; px <= w; px += 2) {
            double x = (px - originX) / scale;
            double y;
            try {
                y = parser.eval(x);
            } catch (RuntimeException e) {
                penDown = false;
                continue;
            }

            if (Double.isNaN(y) || Double.isInfinite(y)) {
                penDown = false;
                continue;
            }

            float py = (float) (originY - y * scale);

            if (py < -5000 || py > h + 5000) {
                penDown = false;
                continue;
            }

            if (!penDown || Math.abs(py - previousPy) > h) {
                path.moveTo(px, py);
                penDown = true;
            } else {
                path.lineTo(px, py);
            }
            previousPy = py;
        }
        canvas.drawPath(path, curvePaint);
    }
}