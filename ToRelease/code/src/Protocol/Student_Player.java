package Protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Simulator.*;

import java.util.*;

public class Student_Player extends Player {

    final boolean isSender; // true if this player is the designated sender

    public Student_Player(int key, int id, Fauth authenticate, Fsign signature, Simulation_engine engine, int num,
            boolean isSender) {
        super(key, id, authenticate, signature, engine, num);
        this.isSender = isSender;
    }

    /**
     * This method is called by the simulator every round.
     * Use output() function to output the bit.
     */
    @Override
    public void action(int roundNumber) {
        // TODO: Implement your protocol here
    }

    int maxRound() {
        return engine.numOfFaultyPlayers + 1;
    }

}