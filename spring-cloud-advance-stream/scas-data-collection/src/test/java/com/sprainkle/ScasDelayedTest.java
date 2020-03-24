package com.sprainkle;

import cn.hutool.core.util.RandomUtil;
import com.sprainkle.event.model.DelayModel;
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
@EnableBinding({ScasDelayedTest.MessageSink.class, ScasDelayedTest.MessageSource.class})
public class ScasDelayedTest {

    @Autowired
    private DelayedQueueProducer delayedQueueProducer;

    /**
     *
     */
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            long delay = RandomUtil.randomLong(3, 8) * 1000;
            delayedQueueProducer.publish(new DelayModel(delay));
        }

        Thread.sleep(1000000);
    }

    @Component
    public static class DelayedQueueProducer {

        @Autowired
        private MessageSource messageSource;

        public void publish(DelayModel model) {
            long delay = model.getDelay();
            Message<DelayModel> message = MessageBuilder.withPayload(model).setHeader("x-delay", delay).build();
            messageSource.delayedQueueOutput().send(message);
            log.info("发布延迟队列消息: {}", model);
        }

    }

    @Component
    public static class DelayedQueueHandler {

        @StreamListener("delayedQueueInput")
        public void handle(DelayModel model) throws InterruptedException {
            log.info("消费延迟队列的消息. model: [{}].", model);
        }

    }

    public interface MessageSink {

        @Input("delayedQueueInput")
        SubscribableChannel delayedQueueInput();

    }

    public interface MessageSource {

        @Output("delayedQueueOutput")
        MessageChannel delayedQueueOutput();

    }

}
