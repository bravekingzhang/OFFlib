/*
 * Copyright (C) 2014 The Igame Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.tencent.com.offdemo.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Title: Time Tools. <br>
 */
public class TimeUtils {
    /**
     * Format the timestamp(MS);
     *
     * @param timestamp (MS)
     * @return
     */
    public static long getFormatTimeStamp(long timestamp) {
        if (timestamp > 1000000000000000L && timestamp < 1000000000000000L) {
            timestamp = timestamp / 1000;
        } else if (timestamp < 1000000000000L) {
            timestamp = timestamp * 1000;
        }
        return timestamp;
    }

    public static CharSequence showYearMonthDay(long timestamp) {
        if (timestamp == 0) {
            return "";
        }
        timestamp = getFormatTimeStamp(timestamp);
        return DateFormat.format("yyyy-MM-dd", timestamp);
    }

    /**
     * Change timestamp to normal pattern(Pattern like this : '2014年(同年时消失) 下午5：45').
     *
     * @param timestamp
     * @return
     */
    public static String show24Time(long timestamp) {
        if (timestamp == 0) {
            return "";
        }
        timestamp = getFormatTimeStamp(timestamp);

        Calendar showCalendar = Calendar.getInstance();
        showCalendar.setTime(new Date(timestamp));

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        StringBuffer showBuffer = new StringBuffer();

        //		if (timestamp > System.currentTimeMillis()) {
        //			return null;
        //		}

        if (showCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)) {
            showBuffer.append(showCalendar.get(Calendar.YEAR)).append("-");
        }
        showBuffer.append(showCalendar.get(Calendar.MONTH) + 1).append("-")
                .append(showCalendar.get(Calendar.DAY_OF_MONTH)).append(" ");
        showBuffer.append(showCalendar.get(Calendar.HOUR_OF_DAY));

        showBuffer.append(":");

