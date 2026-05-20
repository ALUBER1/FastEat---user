package models.dto;

import models.Area;

public class AreasDisponibiliDto {

    private int statusCode;
    private Area[] areas;

    public AreasDisponibiliDto(
            int statusCode,
            Area[] areas
    ) {
        this.statusCode = statusCode;
        this.areas = areas;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Area[] getAreas() {
        return areas;
    }
}
