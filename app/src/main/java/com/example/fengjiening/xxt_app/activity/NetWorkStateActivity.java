package com.example.fengjiening.xxt_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.fengjiening.xxt_app.FormConfiguration;
import com.example.fengjiening.xxt_app.R;
import com.example.fengjiening.xxt_app.App;
import com.example.fengjiening.xxt_app.activity.practice.ProfilePracticeActivity;
import com.example.fengjiening.xxt_app.util.StatusBarUtil;
import com.sinovoice.util.debug.JTLog;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 检测网路状态的Activity TODO
 */
public class NetWorkStateActivity extends BaseActivity {
	private static final String TAG = NetWorkStateActivity.class.getSimpleName();
	private  final int SPLASH_DISPLAY_LENGHT = 2000;//两秒后进入系统，时间可自行调整


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatusBarUtil.immersive(this, 0xff000000, 0.1f);
		String url = FormConfiguration.SERVER_URL+1;
		setContentView(R.layout.activity_main_lodding);
		if (!FormConfiguration.parseConfig()) {
			Toast.makeText(NetWorkStateActivity.this, "配置文件异常,请修改后重试", Toast.LENGTH_LONG).show();
			/*new Handler().postDelayed(new Runnable() {
				public void run() {
					Process.killProcess(Process.myPid());
				}
			}, 4000);*/

		} else {
			Log.d(TAG, "Try to connect " + url);

		}
		checkNetWorkState(url);
	}

	/**
	 * 检测制定地址是否可访问，网络操作在AsyncHttpClient中进行
	 * 
	 * @param url
	 *            void
	 */
	public void checkNetWorkState(String url) {
		JTLog.i(TAG, "request_请求服务器的url: " + url);

		String jsonString = "{'interFaceType':'9','params':{}}";
		Log.d(TAG, "请求配置信息信息 url=" + url);
		RequestBody body = new FormBody.Builder().add("data", jsonString).build();
		App.mOkHttpClient.newCall(new Request.Builder().url(url).post(body).build()).enqueue(new Callback() {
			Intent intent = new Intent(NetWorkStateActivity.this, MainActivity.class);
			public void onFailure(Call call, IOException exception) {
				Log.d(TAG, "请求失败。。" );
				onFinish(false,"");
			}

			public void onResponse(Call call, Response response) throws IOException {
				// 错误处理
				if (response == null) {
					showToast("请求_服务器返回的数据为 null");
					onFinish(false,"");
					return;
				}

				String detail = response.body().string();
				if (TextUtils.isEmpty(detail)) {
					showToast("请求_服务器返回的数据为空.");
					onFinish(false,"");
					return;
				}
				Log.e(" 网络 detail---------", detail);

				// 解析
				/*JSONObject jsonObject = JSON.parseObject(detail);

				int errorCode = jsonObject.getInteger("errorCode");
				if (errorCode == 0) {
					String result = jsonObject.getString("result");
					Log.e(" 网络 result---------", result);
					//放入缓冲 result
					//SharedPrfUtil.writeGetTemplateResponseToSharedPrefernce(getServerTypeResponse);
					onFinish(1,JSON.parseObject(result).getString("provinceName"));
				} else {
					String errorMsg = jsonObject.getString("errorMsg");
					onFinish(0,"");
				}*/
                onFinish(true,FormConfiguration.PROVINCE);
			}

			private void onFinish(final boolean isNetoK,String message) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Intent mainIntent = new Intent(NetWorkStateActivity.this,IndexMainActivity.class);
						NetWorkStateActivity.this.startActivity(mainIntent);
						NetWorkStateActivity.this.finish();
					}
				},SPLASH_DISPLAY_LENGHT);//延时1s执行
			}
		});

	}
}
