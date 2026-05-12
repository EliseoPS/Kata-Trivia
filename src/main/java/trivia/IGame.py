from abc import ABC, abstractmethod
from IGame import IGame

class IGame(ABC):

    @abstractmethod
    def add(self, player_name: str) -> bool:
        """Agrega un jugador al juego."""
        pass

    @abstractmethod
    def roll(self, roll: int) -> None:
        """Simula el lanzamiento de un dado."""
        pass

    @abstractmethod
    def handle_correct_answer(self) -> bool:
        """Maneja la lógica cuando un jugador responde correctamente."""
        pass

    @abstractmethod
    def wrong_answer(self) -> bool:
        """Maneja la lógica cuando un jugador responde incorrectamente."""
        pass