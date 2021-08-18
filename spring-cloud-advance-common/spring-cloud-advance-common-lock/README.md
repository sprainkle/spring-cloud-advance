# 分布式锁的使用

## 配置文件

### 其它相关配置
目前分布式锁有两种实现, 分别是基于``redis``和``zookeeper``。默认使用redis实现，如使用zookeeper实现，请添加zookeeper依赖。

```xml
<!--锁处理 redis-->
<dependency>
  <groupId>com.sprainkle</groupId>
  <artifactId>spring-cloud-advance-common-lock</artifactId>
  <version>${sc-advance-common.version}</version>
</dependency>

<!--锁处理 zookeeper-->
<dependency>
  <groupId>com.techtcm</groupId>
  <artifactId>tcm-common-lock</artifactId>
  <version>${tcm-common.version}</version>
        <exclusions>
            <exclusion>
                <groupId>org.redisson</groupId>
                <artifactId>redisson</artifactId>
            </exclusion>
        </exclusions>
</dependency>
<dependency>
  <groupId>org.apache.curator</groupId>
  <artifactId>curator-recipes</artifactId>
</dependency>
```

#### 公共配置
```
sca-common:
  distributed:
    lock:
      namespace: ${spring.application.name} # 分布式锁名的统一前缀（可以理解为命名空间），用来隔开不同模块的锁。缺省值：${spring.application.name}，即该服务的服务名
      impl: # redis/zoo, 分别对应2种实现方式。 缺省值为：redis
      lockTimoutMs: # 锁全局过期时间，单位：毫秒. 缺省值：5000
      waitTimeoutMs: # 尝试锁全局等待时间，单位：毫秒. 缺省值：10000
```

