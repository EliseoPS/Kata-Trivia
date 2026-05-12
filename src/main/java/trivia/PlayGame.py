import random
import sys
import re
from GameOld import GameOld


# Asumiendo que tus clases están en archivos llamados game.py
# Si están en el mismo archivo, no necesitas los imports.
# from game import GameOld 

def read_yes_no():
    while True:
        yn = input(">> Was the answer correct? [y/n] ").strip().upper()
        if re.match(r"[YN]", yn):
            return yn == "Y"
        print("y or n please", file=sys.stderr)

def read_roll():
    while True:
        prompt = ">> Throw a die and input roll, or [ENTER] to generate a random roll: "
        roll_str = input(prompt).strip()
        
        if not roll_str:
            roll = random.randint(1, 6)
            print(f">> Random roll: {roll}")
            return roll
        
        if not roll_str.isdigit():
            print(f"Not a number: '{roll_str}'", file=sys.stderr)
            continue
            
        roll = int(roll_str)
        if 1 <= roll <= 6:
            return roll
        
        print("Invalid roll", file=sys.stderr)

def main():
    print("*** Welcome to Trivia Game ***\n")
    
    try:
        player_count_str = input("Enter number of players: 1-4\n")
        player_count = int(player_count_str)
        if player_count < 1 or player_count > 4:
            raise ValueError()
    except ValueError:
        print("No player 1..4", file=sys.stderr)
        return

    print(f"Reading names for {player_count} players:")

    # Aquí usamos GameOld para entender el comportamiento original
    from Game import GameOld 
    a_game = GameOld()

    for i in range(1, player_count + 1):
        player_name = input(f"Player {i} name: ")
        a_game.add(player_name)

    print("\n\n--Starting game--")

    not_a_winner = True
    while not_a_winner:
        roll = read_roll()
        a_game.roll(roll)

        correct = read_yes_no()
        if correct:
            not_a_winner = a_game.handle_correct_answer()
        else:
            not_a_winner = a_game.wrong_answer()

    print(">> Game won!")

if __name__ == "__main__":
    main()