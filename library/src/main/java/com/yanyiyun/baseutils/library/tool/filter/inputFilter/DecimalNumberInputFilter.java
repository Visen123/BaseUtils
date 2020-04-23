package com.yanyiyun.baseutils.library.tool.filter.inputFilter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

public class DecimalNumberInputFilter implements InputFilter {
    /**
     * 小数位数
     */
    private int decimalNumber;

    public DecimalNumberInputFilter(int decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceContent = source.toString();
        String lastInputContent = dest.toString();

        //验证删除等按键
        if (TextUtils.isEmpty(sourceContent)) {
            return "";
        }
        //以小数点"."开头，默认为设置为“0.”开头
        if (sourceContent.equals(".") && lastInputContent.length() == 0) {
            return "0.";
        }
        //输入“0”，默认设置为以"0."开头
        if (sourceContent.equals("0") && lastInputContent.length() == 0) {
            return "0.";
        }

        //小数点后保留decimalNumber位
        if (lastInputContent.contains(".")) {
            int index = lastInputContent.indexOf(".");
            if (lastInputContent.length() - index >= decimalNumber +1&&dstart>index) {
                return "";
            }

        }
        return null;
    }
}
