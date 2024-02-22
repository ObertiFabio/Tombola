import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class clientController extends Thread{
    private Socket clientSocket;
    private tombolaGame game;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    //private List<Integer> extractedNumbers;

    public clientController(Socket clientSocket, tombolaGame game) {
        try{
            this.clientSocket = clientSocket;
            this.game = game;
            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //this.extractedNumbers = extractedNumbers;
    }

    @Override
    public void run() {
        try {
            String name = inFromClient.readObject().toString();
            game.addPlayer(this, name);
            String n = inFromClient.readObject().toString();
            outToClient.writeObject(game.generatePlayerCard(Integer.parseInt(n), name));
            if(game.getReadyPlayers() == 3){
                game.play();
            }

        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        try{
            outToClient.writeObject(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
