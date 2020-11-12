package com.example.singleton;

class InnerStaticSingleton {

    private InnerStaticSingleton(){}

    private static class Impl
    {
        private static final InnerStaticSingleton
        INSTANCE = new InnerStaticSingleton();
    }

    public InnerStaticSingleton getInstance()
    {
        return Impl.INSTANCE;
    }
}
/**
 *Summary:
 * 这种方法是thread safety的， 因为用了final所以引用被固定了 只能返回一个单例
 *
 */
