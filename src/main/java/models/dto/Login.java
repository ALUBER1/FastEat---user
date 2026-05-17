package models.dto;

public class Login {
    private String username;

    public Login(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}