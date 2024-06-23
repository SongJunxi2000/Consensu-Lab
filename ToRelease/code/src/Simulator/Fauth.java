package Simulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Fauth {
    HashMap<Integer, LinkedList<Package>> ready_messages;// key is the receiver id
    Fsign sign;
    Adversary adv;
    private int[] players_key;
    int roundN; // current round number

    /**
     * Construct and initialize an authentication channel with the adversary and
     * Fsign objects
     * 
     * @param adversary an adversary object
     * @param signature a fauth object
     * @return a newly constructed Fauth object
     */
    public Fauth(Adversary adversary, Fsign signature) {
        sign = signature;
        adv = adversary;

    }

    /**
     * initialize players' keys
     * 
     * @param keys keys initialized by players
     */
    public void setAdKeys(int[] keys) {
        players_key = keys;
    }

    /**
     * If the private key matches the sender's key, Fauth wrap the msg with the
     * sender, receiver and
     * round information and sent it to the adversary.
     * 
     * @param msg         self-explanatory
     * @param receiver    self-explanatory
     * @param sender      self-explanatory
     * @param private_key self-explanatory
     */
    public void send(String msg, int receiver, int sender, int private_key) {
        if (players_key[sender] == private_key) {
            Package wrapper = new Package(msg, receiver, sender);
            adv.receive(wrapper);
            // if(sender == 0){
            // System.out.println(msg+sender);
            // }
        }
    }

    /**
     * Update the current round number of Fauth, receive messages from the adversary
     * and add them to the ready message
     */
    public void update_receive() {
        HashMap<Integer, LinkedList<Package>> toAdd = adv.sendInThisRound(roundN);
        if (ready_messages == null)
            ready_messages = toAdd;
        else
            for (Map.Entry<Integer, LinkedList<Package>> entry : toAdd.entrySet()) {
                int player = entry.getKey();
                LinkedList<Package> messages = entry.getValue();
                LinkedList<Package> temp = ready_messages.getOrDefault(player, new LinkedList<>());
                temp.addAll((messages));
                ready_messages.put(player, temp);
            }
    }

    /**
     * update the ready message; if the player's key matches, return the ready
     * messages for player id.
     * 
     * @param key the player's private key
     * @param id  the play's id
     * @return a list of messages that is available for the player id in the current
     *         round.
     */
    public LinkedList<Package> receive(int key, int id) {
        if (players_key[id] != key)
            return null;
        LinkedList<Package> result = ready_messages.getOrDefault(id, new LinkedList<>());
        ready_messages.remove(id);
        return result;
    }

    public void update_round(int roundNumber) {
        roundN = roundNumber;
    }

}