#### Redis配置
默认使用redis实现, redis实现的分布式锁功能是基于客户端``redisson``, ``redisson``的使用见[开发文档](https://github.com/redisson/redisson/wiki).
所以需要向spring注入``RedissonClient``实例, 有3种注入方式:

* 手动注入
* 配置文件注入
* 使用默认配置注入

>注: 这3种方式的优先级: 手动注入 > 配置文件注入 > 使用默认配置注入

##### 手动注入
这里示范单Redis节点的配置. 

###### 配置文件注入

```
redisson:
  singleServerConfig:
    address: redis://127.0.0.1:6379
    password: xxx # 如果密码为空, 这一行要注释掉
```
更多配置见: [单Redis节点模式](https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95#26-%E5%8D%95redis%E8%8A%82%E7%82%B9%E6%A8%A1%E5%BC%8F)
###### 配置类
```
@Configuration
@ConfigurationProperties(prefix = "redisson")
public class RedissonSingleServerConfig extends Config {

    @ConfigurationProperties(prefix = "redisson.singleServerConfig")
    public SingleServerConfig getSingleServerConfig() {
        return super.useSingleServer();
    }
}
```
```
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson(RedissonSingleServerConfig config) throws IOException {
        return Redisson.create(config);
    }
}
```

##### 配置文件
```yaml
sca-common:
  distributed:
    lock:
      impl: redis
      redis:
        host: 127.0.0.1
        password: xxx
        port: 6379
```
```yaml
# 如果是哨兵模式, 可使用下面配置
sca-common:
  distributed:
    lock:
      impl: redis
      redis:
        sentinel:
          master: ${env.redis.sentinel.master}
          nodes: ${env.redis.sentinel.nodes}
```


##### 使用默认配置注入
使用默认配置, 即除了开启分布式锁的配置外, 不添加其它配置, 则会默认创建一个默认配置的``RedissonClient``实例, Redis地址为``127.0.0.1``

### Zookeeper配置
分布式锁的另一种实现方式是基于``Zookeeper`实现的, 所以需要加一些简单配置.
```
sca-common:
  distributed:
    lock:
      enabled: true
      namespace: ${spring.application.name} 
      impl: 'zoo'
      zoo:
        connectString: '127.0.0.1:2181' # Zookeeper服务器地址, 当有多个时, 用','隔开. 默认值: 127.0.0.1:2181
        sessionTimeoutMs: 5000 # Zookeeper连接的会话过期时间, 单位: 毫秒. 可用于配置锁全局的过期时间. 默认值: 5000
        connectionTimeoutMs: 60000 # Zookeeper连接超时时间, 单位: 毫秒. 默认值: 60000
```

## 注解``@DistributedLock``的使用

### 使用 SpEL
因为 `SpEL` 的解析器使用了 模板解析, 模板为: `#{}`, 所以使用 `SpEL` 时需要用 `#{}` 包裹表达式. 示例如下:

#### 直接使用方法的参数
```java
@Service
public class XxxService {

    @DistributedLock(lockName = "#{#request.id}")
    public XxxUpdateRespData update(XxxUpdateRequest request) {
        // do something
        return result;
    }

    @Data
    public class XxxUpdateRequest {
        private Long id;
    }
}

```

#### 使用特定的方法获取锁名
```java
@Service
public class XxxService {

    @DistributedLock(lockName = "#{#root.target.getXxxLockName()}")
    public XxxUpdateRespData update(XxxUpdateRequest request) {
        // do something
        return result;
    }

    
    public String getXxxLockName(XxxUpdateRequest request) {
        return request.getId();
    }
}
```
> 注: 上面的 `getXxxLockName()` 方法必须使用 `public` 修饰符.

### 使用分布式锁管理器
如果锁名的生成比较繁琐, 也可以定义一个 `DistributedLockManager`, 专门管理分布式锁, 示例如下:

注解``@DistributedLock``需要加在``Spring``能够扫描到的地方, 也可以创建一个专门的管理器``DistributedLockManager``来专门管理分布式锁。
```java
@Service
public class DistributedLockManager {

    @Transactional
    @DistributedLock(lockName = "xxx")
    public Object method_1() {
        // do something
        return result;
    }

}
```

上面的方法``method_1``会在获得锁后执行，而其中的``do something``就是需要执行的业务逻辑。但是这部分业务逻辑一般是另一个大业务逻辑的子业务，如果可以将这一子业务逻辑放到与大业务逻辑同一个``Service``中就更好了。所以就有了另一种基于lambda表达式的写法。

```java
@Service
public class DistributedLockManager {

    @Transactional
    @DistributedLock(lockName = "xxx")
    public Object method_2(Supplier<Object> supplier) {
        return supplier.get();
    }

}
```

然后在Service层使用：

```java
@Service
public class XxxService {
    private DistributedLockManager distributedLockManager;
    
    // 这里尽量不要加 @Transactional 注解
    // @Transactional
    public void method_3() {
        // ...
        distributedLockManager.method_2( () -> method_1());
        // ...
    }
    
    private Object method_1() {
        // do something
        return result;
    }
}
```

可以看到，通过使用lambda表达式，可以使代码变得更优雅。当然，如果还有其它需求，比如需要消费某个对象，则可以使用``Consumer<Xxx>``; 如果需要消费某个对象并返回另一个对象，则可以使用``Function<Xxx1, Xxx2>``; 甚至只是需要执行某段代码，不用消费也不用返回，则可以直接使用``Action``接口，因为jdk1.8没有提供类似的功能性接口，该接口在``api``包额外有定义。

> 注: 在 Service 层使用 DistributedLockManager, 如果是需要配合 @Transactional 注解, 该注解应该写在 DistributedLockManager 中, 
> 与 @DistributedLock 写在一起, 而不能写在 Service 层的方法上面.
>
> 因为在竞争锁的时候, 已经开启了事务, 如果数据库隔离级别是 **可重复读**(innodb 的默认隔离级别), 那么有可能出现这样的情况: 
> 第一个获得锁的线程处理完成逻辑后, 第二个线程获得锁, 但第一个线程提交的东西对第二个线程所在的事务是不可见的, 从而可能导致不及预期的情况, 即锁不生效


