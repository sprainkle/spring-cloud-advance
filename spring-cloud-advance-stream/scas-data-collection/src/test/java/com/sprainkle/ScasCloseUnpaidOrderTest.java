package com.sprainkle;

import cn.hutool.core.util.RandomUtil;
import com.sprainkle.event.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/6/2
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("delayed")
@EnableBinding({ScasCloseUnpaidOrderTest.MessageSink.class, ScasCloseUnpaidOrderTest.MessageSource.class})
public class ScasCloseUnpaidOrderTest {

    @Autowired
    private CloseUnpaidOrderProducer closeUnpaidOrderProducer;

    @Test
    public void test() throws InterruptedException {

        // 模拟每分钟的0秒执行定时任务
        long toSleep = 60000 - System.currentTimeMillis() % 60000;
        Thread.sleep(toSleep);

        List<OrderModel> models = buildUnpaidOrderModel();
        for (OrderModel model : models) {
            closeUnpaidOrderProducer.publish(model);
        }

        Thread.sleep(1000000);

    }

    private List<OrderModel> buildUnpaidOrderModel() {

        long now = System.currentTimeMillis();

        List<OrderModel> models = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {

            long id = RandomUtil.randomLong(10000, 100000);
            // 模拟 订单将在小于60s内过期
            long expireTime = now + RandomUtil.randomLong(0, 60) * 1000;

            OrderModel model = new OrderModel();
            model.setId(id);
            model.setExpireTime(expireTime);
            models.add(model);
        }
        return models;
    }

    @Component
    public static class CloseUnpaidOrderProducer {

        @Autowired
        private MessageSource messageSource;

        public void publish(OrderModel model) {
            long now = System.currentTimeMillis();
            long delay = model.getExpireTime() - now;
            Message<OrderModel> message = MessageBuilder.withPayload(model).setHeader("x-delay", delay).build();
            messageSource.closeUnpaidOrderOutput().send(message);
            log.info("发布 [关闭超时未支付订单] 消息. delay: {}, model: {}", delay, model);
        }

    }

    @Component
    public static class CloseUnpaidOrderHandler {

        private Random random = new Random();

        @StreamListener("closeUnpaidOrderInput")
        public void handle(OrderModel model) throws InterruptedException {

            log.info("检查订单状态, 关闭支付超时订单. model: {}", model);

            if (isPaySuccess()) {
                log.info("订单 [{}] 支付超时. 关闭订单.", model.getId());
            } else {
                log.info("订单 [{}] 支付完成.", model.getId());
            }
        }

        private boolean isPaySuccess() {
            // 模拟从支付系统查询支付状态.
            return random.nextInt(10) % 3 == 0;
        }

    }

    public interface MessageSource {

        @Output("closeUnpaidOrderOutput")
        MessageChannel closeUnpaidOrderOutput();

    }

    public interface MessageSink {

        @Input("closeUnpaidOrderInput")
        SubscribableChannel closeUnpaidOrderInput();

    }

}
