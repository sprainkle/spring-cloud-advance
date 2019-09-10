package com.sprainkle.spring.cloud.advance.common.core.util;

import cn.hutool.core.util.StrUtil;
import com.sprainkle.spring.cloud.advance.common.core.constant.enums.CommonResponseEnum;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 *
 * </p>
 *
 * @author sprinkle
 * @date 2019-09-10
 */
public class TimeUtil {

    /**
     * {@link DateTimeFormatter}是线程安全的, 所以可以缓存起来
     */
    public final static ConcurrentMap<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap<>(8);

    public final static String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public final static String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * 星期数组
     */
    public final static String[] DAY_OF_WEEK = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    /**
     * {@link LocalDateTime} 转换为 {@link Date}
     *
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();

        return Date.from(instant);
    }

    /**
     * {@link LocalDate} 转换为 {@link Date}
     *
     * @param localDate
     * @return
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();

        return Date.from(instant);
    }

    /**
     * {@link LocalTime} 转换为 {@link Date}
     *
     * @param localTime
     * @return
     */
    public static Date toDate(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = LocalDateTime.of(LocalDate.now(), localTime).atZone(zone).toInstant();

        return Date.from(instant);
    }

    /**
     * timestamp(时间戳) 转换为 {@link LocalDateTime}
     *
     * @param timestamp 时间戳
     * @return
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
    }

    /**
     * timestamp(时间戳) 转换为 {@link LocalDate}
     *
     * @param timestamp 时间戳
     * @return
     */
    public static LocalDate toLocalDate(long timestamp) {
        return toLocalDateTime(timestamp).toLocalDate();
    }

    /**
     * timestamp(时间戳) 转换为 {@link LocalTime}
     *
     * @param timestamp 时间戳
     * @return
     */
    public static LocalTime toLocalTime(long timestamp) {
        return toLocalDateTime(timestamp).toLocalTime();
    }

    /**
     * 根据 {@link LocalDate} 获取对应 零点 的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localDate
     * @return
     */
    public static Long toSecond(LocalDate localDate) {
        return toSecond(localDate, ZoneId.systemDefault());
    }

    /**
     * 根据 {@link LocalDate} 获取对应 零点 的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localDate
     * @return
     */
    public static Long toMilli(LocalDate localDate) {
        return toMilli(localDate, ZoneId.systemDefault());
    }

    /**
     * 根据 {@link LocalTime} 获取对应 当天零点 的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localTime
     * @return
     */
    public static Long toSecond(LocalTime localTime) {
        return toSecond(localTime, ZoneId.systemDefault());
    }

    /**
     * 根据 {@link LocalTime} 获取对应 当天零点 的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localTime
     * @return
     */
    public static Long toMilli(LocalTime localTime) {
        return toMilli(localTime, ZoneId.systemDefault());
    }

    /**
     * 根据 {@link LocalDateTime} 获取对应的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localDateTime
     * @return
     */
    public static Long toSecond(LocalDateTime localDateTime) {
        return toSecond(localDateTime, ZoneId.systemDefault());
    }

    /**
     * 根据 {@link LocalDateTime} 获取对应的毫秒数. 时区默认使用的是系统当前时区.
     *
     * @param localDateTime
     * @return
     */
    public static Long toMilli(LocalDateTime localDateTime) {
        return toMilli(localDateTime, ZoneId.systemDefault());
    }

    /**
     * 根据 {@link LocalDate} 获取对应 零点 的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localDate
     * @param timeZone
     * @return
     */
    public static Long toSecond(LocalDate localDate, TimeZone timeZone) {
        if (timeZone == null) {
            return null;
        }

        return toSecond(localDate, timeZone.toZoneId());
    }

    /**
     * 根据 {@link LocalTime} 获取对应 当天零点 的毫秒数. 时区默认使用的是系统当前时区.
     *
     * @param localTime
     * @param timeZone
     * @return
     */
    public static Long toMilli(LocalTime localTime, TimeZone timeZone) {
        if (timeZone == null) {
            return null;
        }

        return toMilli(localTime, timeZone.toZoneId());
    }

