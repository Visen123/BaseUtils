package com.yanyiyun.tool.comparator;

import java.util.Comparator;

/**
 * 字符串比较器
 */
public class StringComparator implements Comparator<String> {
    /**
     * 比较两字符串的大小
     * a<b 返回-1
     * a==b 返回0
     * a>b 返回1
     * 比较原理:先着个字母比较大小，如果较短的字符串中的字母都和较长字符串前面的字母相同则比较字符串的长度
     * 字符串长度相等并字符串中每个字母相等则字符串相等
     */
    @Override
    public int compare(String a, String b) {
        int limit=Math.min(a.length(),b.length());
        for(int i=0;i<limit;i++){
            char charA=a.charAt(i);
            char charB=b.charAt(i);
            if(charA!=charB){
                return charA<charB ? -1 :1;
            }
        }
        int lengthA=a.length();
        int lengthB=b.length();
        if(lengthA!=lengthB){
            return lengthA<lengthB ? -1:1;
        }
        return 0;
    }
}
