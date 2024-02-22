import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

public class tombolaGame {
    private List<Integer> numbers;
    private HashMap<String, LinkedList<StringBuilder>> playerCards;
    private HashMap<String, clientController> players;
    private int readyPlayers = 0;

    public tombolaGame(){
        initalizeGame();
        playerCards = new HashMap<>();
        players = new HashMap<>();

    }

    public void initalizeGame(){
        //iniziallizzala la lista di numeri e la lista di cartelle
        numbers = new ArrayList<>();
        for(int i = 1; i <= 90; i++){
            numbers.add(i);
        }
    }

    public void play(){
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        initalizeGame();
        List<Integer> extractedNumbers = new ArrayList<>();
        boolean tombolaFound = false;
        boolean cinquinaFound = false;
        while(true){
            int extractedNumber = extractNumber();
            if(extractedNumber == -1){
                broadcast("Tutti i numeri sono stati estratti!");
                break;
            }
            extractedNumbers.add(extractedNumber);
            broadcast("E' stato estratto il numero " + extractedNumber);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //verifica della cinquina
            if (!cinquinaFound && !tombolaFound) {
                String cinquina = checkCinquina(extractedNumbers);
                if (cinquina != null) {
                    broadcast("Il giocatore " + cinquina + " ha fatto una cinquina!");
                    cinquinaFound = true; 
                }
            }
            if (!tombolaFound) {
                String winner = determinateWinner(extractedNumbers);
                if (winner != null) {
                    broadcast("Il giocatore " + winner + " ha fatto tombola!");
                    tombolaFound = true; 
                    break;
                }
            }
        }
    }

    public LinkedList<StringBuilder> generatePlayerCard(int n, String nome){
        // Genera una cartella per il giocatore con 3 righe da 5 numeri ciascuna
        for(int j = 0; j < n; j++){
            
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
            playerCards.get(nome).add(card);
        }
        readyPlayers++;
        return playerCards.get(nome);
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

    public int extractNumber() {
        // Estrae un numero casuale dalla lista di numeri
        if (!numbers.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(numbers.size());
            int extractedNumber = numbers.remove(index);
            return extractedNumber;
        } else {
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


    public String determinateWinner(List<Integer> extractedNumbers) {
        for (String player : playerCards.keySet()) {
            for (StringBuilder card : playerCards.get(player)) {
                if (isWinner(card.toString(), extractedNumbers)) {
                    return player; // Il giocatore ha fatto tombola
                }
            }
        }
        return null; // Nessun vincitore trovato
    }

    
    public String checkCinquina(List<Integer> extractedNumbers) {
        for (String player : playerCards.keySet()) {
            for (StringBuilder card : playerCards.get(player)) {
                String[] rows = (card.toString()).split("\n");
                for (String row : rows) {
                    List<Integer> rowNumbers = convertToIntegerList(row.split("\\s+"));
                    if (extractedNumbers.containsAll(rowNumbers)) {
                        return player; // Il giocatore ha fatto cinquina
                    }
                }
            }
        }
        return null; // Nessun vincitore trovato
    }

    public void addPlayer(clientController clientThread, String nome){
        playerCards.put(nome, new LinkedList<StringBuilder>());
        players.put(nome, clientThread);
    }

    public void broadcast(String message){
        for (clientController player : players.values()) {
            player.send(message);
        }
    }

    public int getReadyPlayers(){
        return readyPlayers;
    }
}
