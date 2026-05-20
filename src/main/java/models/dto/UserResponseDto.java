package models.dto;

import models.Utente;

public class UserResponseDto {
    private int statusCode;
    private Utente utente;

    public UserResponseDto(int statusCode, Utente utente) {
        this.statusCode = statusCode;
        this.utente = utente;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public static UserResponseDto success(Utente o) {
        return new UserResponseDto(200, o);
    }

    public static UserResponseDto error() {
        return new UserResponseDto(500, null);
    }

    public Utente getUtente() {
        return utente;
    }
}
