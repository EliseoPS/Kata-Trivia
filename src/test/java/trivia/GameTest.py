import unittest
import random
import io
from contextlib import redirect_stdout

# Asumiendo que Game y GameOld están en el mismo archivo o importados
# from game import Game, GameOld, IGame

class GameTest(unittest.TestCase):

    def test_characterization(self):
        """Ejecuta 10,000 partidas 'aleatorias' para ver si la salida coincide."""
        for seed in range(1, 10001):
            self.run_test_seed(seed, False)

    def run_test_seed(self, seed: int, print_expected: bool):
        # Usamos GameOld como el "Oráculo" (el que sabemos que funciona 'bien' aunque sea feo)
        expected_output = self.extract_output(random.Random(seed), GameOld())
        
        if print_expected:
            print(expected_output)

        # Usamos Game como la versión que vamos a refactorizar
        actual_output = self.extract_output(random.Random(seed), Game())

        # Si algo cambia en tu refactorización, este assert fallará
        self.assertEqual(
            expected_output, 
            actual_output, 
            f"Cambio detectado en la semilla {seed}. Para debuguear, ejecuta solo esta semilla."
        )

    def test_one_seed(self):
        """Equivalente al @Ignore: sirve para probar una sola semilla y ver qué sale."""
        # Cambia el 1 por la semilla que esté fallando
        self.run_test_seed(1, True)

    def extract_output(self, rand: random.Random, a_game) -> str:
        """Captura todo lo que el juego imprime en consola y lo devuelve como String."""
        f = io.StringIO()
        with redirect_stdout(f):
            a_game.add("Chet")
            a_game.add("Pat")
            a_game.add("Sue")

            not_a_winner = True
            while not_a_winner:
                # Java: nextInt(5) + 1 -> 1 a 5
                # Python: randint(1, 5) -> 1 a 5
                a_game.roll(rand.randint(1, 5))

                # Simulación de respuesta correcta/incorrecta (1 de cada 9 es incorrecta)
                if rand.randint(0, 8) == 7:
                    not_a_winner = a_game.wrong_answer()
                else:
                    not_a_winner = a_game.handle_correct_answer()
        
        return f.getvalue()

if __name__ == '__main__':
    unittest.main()