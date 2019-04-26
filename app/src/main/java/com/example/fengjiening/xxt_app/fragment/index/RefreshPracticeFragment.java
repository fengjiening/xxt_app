package com.example.fengjiening.xxt_app.fragment.index;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.fengjiening.xxt_app.App;
import com.example.fengjiening.xxt_app.R;
import com.example.fengjiening.xxt_app.activity.FragmentActivity;
import com.example.fengjiening.xxt_app.activity.practice.ProfilePracticeActivity;
import com.example.fengjiening.xxt_app.adapter.BaseRecyclerAdapter;
import com.example.fengjiening.xxt_app.adapter.SmartViewHolder;
import com.example.fengjiening.xxt_app.fragment.practice.InstantPracticeFragment;
import com.example.fengjiening.xxt_app.loader.GlideImageLoader;
import com.example.fengjiening.xxt_app.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import com.example.fengjiening.xxt_app.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 主页
 *
 */
public class RefreshPracticeFragment extends Fragment implements OnBannerListener {

    private Banner banner;
    private Context context;
    ListView listView;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        initView(root);
        context=root.getContext();
        super.onViewCreated(root, savedInstanceState);

    }
    private void initView(View root) {
        banner = (Banner) root.findViewById(R.id.banner);
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

    public void OnBannerClick(int position) {
        final int a =position;
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("你点击了："+a)
                .show();
    }

}
