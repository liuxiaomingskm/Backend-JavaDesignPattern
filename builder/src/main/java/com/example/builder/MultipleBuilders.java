package com.example.builder;


class Person1
{
    // address
    public String streetAddress, postcode, city;

    // employment
    public String companyName, position;
    public int annualIncome;

    @Override
    public String toString()
    {
        return "Person{" +
                "streetAddress='" + streetAddress + '\'' +
                ", postcode='" + postcode + '\'' +
                ", city='" + city + '\'' +
                ", companyName='" + companyName + '\'' +
                ", position='" + position + '\'' +
                ", annualIncome=" + annualIncome +
                '}';
    }
}

// builder facade
class PersonBuilder1
{
    // the object we are going to build
    protected Person1 person = new Person1(); // reference!

    public PersonJobBuilder works()
    {
        return new PersonJobBuilder(person);
    }

    public PersonAddressBuilder lives()
    {
        return new PersonAddressBuilder(person);
    }

    public Person1 build() {
        return person;
    }
}

class PersonAddressBuilder extends PersonBuilder1
{
    public PersonAddressBuilder(Person1 person)
    {this.person = person;
    }

    public PersonAddressBuilder at(String streetAdress)
    {
        person.streetAddress = streetAdress;
        return this;
    }

    public PersonAddressBuilder withPostcode(String postcode)
    {
        person.postcode = postcode;
        return this;
    }

    public PersonAddressBuilder in(String city)
    {
        person.city = city;
        return this;
    }
}

class PersonJobBuilder extends PersonBuilder1
{
    public PersonJobBuilder(Person1 person)
    {
        this.person = person;
    }

    public PersonJobBuilder at(String companyName)
    {
        person.companyName = companyName;
        return this;
    }

    public PersonJobBuilder asA(String position)
    {
        person.position = position;
        return this;
    }

    public PersonJobBuilder earning(int annualIncome)
    {
        person.annualIncome = annualIncome;
        return this;
    }
}

public class MultipleBuilders {

    public static void main(String[] args) {
        PersonBuilder1 pb = new PersonBuilder1();
        Person1 person = pb
                .lives()
                .at("123 London Road")
                .in("London")
                .withPostcode("66223")
                .works()
                    .at("Cerner")
                    .asA("Software Engineer")
                    .earning(71000)
                .build();
        System.out.println(person);
    }
}
