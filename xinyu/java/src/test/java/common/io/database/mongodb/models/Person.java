package common.io.database.mongodb.models;

import common.io.database.mongodb.models.Address;
import org.bson.types.ObjectId;

public final class Person {
    // This is my personal design: maintain a public static final String field for storage table name in POJO class
    public static final String TABLE_NAME = "test_collection_object";
    // This ObjectId is essential for Mongodb POJO
    private ObjectId id;
    private String name;
    // Can be int or Integer
    private Integer age;
    private Address address;

    public Person() {
    }

    public Person(String name, Integer age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}