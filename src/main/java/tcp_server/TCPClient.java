package tcp_server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String serverHost = "localhost"; // Replace with your server's IP address or hostname
        int serverPort = 9090; // Replace with the port your server is listening on

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String a = scanner.nextLine();
            // Send data to the server
            out.println("Hello, server!" +a);

            System.out.println("Data sent to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

