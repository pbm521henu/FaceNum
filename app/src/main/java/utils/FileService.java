package utils;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FileService {
	public  static  String saveImage(byte[] data) { // 保存jpg到SD卡中
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		String tofile = Environment.getExternalStorageDirectory()
				+ File.separator + Environment.DIRECTORY_DCIM + File.separator
				+ date + ".jpg";
		//String tofile1 = Environment.getExternalStorageDirectory().getAbsolutePath() +"/picture/"+ date+ ".jpg";
		try {
			File file = new File(tofile);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tofile;
	}
	public static String saveFileToSdcard (String fileName, byte[] data) {
		
		String state = Environment.getExternalStorageState();

		FileOutputStream outputStream = null;
		File root = Environment.getExternalStorageDirectory();

		if(state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(root.getAbsolutePath() + "/picture");
			//File file = new File("/picture");
			if(!file.exists()) {
				file.mkdirs();
			}
			try {
				outputStream = new FileOutputStream(new File(file,fileName));
				outputStream.write(data, 0, data.length);
				return file.getAbsolutePath() + "/" + fileName;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}	
	
	public static byte[] readContentFromSdcard (String fileName) {
		File root = Environment.getExternalStorageDirectory();
		FileInputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(root.getAbsolutePath() + "/picture");
			File file2 = new File(file, fileName);
			int len = 0;
			byte[] data = new byte[1024];
			if(file2.exists()) {
				try {
					inputStream = new FileInputStream(file2);
					while((len = inputStream.read(data)) != -1) {
						outputStream.write(data, 0, data.length);
					}
					return outputStream.toByteArray();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if(inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
}
