package com.example.fengjiening.xxt_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.fengjiening.xxt_app.R;
import com.example.fengjiening.xxt_app.activity.practice.ProfilePracticeActivity;
import com.example.fengjiening.xxt_app.fragment.index.RefreshChatFragment;
import com.example.fengjiening.xxt_app.fragment.index.RefreshExampleFragment;
import com.example.fengjiening.xxt_app.fragment.index.RefreshMeFragment;
import com.example.fengjiening.xxt_app.fragment.index.RefreshPracticeFragment;
import com.example.fengjiening.xxt_app.fragment.index.RefreshStylesFragment;
import com.example.fengjiening.xxt_app.helper.BottomNavigationViewHelper;
import com.example.fengjiening.xxt_app.util.StatusBarUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;

/**
 * 首页
 */
public class IndexMainActivity extends BaseActivity implements OnNavigationItemSelectedListener {
    private  Intent intent;
    private boolean netOk;
    private enum TabFragment {
        main(R.id.navigation_practice, RefreshPracticeFragment.class),
        find(R.id.navigation_style, RefreshStylesFragment.class),
        circle(R.id.navigation_example, RefreshExampleFragment.class),
        chat(R.id.navigation_chat, RefreshChatFragment.class),
        mine(R.id.navigation_me, RefreshMeFragment.class);

        private Fragment fragment;
        private final int menuId;
        private final Class<? extends Fragment> clazz;

        TabFragment(@IdRes int menuId, Class<? extends Fragment> clazz) {
            this.menuId = menuId;
            this.clazz = clazz;
        }

        @NonNull
        public Fragment fragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static TabFragment from(int itemId) {
            for (TabFragment fragment : values()) {
                if (fragment.menuId == itemId) {
                    return fragment;
                }
            }
            return main;
        }

        public static void onDestroy() {
            for (TabFragment fragment : values()) {
                fragment.fragment = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_main);
        intent=getIntent();
        netOk = intent.getBooleanExtra("netOk",false);
        if(!netOk){
            //网络不通
            showAletByERR("提示","请检查网络!");
        }

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        ViewPager viewPager = findViewById(R.id.content);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return TabFragment.values().length;
            }
            @Override
            public Fragment getItem(int position) {
                return TabFragment.values()[position].fragment();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(TabFragment.values()[position].menuId);
            }
        });

        //状态栏透明和间距处理
        //StatusBarUtil.immersive(this, 0xff000000, 0.1f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabFragment.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        ((ViewPager)findViewById(R.id.content)).setCurrentItem(TabFragment.from(item.getItemId()).ordinal());
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .replace(R.id.content,TabFragment.from(item.getItemId()).fragment())
//                .commit();
        return true;
    }

}
