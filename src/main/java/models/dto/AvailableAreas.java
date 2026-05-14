package models.dto;

import java.util.List;

public class AvailableAreas {
    private List<String> areas;
    private int statusCode;

    public AvailableAreas(List<String> areas, int statusCode) {
        this.areas = areas;
        this.statusCode = statusCode;
    }

    public List<String> getAreas() {
        return areas;
    }

    public int getStatusCode() {
        return statusCode;
    }
}