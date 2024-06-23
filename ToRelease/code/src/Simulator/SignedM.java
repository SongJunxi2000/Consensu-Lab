package Simulator;

/**
 * This is the class for Gson object used when a player sends a message.
 */
public class SignedM {
    public SignedM(String m, int p, String s) {
        msg = m;
        signer = p;
        sig = s;
    }

    public String msg;
    public int signer;
    public String sig;
}
