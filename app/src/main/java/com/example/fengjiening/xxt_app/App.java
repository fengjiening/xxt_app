package com.example.fengjiening.xxt_app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;


import com.example.fengjiening.xxt_app.activity.MainActivity;
import com.example.fengjiening.xxt_app.util.CrashHandler;
import com.example.fengjiening.xxt_app.util.DynamicTimeFormat;
import com.sinovoice.util.debug.JTAssert;
import com.sinovoice.util.debug.JTLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zxy.recovery.core.Recovery;

import okhttp3.OkHttpClient;

import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
/**
 * 自定义Application 用户存储全局数据
 * 
 * @author chenronggang
 * 
 */
public class App extends Application {
	private static final String TAG = App.class.getSimpleName();
	private List<Activity> activitys = new LinkedList<Activity>();
	private static App mInstance;
	public static Context context;

	public static List<?> images=new ArrayList<>();
	public static List<String> titles=new ArrayList<>();
	public static int H,W;

	static {
		//启用矢量图兼容
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		//设置全局默认配置（优先级最低，会被其他设置覆盖）
		SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
			@Override
			public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
				//全局设置（优先级最低）
				layout.setEnableAutoLoadMore(true);
				layout.setEnableOverScrollDrag(false);
				layout.setEnableOverScrollBounce(true);
				layout.setEnableLoadMoreWhenContentNotFull(true);
				layout.setEnableScrollContentWhenRefreshed(true);
			}
		});
		SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
			@NonNull
			@Override
			public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
				//全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
				layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);

				return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
			}
		});
	}

	/**
	 * OkHttp 3 请求构造器
	 */
	public static OkHttpClient mOkHttpClient;


	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		context = getApplicationContext();

		// 在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);

		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);

		// JTLog开关，打印所有等级log信息，并且保存到/mnt/sdcard/sinovoice/HciCloudSmartForm/log.txt
		JTLog.config(0, false, "HciCloudSmartForm");

		// JTAssert开关,打开
		JTAssert.config(false);

		// OkHttp 3.8 构造器初始化
		mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
				.build();

		getScreen(this);
		Fresco.initialize(this);
		Recovery.getInstance()
				.debug(true)
				.recoverInBackground(false)
				.recoverStack(true)
				.mainPage(MainActivity.class)
				.init(this);
		String[] urls = getResources().getStringArray(R.array.url);
		String[] tips = getResources().getStringArray(R.array.title);
		List list = Arrays.asList(urls);
		images = new ArrayList(list);
		List list1 = Arrays.asList(tips);
		titles= new ArrayList(list1);
	}

	/**
	 * 获得SmartFormApplication实例
	 * 
	 * @return
	 */
	public static App getContext() {
		return mInstance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}
	}

	// 遍历所有Activity并finish
	public void finishActivites() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 获得SmartFormApplication实例
	 *
	 * @return
	 */
	public static App getInstance() {
		return mInstance;
	}
	public void getScreen(Context aty) {
		DisplayMetrics dm = aty.getResources().getDisplayMetrics();
		H=dm.heightPixels;
		W=dm.widthPixels;
	}
}
