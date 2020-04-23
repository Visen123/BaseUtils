package com.yanyiyun.tool;

import android.util.Log;

public class log {

	public static  boolean isOutput = true;
	public  static final int I=0;
	public static final int E=1;
	public static final int D=2;
	public static final int V=3;
	public static final int W=4;
	private static int currentTag=I;

	public void setIsOutput(boolean is){
		isOutput=is;
	}
	
	public static  void setInLog(int tag){
		currentTag=tag;
	}
	
	/**
	 * jar包内部使用的log
	 * @param s
	 */
	public static void inlog(String s){
		switch (currentTag) {
		case I:
			i(s);
			break;
		case E:
			e(s);
			break;
		case D:
			d(s);
			break;
		case V:
			v(s);
			break;
		case W:
			w(s);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 信息 
	 * @param s
	 */
	public static void i(String s) {
		if (isOutput) {
			Log.i(getTraceInfo(), s);
		}
	}

	/**
	 * 错误
	 * @param s
	 */
	public static void e(String s) {
		if (isOutput) {
			Log.e(getTraceInfo(), s);
		}
	}

	/**
	 * debug
	 * @param s
	 */
	public static void d(String s) {
		if (isOutput) {
			Log.d(getTraceInfo(), s);
		}
	}

	/**
	 * Verbose
	 * @param s
	 */
	public static void v(String s) {
		if (isOutput) {
			Log.v(getTraceInfo(), s);
		}
	}

	/**
	 * 警告
	 * @param s
	 */
	public static void w(String s) {
		if (isOutput) {
			Log.w(getTraceInfo(), s);
		}
	}

	public static String getTraceInfo() {
		StringBuffer sb = new StringBuffer();

		StackTraceElement[] stacks = new Throwable().getStackTrace();
		sb.append(stacks[2].getClassName()).append(".")
				.append(stacks[2].getLineNumber());

		return sb.toString();
	}
}