    /**
     * 根据 {@link LocalTime} 获取对应 当天零点 的秒数. 时区默认使用的是系统当前时区.
     *
     * @param localTime
     * @param timeZone
     * @return
     */
    public static Long toSecond(LocalTime localTime, TimeZone timeZone) {
        if (timeZone == null) {
            return null;
        }

        return toSecond(localTime, timeZone.toZoneId());
    }

    /**
     * 根据 {@link LocalDate} 获取对应 零点 的毫秒数. 时区默认使用的是系统当前时区.
     *
     * @param localDate
     * @param timeZone
     * @return
     */
    public static Long toMilli(LocalDate localDate, TimeZone timeZone) {
        if (timeZone == null) {
            return null;
        }

        return toMilli(localDate, timeZone.toZoneId());
    }

    /**
     * 根据 {@link LocalDateTime} 获取指定时区对应的秒数.
     *
     * @param localDateTime
     * @param timeZone      指定时区
     * @return
     */
    public static Long toSecond(LocalDateTime localDateTime, TimeZone timeZone) {
        if (timeZone == null) {
            return null;
        }

        return toSecond(localDateTime, timeZone.toZoneId());
    }

    /**
     * 根据 {@link LocalDateTime} 获取指定时区对应的毫秒数.
     *
     * @param localDateTime
     * @param timeZone      指定时区
     * @return
     */
    public static Long toMilli(LocalDateTime localDateTime, TimeZone timeZone) {
        if (timeZone == null) {
            return null;
        }
        return toMilli(localDateTime, timeZone.toZoneId());
    }

