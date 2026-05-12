from collections import deque

class Player:
    def __init__(self, name):
        self.name = name
        self.position = 1
        self.coins = 0
        self.in_penalty_box = False
        self.is_getting_out = False

    def add_coin(self):
        self.coins += 1

    def send_to_penalty_box(self):
        self.in_penalty_box = True

    def has_won(self, winning_coins):
        return self.coins == winning_coins

    def advance(self, roll, board_size):
        self.position += roll
        if self.position > board_size:
            self.position -= 12

class QuestionDeck:
    def __init__(self):
        self.pop_questions = deque([f"Pop Question {i}" for i in range(50)])
        self.science_questions = deque([f"Science Question {i}" for i in range(50)])
        self.sports_questions = deque([f"Sports Question {i}" for i in range(50)])
        self.rock_questions = deque([f"Rock Question {i}" for i in range(50)])

    def draw_question(self, category):
        if category == "Pop": return self.pop_questions.popleft()
        if category == "Science": return self.science_questions.popleft()
        if category == "Sports": return self.sports_questions.popleft()
        if category == "Rock": return self.rock_questions.popleft()
        return "Unknown Category"

class Game:
    BOARD_SIZE = 12
    WINNING_COINS = 6
    MAX_PLAYERS = 6

    def __init__(self):
        self.players = []
        self.deck = QuestionDeck()
        self.current_player_idx = 0

    @property
    def current_player(self):
        return self.players[self.current_player_idx]

    def add(self, player_name):
        if len(self.players) == self.MAX_PLAYERS:
            self.announce(f"Cannot add {player_name}. Game is full (Max {self.MAX_PLAYERS} players).")
            return False

        self.players.append(Player(player_name))
        self.announce(f"{player_name} was added")
        self.announce(f"They are player number {len(self.players)}")
        return True

    def roll(self, roll_value):
        player = self.current_player
        self.announce(f"{player.name} is the current player")
        self.announce(f"They have rolled a {roll_value}")

        if player.in_penalty_box:
            self.handle_penalty_box_turn(player, roll_value)
        else:
            self.handle_normal_turn(player, roll_value)

    def handle_normal_turn(self, player, roll_value):
        player.advance(roll_value, self.BOARD_SIZE)
        self.announce(f"{player.name}'s new location is {player.position}")
        self.announce(f"The category is {self.current_category(player.position)}")
        self.ask_question()

    def handle_penalty_box_turn(self, player, roll_value):
        if roll_value % 2 != 0:
            player.is_getting_out = True
            self.announce(f"{player.name} is getting out of the penalty box")
            self.handle_normal_turn(player, roll_value)
        else:
            self.announce(f"{player.name} is not getting out of the penalty box")
            player.is_getting_out = False

    def ask_question(self):
        category = self.current_category(self.current_player.position)
        self.announce(self.deck.draw_question(category))

    def current_category(self, position):
        if position in [1, 5, 9]: return "Pop"
        if position in [2, 6, 10]: return "Science"
        if position in [3, 7, 11]: return "Sports"
        return "Rock"

    def handle_correct_answer(self):
        player = self.current_player
        if player.in_penalty_box:
            return self.handle_penalty_box_branch(player.is_getting_out)
        else:
            self.add_gold_coin("Answer was correct!!!!")
            still_going = not self.did_player_win()
            self.next_player()
            return still_going

    def wrong_answer(self):
        player = self.current_player
        print("Question was incorrectly answered")
        print(f"{player.name} was sent to the penalty box")
        player.send_to_penalty_box()
        self.next_player()
        return True

    def did_player_win(self):
        return self.current_player.has_won(self.WINNING_COINS)

    def handle_penalty_box_branch(self, is_getting_out):
        if is_getting_out:
            self.add_gold_coin("Answer was correct!!!!")
            still_going = not self.did_player_win()
            self.next_player()
            return still_going
        else:
            self.next_player()
            return True

    def next_player(self):
        self.current_player_idx += 1
        if self.current_player_idx == len(self.players):
            self.current_player_idx = 0

    def add_gold_coin(self, message):
        player = self.current_player
        self.announce(message)
        player.add_coin()
        self.announce(f"{player.name} now has {player.coins} Gold Coins.")

    def announce(self, message):
        print(message)