import java.io.*;
import java.net.Socket;
import java.util.List;

public class clientController implements Runnable {
    private Socket clientSocket;
    private List<Integer> extractedNumbers;

    public clientController(Socket clientSocket, List<Integer> extractedNumbers) {
        this.clientSocket = clientSocket;
        this.extractedNumbers = extractedNumbers;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            outToClient.writeObject("Benvenuto a Tombola!");

            tombolaGame game = new tombolaGame();
            boolean gameEnded = false;

            while (!gameEnded) {
                outToClient.writeObject(game.playerCards);

                int extractedNumber = game.extractNumber();
                extractedNumbers.add(extractedNumber);
                outToClient.writeObject(extractedNumber);

                String clientResponse = (String) inFromClient.readObject();
                System.out.println("Risposta del client: " + clientResponse);

                int winner = game.determinateWinner(extractedNumbers);

                if (winner != -1) {
                    outToClient.writeObject("Il giocatore " + winner + " ha vinto!");
                    gameEnded = true;
                }

                String continueGame = (String) inFromClient.readObject();
                System.out.println("Risposta del client: " + continueGame);

                if (continueGame.equals("n")) {
                    clientSocket.close();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}