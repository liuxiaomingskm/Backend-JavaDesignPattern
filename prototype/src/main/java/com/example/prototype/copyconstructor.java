package com.example.prototype;


class Employee
{
    public String name;
    public Address address;

    public Employee(String name, Address address)
    {
        this.name = name;
        this.address = address;
    }

    public Employee(Employee other)
    {
        name = other.name;
        address = new Address(other.address);
    }

    @Override
    public String toString()
    {
        return "Employee{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}

class CopyConstructorDemo
{
    public static void main(String[] args) {
        Employee john = new Employee("John",
                new Address("6741 W 138TH Ter, APT 1422", "Overland Park","UK"));

        //Employee chris john;
        Employee chris = new Employee(john);

        chris.name = "Chris";

        System.out.println(john);
        System.out.println(chris);
    }

}
