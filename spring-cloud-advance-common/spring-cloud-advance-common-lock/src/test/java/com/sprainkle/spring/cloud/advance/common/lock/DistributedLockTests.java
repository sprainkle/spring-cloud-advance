package com.sprainkle.spring.cloud.advance.common.lock;

import com.sprainkle.spring.cloud.advance.common.lock.api.Action;
import com.sprainkle.spring.cloud.advance.common.lock.entity.TestItem;
import com.sprainkle.spring.cloud.advance.common.lock.service.TestItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author sprainkle
 * @date 2021/8/14
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DistributedLockTestApplication.class)
public class DistributedLockTests {

    private static int count = 10;

    @Autowired
    private TestItemService testItemService;

    @Test
    public void testPlainLockName() {
        Consumer<TestItem> consumer = testItem -> {
            Integer stock = testItemService.testPlainLockName(testItem);
            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testSpel() {
        Consumer<TestItem> consumer = testItem -> {
            Integer stock = testItemService.testSpel(testItem);
            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testCheckBefore() {
        Consumer<TestItem> consumer = testItem -> {

            Integer stock = -1;

            try {
                stock = testItemService.testCheckBefore(testItem);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + ": 系统繁忙");
                return;
            }

            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testTryLock() {
        Consumer<TestItem> consumer = testItem -> {

            Integer stock = -1;

            try {
                stock = testItemService.testTryLock(testItem);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + ": " + e.getMessage());
                return;
            }

            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testFairLock() {
        Consumer<TestItem> consumer = testItem -> {

            Integer stock = -1;

            try {
                stock = testItemService.testFairLock(testItem);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + ": " + e.getMessage());
                return;
            }

            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testLeaseTime() {
        Consumer<TestItem> consumer = testItem -> {

            Integer stock = testItemService.testLeaseTime(testItem);

            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testLeaseTimeOversold() {
        Consumer<TestItem> consumer = testItem -> {

            Integer stock = testItemService.testLeaseTimeOversold(testItem);

            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }

    @Test
    public void testLeaseTimeWithTransactional() {
        Consumer<TestItem> consumer = testItem -> {

            Integer stock = testItemService.testLeaseTimeWithTransactional(testItem);

            if (stock >= 0) {
                System.out.println(Thread.currentThread().getName() + ": reset stock = " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + ": sold out.");
            }
        };

        commonTest(consumer);
    }


    private void commonTest(Consumer<TestItem> consumer)  {
        try {
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(count);

            TestItem item = testItemService.initStock(1L, 8);

            for (int i = 0; i < count; ++i) {

                Action action = () -> consumer.accept(item);

                new Thread(new Worker(startSignal, doneSignal, action)).start();
            }

            startSignal.countDown(); // let all threads proceed
            doneSignal.await();
            System.out.println("All processors done. Shutdown connection");
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static class Worker implements Runnable {

        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private final Action action;

        public Worker(CountDownLatch startSignal, CountDownLatch doneSignal, Action action) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.action = action;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " start");
                // 阻塞, 直到接收到启动信号. 保证所有线程的起跑线是一样的, 即都是同时启动
                startSignal.await();
                // 具体逻辑
                action.execute();
                // 发送 已完成 信号
                doneSignal.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
