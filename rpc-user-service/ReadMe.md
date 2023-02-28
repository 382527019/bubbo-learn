## 1.RPC 客户端实现

### 1.1客户端

* 引入依赖
~~~
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>order-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
~~~
* 服务消费 Main.java
~~~
public class Main {
 public static void main(String[] args) {
       generalAct();
    }

    static void generalAct() {
        RpcProxyClient rpcProxyClient = new RpcProxyClient();
        //动态代理调用
        IOrderService orderService = rpcProxyClient.clientProxy(IOrderService.class, "localhost", 8080);
        System.out.println("=====拿到执行结果：" + orderService.selectOrderList());
        System.out.println("=====拿到执行结果：" + orderService.orderById("id"));
    }
}
~~~
* 动态代理客户端 Proxy.newProxyInstance动态代理
~~~
public class RpcProxyClient {
    //final 方法内部（基本类型值不能改变|引用类型引用不能改变，值能改变）
    public <T> T clientProxy(final Class<T> interfaceCls, final String host, final int port) {
        System.out.println("=====动态代理执行："+interfaceCls.toString());
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[]{interfaceCls}, new RemoteInvocationHandler(host,port));
    }
}
~~~
* 实现**InvocationHandler** 接口动态代理重写 **invoke方法**。RemoteInvocationHandler.java
~~~
//实现 InvocationHandler动态代理
public class RemoteInvocationHandler implements InvocationHandler {
@Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //建立远程连接
        RpcNetTransport rpcNetTransport = new RpcNetTransport(host, port);
        System.out.println("====客户端建立远程连接");
        
        //传数据{类名、方法名、参数、参数类型}
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());//类名
        rpcRequest.setMethodName(method.getName());//方法名
        rpcRequest.setArgs(args);//参数
        rpcRequest.setTypes(method.getParameterTypes());//参数类型
        
        return rpcNetTransport.send(rpcRequest);//发送

    }
}
~~~
* RpcNetTransport **RPC连接通道**
    * Output写入 =》序列化请求消息
    * input读出 =》反序列化响应消息
~~~
//RPC连接通道
public class RpcNetTransport {
    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Socket newSocket(){
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    //发送
   public Object send(RpcRequest request){
        Socket socket = newSocket();
       System.out.println("=====发送："+socket.toString());
       
       //IO操作
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);//序列化
            System.out.println("=====序列化");
            objectOutputStream.flush();
            
            System.out.println("=====反序列化");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            System.out.println("=====服务端返回结果："+object);
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
//            objectOutputStream.close();
        }
        return socket;
    }
}
~~~

## 2.RPC 客户端实现 注解方式

### 2.1 客户端实现
* 定义注解 Target字段上 
~~~
@Target(ElementType.FIELD)//字段
@Retention(RetentionPolicy.RUNTIME)//运行时用
@Component
public @interface Refernce {
}
~~~
* 实现spring bean后置处理器，容器创建前扫描注解设置代理
* RefernceInvokeProxy.java
* 扫描注解反射调用Proxy.newProxyInstance执行remoteInvocationHandler
~~~
//BeanPostProcessor 后置处理器
@Component
public class RefernceInvokeProxy implements BeanPostProcessor {

    /** 路由处理器*/
    @Autowired
    RemoteInvocationHandler remoteInvocationHandler;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            //扫描注解，对@Refernce注解设置代理 Proxy.newProxyInstance
            if (field.isAnnotationPresent(Refernce.class)) {
               Object proxy= Proxy.newProxyInstance(field.getType().getClassLoader(), new Class<?>[]{field.getType()}, remoteInvocationHandler);
                field.setAccessible(true);
                try {
                    //对加@Refernce注解设置代理，实现inovcationHandler
                    field.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        return bean;
    }
}
~~~
* 实现InvocationHandler接口进行动态代理
  * 建立通信通道RpcNetTransport
  * 组织RpcRequest
  * send()发送数据
~~~
//实现 InvocationHandler动态代理
@Component
public class RemoteInvocationHandler implements InvocationHandler {
    @Value("${me.host}")
    private String host;
    @Value("${me.port}")
    private int port;


    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RemoteInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //建立远程连接
        RpcNetTransport rpcNetTransport = new RpcNetTransport(host, port);
//        Socket socket = rpcNetTransport.newSocket();
        System.out.println("====客户端建立远程连接");
//        传数据{类名、方法名、参数、参数类型}
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());//类名
        rpcRequest.setMethodName(method.getName());//方法名
        rpcRequest.setArgs(args);//参数
        rpcRequest.setTypes(method.getParameterTypes());//参数类型

        return rpcNetTransport.send(rpcRequest);//发送
    }
}
~~~
* RpcNetTransport.java
* RPC连接通道
* 发送数据、响应结果
~~~
//RPC连接通道
public class RpcNetTransport {
    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Socket newSocket(){
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    //发送
   public Object send(RpcRequest request){
        Socket socket = newSocket();
       System.out.println("=====发送："+socket.toString());

       //IO操作
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);//序列化
            System.out.println("=====序列化");
            objectOutputStream.flush();
            System.out.println("=====反序列化");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            System.out.println("=====服务端返回结果："+object);
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
//            objectOutputStream.close();
        }
        return socket;
    }
}
~~~
* 启动springboot
~~~
@SpringBootApplication
public class Main 
~~~
* 配置文件
* 客户端启动8080，服务端启动8888
~~~
server.port=8080
me.host=127.0.0.1
me.port=8888
~~~
* TestController.java
* 访问controller接口得到服务端的回调结果。
~~~
@RestController
public class TestController 
~~~

