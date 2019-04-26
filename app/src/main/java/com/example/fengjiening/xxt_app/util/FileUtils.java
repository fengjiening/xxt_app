package com.example.fengjiening.xxt_app.util;

import android.widget.Toast;

import com.example.fengjiening.xxt_app.App;
import com.sinovoice.util.debug.JTLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 处理和文件读写相关的工具类，方法为static
 * 
 * @author chenronggang
 * 
 */
public class FileUtils {
	public static final String TAG = FileUtils.class.getSimpleName();

	/**
	 * 获取SdCard路径
	 */
	public static String getExternalStoragePath() {
		// 获取SdCard状态
		String state = android.os.Environment.getExternalStorageState();
		// 判断SdCard是否存在并且是可用的
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			return android.os.Environment.getExternalStorageDirectory()
					.getPath();
		}
		return null;
	}

	/**
	 * 获得模板zip文件的存储路径
	 * 
	 * @return 模板zip文件的存储路径
	 */
	public static String getZipSdcardPath() {
		String zipPath = "/hcismartform/zipfile/";
		return getExternalStoragePath() + zipPath;
	}

	/**
	 * 获得模板zip文件的存储路径
	 * 
	 * @return 模板zip文件的存储路径
	 */
	public static String getUnZipSdcardPath() {
		String zipPath = "/hcismartform/unzipfiles/";
		return getExternalStoragePath() + zipPath;
	}

	/**
	 * 判断sdcard是否可用
	 * 
	 * @return true可用
	 */
	public static boolean isSdcardAvailable() {
		boolean result = false;
		// 获取SdCard状态
		String state = android.os.Environment.getExternalStorageState();
		// 判断SdCard是否存在并且是可用的
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			result = true;
		}
		return result;

	}

	/**
	 * 将fileContent存储在storagePath下，文件名为fileName
	 * @param fileName 保存后的文件名
	 * @param storagePath 保存路径
	 * @param fileContent 保存的数据
	 */
	public static void writeToSDCard(String fileName, String storagePath,
			byte[] fileContent) {
		if (!isSdcardAvailable()) {
			Toast.makeText(App.context, "sdcard不可用",
					Toast.LENGTH_SHORT).show();
		} else {
			File storageDir = new File(storagePath);
			File newFile = new File(storagePath + fileName);

			try {
				if (!storageDir.exists()) {
					storageDir.mkdirs();
				}
				if (!newFile.exists()) {
					newFile.createNewFile();
				}
				FileOutputStream stream = new FileOutputStream(newFile,true);
				stream.write(fileContent);
				stream.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件或目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
    	if(dir.exists()) {
    		if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i=0; i < children.length; i++) {
                	JTLog.d(TAG, children[i]+" is del.");
                    deleteDir(new File(dir, children[i]));
                }
                return dir.delete();
            }
            // 目录此时为空，可以删除
            return dir.delete();
    	}
        return true;
    }




	/**
	 * 是否为指定类型的文件
	 * 
	 * @param name
	 *            文件名
	 * @param ext
	 *            文件后缀名 如 png txt
	 * @return
	 */
	public static boolean checkFileExt(String name, String ext) {
		int index = name.lastIndexOf('.');
		if (index == -1) {
			return false;
		}
		if (index >= name.length() - 1) {
			return false;
		}
		if (ext.equalsIgnoreCase(name.substring(index + 1))) {
			return true;
		}
		return false;
	}
}
