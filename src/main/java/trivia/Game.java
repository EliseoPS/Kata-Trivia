package trivia;

import java.util.ArrayList;
import java.util.LinkedList;



// REFACTOR ME
public class Game implements IGame {
   private static final int BOARD_SIZE = 12;
   private static final int WINNING_COINS = 6;

   ArrayList players = new ArrayList();
   int[] playerPositions = new int[WINNING_COINS];
   int[] playerGoldCoins = new int[WINNING_COINS];
   boolean[] playersInPenaltyBox = new boolean[WINNING_COINS];

   LinkedList popQuestions = new LinkedList();
   LinkedList scienceQuestions = new LinkedList();
   LinkedList sportsQuestions = new LinkedList();
   LinkedList rockQuestions = new LinkedList();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean hasEnoughPlayers() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      playerPositions[howManyPlayers()] = 1;
      playerGoldCoins[howManyPlayers()] = 0;
      playersInPenaltyBox[howManyPlayers()] = false;
      players.add(playerName);

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      System.out.println(players.get(currentPlayer) + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (playersInPenaltyBox[currentPlayer]) {
         if (isOdd(roll)) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
            playerPositions[currentPlayer] = playerPositions[currentPlayer] + roll;
            if (playerPositions[currentPlayer] > BOARD_SIZE) playerPositions[currentPlayer] = playerPositions[currentPlayer] - 12;

            System.out.println(players.get(currentPlayer)
                               + "'s new location is "
                               + playerPositions[currentPlayer]);
            System.out.println("The category is " + currentCategory(playerPositions[currentPlayer]));
            askQuestion();
         } else {
            System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         playerPositions[currentPlayer] = playerPositions[currentPlayer] + roll;
         if (playerPositions[currentPlayer] > BOARD_SIZE) playerPositions[currentPlayer] = playerPositions[currentPlayer] - BOARD_SIZE;

         System.out.println(players.get(currentPlayer)
                            + "'s new location is "
                            + playerPositions[currentPlayer]);
         System.out.println("The category is " + currentCategory(playerPositions[currentPlayer]));
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory(playerPositions[currentPlayer]) == "Pop")
         announce(popQuestions.removeFirst());
      if (currentCategory(playerPositions[currentPlayer]) == "Science")
         announce(scienceQuestions.removeFirst());
      if (currentCategory(playerPositions[currentPlayer]) == "Sports")
         announce(sportsQuestions.removeFirst());
      if (currentCategory(playerPositions[currentPlayer]) == "Rock")
         announce(rockQuestions.removeFirst());
   }


   // private String currentCategory() {
   //    if (playerPositions[currentPlayer] - 1 == 0) return "Pop";
   //    if (playerPositions[currentPlayer] - 1 == 4) return "Pop";
   //    if (playerPositions[currentPlayer] - 1 == 8) return "Pop";
   //    if (playerPositions[currentPlayer] - 1 == 1) return "Science";
   //    if (playerPositions[currentPlayer] - 1 == 5) return "Science";
   //    if (playerPositions[currentPlayer] - 1 == 9) return "Science";
   //    if (playerPositions[currentPlayer] - 1 == 2) return "Sports";
   //    if (playerPositions[currentPlayer] - 1 == 6) return "Sports";
   //    if (playerPositions[currentPlayer] - 1 == 10) return "Sports";
   //    return "Rock";
   // }

   private String currentCategory(int position) {
      if (position == 1 || position == 5 || position == 9) return "Pop";
      if (position == 2 || position == 6 || position == 10) return "Science";
      if (position == 3 || position == 7 || position == 11) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      if (playersInPenaltyBox[currentPlayer]) {
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
      System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
      playersInPenaltyBox[currentPlayer] = true;

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
      return playerGoldCoins[currentPlayer] == WINNING_COINS;
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
      playerGoldCoins[currentPlayer]++;
      announce(players.get(currentPlayer) 
         + " now has " 
         + playerGoldCoins[currentPlayer] 
         + " Gold Coins.");
   }

}
