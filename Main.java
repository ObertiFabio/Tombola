import java.util.ArrayList;
import java.util.List;

public class Main{

    public static void main(String[] args){
        tombolaGame game = new tombolaGame();
        System.out.println("Benvenuto nel gioco della tombola!");
        System.out.println("Ecco le tue cartelle:");

        for(int i =0; i< game.playerCards.size(); i++){
            System.out.println("Giocatore " + (i+1) + ":");
            System.out.println(game.playerCards.get(i));
        }

        System.out.println("Iniziamo a giocare!");
        List<Integer> extractedNumbers = new ArrayList<>();
        boolean tombolaFound = false;
        boolean cinquinaFound = false;
        while(true){
            int extractedNumber = game.extractNumber();
            if(extractedNumber == -1){
                System.out.println("Tutti i numeri sono stati estratti!");
                break;
            }
            extractedNumbers.add(extractedNumber);

            System.out.println("E' stato estratto il numero " + extractedNumber);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            //verifica della cinquina
            if (!cinquinaFound && !tombolaFound) {
                int cinquina = game.checkCinquina(extractedNumbers);
                if (cinquina != -1) {
                    System.out.println("Il giocatore " + cinquina + " ha fatto una cinquina!");
                    cinquinaFound = true; 
                }
            }
            
            if (!tombolaFound) {
                int winner = game.determinateWinner(extractedNumbers);
                if (winner != -1) {
                    System.out.println("Il giocatore " + winner + " ha fatto tombola!");
                    tombolaFound = true; 
                    break;
                }
            }
        
        }
    }
}