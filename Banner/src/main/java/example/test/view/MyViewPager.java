package example.test.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import example.test.listener.MyViewpagerListener;

/**
 * Created by LiShang on 2016/7/23.
 */
public class MyViewPager extends ViewPager {
    private MyViewpagerListener mListener;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mListener != null) {
                    mListener.onTouchDown();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.onTouchUp();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setMyViewPagerListener(MyViewpagerListener l) {
        mListener = l;
    }
}
