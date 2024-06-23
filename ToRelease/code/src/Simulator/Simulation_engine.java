package Simulator;

import Main.*;
import Protocol.*;
import java.util.*;

public class Simulation_engine {

    public int numOfPlayers, numOfFaultyPlayers, delay;
    private int maxRound;
    public int roundNumber = 0;
    private HashMap<Integer, Player> players;// private key -> player
    private int[] players_key;
    public Fauth auth;
    public Fsign sign;
    private Adversary adv;
    private LinkedList<Player> faulty_players;
    private LinkedList<Integer> faulty_players_id;
    private LinkedList<Integer> honest_players_id;
    private LinkedList<Player> honest_players;
    Random rand = new Random();
    HashSet<Player> this_round;
    HashSet<Player> active_players;
    private int[] players_output;
    private PlayerF playerF;

    private boolean execution_error = false;
    private final String senderbit;

    /** Hardcoded designated sender, it's the same to assign any number */
    static public final int DESIGNATED_SENDER = 0;

    /**
     * Initialize a new Simulation engine which simulates Dolve-Strong protocol.
     * 
     * @param numOfPlayers       Total number of players
     * @param numOfFaultyPlayers Maximum number of faulty players, the actual number
     *                           of faulty players would
     *                           be between 0 and the max value
     * @param delay              Maximum delay of messages
     * @param maxRound           Maximum number of rounds the simulator will
     *                           simulate. For Dolev Strong, it's guaranteed to be
     *                           greater than numOfFaultyPlayers.
     * @param identity           an array of size [numOfPlayers], -1 would indicate
     *                           this player is faulty, 0 means honest
     *                           player of the protocol's implementation, 1 means
     *                           the player of student's implementation
     */
    public Simulation_engine(int numOfPlayers, int numOfFaultyPlayers, int delay, int maxRound, PlayerF p,
            AdversaryF adversaryF, String senderBit, int[] identity) throws Exception {
        this.numOfFaultyPlayers = numOfFaultyPlayers;
        this.numOfPlayers = numOfPlayers;
        this.delay = delay;
        this.maxRound = maxRound;
        this.senderbit = senderBit;
        players = new HashMap<>();
        players_key = new int[numOfPlayers];
        faulty_players = new LinkedList<>();
        faulty_players_id = new LinkedList<>();
        honest_players_id = new LinkedList<>();
        honest_players = new LinkedList<>();
        players_output = new int[numOfPlayers];
        playerF = p;

        adv = initializingAdv(adversaryF);

        sign = new Fsign();
        auth = new Fauth(adv, sign);

        int current_number_of_faulty_players = 0;

        for (int i = 0; i < numOfPlayers; i++) {
            int key = rand.nextInt();
            Player player = null;
            switch (identity[i]) {
                case -1:
                    player = initializing(p, key, i, i == DESIGNATED_SENDER);
                    faulty_players.add(player);
                    faulty_players_id.add(i);
                    current_number_of_faulty_players++;
                    break;
                case 0:
                    player = initializing(p, key, i, i == DESIGNATED_SENDER);
                    honest_players_id.add(i);
                    honest_players.add(player);
                    break;
                case 1:
                    player = initializing(PlayerF.STUDENT, key, i, i == DESIGNATED_SENDER);
                    honest_players_id.add(i);
                    honest_players.add(player);
            }
            if (i == DESIGNATED_SENDER) {
                player.receive_bit_as_designated_sender(senderBit);
            }
            players.put(key, player);
            players_key[i] = key;
        }
        System.out.println("\n" + current_number_of_faulty_players + "/" + numOfPlayers + " players are faulty");
        if (current_number_of_faulty_players != numOfFaultyPlayers)
            throw new Exception("identity array should " +
                    "match with total number of faulty players");
        if (identity.length != numOfPlayers)
            throw new Exception("identity array should match " +
                    "with total number of players");

        System.out.print("Honest Players:");
        for (Integer player : honest_players_id)
            System.out.print(player + " ");
        System.out.println();
        System.out.print("Faulty Players:");
        for (Integer player : faulty_players_id)
            System.out.print(player + " ");
        System.out.println();
        adv.setFaultyPlayers(faulty_players, faulty_players_id);
        auth.setAdKeys(players_key);
        sign.setKeys(players_key);

        active_players = new HashSet<>(honest_players);
        runProtocol();
        check_output();
    }

