🃏 Poker Game AI

🎯 Project Overview

This project is an AI-powered poker bot that connects to a dealer, processes game commands, and makes strategic betting decisions based on hand strength and opponent behavior. The system uses Java networking (Sockets) for real-time interaction with the dealer and implements basic AI logic to optimize betting strategies.

⚡ Features
	•	🤖 AI-Driven Decision Making – The bot evaluates hand strength and dynamically adjusts betting behavior.
	•	🔄 Real-Time Dealer Communication – Uses Sockets to interact with the dealer, process game events, and respond to commands.
	•	📈 Betting Strategy Optimization – Implements bluffing, aggressive raises, and conservative folds based on opponent hand strength.
	•	🛠️ Test Mode Simulation – Allows testing of various game scenarios without a real dealer connection.

🏗️ How It Works
	1.	Connects to the Dealer via IP and port.
	2.	Receives game commands (login, bet, status, done).
	3.	Processes hand strength and opponent behavior.
	4.	Decides on bets (raise, call, fold) using probability-based AI.
	5.	Handles game end and logs results.

🔧 Technologies Used
	•	Java – Core language for networking and game logic.
	•	Sockets (TCP/IP) – Real-time communication with the dealer.
	•	Data Streams – Reads and writes game commands efficiently.

🚀 Running the Project

Start the Dealer (Test Mode)
javac TestDealer.java
java TestDealer

Run the Poker Bot

javac Poker.java
java Poker test

Connect to a Real Dealer

java Poker <IP_ADDRESS> <PORT>

## 📊 Betting Strategy  
| **Hand Strength**  | **Opponent Strength**  | **Action**  |
|-------------------|---------------------|------------|
| **Strong** (Aces, Kings)  | **Weaker than AI**  | Aggressive raise  |
| **Medium** (Face cards)  | **Slightly weaker/equal**  | Cautious bet  |
| **Weak** (Low cards)  | **Stronger opponent**  | Fold or Bluff (15% chance)  |

📌 Key Takeaways

✅ AI-Driven Betting – Adjusts strategy dynamically based on game state.
✅ Socket Communication – Simulates real-time poker gameplay.
✅ Test Mode – Debug and refine the AI logic with simulated dealer responses.
