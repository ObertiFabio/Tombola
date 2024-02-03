import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class tombolaServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        List<Integer> extractedNumbers = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(12345);

            System.out.println("Server avviato e in attesa di connessioni...");

            // accetta connessioni in entrata
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connessione accettata da " + clientSocket.getInetAddress().getHostAddress());

            // creazione degli stream di input/output
            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            outToClient.writeObject("Benvenuto a Tombola!");

            tombolaGame game = new tombolaGame();
            boolean gameEnded = false;

            while (!gameEnded) {
                // Invia le cartelle dei giocatori al client
                outToClient.writeObject(game.playerCards);

                // Estrai un numero e invialo al client
                int extractedNumber = game.extractNumber();
                extractedNumbers.add(extractedNumber);
                outToClient.writeObject(extractedNumber);

                // Attendi la conferma del client
                String clientResponse = (String) inFromClient.readObject();
                System.out.println("Risposta del client: " + clientResponse);

                //verifica la presenza di un vincitore
                int winner = game.determinateWinner(extractedNumbers);

                if (winner != -1) {
                    outToClient.writeObject("Il giocatore " + winner + " ha vinto!");
                    gameEnded = true;
                }

                // Attendi la risposta del client per decidere se continuare o uscire
                String continueGame = (String) inFromClient.readObject();
                System.out.println("Risposta del client: " + continueGame);

                if (continueGame.equals("n")) {
                    // Il client ha scelto di uscire, chiudi la connessione
                    clientSocket.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

