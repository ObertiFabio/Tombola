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

            while (true) {
                // Accetto la connessione e creo un socket
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connessione accettata da " + clientSocket.getInetAddress().getHostAddress());

                // Creo nuovo thread
                Thread clientThread = new Thread(new clientController(clientSocket, extractedNumbers));
                clientThread.start();
            }
        } catch (IOException e) {
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