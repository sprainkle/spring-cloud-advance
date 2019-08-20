package com.sprainkle;

import com.alibaba.fastjson.JSON;
import com.sprainkle.event.model.PacketModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitCommonProperties;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
@ActiveProfiles("dlq")
@EnableBinding({ScasDlqTest.MessageSink.class, ScasDlqTest.MessageSource.class})
public class ScasDlqTest {

    @Autowired
    private PacketUplinkProducer packetUplinkProducer;

    private Random random = new Random();
    private List<String> devEuis = new ArrayList<>(10);

    @PostConstruct
    private void initDevEuis() {
        devEuis.add("10001");
        devEuis.add("10002");
        devEuis.add("10003");
        devEuis.add("10004");
        devEuis.add("10005");
        devEuis.add("10006");
        devEuis.add("10007");
        devEuis.add("10008");
        devEuis.add("10009");
        devEuis.add("10010");
    }

    /**
     *
     */
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            String devEui = getDevEuis();
            packetUplinkProducer.publish(new PacketModel(devEui, UUID.randomUUID().toString()));
        }

        Thread.sleep(1000000);

    }

    private String getDevEuis() {
        return devEuis.get(random.nextInt(10));
    }

    /**
     * 原队列名称
     */
    private static final String ORIGINAL_QUEUE = "packetUplinkDlxTopic.scas-data-collection.dlx";
    /**
     * 死信队列名称. 由于没有自定义, 所以根据 spring cloud stream 死信队列名称生成规则, 在原队列名称后追加 '.dlq'.
     */
    private static final String DLQ = ORIGINAL_QUEUE + ".dlq";
    /**
     * 死信队列交换机. 默认为: {@link RabbitCommonProperties#DEAD_LETTER_EXCHANGE}, 值为 "DLX".
     */
    private static final String DLX = RabbitCommonProperties.DEAD_LETTER_EXCHANGE;
    /**
     * 死信交换机将死信路由到死信队列的 routing-key. 由于没有自定义, 所以根据 spring cloud stream 死信队列名称生成规则,
     * routing-key为原队列的名称.
     */
    private static final String routingKey = "packetUplinkDlxTopic.scas-data-collection";

    /**
     * 死信队列的处理逻辑
     * @param failedMessage
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(DLQ),
                    exchange = @Exchange(DLX),
                    key = routingKey
            ),
            concurrency = "1-5"
    )
    public void handleDlq(Message failedMessage) throws InterruptedException {
        Thread.sleep(10);
        log.info("进入 [上行数据包队列] 的死信队列. 完整消息: {};", failedMessage);
        log.info("body: {}", (PacketModel) JSON.parseObject(failedMessage.getBody(), PacketModel.class));
    }

    @Component
    public static class PacketUplinkProducer {

        @Autowired
        private MessageSource messageSource;

        public void publish(PacketModel model) {
            log.info("发布上行数据包消息. model: [{}].", model);
            messageSource.packetUplinkOutput().send(MessageBuilder.withPayload(model).build());
        }

    }

    @Component
    public static class PacketUplinkHandler {

        @StreamListener("packetUplinkInput")
        public void handle(PacketModel model) throws InterruptedException {
            Thread.sleep(1000);
            log.info("消费上行数据包消息. model: [{}].", model);
        }

    }

    public interface MessageSink {

        @Input("packetUplinkInput")
        SubscribableChannel packetUplinkInput();

    }

    public interface MessageSource {

        @Output("packetUplinkOutput")
        MessageChannel packetUplinkOutput();

    }

}
