package models;

public class Area {
    private int id;
    private String nome;
    private String ip;

    public Area() {
    }

    public Area(int id, String nome, String ip) {
        this.id = id;
        this.nome = nome;
        this.ip = ip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getIp() {
        return ip;
    }


    public String toMenu(int index) {
        return "[" + index + "] " + this.nome;
    }
}