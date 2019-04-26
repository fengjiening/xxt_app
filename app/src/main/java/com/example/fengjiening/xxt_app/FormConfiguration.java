package com.example.fengjiening.xxt_app;

import android.text.TextUtils;
import android.util.Xml;

import com.sinovoice.util.debug.JTLog;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * 配置文件读取加载的类，关键数据会进行保存以供全局查询 TODO
 */
public class FormConfiguration {
	private static final String TAG = FormConfiguration.class.getSimpleName();

	public static String SERVER_URL = "https://www.baidu.com";
	public static String PROVINCE = "";
	public static String DEVELOPER_KEY = "";
	public static String APP_KEY = "";

	/**
	 * 缓存文件名
	 */
	public final static String CONSTANT_STRING_FILE ="constantdatas";



	/**
	 * 配置文件的绝对路径
	 */
	private static final String CONFIG_PATH = "/mnt/sdcard/xxt/xxt.xml";

	/**
	 * 从
	 * @return String
	 */
	private static String ReadTxtFile() {
		String xmlString = ""; // 文件内容字符串
		try {
			InputStream instream = new FileInputStream(new File(CONFIG_PATH));
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream, "utf-8");
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line = null;
				StringBuffer strBuffer = new StringBuffer();

				while ((line = buffreader.readLine()) != null) {
					strBuffer.append(line);
				}
				xmlString = strBuffer.toString();
				instream.close();
			}
		} catch (Exception e) {
			JTLog.e(TAG, "Config error =" + e.getMessage());
		}
		return xmlString;
	}

	public static boolean parseConfig() {
		String content = ReadTxtFile();
		if (TextUtils.isEmpty(content)) {
			return false;
		}
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int eventType = parser.getEventType();
			String ServerAddress = "";

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if ("ServerAddress".equals(nodeName)) {
						ServerAddress = parser.nextText();
						SERVER_URL = "https://" + ServerAddress;

					} else if ("developerKey".equals(nodeName)) {
						DEVELOPER_KEY = parser.nextText();
					} else if ("province".equals(nodeName)) {
						PROVINCE = parser.nextText();
					}else if ("appKey".equals(nodeName)) {
						APP_KEY = parser.nextText();
					}
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
