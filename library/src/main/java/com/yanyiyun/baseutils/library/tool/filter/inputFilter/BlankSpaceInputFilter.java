package com.yanyiyun.baseutils.library.tool.filter.inputFilter;

import android.text.InputFilter;
import android.text.Spanned;

import com.yanyiyun.baseutils.library.tool.log;

/**
 * 数字空格
 */
public class BlankSpaceInputFilter implements InputFilter {

    /**
     * 隔几位数字一个空格  默认是4
     */
    private int number=4;

    /**
     * 添加空格的数量
     */
    private int spaceNumber;

    public BlankSpaceInputFilter(){

    }

    public BlankSpaceInputFilter(int number) {
        this.number = number;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int length=dest.length();
        log.e("length:"+length);
        log.e("start:"+start+",end:"+end);
        StringBuffer sb=new StringBuffer();
        for(int i=start;i<end;i++){
            if(length!=0&&(length+i)%number==0){
                sb.append("\t"+source.charAt(i));
                length++;
            }else {
                sb.append(source.charAt(i));
            }
            length++;
        }
        log.e(sb.toString());
        return sb.toString();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
