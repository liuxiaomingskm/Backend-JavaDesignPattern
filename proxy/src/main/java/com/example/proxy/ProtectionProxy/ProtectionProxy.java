package com.example.proxy.ProtectionProxy;

interface Drivable
{
    void drive();
}

class Car implements Drivable
{
    protected Driver driver;

    public Car(Driver driver)
    {
        this.driver = driver;
    }

    @Override
    public void drive()
    {
        System.out.println("Car being driven");
    }
}

class CarProxy extends Car
{
    private Driver driver;

    public CarProxy(Driver driver)
    {
        super(driver);
    }

    @Override
    public void drive()
    {
        if(driver.age >= 18)
            super.drive();
        else
            System.out.println("Driver too young");
    }
}

class Driver
{
    public int age;
    public Driver(int age)
    {
        this.age = age;
    }
}

class ProtectionProxy {
    public static void main(String[] args) {
        Drivable car = new CarProxy(new Driver(12));
        car.drive();
    }

}

/**
 * Proxy:
 * A class that functions as an interface to a particular resource.
 * That resource may be remote, expensive to construct, or may require logging
 * or some other added functionality.
 */
