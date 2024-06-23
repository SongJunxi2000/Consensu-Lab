package Test;

import org.junit.Test;
import static org.junit.Assert.*;

import Main.*;
import Simulator.Simulation_engine;

public class MVTestCases {
    @Test
    public void test1() {
        Simulation_engine engine = null;
        try {
            engine = new Simulation_engine(31, 1, 0, 2,
                    PlayerF.MV, AdversaryF.MV, "1",
                    new int[] { -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0 });
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(engine.check_output());
    }
}
