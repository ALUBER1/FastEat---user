package models.dto;

import java.util.Vector;

public class AreasDisponibiliDto {

    private int statusCode;
    private Vector<AreaDto> areas;

    public AreasDisponibiliDto(
            int statusCode,
            Vector<AreaDto> areas
    ) {
        this.statusCode = statusCode;
        this.areas = areas;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Vector<AreaDto> getAreas() {
        return areas;
    }
}
