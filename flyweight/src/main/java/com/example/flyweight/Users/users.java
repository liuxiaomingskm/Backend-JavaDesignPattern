package com.example.flyweight.Users;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class User
{
    private String fullName;

    public User(String fullName)
    {
        this.fullName = fullName;
    }
}

class User2
{
    static List<String> strings = new ArrayList<>();
    private int[] names;

    public User2(String fullName) {
        Function<String,Integer> getOrAdd = (String s) -> { // Tips 1
            int idx = strings.indexOf(s);
            if (idx != -1) return idx;
            else {
                strings.add(s);
                return strings.size() - 1;
            }
        };

        names = Arrays.stream(fullName.split(" "))
                .mapToInt(s -> getOrAdd.apply(s)).toArray();
    }

    public String getFullName()
    {
        return Arrays.stream(names).mapToObj( i -> strings.get(i))
                .collect(joining(","));
    }
}

class UserDemo {
    public static void main(String[] args) {
        User2 user = new User2("John Smith");
        User2 user2 = new User2("Jane Smith");


        // have "Smith" in common
    }
}

/**
 * Flyweight:
 * A space optimization technique that lets us user less memory by storing externally the data associated with similar
 * objects.
 *
 * It is an algorithm rather than a design pattern.
 * Tip 1: Stream.collect() is one of the Java 8's Stream API‘s terminal methods. It allows us to perform mutable fold
 * operations (repackaging elements to some data structures and applying some additional logic, concatenating them, etc.)
 * on data elements held in a Stream instance.
 *
 *
 */
