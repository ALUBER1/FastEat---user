package models.dto;

import models.Area;

public class AreaSenderDto {
    private Area area;

    public AreaSenderDto(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }


}
