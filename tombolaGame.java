import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.Collections;

public class tombolaGame {
    List<Integer> numbers;
    List<String> playerCards;


    public tombolaGame(){
        initalizeGame();
    }

    public void initalizeGame(){
        //iniziallizzala la lista di numeri e la lista di cartelle
        numbers = new ArrayList<>();
        for(int i = 1; i <= 90; i++){
            numbers.add(i);
        }

        playerCards = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            playerCards.add(generatePlayerCard());
        }
    }

    private String generatePlayerCard(){
        // genera una cartella per il giocatore con 3 righe da 5 numeri ciascuna
        List<Integer> row1 = generateRow();
        List<Integer> row2 = generateRow();
        List<Integer> row3 = generateRow();
       

        //formatto la cartella come stringa e la ritorno
        StringBuilder card = new StringBuilder();
        card.append(formatRow(row1)).append("\n");
        card.append(formatRow(row2)).append("\n");
        card.append(formatRow(row3));
        return card.toString();

    }

    private List<Integer> generateRow(){
        //genera una riga con 5 numeri casuali
        List<Integer> row = new ArrayList<>(numbers);
        Collections.shuffle(row);
        return row.subList(0, 5);
    }

    private String formatRow(List<Integer> row) {
        StringBuilder formattedRow = new StringBuilder();
        for (int num : row) {
            formattedRow.append(num).append(" ");
        }
        return formattedRow.toString().trim();
    }

    public int extractNumber(){
        //estrae un numero casuale dalla lista di numeri
        if(!numbers.isEmpty()){
            Random random = new Random();
            int index = random.nextInt(numbers.size());
            int extractedNumber = numbers.remove(index);
            return extractedNumber;
        }
        else{
            return -1;
        }
        
    }

    public boolean isWinner(String playerCard , List<Integer> extractedNumbers){
        //verifica se tutti i numeri della cartella sono stati estratti
        List<Integer> playerNumbers = convertToIntegerList(playerCard.split("\\s+"));
        return extractedNumbers.containsAll(playerNumbers);
    }

    private List<Integer> convertToIntegerList(String[] array) {
        List<Integer> integerList = new ArrayList<>();
        for (String s : array) {
            s = s.replace("\n", ""); // Rimuovi il carattere di escape \n
            try {
                integerList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // Handle invalid input strings here
                // For example, you can skip the invalid string or log an error message
                System.err.println("Invalid input string: " + s);
            }
        }
        return integerList;
    }


    public int determinateWinner(List<Integer> extractedNumbers) {
        try {
            for (int i = 0; i < playerCards.size(); i++) {
                boolean allNumbersExtracted = true;
                List<Integer> cardNumbers = convertToIntegerList(playerCards.get(i).split("\\s+"));
                for (int number : cardNumbers) {
                    if (!extractedNumbers.contains(number)) {
                        allNumbersExtracted = false;
                        break;
                    }
                }
                if (allNumbersExtracted) {
                    return i + 1; // Il giocatore ha vinto, restituisci l'indice basato su 1
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1; // Nessun vincitore trovato o errore di conversione
    }
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
        while(true){
            int extractedNumber = game.extractNumber();
            if(extractedNumber == -1){
                System.out.println("Tutti i numeri sono stati estratti!");
                break;
            }
            extractedNumbers.add(extractedNumber);

            System.out.println("E' stato estratto il numero " + extractedNumber);

            //verifica se c'è un vincitore
            int winner = game.determinateWinner(extractedNumbers);
            if(winner != -1){
                System.out.println("Il vincitore è il giocatore " + winner);
                break;
            }
        }
    }

}
