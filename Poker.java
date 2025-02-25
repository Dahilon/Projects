// importing needed packegs from program
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Class being declared and  intializes of  the varibles
public class Poker {
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    // Main method where it checks if the program in running in test mode or insted the valid real command line arguments
// with dealer. IF in test mode it calles the Testmode method where i was testing the variuos betting logic
// .IF arguments are provided it connects using the connect() method and starts the game with  real dealer
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("test")) {
            Testmode();
        } else if (args.length >= 2) {
            Poker Pokergamerun = new Poker();
            Pokergamerun.connect(args[0], Integer.parseInt(args[1])); // Establish connection to the dealer
            Pokergamerun.startGame();
        }
    }

    // Connect method to set up the socket and streams got this code from stackflows establishes the connection
    // towards an dealer
    void connect(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    // This method handles the game protocol it reads from the dealer while the game is running and depeding on the
    // command given it calls the method for that command WHen the "done" commaned  it stops the game
    // and closes all the connections and scokets
    void startGame() throws IOException {
        boolean gameRunning = true;
        while (gameRunning) {
            // Reads the command from the dealer using loops to check is comands startwith what dealer gives us
            String command = inputStream.readUTF();
            System.out.println("Command recived" + command);

            if (command.startsWith("login")) {
                Login();
            }
            // Hanndles the betting commands
            else if (command.startsWith("bet")) {
                handlebet(command);
            }
            // Handles status(win) command
            else if (command.startsWith("status")) {
                handleStatus(command);
            }
            // Handle done command
            else if (command.startsWith("done")) {
                handleDone(command);
                gameRunning = false;
                // Close the input stream output stream, and socket once the done command has been said by dealer
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        }
    }

    // Login method send my Github id and player name Dahilon adn BlackHawk
    void Login() throws IOException {
        String Githubid = "Dahilon";
        String Avatarname = "Black Hawk";
        outputStream.writeUTF(Githubid + ":" + Avatarname);
    }

    //This method is used to find the value of the cad which will be used in Handlebet
    int getcardvalue(String card) {
        // Remove the last character to get the cars value
        String value = card.substring(0, card.length() - 1);
        switch (value) {
            case "A":
                return 14;
            case "K":
                return 13;
            case "Q":
                return 12;
            case "J":
                return 11;
            default:
                // for the cards bellow 10
                return Integer.parseInt(value);
        }
    }
    //This method is what handles all the betting process for the game the main way it works is it reads my own cards
    // then based of the previous method getvaluecard it calculates the avergae strength of my cards in addtion also
    // calaualting the avreage hand of the oppent card strength.After getting this information there will be decssion
    // process reuslting in what will happen
    void handlebet(String command) throws IOException {
        String[] parts = command.split(":");
        int instack = Integer.parseInt(parts[1]);
        int currentbet = Integer.parseInt(parts[3]);
        String holecard = parts[4];
        String upcard = parts[5];
        // Found this on stack flow
        int upIndex = command.indexOf("up");
        String[] opupcard = command.substring(upIndex + 3).split(":");
        int holecardvalue = getcardvalue(holecard);
        int upcardcalue = getcardvalue(upcard);
        int myhandstrength = holecardvalue + upcardcalue;

        // Calculate the average strength of opponents up cards
        int totalopstrength = 0;
        for (String opcard : opupcard) {
            totalopstrength += getcardvalue(opcard);
        }
        double avgopstrength = (double) totalopstrength / opupcard.length;

        int betAmount;
// Betting process decsion
        if (instack < currentbet) {
            System.out.println("Not enough chips fold");
            outputStream.writeUTF("fold");
            //My hand is stronger than the apponets by at least 5 bets agressive raises by 10
        } else if (myhandstrength > avgopstrength + 5) {
            betAmount = Math.min(currentbet + 10, instack);
            System.out.println("Strong hand aggressive: " + betAmount);
            //Buffing process with weak hands there will be an 15% chane of this happning using math random
        } else if (Math.random() < 0.15) {
            betAmount = Math.min(currentbet + 5, instack);
            System.out.println("Bluffing. Betting: " + betAmount);
            // Foalding any other type of card delt
        } else {
            System.out.println("Weak hand. Folding.");
            outputStream.writeUTF("fold");
        }

    }

    void handleStatus(String command) {
        String[] sections = command.split(":");
        String result = sections[1]; // win or lose
        String firstwinnincard = sections[2];
        String secondwinningcard = sections[3];
        String thirdwinningcard = sections[4];
        // Print the result
        System.out.println("Game Stat" + result);
        System.out.println("Winning hand" + firstwinnincard + " " + secondwinningcard + " " + thirdwinningcard);
    }

    // Handle done command from the dealer priting method
    void handleDone(String command) {
        String message = command.split(":")[1];
        System.out.println("Game Over " + message);
    }

    // Test mode to simulate commands where i test the basic commands and imitated dealer resposne by printing out lines
    //Tested for bet1 with enough chips and less chips ,status win and lose  and done command
   //Tested my handle bet method using diffrent types of stregth of card delt by dealer
    static void Testmode() {
        try {
            DataOutputStream outputStream = new DataOutputStream(System.out);

            // Simualting login command
            String loginCommand = "login";
            System.out.println("Sending command " + loginCommand);
            outputStream.writeUTF(loginCommand);
            System.out.println(" expected reply = Dahilon Black Hawk");

            //1. Simualting bet  player has enough chips and strong hands expected outcome is aggresive betting style
            String betstronghand = "bet1:300:50:20:AD:KH:up:5S:6H:8C";
            System.out.println("Sending command " + betstronghand);
            outputStream.writeUTF(betstronghand);
            System.out.println("Expected response Aggressive \n");

            // 2.Simualting bet command with okay hands expectd result would be an caution bet which is low or an match
            // depedning on siutation
            String betokayhand = "bet1:150:80:20:8D:7C:up:4H:9S:10C";
            System.out.println("Sending command " + betokayhand);
            outputStream.writeUTF(betokayhand);
            System.out.println("Expected response caution bet or match.\n");

            //3 Simualting bet command with low chips expected outcome causing a fold
            String lowchips = "bet1:10:50:20:5D:3H:up:9C:10S:JD";
            System.out.println("Sending command " + lowchips);
            outputStream.writeUTF(lowchips);
            System.out.println("Expected response Fold not enough chips.\n");

            //4 Simualting bet command with a weak hand but a bluff chance possible consevative bet or bull
            String bluff = "bet1:200:100:15:7S:4C:up:9H:6D:5S";
            System.out.println("Sending command " + bluff);
            outputStream.writeUTF(bluff);
            System.out.println("Expected response small bull or fold .\n");

            // 5 Simualting a status command indicating a win with hand of win
            String statuswincommand = "status:win:10D:KH:AS";
            System.out.println("Sending command " + statuswincommand);
            outputStream.writeUTF(statuswincommand);
            System.out.println("Expected outcome Status win with hand 10D KH AS\n");

            //6 Simualting a status command indicating a loss with hands of loss
            String statuslosecommand = "status:lose:9C:3H:5S";
            System.out.println("Sending command" + statuslosecommand);
            outputStream.writeUTF(statuslosecommand);
            System.out.println("Expected outcome Status lose with hand 9C 3H 5S\n");

            // Simualting the done command to conclude the game expected outcome sockets closing
            String donecommand = "done";
            System.out.println("Sending command" + donecommand);
            outputStream.writeUTF(donecommand);
            System.out.println("Expected output: Game Over\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}