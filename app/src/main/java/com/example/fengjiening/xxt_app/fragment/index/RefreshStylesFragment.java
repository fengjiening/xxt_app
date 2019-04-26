package com.example.fengjiening.xxt_app.fragment.index;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.example.fengjiening.xxt_app.R;
import com.example.fengjiening.xxt_app.activity.style.DropBoxStyleActivity;
import com.example.fengjiening.xxt_app.adapter.BaseRecyclerAdapter;
import com.example.fengjiening.xxt_app.adapter.SmartViewHolder;
import com.example.fengjiening.xxt_app.util.StatusBarUtil;
import com.scwang.smartrefresh.header.DropBoxHeader;
import com.scwang.smartrefresh.header.FunGameHitBlockHeader;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Arrays;
import java.util.Collection;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.example.fengjiening.xxt_app.R.id.recyclerView;

/**
 * 发现
 */
public class RefreshStylesFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_example_snaphelper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        //StatusBarUtil.setPaddingSmart(getContext(), root.findViewById(R.id.toolbar));

        View view = root.findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new BaseRecyclerAdapter<Integer>(loadModels(), R.layout.item_example_snaphelper) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Integer model, int position) {
                    holder.image(R.id.imageView, model);
                }
            });
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }

        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    //此处 获取可以通过接口获取数据
                    //设置刷新时长
                    refreshLayout.finishRefresh(3000);
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           /* 下面可以设置刷新 的样式
                           RefreshHeader refreshHeader = refreshLayout.getRefreshHeader();
                            RefreshFooter refreshFooter =refreshLayout.getRefreshFooter();

                            if (refreshHeader instanceof RefreshHeaderWrapper) {
                                refreshLayout.setRefreshHeader(new PhoenixHeader(getContext()));
                            } else if (refreshHeader instanceof PhoenixHeader) {
                                refreshLayout.setRefreshHeader(new DropBoxHeader(getContext()));
                            } else if (refreshHeader instanceof DropBoxHeader) {
                                refreshLayout.setRefreshHeader(new FunGameHitBlockHeader(getContext()));
                            } else if (refreshHeader instanceof FunGameHitBlockHeader) {
                                refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
                            } else {
                                refreshLayout.setRefreshHeader(new RefreshHeaderWrapper(new BallPulseFooter(getContext())));
                            }*/
                           //设置 头主题颜色
                            refreshLayout.setPrimaryColorsId(R.color.colorAccent, android.R.color.white);
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
}
