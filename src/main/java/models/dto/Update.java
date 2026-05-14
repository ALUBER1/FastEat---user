package models.dto;

public class Update {
    private String username;
    private String area;
    private String label;
    private String command;

    public Update(String username, String area, String label, String command) {
        this.username = username;
        this.area = area;
        this.label = label;
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public String getArea() {
        return area;
    }

    public String getLabel() {
        return label;
    }

    public String getCommand() {
        return command;
    }
}