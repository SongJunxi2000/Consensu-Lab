package Protocol;

import Simulator.*;
import Simulator.Package;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Majority_Vote_Player extends Player {

    boolean isSender;
    int count1 = 0;
    int count0 = 0;
    boolean[] voted1;
    boolean[] voted0;
    boolean vote0;
    boolean vote1;

    /**
     * Initialize a player.
     *
     * @param key          The private key of this player
     * @param id           The public id of this player
     * @param authenticate The authenticate channel which the player uses to send
     *                     and messages
     * @param signature    The signature model which the player uses to sign and
     *                     verify messages
     * @param engine       The simulation enginereceive
     * @param num          Total number of players
     */
    public Majority_Vote_Player(int key, int id, Fauth authenticate, Fsign signature, Simulation_engine engine,
            int num, boolean isSender) {
        super(key, id, authenticate, signature, engine, num);
        this.isSender = isSender;
        voted1 = new boolean[total_num_of_players];
        voted0 = new boolean[total_num_of_players];
        vote0 = vote1 = false;
    }

    public int receive_input() {
        if (isSender && engine.roundNumber == 0)
            return Integer.parseInt(SENDERBIT);
        return -1;
    }

    /**
     * This method sends the bit to everyone if this player is the sender and is in
     * round 0;
     */
    private void SenderActionRound0() {
        String msg = sign(Integer.toString(receive_input()));
        // System.out.println(msg+" message sent by the players ");
        for (int i = 0; i < total_num_of_players; i++) {
            send(msg, i);
        }
    }

    /**
     * This method decides which bit to vote by finding the bit sent by designated
     * sender.
     *
     * @return the bit to vote, -1 if it shouldn't vote for any bit
     */
    private int bitToVote() {
        LinkedList<Package> received = receive();
        for (int i = 0; i < received.size(); i++) {
            Package aPackage = received.get(i);
            SignedM aSignedM = parseSingleMsg(aPackage.getStrSignedM());
            if (aPackage.getSender() == engine.DESIGNATED_SENDER &&
                    verify(aSignedM.signer, aSignedM.msg, aSignedM.sig)) {
                return Integer.parseInt(aSignedM.msg);
            }
        }
        return -1;
    }

    /**
     * It does the voting action for the first round: send the bit with signature to
     * all players.
     */
    private void votingRound1() {
        if (vote1 && !vote0) {
            String msg = sign(Integer.toString(1));
            for (int j = 0; j < total_num_of_players; j++) {
                send(msg, j);
            }
            return;
        }
        String msg = sign(Integer.toString(0));
        for (int j = 0; j < total_num_of_players; j++) {
            send(msg, j);
        }
    }

    /**
     * This method counts the voting for both bit, and should set [count0] and
     * [count1] to the number of people
     * voted for 0 and 1 after the function call ends.
     */
    private void countingVote() {
        LinkedList<Package> received = receive();
        for (int i = 0; i < received.size(); i++) {
            Package aPackage = received.get(i);
            SignedM signM = parseSingleMsg(aPackage.getStrSignedM());
            int vote = Integer.parseInt(signM.msg);
            if (vote == 0) {
                if (!voted0[aPackage.getSender()] && verify(signM.signer, signM.msg, signM.sig)) {
                    voted0[aPackage.getSender()] = true;
                    count0++;
                }
            } else if (vote == 1) {
                if (!voted1[aPackage.getSender()] && verify(signM.signer, signM.msg, signM.sig)) {
                    voted1[aPackage.getSender()] = true;
                    count1++;
                }
            }
            // if vote is not 0 or 1 then do nothing
        }
    }

    /**
     * outputs the bit based on [count0] and [count1]
     */
    private void finalOutput() {
        if (isSender) {
            output(Integer.parseInt(SENDERBIT));
            return;
        }
        if (count1 * 2 >= total_num_of_players && count0 * 2 < total_num_of_players) {
            output(1);

            return;
        }
        output(0);

    }

    public void action(int roundNumber) {

        if (roundNumber == 0 && isSender) {
            SenderActionRound0();
        }
        if (roundNumber == 1) {
            int bit = bitToVote();
            if (bit == 0) {
                vote0 = true;
            }
            if (bit == 1) {
                vote1 = true;
            }
            votingRound1();
        }

        if (roundNumber == 2) {
            countingVote();
            finalOutput();
        }
    }

    /**
     * Check the output of all players
     *
     * @param designated_sender id of the designated sender
     * @param honest_players_id a linked list of all honest players' ids
     * @param outputs           an array of all palyers' outputs
     * @return true if the protocol holds, false if the protocol is broken
     */
    public static boolean CHECK_OUTPUT(int designated_sender, LinkedList<Integer> honest_players_id, int[] outputs) {
        boolean result = true;
        Iterator honest_player = honest_players_id.iterator();
        int bit = outputs[(int) honest_players_id.getFirst()];
        if (honest_players_id.contains(designated_sender)) {
            bit = outputs[designated_sender];
        }
        while (honest_player.hasNext()) {
            if (outputs[(int) honest_player.next()] != bit)
                result = false;
        }
        return result;
    }
}
