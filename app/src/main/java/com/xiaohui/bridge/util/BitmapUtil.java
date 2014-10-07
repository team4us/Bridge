package com.xiaohui.bridge.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;

import com.xiaohui.bridge.XhApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理 获取位图超出缓存时压缩图片
 * 
 * @date 2012-12-25
 * @author eric.huang
 */
public class BitmapUtil {
	/* eric.huang 2013-9-2
	 * 此处4M是暂定值，为了适应目前所有机型长微博能够清晰显示
	 * 查阅了资料，系统给虚拟机分配至少16M的空间来跑进程，以后系统会自动分配空间给进程
	 * 我们能够获得的只有这个16M值，如果给虚拟机设置最大内存值，只是系统分配不超过最大值而已，而不是扩大初始值
	 * 所以，无法通过设置内存大小来达到避免内存溢出的问题，只能是去避免
	 * 具体方法是设置图片压缩比，创建图片缓存区，软引用方式来减少内存溢出的可能
	 */
	private final static double MAX_IMAGE_DECODE_ZONE = 1024 * 1024 * 4; // 4M
	public static int roundCornerPx = 24; // 圆角像素
	private final static String IMAGE_EXPANDED_NAME = ".jpg";

	/**
	 * 默认2m压缩空间获取图片
	 * 
	 * @author eric.huang
	 * @date 2013-8-21
	 * @param imageData
	 * @return
	 */
	public static Bitmap getBitmapFromByte(byte[] imageData) {
		return getBitmapFromByte(imageData, 0);
	}
	
	/**
	 * 将字节数据压缩后获取图片 采用先将图片压缩然后放大的方式获取
	 * 
	 * @date 2013-1-11
	 * @author eric.huang
	 * @param maxNumOfPixels 最大像素
	 */
	public static Bitmap getBitmapFromByte(byte[] imageData, double maxNumOfPixels) {
		if (null == imageData) {
			return null;
		}

		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inPurgeable = true;
		
		bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, opts);

