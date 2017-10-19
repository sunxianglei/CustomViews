package com.xianglei.customviews.utils;

import java.math.BigDecimal;

/**
 * 数学计算工具类
 * Created by sunxianglei on 2017/10/18.
 */

public class MathUtil {

    /**
     * 两个Float相加不失精度
     * @param a
     * @param b
     * @return
     */
    public static Float addFloatNum(float a, float b){
        BigDecimal bd1 = new BigDecimal(Float.toString(a));
        BigDecimal bd2 = new BigDecimal(Float.toString(b));
        return bd1.add(bd2).floatValue();
    }

    /**
     * 两个Float相减不失精度
     * @param a
     * @param b
     * @return
     */
    public static Float subFloatNum(float a, float b){
        BigDecimal bd1 = new BigDecimal(Float.toString(a));
        BigDecimal bd2 = new BigDecimal(Float.toString(b));
        return bd1.subtract(bd2).floatValue();
    }

    /**
     * 保留小数点后几位
     * @param a
     * @param num
     * @return
     */
    public static Float keepDecimals(float a, int num){
        return (float)(Math.round(a * Math.pow(10, num))) / (float) Math.pow(10, num);
    }

}
