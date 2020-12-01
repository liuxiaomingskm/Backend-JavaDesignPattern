package com.example.mediator.RxMediator;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

import java.util.ArrayList;
import java.util.List;


class EventBroker extends Observable<Integer>
{
    private List<Observer<? super Integer>>
            observers = new ArrayList<>();

    @Override
    public void subscribeActual(Observer<? super Integer> observer)
    {
        observers.add(observer);
    }

    public void publish(int n)
    {
        for (Observer<? super Integer> o : observers)
            o.onNext(n); // Tip 1
    }
}

class FootballPlayer
{
    private int goalsScored = 0;
    private EventBroker broker;
    public String name;
    public FootballPlayer(EventBroker broker, String name)
    {
        this.broker = broker;
        this.name = name;
    }

    public void score()
    {
        broker.publish(++goalsScored);
    }
}

class FootballCoach
{
    public FootballCoach(EventBroker broker)
    {
        broker.subscribe(i -> {
            System.out.println("Hey, you scored " + i + " goals!");
        });
    }
}

class RxEventBrokerDemo
{
    public static void main(String [] args)
    {
        EventBroker broker = new EventBroker();
        FootballPlayer player = new FootballPlayer(broker, "jones");
        FootballCoach coach = new FootballCoach(broker);

        player.score();
        player.score();
        player.score();
    }
}
//对eactive Extension还有不了解的地方 需要继续查阅资料
/**
 * Subscribe
 * operate upon the emissions and notifications from an Observable
 * The Subscribe operator is the glue that connects an observer to an Observable. In order for an observer to see the
 * items being emitted by an Observable, or to receive error or completed notifications from the Observable, it must
 * first subscribe to that Observable with this operator.
 *
 * A typical implementation of the Subscribe operator may accept one to three methods (which then constitute the observer),
 * or it may accept an object (sometimes called an Observer or Subscriber) that implements the interface which includes
 * those three methods:
 *
 * onNext
 * An Observable calls this method whenever the Observable emits an item. This method takes as a parameter the item emitted
 * by the Observable.
 * onError
 * An Observable calls this method to indicate that it has failed to generate the expected data or has encountered some
 * other error. This stops the Observable and it will not make further calls to onNext or onCompleted. The onError method
 * takes as its parameter an indication of what caused the error (sometimes an object like an Exception or Throwable, other
 * times a simple string, depending on the implementation).
 * onCompleted
 * An Observable calls this method after it has called onNext for the final time, if it has not encountered any errors.
 * An Observable is called a “cold” Observable if it does not begin to emit items until an observer has subscribed to it;
 * an Observable is called a “hot” Observable if it may begin emitting items at any time, and a subscriber may begin
 * observing the sequence of emitted items at some point after its commencement, missing out on any items emitted previously
 * to the time of the subscription.
 *
 */
