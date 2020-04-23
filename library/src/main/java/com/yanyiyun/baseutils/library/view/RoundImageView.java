package com.yanyiyun.baseutils.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yanyiyun.baseutils.library.R;


/**
 * 鍦嗗舰ImageView锛屽彲璁剧疆锟�?澶氫袱涓搴︿笉鍚屼笖棰滆壊涓嶅悓鐨勫渾褰㈣竟妗嗭拷??
 * 
 * @author Alan
 */
public class RoundImageView extends AppCompatImageView {
	private int mBorderThickness = 0;
	private Context mContext;
	private int defaultColor = 0xFFFFFFFF;
	// 濡傛灉鍙湁鍏朵腑锟�?涓湁鍊硷紝鍒欏彧鐢讳竴涓渾褰㈣竟锟�?
	private int mBorderOutsideColor = 0;
	private int mBorderInsideColor = 0;
	// 鎺т欢榛樿闀匡拷?锟藉
	private int defaultWidth = 0;
	private int defaultHeight = 0;

	public RoundImageView(Context context) {
		super(context);
		mContext = context;
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.roundedimageview);
		mBorderThickness = a.getDimensionPixelSize(
				R.styleable.roundedimageview_border_thickness, 0);
		mBorderOutsideColor = a
				.getColor(R.styleable.roundedimageview_border_outside_color,
						defaultColor);
		mBorderInsideColor = a.getColor(
				R.styleable.roundedimageview_border_inside_color, defaultColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		this.measure(0, 0);
		if (drawable.getClass() == NinePatchDrawable.class)
			return;
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Config.ARGB_8888, true);
		if (defaultWidth == 0) {
			defaultWidth = getWidth();
		}
		if (defaultHeight == 0) {
			defaultHeight = getHeight();
		}
		// 淇濊瘉閲嶆柊璇诲彇鍥剧墖鍚庝笉浼氬洜涓哄浘鐗囧ぇ灏忥拷?锟芥敼鍙樻帶浠跺銆侀珮鐨勫ぇ灏忥紙閽堝瀹斤拷?锟介珮涓簑rap_content甯冨眬鐨刬mageview锛屼絾浼氬鑷磎argin鏃犳晥锟�?
		// if (defaultWidth != 0 && defaultHeight != 0) {
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// defaultWidth, defaultHeight);
		// setLayoutParams(params);
		// }
		int radius = 0;
		/*if (mBorderInsideColor != defaultColor
				&& mBorderOutsideColor != defaultColor) 
		*/
		
		if (mBorderInsideColor != defaultColor
				&& mBorderOutsideColor != defaultColor) {// 定义画两个边框，分别为外圆边框和内圆边框
			radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - 2 * mBorderThickness;
			// 画内圆
			drawCircleBorder(canvas, radius + mBorderThickness / 2,
					mBorderInsideColor);
			// 画外圆
			drawCircleBorder(canvas, radius + mBorderThickness
					+ mBorderThickness / 2, mBorderOutsideColor);
		} else if (mBorderInsideColor != defaultColor
				&& mBorderOutsideColor == defaultColor) {// 定义画一个边框
			radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2,
					mBorderInsideColor);
		} else if (mBorderInsideColor == defaultColor
				&& mBorderOutsideColor != defaultColor) {// 定义画一个边框
			radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2,
					mBorderOutsideColor);
		} else {// 没有边框
			radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2;
		}
		Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
		canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight
				/ 2 - radius, null);
	}

	/**
	 * 鑾峰彇瑁佸壀鍚庣殑鍦嗗舰鍥剧墖
	 * 
	 * @param radius
	 *            鍗婂緞
	 */
	public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 涓轰簡闃叉瀹介珮涓嶇浉绛夛紝閫犳垚鍦嗗舰鍥剧墖鍙樺舰锛屽洜姝ゆ埅鍙栭暱鏂瑰舰涓浜庝腑闂翠綅缃渶澶х殑姝ｆ柟褰㈠浘锟�?
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 楂樺ぇ浜庡
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 鎴彇姝ｆ柟褰㈠浘锟�?
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {// 瀹藉ぇ浜庨珮
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap鍥炴敹(recycle瀵艰嚧鍦ㄥ竷锟�?鏂囦欢XML鐪嬩笉鍒版晥锟�?)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	/**
	 * 杈圭紭鐢诲渾
	 */
	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		Paint paint = new Paint();
		/* 鍘婚敮锟�? */
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		/* 璁剧疆paint鐨勶拷??style锟�?涓篠TROKE锛氱┖锟�? */
		paint.setStyle(Paint.Style.STROKE);
		/* 璁剧疆paint鐨勫妗嗗锟�? */
		paint.setStrokeWidth(mBorderThickness);
		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
	}

}
