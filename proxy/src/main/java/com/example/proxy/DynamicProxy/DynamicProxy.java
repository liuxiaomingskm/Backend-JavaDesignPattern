package com.example.proxy.DynamicProxy;

import javassist.scopedpool.SoftValueHashMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

interface Human
{
    void walk();
    void talk();
}

class Person implements  Human
{
    @Override
    public void walk()
    {
        System.out.println("I am walking");
    }

    @Override
    public void talk()
    {
        System.out.println("I am talking");
    }
}

class LoggingHandler implements InvocationHandler
{
    private final Object target;
    private Map<String, Integer> calls  = new HashMap<>(); //Tip 1  difference between map and Hashmap

    LoggingHandler(Object target)
    {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable // Tip 2 meaning of invoke method
    {
        String name = method.getName();

        if (name.contains("toString"))
        {
            return calls.toString();
        }

        // add or increment number of calls
        calls.merge(name, 1, Integer::sum); // Tip 3 ::
        return method.invoke(target,args);
    }

}

class DynamicLoggingProxyDemo
{
    @SuppressWarnings("unchecked") // Tip 4
    public static <T> T withLogging(T target, Class<T> itf)
    {
        return (T) Proxy.newProxyInstance(
                itf.getClassLoader(),
                new Class<?>[] { itf },
                new LoggingHandler(target)); // Tip 5

    }

    public static void main(String[] args) {
        Person person = new Person();
        Human logged = withLogging(person, Human.class);
        logged.walk();
        logged.talk();
        logged.talk();

        System.out.println(logged);
    }

}

/**
 * Summary:
 * Tip 1: Map is the interface, and Hashmap is the implementation of map. Map --> abstractMap -> hashmap，所以这里可以用hashmap替代map 创建实例
 *----------------------------------------------------------------------------------------------------------------------
 * Tip 2: InvocationHandler is the interface implemented by the invocation handler of a proxy instance.
 * Each proxy instance has an associated invocation handler. When a method is invoked on a proxy instance, the method invocation
 * is encoded and dispatched to the invoke method of its invocation handler.
 *Parameters:
 * proxy - the proxy instance that the method was invoked on
 * method - the Method instance corresponding to the interface method invoked on the proxy instance. The declaring class of the Method object will be the interface that the method was declared in, which may be a superinterface of the proxy interface that the proxy class inherits the method through.
 * args - an array of objects containing the values of the arguments passed in the method invocation on the proxy instance, or null if interface method takes no arguments. Arguments of primitive types are wrapped in instances of the appropriate primitive wrapper class, such as java.lang.Integer or java.lang.Boolean.
 *----------------------------------------------------------------------------------------------------------------------
 * Tip 3: The double colon (::) operator, also known as method reference operator in Java, is used to call a method by
 * referring to it with the help of its class directly. They behave exactly as the lambda expressions. The only difference
 * it has from lambda expressions is that this uses direct reference to the method by name instead of providing a delegate to the method.
 *
 * Example:
 class GFG {
 public static void main(String[] args)
 {

 // Get the stream
 Stream<String> stream
 = Stream.of("Geeks", "For",
 "Geeks", "A",
 "Computer",
 "Portal");

 // Print the stream
 stream.forEach(s -> System.out.println(s));
 }

 }
 Double Colon ::  : Very simply put, when we are using a method reference – the target reference is placed before the delimiter ::
 and the name of the method is provided after it.
 merge(key, value, bifunction), bifunction使用了函数引用，引用了Integer.sum,Integer.sum可以接受两个参数，所以这里正好需要放入两个参数
 ----------------------------------------------------------------------------------------------------------------------
 * Tip 4:
 * The compiler will issue a warning about this method. It'll warn that we're using a raw-typed collection.
 * If we don't want to fix the warning, then we can suppress it with the @SuppressWarnings annotation.
 *
 * This annotation allows us to say which kinds of warnings to ignore. While warning types can vary by compiler vendor,
 * the two most common are deprecation and unchecked.
 *
 * deprecation tells the compiler to ignore when we're using a deprecated method or type.
 * unchecked tells the compiler to ignore when we're using raw types.
 *----------------------------------------------------------------------------------------------------------------------
 * Tip 5:
 * public static Object newProxyInstance(ClassLoader loader,
 *                       Class<?>[] interfaces,
 *                       InvocationHandler h)
 *                                throws IllegalArgumentException
 * Returns an instance of a proxy class for the specified interfaces that dispatches method invocations to the specified
 * invocation handler. This method is equivalent to:
 *      Proxy.getProxyClass(loader, interfaces).
 *          getConstructor(new Class[] { InvocationHandler.class }).
 *          newInstance(new Object[] { handler });
 *
 * Proxy.newProxyInstance throws IllegalArgumentException for the same reasons that Proxy.getProxyClass does.
 *
 * Parameters:
 * loader - the class loader to define the proxy class
 * interfaces - the list of interfaces for the proxy class to implement
 * h - the invocation handler to dispatch method invocations to
 * Returns:
 * a proxy instance with the specified invocation handler of a proxy class that is defined by the specified class loader
 * and that implements the specified interfaces
 *
 * Summary:
 * 1. A proxy has the same interface as the underlying object
 * 2. To create a proxy, simply replicate the existing interface of an object
 * 3. Add relevant functionality to the redefined member functions.
 */


