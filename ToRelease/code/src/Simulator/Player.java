package Simulator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;

public class Player {
    private int player_private_key;
    private int player_id;
    public Fauth Fauth_object;
    public Fsign Fsign_object;
    public Simulation_engine engine;
    public int total_num_of_players;

    protected String SENDERBIT;

    Gson gson = new Gson();

    /**
     * Initialize a player.
     * 
     * @param key          The private key of this player
     * @param id           The public id of this player, which could be used to
     *                     receive messages
     * @param authenticate The authenticate channel which the player uses to send
     *                     and receive messages
     * @param signature    The signature model which the player uses to sign and
     *                     verify messages
     * @param engine       The simulation enginereceive
     * @param num          Total number of players
     */
    public Player(int key, int id, Fauth authenticate, Fsign signature, Simulation_engine engine, int num) {
        player_private_key = key;
        player_id = id;
        Fauth_object = authenticate;
        Fsign_object = signature;
        this.engine = engine;
        total_num_of_players = num;
    }

    /**
     * Simulation engine would call this function to distribute the sender bit if
     * you're a designated sender
     * 
     * @param bit the bit the designated sender want to communicate with other
     *            players
     */
    public void receive_bit_as_designated_sender(String bit) {
        SENDERBIT = bit;
    }

    /**
     * Send a message. Player should call this function when she wants to send a
     * message.
     * 
     * @param msg      The arbitrary string the player is sending
     * @param receiver The receiver
     */
    public void send(String msg, int receiver) {
        Fauth_object.send(msg, receiver, player_id, player_private_key);
    }

    /**
     * Receive a message. A player should call this function when he wants to
     * receive messages from authenticated channel
     * 
     * @return A linked list of Message objects
     */
    public LinkedList<Package> receive() {
        return Fauth_object.receive(player_private_key, player_id);
    }

    /**
     * Terminate this round
     */
    public void endRound() {
        engine.endRound(player_id, player_private_key);
    }

    /**
     * Terminate the protocol and leave
     */
    public void terminate() {
        engine.terminate(player_id, player_private_key);
    }

    /**
     * This will call sign in Fsign model and get a signed string which is a json
     * object of SignedM
     * 
     * @param msg The actual message a player wants to sign (ex. "hi Bob, this is
     *            Alice :)")
     * @return a string which is a json object of SignedM, that includes the
     *         message, player's public id, and signature
     *         Note: to prevent the misuse of sign, Player object shouldn't been
     *         passed to adversary, otherwise, a message
     *         could be signed under the name of this player
     */
    public String sign(String msg) {
        String sig = Fsign_object.sign(msg, player_id, player_private_key);
        SignedM signM = new SignedM(msg, player_id, sig);
        return gson.toJson(signM);
    }

    /**
     * Verify a message
     * 
     * @param player_id the id of the player who claims to sign this message, which
     *                  is also the public key here
     * @param msg       the message he sends
     * @param sig       the signature provided by him
     * @return true if the sender indeed signed the message, false otherwise
     */
    public boolean verify(int player_id, String msg, String sig) {
        return Fsign_object.verification(player_id, msg, sig);
    }

    /**
     * Parse a single message from the channel
     * 
     * @param msg_from_channel the message from the channel
     * @return a SignedM object
     */
    public SignedM parseSingleMsg(String msg_from_channel) {
        return gson.fromJson(msg_from_channel, SignedM.class);
    }

    /***
     * Wrapper function for Dolev_Strong, please use it so your implementation could
     * communicate with ours
     * 
     * @param msgs the linked list of SignedM that you want to send
     * @return a string that will be taken by the authenticated channel
     * @throws Exception you should make sure that all your msg signed the same bit,
     *                   otherwise it will throw
     *                   the exception
     */
    public String DS_Serialize(LinkedList<SignedM> msgs) throws Exception {
        if (msgs == null) {
            throw new Exception();
        }
        SignedM first = msgs.getFirst();
        String bit = first.msg;
        Gson gson = new Gson();
        String output = gson.toJson(first);
        for (int i = 1; i < msgs.size(); i++) {
            SignedM SignedMi = msgs.get(i);
            if (SignedMi.msg.equals(bit)) {
                output += "," + gson.toJson(SignedMi);
            } else {
                throw new Exception();
            }
        }
        return output;
    }

    /***
     * Wrapper function we created for you, which would parse the message from the
     * channel and return a list of SignedM
     * objects, all signing the same bit
     * 
     * @param strSMs the message from Package that you want to parse
     * @return a list of SignedM
     * @throws Exception if they signed different bits or the message from Package
     *                   in unable to parse
     */
    public LinkedList<SignedM> DS_Parse(String strSMs) throws Exception {
        Gson gson = new Gson();
        LinkedList<SignedM> list = gson.fromJson('[' + strSMs + ']', new TypeToken<LinkedList<SignedM>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            throw new Exception();
        }
        String bit = list.getFirst().msg;
        for (int i = 0; i < list.size(); i++) {
            if (!bit.equals(list.get(i).msg))
                throw new Exception();
        }
        return list;

    }

    /**
     * Player should call this when he wants to output to simulator
     * 
     * @param output The message the player wants to output
     */
    public void output(int output) {
        engine.output(player_id, player_private_key, output);
        terminate();
    }

    /**
     * The action the player will take in each round. Expected to be overridden.
     */
    public void action(int roundNumber) {
    }

    /**
     * Check whether all players output satisfy the requirements. Expected to be
     * overridden.
     * 
     * @param designated_sender The designated sender
     * @param honest_players_id a linked list of honest players' ids
     * @param outputs           The outputs of all players. outputs[i] is the output
     *                          of player with public id being i
     * @return true if all requirements are satisfied, false otherwise
     */
    static boolean check_output(int designated_sender, LinkedList<Integer> honest_players_id, int[] outputs) {
        return false;
    };

}
