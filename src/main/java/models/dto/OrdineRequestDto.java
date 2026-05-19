package models.dto;

import models.Prodotto;

import java.util.Vector;

public class OrdineRequestDto {
    private String dettaglio = "";

    public OrdineRequestDto(Vector<Prodotto> prodotti) {
        for (Prodotto prodotto : prodotti) {
            if (dettaglio.isEmpty())
                dettaglio = prodotto.getQuantita() + " x " + prodotto.getNome();
            else
                dettaglio = dettaglio.concat(",\n" + prodotto.getQuantita() + " x " + prodotto.getNome());
        }
    }

    public String getDettaglio() {
        return dettaglio;
    }
}