		// 压缩到适应屏幕高宽
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels > 0 ? maxNumOfPixels : MAX_IMAGE_DECODE_ZONE);

		/*
		 * eric.huang 2013-8-29
		 * 
		 * 默认是Bitmap.Config.ARGB_8888, 图片编码格式
		 * 每个颜色分量8位，取值范围是0-255，采用一个int类型32位就可以表示，所以每个像素点4个字节
		 * 
		 * Bitmap.Config.ARGB_4444编码
		 * 第1和第3个颜色分量各占5位，取值范围0-31，第2个颜色分量占6位，取值范围0-63；
		 * 所以采用一个short类型16位就可以表示，所以每个像素点2个字节
		 * 
		 * 所以同一张图片采用Bitmap.Config.ARGB_4444编码理论上可以缩小1倍内存
		 * 
		 * opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
		 */
		opts.inJustDecodeBounds = false;

		try {
			bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, opts);
		} catch (OutOfMemoryError e) {
            e.printStackTrace();
		}

		return bmp;
	}

	/**
	 * bitmap转为字节
	 * 
	 * @author eric.huang
	 * @date 2013-7-8
	 * @param bmp
	 * @return
	 */
	public static byte[] getByteFromBitmap(Bitmap bmp) {
		if(null == bmp){
			return null;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 默认2m压缩空间获取图片
	 * @author eric.huang
	 * @date 2013-8-21
	 * @param imageFile
	 * @return
	 */
	public static Bitmap getBitmapFromFilePath(String imageFile) {
		return getBitmapFromFilePath(imageFile, 0);
	}
	
	/**
	 * 根据路径获取压缩后图片 采用先将图片压缩然后放大的方式获取
	 * 
	 * @date 2013-1-8
	 * @author eric.huang
	 * @param maxNumOfPixels 最大像素
	 */
	public static Bitmap getBitmapFromFilePath(String imageFile, double maxNumOfPixels) {
		if(null == imageFile){
			return null;
		}
		
		if(imageFile.length() < 1){
			return null;
		}
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inPurgeable = true;
		BitmapFactory.decodeFile(imageFile, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels > 0 ? maxNumOfPixels : MAX_IMAGE_DECODE_ZONE);
		
		// 默认是Bitmap.Config.ARGB_8888, 图片编码格式
		// opts.inPreferredConfig = Bitmap.Config.ARGB_4444;

		opts.inJustDecodeBounds = false;
		Bitmap bmp = null;

		try {
			bmp = BitmapFactory.decodeFile(imageFile, opts);
		} catch (OutOfMemoryError e) {
            e.printStackTrace();
		}

		return bmp;
	}

	public static byte[] getByteFormImageFile(String imageFile){
		return getByteFormImageFile(imageFile, 0);
	}
	
	/**
	 * 按照最大像素压缩图片 
	 * 
	 * @author eric.huang
	 * @date 2013-8-20
	 * @param imageFile
	 * @param maxNumOfPixels
	 * @return
	 */
	public static byte[] getByteFormImageFile(String imageFile, double maxNumOfPixels) {
		if(null == imageFile){
			return null;
		}
		
		if(imageFile.length() < 1){
			return null;
		}
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inPurgeable = true;
		BitmapFactory.decodeFile(imageFile, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels > 0 ? maxNumOfPixels : MAX_IMAGE_DECODE_ZONE);
		
		// 默认是Bitmap.Config.ARGB_8888, 图片编码格式 
//		opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
				
		opts.inJustDecodeBounds = false;
		Bitmap bmp = null;
		
		try {
			bmp = BitmapFactory.decodeFile(imageFile, opts);
		} catch (OutOfMemoryError e) {
            e.printStackTrace();
		}
		
		return getByteFromBitmap(bmp);
	}
	
	/**
	 * 从字节中获取bitmapDrawable对象
	 * 
	 * @author eric.huang
	 * @date 2013-8-22
	 * @param dataByte
	 * @return
	 */
	public static BitmapDrawable getBitmapDrawableFormByte(byte[] dataByte){
		Bitmap bitmap = getBitmapFromByte(dataByte);
		BitmapDrawable bitmapDrawable = null;
		if(null != bitmap){
			bitmapDrawable = new BitmapDrawable(XhApplication.getAppication().getResources(), bitmap);
		}
		return bitmapDrawable;
	}
	
	/**
	 * 从资源中获取BitMapDrawable
	 * 
	 * @author eric.huang
	 * @date 2013-8-28
	 * @param resourceId
	 * @return
	 */
	public static BitmapDrawable getBitmapDrawableFormResource(int resourceId){
		Bitmap bitmap = getBitmapFromResource(resourceId);
		BitmapDrawable bitmapDrawable = null;
		if(null != bitmap){
			bitmapDrawable = new BitmapDrawable(XhApplication.getAppication().getResources(), bitmap);
		}
		return bitmapDrawable;
	}
	
	/**
	 * 通过输入流加载位图
	 * BitmapFactory.decodeResource 是通过Java层来createBitmap来完成图片的加载，增加了java层的内存消耗。
	 * 而 BitmapFactory.decodeStream 则是直接调用了JNI，避免了java层的消耗。
	 * 同时，在加载图片时，图片Config参数也可以有效减少内存的消耗。
	 * 比如图片存储的位数及options.inSampleSize 图片的尺寸等。
	 * 
	 * @author eric.huang
	 * @date 2013-8-7
	 * @param resId
	 * @return
	 */
	public static Bitmap getBitmapFromResource(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		Bitmap bitmap = null;
		
		// 图片存储位数
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;

		// 设置图片可以被回收
		opt.inPurgeable = true;

		// 对象引用是否可分享 只有inPurgeable为true时才生效
		opt.inInputShareable = true;

		InputStream is = XhApplication.getAppication().getResources().openRawResource(resId);
		bitmap = BitmapFactory.decodeStream(is, null, opt);
		
		if(null != is){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 压缩图片缩放比
	 * 
	 * @date 2012-12-25
	 * @author eric.huang
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, double maxNumOfPixels) {
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

	/**
	 * 获取二进制图像的缩放比
	 * 
	 * @date 2012-12-25
	 * @author eric.huang
	 * @param minSideLength 最小边长
	 * @param maxNumOfPixels 期望得到的图像像素
	 * 
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, double maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
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
	 * 将图片圆角化处理
	 * 
	 * @param bitmap 原图对象
	 * @param pixels 圆角的像素值
	 * 
	 * @return 返回圆角处理后的图片对象
	 */
	public static Bitmap setRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 获取图片属性旋转角度
	 */
	public static int getPicRotate(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 获取图片文件的信息，是否旋转了，如果没转则旋转
	 */
	public static Bitmap picRotate(Bitmap bitmap, String path) {
		int degree = getPicRotate(path);
		if (degree != 0) {
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
		}
		return bitmap;
	}

}
