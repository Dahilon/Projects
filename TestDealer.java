import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TestDealer {

    public static void main(String[] args) {
        try {
            // Step 1: Create a ServerSocket to listen for connections from Poker
            ServerSocket serverSocket = new ServerSocket(8081);  // Use the same port as Poker connects to
            System.out.println("Dealer is waiting for a connection...");

            // Step 2: Accept a connection from Poker
            Socket socket = serverSocket.accept();
            System.out.println("Player connected.");

            // Step 3: Set up input and output streams to communicate with Poker
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Step 4: Simulate dealer sending a login command
            outputStream.writeUTF("login");
            outputStream.flush();
            System.out.println("Sent command: login");

            // Step 5: Wait for a response from Poker (GitHub ID and Avatar Name)
            String response = inputStream.readUTF();
            System.out.println("Received response: " + response);

            // Step 6: Test different betting scenarios by sending bet1 commands

            // Scenario 1: Player has enough chips to bet
            String bet1Command = "bet1:100:50:10";  // 100 chips in stack, 50 pot size, current bet 10
            outputStream.writeUTF(bet1Command);
            outputStream.flush();
            System.out.println("Sent command: " + bet1Command);

            // Wait for a response (expecting a bet)
            response = inputStream.readUTF();
            System.out.println("Received response (Scenario 1): " + response);

            // Scenario 2: Player has just enough chips to match the bet
            bet1Command = "bet1:10:50:10";  // 10 chips in stack, 50 pot size, current bet 10
            outputStream.writeUTF(bet1Command);
            outputStream.flush();
            System.out.println("Sent command: " + bet1Command);

            // Wait for a response (expecting a bet of 10 chips)
            response = inputStream.readUTF();
            System.out.println("Received response (Scenario 2): " + response);

            // Scenario 3: Player doesn't have enough chips to match the bet
            bet1Command = "bet1:5:50:10";  // 5 chips in stack, 50 pot size, current bet 10
            outputStream.writeUTF(bet1Command);
            outputStream.flush();
            System.out.println("Sent command: " + bet1Command);

            // Wait for a response (expecting a fold)
            response = inputStream.readUTF();
            System.out.println("Received response (Scenario 3): " + response);

            // Step 7: Simulate dealer sending a status command (e.g., win status)
            String statusCommand = "status:win:10D:KH:AS";  // Example: win status with a winning hand
            outputStream.writeUTF(statusCommand);
            outputStream.flush();
            System.out.println("Sent command: " + statusCommand);

            // No response expected from status, continue...

            // Step 8: Simulate dealer sending a done command
            String doneCommand = "done:Game Over";
            outputStream.writeUTF(doneCommand);
            outputStream.flush();
            System.out.println("Sent command: " + doneCommand);

            // Step 9: Close the connection and server socket
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
