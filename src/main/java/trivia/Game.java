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
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
            playerPositions[currentPlayer] = playerPositions[currentPlayer] + roll;
            if (playerPositions[currentPlayer] > BOARD_SIZE) playerPositions[currentPlayer] = playerPositions[currentPlayer] - 12;

            System.out.println(players.get(currentPlayer)
                               + "'s new location is "
                               + playerPositions[currentPlayer]);
            System.out.println("The category is " + currentCategory());
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
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory() == "Pop")
         System.out.println(popQuestions.removeFirst());
      if (currentCategory() == "Science")
         System.out.println(scienceQuestions.removeFirst());
      if (currentCategory() == "Sports")
         System.out.println(sportsQuestions.removeFirst());
      if (currentCategory() == "Rock")
         System.out.println(rockQuestions.removeFirst());
   }


   private String currentCategory() {
      if (playerPositions[currentPlayer] - 1 == 0) return "Pop";
      if (playerPositions[currentPlayer] - 1 == 4) return "Pop";
      if (playerPositions[currentPlayer] - 1 == 8) return "Pop";
      if (playerPositions[currentPlayer] - 1 == 1) return "Science";
      if (playerPositions[currentPlayer] - 1 == 5) return "Science";
      if (playerPositions[currentPlayer] - 1 == 9) return "Science";
      if (playerPositions[currentPlayer] - 1 == 2) return "Sports";
      if (playerPositions[currentPlayer] - 1 == 6) return "Sports";
      if (playerPositions[currentPlayer] - 1 == 10) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      if (playersInPenaltyBox[currentPlayer]) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            playerGoldCoins[currentPlayer]++;
            System.out.println(players.get(currentPlayer)
                               + " now has "
                               + playerGoldCoins[currentPlayer]
                               + " Gold Coins.");

            boolean winner = isGameStillGoing();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
         } else {
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;
            return true;
         }


      } else {

         System.out.println("Answer was corrent!!!!");
         playerGoldCoins[currentPlayer]++;
         System.out.println(players.get(currentPlayer)
                            + " now has "
                            + playerGoldCoins[currentPlayer]
                            + " Gold Coins.");

         boolean winner = isGameStillGoing();
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
      playersInPenaltyBox[currentPlayer] = true;

      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }


   private boolean isGameStillGoing() {
      return !(playerGoldCoins[currentPlayer] == WINNING_COINS);
   }
}
