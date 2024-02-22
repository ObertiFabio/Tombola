import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class tombolaServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        //List<Integer> extractedNumbers = new ArrayList<>();
        tombolaGame game = new tombolaGame();
        
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server avviato e in attesa di connessioni...");

            for (int i=0; i<3; i++) {
                // Accetto la connessione e creo un socket
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connessione accettata da " + clientSocket.getInetAddress().getHostAddress());

                // Creo nuovo thread
                clientController clientThread = new clientController(clientSocket, game);
                clientThread.start();
            }
            System.out.println("Partita al completo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}