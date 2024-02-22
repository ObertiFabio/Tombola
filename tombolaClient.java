import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.List;
public class tombolaClient {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader stdIn;
    private String n;

    public tombolaClient(){
        try{
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws Exception{

        System.out.print("Inserisci il tuo nome:");
        String name = stdIn.readLine();
        out.writeObject(name);
        
        System.out.println("Benvenuto a Tombola!");
        
        do{
            System.out.print("Quante cartelle vuoi?");
            n = stdIn.readLine();
        }while((!n.matches("[0-9]+") || n.equals("")));
        out.writeObject(n);

        List<StringBuilder> cards = (List<StringBuilder>) in.readObject();
        System.out.println("Ecco le tua cartelle:");
        for(StringBuilder card : cards){
            System.out.println(card);
        }

        System.out.println("In attesa degli altri giocatori...");

        while(true){
            String message = (String) in.readObject();
            if(message != null){
                System.out.println(message);
            }
            else{
                break;
            }
        }

    }
    public static void main(String[] args){
        tombolaClient client = new tombolaClient();
        try{
            client.start();
        }catch(Exception e){
            
        }
    }
}
