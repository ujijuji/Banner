package example.test.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import example.banner.R;
import example.test.adapter.BannerAdapter;
import example.test.listener.BannerAdapterListener;
import example.test.listener.MyViewpagerListener;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LiShang on 2016/7/23.
 */
public class BannerView<T> extends FrameLayout {
    private Context context;
    private MyViewPager viewPager;
    private LinearLayout indicator;
    private BannerAdapter adapter;
    private BannerAdapterListener adapterListener;
    private int time = 2;
    private List<T> mData;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Subscription mLoop;
    private List<CircleView> circle = new ArrayList<>();
    private int color_in;
    private int color_out;

    public enum IndicatorType {
        CIRCLE, CUBE
    }

    public BannerView(Context context) {
        this(context, null);
        this.context = context;
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, null);
        viewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        indicator = (LinearLayout) view.findViewById(R.id.indicator);
        addView(view);
    }

    public BannerView setTime(int time) {
        this.time = time;
        return this;
    }

    public BannerView setImgData(List<T> list) {
        this.mData = list;
        return this;
    }

    public BannerView setIndicatorColor(int current, int other) {
        if (mData == null) {
            throw new AssertionError("check if invoked setImgData before.");
        }
        color_in = current;
        color_out = other;
        for (int i = 0; i < mData.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            CircleView view = new CircleView(context, current, other);
            indicator.addView(view, params);
            circle.add(view);
        }
        return this;
    }

    public void destroyView() {
        stopLoop();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void startLoop() {
        if (mLoop == null) {
            mLoop = Observable.interval(time, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int index = viewPager.getCurrentItem() + 1;
                            viewPager.setCurrentItem(index);
                        }
                    });
            subscription.add(mLoop);
        }
    }

    private void stopLoop() {
        if (mLoop != null && !mLoop.isUnsubscribed()) {
            mLoop.unsubscribe();
            mLoop = null;
        }
    }

    public BannerView setClickListener(BannerAdapterListener listener) {
        this.adapterListener = listener;
        return this;
    }

    public void start() {
        adapter = new BannerAdapter(mData, context);
        if (adapterListener != null) {
            adapter.setAdapterOnClickListener(adapterListener);
        }
        viewPager.setAdapter(adapter);
        int index = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mData.size();
        viewPager.setCurrentItem(index);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setMyViewPagerListener(viewpagerListener);
        startLoop();
    }

    private MyViewpagerListener viewpagerListener = new MyViewpagerListener() {
        @Override
        public void onTouchDown() {
            stopLoop();
        }

        @Override
        public void onTouchUp() {
            startLoop();
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            chargeIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void chargeIndicator(int position) {
        for (View view : circle) {
            ((CircleView) view).setColor(color_out);
        }
        ((CircleView) circle.get(position % circle.size())).setColor(color_in);
    }
}
