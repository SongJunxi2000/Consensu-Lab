package Protocol;

import Simulator.*;

import com.google.gson.Gson;

import Simulator.Package;
import java.util.LinkedList;
import java.util.Random;

/**
 * This is a dummy protocol which works as follow: there is one designated
 * sender, honest players and dishonest players.
 * Every honest player sends a random bit, as his vote, to the designated sender
 * (player 0 by default) and
 * terminate in round 0.
 * If the designated sender is honest, she will count how many 1's she received
 * and how many 0's she received in the
 * first three round. (If a player sends multiple messages to the designated
 * sender, she will take the first one. )
 * Then she outputs # of 1's minus # of 0's. The protocol checks whether the
 * designated sender's output is correct.
 * If the designated sender is dishonest, the protocol doesn't do any check but
 * simply outputs "true".
 */
public class Default_Player extends Player {

    boolean isSender;
    boolean[] votedP;
    int count = 0;

    /**
     * Initialize a player.
     *
     * @param key          The private key of this player
     * @param id           The public id of this player
     * @param authenticate The authenticate channel which the player uses to send
     *                     and receive messages
     * @param signature    The signature model which the player uses to sign and
     *                     verify messages
     * @param engine       The simulation engine
     * @param num          Total number of players
     */
    public Default_Player(int key, int id, Fauth authenticate, Fsign signature, Simulation_engine engine, int num,
            boolean isSender) {
        super(key, id, authenticate, signature, engine, num);
        this.isSender = isSender;
        votedP = new boolean[total_num_of_players];
    }

    @Override
    public void action(int roundNumber) {
        /**
         * If this player is not the designated sender.
         */
        if (!isSender) {
            Random rand = new Random();
            int rand_number = rand.nextInt(1000);
            String m;
            if (rand_number < 600) {
                m = "1";
            } else
                m = "0";
            send(sign(m), engine.DESIGNATED_SENDER);
            output(Integer.parseInt(m));

        }
        /**
         * If this player is the designated sender
         */
        else {

            /**
             * In the first three rounds, count 0's and 1's the designate sender received,
             * and ignore the message
             * if she receives multiple votes from the same player, simply ignore. [votedP]
             * keep track of voted person
             */
            if (roundNumber != 0 && roundNumber <= 3) {
                LinkedList<Package> received = receive();
                for (int i = 0; i < received.size(); i++) {
                    Package aPackage = received.get(i);
                    SignedM signM = parseSingleMsg(aPackage.getStrSignedM());
                    if (!votedP[aPackage.getSender()]
                            && verify(signM.signer, signM.msg, signM.sig)) {
                        votedP[aPackage.getSender()] = true;
                        int vote = Integer.parseInt(signM.msg);
                        if (vote == 0)
                            count--;
                        else if (vote == 1)
                            count++;
                    }
                }
            }
            if (roundNumber == 3) {
                output(count);

            }
        }
    }

    public static boolean check_output(int designated_sender, LinkedList<Integer> honest_players_id, int[] outputs) {
        if (!honest_players_id.contains(designated_sender))
            return true;
        int count = 0;
        for (int i = 0; i < honest_players_id.size(); i++) {
            if (outputs[honest_players_id.get(i)] == 1) {
                count++;
            } else if (outputs[honest_players_id.get(i)] == 0)
                count--;
        }
        return outputs[designated_sender] == count;
    };
}
