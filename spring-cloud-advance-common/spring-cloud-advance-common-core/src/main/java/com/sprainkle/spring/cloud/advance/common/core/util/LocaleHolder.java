package com.sprainkle.spring.cloud.advance.common.core.util;


import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 语言工具类
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class LocaleHolder {

    /**
     * 获取当前语言
     * @return
     */
    public static String getLang() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            return "zh_CN";
        }

        return locale.toString();
    }

    /**
     * 获取当前语言
     * @return
     */
    public static Locale getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            return Locale.SIMPLIFIED_CHINESE;
        }

        return locale;
    }
}
