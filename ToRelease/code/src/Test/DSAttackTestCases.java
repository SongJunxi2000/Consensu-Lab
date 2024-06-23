package Test;

import org.junit.Test;
import static org.junit.Assert.*;

import Main.AdversaryF;
import Main.PlayerF;
import Simulator.Simulation_engine;

public class DSAttackTestCases {
    @Test
    public void testFM1R() {
        Simulation_engine engine = null;
        try {
            engine = new Simulation_engine(30, 10, 0, 10,
                    PlayerF.DS_FROUND, AdversaryF.DS_FROUND, "1",
                    new int[] { -1, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0 });
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(engine.check_output());
    }

}
