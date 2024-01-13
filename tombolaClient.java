import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.List;
public class tombolaClient {
    public static void main(String[] args){
        try{
            //connetti al server

            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            String welcomeMessage = (String) in.readObject();
            System.out.println(welcomeMessage);


           while(true){
                //ricevi le cartelle dei giocatori dal server
                List<String> playerCards = (List<String>) in.readObject();
                
                System.out.println("Ecco le tue cartelle:");
                for(int i =0; i< playerCards.size(); i++){
                    System.out.println("Giocatore " + (i+1) + ":");
                    System.out.println(playerCards.get(i));
                }
                //attendi l'estrazione di un numero
                System.out.println("In attesa dell'estrazione di un numero...");

                //ricevi il numero etsrattto dal server
                int extractedNumber = (int) in.readObject();
                System.out.println("E' stato estratto il numero " + extractedNumber);

                //invia conferma al server
                System.out.println("Premi invio per confermare");

                //attendi il messaggio del server (vincitore o completare il gioco)
                String message = (String) in.readObject();
                System.out.println(message);

                //chiedi all'utnete se vuole continuare o uscire
                System.out.println("Vuoi continuare a giocare? (s/n)");
                String response= scanner.nextLine().toLowerCase();
                if(response.equals("n")){
                    socket.close();
                    break;
                }
            
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
