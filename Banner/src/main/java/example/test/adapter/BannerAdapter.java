package example.test.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.banner.R;
import example.test.listener.BannerAdapterListener;

/**
 * Created by LiShang on 2016/7/22.
 */
public class BannerAdapter extends PagerAdapter {
    private List<View> mList;
    private BannerAdapterListener mListener;
    private Context context;

    public BannerAdapter(List<View> list, Context context) {
        mList = list;
        this.context = context;
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
        View view = mList.get(position % mList.size());
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
        container.removeView(mList.get(position % mList.size()));
    }
}
