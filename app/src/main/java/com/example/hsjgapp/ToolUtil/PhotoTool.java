package com.example.hsjgapp.ToolUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import Decoder.BASE64Encoder;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class PhotoTool {
	public static String getphotodata(String currentImagePath) {
		File file = new File(currentImagePath);
		if (!file.exists())
			return "";
		String photodata = null;
		Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
		// photodata = GetImageStr(currentImagePath);
		photodata = base64(bitmap);
		return photodata;
	}

	public static String base64(Bitmap bitmap) {
		String photodatabase64 = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		photodatabase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
		return photodatabase64;
	}

	public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double h = options.outWidth;
		double w = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 对图片进行长度压缩
	 * 
	 * @param photoPath
	 * @return
	 */
	public static byte[] getCompressPhotoByPixel(Bitmap photoBitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 将压缩后的图片保存到baos中
		int quality = 100;
		while (baos.toByteArray().length > 250 * 1024) {
			baos.reset();
			photoBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 3;
		}
		byte[] reducedPhoto = baos.toByteArray();
		return reducedPhoto;
	}

	public static Bitmap getPhotoByPixel(Bitmap photoBitmap, double newWidth) {
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (photoBitmap.getWidth() <= newWidth)
			return photoBitmap;
		float width = photoBitmap.getWidth();
		float height = photoBitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = 0;
		float scaleHeight = 0;
		double newHeight = newWidth * height / width;
		if (width > height) {
			scaleWidth = (float) (newWidth / width);
			scaleHeight = (float) (newHeight / height);
		} else {
			scaleWidth = (float) (newWidth / height);
			scaleHeight = (float) (newHeight / width);
		}
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(photoBitmap, 0, 0, (int) width, (int) height, matrix, true);
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// byte[] reducedPhoto = baos.toByteArray();
		return bitmap;
	}
}
