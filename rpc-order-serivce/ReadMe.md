## 1.RPC 服务端
### 1.2 服务端
* 请求数据类 RpcRequest .java
~~~
public class BootStrap {
    public static void main(String[] args) {
        generalAct();
    }

    static void generalAct() {
        //待实现的接口
        OrderServiceImpl orderService = new OrderServiceImpl();
        
        //发布服务
        RpcProxyServer rpcProxyServer = new RpcProxyServer();
        rpcProxyServer.publisher(orderService, 8080);
    }

}
~~~
* RpcProxyServer 服务代理
    * ServerSocket开启监听
    * while serverSocket.accept();并发执行解决阻塞
    * 线程池执行 ProcessorHandler
~~~
public class RpcProxyServer {
    //线程池
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    public void publisher(Object service, int port) {
        System.out.println("=====开始监听"+service.toString()+"=====端口："+port);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();//监听请求
                System.out.println("=====收到请求"+socket);
                // 执行 ProcessorHandler(socket,service)
                executorService.execute(new ProcessorHandler(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
~~~
* ProcessorHandler 编写线程体
    * 拿到客户端的请求 RpcRequest
    * 反序列化 后 invoke(rpcRequest)反射调用
    * method.invoke(service, rpcRequest.getArgs())
~~~
public class ProcessorHandler implements Runnable {
    private Socket socket;

    private Object service;

    public ProcessorHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    //线程体
    @Override
    public void run() {
        //IO操作
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            System.out.println("=====拿到客户端数据，反序列化");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest =(RpcRequest) objectInputStream.readObject();//反序列化

            //反射调用
            Object invoke = invoke(rpcRequest);
            System.out.println("=====服务端收到客户端请求处理后："+invoke);

            System.out.println("=====发送给客户端数据，序列化");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            
            //反射调用结果写入客户端
            objectOutputStream.writeObject(invoke);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }finally {
//            objectInputStream.close();
        }

    }
    
//动态代理 反射执行
    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(rpcRequest.getClassName());
        Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getTypes());
        System.out.println("====动态代理 反射执行====类名："+clazz.getName()+"====方法："+method.getName()+"=====参数"+rpcRequest.getArgs().toString());
        return method.invoke(service, rpcRequest.getArgs());
    }
}
~~~




----

## 2.注解 服务端
### 2.2 服务端
* 注解类 RemoteService.java
~~~
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)//运行时用
@Component
public @interface RemoteService {
}
~~~
* 存储bean和method类 BeanMethod.java
~~~
public class BeanMethod 
~~~

* 存储发布服务实例的map
* processor(RpcRequest rpcRequest)拿到请求数据，根据key拿到服务实例反射调用执行服务方法。
~~~
public class Mediator 
~~~
* Bean后置处理器，容器创建后扫描自定义注解@RemoteService。
* 拿到标注的类上的方法放入存储发布服务实例的map。
* InitialMerdiator.java
~~~
//BeanPostProcessor
// 称Bean后置处理器，它是Spring中定义的接口，
// 在Spring容器的创建过程中（具体为Bean初始化前后）会回调BeanPostProcessor中定义的两个方法
@Component
public class InitialMerdiator implements BeanPostProcessor 
~~~
* 实现spring的 ApplicationListener
* spring容器启动完成后会监听到一个ContextRedfreshdEvent事件
* 拿到请求交给线程池处理AnnotationProcessorHandler（socket）
* SocketServerInitial.java
~~~
//spring容器启动完成后会监听到一个ContextRedfreshdEvent事件
@Component
public class SocketServerInitial implements ApplicationListener<ContextRefreshedEvent> 
~~~
AnnotationProcessorHandler.java
~~~
public class AnnotationProcessorHandler implements Runnable
~~~
* 启动容器，定义扫描包路径 BootStrap.java
~~~
@Configurable
@ComponentScan("com.example")
public class BootStrap 
~~~
