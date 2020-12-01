package com.example.nullobject.Concept;

public class Concept {
}

/**
 * Null Object:
 * A no-op object that conforms to the required interface, satisfying a dependency requirement of some
 * other object.
 *
 * When to Use the Null Object Pattern
 * We should use the Null Object Pattern when a Client would otherwise check for null just to skip execution or perform
 * a default action. In such cases, we may encapsulate the neutral logic within a null object and return that to the client
 * instead of the null value. This way client's code no longer needs to be aware if a given instance is null or not.
 *
 * Such an approach follows general object-oriented principles, like Tell-Don't-Ask.
 *
 * To better understand when we should use the Null Object Pattern, let's imagine we have to implement CustomerDao interface
 * defined as follows:
 *
 * public interface CustomerDao {
 *     Collection<Customer> findByNameAndLastname(String name, String lastname);
 *     Customer getById(Long id);
 * }
 * Most of the developers would return Collections.emptyList() from findByNameAndLastname() in case none of the customers
 * matches the provided search criteria. This is a very good example of following the Null Object Pattern.
 *
 * In contrast, the getById() should return the customer with the given id. Someone calling this method expects to get
 * the specific customer entity. In case no such customer exists we should explicitly return null to signal there is something
 * wrong with the provided id.
 *
 * 感觉null object pattern就是当觉得返回值是null(工厂创造实例，但是当找不到符合要求的实例时，可能返回null)，还要多加一层判断是否null 而麻烦时，自动返回一个没有任何动作的obhect
 */
