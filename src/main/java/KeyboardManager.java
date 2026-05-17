import java.util.Scanner;
import java.util.Vector;

import models.dto.OrdineRequestDto;
import models.dto.Prodotto;

public class KeyboardManager extends Thread {
    Storage s;

    public KeyboardManager(Storage s) {
        this.s = s;
    }
    
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        
        while (s.running()) {
            s.changeState(OrderStates.INMENU);
            s.writeMenu("[1] ordina\n[2] esci\ninput: ");
            String input = scanner.nextLine();

            Util.clearConsole();

            if (input.equals("1")) {
                s.changeState(OrderStates.ORDERING);
                Vector<Prodotto> prodotti = new Vector<>();

                while (s.getState().equals(OrderStates.ORDERING)) {
                    s.writeMenu("scrivi il nome del prodotto da ordinare (done per finire ordine)\ninput: ");

                    input = scanner.nextLine();
                    if (!input.matches("[a-zA-Z\\s\\-]+")) {
                        Util.errorMessage("inserisci solo caratteri dell'alfabeto");
                        scanner.nextLine();
                        continue;
                    }
                    Util.clearConsole();
                    if (input.equals("done")) {
                        s.changeState(OrderStates.ORDERED);
                        continue;
                    }
                    s.writeMenu("inserisci la quantità\ninput: ");
                    String quantitaS = "";
                    while (!quantitaS.matches("\\d+")) {
                        quantitaS = scanner.nextLine();
                        if (!quantitaS.matches("\\d+")) {
                            Util.errorMessage("inserisci solo numeri");
                            scanner.nextLine();
                            quantitaS = "";
                        }
                        Util.clearConsole();
                    }
                    prodotti.add(new Prodotto(input, Integer.parseInt(quantitaS)));
                }
                s.addToSend(new OrdineRequestDto(prodotti));
            } else if(input.equals("2")) {
                s.addToSend("!disconnect!");
                return;
            } else {
                Util.errorMessage("input non riconosciuto");
                scanner.nextLine();
                Util.clearConsole();
            }
        }

        scanner.close();
    }
}
