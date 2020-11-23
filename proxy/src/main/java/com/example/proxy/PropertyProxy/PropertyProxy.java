package com.example.proxy.PropertyProxy;

import com.fasterxml.jackson.databind.annotation.JsonAppend;

class Property<T>
{
    private T value;

    public Property(T value)
    {
        this.value = value;
    }
    public void setValue(T value)
    {
        // do some logging here
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property<?> property = (Property<?>) o;

        return value != null ? value.equals(property.value) : property.value == null;
    }

    @Override
    public int hashCode()
    {
        return value != null ? value.hashCode(): 0;
    }
}

class Creature
{
    private Property<Integer> agility = new Property<>(10);

    public void setAgility(Integer value)
    {
        // we cannot do agility = value, sadly
        agility.setValue(value);
    }

    public Integer getAgility()
    {
        return agility.getValue();
    }
}

class PropertyProxyDemo
{
    public static void main(String[] args) {
        Creature c = new Creature();
        c.setAgility(12);
    }}

/**
 * Summary:
 * 使用PropertyProxy的原因是想对某个field有更大的控制权，因为只是纯粹的field的话，不仅可以用get，set()修改该值，也可以在初始化的时候直接赋值
 * 所以无法log所有情况，如果使用代理的话 就不一样了 所有用到该field的情况都需要使用该field的proxy，所以可以log所有状况。
 */
