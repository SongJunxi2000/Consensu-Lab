package Protocol;

import Simulator.Adversary;
import Simulator.Player;

import java.util.Random;

public class Dolev_Strong_Drop_Adversary extends Adversary {
    public Dolev_Strong_Drop_Adversary(int numOfPlayers, int numOfFaultyPlayers, int delay, int maxRound) {
        super(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
    }

    @Override
    public void attack(int roundNumber) {
        Random rand = new Random();
        for (Player player : faulty_players) {
            if (rand.nextFloat() > 0.5)
                player.action(roundNumber);
        }
    }
}
