package com.sprainkle;

import com.sprainkle.event.model.PacketModel;
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
@ActiveProfiles("maxConcurrency")
@EnableBinding({ScasMaxConcurrencyTest.MessageSink.class, ScasMaxConcurrencyTest.MessageSource.class})
public class ScasMaxConcurrencyTest {

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

    @Test
    public void test() throws InterruptedException {

        // 启动时先睡眠20s, 方便在控制面板查看 初始消费者数量
        Thread.sleep(20000);

        for (int i = 0; i < 50000; i++) {
            String devEui = getDevEuis();
            packetUplinkProducer.publish(new PacketModel(devEui, UUID.randomUUID().toString()));
        }

        Thread.sleep(10000000);

    }

    private String getDevEuis() {
        return devEuis.get(random.nextInt(10));
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
            Thread.sleep(20);
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
