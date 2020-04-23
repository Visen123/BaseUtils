package com.yanyiyun.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str==null||"".equals(str)||str.length()==0;
    }

    /**
     * 检查字符串是否为空白
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        int length;
        if(str==null||((length=str.length())==0)){
            return true;
        }

        for(int i=0;i<length;i++){
            if(!Character.isWhitespace(str.charAt(i))){
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str){
        Pattern pattern=Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取字符串中的存数字字符串
     * @param str
     * @return
     */
    public static String getNumberString(String str){
        String regEx="[^0-9]";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(str);
        return matcher.replaceAll("").trim();
    }

    /**
     * 删除字符串中的数字
     * @param str
     * @return
     */
    public static String removeNumber(String str){
        String regEx="[0-9]";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(str);
        return matcher.replaceAll("").trim();
    }
}
