package com.example.fengjiening.xxt_app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fengjiening.xxt_app.App;
import com.example.fengjiening.xxt_app.R;
import com.example.fengjiening.xxt_app.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;


import java.util.ArrayList;


public class MainActivity extends BaseActivity implements OnBannerListener {
    private  Intent intent;
    private boolean netOk;
    private Banner banner;
    ListView listView;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent= getIntent();
        initView();
        initSerch();
        netOk = intent.getBooleanExtra("netOk",false);
        if(!netOk){
            //网络不通
            showAletByERR("提示","请检查网络!");
        }

    }
    private void initSerch(){

    }
    private void initView() {
        banner = (Banner) findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

        //默认是CIRCLE_INDICATOR
        banner.setImages(App.images)
                .setBannerTitles(App.titles)
                .setImageLoader(new GlideImageLoader())
                //设置banner动画效果
                //.setBannerAnimation(Transformer.ZoomOut)
                .setBannerAnimation(Transformer.CubeOut)
                .setDelayTime(3000)
                .setOnBannerListener(this)
                .isAutoPlay(true)
                .start();
    }
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }


    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
