package example.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.test.listener.BannerAdapterListener;
import example.test.view.BannerView;
import example.test.view.IndicatorType;

public class MainActivity extends AppCompatActivity {
    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.banner_view);
        bannerView.setTime(4)
                .setIndicatorType(IndicatorType.CUBE)
                .setImgData(urlList())
                .setIndicatorColor(Color.parseColor("#DAA520"), Color.parseColor("#ffffff"))
                .setTextDescription(descList())
                .setClickListener(new BannerAdapterListener() {
                    @Override
                    public void onClickLoop(int index) {
                        Toast.makeText(MainActivity.this, ""+index, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    public List<String> urlList(){
        List<String> list = new ArrayList<>();
        list.add("http://tupian.qqjay.com/u/2013/1127/19_222949_14.jpg");
        list.add("http://tupian.qqjay.com/u/2013/1127/19_222949_3.jpg");
        list.add("http://tupian.qqjay.com/u/2013/1127/19_222949_4.jpg");
        list.add("http://tupian.qqjay.com/u/2013/1127/19_222949_6.jpg");
        list.add("http://tupian.qqjay.com/u/2013/1127/19_222949_10.jpg");
        return list;
    }

    public List<Integer> pictureList(){
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.pic_1);
        list.add(R.mipmap.pic_2);
        list.add(R.mipmap.pic_3);
        list.add(R.mipmap.pic_4);
        list.add(R.mipmap.pic_5);
        return list;
    }

    public List<String> descList(){
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.desc_1));
        list.add(getString(R.string.desc_2));
        list.add(getString(R.string.desc_3));
        list.add(getString(R.string.desc_4));
        list.add(getString(R.string.desc_5));
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.destroyView();
    }
}
