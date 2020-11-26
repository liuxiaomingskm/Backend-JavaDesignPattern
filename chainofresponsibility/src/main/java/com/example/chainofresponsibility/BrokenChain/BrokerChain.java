package com.example.chainofresponsibility.BrokenChain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

class Event<Args> // Tip 1
{
    private int index = 0;
    private Map<Integer, Consumer<Args>> // Tip 5
    handlers = new HashMap<>();

    public int subscribe(Consumer<Args> handler){
        int i = index;
        handlers.put(index++, handler);
        return i;
    }

    public void unsubscribe (int key)
    {
        handlers.remove(key);
    }

    public void fire(Args args)
    {
        for (Consumer<Args> handler : handlers.values())
            handler.accept(args);
    }
}

class Query // Tip 2
{
    public String creatureName;
    enum Argument
    {
        ATTACK, DEFENSE
    }
    public Argument argument;
    public int result;

    public Query(String creatureName, Argument argument, int result){
        this.creatureName = creatureName;
        this.argument = argument;
        this.result = result;
    }
}

class Game // Tip 3
{
    public Event<Query> queries = new Event<>();
}

class Creature
{
    private Game game;
    public String name;
    private int baseAttack, baseDefense;

    public Creature(Game game, String name,
                    int baseAttack, int baseDefense)
    {
        this.game = game;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.name = name;
    }

    int getAttack(){
        Query q = new Query(name, Query.Argument.ATTACK,baseAttack);
        game.queries.fire(q);
        return q.result;
    }

    int getDefense() {
        Query q = new Query(name, Query.Argument.DEFENSE, baseDefense);
        game.queries.fire(q);
        return q.result;
    }

    @Override
    public String toString() //avoid printing game
    {
        return "Creature{" +
                "name='" + name + '\'' +
                ", attack=" + getAttack() + // !!!
                ", defense=" + getDefense() +
                '}';
    }
}

class CreatureModifier
{
    protected Game game;
    protected Creature creature;

    public CreatureModifier(Game game, Creature creature)
    {
        this.game = game;
        this.creature = creature;
    }
}

class IncreasedDefenseModifier
    extends CreatureModifier
{
    public IncreasedDefenseModifier(Game game, Creature creature) {
        super(game, creature);

        game.queries.subscribe( q -> {
            if (q.creatureName.equals(creature.name)
                && q.argument == Query.Argument.DEFENSE)
            { q.result += 3;}
        });
    }
}

class DoubleAttackModifier
        extends CreatureModifier
        implements AutoCloseable // Tip 4
{
    private int token;
    public DoubleAttackModifier(Game game, Creature creature)
    {
        super(game, creature);

        token = game.queries.subscribe(q -> {
            if (q.creatureName.equals(creature.name)
                    && q.argument == Query.Argument.ATTACK)
            {
                q.result *= 2;
            }
        });
    }

    @Override // commented out exception
    public void close() /*throws Exception*/
    {
        game.queries.unsubscribe(token);
    }
}

class BrokenChianDemo
{
    public static void main(String[] args) {
        Game game = new Game();
        Creature goblin = new Creature(game, "Strong Goblin", 2, 2);
        System.out.println(goblin);

        //modifiers can be piled up
        IncreasedDefenseModifier icm = new IncreasedDefenseModifier(game,goblin);

        try(DoubleAttackModifier dam = new DoubleAttackModifier(game,goblin))
        {
            System.out.println(goblin);
        }
        System.out.println(goblin);

    }
}
/**
 * 总结：
 * Tip 1. Event<Arg>主要负责将各类事件放入成员变量哈希表中，并且override fire函数，使某个query对象遍历所有事件。
 * Tip 2.Query用来做所有consumer函数的唯一参数，里面设置了各种属性和字段，用来和事件做匹配，匹配成功则修改result的值，不成功就跳过
 * Tip 3.Game 类设置了event<Arg>变量，全局就创建了唯一一个game实例，他会赋值给creature，也会赋值给各种各样的modifiers（也就是事件），从而保证他们都作用于同一个事件集合
 * Tip 4. DoubleAttackModifier实现了autoCloseable接口，他的作用是在实现自动关闭功能，在最底下的main函数中，有try-with-resource代码块, try(...) 里面声明了某个实现了autoCloseable接口
 *        的变量，无论try-catch block成功执行与否，这个变量所代表的流最终会在执行完毕后关闭，相当于之前的finally写法，但是更简洁
 *
 * Tip 5. 此处关键是event<Arg> 它是事件中心，被内置入game中，Consumer<Arg>是函数式编程的写法，代表一个函数，这个函数有唯一参数Arg
 *
 * 从 broker chain中学到特别多的东西，感谢Udemy这门课~
 */
