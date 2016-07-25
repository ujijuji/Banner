package example.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by LiShang on 2016/7/22.
 */
public class CircleView extends View {
    private int selected_color = Color.parseColor("#ffffff");
    private int unselected_color = Color.parseColor("#80500000");

    public CircleView(Context context, int incolor, int outcolor) {
        super(context);
        init(incolor, outcolor);
    }

    private void init(int incolor, int outcolor) {
        selected_color = incolor;
        unselected_color = outcolor;
        setBackgroundColor(unselected_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 30;
        int height = 30;
        setMeasuredDimension(width, height);
    }

    public void setColor(boolean flag) {
        setBackgroundColor(flag ? selected_color : unselected_color);
    }

    public void setColor(int color){
        setBackgroundColor(color);
    }
}
