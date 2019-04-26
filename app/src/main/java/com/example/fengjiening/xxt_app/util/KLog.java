package com.example.fengjiening.xxt_app.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KLog {
	private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
	private static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sinovoice" + File.separator + "king"
			+ File.separator;
	private static final String ERR_LOG = "detail.txt";
	private static int LOG_LEVEl;

	static {
		if (new File(BASE_PATH).exists()) {
			LOG_LEVEl = 5;
		} else {
			LOG_LEVEl = 0;
		}
	}

	public static void setLogLevel(int level) {
		LOG_LEVEl = level;
	}

	public static int getLogLevel() {
		return LOG_LEVEl;
	}

	public static void e(String TAG, String detail) {
		Log.e(TAG, detail);
		if (LOG_LEVEl > 0) {
			String log = DATA_FORMAT.format(new Date()) + "\t E : [ " + TAG + " ] - " + detail + "\n";
			writeLog(log);
		}
	}

	public static void w(String TAG, String detail) {
		Log.w(TAG, detail);
		if (LOG_LEVEl > 1) {
			String log = DATA_FORMAT.format(new Date()) + "\t W : [ " + TAG + " ] - " + detail + "\n";
			writeLog(log);
		}

	}

	public static void i(String TAG, String detail) {
		Log.i(TAG, detail);
		if (LOG_LEVEl > 2) {
			String log = DATA_FORMAT.format(new Date()) + "\t I : [ " + TAG + " ] - " + detail + "\n";
			writeLog(log);
		}

	}

	public static void d(String TAG, String detail) {
		Log.d(TAG, detail);
		if (LOG_LEVEl > 3) {
			String log = DATA_FORMAT.format(new Date()) + "\t D : [ " + TAG + " ] - " + detail + "\n";
			writeLog(log);
		}

	}

	public static void v(String TAG, String detail) {
		Log.v(TAG, detail);
		if (LOG_LEVEl > 4) {
			String log = DATA_FORMAT.format(new Date()) + "\t V : [ " + TAG + " ] - " + detail + "\n";
			writeLog(log);
		}
	}

	private synchronized static void writeLog(String detail) {
		File aimFile = new File(BASE_PATH, ERR_LOG);
		OutputStreamWriter dos = null;
		FileInputStream fis = null;

		try {
			dos = new OutputStreamWriter(new FileOutputStream(aimFile.getAbsoluteFile(), true), Charset.defaultCharset());
			dos.write(detail);
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		try {
			fis = new FileInputStream(aimFile.getAbsoluteFile());
			if (fis.available() > 1024 * 1024) {
				aimFile.renameTo(new File(BASE_PATH, ERR_LOG + DATA_FORMAT.format(new Date())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
