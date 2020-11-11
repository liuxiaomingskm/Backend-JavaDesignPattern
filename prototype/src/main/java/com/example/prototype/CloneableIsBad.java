package com.example.prototype;

import java.util.Arrays;

/**
 * Some concept
 * An existing (partially or fully constructed) design is a Prototype
 *
 * We make a copy (clone) the prototype and customize it
 * .
 */


// Cloneable is a marker interface
class Address implements Cloneable {
    public String streetAddress, city, country;

    public Address(String streetAddress, String city, String country)
    {
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    //比较新奇的写法 以地址作为参数的构造函数 this指代构造函数
    public Address(Address other)
    {
        this(other.streetAddress, other.city, other.country);
    }

    @Override
    public String toString()
    {
        return "Address{" +
                "streetName='" + streetAddress +'\'' +
                ", city=" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    // base class clone() is protected
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new Address(streetAddress, city,country);
    }
}

class Person implements Cloneable
{
    public String [] names;
    public Address address;

    public Person(String[] names, Address address)
    {
        this.names = names;
        this.address = address;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "names=" + Arrays.toString(names) +
                ", address=" + address +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new Person(
            // clone() creates a shallow copy!
        /*names */ names.clone(),

        // fixes address but not names
        /*address */ // (Address) address.clone();
        address instanceof Cloneable ? (Address) address.clone() : address
        );
    }
}

class CloneableDemo {
    public static void main(String[] args) throws CloneNotSupportedException{
        Person john =  new Person(new String[]{"John", "Smith"},
                new Address("London Road", "Overland Park", "US"));


        //jane is the girl next door
        Person jane = (Person) john.clone();
        jane.names[0] = "Jane"; // clone is (originally) shallow copy
        jane.address.country = "UK";

        System.out.println(john);
        System.out.println(jane);

        // list.clone is deep copy or shallow copy?
        /**
         * Summary:
         *
         * Based on the GeeksforGeeks, clone() and System.arraycopy() is deepcopy for arrayList
         *
         * Interface Cloneable is a marker interface, it doesn't contain anything.
         *
         * The default clone() is shallow copy, so we have to override it.
         */
    }
}
