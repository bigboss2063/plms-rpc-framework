# plms-rpc-framework
> 一个简单的、基于Netty+Kyro+Zookeeper的RPC框架，目前实现了一些最简单的功能。

## 项目目前情况和待优化的点

- [x] 使用Netty进行网络传输；
- [x] 使用Kyro进行序列化；
- [x] 使用Zookeeper存储服务的地址信息；
- [x] 使用心跳检测机制；
- [ ] 客户端远程调用的时候根据相应的策略进行负载均衡；
    - [x] 随机权重 RandomLoadBalance；
    - [x] 加权轮询算法 LeastActiveLoadBalance；
    - [ ] 最少活跃数算法 RoundRobinLoadBalance；
    - [ ] 一致性哈希算法 ConsistentHashLoadBalance；
- [ ] 处理一个类有多个类实现的情况；
- [ ] 集成plms-spring通过注解注册消费服务；
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
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRpcClient);
        TestService proxy = rpcClientProxy.getProxy(TestService.class); // 获取服务接口类的代理类实例
        proxy.hello(); // 远程调用服务
    }
}
```

```java
// 服务端
public class RpcServer {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        NettyRpcSever nettyRpcSever = new NettyRpcSever(9999); // 配置服务端口号
        Class<?> target = Class.forName("com.plms.rpc.TestServiceImpl");
        Object o = target.newInstance();
        Integer weight = 3;
        RpcConfig rpcConfig = RpcConfig.builder()
                .service(o)
                .weight(weight) // 服务权值
                .build();
        nettyRpcSever.registerService(rpcConfig); // 注册服务
        nettyRpcSever.start(); // 启动服务
    }
}
```

```properties
zookeeper.address=zookeeper服务地址 #resources/config目录下的配置文件
```

