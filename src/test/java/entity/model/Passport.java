package entity.model;

public class Passport {
    private Integer id;
    private Person person;
    private Integer seria;
    private Integer number;


    public Passport withId(Integer id) {
        this.id = id;
        return this;
    }

    public Passport withPerson(Person person) {
        this.person = person;
        return this;
    }

    public Passport withSeria(Integer seria) {
        this.seria = seria;
        return this;
    }

    public Passport withNumber(Integer number) {
        this.number = number;
        return this;
    }

    public Integer id() {
        return id;
    }

    public Person person() {
        return person;
    }

    public Integer seria() {
        return seria;
    }

    public Integer number() {
        return number;
    }
}
