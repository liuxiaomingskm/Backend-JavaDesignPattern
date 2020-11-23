package com.example.chainofresponsibility.MethodChain;


import org.springframework.core.NamedInheritableThreadLocal;

class Creature
{
    public String name;
    public int attack, defense;

    public Creature(int attack, int defense, String name)
    {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    @Override
    public String toString()
    {
        return "Creature: " +
        "name ='" + name + '\'' +
        ", attack = " + attack +
        ", defense=" + defense ;
    }
}

class CreatureModifier
{
    protected Creature creature;
    protected CreatureModifier next;

    public CreatureModifier(Creature creature)
    {
        this.creature = creature;
    }

    public void add (CreatureModifier cm)
    {
        if (next != null)
        {next.add(cm);}
        else next = cm;  // simplified the adding step, pass cm to next modifier until modifier is null!
    }

    public void handle()
    {
        if (next != null) next.handle(); //key step!!!
    }
}

class DoubleAttackModifier extends CreatureModifier
{
    public DoubleAttackModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle()
    {
        System.out.println("Doubling " + creature.name + "'s attack");
        creature.attack *= 2;
        super.handle();
    }
}

class IncreaseDefenseModifier extends CreatureModifier
{
    public IncreaseDefenseModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle()
    {
        System.out.println("Increasing " + creature.name + "'s defense");
        creature.defense += 3;
        super.handle(); // 关键一步 传递给下一个modifier
    }
}

class NoBonusesModifier extends CreatureModifier
{
    public NoBonusesModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle()
    {
        System.out.println("No bonuses since then!");
    }
}

class Demo
{
    public static void main(String[] args) {
        Creature goblin = new Creature(2, 3,"Goblin");
        System.out.println(goblin);

        CreatureModifier root = new CreatureModifier(goblin);

        System.out.println("No bonouses since then!!");
        root.add(new NoBonusesModifier(goblin));

        System.out.println("Doubling attack");
        root.add(new DoubleAttackModifier(goblin));

        System.out.println("Increasing goblin's denfense");
        root.add(new IncreaseDefenseModifier(goblin));
        root.handle();
        System.out.println(goblin);
    }
}

public class MethodChain {
}
