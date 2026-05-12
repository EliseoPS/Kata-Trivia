package trivia;

import java.util.ArrayList;
import java.util.LinkedList;



// REFACTOR ME
public class Game implements IGame {
   private static final int BOARD_SIZE = 12;
   private static final int WINNING_COINS = 6;

   ArrayList<Player> players = new ArrayList<>();   

   private QuestionDeck deck = new QuestionDeck();
   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean hasEnoughPlayers() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      players.add(new Player(playerName));

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      Player current = players.get(currentPlayer);
      announce(current.name + " is the current player");
      announce("They have rolled a " + roll);

      if (current.inPenaltyBox) {
         handlePenaltyBoxTurn(current, roll);
      } else {
         handleNormalTurn(current, roll);
      }
   }

   private void handleNormalTurn(Player player, int roll) {
      player.advance(roll, BOARD_SIZE);
      announce(player.name + "'s new location is " + player.position);
      announce("The category is " + currentCategory(player.position));
      askQuestion();
   }

   private void handlePenaltyBoxTurn(Player player, int roll) {
      if (isOdd(roll)) {
         isGettingOutOfPenaltyBox = true;
         announce(player.name + " is getting out of the penalty box");
         handleNormalTurn(player, roll); // Una vez que sale, se mueve como un turno normal
      } else {
         announce(player.name + " is not getting out of the penalty box");
         isGettingOutOfPenaltyBox = false;
      }
   }

   private void askQuestion() {
      String category = currentCategory(players.get(currentPlayer).position);
      announce(deck.drawQuestion(category));
   }


   // private String currentCategory() {
   //    if (players.get(currentPlayer).position - 1 == 0) return "Pop";
   //    if (players.get(currentPlayer).position - 1 == 4) return "Pop";
   //    if (players.get(currentPlayer).position - 1 == 8) return "Pop";
   //    if (players.get(currentPlayer).position - 1 == 1) return "Science";
   //    if (players.get(currentPlayer).position - 1 == 5) return "Science";
   //    if (players.get(currentPlayer).position - 1 == 9) return "Science";
   //    if (players.get(currentPlayer).position - 1 == 2) return "Sports";
   //    if (players.get(currentPlayer).position - 1 == 6) return "Sports";
   //    if (players.get(currentPlayer).position - 1 == 10) return "Sports";
   //    return "Rock";
   // }

   private String currentCategory(int position) {
      if (position == 1 || position == 5 || position == 9) return "Pop";
      if (position == 2 || position == 6 || position == 10) return "Science";
      if (position == 3 || position == 7 || position == 11) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      if (players.get(currentPlayer).inPenaltyBox) {
         return handlePenaltyBoxBranch(isGettingOutOfPenaltyBox);
      } else {
         addGoldCoin("Answer was corrent!!!!"); // <--- Con 'n'
         boolean winner = isGameStillGoing();
         nextPlayer();  
         return winner;
      }
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(players.get(currentPlayer).name + " was sent to the penalty box");
      players.get(currentPlayer).sendToPenaltyBox();

      nextPlayer(); // <--- Mucho más limpio que las 2 líneas de antes
      return true;
   }


   private boolean isGameStillGoing() {
      return !didPlayerWin();
   }

   private boolean isOdd(int roll) {
      return roll % 2 != 0;
   }

   private void announce(Object message) {
      System.out.println(message);
   }

   private boolean didPlayerWin() {
      return players.get(currentPlayer).hasWon(WINNING_COINS);
   }

   private boolean handlePenaltyBoxBranch(boolean isGettingOut) {
      if (isGettingOut) {
         addGoldCoin("Answer was correct!!!!");          
         boolean winner = isGameStillGoing();
         nextPlayer();
         return winner;
      } else {
         nextPlayer();
         return true;
      }
   }

   private void nextPlayer() {
      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
   }

   private void addGoldCoin(String message) {
      announce(message); 
      players.get(currentPlayer).addCoin();
      announce(players.get(currentPlayer).name 
         + " now has " 
         + players.get(currentPlayer).coins 
         + " Gold Coins.");
   }

}

class Player {
   final String name;
   int position = 1; 
   int coins = 0;
   boolean inPenaltyBox = false;

   Player(String name) { this.name = name; }



   public void addCoin() {
      this.coins++;
   }

   public void sendToPenaltyBox() {
      this.inPenaltyBox = true;
   }

   public boolean hasWon(int winningCoins) {
      return this.coins == winningCoins;
   }

   public void advance(int roll, int boardSize) {
      this.position = this.position + roll;
      if (this.position > boardSize) {
         this.position = this.position - 12; 
      }
   }
}

class QuestionDeck {
   private LinkedList<String> popQuestions = new LinkedList<>();
   private LinkedList<String> scienceQuestions = new LinkedList<>();
   private LinkedList<String> sportsQuestions = new LinkedList<>();
   private LinkedList<String> rockQuestions = new LinkedList<>();

   public QuestionDeck() {
      for (int i = 0; i < 50; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast("Science Question " + i);
         sportsQuestions.addLast("Sports Question " + i);
         rockQuestions.addLast("Rock Question " + i);
      }
   }

   public String drawQuestion(String category) {
      if (category.equals("Pop")) return popQuestions.removeFirst();
      if (category.equals("Science")) return scienceQuestions.removeFirst();
      if (category.equals("Sports")) return sportsQuestions.removeFirst();
      if (category.equals("Rock")) return rockQuestions.removeFirst();
      return "Unknown Category";
   }
}

