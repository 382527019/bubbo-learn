
# 一、网络通信Socket
* 服务端 ServerSocket
```
public class ServerSocketDemo {}
```

* 客户端Socket
```
public class SocketClientDemo {}
```
## 1.1 OSI模型


| OSI     | 负载均衡依据  |各层解释  |
|:--------:| -------------:|-------------:|
应用层  |URL                         |为应用程序提供服务
表示层|                                  |数据格式转换、数据加密
会话层|                                  |建立、管理和维护会话
传输层|       TCP/IP :端口号   |建立、管理和维护到端的连接
网络层|       IP                        |IP选址及路由选择
数据链路层|    MAP               |提供介质访问和链路管理
物理层|        |物理层

## 1.2 三挥四握
# 二、阻塞和非阻塞机制
## 2.1 阻塞IO(BIO)
* 连接阻塞
* IO阻塞
    * IO阻塞优化（使用异步线程）
    * serverSocketDemo.java
```
public class serverSocketDemo{}
```
* SocketThread.java
```
//1.实现Runnable接口。
//2.IO操作写在线程体
//3.把Socket传进来
public class SocketThread implements Runnable{}
```
## 2.2 非阻塞IO(NIO)（New IO）非阻塞模型（No-Blocked IO）
* 连接阻塞 =》连接非阻塞
* IO阻塞 =》IO非阻塞

NewIOServer.java 非阻塞IO服务端
~~~
public class NewIOServer {}
//非阻塞IO ServerSocketChannel //Channel Buffer Selector
~~~
NewIOSocketClientDemo.java 非阻塞IO客户端
~~~
public class NewIOSocketClientDemo {}
~~~
### 2.2.1多路复用
* 多路复用器 Selector
  NewIoSelectorServer.java
~~~
public class NewIoSelectorServer {}
~~~
# 二、序列化与反序列化
## 2.1 序列化与反序列化
* 序列化 obj ->stream
* 反序列化 stream -> obj
* 选型
    * 压缩比
    * 夸语言

## 2.2Java实现
* implements Serializable
* ObjectInputStream ,对象输入流
* ObjectOutputStream， 对象输出流
* private static final long serialVersionUID = -214124124121221L;
* WriteObject/ReadObject
* transient

JAVASerializerImpl.java  |JAVA序列化
* ObijcetXXXStream
~~~
public class JAVASerializerImpl implements ISerializer {}
~~~
* SeralizerMain.java
~~~
public class SeralizerMain {}
~~~
User.java
~~~
//实现Serializable接口=》序列化对象
public class User implements Serializable {}
~~~
输出
~~~
=====反射调用writeObject
====序列化
-84 -19 0 5 115 114 0 36 116 111 112 46 104 121 103 121 120 120 46 100 101 109 111 46 97 99 116 83 101 114 105 97 108 105 122 97 98 108 101 46 85 115 101 114 -1 -1 -20 -122 -120 -61 -78 -85 3 0 1 76 0 4 110 97 109 101 116 0 18 76 106 97 118 97 47 108 97 110 103 47 83 116 114 105 110 103 59 120 112 116 0 4 110 97 109 101 119 4 0 0 0 10 120 
====反序列化
=====反射调用readObject
User{name='name', age='10'}
~~~
## 2.3 分布式架构下的序列化技术
* 序列化数据大小、影响传输效率。
* 跨语言-》json 、xml
### 2.3.1 服务与服务之间的通信
* hassian
* msgpack
* protobuf（压缩率高）
* dubbo-》hassian2
* kyro
* avro
* jute（zookeeper）
* xml
* json跨语言、序列化效率高
###  2.3.2 JSON
* Fastjson
* Jackson(spring mvc) 性能高
* Gson
### 2.3.3 Hessian
* 性能、易用性比默认好
* 序列化报文规则
* dubbo hessian

HessianSerializer.java |Hessian序列化
~~~
public class HessianSerializer implements ISerializer{}
~~~

SeralizerMain .java
~~~
public class SeralizerMain {}
~~~
输出
~~~
=====Hessian序列化
=====长度55
77 116 0 36 116 111 112 46 104 121 103 121 120 120 46 100 101 109 111 46 97 99 116 83 101 114 105 97 108 105 122 97 98 108 101 46 85 115 101 114 83 0 4 110 97 109 101 83 0 4 110 97 109 101 122 
=====反序列化
User{name='name', age='0'}
~~~

### 2.3.4 Protobuf
* 数据压缩（varint 算法）
    * 编码、解码 =》使用位运算
    * 压缩数据小（varint , zigzag（负数）算法）
* TAG的计算
* T-L-V（Tag - Length - Value)存储
* 使用
    * 使用工具生成类 ![在这里插入图片描述](https://img-blog.csdnimg.cn/194b6dc0c7204666b93e4c561456d776.png)
~~~
//protobuf生成类
public final class UserOuterClass {
~~~

~~~
public class SeralizerMain {}
~~~
输出
~~~
=====压缩后长度：7
10 3 109 105 120 16 18 
name: "mix"
age: 18
~~~
### 2.4.5 BitMap(布隆过滤器)
---