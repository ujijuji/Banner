package example.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by LiShang on 2016/7/22.
 */
public class ShapeView extends View {
    private int selected_color = Color.parseColor("#ffffff");
    private int unselected_color = Color.parseColor("#80500000");
    private Paint mPaint;
    private IndicatorType mType = IndicatorType.CIRCLE;

    public ShapeView(Context context, int incolor, int outcolor, IndicatorType type) {
        super(context);
        init(incolor, outcolor, type);
    }

    private void init(int incolor, int outcolor, IndicatorType type) {
        selected_color = incolor;
        unselected_color = outcolor;
        mType = type;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(unselected_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        if(mType == IndicatorType.CIRCLE){
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        }else if(mType == IndicatorType.CUBE){
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 20;
        int height = 20;
        setMeasuredDimension(width, height);
    }

    public void setColor(boolean flag) {
        setBackgroundColor(flag ? selected_color : unselected_color);
    }

    public void setColor(int color) {
        mPaint.reset();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        invalidate();
    }
}
