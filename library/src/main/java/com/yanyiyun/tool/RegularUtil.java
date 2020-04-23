package com.yanyiyun.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegularUtil {
    /**
     * 一个或多个汉字
     */
    private final String CHINESE_CHARACTERS="^[\\u0391-\\uFFE5]+$";
    /**
     * 邮政编码
     */
    private final String THE_ZIP_CODE="^[1-9]\\d{5}$";
    /**
     * qq号码
     */
    private final String QQ_CODE="^[1-9]\\d{4,10}$";
    /**
     * 邮箱
     */
    private final String EMAIL="^[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-Z0-9]-*){1,}\\.){1,3}[a-zA-Z\\-]{1,}$";
    /**
     * 用户名（字母开头 + 数字/字母/下划线）
     */
    private final String USER_NAME="^[A-Za-z][A-Za-z1-9_-]+$";

    /**
     * 手机号
     */
    private final String PHONE="^1[3|3|5|8|4][0-9]\\d{8}$";

    /**
     * URL
     */
    private final String RURL="^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$ ";
    /**
     * 身份证号码
     */
    private final String ID_CARD_NUMBER="^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$^(\\d{6}(18|19|20)?";

    private static RegularUtil regularUtil;

    private RegularUtil(){}

    public static RegularUtil getInstance(){
        if(regularUtil==null){
            synchronized (RegularUtil.class){
                regularUtil=new RegularUtil();
            }
        }
        return regularUtil;
    }

    public enum RegularType{
        /**
         * 一个或多个汉字
         */
        CHINESE_CHARACTERS,
        /**
         * 邮政编码
         */
        THE_ZIP_CODE,
        /**
         * qq号码
         */
        QQ_CODE,
        /**
         * 邮箱
         */
        EMAIL,
        /**
         * 用户名（字母开头 + 数字/字母/下划线）
         */
        USER_NAME,
        /**
         * 手机号
         */
        PHONE,
        /**
         * URL
         */
        RURL,
        /**
         * 身份证号码
         */
        ID_CARD_NUMBER,
    }

    public boolean check(RegularType type,String s){
        Pattern pattern=Pattern.compile(getReg(type));
        Matcher matcher=pattern.matcher(s);
        return matcher.find();
    }

    /**
     * 获得正则表达式
     * @param type
     * @return
     */
    private String getReg(RegularType type){
        String s=null;
        if(type==RegularType.CHINESE_CHARACTERS){
            s=CHINESE_CHARACTERS;
        }else if(type==RegularType.THE_ZIP_CODE){
            s=THE_ZIP_CODE;
        }else if(type==RegularType.QQ_CODE){
            s=QQ_CODE;
        }else if(type==RegularType.EMAIL){
            s=EMAIL;
        }else if(type==RegularType.USER_NAME){
            s=USER_NAME;
        }else if(type==RegularType.PHONE){
            s=PHONE;
        }else if(type==RegularType.RURL){
            s=RURL;
        }else if(type==RegularType.ID_CARD_NUMBER){
            s=ID_CARD_NUMBER;
        }
        return s;
    }
}
