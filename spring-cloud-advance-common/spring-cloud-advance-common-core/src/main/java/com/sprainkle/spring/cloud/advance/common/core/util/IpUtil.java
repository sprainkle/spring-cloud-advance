package com.sprainkle.spring.cloud.advance.common.core.util;

import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 *  获取ip工具类
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class IpUtil {

    private static final String DOT = ".";

    /**
     * 获取客户端ip. 请求体默认从全局上下文获取.
     * @param otherHeaderNames
     * @return
     */
    public static String getClientIP(String... otherHeaderNames) {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }

        return getClientIP(request, otherHeaderNames);
    }

    /**
     * 获取客户端ip
     * @param request 请求对象
     * @param otherHeaderNames 其他自定义头文件
     * @return
     */
    public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
        return ServletUtil.getClientIP(request, otherHeaderNames);
    }

    /**
     * <p>ip使用int表示.</p>
     * <p>
     *     ip有4段, 每段最大值为255, 即 2^8 - 1, 刚好是一个字节能表示的最大值,
     *     所以4个字节的int刚好能用来表示一个ip地址.
     * </p>
     * @param ip
     * @return
     */
    public static Integer ipToInt(String ip) {
        String[] ips = ip.split("\\.");
        int ipFour = 0;
        //因为每个位置最大255，刚好在2进制里表示8位
        for(String ip4: ips){
            int ip4a = Integer.parseInt(ip4);
            //这里应该用+也可以,但是位运算更快
            ipFour = (ipFour << 8) | ip4a;
        }
        return ipFour;
    }

    /**
     * 使用int表示的ip地址, 转换成字符串的ip
     * @param ip 使用int表示的ip地址
     * @return
     */
    public static String intToIp(Integer ip) {
        //思路很简单，每8位拿一次，就是对应位的IP
        StringBuilder sb = new StringBuilder();
        for(int i = 3; i >= 0; i--){
            int ipa = (ip >> (8 * i)) & (0xff);
            sb.append(".").append(ipa);
        }
        sb.delete(0, 1);
        return sb.toString();
    }

}