        if (showCalendar.get(Calendar.MINUTE) < 10) {
            showBuffer.append("0");
        }
        showBuffer.append(showCalendar.get(Calendar.MINUTE));
        return showBuffer.toString();

    }

    /**
     * Change timestamp to normal pattern(Pattern like this : '2014年(同年时消失)12月3日(同年同月同日时消失) 下午5：45').
     *
     * @param timestamp
     * @return
     */
    public static String showTime(long timestamp) {
        if (timestamp == 0) {
            return "暂无登录记录";
        }

        timestamp = getFormatTimeStamp(timestamp);

        Calendar showCalendar = Calendar.getInstance();
        showCalendar.setTime(new Date(timestamp));

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        StringBuffer showBuffer = new StringBuffer();

        //		if (timestamp > System.currentTimeMillis()) {
        //			return null;
        //		}

        if (showCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)) {
            showBuffer.append(showCalendar.get(Calendar.YEAR)).append("-");
        }

        if (showCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)
                || showCalendar.get(Calendar.DAY_OF_MONTH) != currentCalendar.get(Calendar.DAY_OF_MONTH)) {
            showBuffer.append(showCalendar.get(Calendar.MONTH) + 1).append("-")
                    .append(showCalendar.get(Calendar.DAY_OF_MONTH)).append(" ");
        }

        if (showCalendar.get(Calendar.AM_PM) == Calendar.AM) {
            showBuffer.append("上午");
            showBuffer.append(showCalendar.get(Calendar.HOUR));
        } else if (showCalendar.get(Calendar.AM_PM) == Calendar.PM) {
            showBuffer.append("下午");
            if (showCalendar.get(Calendar.HOUR) == 0) {
                showBuffer.append(12);
            } else {
                showBuffer.append(showCalendar.get(Calendar.HOUR));
            }
        }

        showBuffer.append(":");

        if (showCalendar.get(Calendar.MINUTE) < 10) {
            showBuffer.append("0");
        }
        showBuffer.append(showCalendar.get(Calendar.MINUTE));
        return showBuffer.toString();
    }

    /**
     * Change timestamp to normal pattern(Pattern like this : '2014年(同年时消失)12月3日(同年同月同日时消失) 下午5：45').
     *
     * @param timestamp
     * @return
     */
    public static String showTimeOfDay(long timestamp) {
        if (timestamp == 0) {
            return "暂无登录记录";
        }

        timestamp = getFormatTimeStamp(timestamp);

        Calendar showCalendar = Calendar.getInstance();
        showCalendar.setTime(new Date(timestamp));

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        StringBuffer showBuffer = new StringBuffer();

        //		if (timestamp > System.currentTimeMillis()) {
        //			return null;
        //		}

        if (showCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)) {
            showBuffer.append(showCalendar.get(Calendar.YEAR)).append("-");
        }

        showBuffer.append(showCalendar.get(Calendar.MONTH) + 1).append("-")
                .append(showCalendar.get(Calendar.DAY_OF_MONTH)).append(" ");
        showBuffer.append(showCalendar.get(Calendar.HOUR_OF_DAY));
        showBuffer.append(":");

        if (showCalendar.get(Calendar.MINUTE) < 10) {
            showBuffer.append("0");
        }
        showBuffer.append(showCalendar.get(Calendar.MINUTE));
        return showBuffer.toString();
    }

    /**
     * Whether chat need show the time.
     *
     * @param lastTime
     * @param time
     * @return
     */
    public static boolean needShowTime(long lastTime, long time) {
        if (lastTime + 120000 < time) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return current time.(Pattern like this : '2013-06-01 03:01:06').
     *
     * @return
     */
    public static String showCurrentTime() {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        StringBuffer showBuffer = new StringBuffer();
        showBuffer.append(currentCalendar.get(Calendar.YEAR)).append("-")
                .append(currentCalendar.get(Calendar.MONTH) < 10 ? "0" : "")
                .append(currentCalendar.get(Calendar.MONTH)).append("-")
                .append(currentCalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")
                .append(currentCalendar.get(Calendar.DAY_OF_MONTH)).append(" ")
                .append(currentCalendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "")
                .append(currentCalendar.get(Calendar.HOUR_OF_DAY)).append(":")
                .append(currentCalendar.get(Calendar.MINUTE) < 10 ? "0" : "")
                .append(currentCalendar.get(Calendar.MINUTE)).append(":")
                .append(currentCalendar.get(Calendar.SECOND) < 10 ? "0" : "")
                .append(currentCalendar.get(Calendar.SECOND));

        return showBuffer.toString();
    }

    /**
     * Calculate the time before.(Pattern like this : '1年前','个月前','12小时前','5分钟前').
     *
     * @param timestamp
     * @return
     */
    public static String calTimesBefore(long timestamp) {
        if (timestamp > 1000000000000L && timestamp < 1000000000000000L) {
            timestamp = timestamp / 1000;
        } else if (timestamp > 1000000000000000L && timestamp < 1000000000000000L) {
            timestamp = timestamp / 1000000;
        }

        int hour = (int) ((System.currentTimeMillis() / 1000 - timestamp) / 3600);
        int minute = (int) ((System.currentTimeMillis() / 1000 - timestamp) / 60);
        int day = hour / 24;
        int month = day / 30;
        int year = day / 365;

        if (year > 0) {
            return year + "年前";
        } else if (month > 0) {
            return month + "个月前";
        } else if (day > 0) {
            return day + "天前";
        } else if (hour > 0) {
            return hour + "小时前";
        } else if (minute > 0) {
            return minute + "分钟前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 判断当前日期是星期几
     *
     * @param year  设置的需要判断的时间
     * @param month the pattern
     * @param day
     * @return dayForWeek 判断结果
     */
    public static String getWeek(int year, int month, int day) {
        String week = "周";

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week += "天";
                break;
            case Calendar.MONDAY:
                week += "一";
                break;
            case Calendar.TUESDAY:
                week += "二";
                break;
            case Calendar.WEDNESDAY:
                week += "三";
                break;
            case Calendar.THURSDAY:
                week += "四";
                break;
            case Calendar.FRIDAY:
                week += "五";
                break;
            case Calendar.SATURDAY:
                week += "六";
                break;
            default:
                break;
        }

        return week;
    }
}
