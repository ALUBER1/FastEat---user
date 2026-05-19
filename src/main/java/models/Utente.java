package models;

public class Utente {
    private int id;
    private int zona_id;
    private String username;

    public Utente() {
    }

    public Utente(int id, int zona_id, String username) {
        this.id = id;
        this.zona_id = zona_id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public int getZona_id() {
        return zona_id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZona_id(int zona_id) {
        this.zona_id = zona_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", zona_id=" + zona_id +
                ", username='" + username + '\'' +
                '}';
    }
}