package com.sprainkle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2020/6/25
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dynamic")
@EnableBinding({ScasDynamicRoutingTest.MessageSink.class, ScasDynamicRoutingTest.MessageSource.class})
public class ScasDynamicRoutingTest {

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

        for (int i = 0; i < 5; i++) {
            String devEui = getDevEuis();
            String type = "waterLevel";
            packetUplinkProducer.publish(new PacketModel(devEui, type));
        }

        for (int i = 0; i < 5; i++) {
            String devEui = getDevEuis();
            String type = "temperature";
            packetUplinkProducer.publish(new PacketModel(devEui, type));
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
            Message<PacketModel> message = MessageBuilder.withPayload(model).setHeader("type", model.getType()).build();
            messageSource.packetUplinkOutput().send(message);
        }

    }

    @Component
    public static class PacketUplinkHandler {

        @StreamListener("waterLevelInput")
        public void handleWaterLevelPacket(PacketModel model) throws InterruptedException {
            log.info("消费【水位监测器】数据包消息. model: [{}].", model);
        }

        @StreamListener("temperatureInput")
        public void handleTemperaturePacket(PacketModel model) throws InterruptedException {
            log.info("消费【温度监测器】数据包消息. model: [{}].", model);
        }

    }

    public interface MessageSink {

        @Input("waterLevelInput")
        SubscribableChannel waterLevelInput();

        @Input("temperatureInput")
        SubscribableChannel temperatureInput();

    }

    public interface MessageSource {

        @Output("packetUplinkOutput")
        MessageChannel packetUplinkOutput();

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class PacketModel {
        /**
         * 设备 eui
         */
        private String devEui;

        /**
         * 设备类型
         */
        private String type;
    }

}
