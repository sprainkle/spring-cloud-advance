package com.sprainkle;

import com.sprainkle.event.model.PacketModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
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
@EnableBinding({ScasOnlyProducerTest.MessageSource.class})
@ActiveProfiles("onlyProducer")
public class ScasOnlyProducerTest {

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
    public void test1() throws InterruptedException {

        while (true) {
            int msgCount = 10;
            for (int i = 0; i < msgCount; i++) {
                String devEui = getDevEuis();
                packetUplinkProducer.publish(new PacketModel(devEui, UUID.randomUUID().toString()));
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test2() throws InterruptedException {

        while (true) {
            int msgCount = 3;
            for (int i = 0; i < msgCount; i++) {
                String devEui = getDevEuis();
                packetUplinkProducer.publish(new PacketModel(devEui, UUID.randomUUID().toString()));
            }
            Thread.sleep(1000);
        }
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

    public interface MessageSource {

        @Output("packetUplinkOutput")
        MessageChannel packetUplinkOutput();

    }

}
