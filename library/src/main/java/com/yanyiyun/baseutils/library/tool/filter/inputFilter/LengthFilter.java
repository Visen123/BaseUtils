package com.yanyiyun.baseutils.library.tool.filter.inputFilter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 限制输入的长度
 */
public class LengthFilter implements InputFilter {

    /**
     * 最大长度
     */
    private int mMax;

    public LengthFilter(int mMax) {
        this.mMax = mMax;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }

    }

    public int getMax() {
        return mMax;
    }
}
