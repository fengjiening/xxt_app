package com.example.fengjiening.xxt_app.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.fengjiening.xxt_app.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * 应用内界面的基类，屏蔽系统按键 TODO
 */
public class BaseActivity extends AppCompatActivity {
	private static final String TAG = BaseActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
	private Context context;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private SweetAlertDialog pDialog;
	private static int count = 0;
	private boolean isPause = false;
	private boolean isStop = true;

	private  int delay = 1000;  //1s
	private  int period = 1000;  //1s
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context=this;
		super.onCreate(savedInstanceState);
	}

	protected void onResume() {
		super.onResume();
		reset();
	}

	@Override
	public void onWindowFocusChanged(boolean pHasWindowFocus) {
		super.onWindowFocusChanged(pHasWindowFocus);
		if (!pHasWindowFocus) {
			sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 在失去焦点时 通过管理器将当前栈移到栈顶
		/*ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.moveTaskToFront(getTaskId(), 0);*/
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		reset();
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		reset();
		return super.dispatchKeyEvent(event);
	}

	private long lastTime;
	private int clickCount;
	private static final int MAX_COUNT = 20;
	private static final long CLICK_GAP =1000;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG,"keyCode = "+keyCode);
		reset();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 首次点击
			if(clickCount==0 || lastTime==0l){
				clickCount = 1;
				lastTime = System.currentTimeMillis();
			}else{
				// 当前时间
				long now = System.currentTimeMillis();

				// 在规定时间间隔内
				if(now -lastTime< CLICK_GAP){
					clickCount++;
					lastTime = now;
				}else{
					// 点击超时，重新计数
					clickCount =0;
					lastTime=0;
				}
			}

			Log.d(TAG,"点击计数 = "+clickCount);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {// MENU键
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示等待界面
	 */
	void showLoadingView() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog.setTitle(R.string.title_dialog_waiting);
		mProgressDialog.setMessage(getString(R.string.wait_info));
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
	}

	/**
	 * 取消等待界面
	 */
	void dismissLoadingView() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}
	/**
	 * 显示等待界面Sweet
	 */
	void showLoadingViewSweet() {
		pDialog= new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
				.setTitleText("Loading");
		pDialog.show();
		pDialog.setCancelable(false);
		startTimer(pDialog);

	}
	/**
	 * 取消等待界面Sweet
	 */
	void dismissLoadingViewSweet() {
			stopTimer();
			if(pDialog!=null){
				runOnUiThread(new Runnable() {
					public void run() {
						pDialog.dismissWithAnimation();
					}
				});

			}

	}

	private void startTimer(final SweetAlertDialog pDialog){
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					Log.i(TAG, "count: "+String.valueOf(count));

					switch (count){
						case 0:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
							break;
						case 1:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
							break;
						case 2:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
							break;
						case 3:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
							break;
						case 4:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
							break;
						case 5:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
							break;
						case 6:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
							break;
					}
					if(count==6)
						count=-1;
					do {
						try {
							Log.i(TAG, "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					} while (isPause);
					count ++;
				}
			};
		}

		if(mTimer != null && mTimerTask != null )
			mTimer.schedule(mTimerTask, delay, period);

	}

	private void stopTimer(){
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}

		count = 0;

	}
	/**
	 * 全局弹窗的方法
	 *
	 * @param detail
	 *            void
	 */
	void showToast(final String detail) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(BaseActivity.this, detail, Toast.LENGTH_SHORT).show();
			}
		});
	}
	void showToastSweet(final String title,final String detail) {
		runOnUiThread(new Runnable() {
			public void run() {
				new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
						.setTitleText(title)
						.setContentText(detail)
						.setCustomImage(R.drawable.custom_img)
						.show();
			}
		});
	}
	void showAletByERR(final String title,final String detail){
		runOnUiThread(new Runnable() {
			public void run() {
				new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
						.setTitleText(title)
						.setContentText(detail)
						.show();
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy 取消");

		dismissLoadingView();
	}

	void reset(){

	}
}
