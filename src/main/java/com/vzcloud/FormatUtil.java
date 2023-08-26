package com.vzcloud;

import cn.hutool.core.io.FileUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzcloud.entity.AppInfo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @version V2.3
 * @ClassName:FormatUtil.java
 * @author: wgcloud
 * @date: November 16, 2019
 * @Description: FormatUtil.java
 * @Copyright: 2017-2022 www.wgstart.com. All rights reserved.
 */
public class FormatUtil {

    private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Format the double data, cut the number after the decimal point
     *
     * @param str
     * @param num
     * @return
     */
    public static double formatDouble(double str, int num) {
        java.math.BigDecimal b = new java.math.BigDecimal(str);
        double myNum3 = b.setScale(num, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        return myNum3;
    }

    public static String delChar(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        str = str.replace("%", "");
        return str;
    }

    /**
     * Get the current time
     *
     * @return Current date
     */
    public static Timestamp getNowTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        Timestamp nowTime = Timestamp.valueOf(dateFormat.format(new Date()));
        return nowTime;
    }

    public static Date getDateBefore(Date datetimes, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(datetimes);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * m turn to G to G
     *
     * @param str
     * @return
     */
    public static double mToG(String str) {
        double result = 0;
        double mod = 1024;
        if (str.contains("M")) {
            double f = Double.valueOf(str.replace("M", ""));
            result = f / mod;
        } else if (str.contains("K")) {
            double f = Double.valueOf(str.replace("K", ""));
            result = (f / mod) / mod;
        } else if (str.contains("T")) {
            double f = Double.valueOf(str.replace("T", ""));
            result = f * 1024;
        } else if (str.contains("G")) {
            result = Double.valueOf(str.replace("G", ""));
        }
        return formatDouble(result, 2);
    }

    public static String getPidByFile(AppInfo appInfo) {
        if ("1".equals(appInfo.getAppType())) {
            return appInfo.getAppPid();
        } else {
            try {
                String pid = FileUtil.readString(appInfo.getAppPid(), "UTF-8");
                if (!StringUtils.isEmpty(pid)) {
                    return pid.trim();
                }
            } catch (Exception e) {
                logger.error("Get PID file error", e);
            }
        }
        return "";
    }
}
