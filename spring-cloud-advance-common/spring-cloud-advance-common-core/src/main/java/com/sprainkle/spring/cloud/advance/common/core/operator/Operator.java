package com.sprainkle.spring.cloud.advance.common.core.operator;

/**
 * <pre>
 *  面向操作编程???
 *
 *  先举个例子:
 *  用户的登录账号有手机号码和邮箱等, 所以现在有 <b>AccountTypeEnum</b> 枚举,
 *  在注册过程的最后一步 -- 插入数据, 我们需要将注册账号 set 到对应的字段,
 *  即: 手机 -> setPhone(phone), 邮箱 -> setEmail(email);
 *
 *  一般情况, 我们可以通过 switch(account) 的方式, 根据不同的账号类型把账号 set 到对应字段.
 *  这样做没错, 但很麻烦, 后续的维护成本也很高, 比如:
 *  1. 所有跟账号类型有关的逻辑, 都会有一个 switch(account) 代码块;
 *  2. 以后再加一种 account type, 所有 switch(account) 代码块都必须修改;
 *
 *  而<b>面向操作编程</b>可以屏蔽不用类型的差异, 只要在业务逻辑开始之前, 根据 account type 路由
 *  到对应的 <b>Operator</b>, 比如: phone -> PhoneOperator,
 *  接下来的所有与 account type 有关的操作, 只需要跟 <b>PhoneOperator</b> 打交道即可.
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019-09-09
 */
public interface Operator<K> {

    /**
     * Operator的名称, 同一类型的Operator的路由器{@link OperatorRouter}能够根据该值路由到当前的Operator
     * @return route key
     */
    K getName();

}
