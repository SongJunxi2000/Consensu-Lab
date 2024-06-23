package Protocol;

import Simulator.*;

// TODO: OVERWRITE THIS FILE IN CHECKPOINT 2

public class Dolev_Strong_FRound_Player extends Player {

    public Dolev_Strong_FRound_Player(int key, int id, Fauth authenticate, Fsign signature, Simulation_engine engine,
            int num, boolean isSender) {
        super(key, id, authenticate, signature, engine, num);
    }
}
