package models.dto;

public class Area {
    private String areaName;

    public Area(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public String toMenu(int index) {
        return "[" + index + "] " + areaName;
    }
}