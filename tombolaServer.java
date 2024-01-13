import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
public class tombolaServer {
    public static void main(String[] args){
        try{
            //crea il server
            ServerSocket serverSocket = new ServerSocket(12345);

            System.out.println("Server avviato e in attesa di connessioni...");

            //accetta connessioni in entrata

            Socket clinetSocket= serverSocket.accept();
            System.out.println("Connessione accettata da " + clinetSocket.getInetAddress().getHostAddress());

            //creazone degli stream di input/output
            ObjectOutputStream outToClient = new ObjectOutputStream(clinetSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clinetSocket.getInputStream());

            outToClient.writeObject("Benvenuto a Tombola!");

            tombolaGame game = new tombolaGame();
            /* 
            while(true){
                //accetta la connessione di un client
                Socket socket = server.accept();
                System.out.println("Connessione accettata");
                //invia il messaggio di benvenuto al client
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("Benvenuto a Tombola!");
                //crea una nuova partita
                tombolaGame game = new tombolaGame();
                //invia le cartelle al client
                List<String> playerCards = game.getPlayerCards();
                out.writeObject(playerCards);
                //invia i numeri estratti al client
                while(true){
                    //estrai un numero
                    int extractedNumber = game.extractNumber();
                    //invia il numero al client
                    out.writeObject(extractedNumber);
                    //attendi conferma dal client
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    String response = (String) in.readObject();
                    //controlla se c'è un vincitore
                    String message = game.checkWinner();
                    if(message != null){
                        //invia il messaggio al client
                        out.writeObject(message);
                        //chiudi la connessione
                        socket.close();
                        break;
                    }
                }
            }
            */
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
