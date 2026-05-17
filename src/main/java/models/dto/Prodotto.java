package models.dto;
import java.io.Serializable;

public class Prodotto implements Serializable {
    String nome;
    int quantita;

    public Prodotto(String nome, int quantita) {
        this.nome = nome;
        this.quantita = quantita;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }
}
