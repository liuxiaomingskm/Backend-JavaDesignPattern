package com.example.builder;


import java.util.function.DoubleToIntFunction;

class Person
{
    public String name;
    public String position;

    @Override
    public String toString()
    {
        return "Person{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}

//SELF means add a type parameter into function and this type has to be the subclass of PersonBuilder
class PersonBuilder<SELF extends PersonBuilder<SELF>>
{
      protected Person person = new Person();

    //critical to return SELF here
    public SELF withNAme(String name)
    {
        person.name = name;
        return self();
    }

    protected SELF self()
    {
        //unchecked cast, but actually safe
        //proof: try sticking a non-PersonBuilder
        // as SELF parameter; it won't work!
        return (SELF) this;
    }

    public Person build()
    {
        return person;
    }
}

class EmployeeBuilder
        extends PersonBuilder<EmployeeBuilder>
{
    public EmployeeBuilder worksAs(String position)
    {
        person.position = position;
        return self();
    }

    @Override
    protected EmployeeBuilder self()
    {
        return this;
    }
}

public class InterfaceWithRecursiveGenerics {
    public static void main(String[] args) {

        EmployeeBuilder eb= new EmployeeBuilder()
                .withNAme("Xiaoming")
                .worksAs("Software Engineer");

        System.out.println(eb.build());
    }
}
