package entity.model;

import java.util.Date;
import java.util.Objects;

public class Person {
    private Integer id;
    private String surname;
    private String name;
    private String lastname;
    private Date birthdate;

    public Person withId(Integer id) {
        this.id = id;
        return this;
    }

    public Person withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public Person withName(String name) {
        this.name = name;
        return this;
    }

    public Person withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public Person withBirthdate(Date birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public Integer id() {
        return id;
    }

    public String surname() {
        return surname;
    }

    public String name() {
        return name;
    }

    public String lastname() {
        return lastname;
    }

    public Date birthdate() {
        return birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(name, person.name) &&
                Objects.equals(lastname, person.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, lastname);
    }
}
