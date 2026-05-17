package models.dto;

public class InsertResponseDto {
    private int statusCode;
    private String msg;

    public InsertResponseDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public static InsertResponseDto success(String msg) {
        return new InsertResponseDto(200, msg);
    }

    public static InsertResponseDto error(String msg) {
        return new InsertResponseDto(500, msg);
    }
}