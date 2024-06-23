package Simulator;

public class Package {
    private String strSignedM;
    private int receiver;
    private int sender;

    /**
     * Message class used by authenticated channel, which records the message sends
     * by the player, the actual receiver,
     * actual sender and in which round the sender sends this message.
     * 
     * @param jsonSignedM The string send by the player, if correctly implemented,
     *                    it should be a json string
     *                    of the SignedM class
     * @param receiver    The receiver of the message
     * @param sender      The actual sender call the send function
     */
    public Package(String jsonSignedM, int receiver, int sender) {
        this.strSignedM = jsonSignedM;
        this.receiver = receiver;
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public int getSender() {
        return sender;
    }

    public String getStrSignedM() {
        return strSignedM;
    }

}