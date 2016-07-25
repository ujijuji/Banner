package example.test.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.banner.R;
import example.test.listener.BannerAdapterListener;

/**
 * Created by LiShang on 2016/7/22.
 */
public class BannerAdapter<T> extends PagerAdapter {
    private List<T> mList;
    private BannerAdapterListener mListener;
    private Context context;
    private List<View> pool = new ArrayList<>();

    public BannerAdapter(List<T> list, Context context) {
        mList = list;
        this.context = context;
        init();
    }

    private void init() {
        for (int i = 0; i < mList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.page_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
            imageView.setImageResource((Integer) mList.get(i));
            pool.add(view);
        }
    }

    public void setViewList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setAdapterOnClickListener(BannerAdapterListener l) {
        this.mListener = l;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        View view = LayoutInflater.from(context).inflate(R.layout.page_item, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
//        T t = mList.get(0);
//        if (t instanceof String) {
//            Glide.with(context)
//                    .load((String) mList.get(position))
//                    .into(imageView);
//        } else if (t instanceof Integer) {
//            imageView.setBackgroundResource((Integer) mList.get(position % mList.size()));
//        }
        View view = pool.get(position % mList.size());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickLoop(position % mList.size());
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pool.get(position % mList.size()));
    }
}
