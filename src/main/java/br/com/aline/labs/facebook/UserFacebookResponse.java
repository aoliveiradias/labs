package br.com.aline.labs.facebook;

public class UserFacebookResponse {
    private String id;
    private String name;

    @Deprecated
    UserFacebookResponse() {
    }

    public UserFacebookResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
