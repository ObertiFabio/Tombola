import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;

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
        // Genera una cartella per il giocatore con 3 righe da 5 numeri ciascuna
    List<List<Integer>> rows = new ArrayList<>();

    // Genera le righe con numeri unici
    for (int i = 0; i < 3; i++) {
        rows.add(generateUniqueRow());
    }

    // Formatta la cartella come stringa e la ritorna
    StringBuilder card = new StringBuilder();
    for (List<Integer> row : rows) {
        card.append(formatRow(row)).append("\n");
    }
    return card.toString();

    }

    private List<Integer> generateUniqueRow() {
        List<Integer> row = new ArrayList<>();
        Set<Integer> usedNumbers = new HashSet<>();

        // Genera numeri casuali unici per la riga
        while (row.size() < 5) {
            int randomNumber = getRandomNumber();
            if (!usedNumbers.contains(randomNumber)) {
                row.add(randomNumber);
                usedNumbers.add(randomNumber);
            }
        }
        return row;
    }

    private int getRandomNumber() {
        // Genera un numero casuale compreso tra 1 e 90
        return ThreadLocalRandom.current().nextInt(1, 91);
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

    
    public int checkCinquina(List<Integer> extractedNumbers) {
        for (int i = 0; i < playerCards.size(); i++) {
            String[] rows = playerCards.get(i).split("\n");
            for (String row : rows) {
                List<Integer> cardNumbers = convertToIntegerList(row.split("\\s+"));
                if (extractedNumbers.containsAll(cardNumbers)) {
                    return i + 1; // Il giocatore ha una cinquina, restituisci l'indice basato su 1
                }
            }
        }
        return -1; // Nessuna cinquina trovata
    }

}
