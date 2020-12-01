package com.example.iterator.ArrayBackedProperties;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

//最原始办法，增加一个变量就需要修改所有的max,sum，average方法
class SimpleCreature
{
    private int strength, agility, intelligence;

    public int max()
    {
        return Math.max(strength,
                Math.max(agility, intelligence));
    }

    public int sum()
    {
        return strength+agility+intelligence;
    }

    public double average()
    {
        return sum() / 3.0;
    }

    public int getStrength()
    {
        return strength;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    public int getAgility()
    {
        return agility;
    }

    public void setAgility(int agility)
    {
        this.agility = agility;
    }

    public int getIntelligence()
    {
        return intelligence;
    }

    public void setIntelligence(int intelligence)
    {
        this.intelligence = intelligence;
    }
}

class Creature implements Iterable<Integer>
{
    private int [] stats = new int[3];

    private final int str = 0;

    public int getStrength() { return stats[str]; }
    public void setStrength(int value)
    {
        stats[str] = value;
    }

    public int getAgility() { return stats[1]; }
    public void setAgility(int value)
    {
        stats[1] = value;
    }

    public int getIntelligence() { return stats[2]; }
    public void setIntelligence(int value)
    {
        stats[2] = value;
    }

    public int sum()
    {
        return IntStream.of(stats).sum(); // Tip 1
    }

    public int max()
    {
        return IntStream.of(stats).max().getAsInt();
    }

    public double average()
    {
        return IntStream.of(stats).average().getAsDouble();
    }

    @Override
    public void forEach(Consumer<? super Integer> action)
    {
        for (int x : stats)
            action.accept(x);
    }

    @Override
    public Spliterator<Integer> spliterator()
    {
        return Arrays.spliterator(stats);
    }

    @Override
    public Iterator<Integer> iterator()
    {
        return IntStream.of(stats).iterator();
    }

}
public class ArrayBackedProperties {
    public static void main(String[] args) {
        Creature creature = new Creature();
        creature.setAgility((12));
        creature.setIntelligence(13);
        creature.setStrength(17);
        System.out.println(        "Creature has a max stat of " + creature.max()
                + ", total stats of " + creature.sum()
                + " and an average stat of " + creature.average());
    }
}

/**
 * Tip 1:
 * A sequence of primitive int-valued elements supporting sequential and parallel aggregate operations. This is the int
 * primitive specialization of Stream.
 *
 * static IntStream	of​(int... values)
 * Returns a sequential ordered stream whose elements are the specified values. //注意此方法为java11方法，Java 8 中不存在此方法
 *
 *  Summary:
 *  ArrayBackedProperty的好处就是把所有属性几种放到一个array中，当统计所有元素的stats时，调用array的相关方法sum,max,average 就能获得
 *  就算添加其他属性后，也不需要修改这些统计方法
 */
