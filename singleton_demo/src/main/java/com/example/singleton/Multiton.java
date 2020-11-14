package com.example.singleton;

import java.util.HashMap;

enum Subsystem
{
    PRIMARY,
    AUXILIARY,
    FALLBACK
}

class Printer
{
    private static int instanceCount = 0;
    private Printer()
    {
        instanceCount++;
        System.out.println("A total of " +
                instanceCount + " instances created.");
    }

    public static HashMap<Subsystem,Printer>
        instances = new HashMap<>();

    public static Printer get(Subsystem ss)
    {
        if (instances.containsKey(ss))
            return instances.get(ss);

        Printer instance = new Printer();
        instances.put(ss, instance);
        return instance;
    }

}

public class Multiton {
    public static void main(String[] args) {
        Printer printer = Printer.get(Subsystem.PRIMARY);
        Printer printer1 = Printer.get(Subsystem.AUXILIARY);
        Printer printer2 = Printer.get(Subsystem.FALLBACK);
        Printer printer3 = Printer.get(Subsystem.AUXILIARY);
        Printer printer4 = Printer.get(Subsystem.FALLBACK);
    }
}
