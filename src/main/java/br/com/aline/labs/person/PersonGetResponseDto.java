package br.com.aline.labs.person;

class PersonGetResponseDto {

    private String facebookId;

    private String name;

    public PersonGetResponseDto(String facebookId, String name) {
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
