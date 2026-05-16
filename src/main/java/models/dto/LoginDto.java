package models.dto;

public class LoginDto {

    private String username;
    private String type;

    public LoginDto(String username) {
        this.username = username;
        this.type = "user";
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }
}
