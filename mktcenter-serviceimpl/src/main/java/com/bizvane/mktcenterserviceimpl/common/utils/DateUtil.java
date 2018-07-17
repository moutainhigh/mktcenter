package com.bizvane.mktcenterserviceimpl.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chen.li
 * @date on 2018/7/13 9:09
 * @description
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
public class DateUtil {

    /**
     * 格式化时间
     * @param date
     * @param dateFormat
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 获取cron表达式
     * @param date
     * @return
     */
    public static String getCronExpression(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }
}