package example.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.test.listener.BannerAdapterListener;
import example.test.view.BannerView;

public class MainActivity extends AppCompatActivity {
    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.banner_view);
        bannerView.setTime(2)
                .setImgData(comeList())
                .setIndicatorColor(Color.BLACK, Color.WHITE)
                .setClickListener(new BannerAdapterListener() {
                    @Override
                    public void onClickLoop(int index) {
                        Toast.makeText(MainActivity.this, ""+index, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    public List<Integer> comeList(){
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.pic_1);
        list.add(R.mipmap.pic_2);
        list.add(R.mipmap.pic_3);
        list.add(R.mipmap.pic_4);
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.destroyView();
    }
}