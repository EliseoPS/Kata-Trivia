package trivia;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerTest {
    @Test
public void playerShouldAdvanceCorrectly() {
    Player player = new Player("Eliseo");
    player.position = 10;
    
    player.advance(5, 12); 
    
    assertEquals(3, player.position);
}

@Test
public void playerShouldGainCoins() {
    Player player = new Player("Eliseo");
    player.addCoin();
    
    assertEquals(1, player.coins);
}
}