    /**
     * Initializing a new player base on the player type passed into the simulator
     * 
     * @param p        the type of the player
     * @param key      the private key of the player
     * @param id       the private id of the palyer
     * @param isSender whether the palyer is the designated sender or not
     * @return a newly initialized player obejct
     */
    private Player initializing(PlayerF p, int key, int id, boolean isSender) {
        switch (p) {
            case DEFAULT:
                return new Default_Player(key, id, auth, sign, this, numOfPlayers, isSender);
            case STUDENT:
                return new Student_Player(key, id, auth, sign, this, numOfPlayers, isSender);
            case DS:
                return new Dolev_Strong_Player(key, id, auth, sign, this, numOfPlayers, isSender);
            case DS_FROUND:
                return new Dolev_Strong_FRound_Player(key, id, auth, sign, this, numOfPlayers, isSender);
            case MV:
                return new Majority_Vote_Player(key, id, auth, sign, this, numOfPlayers, isSender);
            default:
                return new Default_Player(key, id, auth, sign, this, numOfPlayers, isSender);
        }
    }

    /**
     * Initializing an adversary based on the type passed into the simulator
     * 
     * @param adversaryF the type of adversary
     * @return an adversary obejct
     */
    private Adversary initializingAdv(AdversaryF adversaryF) {
        Adversary adv;
        switch (adversaryF) {
            case DS_FROUND:
                adv = new Dolev_Strong_FRound_Adversary(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
                break;
            case DS_RANDOM_MESSAGE:
                adv = new Dolev_Strong_RandMsg_Adversary(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
                break;
            case DEFAULT:
                adv = new Default_Adversary(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
                break;
            case MV:
                adv = new Majority_Vote_Adversary(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
                break;
            case DS_DROP:
                adv = new Dolev_Strong_Drop_Adversary(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + adversaryF);
        }
        return adv;
    }

    /**
     * Run the protocol without checking whether the outputs satisfy validity and
     * consistency
     */
    public void runProtocol() {
        try {

            for (int i = 0; i <= maxRound; i++) {
                // System.out.println("round begin: "+ i);
                this_round = (HashSet) active_players.clone();
                roundNumber = i;
                adv.update_round(roundNumber);
                auth.update_round(roundNumber);
                auth.update_receive();
                Iterator<Player> iterable_players = this_round.iterator();
                while (iterable_players.hasNext()) {
                    iterable_players.next().action(roundNumber);
                }
                adv.attack(roundNumber);

            }
            // print for all players' output
            // for (int i : players_output)
            // System.out.print(i + " ");
            // System.out.println("\n");
        } catch (Exception e) {
            execution_error = true;
            e.printStackTrace();
        }
    }

    /**
     * The player should call this function when she tries to output something
     * 
     * @param sender      The id of the player who wants to output
     * @param private_key The private key of the player who wants to output
     * @param output      The actual output
     */
    public void output(int sender, int private_key, int output) {
        if (check_actioner(sender, private_key)) {
            players_output[sender] = output;
        }
    }

    /**
     * Simulator checks whether the public id and private key of the player match
     * 
     * @param sender      The public id of the player
     * @param private_key The private key of the player
     * @return true if the id and key match, false otherwise
     */
    private boolean check_actioner(int sender, int private_key) {
        return players_key[sender] == private_key;
    }

    /**
     * Player should call this function when he wants to terminate and leave
     * 
     * @param sender      The public id og the player
     * @param private_key The private key of the player
     */
    public void terminate(int sender, int private_key) {
        if (check_actioner(sender, private_key)) {
            active_players.remove(players.get(private_key));
        }
    }

    /**
     * Player should call this function when he wants to end this round
     * 
     * @param sender      The public id og the player
     * @param private_key The private key of the player
     */
    public void endRound(int sender, int private_key) {
        if (check_actioner(sender, private_key)) {
            this_round.remove(players.get(private_key));
        }
    }

    /**
     * Check whether the outputs of all players satisfy validity and consistency
     * 
     * @return true if both validity and consistency are satisfied, false otherwise
     */
    public boolean check_output() {
        if (execution_error)
            return false;
        switch (playerF) {
            case DS_FROUND:
            case DS:
                return Dolev_Strong_Player.check_output(DESIGNATED_SENDER, honest_players_id, players_output,
                        senderbit);
            case MV:
                return Majority_Vote_Player.CHECK_OUTPUT(DESIGNATED_SENDER, honest_players_id, players_output);
            case DEFAULT:
                return Default_Player.check_output(DESIGNATED_SENDER, honest_players_id, players_output);
            default:
                return false;
        }
    }

}
