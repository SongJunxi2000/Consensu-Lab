package Simulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Adversary {
    public HashMap<Integer, LinkedList<Package>> ready_messages = new HashMap<>();
    public LinkedList<Package> unready_messages = new LinkedList<Package>();
    public LinkedList<Player> faulty_players = new LinkedList<>();
    public LinkedList<Integer> faulty_players_id = new LinkedList<>();
    public int numOfPlayers = 0;
    public int numOfFaultyPlayers = 0;
    public int maxDelay = 1;
    public int maxRound = 0;
    public Player desig_sender = null;
    protected HashMap<Package, Integer> sendRound = new HashMap<>();
    public int roundNumber = 0;

    public Adversary(int numOfPlayers, int numOfFaultyPlayers, int delay, int maxRound) {
        this.numOfPlayers = numOfPlayers;
        this.numOfFaultyPlayers = numOfFaultyPlayers;
        this.maxDelay = delay;
        this.maxRound = maxRound;
    }

    /**
     * After Simulation engine generates all faulty players, it will pass all faulty
     * players to the adversary.
     * 
     * @param faulty_players_given A linked list of all faulty players
     * @param f_id                 A linked list of all faulty players' ids, the
     *                             sequence of the two linked lists matches, which
     *                             means
     *                             f_id.get(i) is the public id of player
     *                             faulty_players_given.get(i)
     */
    public void setFaultyPlayers(LinkedList<Player> faulty_players_given, LinkedList<Integer> f_id) {
        this.faulty_players_id = f_id;
        this.faulty_players = faulty_players_given;
        numOfFaultyPlayers = f_id.size();
    }

    /**
     * The authenticated channel will call this function when someone tries to
     * receive a message.
     * 
     * @param roundNumber Self explanatory
     * @return A hash map which maps the public id of the player to a linked list of
     *         messages she will
     *         * receive in the given round.
     */
    public HashMap<Integer, LinkedList<Package>> sendInThisRound(int roundNumber) {
        Iterator um_iterator = unready_messages.iterator();
        LinkedList<Package> tem = new LinkedList<>();

        while (um_iterator.hasNext()) {
            Package aPackage = (Package) um_iterator.next();
            if (sendRound.get(aPackage) >= roundNumber) {
                tem.add(aPackage);
            } else {
                LinkedList<Package> list = ready_messages.getOrDefault(aPackage.getReceiver(),
                        new LinkedList<Package>());
                list.add(aPackage);
                ready_messages.put(aPackage.getReceiver(), list);
            }
        }
        unready_messages = (LinkedList<Package>) tem.clone();
        HashMap<Integer, LinkedList<Package>> temp;
        temp = (HashMap<Integer, LinkedList<Package>>) ready_messages.clone();
        ready_messages = new HashMap<Integer, LinkedList<Package>>();
        return temp;
    }

    /**
     * The authenticated channel will call this function when a player sends a
     * message
     * 
     * @param message The message created by authenticated channel
     */
    public void receive(Package message) {
        unready_messages.add(message);
        sendRound.put(message, roundNumber);
    }

    public LinkedList<Package> honestMsgs() {
        return unready_messages;
    }

    /**
     * Adversary's attack
     */
    public void attack(int roundNumber) {

    }

    public void update_round(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
