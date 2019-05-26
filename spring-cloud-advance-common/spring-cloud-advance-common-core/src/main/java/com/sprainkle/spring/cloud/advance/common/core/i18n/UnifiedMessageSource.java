package com.sprainkle.spring.cloud.advance.common.core.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Locale;

@Service
public class UnifiedMessageSource {

    @Resource
    private MessageSource messageSource;

    /**
     * 获取国际化消息
     * @param code 消息code
     * @return
     */
    public String getMessage(String code) {

        return getMessage(code, null);
    }

    /**
     * 获取国际化消息
     * @param code 消息code
     * @param args 参数
     * @return
     */
    public String getMessage(String code, Object[] args) {

        return getMessage(code, args, "");
    }

    /**
     * 获取国际化消息
     * @param code 消息code
     * @param args 参数
     * @param defaultMessage 默认消息
     * @return
     */
    public String getMessage(String code, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}