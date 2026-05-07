import java.util.Scanner;
import java.util.Vector;

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
            String input = scanner.nextLine();
            
            if (input.equals("1")) {
                s.changeState(OrderStates.ORDERING);
                Vector<Prodotto> prodotti = new Vector<>();

                while (s.getState().equals(OrderStates.ORDERING)) {
                    System.out.println("scrivi il nome del prodotto da ordinare (done per finire ordine)");
                    input = scanner.nextLine();
                    if (!input.matches("[a-zA-Z]+")) {
                        Util.errorMessage("inserisci solo caratteri dell'alfabeto");
                        continue;
                    }
                    Util.clearConsole();
                    System.out.println("inserisci la quantità");
                    String quantitaS = "";
                    while (!quantitaS.matches("\\d+")) {
                        quantitaS = scanner.nextLine();
                        if (!quantitaS.matches("\\d+")) {
                            Util.errorMessage("inserisci solo numeri");
                        }
                        Util.clearConsole();
                    }
                    prodotti.add(new Prodotto(input, Integer.parseInt(quantitaS)));
                }
                s.changeState(OrderStates.ORDERED);
                s.addProducts(prodotti);
            } else if(input.equals("2")) {
                s.stop();
                continue;
            } else {
                System.out.println("input non riconosciuto");
            }
        }

        scanner.close();
    }
}
