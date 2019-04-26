package com.example.fengjiening.xxt_app.fragment.index;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.fengjiening.xxt_app.R;
import com.example.fengjiening.xxt_app.activity.FragmentActivity;
import com.example.fengjiening.xxt_app.activity.example.SnapHelperExampleActivity;
import com.example.fengjiening.xxt_app.activity.style.DropBoxStyleActivity;
import com.example.fengjiening.xxt_app.adapter.BaseRecyclerAdapter;
import com.example.fengjiening.xxt_app.adapter.SmartViewHolder;
import com.example.fengjiening.xxt_app.util.StatusBarUtil;
import com.scwang.smartrefresh.header.DropBoxHeader;
import com.scwang.smartrefresh.header.FunGameHitBlockHeader;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.Arrays;
import java.util.Collection;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


/**
 * 朋友圈
 */
public class RefreshExampleFragment extends Fragment implements AdapterView.OnItemClickListener{



    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_circle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        //StatusBarUtil.setPaddingSmart(getContext(), root.findViewById(R.id.toolbar));


        mRefreshLayout = root.findViewById(R.id.refreshLayout);
        View view = root.findViewById(R.id.recyclerView);

        // mRefreshLayout.setPrimaryColorsId(R.color.colorPrimary, R.color.colorPrimaryDark);
        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new BaseRecyclerAdapter<Integer>(loadModels(), R.layout.item_example_snaphelper,this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Integer model, int position) {
                    holder.image(R.id.imageView, model);
                }

                @Override
                public int getViewTypeCount() {
                    return 2;
                }

                @Override
                public int getItemViewType(int position) {
                    return position == 0 ? 0 : 1;
                }
            });
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.finishRefresh(3000);
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RefreshHeader refreshHeader = refreshLayout.getRefreshHeader();
                            refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                        }
                    },4000);
                }
            });
        }
    }

    private Collection<Integer> loadModels() {
        return Arrays.asList(
                R.mipmap.image_weibo_home_1,
                R.mipmap.image_weibo_home_2,
                R.mipmap.image_weibo_home_1,
                R.mipmap.image_weibo_home_2,
                R.mipmap.image_weibo_home_1,
                R.mipmap.image_weibo_home_2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("你点击了："+position)
                .show();

    }
}
