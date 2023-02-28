package top.hygyxx.demo.actSerializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/23
 * Time:23:20
 */
//实现Serializable接口=》序列化对象
public class User implements Serializable {
private static final long serialVersionUID = -21412412411221L;
    private String name;
    //transient不序列化
    private transient int age;


    //transient不直接传过去，先还要对其进行处理，用writeObject/readObject
    private void writeObject(ObjectOutputStream out) throws IOException {
        System.out.println("=====反射调用writeObject");
        out.defaultWriteObject();
        out.writeInt(age);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        System.out.println("=====反射调用readObject");
        in.defaultReadObject();
        age = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
