# plms-rpc-framework
> 一个简单的、基于Netty+Kyro+Zookeeper的RPC框架，目前实现了一些最简单的功能。

## 项目已实现的功能：

- [x] 使用Netty进行网络传输；
- [x] 使用Kyro进行序列化；
- [x] 使用Zookeeper存储服务的地址信息；
- [x] 使用心跳检测机制：保证客户端和服务端的连接不被断掉，避免重连；
- [x] 负载均衡：调用服务的时候从众多服务器中根据负载均衡算法选出一个服务地址；
    - [x] 随机权重算法 RandomLoadBalance；
    - [x] 加权轮询算法 LeastActiveLoadBalance；
- [x] 处理一个类有多个类实现的情况：在服务发布的时候增加一个group字段，服务器根据客户端请求消息里的group字段来选择调用哪个实现类；
- [x] 增加服务版本号：为应对接口的不向后兼容升级；
- [x] 集成Spring通过注解注册消费服务；
- [x] 模仿Dubbo使用SPI机制；
- [x] 设置gzip压缩；

## 还能继续拓展的地方：

1. 由于实现了SPI机制，那么序列化、注册中心、负载均衡还有压缩的实现都可以更好的通过配置文件的方式来拓展，避免硬编码。可以增加Redis作为注册中心；增加ProtoStuff来实现注册化；还可以实现Dubbo中剩下的两种负载均衡方式：最少活跃调用数均衡算法、一致性Hash均衡算法；
2. 可以添加一个像Dubbo Admin一样的服务监控中心；
3. 优化代码质量。

## 使用

``` java
@RpcScan(basePackage = {"com.plms.rpc"})
public class RpcClient {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcClient.class);
        TestController testController = (TestController) applicationContext.getBean("testController");
        testController.test();
    }
}
```

```java
@Slf4j
@RpcScan(basePackage = {"com.plms.rpc"})
public class RpcServer {
    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcServer.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        nettyRpcServer.start(); // 启动服务
    }
}
```

```properties
zookeeper.address=zookeeper服务地址 #resources/config目录下的配置文件
```

