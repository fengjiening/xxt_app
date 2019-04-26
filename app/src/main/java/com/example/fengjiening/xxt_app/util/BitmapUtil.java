package com.example.fengjiening.xxt_app.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 与图片数据相关的工具类 TODO
 */
public class BitmapUtil {
	/**
	 * 使用Base64 转码保存图像数据
	 * 
	 * @param bitmap
	 * @return String
	 */
	public static String convertToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 50, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	/**
	 * Base64 转码后的图像数据还原的方法
	 * 
	 * @param st
	 */
	public static Bitmap convertStringToBitmap(String st) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}
}
