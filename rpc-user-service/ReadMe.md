## 1.RPC 客户端实现

### 1.1客户端
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
* 定义注解 Target字段上 Refernce.java
~~~
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)//运行时用
@Component
public @interface Refernce {
}
~~~
* 实现spring bean后置处理器，容器创建前扫描注解设置代理
* RefernceInvokeProxy.java
* 扫描到指定注解代理执行remoteInvocationHandler
~~~
@Component
public class RefernceInvokeProxy implements BeanPostProcessor 
~~~
* 实现InvocationHandler接口进行动态代理
* RemoteInvocationHandler.java
* send（）发送数据
~~~
//实现 InvocationHandler动态代理
@Component
public class RemoteInvocationHandler implements InvocationHandler 
~~~
* RpcNetTransport.java
* RPC连接通道
* 发送数据、响应结果
~~~
//RPC连接通道
public class RpcNetTransport
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
* 访问8080controller接口得到服务端的回调结果。
~~~
@RestController
public class TestController 
~~~

