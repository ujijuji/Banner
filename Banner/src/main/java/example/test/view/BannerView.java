package example.test.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.annotation.Size;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    private RelativeLayout indicator;
    private BannerAdapter adapter;
    private BannerAdapterListener adapterListener;
    private int time;
    private List<T> mData;
    private List<View> viewList;
    private CompositeSubscription subscription;
    private Subscription mLoop;
    private List<ShapeView> shapeList;
    private int color_in;
    private int color_out;
    private List<String> descList;
    private TextView tv_desc;
    private IndicatorType type = IndicatorType.CIRCLE;

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
        subscription = new CompositeSubscription();
        shapeList = new ArrayList<>();
        descList = new ArrayList<>();
        time = 3;
        color_in = Color.parseColor("#ffffff");
        color_out = Color.parseColor("#000000");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, null);
        viewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        indicator = (RelativeLayout) view.findViewById(R.id.indicator);
        addView(view);
    }

    public BannerView setTime(@IntRange(from = 3, to = 15) int time) {
        this.time = time;
        return this;
    }

    public BannerView setIndicatorType(IndicatorType type) {
        this.type = type;
        return this;
    }

    public BannerView setImgData(List<T> list) {
        this.mData = list;
        viewList = new ArrayList<>();
        T t = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.page_item, null);
            ImageView img = (ImageView) view.findViewById(R.id.item_img);
            if (t.getClass() == Integer.class) {
                img.setImageResource((Integer) list.get(i));
            } else if (t.getClass() == String.class) {
                Glide.with(context)
                        .load((String) list.get(i))
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_image_loadfail)
                        .into(img);
            }
            viewList.add(view);
        }
        adapter = new BannerAdapter(viewList, context);
        return this;
    }

    public BannerView setIndicatorColor(int current, int other) {
        if (mData == null) {
            throw new AssertionError("check if invoked setImgData before.");
        }
        color_in = current;
        color_out = other;
        for (int i = 0; i < mData.size(); i++) {
            ShapeView view = new ShapeView(context, current, other, type);
            view.setId(i);
            shapeList.add(view);
        }
        for (int i = mData.size() - 1; i >= 0; i--) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            ShapeView view = shapeList.get(i);
            if (i == mData.size() - 1) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            } else {
                params.addRule(RelativeLayout.LEFT_OF, shapeList.get(i + 1).getId());
            }
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.setMargins(0, 0, 15, 0);
            indicator.addView(view, params);
        }
        return this;
    }

    public BannerView setTextDescription(List<String> list) {
        if (mData == null) {
            throw new AssertionError("check if invoked setImgData before.");
        }
        if (list.size() != mData.size()) {
            throw new IllegalArgumentException("check if the list size is correct");
        }
        for (String str : list) {
            descList.add(str);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(20, 0, 0, 0);
        tv_desc = new TextView(context);
        tv_desc.setTextColor(Color.parseColor("#FFFFFF"));
        tv_desc.setTextSize(13);
        indicator.addView(tv_desc, params);
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
//        adapter = new BannerAdapter(mData, context);
        if (adapterListener != null) {
            adapter.setAdapterOnClickListener(adapterListener);
        }
        viewPager.setAdapter(adapter);
        int index = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mData.size();
        viewPager.setCurrentItem(index);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setMyViewPagerListener(viewpagerListener);
        if (shapeList.size() > 0) {
            shapeList.get(0).setColor(color_in);
        }
        if (descList.size() > 0) {
            tv_desc.setText(descList.get(0));
        }
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
            if (shapeList.size() > 0) {
                chargeIndicator(position);
            }
            if (descList.size() > 0) {
                chargeText(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void chargeText(int position) {
        tv_desc.setText(descList.get(position % shapeList.size()));
    }

    private void chargeIndicator(int position) {
        for (ShapeView view : shapeList) {
            view.setColor(color_out);
        }
        shapeList.get(position % shapeList.size()).setColor(color_in);
    }
}
