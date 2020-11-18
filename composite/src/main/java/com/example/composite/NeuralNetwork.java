package com.example.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

interface SomeNeurons extends Iterable<Neuron> // see Tip 1
{
    default void connectTo(SomeNeurons other) // interface中也可以建立具体函数，这样子实现这个借口的函数就不用override了，避免
            //增加一个接口方法，然后所有实现该接口的类都要覆盖这个新方法
    {
        if (this == other) return;

        for (Neuron from : this)
            for (Neuron to : other)
            {
                from.out.add(to);
                to.in.add(from);
            }
    }
}

class Neuron implements SomeNeurons
{
    public ArrayList<Neuron> in, out;

    @Override
    public Iterator<Neuron> iterator()
    {
        return Collections.singleton(this).iterator(); // see bottom Tip 2
    }

    @Override
    public void forEach(Consumer<? super Neuron> action) // see Tip 3
    {
        action.accept(this);
    }

    @Override
    public Spliterator<Neuron> spliterator() // see Tip 4
    {
        return Collections.singleton(this).spliterator();
    }

    //  public void connectTo(Neuron other)
//  {
//    out.add(other);
//    other.in.add(this);
//  }
}

class NeuronLayer extends ArrayList<Neuron>
    implements SomeNeurons
{
}


class NeuralNetworkDemo {
    public static void main(String[] args) {
        Neuron neuron = new Neuron();
        Neuron neuron2 = new Neuron();
        NeuronLayer layer = new NeuronLayer();
        NeuronLayer layer2 = new NeuronLayer();

        neuron.connectTo(neuron2);
        neuron.connectTo(layer);
        layer.connectTo(neuron);
        layer.connectTo(layer2);
    }
}
/**
 * Tips:
 * 1. an interface can extends multiple interface, and a non-interface class should not use extends to inherit an interface.
 *
 * 2.java.util.Collections.singleton() method is a java.util.Collections class method. It creates a immutable set over a
 * single specified element. An application of this method is to remove an element from Collections like List and Set.
 *
 *
 * 3. <? super E> means the ? is the ancestor of E, <? extends E> means the ? is the subclass of E, in both cases, E itself is OK.
 *    The Consumer Interface is a part of java.util.function package which has been introduced in Java 8 to implement functional programming
 *    in Java.It represents a function which takes in one argument and produces a result. However these kind of functions don’t return any value.
 *    accept() : This method accepts one value and performs the operation on the given argument.
 *    Example:
 *             Consumer<Integer> display = a -> System.out.println(a);
 *             display.accept(10); // print out 10 in console
 *
 * // Consumer to multiply 2 to every integer of a list
 *         Consumer<List<Integer> > modify = list ->
 *         {
 *             for (int i = 0; i < list.size(); i++)
 *                 list.set(i, 2 * list.get(i));
 *         };
 *
 *         // Consumer to display a list of numbers
 *         Consumer<List<Integer> >
 *             dispList = list -> list.stream().forEach(a -> System.out.print(a + " "));
 *
 *         List<Integer> list = new ArrayList<Integer>();
 *         list.add(2);
 *         list.add(1);
 *         list.add(3);
 *
 *         // Implement modify using accept()
 *         modify.accept(list);
 *
 *         // Implement dispList using accept()
 *         dispList.accept(list);
 *
 *         console output: 4 2 6
 *
 * 4.Java Interface Spliterator. Spliterators can be used for traversing the elements of a source one by one. These
 * sources could be an array, a Collection, an IO Channel or a generator function. The Interface Spliterator is included
 * in JDK 8 for taking the advantages of parallelism in addition to sequential traversal.
 *
 * 5. 对复合模式的理解：
 * 复合模式就是对个体和组合体采用一致的方法处理，在代码中 neuron实现了someNeuron接口，这个接口又继承了iterable接口，通过重写iterator，forEach
 * 等方法，又采用单例模式将该neuron放入set中返回iterator，neuronLayer通过共同实现someNeuron接口，从而达到neuron和neuronLayer可以互联，
 * layer和layer互联。从而简化代码，不用connectTo写多遍。
 *
 */
