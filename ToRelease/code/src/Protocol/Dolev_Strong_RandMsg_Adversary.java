package Protocol;

import Simulator.Adversary;
import Simulator.Player;

public class Dolev_Strong_RandMsg_Adversary extends Adversary {
    public Dolev_Strong_RandMsg_Adversary(int numOfPlayers, int numOfFaultyPlayers, int delay, int maxRound) {
        super(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
    }

    public void attack(int roundNumber) {
        if (faulty_players_id.getFirst() == 0) {
            if (roundNumber == 0) {
                String msg = "";
                for (int i = 0; i < numOfFaultyPlayers; i++) {
                    msg = msg + "," + faulty_players.get(i).sign("2");
                }
                msg = msg.substring(1);

                Player sender = faulty_players.getFirst();
                for (int i = 1; i < numOfPlayers; i++) {
                    sender.send(msg, i);
                }
            }

        }

    }
}
