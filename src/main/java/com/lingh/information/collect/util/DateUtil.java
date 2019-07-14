package com.lingh.information.collect.util;

import java.util.Date;

public class DateUtil extends cn.hutool.core.date.DateUtil {

    public static Date after(Date date, Long offset){
        long time = date.getTime();
        time += offset;
        return new Date(time);
    }

}
