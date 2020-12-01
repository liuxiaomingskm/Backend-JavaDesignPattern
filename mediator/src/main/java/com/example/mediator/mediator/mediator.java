package com.example.mediator.mediator;

import java.util.ArrayList;
import java.util.List;

class Person
{
    public String name;
    public ChatRoom room;
    private List<String> chatLog = new ArrayList<>();

    public Person(String name)
    {
        this.name = name;
    }

    public void receive(String sender, String message)
    {
        String s = sender + ": '" + message + "'";
        System.out.println("[" + name + "'s chat session] " + s);
        chatLog.add(s);
    }

    public void say(String message)
    {
        room.broadcast(name, message);
    }

    public void privateMessage(String who, String message)
    {
        room.message(name,who,message);
    }
}

class ChatRoom
{
    private List<Person> people = new ArrayList<>();

    public void broadcast(String source, String message)
    {
        for (Person person : people)
        {
            if (!person.name.equals(source))
                person.receive(source, message);
        }
    }

    public void join(Person p)
    {
        String joinMsg = p.name + "joins the chat";
        broadcast("room", joinMsg);
        p.room = this;
        people.add(p);
    }

    public void message(String source, String destination, String message)
    {
        people.stream()
                .filter( p -> p.name.equals(destination))
                .findFirst()
                .ifPresent(person -> person.receive(source, message)); // Tip 1
    }
}

public class mediator {

    public static void main(String[] args) {
        ChatRoom room = new ChatRoom();

        Person john = new Person("John");
        Person jane = new Person("Jane");

        room.join(john); // no message here
        room.join(jane);

        john.say("hi, room");
        jane.say("oh, hey john!");

        Person simon = new Person("Simon");
        room.join(simon);
        simon.say("hi, everyone!");

        jane.privateMessage("Simon", "glad you could join us!");
    }
}

/**
 *  Tip 1 :
 *  Stream findFirst() returns an Optional (a container object which may or may not contain a non-null value) describing
 * the first element of this stream, or an empty Optional if the stream is empty. If the stream has no encounter order, t
 * hen any element may be returned.
 *
 *  public void ifPresent(Consumer<? super T> consumer)
 * If a value is present, invoke the specified consumer with the value, otherwise do nothing.
 * Parameters:
 * consumer - block to be executed if a value is present
 * Throws:
 * NullPointerException - if value is present and consumer is null
 */
