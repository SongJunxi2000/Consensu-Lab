package Protocol;

import Simulator.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public final class Dolev_Strong_Player extends Student_Player {
    HashSet<String> EXTR = new HashSet<String>();
    LinkedList<SignedM> SignedM_in_this_package;
    boolean isSender;

    public Dolev_Strong_Player(int key, int id, Fauth authenticate, Fsign signature, Simulation_engine engine, int num,
            boolean isSender) {
        super(key, id, authenticate, signature, engine, num, isSender);
        this.isSender = isSender;
    }

    public static boolean check_output(int designated_sender, LinkedList<Integer> honest_players_id, int[] outputs,
            String senderBit) {
        Iterator honest_player = honest_players_id.iterator();
        int bit = outputs[(int) honest_players_id.getFirst()];
        if (honest_players_id.contains(designated_sender)) {
            bit = Integer.valueOf(senderBit);
        }
        while (honest_player.hasNext()) {
            if (outputs[(int) honest_player.next()] != bit)
                return false;
        }
        return true;
    }

    int maxRound() {
        return engine.numOfFaultyPlayers + 1;
    }

}
