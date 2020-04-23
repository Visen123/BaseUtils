package com.yanyiyun.tool.filter.inputFilter;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * 全大写过滤器
 */
public class CapitalInputFilter implements InputFilter {
    /**
     *
     * @param source  输入的文字
     * @param start 输入 - 0，删除 - 0
     * @param end  输入 - source文字的长度，删除 - 0
     * @param dest 原先显示的内容
     * @param dstart 输入 - 原光标位置，删除 - 光标删除结束位置
     * @param dend  输入 - 原光标位置，删除 - 光标删除开始位置
     * @return  返回null则认同当前操作  返回其他则用返回的值替代当前操作
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        for(int i=start;i<end;i++){
            if(Character.isLowerCase(source.charAt(i))){
                char[] v=new char[end-start];
                TextUtils.getChars(source,start,end,v,0);
                String s=new String(v).toUpperCase();

                if(source instanceof  Spanned){
                    SpannableString sp=new SpannableString(s);
                    TextUtils.copySpansFrom((Spanned) source,start,end,null,sp,0);
                    return sp;
                }else {
                    return s;
                }
            }
        }
        return null;
    }
}
