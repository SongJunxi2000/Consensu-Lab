package Protocol;

import Simulator.Adversary;
import Simulator.Package;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This dummy adversary works as follow:
 * It randomly decides how long the message needs to delay for this round, and
 * if the delay is larger than max delay
 * it will deliver this message, other wise the message is delayed until the
 * next round.
 */
public class Default_Adversary extends Adversary {

    public Default_Adversary(int numOfPlayers, int numOfFaultyPlayers, int delay, int maxRound) {
        super(numOfPlayers, numOfFaultyPlayers, delay, maxRound);
    }

    @Override
    public HashMap<Integer, LinkedList<Package>> sendInThisRound(int roundNumber) {

        Iterator um_iterator = unready_messages.iterator();
        LinkedList<Package> tem = new LinkedList<>();

        while (um_iterator.hasNext()) {
            Package aPackage = (Package) um_iterator.next();
            int delay = (int) (Math.random() * maxDelay);
            if (sendRound.get(aPackage) + delay > roundNumber) {
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
}
