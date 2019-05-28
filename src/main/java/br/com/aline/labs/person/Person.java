package br.com.aline.labs.person;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "person")
class Person {

    @Id
    private String facebookId;

    private String name;

    @Deprecated
    Person() {
    }

    Person(String facebookId, String name) {
        this.facebookId = facebookId;
        this.name = name;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getName() {
        return name;
    }
}
