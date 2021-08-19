package com.sprainkle.spring.cloud.advance.common.lock.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprainkle.spring.cloud.advance.common.core.constant.enums.CommonResponseEnum;
import com.sprainkle.spring.cloud.advance.common.lock.annotation.DistributedLock;
import com.sprainkle.spring.cloud.advance.common.lock.entity.TestItem;
import com.sprainkle.spring.cloud.advance.common.lock.mapper.TestItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sprainkle
 * @date 2021/8/14
 */
@Slf4j
@Service
public class TestItemService extends ServiceImpl<TestItemMapper, TestItem> {

    private static final AtomicInteger i = new AtomicInteger(10);


    @Transactional(rollbackFor = Throwable.class)
    public TestItem initStock(Long id, Integer stock) {
        TestItem item = this.getById(id);

        if (item == null) {
            item = new TestItem(1, "牛奶");
        }

        item.setStock(stock);

        this.saveOrUpdate(item);

        return this.getById(id);
    }

    /**
     * 锁名为固定的字符串
     *
     * @param testItem
     * @return
     */
    @DistributedLock(lockName = "1", lockNamePre = "item")
    public Integer testPlainLockName(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item"
    )
    public Integer testSpel(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            checkBefore = "#{#root.target.check(#testItem)}"
    )
    public Integer testCheckBefore(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    public void check(TestItem testItem) {
        int randomInt = RandomUtil.randomInt(100, 10000);
        if (randomInt % 3 == 0) {
            System.out.println(String.format("current thread: %s, randomInt: %d", getCurrentThreadName(), randomInt));
            CommonResponseEnum.SERVER_BUSY.assertFail();
        }
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            // 默认为 true
            tryLock = true,
            // 尝试获取锁, 若3秒内无法获取, 直接返回
            waitTime = 3000
    )
    public Integer testTryLock(TestItem testItem) {
        int ci = i.decrementAndGet();
        if (ci == 5) {
            System.out.println("获得锁, 模拟阻塞. current thread: " + getCurrentThreadName());
            sleep(4000L);
        }

        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            checkBefore = "#{#root.target.blockRandomTime()}",
            fairLock = true
    )
    public Integer testFairLock(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        sleep(200L);

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    public void blockRandomTime() {
        int randomInt = RandomUtil.randomInt(50, 150);
        sleep(randomInt);
        log.info("阻塞完成.");
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            leaseTime = 2000
    )
    public Integer testLeaseTime(TestItem testItem) {
        int ci = TestItemService.i.getAndDecrement();
        if (ci == 10) {
            sleep(5000L);
            log.info("模拟阻塞完成");
        } else {
            sleep(300L);
        }

        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);

        } else {
            stock = -1;
        }

        return stock;
    }

    /**
     * 超卖
     *
     * @param testItem
     * @return
     */
    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            leaseTime = 2000
    )
    public Integer testLeaseTimeOversold(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        int ci = TestItemService.i.getAndDecrement();
        if (ci == 10) {
            sleep(5000L);
            log.info("模拟阻塞完成");
        } else {
            sleep(300L);
        }

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            leaseTime = 2000
    )
    @Transactional(rollbackFor = Throwable.class)
    public Integer testLeaseTimeWithTransactional(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());

        int ci = TestItemService.i.getAndDecrement();
        if (ci == 10) {
            sleep(5000L);
            log.info("模拟阻塞完成");
        } else {
            sleep(300L);
        }

        Integer stock = item.getStock();

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            checkBefore = "#{#root.target.checkbefore(#testItem)}"
    )
    @Transactional(rollbackFor = Throwable.class)
    public Integer testInoperative(TestItem testItem) {
        sleep(100L);

        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();
        System.out.println(String.format("current thread: %s, current stock: %d", getCurrentThreadName(), item.getStock()));

        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }

    public void checkbefore(TestItem testItem) {
        TestItem item = this.getById(testItem.getId());
        System.out.println(String.format("current thread: %s, check before. stock: %d", getCurrentThreadName(), item.getStock()));
    }

    @DistributedLock(
            lockName = "#{#testItem.id}",
            lockNamePre = "item",
            checkBefore = "#{#root.target.checkbefore(#testItem)}"
    )
    @Transactional(rollbackFor = Throwable.class)
    public Integer testRollbackWhenLostTheLock(TestItem testItem) {

        TestItem item = this.getById(testItem.getId());
        Integer stock = item.getStock();

        int ci = i.getAndDecrement();
        if (ci == 10) {
            System.out.println(String.format("current thread: %s, got the lock first, now sleep a few seconds.", getCurrentThreadName()));
            sleep(6000L);
        }

        System.out.println(String.format("current thread: %s, current stock: %d", getCurrentThreadName(), item.getStock()));
        if (stock > 0) {
            stock = stock - 1;
            item.setStock(stock);
            this.saveOrUpdate(item);
        } else {
            stock = -1;
        }

        return stock;
    }


    private String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
