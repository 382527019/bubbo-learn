package top.hygyxx.demo.actSerializable.myseralizer;

import com.google.protobuf.InvalidProtocolBufferException;
import top.hygyxx.demo.actSerializable.User;
import top.hygyxx.demo.actSerializable.myseralizer.protobufSeralizer.UserOuterClass;


/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/24
 * Time:9:06
 */
public class SeralizerMain {
    public static void main(String[] args) {
//        javaSeralizer();
//        hessianSeralizer();
        protobufSeralizer();
    }

    public static void protobufSeralizer(){
        UserOuterClass.User user = UserOuterClass.User.newBuilder().setName("mix").setAge(18).build();
        byte[] bytes = user.toByteArray();//序列化
        System.out.println("=====压缩后长度："+bytes.length);
        for (byte by :bytes){
            System.out.print(by+" ");
        }
        System.out.println();
        UserOuterClass.User user1 = null;
        try {
            user1 = UserOuterClass.User.parseFrom(bytes);//反序列化
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.println(user1);
    }

    public static void javaSeralizer(){
        ISerializer javaSerializer = new JAVASerializerImpl();
        User user = new User();
        user.setName("name");
        user.setAge(10);
        byte[] bytes = javaSerializer.serializer(user);
        for (byte bt : bytes) {
            System.out.print(bt + " ");
        }//-84 -19 0 5 115 114 0 36 11
        System.out.println();
        user = javaSerializer.deserializer(bytes);
        System.out.println(user);//User{name='我的名字'}
    }

    public static void hessianSeralizer(){
        ISerializer javaSerializer = new HessianSerializer();
        User user = new User();
        user.setName("name");
        user.setAge(10);
        byte[] bytes = javaSerializer.serializer(user);
        System.out.println("=====长度"+bytes.length);
        for (byte bt : bytes) {
            System.out.print(bt + " ");
        }//-84 -19 0 5 115 114 0 36 11
        System.out.println();
        user = javaSerializer.deserializer(bytes);
        System.out.println(user);//User{name='我的名字'}
    }
}
