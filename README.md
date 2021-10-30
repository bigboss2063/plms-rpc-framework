# plms-rpc-framework
> 一个简单的、基于Netty+Kyro+Zookeeper的RPC框架，目前实现了一些最简单的功能。

## 项目目前情况和待优化的点

- [x] 使用Netty进行网络传输；
- [x] 使用Kyro进行序列化；
- [x] 使用Zookeeper存储服务的地址信息；
- [x] 使用心跳检测机制：保证客户端和服务端的连接不被断掉，避免重连；
- [ ] 负载均衡：调用服务的时候从众多服务器中根据负载均衡算法选出一个服务地址；
    - [x] 随机权重算法 RandomLoadBalance；
    - [x] 加权轮询算法 LeastActiveLoadBalance；
    - [ ] 最少活跃数算法 RoundRobinLoadBalance；
    - [ ] 一致性哈希算法 ConsistentHashLoadBalance；
- [x] 处理一个类有多个类实现的情况：在服务发布的时候增加一个group字段，服务器根据客户端请求消息里的group字段来选择调用哪个实现类；
- [ ] 增加服务版本号：为应对接口的不向后兼容升级；
- [ ] 集成Spring通过注解注册消费服务；
- [x] 模仿Dubbo使用SPI机制；
- [ ] 增加序列化的方式；
- [ ] 增加Redis作为注册中心；
- [ ] 设置gzip压缩；

## 使用

``` java
// 客户端
public class RpcClient {
    public static void main(String[] args) throws IOException {
        NettyRpcClient nettyRpcClient = new NettyRpcClient();
        RpcConfig rpcConfig = RpcConfig.builder()
                .group("Test2") // 指明调用实现类的分组
                .version("Version1") // 指明版本
                .build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRpcClient, rpcConfig);
        TestService proxy = rpcClientProxy.getProxy(TestService.class); // 获取服务接口类的代理类实例
        System.out.println(proxy.hello());
    }
}
```

```java
// 服务端
@Slf4j
public class RpcServer {
    @SneakyThrows
    public static void main(String[] args) {
        NettyRpcSever nettyRpcSever = new NettyRpcSever(9999); // 配置服务端口号
        Class<?> TestServiceImpl1target = Class.forName("com.plms.rpc.TestServiceImpl1");
        Object testServiceImpl1 = TestServiceImpl1target.newInstance();
        Class<?> TestServiceImpl2target = Class.forName("com.plms.rpc.TestServiceImpl2");
        Object testServiceImpl2 = TestServiceImpl2target.newInstance();
        Integer weight = 3;
        /*
          分别对同一个接口注册两个实现类
        */
        RpcConfig rpcConfig1 = RpcConfig.builder()
                .service(testServiceImpl1)
                .weight(weight) // 服务权值
                .group("Test1")
                .version("Version1")
                .build();
        RpcConfig rpcConfig2 = RpcConfig.builder()
                .service(testServiceImpl2)
                .weight(weight) // 服务权值
                .group("Test2")
                .version("Version1")
                .build();
        nettyRpcSever.registerService(rpcConfig1); // 注册服务
        nettyRpcSever.registerService(rpcConfig2); // 注册服务
        nettyRpcSever.start(); // 启动服务
    }
}
```

```properties
zookeeper.address=zookeeper服务地址 #resources/config目录下的配置文件
```

