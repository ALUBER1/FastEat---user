package models.dto;

import models.Area;

public class AvailableAreas {
    private Area[] areas;
    private int statusCode;

    public AvailableAreas(Area[] areas, int statusCode) {
        this.areas = areas;
        this.statusCode = statusCode;
    }

    public Area[] getAreas() {
        return areas;
    }

    public int getStatusCode() {
        return statusCode;
    }
}