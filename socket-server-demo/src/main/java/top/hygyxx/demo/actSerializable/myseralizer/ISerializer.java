package top.hygyxx.demo.actSerializable.myseralizer;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/24
 * Time:8:52
 */
public interface ISerializer {

    <T>byte[] serializer(T Object);

    <T> T deserializer(byte[] data);
}
