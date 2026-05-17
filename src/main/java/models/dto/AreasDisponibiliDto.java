package models.dto;

public class AreasDisponibiliDto {

    private int statusCode;
    private AreaDto[] areas;

    public AreasDisponibiliDto(
            int statusCode,
            AreaDto[] areas
    ) {
        this.statusCode = statusCode;
        this.areas = areas;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public AreaDto[] getAreas() {
        return areas;
    }
}
