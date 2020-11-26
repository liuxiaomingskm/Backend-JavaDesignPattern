package com.example.command.BankAccount;

import org.assertj.core.util.Lists;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BankAccount {
     private int balance;
     private int overdraftLimit = -500;

     public void deposit (int amount)
     {
         balance += amount;
         System.out.println("Deposited " + amount + ", balance is now " + balance);
     }

     public boolean withdraw(int amount)
     {
         if (balance - amount >= overdraftLimit)
         {
             balance -= amount;
             System.out.println("Withdrew" + amount + ", balance is now " + balance);
             return true;
         }
         return false;
     }

    @Override
     public String toString(){
         return "BankAccount{" +
                 "balance=" + balance +
                 '}';
     }
}

interface Command
{
    void call();
    void undo();
}

class BankAccountCommand implements Command
{
    private BankAccount account;
    private Action action;
    private int ammount;
    private boolean succeeded;

    public BankAccountCommand(BankAccount account, Action action, int ammount) {
        this.account = account;
        this.action = action;
        this.ammount = ammount;

    }

    @Override
    public void call()
    {
        switch (action){
            case DEPOSIT:
                succeeded = true;
                account.deposit(ammount);
                break;
            case WITHDRAW:
                succeeded = account.withdraw(ammount);
                break;
        }
    }

    @Override
    public void undo()
    {
        if (!succeeded) return;
        switch (action){

            case DEPOSIT:
                account.withdraw(ammount);
                break;
            case WITHDRAW:
                account.deposit(ammount);
                break;
        }
    }

    public enum Action
    {
        DEPOSIT,WITHDRAW
    }
    public static List<BankAccountCommand> reverseList(List<BankAccountCommand> commands){
        List<BankAccountCommand> newList = new ArrayList<BankAccountCommand>();
        for (int i = commands.size() - 1; i >= 0;i--){
            newList.add(commands.get(i));
        }
        return newList;
    }
}

class Demo
{
    public static void main(String[] args) {
        BankAccount ba =  new BankAccount();
        System.out.println(ba);

        List<BankAccountCommand> commands = List.of(
                new BankAccountCommand(ba, BankAccountCommand.Action.DEPOSIT,100),
                new BankAccountCommand(ba, BankAccountCommand.Action.WITHDRAW,1000)
        );

        for (BankAccountCommand c : commands)
        {
            c.call();
            System.out.println(ba);
        }

        commands = BankAccountCommand.reverseList(commands);
        for (BankAccountCommand c : commands)
        {
            c.undo();
            System.out.println(ba);
        }

    }
}
