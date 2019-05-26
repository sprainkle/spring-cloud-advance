package com.sprainkle.spring.cloud.advance.common.core.util;

import com.sprainkle.spring.cloud.advance.common.core.constant.enums.CommonResponseEnum;
import com.sprainkle.spring.cloud.advance.common.core.pojo.response.QR;
import com.sprainkle.spring.cloud.advance.common.core.pojo.response.QueryData;
import com.sprainkle.spring.cloud.advance.common.core.pojo.response.R;

import java.util.function.Supplier;

/**
 * <pre>
 *  远程调用工具类
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class ClientUtil {

    /**
     * 封装远程调用, 只返回关心的内容
     * @param supplier 远程调用真正逻辑, 返回内容为: {@link R<T>}
     * @param <T> 关心的内容类型
     * @return 关心的内容
     */
    public static <T> T execute(Supplier<R<T>> supplier) {
        R<T> r = supplier.get();
        CommonResponseEnum.assertSuccess(r);
        return r.getData();
    }

    /**
     * 封装远程调用, 只返回关心的内容
     * @param supplier 程调用真正逻辑, 返回内容为: {@link QR<T>}
     * @param <T> 关心的内容类型
     * @return 关心的内容
     */
    public static <T> QueryData<T> executePage(Supplier<QR<T>> supplier) {
        QR<T> qr = supplier.get();
        CommonResponseEnum.assertSuccess(qr);
        return qr.getData();
    }

}
