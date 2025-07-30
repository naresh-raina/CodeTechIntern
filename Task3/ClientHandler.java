

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Set<ClientHandler> clientHandlers;
    private String clientName;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Enter your name:");
            clientName = in.readLine();
            broadcastMessage("🔔 " + clientName + " has joined the chat!");
        } catch (IOException e) {
            System.err.println("Error initializing client handler: " + e.getMessage());
            closeConnection();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/exit")) {
                    break;
                }
                broadcastMessage("[" + clientName + "]: " + message);
            }
        } catch (IOException e) {
            System.err.println("Connection lost with client: " + clientName);
        } finally {
            closeConnection();
        }
    }

    private void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                if (client != this) {
                    client.out.println(message);
                }
            }
        }
    }

    private void closeConnection() {
        try {
            clientHandlers.remove(this);
            broadcastMessage("🚪 " + clientName + " has left the chat.");
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection for: " + clientName);
        }
    }
}
