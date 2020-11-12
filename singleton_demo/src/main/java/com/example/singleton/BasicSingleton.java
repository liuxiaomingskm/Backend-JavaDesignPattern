package com.example.singleton;

import javax.websocket.RemoteEndpoint;
import java.io.*;
import java.sql.SQLOutput;

class BasicSingleton implements Serializable
{
    // cannot new this class, however
    // * instance can be created deliberately (reflection)
    // * instance can be created accidentally (serialization)

    private BasicSingleton() {
        System.out.println("Singleton is initializing!");
    }

    private static final BasicSingleton INSTANCE = new BasicSingleton();

    private int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    //require for correct serialization
    //readResolve is used for _replacing_ the object read from the stream

//    protected Object readResolve()
//    {
//        return INSTANCE;
//    }

    // generated getter
    public static BasicSingleton getInstance(){
        return INSTANCE;
    }
}

class BasicSingletonDemo {
    static void saveToFile(BasicSingleton singleton, String filename)
            throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(singleton);
        }

    }

    static BasicSingleton readFromFile(String fileName)
        throws Exception
    {
        try(
                FileInputStream fileIn = new FileInputStream(fileName);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                )
        {
            return (BasicSingleton)in.readObject();
        }
    }

    public static void main(String[] args) throws Exception
    {
        BasicSingleton singleton = BasicSingleton.getInstance();
        singleton.setValue(111);

        String filename = "singleton.bin";
        saveToFile(singleton, filename);

        singleton.setValue(222);

        BasicSingleton singleton2 = readFromFile(filename);

        System.out.println(singleton == singleton2);

        System.out.println(singleton.getValue());
        System.out.println(singleton2.getValue());
    }
}
/**总结：
 * BasicSingleton不是完全保证只有一个实例，可以通过反射和反序列化（serialization）来创造另一个实例，如果想避免反序列化创造实例的漏铜，可以
 * 在类中创建如下方法：
 *
 *     //require for correct serialization
 *     //readResolve is used for _replacing_ the object read from the stream
 *      protected Object readResolve()
 *      {
 *          return INSTANCE;
 *      }
 *
 *      该方法用来代替数据流中读取的对象，返回单例
 */
