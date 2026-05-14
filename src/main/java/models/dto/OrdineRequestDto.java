package models.dto;

import java.util.Vector;

public class OrdineRequestDto {
    private String dettaglio = "";

    public OrdineRequestDto(Vector<Prodotto> prodotti) {
        for (Prodotto prodotto : prodotti) {
            if (dettaglio.isEmpty())
                dettaglio = prodotto.quantita + " x " + prodotto.nome;
            else
                dettaglio = dettaglio.concat(",\n" + prodotto.quantita + " x " + prodotto.nome);
        }
    }

    public String getDettaglio() {
        return dettaglio;
    }
}