    /**
     * 根据 {@link LocalDate} 和 {@link ZoneId} 时区, 获取指定时区 零点 对应的秒数.
     *
     * @param localDate
     * @param zoneId
     * @return
     */
    public static Long toSecond(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null || zoneId == null) {
            return null;
        }
        return toSecond(LocalDateTime.of(localDate, LocalTime.MIN), zoneId);
    }

    /**
     * 根据 {@link LocalDate} 和 {@link ZoneId} 时区, 获取指定时区 零点 对应的毫秒数.
     *
     * @param localDate
     * @param zoneId
     * @return
     */
    public static Long toMilli(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null || zoneId == null) {
            return null;
        }
        return toMilli(LocalDateTime.of(localDate, LocalTime.MIN), zoneId);
    }

    /**
     * 根据 {@link LocalTime} 和 {@link ZoneId} 时区, 获取指定时区 当天零点 对应的秒数.
     *
     * @param localTime
     * @param zoneId
     * @return
     */
    public static Long toSecond(LocalTime localTime, ZoneId zoneId) {
        if (localTime == null || zoneId == null) {
            return null;
        }
        return toSecond(LocalDateTime.of(LocalDate.now(), localTime), zoneId);
    }

    /**
     * 根据 {@link LocalTime} 和 {@link ZoneId} 时区, 获取指定时区 当天零点 对应的毫秒数.
     *
     * @param localTime
     * @param zoneId
     * @return
     */
    public static Long toMilli(LocalTime localTime, ZoneId zoneId) {
        if (localTime == null || zoneId == null) {
            return null;
        }
        return toMilli(LocalDateTime.of(LocalDate.now(), localTime), zoneId);
    }

    /**
     * 根据 {@link LocalDateTime} 和 {@link ZoneId} 时区, 获取指定时区对应的秒数.
     *
     * @param localDateTime
     * @param zoneId    时区
     * @return
     */
    public static Long toSecond(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null || zoneId == null) {
            return null;
        }

        return localDateTime.atZone(zoneId).toInstant().getEpochSecond();
    }

    /**
     * 根据 {@link LocalDateTime} 和 {@link ZoneId} 时区, 获取指定时区对应的毫秒数.
     *
     * @param localDateTime
     * @param zoneId    时区
     * @return
     */
    public static Long toMilli(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null || zoneId == null) {
            return null;
        }

        return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    /**
     * 根据 {@link LocalDate} 和 {@link ZoneOffset} 时区偏移量, 获取指定时区 零点 对应的秒数.
     *
     * @param localDate
     * @param zoneOffset
     * @return
     */
    public static Long toSecond(LocalDate localDate, ZoneOffset zoneOffset) {
        if (localDate == null || zoneOffset == null) {
            return null;
        }
        return toSecond(LocalDateTime.of(localDate, LocalTime.MIN), zoneOffset);
    }

    /**
     * 根据 {@link LocalDate} 和 {@link ZoneOffset} 时区偏移量, 获取指定时区 零点 对应的毫秒数.
     *
     * @param localDate
     * @param zoneOffset
     * @return
     */
    public static Long toMilli(LocalDate localDate, ZoneOffset zoneOffset) {
        if (localDate == null || zoneOffset == null) {
            return null;
        }
        return toMilli(LocalDateTime.of(localDate, LocalTime.MIN), zoneOffset);
    }

    /**
     * 根据 {@link LocalTime} 和 {@link ZoneOffset} 时区偏移量, 获取指定时区 当天零点 对应的秒数.
     *
     * @param localTime
     * @param zoneOffset
     * @return
     */
    public static Long toSecond(LocalTime localTime, ZoneOffset zoneOffset) {
        if (localTime == null || zoneOffset == null) {
            return null;
        }
        return toSecond(LocalDateTime.of(LocalDate.now(), localTime), zoneOffset);
    }

    /**
     * 根据 {@link LocalTime} 和 {@link ZoneOffset} 时区偏移量, 获取指定时区 当天零点 对应的毫秒数.
     *
     * @param localTime
     * @param zoneOffset
     * @return
     */
    public static Long toMilli(LocalTime localTime, ZoneOffset zoneOffset) {
        if (localTime == null || zoneOffset == null) {
            return null;
        }
        return toMilli(LocalDateTime.of(LocalDate.now(), localTime), zoneOffset);
    }

    /**
     * 根据 {@link LocalDateTime} 和 {@link ZoneOffset} 时区偏移量, 获取指定时区对应的秒数.
     *
     * @param localDateTime
     * @param zoneOffset    时区偏移量
     * @return
     */
    public static Long toSecond(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        if (localDateTime == null || zoneOffset == null) {
            return null;
        }

        return localDateTime.toEpochSecond(zoneOffset);
    }

    /**
     * 根据 {@link LocalDateTime} 和 {@link ZoneOffset} 时区偏移量, 获取指定时区对应的毫秒数.
     *
     * @param localDateTime
     * @param zoneOffset    时区偏移量
     * @return
     */
    public static Long toMilli(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        if (localDateTime == null || zoneOffset == null) {
            return null;
        }

        return localDateTime.toInstant(zoneOffset).toEpochMilli();
    }

    /**
     * 解析时间，日期格式：yyyy-MM-dd HH:mm:ss
     *
     * @param datetime 时间
     * @return
     */
    public static LocalDateTime parseDateTime(String datetime) {
        return parseDateTime(datetime, DEFAULT_DATE_TIME_PATTERN);
    }

    /**
     * 解析时间，日期格式：yyyy-MM-dd HH:mm:ss. 可返回 null.
     * @param datetime 时间
     * @return 日期字符串为空时, 是否返回null. 否则, 返回正确解析结果.
     */
    public static LocalDateTime parseDateTimeNullable(String datetime) {
        return parseDateTime(datetime, DEFAULT_DATE_TIME_PATTERN, true);
    }

    /**
     * 解析时间，日期格式：yyyy-MM-dd HH:mm:ss.
     * @param datetime 时间
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalDateTime parseDateTime(String datetime, boolean nullable) {
        return parseDateTime(datetime, DEFAULT_DATE_TIME_PATTERN, nullable);
    }

    /**
     * 解析日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return
     */
    public static LocalDateTime parseDateTime(String date, String pattern) {
        return parseDateTime(date, pattern, false);
    }

    /**
     * 解析日期
     *
     * @param date    日期
     * @param pattern 格式
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalDateTime parseDateTime(String date, String pattern, boolean nullable) {
        date = checkNotEmpty(date, nullable);
        if (date == null) {
            return null;
        }

        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * 解析日期，日期格式：yyyy-MM-dd
     *
     * @param date    日期
     * @return
     */
    public static LocalDate parseDate(String date) {
        return parseDate(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * 解析日期，日期格式：yyyy-MM-dd
     *
     * @param date    日期
     * @return 日期字符串为空时, 是否返回null. 否则, 返回正确解析结果.
     */
    public static LocalDate parseDateNullable(String date) {
        return parseDate(date, DEFAULT_DATE_PATTERN, true);
    }

    /**
     * 解析日期，日期格式：yyyy-MM-dd
     *
     * @param date    日期
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalDate parseDate(String date, boolean nullable) {
        return parseDate(date, DEFAULT_DATE_PATTERN, nullable);
    }

    /**
     * 解析日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return
     */
    public static LocalDate parseDate(String date, String pattern) {
        return parseDate(date, pattern, false);
    }

    /**
     * 解析日期
     *
     * @param date    日期
     * @param pattern 格式
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalDate parseDate(String date, String pattern, boolean nullable) {
        date = checkNotEmpty(date, nullable);
        if (date == null) {
            return null;
        }

        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalDate.parse(date, formatter);
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @return
     */
    public static LocalTime parseTime(String time) {
        return parseTime(time, DEFAULT_TIME_PATTERN);
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @return 日期字符串为空时, 是否返回null. 否则, 返回正确解析结果.
     */
    public static LocalTime parseTimeNullable(String time) {
        return parseTime(time, DEFAULT_TIME_PATTERN, true);
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalTime parseTime(String time, boolean nullable) {
        return parseTime(time, DEFAULT_TIME_PATTERN, nullable);
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @param pattern 格式
     * @return
     */
    public static LocalTime parseTime(String time, String pattern) {
        CommonResponseEnum.DATE_NOT_NULL.assertNotEmpty(time);

        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalTime.parse(time, formatter);
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @param pattern 格式
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalTime parseTime(String time, String pattern, boolean nullable) {
        time = checkNotEmpty(time, nullable);
        if (time == null) {
            return null;
        }

        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalTime.parse(time, formatter);
    }

    /**
     * 校验时间字符串非空
     * @param time 日期或时间 字符串
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     */
    private static String checkNotEmpty(String time, boolean nullable) {
        if (StrUtil.isNotBlank(time)) {
            return time;
        }

        if (!nullable) {
            CommonResponseEnum.DATE_NOT_NULL.assertFail(time);
        }

        return null;
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @return
     */
    public static LocalTime parseTimeWithoutSecond(String time) {
        return parseTime(time, "HH:mm");
    }

    /**
     * 解析时间
     *
     * @param time    时间
     * @param nullable 日期字符串为空时, 是否返回null. 若为false, 当日期字符串为空时, 直接抛异常. 反之, 返回 null.
     * @return
     */
    public static LocalTime parseTimeWithoutSecond(String time, boolean nullable) {
        return parseTime(time, "HH:mm", nullable);
    }

    /**
     * 格式化
     * @param localDateTime 时间
     * @param pattern 格式
     * @return 格式化字符串
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(getFormatter(pattern));
    }

    /**
     * 格式化为: yyyy-MM-dd HH:mm:ss
     * @param localDateTime 时间
     * @return 格式化字符串
     */
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DEFAULT_DATE_TIME_PATTERN);
    }

    /**
     * 格式化
     * @param localDate 时间
     * @param pattern 格式
     * @return 格式化字符串
     */
    public static String format(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return null;
        }

        return localDate.format(getFormatter(pattern));
    }

    /**
     * 格式化为: yyyy-MM-dd
     * @param localDate 时间
     * @return 格式化字符串
     */
    public static String format(LocalDate localDate) {

        return format(localDate, DEFAULT_DATE_PATTERN);
    }

    /**
     * 格式化
     * @param localTime 时间
     * @param pattern 格式
     * @return 格式化字符串
     */
    public static String format(LocalTime localTime, String pattern) {
        if (localTime == null) {
            return null;
        }

        return localTime.format(getFormatter(pattern));
    }

    /**
     * 格式化为: HH:mm:ss
     * @param localTime 时间
     * @return 格式化字符串
     */
    public static String format(LocalTime localTime) {

        return format(localTime, DEFAULT_DATE_PATTERN);
    }

    /**
     * 格式化为: HH:mm
     * @param localTime 时间
     * @return 格式化字符串
     */
    public static String formatTimeWithoutSecond(LocalTime localTime) {
        return format(localTime, "HH:mm");
    }

    /**
     * 克隆一个全新的{@link LocalDateTime}
     * @param dateTime
     * @return
     */
    public static LocalDateTime clone(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.of(dateTime.toLocalDate(), dateTime.toLocalTime());
    }

    /**
     * 克隆一个全新的{@link LocalDate}
     * @param date
     * @return
     */
    public static LocalDate clone(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
    }

    /**
     * 克隆一个全新的{@link LocalTime}
     * @param time
     * @return
     */
    public static LocalTime clone(LocalTime time) {
        if (time == null) {
            return null;
        }
        return LocalTime.of(time.getHour(), time.getMinute(), time.getSecond());
    }

    /**
     * 格式化为: 周*
     * @param dayOfWeek
     * @return
     */
    public static String formatDayOfWeek(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return null;
        }
        return formatDayOfWeek(dayOfWeek.getValue());
    }

    /**
     * 格式化为: 周*
     * @param dayOfWeek
     * @return
     */
    public static String formatDayOfWeek(int dayOfWeek) {
        return DAY_OF_WEEK[dayOfWeek-1];
    }

    /**
     * 格式化为: prefix+*. eg: prefix = '星期', 格式化结果为: 星期*
     * @param dayOfWeek {@link DayOfWeek}
     * @param prefix 前缀, 如: 星期
     * @return
     */
    public static String formatDayOfWeek(DayOfWeek dayOfWeek, String prefix) {
        if (dayOfWeek == null) {
            return null;
        }
        return formatDayOfWeek(dayOfWeek.getValue(), prefix);
    }

    /**
     * 格式化为: prefix+*. eg: prefix = '星期', 格式化结果为: 星期*
     * @param dayOfWeek 星期一: 1, ..., 星期天: 7
     * @param prefix 前缀, 如: 星期
     * @return
     */
    public static String formatDayOfWeek(int dayOfWeek, String prefix) {
        String tmp = formatDayOfWeek(dayOfWeek);
        if (StrUtil.isBlank(prefix)) {
            return tmp;
        }
        return prefix + tmp.substring(1);
    }

    /**
     * 根据出生年份计算年龄
     * @param birthday 为空时, 返回0
     * @return 年龄
     */
    public static Integer getAge(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        LocalDate now = LocalDate.now();
        int thisYear = now.getYear();
        int birthYear = birthday.getYear();
        return thisYear - birthYear;
    }

    /**
     * 获取本月第一天
     * @return
     */
    public static LocalDate getMonthFirstDay(LocalDate today) {
        return today.minusDays(today.getDayOfMonth() - 1);
    }

    /**
     * 获取本月最后一天
     * @return
     */
    public static LocalDate getMonthLastDay(LocalDate today) {
        return today.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取本周周一
     * @return
     */
    public static LocalDate getWeekFirstDay(LocalDate today) {
        return today.minusDays(today.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }

    /**
     * 获取本周周日
     * @return
     */
    public static LocalDate getWeekLastDay(LocalDate today) {
        return today.plusDays(DayOfWeek.SUNDAY.getValue()  - today.getDayOfWeek().getValue());
    }

    /**
     * 是否在某个时间段内
     * @param date 目标日期
     * @param startDate 开始日期
     * @param endDate 截止日期. 当为null时, 只要 date > startDate 则返回true.
     * @return
     */
    public static boolean isBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null) {
            return false;
        }

        return !date.isBefore(startDate) && (endDate == null || !date.isAfter(endDate));
    }

    /**
     * 是否在某个时间段内
     * @param time 目标时间
     * @param startTime 开始时间
     * @param endTime 截止时间. 当为null时, 只要 time > startTime 则返回true.
     * @return
     */
    public static boolean isBetween(LocalDateTime time, LocalDateTime startTime, LocalDateTime endTime) {
        if (time == null || startTime == null) {
            return false;
        }

        return !time.isBefore(startTime) && (endTime == null || !endTime.isAfter(time));
    }

    /**
     * 从缓存中获取{@link DateTimeFormatter}
     * @param pattern
     * @return
     */
    private static DateTimeFormatter getFormatter(String pattern) {
        CommonResponseEnum.PATTERN_NOT_NULL.assertNotEmpty(pattern);

        DateTimeFormatter formatter = FORMATTER_MAP.get(pattern);
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            FORMATTER_MAP.putIfAbsent(pattern, formatter);
        }
        return formatter;
    }

    /**
     * 获取日期区间的所有日期. 包含首尾日期.
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return 日期区间列表
     */
    public static List<LocalDate> getBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return Collections.emptyList();
        }

        if (startDate.isAfter(endDate)) {
            return Collections.emptyList();
        }

        List<LocalDate> result = new LinkedList<>();
        LocalDate tmpDate = TimeUtil.clone(startDate);
        while (!tmpDate.isAfter(endDate)) {
            result.add(tmpDate);
            tmpDate = tmpDate.plusDays(1);
        }
        return result;
    }

    /**
     * 获取两个时间相差秒数
     * @param startDateTime 开始时间
     * @param endDateTime 结束时间
     * @return
     */
    public static long getBetweenSeconds(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        CommonResponseEnum.DATETIME_NOT_NULL.assertNotNull(startDateTime);
        CommonResponseEnum.DATETIME_NOT_NULL.assertNotNull(endDateTime);

        Duration between = Duration.between(startDateTime, endDateTime);

        return between.getSeconds();
    }

    /**
     * 获取两个时间相差秒数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static long getBetweenSeconds(LocalTime startTime, LocalTime endTime) {
        CommonResponseEnum.TIME_NOT_NULL.assertNotNull(startTime);
        CommonResponseEnum.TIME_NOT_NULL.assertNotNull(endTime);

        Duration between = Duration.between(startTime, endTime);

        return between.getSeconds();
    }

    /**
     * 分秒置0
     * @param dateTime 目标时间
     * @return
     */
    public static LocalDateTime resetMinSecToZero(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusSeconds(dateTime.getMinute() * 60 + dateTime.getSecond());
    }

    /**
     * 获取明天日期
     * @return 明天日期
     */
    public static LocalDateTime getTomorrow() {
        return getTomorrow(LocalDateTime.now());
    }

    /**
     * 获取给定日期的明天
     * @param date 给定日期
     * @return 给定日期的明天
     */
    public static LocalDateTime getTomorrow(LocalDateTime date) {
        if (date == null) {
            return null;
        }

        return clone(date).plusDays(1);
    }

    /**
     * 获取给定日期的明天
     * @param date 给定日期
     * @return 给定日期的明天
     */
    public static LocalDate getTomorrow(LocalDate date) {
        if (date == null) {
            return null;
        }

        return clone(date).plusDays(1);
    }

    /**
     * 获取昨天日期
     * @return 昨天日期
     */
    public static LocalDateTime getYesterday() {
        return getYesterday(LocalDateTime.now());
    }

    /**
     * 获取给定日期的昨天
     * @param date 给定日期
     * @return 给定日期的昨天
     */
    public static LocalDateTime getYesterday(LocalDateTime date) {
        if (date == null) {
            return null;
        }

        return clone(date).plusDays(-1);
    }

    /**
     * 获取给定日期的昨天
     * @param date 给定日期
     * @return 给定日期的昨天
     */
    public static LocalDate getYesterday(LocalDate date) {
        if (date == null) {
            return null;
        }

        return clone(date).plusDays(-1);
    }

    /**
     * 获取当天零点
     * @return 当天零点
     */
    public static LocalDateTime getMidnight() {
        return getMidnight(LocalDateTime.now());
    }

    /**
     * 获取指定日期零点
     * @param date 指定日期
     * @return 指定日期零点
     */
    public static LocalDateTime getMidnight(LocalDateTime date) {
        if (date == null) {
            return null;
        }

        return LocalDateTime.of(date.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取明天零点
     * @return 明天零点
     */
    public static LocalDateTime getTomorrowMidnight() {
        return getMidnight(getTomorrow());
    }

    /**
     * 获取昨天零点
     * @return 昨天零点
     */
    public static LocalDateTime getYesterdayMidnight() {
        return getMidnight(getYesterday());
    }

}
