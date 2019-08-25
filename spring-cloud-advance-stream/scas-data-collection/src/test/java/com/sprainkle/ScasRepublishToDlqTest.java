package com.sprainkle;

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
@ActiveProfiles("republishToDlq")
@EnableBinding({ScasRepublishToDlqTest.MessageSink.class, ScasRepublishToDlqTest.MessageSource.class})
public class ScasRepublishToDlqTest {

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
        for (int i = 0; i < 1; i++) {
            String devEui = getDevEuis();
            packetUplinkProducer.publish(new PacketModel(devEui, UUID.randomUUID().toString()));
        }

        Thread.sleep(1000000);

    }

    private String getDevEuis() {
        return devEuis.get(random.nextInt(10));
    }



    private static final String ORIGINAL_QUEUE = "packetUplinkDlxTopic.scas-data-collection.dlx";

    private static final String DLQ = ORIGINAL_QUEUE + ".dlq";

    private static final String routingKey = "packetUplinkDlxTopic.scas-data-collection";

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(DLQ)
                    , exchange = @Exchange(RabbitCommonProperties.DEAD_LETTER_EXCHANGE)
                    , key = routingKey
            )
            , concurrency = "1-5"
    )
    public void handleDlq(Message failedMessage) {
        log.info("进入 [上行数据包队列] 的死信队列. 完整消息: {};", failedMessage);

        Object stacktrace = failedMessage.getMessageProperties().getHeaders().get("x-exception-stacktrace");
        log.info("异常栈: {}", stacktrace);
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
        public void handle(PacketModel model) {
            log.info("消费上行数据包消息. model: [{}].", model);
            throw new RuntimeException("Test dlq republish.");
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
