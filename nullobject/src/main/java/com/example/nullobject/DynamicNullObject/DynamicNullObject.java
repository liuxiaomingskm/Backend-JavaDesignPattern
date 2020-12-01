package com.example.nullobject.DynamicNullObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


interface Log
{
    void info(String msg);
    void warn(String msg);
}

class ConsoleLog implements Log
{

    @Override
    public void info(String msg)
    {
        System.out.println(msg);
    }

    @Override
    public void warn(String msg)
    {
        System.out.println("WARNING: " + msg);
    }
}

class BankAccount
{
    private Log log;
    private int balance;

    public BankAccount(Log log)
    {
        this.log = log;
    }

    public void deposit(int amount)
    {
        balance += amount;

        // check for null everywhere?
        if (log != null)
        {
            log.info("Deposited " + amount
                    + ", balance is now " + balance);

        }
    }

    public void isNull()
    {
        if (log == null)
            System.out.println("log is null!!");
        else{
            System.out.println("log is not null!!");
        }
    }
    public void withdraw(int amount)
    {
        if (balance >= amount)
        {
            balance -= amount;
            if (log != null)
            {
                log.info("Withdrew " + amount
                        + ", we have " + balance + " left");
            }
        }
        else {
            if (log != null)
            {
                log.warn("Could not withdraw "
                        + amount + " because balance is only " + balance);
            }
        }
    }
}

final class NullLog implements Log
{
    @Override
    public void info(String msg)
    {

    }

    @Override
    public void warn(String msg)
    {

    }
}

class Demo
{
    @SuppressWarnings("unchecked")
    public static <T> T noOp(Class<T> itf)
    {
        return (T) Proxy.newProxyInstance(
                itf.getClassLoader(),
                new Class<?>[]{itf},
                (proxy, method, args) ->
                {
                    if (method.getReturnType().equals(Void.TYPE)){
                        System.out.println("return null!!!");
                        return null;}

                    else
                        return method.getReturnType().getConstructor().newInstance();
                });
    }

    public static void main(String[] args)
    {
//        ConsoleLog log = new ConsoleLog();
        //NullLog log = new NullLog();

        Log log = noOp(Log.class);

        log.info("123");

        BankAccount ba = new BankAccount(log);
        ba.isNull();
        ba.deposit(100);
        ba.withdraw(200);
    }
}

/**
 *  nullObject的意义是创建所有方法，但是方法都没有任何操作，但nullObject实例本身不是null，所以可以避免判断null这个繁琐的操作
 *  使用动态代理的意义是，不需要在创建一个nullLog类，直接创建一个代理实例，任何调用方法都会返回null，达到类似没有任何操作的目的，视频讲解中invocation
 *  handler的写法不太好，容易误导观众，其实应该写成任何method调用都是返回null 就更加符合nullObject的定义了
 */
