package hello;


import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    // only AND must exists for the sake of JPA
    protected Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("%s [id=%d, firstName='%s', lastName='%s']", getClass().getSimpleName(), id, firstName, lastName);
    }
}
