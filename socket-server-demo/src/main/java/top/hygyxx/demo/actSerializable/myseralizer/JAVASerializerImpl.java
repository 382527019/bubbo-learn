package top.hygyxx.demo.actSerializable.myseralizer;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.*;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/24
 * Time:8:54
 */
public class JAVASerializerImpl implements ISerializer {
    //obj->stream
    @Override
    public <T> byte[] serializer(T Object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(Object);
            System.out.println("====序列化");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关流
//            byteArrayOutputStream.close();
//            objectOutputStream.close();
        }
        return null;
    }
    //stream->obj
    @Override
    public <T> T deserializer(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            System.out.println("====反序列化");
            return (T)objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally {
//            byteInputStream.close();
//            objectInputStream.close();
        }
        return null;
    }
}
