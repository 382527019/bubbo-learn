package top.hygyxx.demo.actSerializable.myseralizer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:0:05
 */
public class HessianSerializer implements ISerializer{
    @Override
    public <T> byte[] serializer(T Object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        try {
            hessianOutput.writeObject(Object);
            System.out.println("=====Hessian序列化");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            byteArrayOutputStream.close();
//            hessianOutput.close();
        }
        return new byte[0];
    }

    @Override
    public <T> T deserializer(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        try {
            System.out.println("=====反序列化");
            return (T)hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            byteArrayInputStream.close();
//            hessianInput.close();
        }
        return null;
    }
}
