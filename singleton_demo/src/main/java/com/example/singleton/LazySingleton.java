package com.example.singleton;

class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton()
    {
        System.out.println("initializing a lazy singleton");
    }

//    public static LazySingleton getInstance()
//    {
//        if (instance == null)
//        {
//            instance = new LazySingleton();
//        }
//        return instance;
//    }

    // double-checked locking
    public static LazySingleton getInstance()
    {
        if (instance == null)
        {
            synchronized (LazySingleton.class)
            {
                if (instance == null)
                {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
}}
/**
 * Summary:
 * Singleton is not thread-safe. If two threads visit the constructor at the same time, they might create two instance.
 * The answer is to use double-checked locking, if instance is null, lock the singleton class, and if instance is still
 * null, create a new instance and returned. The second judgement is to ensure any waiting thread check the instance again
 * to ensure not create another instance after they got the lock.
 *
 * Double-checked locking is thread safe.
 */
